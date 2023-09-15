package org.example;
import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.gson.Gson;
import request.requisition.LoginRequisition;
import response.LoginResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Main {
    public static void main(String[] args) throws IOException {

        BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Forneça o host");
        String serverHost = stdin.readLine();

        System.out.println("Forneça a porta");
        int port = Integer.parseInt(stdin.readLine());

        System.out.println(serverHost);

        Socket echoSocket = null;
        PrintWriter out = null;
        BufferedReader in = null;

        try{
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
        boolean isLogged = false;
        while((userInput = stdin.readLine()) != null) {
            if(userInput.equals("Bye.")) {
                break;
            }
                LoginRequisition login = new LoginRequisition("email", "senha");
                String request = new Gson().toJson(login);
                out.println(request);

            System.out.println("Sending data: " + request);
            String response = in.readLine();
            var a = new Gson().fromJson(response, LoginResponse.class);
            DecodedJWT token = JWT.decode(a.payload().token());
            int userId = token.getClaim("userId").asInt();
            System.out.println("User id: " + userId);
        }

        out.close();
        in.close();
        stdin.close();
        echoSocket.close();
    }
}

