package protocol.commons;

import lombok.Data;
import lombok.NonNull;

@Data
public class Command {
    @NonNull
    String nome_inicio;
    @NonNull
    String nome_final;
    @NonNull
    Double distancia;
    @NonNull
    String aviso;
    @NonNull
    String direcao;
}
