package io.github.cwireset.tcc.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Anuncio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @NotNull
    private TipoAnuncio tipoAnuncio;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_imovel")
    @Valid
    private Imovel imovel;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_anunciante")
    @Valid
    private Usuario anunciante;

    @NotNull
    private BigDecimal valorDiaria;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    @NotNull
    private List<FormaPagamento> formasAceitas;

    @NotBlank
    private String descricao;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private boolean ativo;

}
