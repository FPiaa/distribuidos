package server;

import helper.json.JsonHelper;
import protocol.request.RequisitionOperations;
import protocol.response.LogoutResponse;
import protocol.response.Response;
import server.exceptions.ServerResponseException;
import server.layer.initialLayer.*;
import server.router.Router;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread {
    private final Socket clientSocket;
    private Router routes = null;

    private Server(Socket clientSoc) {
        clientSocket = clientSoc;
        if (routes == null) {
            routes = Router.builder()
                    .addRoute(RequisitionOperations.LOGIN, new StartLogin())
                    .addRoute(RequisitionOperations.LOGOUT, new StartLogout())
                    .addRoute(RequisitionOperations.ADMIN_BUSCAR_USUARIOS, new StartAdminFindUsers())
                    .addRoute(RequisitionOperations.ADMIN_BUSCAR_USUARIO, new StartAdminFindUser())
                    .addRoute(RequisitionOperations.ADMIN_CADASTRAR_USUARIO, new StartAdminCreateUser())
                    .addRoute(RequisitionOperations.ADMIN_ATUALIZAR_USUARIO, new StartAdminUpdateUser())
                    .build();
        }
        start();
    }

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(24800)) {
            System.out.println("Connection Socket Created");
            while (true) {
                try {
                    System.out.println("Waiting for Connection");
                    new Server(serverSocket.accept());
                } catch (IOException e) {
                    System.err.println("Accept failed.");
                    System.exit(1);
                }
            }
        } catch (IOException e) {
            System.err.println("Could not listen on port: 10008.");
            System.exit(1);
        }
    }

    public void run() {
        System.out.println("New Communication Thread Started");

        try (clientSocket;
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        ) {

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                System.out.println("Recebido: " + inputLine);
                Response<?> response;

                try {
                    response = routes.serve(inputLine);
                } catch (ServerResponseException e) {
                    response = e.intoResponse();
                }

                String jsonResponse = JsonHelper.toJson(response);
                System.out.println("Enviado: " + jsonResponse);
                out.println(jsonResponse);

                if (response instanceof LogoutResponse)
                    break;
            }
        } catch (IOException e) {
            System.err.println("Problem with Communication Server");
            System.exit(1);
        }

        assert (clientSocket.isClosed());
    }
}
