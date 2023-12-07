package server.graph;

import commons.Position;
import lombok.Builder;
import lombok.Data;
import protocol.commons.dto.SegmentDTO;

import java.util.HashMap;

@Data
@Builder
public class Node {
    Long id;
    String nome;
    String aviso;
    Position posicao;
    Boolean acessivel;
    HashMap<Long, SegmentDTO> vizinhos;
}
