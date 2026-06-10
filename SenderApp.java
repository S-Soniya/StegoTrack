import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class SenderApp {
    private boolean offlineMode;
    private JTextField gpsField;
    private JTextArea notesArea;
    private JTextField speciesTagField;
    private JPasswordField passwordField;
    private JLabel selectedFileLabel;   
    private File selectedImageFile;
    private String role;

    public SenderApp() {}

    public SenderApp(String role) {
        this.role = role;
    }

    public void createUI() {
        JFrame frame = new JFrame("Sender App: StegoTrack");
        frame.setSize(700, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setUndecorated(true);

        BackgroundPanel panel = new BackgroundPanel("D:/WildSteg/Tiger.jpg");
        panel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("📸 Encode Animal Photo & GPS");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(title, gbc);

        // Species Tag
        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 1;
        JLabel speciesTagLabel = new JLabel("Enter Species Tag:");
        styleLabel(speciesTagLabel);
        panel.add(speciesTagLabel, gbc);

        speciesTagField = new JTextField(20);
        styleTextField(speciesTagField);
        gbc.gridx = 1; gbc.gridy = 1; gbc.gridwidth = 2;
        panel.add(speciesTagField, gbc);

        // Select Image
        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 2;
        JLabel selectImgLabel = new JLabel("Select Photo:");
        styleLabel(selectImgLabel);
        panel.add(selectImgLabel, gbc);

        JButton selectImgBtn = new JButton("Browse...");
        styleButton(selectImgBtn);
        selectImgBtn.addActionListener(e -> selectImage());
        gbc.gridx = 1; gbc.gridy = 2;
        panel.add(selectImgBtn, gbc);

        selectedFileLabel = new JLabel("No file selected");
        selectedFileLabel.setForeground(Color.LIGHT_GRAY);
        selectedFileLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        gbc.gridx = 2; gbc.gridy = 2;
        panel.add(selectedFileLabel, gbc);

        // GPS
        gbc.gridx = 0; gbc.gridy = 3;
        JLabel gpsLabel = new JLabel("Enter GPS Coordinates (Lat, Long):");
        styleLabel(gpsLabel);
        panel.add(gpsLabel, gbc);

        gpsField = new JTextField(20);
        styleTextField(gpsField);
        gbc.gridx = 1; gbc.gridy = 3; gbc.gridwidth = 2;
        panel.add(gpsField, gbc);

        // Notes
        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 4;
        JLabel notesLabel = new JLabel("Field Notes (Optional):");
        styleLabel(notesLabel);
        panel.add(notesLabel, gbc);

        notesArea = new JTextArea(5, 20);
        notesArea.setBackground(new Color(30, 30, 30, 180)); // slightly transparent for readability
        notesArea.setForeground(Color.WHITE);
        notesArea.setCaretColor(Color.WHITE);
        notesArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        notesArea.setBorder(BorderFactory.createEmptyBorder(5, 8, 5, 8));
        JScrollPane scrollPane = new JScrollPane(notesArea);
        gbc.gridx = 1; gbc.gridy = 4; gbc.gridwidth = 2;
        panel.add(scrollPane, gbc);

        // Password
        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 5;
        JLabel passwordLabel = new JLabel("Enter Encryption Password:");
        styleLabel(passwordLabel);
        panel.add(passwordLabel, gbc);

        passwordField = new JPasswordField(20);
        styleTextField(passwordField);
        gbc.gridx = 1; gbc.gridy = 5; gbc.gridwidth = 2;
        panel.add(passwordField, gbc);

        // Buttons row
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        btnPanel.setOpaque(false);

        JButton submitBtn = new JButton("Submit & Encode");
        styleButton(submitBtn);
        submitBtn.addActionListener(e -> encodeAndSaveImage());
        btnPanel.add(submitBtn);

        JButton backBtn = new JButton("← Back");
        styleDarkButton(backBtn);
        backBtn.addActionListener(e -> {
            frame.dispose();
            new Dashboard("Field Agent").showUI();
        });
        btnPanel.add(backBtn);

        JButton exitBtn = new JButton("Exit");
        exitBtn.setBackground(new Color(220, 20, 60));
        exitBtn.setForeground(Color.WHITE);
        exitBtn.setFocusPainted(false);
        exitBtn.addActionListener(e -> System.exit(0));
        btnPanel.add(exitBtn);

        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 3;
        panel.add(btnPanel, gbc);

        frame.setContentPane(panel);
        frame.setVisible(true);
    }

    private void styleTextField(JTextField field) {
        field.setBackground(new Color(30, 30, 30, 180)); // semi-transparent for readability
        field.setForeground(Color.WHITE);
        field.setCaretColor(Color.WHITE);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)));
    }

    private void styleButton(JButton button) {
        button.setBackground(new Color(200, 200, 200));
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBorder(BorderFactory.createEmptyBorder(8, 18, 8, 18));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void styleDarkButton(JButton button) {
        button.setBackground(new Color(60, 60, 60));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBorder(BorderFactory.createEmptyBorder(8, 18, 8, 18));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void styleLabel(JLabel label) {
        label.setForeground(Color.LIGHT_GRAY);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 16));
    }

    private void selectImage() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select an Image");
        int result = fileChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            selectedImageFile = fileChooser.getSelectedFile();
            selectedFileLabel.setText(selectedImageFile.getName());
        }
    }

    private void encodeAndSaveImage() {
        String species = speciesTagField.getText();
        String gps = gpsField.getText();
        String notes = notesArea.getText();
        String message = "Species: " + species + "\nGPS: " + gps + "\nNotes: " + notes;
        String encryptionPassword = new String(passwordField.getPassword());

        try {
            if (selectedImageFile == null) {
                JOptionPane.showMessageDialog(null, "Please select an image first.");
                return;
            }

            String encryptedMessage = EncryptUtil.encrypt(message, encryptionPassword);
            BufferedImage image = ImageIO.read(selectedImageFile);
            BufferedImage encodedImage = StegoLSB.encodeText(image, encryptedMessage);

            File outputFolder = new File("outbox");
            if (!outputFolder.exists()) outputFolder.mkdirs();

            File outputImage = new File(outputFolder, "encoded_image.png");
            ImageIO.write(encodedImage, "PNG", outputImage);

            JOptionPane.showMessageDialog(null, "✅ Image encoded and saved successfully in outbox!");
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error encoding image.");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error encrypting or saving image.");
        }
    }

// 🔳 BackgroundPanel Class for Setting Background Image
class BackgroundPanel extends JPanel {
    private Image bgImage;

    public BackgroundPanel(String imagePath) {
        try {
            bgImage = ImageIO.read(new File(imagePath));
        } catch (IOException e) {
            System.err.println("Background image not found: " + imagePath);
        }
        setLayout(new GridBagLayout());
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (bgImage != null) {
            g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}

}