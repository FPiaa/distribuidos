package client;

import com.google.gson.JsonSyntaxException;
import helper.json.JsonHelper;
import helper.validation.ConstraintViolated;
import helper.validation.ValidationHelper;
import protocol.Optional;
import protocol.request.*;
import protocol.request.header.Header;
import protocol.response.*;

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
                Response<?> response = handleResponse(jsonResponse, request);
                System.out.println("Objeto criado: " + response);

                if (response == null) {
                    continue;
                }

                if (response instanceof LoginResponse) {
                    token = ((LoginResponse) response).payload().token();
                    System.out.println("token was set");
                }

                if (response instanceof LogoutResponse) {
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
                case RequisitionOperations.ADMIN_BUSCAR_USUARIOS:
                    return makeRequest(stdin, token, AdminFindUsersRequest.class);
                case RequisitionOperations.ADMIN_BUSCAR_USUARIO:
                    return makeRequest(stdin, token, AdminFindUserRequest.class);
                case RequisitionOperations.ADMIN_CADASTRAR_USUARIO:
                    return makeRequest(stdin, token, AdminCreateUserRequest.class);
                case RequisitionOperations.ADMIN_ATUALIZAR_USUARIO:
                    return makeRequest(stdin, token, AdminUpdateUserRequest.class);
                case RequisitionOperations.ADMIN_DELETAR_USUARIO:
                    return makeRequest(stdin, token, AdminDeleteUserRequest.class);
            }
        }
    }

    private static Response<?> handleResponse(String json, Request<?> request) {
        Response<?> response = null;
        try {
            Class<?> clazz = request.getClass();
            if (clazz == LoginRequest.class) {
                response = JsonHelper.fromJson(json, LoginResponse.class);
            }
            if (clazz == LogoutRequest.class) {
                response = JsonHelper.fromJson(json, LogoutResponse.class);
            }
            if (clazz == AdminFindUsersRequest.class) {
                response = JsonHelper.fromJson(json, FindUsersResponse.class);
            }
            if (clazz == AdminFindUserRequest.class) {
                response = JsonHelper.fromJson(json, FindUserResponse.class);
            }
            if (clazz == AdminCreateUserRequest.class) {
                response = JsonHelper.fromJson(json, AdminCreateUserResponse.class);
            }
            if (clazz == AdminUpdateUserRequest.class) {
                response = JsonHelper.fromJson(json, AdminUpdateUserResponse.class);
            }
            if (clazz == AdminDeleteUserRequest.class) {
                response = JsonHelper.fromJson(json, AdminDeleteUserResponse.class);
            }

            if (response == null || response.payload() == null) {
                response = JsonHelper.fromJson(json, ErrorResponse.class);
            }
            ValidationHelper.validate(response);
            return response;
        } catch (ConstraintViolated e) {
            System.err.println("Não foi possível validar a resposta\n" + e.getMessage());
            return response;
        } catch (JsonSyntaxException e) {
            System.err.println("Erro no json recebido");
        }
        return null;
    }


    @SuppressWarnings("unchecked")
    private static <T> T makeRequest(BufferedReader stdin, String token, Class<T> clazz) throws IOException {
        for (Constructor<?> constructor : clazz.getConstructors()) {
            Parameter[] parameters = constructor.getParameters();
            boolean shouldSkip = false;

            for (Parameter parameter : parameters) {
                if (parameter.getType() == Header.class) {
                    shouldSkip = true;
                    break;
                }
            }
            if (shouldSkip) {
                // it is the default constructos
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
                String line = stdin.readLine();
                if (line.isBlank() || line.isEmpty()) {
                    constructorArguments[i] = null;
                } else if (parameters[i].getType() == Long.class) {
                    constructorArguments[i] = Long.parseLong(line);
                } else if (parameters[i].getType() == Integer.class) {
                    constructorArguments[i] = Integer.parseInt(line);
                } else if (parameters[i].getType() == Boolean.class) {
                    constructorArguments[i] = Boolean.parseBoolean(line);
                } else {
                    constructorArguments[i] = line;
                }
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

