package io.github.cwireset.tcc.domain;

import lombok.*;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Imovel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String identificacao;

    @Enumerated(EnumType.STRING)

    private TipoImovel tipoImovel;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_endereco")
    @Valid
    private Endereco endereco;

    @ManyToOne
    @JoinColumn(name = "id_proprietario")
    @Valid
    private Usuario proprietario;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "id_imovel")
    private List<CaracteristicaImovel> caracteristicas;

    public Imovel(String identificacao, TipoImovel tipoImovel, Endereco endereco, Usuario proprietario, List<CaracteristicaImovel> caracteristicas) {
    }
}
