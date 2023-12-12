package server.graph;

public enum Direction {
    Frente,
    Esquerda,
    Direita;

    public Direction fromAngle(Direction currentDirection, double previousAngle, double newAngle) {
        final double epsilon = Math.PI/36; // epsilon de 5 graus
        double diff = newAngle - previousAngle;

        if((Math.abs(diff)) < epsilon) {
            return Frente;
        }

        if(Math.abs(diff) > Math.PI) {
            diff -= Math.PI;
            diff *= -1;

        }

        if(diff > 0) {
            return Esquerda;
        }
        if (diff < 0) {
            return Direita;
        }

        System.err.println("Current direction zoada");
        return currentDirection;
    }
    @Override
    public String toString() {
        switch (this) {
            case Frente -> {
                return "Siga em frente";
            }
            case Direita -> {
                return "Vire a direita";
            }
            case Esquerda -> {
                return "Vire a esquerda";
            }
        }
        return "Comando";
    }
}
