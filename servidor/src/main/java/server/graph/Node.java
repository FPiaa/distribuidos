package server.graph;

import commons.Position;
import lombok.Builder;
import lombok.Data;
import protocol.commons.dto.SegmentDTO;

import java.util.List;

@Data
@Builder
public class Node {
    Long id;
    String nome;
    String aviso;
    Position posicao;
    Boolean acessivel;
    List<SegmentDTO> vizinhos;

    public void addVizinho(SegmentDTO segmentDTO) {
        vizinhos.add(segmentDTO);
    }

}
