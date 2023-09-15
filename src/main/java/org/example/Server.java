package org.example;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.google.gson.Gson;
import request.requisition.LoginRequisition;
import response.LoginResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread {
    protected Socket clientSocket;

    private Server(Socket clientSoc) {
        clientSocket = clientSoc;
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
                String token = null;
                try {
                    Algorithm alg = Algorithm.HMAC256("hiuhi");
                    token = JWT.create()
                            .withIssuer("me")
                            .withClaim("isAdmin", false)
                            .withClaim("userId", userId)
                            .sign(alg);
                    userId += 1;
                } catch (JWTCreationException e) {
                    System.err.println("oh no");
                }

                LoginResponse response = new LoginResponse(token);
                out.println(response.toJson());

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
