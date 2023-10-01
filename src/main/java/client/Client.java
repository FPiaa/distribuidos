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
        BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
        String serverHost = "localhost";
        int port = 24800;

        Socket echoSocket = null;
        PrintWriter out = null;
        BufferedReader in = null;

        try {
            echoSocket = new Socket(serverHost, port);
            out = new PrintWriter(echoSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(
                    echoSocket.getInputStream()));
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host: " + serverHost);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for "
                    + "the connection to: " + serverHost);
            System.exit(1);
        }

        String token = "";
        String userInput;
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
                var loginResponse = new Gson().fromJson(response, LoginResponse.class);
                token = loginResponse.payload().token();
                DecodedJWT decodedJWT = JWT.decode(token);
                int userId = decodedJWT.getClaim("userId").asInt();
                System.out.println("User id: " + userId);
            } catch (JsonSyntaxException e) {
                ErrorResponse error = new Gson().fromJson(response, ErrorResponse.class);
                System.out.println(error.toString());
            }
        }

        out.close();
        in.close();
        stdin.close();
        echoSocket.close();
    }
}

