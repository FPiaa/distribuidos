package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import commons.Position;
import jakarta.persistence.Persistence;
import org.hibernate.SessionFactory;
import protocol.request.CreatePoiRequest;
import protocol.request.CreateSegmentRequest;
import protocol.request.UpdateSegmentRequest;
import server.controller.GraphController;
import server.dto.PoiDTO;

public class Age {
    public static void main(String[] args) {

        SessionFactory sessionFactory = (SessionFactory) Persistence.createEntityManagerFactory("server.entity");
        PoiDTO poi = new PoiDTO(1L, "ponto 1", new Position(0 ,0, 0), "", true);
        PoiDTO poi2 = new PoiDTO(2L, "ponto 2", new Position(1 ,1, 1), "", true);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try {
            var a = new CreatePoiRequest(null, "ponto 1", 0, 0, 0, "aviso", true);
            var b = new CreatePoiRequest(null, "ponto 2", 10, 10, 10, "blah", true);
            PoiDTO p = null;
            PoiDTO p2 = null;
            p = GraphController.getInstance().createPoi(a);
            p2 = GraphController.getInstance().createPoi(b);



            var s = new CreateSegmentRequest("", p.id(), p2.id(), "fodar", true );
            var seg = GraphController.getInstance().createSegment(s);
            System.out.println(gson.toJson(seg));

            seg = GraphController.getInstance().findSegment(p2.id(), p.id());
            System.out.println(gson.toJson(seg));
            var up = new UpdateSegmentRequest("", p.id(), p2.id(), "aaa", false);
            seg = GraphController.getInstance().updateSegment(up);
            System.out.println(gson.toJson(seg));
            System.out.println();
            var segs = GraphController.getInstance().findSegments();
            System.out.println(gson.toJson(segs));
            System.out.println();
            seg = GraphController.getInstance().deleteSegment(p.id(), p2.id());
            System.out.println(gson.toJson(seg));
            System.out.println();
            segs = GraphController.getInstance().findSegments();
            System.out.println(gson.toJson(segs));

//            p = GraphController.getInstance().findPoi(p.id());
//            System.out.println(gson.toJson(p));
//            var update = new UpdatePoiRequest(null, 1L, "ponto 2", 1, 2, 3, null, null);
//            var p = GraphController.getInstance().updatePoi(update);
//            System.out.println(gson.toJson(p));
//            GraphController.getInstance().deletePoi(1L);
            var ps = GraphController.getInstance().findPois();
            System.out.println(gson.toJson(ps));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
