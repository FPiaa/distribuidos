package server.controller;

import protocol.request.CreatePoiRequest;
import protocol.request.UpdatePoiRequest;
import server.dto.PoiDTO;
import server.exceptions.ResourceNotFoundException;
import server.repository.GraphRepository;

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
        long id = System.nanoTime();
        var dto = PoiDTO.builder()
                .id(id)
                .nome(poi.getPayload().name())
                .acessivel(poi.getPayload().acessivel())
                .aviso(poi.getPayload().aviso())
                .posicao(poi.getPayload().position())
                .build();

        return repository.createPoi(dto);
    }

    public PoiDTO updatePoi(UpdatePoiRequest request) throws ResourceNotFoundException {
       var dto = PoiDTO.builder()
               .id(request.getPayload().id())
               .posicao(request.getPayload().posicao())
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

}
