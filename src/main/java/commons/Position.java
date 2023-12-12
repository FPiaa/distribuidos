package commons;

import jakarta.validation.constraints.NotNull;

public record Position(@NotNull Double x, @NotNull Double y){
    public double angle(Position other) {
        return Math.atan2(other.y() - y(), other.x() - x());
    }

}
