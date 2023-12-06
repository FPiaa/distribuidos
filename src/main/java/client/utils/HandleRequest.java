package client.utils;

import com.google.gson.JsonSyntaxException;
import helper.json.JsonHelper;
import helper.validation.ConstraintViolated;
import helper.validation.ValidationHelper;
import lombok.Getter;
import protocol.request.*;
import protocol.response.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.function.Consumer;

@Getter
public class HandleRequest {
    private String host = "localhost";
    private int port = 24800;
    private static HandleRequest instance = null;
    private Socket echoSocket;

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
        try {
            echoSocket = new Socket(host, port);
        } catch (IOException ignored) {

        }
    }

    public <T> void makeRequest(Request<T> obj, Consumer<Response<?>> onSuccess, Consumer<? super String> onFailure) {
        try {
            PrintWriter out = new PrintWriter(echoSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
            String sendJson = JsonHelper.toJson(obj);
            System.out.println("Enviado:   " + sendJson);
            out.println(sendJson);

            String receivedJson = in.readLine();
            System.out.println("\n\nRecebido:  " + receivedJson);
            if (receivedJson == null) {
                var error = new ErrorResponse(1, "O servidor não retornou nenhuma resposta");
                onFailure.accept(error.payload().message());
                return;
            }
            Response<?> res = handleResponse(receivedJson, obj);
            if (res instanceof ErrorResponse) {
                onFailure.accept(((ErrorResponse) res).error().message());
                return;
            }
            onSuccess.accept(res);

        } catch (UnknownHostException e) {
            var error = new ErrorResponse(2, "Host %s:%d  desconhecido.".formatted(host, port));
            onFailure.accept(error.payload().message());
        } catch (IOException e) {
            var error = new ErrorResponse(3, "Comunicação com o Host %s:%d  falhou.".formatted(host, port));
            onFailure.accept(error.payload().message());
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
            if (clazz == CreatePoiRequest.class) {
                response = JsonHelper.fromJson(json, CreatePoiResponse.class);
            }
            if (clazz == UpdatePoiRequest.class) {
                response = JsonHelper.fromJson(json, UpdatePoiResponse.class);
            }
            if (clazz == DeletePoiRequest.class) {
                response = JsonHelper.fromJson(json, DeletePoiResponse.class);
            }
            if (clazz == FindPoisRequest.class) {
                response = JsonHelper.fromJson(json, FindPoisResponse.class);
            }
            if (clazz == CreateSegmentRequest.class) {
                response = JsonHelper.fromJson(json, CreateSegmentResponse.class);
            }
            if (clazz == UpdateSegmentRequest.class) {
                response = JsonHelper.fromJson(json, UpdateSegmentResponse.class);
            }
            if (clazz == DeleteSegmentRequest.class) {
                response = JsonHelper.fromJson(json, DeleteSegmentResponse.class);
            }
            if (clazz == FindSegmentsRequest.class) {
                response = JsonHelper.fromJson(json, FindSegmentsResponse.class);
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
