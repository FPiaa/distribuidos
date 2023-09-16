package org.example;

import com.google.gson.Gson;
import request.RequisitionOperations;
import request.requisition.LoginRequisition;
import response.Response;
import response.error.ErrorResponse;
import server.router.Router;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread {
    protected Socket clientSocket;
    protected Router<?, ?> routes;

    private Server(Socket clientSoc) {
        clientSocket = clientSoc;
        routes = Router.builder()
                .addRoute(RequisitionOperations.LOGIN, null)
                .build();
        start();
    }

    public static void main(String[] args) {
        ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket(24800);
            System.out.println("Connection Socket Created");
            try {
                while (true) {
                    System.out.println("Waiting for Connection");
                    new Server(serverSocket.accept());
                }
            } catch (IOException e) {
                System.err.println("Accept failed.");
                System.exit(1);
            }
        } catch (IOException e) {
            System.err.println("Could not listen on port: 10008.");
            System.exit(1);
        } finally {
            try {
                assert serverSocket != null;
                serverSocket.close();
            } catch (IOException e) {
                System.err.println("Could not close port: 10008.");
                System.exit(1);
            }
        }
    }

    public void run() {
        System.out.println("New Communication Thread Started");

        try {
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(),
                    true);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));

            String inputLine;
            Gson gson = new Gson();
            int userId = 0;
            while ((inputLine = in.readLine()) != null) {
                System.out.println("Server: " + inputLine);
                LoginRequisition req = gson.fromJson(inputLine, LoginRequisition.class);
                System.out.println(req.toString());
                userId += 1;
                if (userId % 2 == 0) {
                    Response<?> response = routes.serve(RequisitionOperations.LOGIN);
                    out.println(response.toJson());
                } else {
                    ErrorResponse error = new ErrorResponse(123123, "asdfkjhasdf");
                    out.println(error.toJson());
                }

                if (inputLine.equals("Bye."))
                    break;
            }

            out.close();
            in.close();
            clientSocket.close();
        } catch (IOException e) {
            System.err.println("Problem with Communication Server");
            System.exit(1);
        }
    }
}
