package domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_solicitante")
    @Valid
    private Usuario solicitante;

    @ManyToOne
    @JoinColumn(name = "id_anuncio")
    @Valid
    private Anuncio anuncio;

    @Embedded
    @NotNull
    @Valid
    private Periodo periodo;

    @NotNull
    private Integer quantidadePessoas;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull
    private LocalDateTime dataHoraReserva;

    @Embedded
    @NotNull
    private Pagamento pagamento;

}
