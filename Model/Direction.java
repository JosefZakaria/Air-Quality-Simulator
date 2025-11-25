package Model;

public enum Direction {
    NORTH(-1, 0), SOUTH(1, 0), EAST(0, 1), WEST(0, -1);

    private final int rowOffset;
    private final int colOffset;

    Direction(int rowOffset, int colOffset) {
        this.rowOffset = rowOffset;
        this.colOffset = colOffset;
    }

    public int getRowOffset() {
        return rowOffset;
    }

    public int getColOffset() {
        return colOffset;
    }

    public static Direction randomDirection() {
        Direction[] directions = values();
        return directions[(int) (Math.random() * directions.length)];
    }
}

