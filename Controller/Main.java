package Controller;

import javax.swing.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import Model.*;
import View.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("🚀 Program startar...");

        int screenWidth = 800;
        int screenHeight = 600;
        int gridRows = 70;
        int gridCols = 70;

        // Försök ladda kartbild
        BufferedImage mapImage = null;
        try {
            System.out.println("🗺️ Försöker ladda kartbild...");
            mapImage = ImageIO.read(Main.class.getResource("/images/malmo.png"));
            if (mapImage == null) {
                System.err.println("❌ Kartbilden är NULL! Fortsätter utan bakgrund...");
            } else {
                System.out.println("✅ Kartbilden laddades!");
            }
        } catch (IOException e) {
            System.err.println("❌ Fel vid laddning av kartbild: " + e.getMessage());
        }

        // Skapa View
        View view = new View(mapImage);

        // Skapa Dropdown-meny
        String[] elements = {"Car", "Bus", "Bike", "Factory", "Woodland", "Airplane"};
        JComboBox<String> elementSelector = new JComboBox<>(elements);

        // Skapa startknapp
        JButton startButton = new JButton("▶ Start");

        // Lägg till i kontrollpanelen
        JPanel controlPanel = new JPanel();
        controlPanel.add(elementSelector);
        controlPanel.add(startButton);

        // Skapa modell och controller
        GridManager gridManager = new GridManager(screenWidth, screenHeight, gridRows, gridCols);
        ElementManager elementManager = new ElementManager(gridManager);
        Controller controller = new Controller(gridManager, elementManager, view);

        view.setController(controller);
        view.setElementSelector(elementSelector);  // 🔥 Skickar dropdown-menyn till View

        // Lägg till actionListener till startknappen
        startButton.addActionListener(e -> controller.startSimulation());

        // Skapa och konfigurera JFrame
        JFrame frame = new JFrame("Interactive Map");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(screenWidth, screenHeight);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

        frame.add(controlPanel, BorderLayout.NORTH);
        frame.add(view, BorderLayout.CENTER);
        frame.setVisible(true);

        System.out.println("🖥️ View är nu synlig!");
    }
}
