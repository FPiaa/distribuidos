package org.example;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.gson.Gson;
import request.requisition.LoginRequisition;
import response.LoginResponse;
import response.error.ErrorResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;


public class Main {
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


        String userInput;
        while ((userInput = stdin.readLine()) != null) {
            if (userInput.equals("Bye.")) {
                break;
            }
            LoginRequisition login = new LoginRequisition("email", "senah");
            String request = new Gson().toJson(login);
            out.println(request);

            System.out.println("Sending data: " + request);
            String response = in.readLine();
            System.out.println("Recebido: " + response);
            try {
                var a = new Gson().fromJson(response, LoginResponse.class);
                DecodedJWT token = JWT.decode(a.payload().getToken());
                int userId = token.getClaim("userId").asInt();
                System.out.println("User id: " + userId);
            } catch (NullPointerException e) {
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

