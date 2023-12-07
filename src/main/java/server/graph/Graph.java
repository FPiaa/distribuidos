package server.graph;

import protocol.commons.dto.PoiDTO;
import protocol.commons.dto.SegmentDTO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Graph {
    private final Map<Long, Node> graph;

    public Graph(List<PoiDTO> pois, List<SegmentDTO> segments) {
        graph = pois.stream()
                .map((x) -> new Node(x.id(),
                    x.nome(),
                    x.aviso(),
                    x.posicao(),
                    x.acessivel(),
                    new HashMap<>(segments.size() * 2))
                ).collect(Collectors.toMap(Node::getId, Function.identity()));
        for (SegmentDTO segment :
                segments) {
            var node = graph.get(segment.pdi_inicial());
            node.vizinhos.put(segment.pdi_final(), segment);
        }
    }
}
