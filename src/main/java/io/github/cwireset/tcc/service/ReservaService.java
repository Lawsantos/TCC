package io.github.cwireset.tcc.service;

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
        InformacaoReservaResponse reservaResponse = new InformacaoReservaResponse();

        Usuario usuarioReserva = usuarioService.buscarUsuarioPorId(cadastrarReservaRequest.getIdSolicitante());
        Anuncio anuncioReserva = anuncioService.buscarAnuncioPorId(cadastrarReservaRequest.getIdAnuncio());
        BigDecimal total = anuncioReserva.getValorDiaria().multiply(BigDecimal.valueOf(calculoDiarias(cadastrarReservaRequest.getPeriodo())));

        if(usuarioReserva.getId().equals(anuncioReserva.getAnunciante().getId())){
            throw new ConditiondInvalidException("O solicitante de uma reserva não pode ser o próprio anunciante.");
        }

        reservaResponse.setSolicitante(
            new DadosSolicitanteResponse(
                usuarioReserva.getId(), usuarioReserva.getNome()));
        reservaResponse.setQuantidadePessoas(cadastrarReservaRequest.getQuantidadePessoas());
        verificarTipoImovel(anuncioReserva.getImovel().getTipoImovel(), cadastrarReservaRequest.getQuantidadePessoas(), calculoDiarias(cadastrarReservaRequest.getPeriodo()));
        reservaResponse.setAnuncio(
            new DadosAnuncioResponse(
                anuncioReserva.getId(),
                anuncioReserva.getImovel(),
                anuncioReserva.getAnunciante(),
                anuncioReserva.getFormasAceitas(),
                anuncioReserva.getDescricao()));
        reservaResponse.setPeriodo(horasSobrescritas(cadastrarReservaRequest.getPeriodo()));
        verificarReserva(anuncioReserva, cadastrarReservaRequest.getPeriodo().getDataHoraInicial(), cadastrarReservaRequest.getPeriodo().getDataHoraFinal());
        reservaResponse.setPagamento(
                new Pagamento(total,
                        null,
                        StatusPagamento.PENDENTE));

        Reserva reserva = new Reserva();
        reserva.setSolicitante(usuarioReserva);
        reserva.setAnuncio(anuncioReserva);
        reserva.setPeriodo(reservaResponse.getPeriodo());
        reserva.setQuantidadePessoas(reservaResponse.getQuantidadePessoas());
        reserva.setDataHoraReserva(LocalDateTime.now());
        reserva.setPagamento(reservaResponse.getPagamento());

        reservaRepository.save(reserva);
        reservaResponse.setIdReserva(reserva.getId());
        return reservaResponse;
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

        }else throw new ConditiondInvalidException(String.format("Não é possível realizar o %s para esta reserva, pois ela não está no status PAGO.", "estorno"));
    }

    public Long calculoDiarias(@NotNull Periodo periodo) throws Exception {

        Long numeroDiarias = periodo.getDataHoraInicial().toLocalDate().until(periodo.getDataHoraFinal().toLocalDate(), ChronoUnit.DAYS);

        if(numeroDiarias < 0){
            throw new ConditiondInvalidException("Período inválido! A data final da reserva precisa ser maior do que a data inicial.");
        }

        if(numeroDiarias < 1){
            throw new ConditiondInvalidException("Período inválido! O número mínimo de diárias precisa ser maior ou igual à 1.");
        }

        return numeroDiarias;
    }

    public Periodo horasSobrescritas(@NotNull Periodo periodo){

        LocalDate dataInicial = periodo.getDataHoraInicial().toLocalDate();
        LocalDate dataFinal = periodo.getDataHoraFinal().toLocalDate();

        LocalTime horaReservaInicial = LocalTime.of(14, 00, 00);
        LocalTime horaReservaFinal = LocalTime.of(12, 00, 00);

        LocalDateTime periodoTempIni = dataInicial.atTime(horaReservaInicial);
        LocalDateTime periodoTempFim = dataFinal.atTime(horaReservaFinal);

        Periodo periodoSobrescrito = new Periodo(periodoTempIni, periodoTempFim);

        return (periodoSobrescrito);
    }

    public boolean verificarReserva(Anuncio anuncio, LocalDateTime dataIni, LocalDateTime dataFinal) throws Exception {

        Periodo periodoTemp = new Periodo(dataIni, dataFinal);
        periodoTemp = horasSobrescritas(periodoTemp);

        if(reservaRepository.existsBy()) {

            if ((reservaRepository.existsByAnuncioAndPeriodoDataHoraInicialLessThanEqualAndPeriodoDataHoraFinalGreaterThanEqual(anuncio, periodoTemp.getDataHoraInicial(), periodoTemp.getDataHoraInicial())) ||
                (reservaRepository.existsByAnuncioAndPeriodoDataHoraInicialLessThanEqualAndPeriodoDataHoraFinalGreaterThanEqual(anuncio, periodoTemp.getDataHoraFinal(), periodoTemp.getDataHoraFinal())) ||
                (reservaRepository.existsByAnuncioAndPeriodoDataHoraInicialGreaterThanEqualAndPeriodoDataHoraFinalLessThanEqual(anuncio, periodoTemp.getDataHoraInicial(), periodoTemp.getDataHoraFinal()))) {

                throw new ConditiondInvalidException("Este anuncio já esta reservado para o período informado.");
            }
            return false;
        }
        return false;
    }

    public void verificarTipoImovel(TipoImovel tipo, Integer quantidadePessoas, Long diarias) throws Exception {

        if(tipo.equals(TipoImovel.HOTEL)){
            if(quantidadePessoas < 2){
                throw new NumberLimitException( 2, "pessoas", "Hotel");
            }
        }

        if(tipo.equals(TipoImovel.POUSADA)){
            if(diarias < 5){
                throw new NumberLimitException( 5, "diárias", "Pousada");
            }
        }
    }

    public Reserva buscarReservaPorId(Long id) throws IdInvalidException {

        if(!reservaRepository.existsById(id)){
            throw new IdInvalidException("Reserva", id);
        }
        return reservaRepository.findById(id);
    }

}
