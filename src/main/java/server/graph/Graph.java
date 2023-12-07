package server.graph;

import protocol.commons.dto.PoiDTO;
import protocol.commons.dto.SegmentDTO;

import java.util.*;
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
                    new ArrayList<>(segments.size()))
                ).collect(Collectors.toMap(Node::getId, Function.identity()));
        for (SegmentDTO segment : segments) {
            var node = graph.get( segment.pdi_inicial());
            node.addVizinho(segment);
        }
        for (Node n :
                graph.values()) {
            assert !n.getVizinhos().isEmpty();

        }
    }

    public Optional<Node> getNode(Long id) {
        return Optional.ofNullable(graph.get(id));
    }

    public List<Node> findShortestPath(Long start, Long end) {
        Set<Long> visitedNodes = new HashSet<>(graph.size() * 2);
        Map<Long, PathComponent> tentativeDistance = new HashMap<>(graph.size() * 2);

        for (Long node : graph.keySet()) {
            tentativeDistance.put(node, new PathComponent(null, graph.get(node), Double.MAX_VALUE));
            System.out.println(graph.get(node).getNome());
        }

        tentativeDistance.put(start, new PathComponent(null, graph.get(start), 0));
        var initial = tentativeDistance.get(start);

        PriorityQueue<PathComponent> nodesToVisit = new PriorityQueue<>(32, (o1, o2) -> (int)(o1.getDistancia() - o2.getDistancia()));

        nodesToVisit.add(initial);
        while(!nodesToVisit.isEmpty()) {
            var pathComponent = nodesToVisit.poll();

            if(visitedNodes.contains(end) && pathComponent.getDistancia() > tentativeDistance.get(end).getDistancia()) {
                break;
            }

            for(var segment: pathComponent.getNode().vizinhos) {
                var neighbor = segment.pdi_final();

                if (visitedNodes.contains(neighbor) || !segment.acessivel()) {
                    continue;
                }

                nodesToVisit.add(tentativeDistance.get(neighbor));
                double currentDistance = tentativeDistance.get(pathComponent.getNode().getId()).getDistancia();

                var edgeSize = segment.distancia();
                var tentative = edgeSize + currentDistance;
                if(tentative < tentativeDistance.get(neighbor).getDistancia()) {
                    var tentativePath = tentativeDistance.get(neighbor);
                    tentativePath.setDistancia(tentative);
                    tentativePath.setParent(pathComponent);
                }
            }

            visitedNodes.add(pathComponent.getNode().getId());
        }

        if(visitedNodes.contains(end)) {
            return reconstructPath(end, tentativeDistance);
        } else {
            return new ArrayList<>();
        }
    }

    private List<Node> reconstructPath(Long end, Map<Long, PathComponent> tentativeDistance) {
        List<Node> path = new ArrayList<>();

        var current = tentativeDistance.get(end);

        while (current != null) {
            path.add(current.getNode());
            current = current.getParent();
        }
        Collections.reverse(path);
        return path;

    }
}
