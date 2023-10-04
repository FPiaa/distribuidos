package client;

import com.fasterxml.jackson.core.JsonProcessingException;
import helper.json.JsonHelper;
import protocol.Optional;
import protocol.request.LoginRequest;
import protocol.request.LogoutRequest;
import protocol.request.Request;
import protocol.request.RequisitionOperations;
import protocol.request.header.Header;
import protocol.response.ErrorResponse;
import protocol.response.LoginResponse;
import protocol.response.LogoutResponse;
import protocol.response.Response;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.net.Socket;
import java.net.UnknownHostException;


public class Client {
    public static void main(String[] args) {
        String serverHost = "localhost";
        int port = 24800;

        try (Socket echoSocket = new Socket(serverHost, port);
             PrintWriter out = new PrintWriter(echoSocket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(
                     echoSocket.getInputStream()));
             BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in))
        ) {
            repl(out, in, stdin);
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host: " + serverHost);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for "
                    + "the connection to: " + serverHost);
            System.exit(1);
        }
    }

    private static void repl(PrintWriter out, BufferedReader in, BufferedReader stdin) {
        String token = null;
        try {
            while (true) {
                Request<?> request = requestFactory(stdin, token);
                String jsonRequest = JsonHelper.toJson(request);
                out.println(jsonRequest);
                System.out.println();
                System.out.println("Objeto de envio criado: " + request);
                System.out.println("Enviado: " + jsonRequest);
                System.out.println();

                String jsonResponse = in.readLine();
                if (jsonResponse == null) {
                    System.err.println("Erro recebendo dados do servidor");
                    break;
                }
                System.out.println("Recebido: " + jsonResponse);
                Response<?> response = handleResponse(jsonResponse,  request);
                System.out.println("Objeto criado: " + response);
                if(response instanceof LoginResponse) {
                    token = ((LoginResponse) response).payload().token();
                }

                if(response instanceof LogoutResponse) {
                    break;
                }

                System.out.println();

            }
        } catch (IOException e) {
            System.out.println("error reading stdin");
        }
    }

    private static Request<?> requestFactory(BufferedReader stdin, String token) throws IOException {

        String operation;
        while (true) {
            System.out.print("Insira a operação: ");
            operation = stdin.readLine();
            if (operation == null) {
                throw new IOException();
            }

            switch (operation) {
                case RequisitionOperations.LOGIN:
                    return makeRequest(stdin, token, LoginRequest.class);
                case RequisitionOperations.LOGOUT:
                    return makeRequest(stdin, token, LogoutRequest.class);
            }
        }
    }

    private static Response<?> handleResponse(String json, Request<?> request) {
        try {
            Class<?> clazz = request.getClass();
            if (clazz == LoginRequest.class) {
                return JsonHelper.fromJson(json, LoginResponse.class);
            }
            if (clazz == LogoutRequest.class) {
                return JsonHelper.fromJson(json, LogoutResponse.class);
            }
        } catch (JsonProcessingException e) {
            try {
                return JsonHelper.fromJson(json, ErrorResponse.class);
            } catch (JsonProcessingException ex) {
                throw new RuntimeException(ex);
            }
        }
        return null;
    }


    @SuppressWarnings("unchecked")
    private static <T> T makeRequest(BufferedReader stdin, String token, Class<T> clazz) throws IOException {
        for (Constructor<?> constructor : clazz.getConstructors()) {
            Parameter[] parameters = constructor.getParameters();
            if (parameters.length == 2 && parameters[0].getType() == Header.class) {
                // default constructor of every request record
                continue;
            }

            Object[] constructorArguments = new Object[parameters.length];
            for (int i = 0; i < parameters.length; i++) {
                if (parameters[i].getName().toLowerCase().contains("token")) {
                    constructorArguments[i] = token;
                    continue;
                }
                System.out.print(parameters[i].getName());
                if (parameters[i].isAnnotationPresent(Optional.class)) {
                    System.out.print(" (opcional)");
                }
                System.out.print(": ");
                constructorArguments[i] = stdin.readLine();
            }

            // this casting is fine, but the compiler cant be sure because its generic type
            // was erased up there
            try {
                return (T) constructor.newInstance(constructorArguments);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }

        throw new RuntimeException("Unable to create a new instance of " + clazz.getName());
    }
}

