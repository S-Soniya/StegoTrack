import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class ReceiverApp {
    private boolean offlineMode;
    private JPasswordField decryptionKeyField;
    private JTextArea outputArea;
    private File selectedImageFile;
    private JLabel selectedFileLabel; // To show selected file name
    private String role;

    public ReceiverApp() {}

    public ReceiverApp(String role) {
        this.role = role;
    }

    public void createUI() {
        JFrame frame = new JFrame("Receiver App: StegoTrack");
        frame.setSize(600, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setUndecorated(true);

        BackgroundPanel panel = new BackgroundPanel("D:/WildSteg/Tiger.jpg");
        panel.setLayout(new GridBagLayout());

        JLabel title = new JLabel("📸 Decode Animal Photo & GPS");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        panel.add(title, new GridBagConstraints(0, 0, 2, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(10, 0, 10, 0), 0, 0));

        // Label for Select Image
        JLabel selectImgLabel = new JLabel("Select Encoded Image:");
        selectImgLabel.setForeground(Color.WHITE);
        selectImgLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        panel.add(selectImgLabel, constraints(0, 1, 1));

        // Panel for Browse button + file name in same line
        JPanel filePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        filePanel.setOpaque(false);

        JButton selectImgBtn = new JButton("Browse...");
        styleButton(selectImgBtn);
        selectImgBtn.addActionListener(e -> selectImage());
        filePanel.add(selectImgBtn);

        selectedFileLabel = new JLabel("No file selected");
        selectedFileLabel.setForeground(Color.LIGHT_GRAY);
        selectedFileLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        filePanel.add(selectedFileLabel);

        panel.add(filePanel, constraints(1, 1, 1));

        // Label for Decryption Key
        JLabel decryptionLabel = new JLabel("Enter Decryption Key:");
        decryptionLabel.setForeground(Color.WHITE);
        decryptionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        panel.add(decryptionLabel, constraints(0, 2, 1));

        decryptionKeyField = new JPasswordField(20);
        styleField(decryptionKeyField);
        panel.add(decryptionKeyField, constraints(1, 2, 1));

        outputArea = new JTextArea(10, 30);
        outputArea.setEditable(false);
        outputArea.setBackground(new Color(40, 40, 40));
        outputArea.setForeground(Color.WHITE);
        JScrollPane scrollPane = new JScrollPane(outputArea);
        panel.add(scrollPane, constraints(0, 3, 2));

        JButton decodeBtn = new JButton("Decode & Extract Data");
        styleButton(decodeBtn);
        decodeBtn.addActionListener(e -> decodeImageAndDisplayData());
        panel.add(decodeBtn, constraints(0, 4, 2));

        JButton backBtn = new JButton("← Back");
        backBtn.setBackground(new Color(80, 80, 80));
        backBtn.setForeground(Color.WHITE);
        backBtn.setFocusPainted(false);
        backBtn.addActionListener(e -> {
            frame.dispose();
            new Dashboard("Field Agent").showUI(); // Adjust as needed
        });
        panel.add(backBtn, constraints(0, 5, 2));

        frame.setContentPane(panel);
        frame.setVisible(true);
    }

    private void styleButton(JButton btn) {
        btn.setBackground(new Color(211, 211, 211));  // Light grey color
        btn.setForeground(Color.BLACK);  // Black text color
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
    }

    private void styleField(JTextField field) {
        field.setBackground(new Color(40, 40, 40));
        field.setForeground(Color.WHITE);
        field.setCaretColor(Color.WHITE);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    }

    private GridBagConstraints constraints(int x, int y, int w) {
        return new GridBagConstraints(x, y, w, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(10, 10, 10, 10), 0, 0);
    }

    private void selectImage() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select an Encoded Image");
        int result = fileChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            selectedImageFile = fileChooser.getSelectedFile();
            // Update label in same line
            selectedFileLabel.setText(selectedImageFile.getName());
        }
    }

    private void decodeImageAndDisplayData() {
        String decryptionKey = new String(decryptionKeyField.getPassword()).trim();

        if (decryptionKey.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Decryption key cannot be empty.");
            return;
        }

        try {
            if (selectedImageFile == null) {
                JOptionPane.showMessageDialog(null, "Please select an image first.");
                return;
            }

            BufferedImage image = ImageIO.read(selectedImageFile);
            if (image == null) {
                JOptionPane.showMessageDialog(null, "Error loading image.");
                return;
            }

            // Extract the encrypted message
            String encryptedMessage = StegoLSB.decodeText(image);
            if (encryptedMessage == null || encryptedMessage.isEmpty()) {
                JOptionPane.showMessageDialog(null, "No hidden data found in the image.");
                return;
            }

            // Decrypt the message
            String decryptedMessage = EncryptUtil.decrypt(encryptedMessage, decryptionKey);

            // Display the decrypted message
            outputArea.setText("Decoded & Decrypted Message:\n" + decryptedMessage);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error reading image.");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error decrypting message. Check the key.");
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
