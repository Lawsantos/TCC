package io.github.cwireset.tcc.request;

import io.github.cwireset.tcc.domain.*;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CadastrarImovelRequest {

    private Long idProprietario;

    private String identificacao;

    @Enumerated(EnumType.STRING)

    private TipoImovel tipoImovel;

    private Endereco endereco;

    private List<CaracteristicaImovel> caracteristicas;

}
