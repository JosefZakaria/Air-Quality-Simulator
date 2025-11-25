package Model;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;

public abstract class ElementIcon {
    protected int row;
    protected int col;
    protected double pollution;
    private BufferedImage icon;
    protected Direction direction;

    public ElementIcon(int row, int col, double pollution, String iconPath, int cellWidth, int cellHeight) {
        this.row = row;
        this.col = col;
        this.pollution = pollution;
        if (this.direction == null) {
            this.direction = Direction.randomDirection();
        }
        try {
            BufferedImage originalImage = ImageIO.read(getClass().getResource("/images/" + iconPath));

            // 🔴 Sänk ikonstorleken till 30% av cellens storlek (tidigare 60%)
//            int iconWidth = (int) (cellWidth * 0.5);
//            int iconHeight = (int) (cellHeight * 0.5);
            this.icon = scaleImage(originalImage, cellWidth, cellHeight);

        } catch (IOException | IllegalArgumentException e) {
            e.printStackTrace();
            try {
                this.icon = scaleImage(ImageIO.read(getClass().getResource("/images/default.png")), (int) (cellWidth * 0.5), (int) (cellHeight * 0.5));
            } catch (IOException ignored) {
                this.icon = new BufferedImage((int) (cellWidth * 0.5), (int) (cellHeight * 0.5), BufferedImage.TYPE_INT_ARGB);
            }
        }
    }

    private BufferedImage scaleImage(BufferedImage original, int width, int height) {
        if (original == null) return new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        Image scaledImage = original.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage bufferedScaled = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = bufferedScaled.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.drawImage(scaledImage, 0, 0, width, height, null);
        g2d.dispose();

        return bufferedScaled;
    }

    public int getRow() { return row; }
    public int getCol() { return col; }
    public double getPollution() { return pollution; }
    public BufferedImage getIcon() { return icon; }

    public boolean isMovable() {
        return this instanceof Car || this instanceof Bike || this instanceof Airplane;
    }

    public abstract void update(GridManager gridManager);


}



class Car extends ElementIcon {
    public Car(int row, int col, int cellWidth, int cellHeight) {
        super(row, col, 5, "car.png", cellWidth, cellHeight);
    }

    @Override
    public void update(GridManager gridManager) {
        gridManager.addPollution(row, col, pollution);

        int newRow = row + direction.getRowOffset();
        int newCol = col + direction.getColOffset();

        // 🔥 Om elementet går utanför kartan, markera det för borttagning
        if (!gridManager.isInsideGrid(newRow, newCol)) {
            System.out.println("🗑️ Car har lämnat kartan och tas bort!");
            gridManager.removeElement(this);
            return; // 🔥 Viktigt! Stoppar ytterligare uppdatering
        }

        // 🔄 Om elementet är kvar på kartan, flytta det
        row = newRow;
        col = newCol;
    }


}

class Airplane extends ElementIcon {
    private final int pollutionLevel = 10;

    public Airplane(int row, int col, int cellWidth, int cellHeight) {
        super(row, col, 10, "airplane.png", cellWidth, cellHeight);
    }

    @Override
    public void update(GridManager gridManager) {
        gridManager.addPollution(row, col, pollution);

        int newRow = row + direction.getRowOffset();
        int newCol = col + direction.getColOffset();

        // 🔥 Om elementet går utanför kartan, markera det för borttagning
        if (!gridManager.isInsideGrid(newRow, newCol)) {
            System.out.println("🗑️ Car har lämnat kartan och tas bort!");
            gridManager.removeElement(this);
            return; // 🔥 Viktigt! Stoppar ytterligare uppdatering
        }

        // 🔄 Om elementet är kvar på kartan, flytta det
        row = newRow;
        col = newCol;
    }


}

class Bike extends ElementIcon {
    public Bike(int row, int col, int cellWidth, int cellHeight) {
        super(row, col, 0, "bike.png", cellWidth, cellHeight);
    }

    @Override
    public void update(GridManager gridManager) {
        int newRow = row + direction.getRowOffset();
        int newCol = col + direction.getColOffset();

        if (!gridManager.isInsideGrid(newRow, newCol)) {
            System.out.println("🗑️ Bike har lämnat kartan och tas bort!");
            gridManager.removeElement(this);
            return;
        }

        row = newRow;
        col = newCol;
    }

}

class Bus extends ElementIcon {
    public Bus(int row, int col, int cellWidth, int cellHeight) {
        super(row, col, 10, "bus.png", cellWidth, cellHeight);
    }

    @Override
    public void update(GridManager gridManager) {
        gridManager.addPollution(row, col, pollution);
    }
}

class Train extends ElementIcon {
    public Train(int row, int col, int cellWidth, int cellHeight) {
        super(row, col, 3, "train.png", cellWidth, cellHeight);
    }

    @Override
    public void update(GridManager gridManager) {
        gridManager.addPollution(row, col, pollution);
    }
}

class Ship extends ElementIcon {
    public Ship(int row, int col, int cellWidth, int cellHeight) {
        super(row, col, 30, "ship.png", cellWidth, cellHeight);
    }

    @Override
    public void update(GridManager gridManager) {
        gridManager.addPollution(row, col, pollution);
    }
}

class Factory extends ElementIcon {
    public Factory(int row, int col, int cellWidth, int cellHeight) {
        super(row, col, 15, "factory.png", cellWidth, cellHeight);
    }

    @Override
    public void update(GridManager gridManager) {
        gridManager.addPollution(row, col, pollution);
    }
}

class Tree extends ElementIcon {
    public Tree(int row, int col, int cellWidth, int cellHeight) {
        super(row, col, -5, "trees.png", cellWidth, cellHeight);
    }

    @Override
    public void update(GridManager gridManager) {
        gridManager.addPollution(row, col, Math.max(-5, -pollution));
    }
}

class Elsparkcykel extends ElementIcon {
    public Elsparkcykel(int row, int col, int cellWidth, int cellHeight) {
        super(row, col, 0, "elsparkcykel.png", cellWidth, cellHeight);
    }

    @Override
    public void update(GridManager gridManager) {
        int newRow = row + direction.getRowOffset();
        int newCol = col + direction.getColOffset();
        if (gridManager.isInsideGrid(newRow, newCol)) {
            row = newRow;
            col = newCol;
        }
    }
}

class WomanWalking extends ElementIcon {
    public WomanWalking(int row, int col, int cellWidth, int cellHeight) {
        super(row, col, 0, "womanwalking.png", cellWidth, cellHeight);
    }

    @Override
    public void update(GridManager gridManager) {
        int newRow = row + direction.getRowOffset();
        int newCol = col + direction.getColOffset();
        if (gridManager.isInsideGrid(newRow, newCol)) {
            row = newRow;
            col = newCol;
        }
    }
}
