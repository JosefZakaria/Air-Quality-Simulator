package Model;

import java.util.ArrayList;
import java.util.List;
import View.View;

public class ElementManager {
    private List<ElementIcon> elements;
    private GridManager gridManager;

    public ElementManager(GridManager gridManager) {
        this.gridManager = gridManager;
        this.elements = new ArrayList<>();
    }

    public List<ElementIcon> getElements() {
        return elements;
    }

    public boolean addElement(int row, int col, String elementType, int cellWidth, int cellHeight) {
        System.out.println("🟢 Försöker lägga till: " + elementType + " på (" + row + ", " + col + ")");

        // 🔍 Kontrollera om positionen är utanför kartan
        if (row < 0 || row >= gridManager.getGridRows() || col < 0 || col >= gridManager.getGridCols()) {
            System.out.println("❌ Element kan inte placeras utanför kartan (" + row + ", " + col + ")");
            return false;
        }

        // 🔍 Kontrollera om ett element av samma typ redan finns på platsen
        for (ElementIcon existingElement : elements) {
            if (existingElement.getRow() == row && existingElement.getCol() == col
                    && existingElement.getClass().getSimpleName().equals(elementType)) {
                System.out.println("⚠️ Element redan på platsen, skapar inte ny instans.");
                return false;
            }
        }

        // 🏗️ Skapa element
        ElementIcon element = createElement(row, col, elementType, cellWidth, cellHeight);

        if (element != null && gridManager.isLand(row, col)) {
            elements.add(element);
            gridManager.getPollutionGrid()[row][col] += element.getPollution();
            System.out.println("✅ " + elementType + " lades till på (" + row + ", " + col + ")");
            return true;
        }

        System.out.println("❌ Kunde inte lägga till " + elementType + " på (" + row + ", " + col + ")");
        return false;
    }



    public void updateElements(GridManager gridManager, View view) {
        List<ElementIcon> toRemove = new ArrayList<>();

        for (ElementIcon element : elements) {
            element.update(gridManager);

            // 🔥 Om elementet lämnar kartan, ta bort det
            if (!gridManager.isInsideGrid(element.getRow(), element.getCol())) {
                System.out.println("🗑️ " + element.getClass().getSimpleName() + " har lämnat kartan och tas bort!");
                toRemove.add(element);
            }
        }

        elements.removeAll(toRemove);

        // 🛠️ 🔥 Uppdatera GUI:t efter att ha tagit bort element
        view.setElements(elements);
        view.repaint();

        gridManager.diffusePollution();
    }



    private ElementIcon createElement(int row, int col, String elementType, int cellWidth, int cellHeight) {
        System.out.println("🛠 Skapa element: " + elementType + " på (" + row + ", " + col + ")");

        switch (elementType) {
            case "Bike": return new Bike(row, col, cellWidth, cellHeight);
            case "Car": return new Car(row, col, cellWidth, cellHeight);
            case "Bus": return new Bus(row, col, cellWidth, cellHeight);
            case "Airplane": return new Airplane(row, col, cellWidth, cellHeight);
            case "WomanWalking": return new WomanWalking(row, col, cellWidth, cellHeight);
            case "Factory": return new Factory(row, col, cellWidth, cellHeight);
            case "Train": return new Train(row, col, cellWidth, cellHeight);
            case "Ship": return new Ship(row, col, cellWidth, cellHeight);
            case "Woodland": return new Tree(row, col, cellWidth, cellHeight);
            case "Elsparkcykel": return new Elsparkcykel(row, col, cellWidth, cellHeight);
            default:
                System.out.println("❌ Okänd typ: " + elementType);
                return null;
        }
    }



}
