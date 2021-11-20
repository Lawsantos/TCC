package io.github.cwireset.tcc.service;

import io.github.cwireset.tcc.domain.*;
import io.github.cwireset.tcc.exception.*;
import io.github.cwireset.tcc.repository.*;
import io.github.cwireset.tcc.request.*;
import io.github.cwireset.tcc.response.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

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

        reservaResponse.getIdReserva();
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
        verificarReserva(cadastrarReservaRequest.getPeriodo().getDataHoraInicial(), cadastrarReservaRequest.getPeriodo().getDataHoraFinal());
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
        return reservaResponse;
    }

    public List<Reserva> listarReservaDeUmSolicitanteEspecifico(Long idSolicitante) throws Exception {
        return reservaRepository.findAllBySolicitante(usuarioService.buscarUsuarioPorId(idSolicitante));
    }

    public List<Reserva> listarReservaDeUmAnuncianteEspecifico(Long idAnunciante) throws Exception {
        return reservaRepository.findAllByAnuncioAnunciante(usuarioService.buscarUsuarioPorId(idAnunciante));
    }

    public void pagarReserva(Long idReserva, FormaPagamento formaPagamento) throws Exception {

        Optional<Reserva> reservaTemp = buscarReservaPorId(idReserva);

        if(reservaTemp.get().getPagamento().getStatus().equals(StatusPagamento.PENDENTE)){

            if(reservaTemp.get().getAnuncio().getFormasAceitas().contains(formaPagamento)){

                reservaTemp.get().getPagamento().setStatus(StatusPagamento.PAGO);
                //return reservaRepository.save(reservaTemp);

            }else throw new ConditiondInvalidException(String.format("O anúncio não aceita %s como forma de pagamento. As formas aceitas são %s.", formaPagamento, reservaTemp.get().getAnuncio().getFormasAceitas()));

        }else throw new ConditiondInvalidException(String.format("Não é possível realizar o %s para esta reserva, pois ela não está no status PENDENTE.", "pagamento"));

    }

    public void cancelarReserva(Long idReserva) throws Exception {

        Optional<Reserva> reservaTemp = buscarReservaPorId(idReserva);

        if(reservaTemp.get().getPagamento().getStatus().equals(StatusPagamento.PENDENTE)){

            reservaTemp.get().getPagamento().setStatus(StatusPagamento.CANCELADO);
            //reservaRepository.save(reservaTemp);

        }else throw new ConditiondInvalidException(String.format("Não é possível realizar o %s para esta reserva, pois ela não está no status PENDENTE.", "cancelamento"));
    }

    public void estornarReserva(Long idReserva) throws Exception {

        Optional<Reserva> reservaTemp = buscarReservaPorId(idReserva);

        if(reservaTemp.get().getPagamento().getStatus().equals(StatusPagamento.PAGO)){

            reservaTemp.get().getPagamento().setStatus(StatusPagamento.ESTORNADO);
            //reservaRepository.save(reservaTemp);

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

    public void verificarReserva(LocalDateTime dataIni, LocalDateTime dataFinal) throws Exception {

        if(reservaRepository.existsBy()) {
            if (((reservaRepository.findByPeriodoDataHoraInicialLessThanEqual(dataIni)) && (reservaRepository.findByPeriodoDataHoraFinalGreaterThanEqual(dataIni))) ||
                ((reservaRepository.findByPeriodoDataHoraInicialLessThanEqual(dataFinal)) && (reservaRepository.findByPeriodoDataHoraFinalGreaterThanEqual(dataFinal))) ||
                ((reservaRepository.findByPeriodoDataHoraInicialGreaterThanEqual(dataIni)) && reservaRepository.findByPeriodoDataHoraFinalLessThanEqual(dataFinal))) {
            throw new ConditiondInvalidException("Este anuncio já esta reservado para o período informado.");
            }

        }
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

    public Optional<Reserva> buscarReservaPorId(Long id) throws IdInvalidException {

        if(!reservaRepository.existsById(id)){
            throw new IdInvalidException("Reserva", id);
        }
        return reservaRepository.findById(id);
    }

}
