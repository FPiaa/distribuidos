package server.graph;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PathComponent  {
    private PathComponent parent = null;
    private Node node;
    private double distancia;


}
