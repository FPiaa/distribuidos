package server;

import helper.json.JsonHelper;
import protocol.commons.dto.CreateUser;
import protocol.request.RequisitionOperations;
import protocol.response.FindUserResponse;
import protocol.response.LogoutResponse;
import protocol.response.Response;
import server.controller.UserController;
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
    private final ConnectedUsers connectedUsers;

    private Server(Socket clientSoc, ConnectedUsers connectedUsers) {

        clientSocket = clientSoc;
        this.connectedUsers = connectedUsers;
        var user = new ConnectedUser(clientSocket.getInetAddress().toString(), null);
        connectedUsers.addUser(user);
        System.out.println(connectedUsers.getUsers());
        if (routes == null) {
            routes = Router.builder()
                    .addRoute(RequisitionOperations.LOGIN, new StartLogin())
                    .addRoute(RequisitionOperations.LOGOUT, new StartLogout())
                    .addRoute(RequisitionOperations.ADMIN_BUSCAR_USUARIOS, new StartAdminFindUsers())
                    .addRoute(RequisitionOperations.ADMIN_BUSCAR_USUARIO, new StartAdminFindUser())
                    .addRoute(RequisitionOperations.ADMIN_CADASTRAR_USUARIO, new StartAdminCreateUser())
                    .addRoute(RequisitionOperations.ADMIN_ATUALIZAR_USUARIO, new StartAdminUpdateUser())
                    .addRoute(RequisitionOperations.ADMIN_DELETAR_USUARIO, new StartAdminDeleteUser())
                    .addRoute(RequisitionOperations.CADASTRAR_USUARIO, new StartCreateUser())
                    .addRoute(RequisitionOperations.ATUALIZAR_USUARIO, new StartUpdateUser())
                    .addRoute(RequisitionOperations.BUSCAR_USUARIO, new StartFindUser())
                    .addRoute(RequisitionOperations.DELETAR_USUARIO, new StartDeleteUser())
                    .addRoute(RequisitionOperations.CADASTRAR_PDI, new StartCreatePoi())
                    .addRoute(RequisitionOperations.ATUALIZAR_PDI, new StartUpdatePoi())
                    .addRoute(RequisitionOperations.BUSCAR_PDIS, new StartFindPois())
                    .addRoute(RequisitionOperations.DELETAR_PDI, new StartDeletePoi())
                    .addRoute(RequisitionOperations.CADASTRAR_SEGMENTO, new StartCreateSegment())
                    .addRoute(RequisitionOperations.ATUALIZAR_SEGMENTO, new StartUpdateSegment())
                    .addRoute(RequisitionOperations.BUSCAR_SEGMENTOS, new StartFindSegments())
                    .addRoute(RequisitionOperations.DELETAR_SEGMENTO, new StartDeleteSegment())
                    .addRoute(RequisitionOperations.BUSCAR_ROTA, new StartFindRoute())
                    .build();
        }
        start();
    }

    public static void listen(int port, ConnectedUsers users) {

        try {
            UserController.getInstance()
                    .createUser(new CreateUser("admin@admin.com", "123456", "Igor", true));

            UserController.getInstance()
                    .createUser(new CreateUser("usuario@usuario.com", "123456", "Igor", false));
        }
        catch (Exception ignored) {}

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Connection Socket Created");
            while (true) {
                try {
                    System.out.println("Waiting for Connection");
                    new Server(serverSocket.accept(), users);
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
                System.out.println("Ip: " + clientSocket.getInetAddress() + "Recebido: " + inputLine);
                Response<?> response;

                try {
                    response = routes.serve(inputLine);
                } catch (ServerResponseException e) {
                    response = e.intoResponse();
                }

                String jsonResponse = JsonHelper.toJson(response);

                if(clientSocket.isClosed() || !clientSocket.isConnected())  {
                    System.out.println("Client closed connection");
                   break;
                }
                out.println(jsonResponse);

                System.out.println("Ip: " + clientSocket.getInetAddress() + "Enviado: " + jsonResponse);

                if (response instanceof LogoutResponse)
                    break;
                if (response instanceof FindUserResponse) {
                    var connect = new ConnectedUser(clientSocket.getInetAddress().toString(),
                            JsonHelper.fromJson(jsonResponse, FindUserResponse.class).payload().email());

                    System.out.println("FIND USER   " + connect);
                    connectedUsers.removeUser(connect);
                    connectedUsers.addUser(connect);
                }
            }
            if(clientSocket.isClosed() || !clientSocket.isConnected())  {
                System.out.println("Client closed connection");
            }
        } catch (IOException e) {
            System.err.println("Problem with Communication Server");
        }
        System.out.println("connection closed");
        assert (clientSocket.isClosed() && !clientSocket.isConnected());

        connectedUsers.removeUser(new ConnectedUser(clientSocket.getInetAddress().toString(), null));
    }

}
