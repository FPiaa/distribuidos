package server.controller;

import com.google.gson.GsonBuilder;
import protocol.commons.Command;
import protocol.commons.dto.PoiDTO;
import protocol.commons.dto.SegmentDTO;
import protocol.request.CreatePoiRequest;
import protocol.request.CreateSegmentRequest;
import protocol.request.UpdatePoiRequest;
import protocol.request.UpdateSegmentRequest;
import server.exceptions.ResourceNotFoundException;
import server.exceptions.ServerResponseException;
import server.graph.Graph;
import server.repository.GraphRepository;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GraphController {
    private static final GraphRepository repository = GraphRepository.getInstance();

    private static GraphController controller = null;

    private GraphController() {
    }

    public static GraphController getInstance() {
        if (controller == null) {
            controller = new GraphController();
        }

        return controller;
    }

    public PoiDTO createPoi(CreatePoiRequest poi) {
        long id = System.currentTimeMillis();
        var dto = PoiDTO.builder()
                .id(id)
                .nome(poi.getPayload().nome())
                .acessivel(poi.getPayload().acessivel())
                .aviso(poi.getPayload().aviso())
                .posicao(poi.getPayload().posicao())
                .build();

        return repository.createPoi(dto);
    }

    public PoiDTO updatePoi(UpdatePoiRequest request) throws ResourceNotFoundException {
        var dto = PoiDTO.builder()
                .id(request.getPayload().id())
                .aviso(request.getPayload().aviso())
                .acessivel(request.getPayload().acessivel())
                .nome(request.getPayload().nome())
                .build();

        return repository.updatePoi(dto);

    }

    public PoiDTO deletePoi(long id) throws ResourceNotFoundException {
        return repository.deletePoi(id);
    }

    public List<PoiDTO> findPois() {
        return repository.findPois();
    }


    public PoiDTO findPoi(long id) throws ResourceNotFoundException {
        return repository.findPoi(id)
                .orElseThrow(() -> new ResourceNotFoundException("Não foi possivel encontrar um PDI com id: " + id));
    }

    public SegmentDTO createSegment(CreateSegmentRequest segmentRequest) throws ServerResponseException {
        var dto = SegmentDTO.builder()
                .pdi_final(segmentRequest.getPayload().pdi_final())
                .pdi_inicial(segmentRequest.getPayload().pdi_inicial())
                .acessivel(segmentRequest.getPayload().acessivel())
                .aviso(segmentRequest.getPayload().aviso())
                .build();

        return repository.createSegment(dto);

    }

    public List<SegmentDTO> findSegments() {
        return repository.findSegments();
    }


    public SegmentDTO findSegment(long id1, long id2) throws ResourceNotFoundException {
        return repository.findSegment(id1, id2)
                .orElseThrow(() -> new ResourceNotFoundException("Não foi possivel encontrar um segmento entre " + id1 + " e " + id2));
    }

    public SegmentDTO updateSegment(UpdateSegmentRequest request) throws ResourceNotFoundException {
        var dto = SegmentDTO.builder()
                .pdi_final(request.getPayload().pdi_final())
                .pdi_inicial(request.getPayload().pdi_inicial())
                .acessivel(request.getPayload().acessivel())
                .aviso(request.getPayload().aviso())
                .build();

        return repository.updateSegment(dto);
    }

    public SegmentDTO deleteSegment(long id1, long id2) throws ResourceNotFoundException {
        return repository.deleteSegment(id1, id2);
    }


    public List<Command> findRoute(long id1, long id2) throws ResourceNotFoundException {
        var pdis = findPois();
        var segments = findSegments();
        var graph = new Graph(pdis, segments);
        var gson = new GsonBuilder().setPrettyPrinting().create();
        var pretty = gson.toJson(graph);
        System.out.println("\n\n\nGRAFOOOOOOOO\n\n\n");
        System.out.println(pretty);
        try {
            var file = new FileWriter("teste.json");
            file.write(pretty);
            file.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println(pretty);
        // TODO: build graph and find shortest path
        return new ArrayList<>();
    }
}
