package commons;

import jakarta.validation.constraints.NotNull;

public record Position(@NotNull Integer x, @NotNull Integer y, @NotNull Integer z){

}
