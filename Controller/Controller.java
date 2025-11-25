package Controller;

import Model.*;
import View.*;


/**
 * Controller class to handle interactions between the view and the model.
 */
public class Controller {
    private GridManager gridManager;
    private ElementManager elementManager;
    private View view;

    public Controller(GridManager gridManager, ElementManager elementManager, View view) {
        this.gridManager = gridManager;
        this.elementManager = elementManager;
        this.view = view;
    }

    public void startSimulation() {
        view.setController(this);
        view.setPollutionGrid(gridManager.getPollutionGrid());
        view.setElements(elementManager.getElements());
        handleNextStep();
        view.repaint();
    }

    public void handleMouseClick(int row, int col, String elementType) {
        System.out.println("🖱️ Klick registrerad på (" + row + ", " + col + ") med typ " + elementType);

        int cellWidth = gridManager.getCellWidth();
        int cellHeight = gridManager.getCellHeight();

        // 🔥 Debug: Kontrollera om row och col är giltiga
        if (row < 0 || col < 0 || row >= gridManager.getGridRows() || col >= gridManager.getGridCols()) {
            System.out.println("❌ Ogiltiga koordinater! row=" + row + ", col=" + col);
            return;
        }

        // 🔥 Lägg till element på den klickade positionen
        boolean success = elementManager.addElement(row, col, elementType, cellWidth, cellHeight);

        if (success) {
            System.out.println("✅ Element " + elementType + " tillagt på (" + row + ", " + col + ")!");
        } else {
            System.out.println("❌ Kunde inte lägga till " + elementType + " på (" + row + ", " + col + ")!");
        }

        // 🔥 Debug: Se till att GUI uppdateras korrekt
        view.setPollutionGrid(gridManager.getPollutionGrid());
        view.setElements(elementManager.getElements());
        view.repaint();

        System.out.println("🔄 GUI uppdaterat!");
    }


    public void handleNextStep() {
        System.out.println("Handling next step");

        elementManager.updateElements(gridManager, view); // 🔥 Flytta endast befintliga element
        gridManager.diffusePollution(); // 🔥 Diffundera förorening

        view.setPollutionGrid(gridManager.getPollutionGrid());
        view.setElements(elementManager.getElements());
        view.repaint();
    }


}
