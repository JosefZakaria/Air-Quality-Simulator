package View;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import Model.ElementIcon;
import Controller.Controller;

public class View extends JPanel {
    private BufferedImage mapImage;
    private double[][] pollutionGrid;
    private List<ElementIcon> elements;
    private Controller controller;
    private JComboBox<String> elementSelector;  // 🔹 Lagrar valt element från dropdown-menyn

    public View(BufferedImage mapImage) {
        this.mapImage = mapImage;
        this.pollutionGrid = null;
        this.elements = null;
    }

    public void setController(Controller controller) {
        if (this.controller != null) {
            return; // 🔥 Förhindra att lyssnaren registreras flera gånger
        }

        this.controller = controller;
        addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int col = e.getX() / getCellWidth();
                int row = e.getY() / getCellHeight();

                if (elementSelector == null) {
                    System.err.println("❌ ElementSelector är NULL!");
                    return;
                }

                String selectedElement = (String) elementSelector.getSelectedItem();
                System.out.println("🖱️ Klick registrerad på (" + row + ", " + col + ") med typ " + selectedElement);
                controller.handleMouseClick(row, col, selectedElement);
            }
        });

    }


    public void setElementSelector(JComboBox<String> elementSelector) {
        this.elementSelector = elementSelector;
    }

    public void setPollutionGrid(double[][] pollutionGrid) {
        this.pollutionGrid = pollutionGrid;
        repaint();
    }

    public void setElements(List<ElementIcon> elements) {
        this.elements = elements;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (mapImage != null) {
            g.drawImage(mapImage, 0, 0, getWidth(), getHeight(), null);
        }
        if (pollutionGrid != null) {
            int cellWidth = getCellWidth();
            int cellHeight = getCellHeight();
            for (int row = 0; row < pollutionGrid.length; row++) {
                for (int col = 0; col < pollutionGrid[0].length; col++) {
                    float alpha = Math.max(0f, Math.min(1f, (float) pollutionGrid[row][col] / 100f));
                    g.setColor(new Color(0, 0, 0, alpha)); // Svart rök

                    g.fillRect(col * cellWidth, row * cellHeight, cellWidth, cellHeight);
                }
            }
        }
        if (elements != null) {
            for (ElementIcon element : elements) {
                int x = element.getCol() * getCellWidth();
                int y = element.getRow() * getCellHeight();
                g.drawImage(element.getIcon(), x, y, getCellWidth(), getCellHeight(), null);
            }
        }
    }

    private int getCellWidth() {
        if (pollutionGrid != null) {
            return getWidth() / pollutionGrid[0].length;
        }
        return 50;
    }

    private int getCellHeight() {
        if (pollutionGrid != null) {
            return getHeight() / pollutionGrid.length;
        }
        return 50;
    }
}
