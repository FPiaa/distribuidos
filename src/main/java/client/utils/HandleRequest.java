package client.utils;

import com.google.gson.JsonSyntaxException;
import helper.json.JsonHelper;
import helper.validation.ConstraintViolated;
import helper.validation.ValidationHelper;
import protocol.request.*;
import protocol.response.*;

import java.util.function.Consumer;

public class HandleRequest {
    private String host = "localhost";
    private int port = 24800;
    private static HandleRequest instance = null;

    private HandleRequest() {
    }

    public static HandleRequest getInstance() {
        if (instance == null) {
            instance = new HandleRequest();
        }

        return instance;
    }

    public void updateEndpoit(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public <T> Response<?> makeRequest(Request<T> obj, Consumer<Void> onSuccess) {

        onSuccess.accept(null);
        return null;
//        try (Socket echoSocket = new Socket(host, port);
//             PrintWriter out = new PrintWriter(echoSocket.getOutputStream(), true);
//             BufferedReader in = new BufferedReader(new InputStreamReader(
//                     echoSocket.getInputStream()));
//             BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in))
//        ) {
//            String sendJson = JsonHelper.toJson(obj);
//            out.println(sendJson);
//
//            String receivedJson = in.readLine();
//            if (receivedJson == null) {
//                return new ErrorResponse(1, "O servidor não retornou nenhuma resposta");
//            }
//            return handleResponse(receivedJson, obj);
//
//        } catch (UnknownHostException e) {
//            return new ErrorResponse(2, "Host %s:%d  desconhecido.".formatted(host, port));
//        } catch (IOException e) {
//            return new ErrorResponse(3, "Comunicação com o Host %s:%d  falhou.".formatted(host, port));
//        }

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
                response = JsonHelper.fromJson(json, CreateUserResponse.class);
            }
            if (clazz == AdminUpdateUserRequest.class) {
                response = JsonHelper.fromJson(json, UpdateUserResponse.class);
            }
            if (clazz == AdminDeleteUserRequest.class) {
                response = JsonHelper.fromJson(json, DeleteUserResponse.class);
            }
            if (clazz == CreateUserRequest.class) {
                response = JsonHelper.fromJson(json, CreateUserResponse.class);
            }
            if (clazz == UpdateUserRequest.class) {
                response = JsonHelper.fromJson(json, UpdateUserResponse.class);
            }
            if (clazz == DeleteUserRequest.class) {
                response = JsonHelper.fromJson(json, DeleteUserResponse.class);
            }
            if (clazz == FindUserRequest.class) {
                response = JsonHelper.fromJson(json, FindUserResponse.class);
            }


            if (response == null || response.payload() == null) {
                response = JsonHelper.fromJson(json, ErrorResponse.class);
            }

            ValidationHelper.validate(response);
            return response;
        } catch (ConstraintViolated e) {
            return new ErrorResponse(new ErrorResponse.Payload(1, "Não foi possível validar a resposta\n" + e.getMessage()));
        } catch (JsonSyntaxException e) {
            return new ErrorResponse(new ErrorResponse.Payload(2, "Json recebido foi mal formatado"));
        }
    }

}
