package service;

import io.github.cwireset.tcc.domain.*;
import io.github.cwireset.tcc.exception.*;
import io.github.cwireset.tcc.repository.*;
import io.github.cwireset.tcc.request.*;
import io.github.cwireset.tcc.response.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

@Service
public class ReservaService {

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private AnuncioService anuncioService;

    public InformacaoReservaResponse realizarReserva(CadastrarReservaRequest cadastrarReservaRequest) throws Exception {

        Periodo periodoFixo = this.sobrescreveHoras(cadastrarReservaRequest.getPeriodo());

        Usuario usuarioReserva = usuarioService.buscarUsuarioPorId(cadastrarReservaRequest.getIdSolicitante());

        Anuncio anuncioReserva = anuncioService.buscarAnuncioPorId(cadastrarReservaRequest.getIdAnuncio());

        if(this.verificaConflitoReserva(anuncioReserva, periodoFixo)) {
            throw new ConditiondInvalidException("Este anuncio já esta reservado para o período informado.");
        }

        long totalDiarias = this.calculoDiarias(cadastrarReservaRequest.getPeriodo());

        this.verificarTipoImovel(
                anuncioReserva.getImovel().getTipoImovel(),
                cadastrarReservaRequest.getQuantidadePessoas(),
                totalDiarias
        );

        this.verificaDiariaMinima(totalDiarias, 0, "Período inválido! A data final da reserva precisa ser maior do que a data inicial.");
        this.verificaDiariaMinima(totalDiarias, 1, "Período inválido! O número mínimo de diárias precisa ser maior ou igual à 1.");

        BigDecimal valorTotalDiarias = anuncioReserva.getValorDiaria().multiply(BigDecimal.valueOf(totalDiarias));

        Pagamento pagamentoPadrao = new Pagamento(valorTotalDiarias, null, StatusPagamento.PENDENTE);

        if(usuarioReserva.getId().equals(anuncioReserva.getAnunciante().getId())){
            throw new ConditiondInvalidException("O solicitante de uma reserva não pode ser o próprio anunciante.");
        }

        Reserva reserva = this.criaNovaReserva(
                usuarioReserva,
                anuncioReserva,
                periodoFixo,
                cadastrarReservaRequest.getQuantidadePessoas(),
                LocalDateTime.now(),
                pagamentoPadrao);

        reservaRepository.save(reserva);

        DadosSolicitanteResponse dadosSolicitanteResponse = new DadosSolicitanteResponse(
                usuarioReserva.getId(), usuarioReserva.getNome()
        );

        DadosAnuncioResponse dadosAnuncioResponse = new DadosAnuncioResponse(
                anuncioReserva.getId(),
                anuncioReserva.getImovel(),
                anuncioReserva.getAnunciante(),
                anuncioReserva.getFormasAceitas(),
                anuncioReserva.getDescricao()
        );

        return this.montaReservaResponse(
                dadosSolicitanteResponse,
                dadosAnuncioResponse,
                cadastrarReservaRequest.getQuantidadePessoas(),
                reserva.getId(),
                reserva.getPagamento(),
                reserva.getPeriodo()
        );
    }

