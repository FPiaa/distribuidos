package server.router;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import server.exceptions.BadRequestException;
import server.exceptions.MethodNotAllowedException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RouterTest {

    private Router router;

    @BeforeEach
    public void createRouter() {
        router = Router.builder()
                .addRoute("OP1", jsonString -> null)
                .build();
    }

    @ParameterizedTest
    @ValueSource(strings = {"""
            {"header": {"operation":"OP1"}}
            """,
            """
                    {"header": {"operation":"OP1", "token": "token"}}
                    """,
            """
                    {"header": {"operation":"OP1", "token": "token"}, "payload": null}
                    """,
            """
                    {"header": {"operation":"OP1", "token": "token"}, "payload": {"some": 123, "object": "foo"}}
                    """
    })
    public void GivenValidRequestHeader_whenServe_getsResponse(String json) {
        assertDoesNotThrow(() -> router.serve(json));
    }

    @ParameterizedTest()
    @ValueSource(strings = {
            """
                    {"header": {"operation": 1}}""",
            """
                    {"header": {"operation":"OP1", "token": 123}}""",
            """
                    {"header": {"operation": 1.0, "token": 123.1}}""",
            """
                    {"header": {"operation":"OP1", "token": 123}}"""
    })
    public void GivenWrongTypeInHeader_whenServe_getsWrongTypeErrorResponse(String json) {
        assertThrows(BadRequestException.class, () -> router.serve(json));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            """
                    {"header": {"operation":null}}
                    """,

            """
                    {"header": {"operation":""}}
                    """,
            """
                    {"header": {"token": "token"}}
                    """,
            """
                    {"header": {}}
                    """,
    })
    public void GivenHeaderMissingOperation_whenServe_getsMissingObligatoryFieldsResponse(String json) {
        assertThrows(BadRequestException.class, () -> router.serve(json));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            """
                    {"header": {"operation":"OP2"}}
                    """,
            """
                    {"header": {"operation":"OP2", "token": "token"}}
                    """,
            """
                    {"header": {"operation":"OP2", "token": "token"}, "payload": null}
                    """,
            """
                    {"header": {"operation":"OP2", "token": "token"}, "payload": {"some": 123, "object": "foo"}}
                    """
    })
    public void GivenValidHeaderWithoutKnownOperation_whenServe_getsMethodNotAllowedErrorResponse(String json) {
        assertThrows(MethodNotAllowedException.class, () -> router.serve(json));

    }


}
