package commons;

import jakarta.validation.constraints.NotNull;

public record Position(@NotNull Float x, @NotNull Float y){

}
