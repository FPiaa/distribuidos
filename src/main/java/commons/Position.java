package commons;

import jakarta.validation.constraints.NotNull;

public record Position(@NotNull Double x, @NotNull Double y){

}
