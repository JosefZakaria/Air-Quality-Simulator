// === Uppdaterad GridManager.java ===
package Model;

import java.util.ArrayList;
import java.util.List;

public class GridManager {
    private int screenWidth;
    private int screenHeight;
    private double[][] pollutionGrid;
    private List<ElementIcon> elements;

    public GridManager(int screenWidth, int screenHeight, int gridRows, int gridCols) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.pollutionGrid = new double[gridRows][gridCols]; // Initiera rutnätet
        this.elements = new ArrayList<>(); // Initiera listan
    }

    public void addPollution(int row, int col, double pollutionLevel) {
        if (isInsideGrid(row, col)) {
            pollutionGrid[row][col] += pollutionLevel;
        }
    }

    public double[][] getPollutionGrid() {
        return pollutionGrid;
    }

    public boolean isLand(int row, int col) {
        // Placeholder: Replace with IsLand logic
        return true;
    }

    public boolean removeElement(ElementIcon element) {
        if (elements.remove(element)) { // 🔥 Tar bort elementet från listan
            if (isInsideGrid(element.getRow(), element.getCol())) {
                pollutionGrid[element.getRow()][element.getCol()] = 0; // 🔥 Rensar pollution om elementet var på kartan
            }
            System.out.println("🗑️ Removed element at (" + element.getRow() + ", " + element.getCol() + ")");
            return true;
        }
        return false;
    }




    public boolean isInsideGrid(int newRow, int newCol) {
        return newRow >= 0 && newRow < pollutionGrid.length && newCol >= 0 && newCol < pollutionGrid[0].length;
    }

    public void diffusePollution() {
        double[][] newGrid = new double[pollutionGrid.length][pollutionGrid[0].length];

        for (int row = 0; row < pollutionGrid.length; row++) {
            for (int col = 0; col < pollutionGrid[row].length; col++) {
                double sum = pollutionGrid[row][col] * 4; // Viktar mitten högre
                int count = 4;

                if (row > 0) { sum += pollutionGrid[row - 1][col]; count++; }
                if (row < pollutionGrid.length - 1) { sum += pollutionGrid[row + 1][col]; count++; }
                if (col > 0) { sum += pollutionGrid[row][col - 1]; count++; }
                if (col < pollutionGrid[row].length - 1) { sum += pollutionGrid[row][col + 1]; count++; }

                newGrid[row][col] = sum / count; // Medelvärdesdiffusion
            }
        }

        pollutionGrid = newGrid;
        System.out.println("🌫️ Pollution diffused successfully!");
    }

    public int getCellWidth() {
        return screenWidth / pollutionGrid[0].length;
    }

    public int getCellHeight() {
        return screenHeight / pollutionGrid.length;
    }

    public int getGridRows() {
        return pollutionGrid.length;
    }

    public int getGridCols() {
        return pollutionGrid[0].length;
    }

}