    public Page<Reserva> listarReservaDeUmSolicitanteEspecifico(
            Long idSolicitante,
            String dataHoraInicial,
            String dataHoraFinal,
            Pageable pageable) throws Exception {

        buscarReservaPorId(idSolicitante);

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            LocalDateTime localDateTimeInicial = LocalDateTime.parse(dataHoraInicial, formatter);
            LocalDateTime localDateTimeFinal = LocalDateTime.parse(dataHoraFinal, formatter);

            return reservaRepository.findAllBySolicitanteAndPeriodoDataHoraInicialGreaterThanEqualAndPeriodoDataHoraFinalLessThanEqual(
                    usuarioService.buscarUsuarioPorId(idSolicitante),
                    localDateTimeInicial,
                    localDateTimeFinal,
                    pageable);

        }catch (Exception e) {

            return reservaRepository.findAllBySolicitante(usuarioService.buscarUsuarioPorId(idSolicitante), pageable);

        }
    }

    public Page<Reserva> listarReservaDeUmAnuncianteEspecifico(Long idAnunciante, Pageable pageable) throws Exception {
        return reservaRepository.findAllByAnuncioAnunciante(usuarioService.buscarUsuarioPorId(idAnunciante), pageable);
    }

    public void pagarReserva(Long idReserva, FormaPagamento formaPagamento) throws Exception {

        Reserva reservaTemp = buscarReservaPorId(idReserva);

        if(reservaTemp.getPagamento().getStatus().equals(StatusPagamento.PENDENTE)){

            if(reservaTemp.getAnuncio().getFormasAceitas().contains(formaPagamento)){

                reservaTemp.getPagamento().setStatus(StatusPagamento.PAGO);
                reservaRepository.save(reservaTemp);

            }else throw new ConditiondInvalidException(String.format("O anúncio não aceita %s como forma de pagamento. As formas aceitas são %s.", formaPagamento, reservaTemp.getAnuncio().getFormasAceitas()));

        }else throw new ConditiondInvalidException(String.format("Não é possível realizar o %s para esta reserva, pois ela não está no status PENDENTE.", "pagamento"));

    }

    public void cancelarReserva(Long idReserva) throws Exception {

        Reserva reservaTemp = buscarReservaPorId(idReserva);

        if(reservaTemp.getPagamento().getStatus().equals(StatusPagamento.PENDENTE)){

            reservaTemp.getPagamento().setStatus(StatusPagamento.CANCELADO);
            reservaRepository.save(reservaTemp);

        }else throw new ConditiondInvalidException(String.format("Não é possível realizar o %s para esta reserva, pois ela não está no status PENDENTE.", "cancelamento"));
    }

    public void estornarReserva(Long idReserva) throws Exception {

        Reserva reservaTemp = buscarReservaPorId(idReserva);

        if(reservaTemp.getPagamento().getStatus().equals(StatusPagamento.PAGO)){

            reservaTemp.getPagamento().setStatus(StatusPagamento.ESTORNADO);
            reservaRepository.save(reservaTemp);

        }else {
            throw new ConditiondInvalidException(String.format("Não é possível realizar o %s para esta reserva, pois ela não está no status PAGO.", "estorno"));
        }

    }

    private InformacaoReservaResponse montaReservaResponse(
            DadosSolicitanteResponse dadosSolicitanteResponse,
            DadosAnuncioResponse dadosAnuncioResponse,
            Integer quantidadePessoas,
            Long reservaId,
            Pagamento pagamento,
            Periodo periodo
    ) {
        return new InformacaoReservaResponse(
                reservaId,
                dadosSolicitanteResponse,
                quantidadePessoas,
                dadosAnuncioResponse,
                periodo,
                pagamento
        );
    }

    private Reserva criaNovaReserva(Usuario solicitante, Anuncio anuncio, Periodo periodo, Integer pessoas, LocalDateTime horaReserva, Pagamento pagamento)
    {
        return new Reserva(null, solicitante, anuncio, periodo, pessoas, horaReserva, pagamento);
    }

    private Long calculoDiarias(@NotNull Periodo periodo) throws Exception {
        return periodo.getDataHoraInicial().toLocalDate().until(periodo.getDataHoraFinal().toLocalDate(), ChronoUnit.DAYS);
    }

    private void verificaDiariaMinima(long totalDias, int minimoDiarias, String mensagemErro) throws Exception {
        if(totalDias < minimoDiarias) {
            throw new ConditiondInvalidException(mensagemErro);
        }
    }

    private Periodo sobrescreveHoras(@NotNull Periodo periodo){

        return new Periodo(
                fixarTempo(periodo.getDataHoraInicial().toLocalDate(), 14),
                fixarTempo(periodo.getDataHoraFinal().toLocalDate(), 12)
        );

    }

    private LocalDateTime fixarTempo(LocalDate data, int hora)
    {
        return data.atTime(LocalTime.of(hora, 0, 0));
    }

    private boolean verificaConflitoReserva(Anuncio anuncio, Periodo periodo) {

        return reservaRepository.existsBy() && ((
                reservaRepository.existsByAnuncioAndPeriodoDataHoraInicialLessThanEqualAndPeriodoDataHoraFinalGreaterThanEqual(
                        anuncio, periodo.getDataHoraInicial(), periodo.getDataHoraInicial())) ||
                (reservaRepository.existsByAnuncioAndPeriodoDataHoraInicialLessThanEqualAndPeriodoDataHoraFinalGreaterThanEqual(
                        anuncio, periodo.getDataHoraFinal(), periodo.getDataHoraFinal())) ||
                (reservaRepository.existsByAnuncioAndPeriodoDataHoraInicialGreaterThanEqualAndPeriodoDataHoraFinalLessThanEqual(
                        anuncio, periodo.getDataHoraInicial(), periodo.getDataHoraFinal())));
    }

    private void verificarTipoImovel(TipoImovel tipo, Integer quantidadePessoas, Long diarias) throws Exception {

        if(tipo.equals(TipoImovel.HOTEL) && quantidadePessoas < 2){
            throw new NumberLimitException( 2, "pessoas", "Hotel");
        }

        if(tipo.equals(TipoImovel.POUSADA) && diarias < 5){
            throw new NumberLimitException( 5, "diárias", "Pousada");
        }
    }

    private Reserva buscarReservaPorId(Long id) throws IdInvalidException {

        if(!reservaRepository.existsById(id)){
            throw new IdInvalidException("Reserva", id);
        }
        return reservaRepository.findById(id);
    }

}
