package client;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import json.JsonHelper;
import protocol.request.LoginRequest;
import protocol.request.LogoutRequest;
import protocol.response.ErrorResponse;
import protocol.response.LoginResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;


public class Client {
    public static void main(String[] args) throws IOException {
        String serverHost = "localhost";
        int port = 24800;

        try (Socket echoSocket = new Socket(serverHost, port);
             PrintWriter out = new PrintWriter(echoSocket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(
                     echoSocket.getInputStream()));
             BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
        ) {
            repl(echoSocket, out, in, stdin);
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host: " + serverHost);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for "
                    + "the connection to: " + serverHost);
            System.exit(1);
        }
    }

    private static void repl(Socket server, PrintWriter out, BufferedReader in, BufferedReader stdin) {
        String token = "";
        String userInput;
        try {
            while ((userInput = stdin.readLine()) != null) {
                if (userInput.equals("Bye.")) {
                    var logoutRequest = new LogoutRequest(token);
                    String request = JsonHelper.toJson(logoutRequest);
                    out.println(request);

                    System.out.println("Enviado: " + request);
                    String response = in.readLine();
                    System.out.println("Recebido: " + response);
                    break;
                }
                LoginRequest login = new LoginRequest("email", "senah");
                String request = JsonHelper.toJson(login);
                out.println(request);


                System.out.println("Enviado: " + request);
                String response = in.readLine();
                System.out.println("Recebido: " + response);
                try {
                    var loginResponse = JsonHelper.fromJson(response, LoginResponse.class);
                    token = loginResponse.payload().token();
                    DecodedJWT decodedJWT = JWT.decode(token);
                    int userId = decodedJWT.getClaim("userId").asInt();
                    System.out.println("User id: " + userId);
                } catch (JsonSyntaxException e) {
                    ErrorResponse error = new Gson().fromJson(response, ErrorResponse.class);
                    System.out.println(error.toString());
                }
            }
        } catch (IOException e) {
            System.out.println("error reading stdin");
        }
    }
}

