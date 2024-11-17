package uco.ima;

public class Move {
    private Position start, end;

    public Move(Position start, Position end) {
        this.start = start;
        this.end = end;
    }
    public Position getStart() {
        return start;
    }
    public Position getEnd() {
        return end;
    }
    public String toString() {
        return start + "-" + end;
    }
}
