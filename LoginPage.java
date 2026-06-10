import javax.swing.*;
import java.awt.*;
import java.io.File;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class LoginPage {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginPage().createUI());
    }

    public void createUI() {
        JFrame frame = new JFrame("🐾 StegoTrack Login");
        frame.setSize(400, 350);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setUndecorated(true);

        // Background panel with image
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                try {
                    BufferedImage bgImage = ImageIO.read(new File("D:/WildSteg/Tiger.jpg"));
                    g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), null);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        panel.setLayout(new GridBagLayout());

        // Main login panel (Transparent)
        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new BoxLayout(loginPanel, BoxLayout.Y_AXIS));
        loginPanel.setOpaque(false); // ✅ transparent instead of white
        loginPanel.setBorder(BorderFactory.createEmptyBorder(25, 35, 25, 35));
        loginPanel.setMaximumSize(new Dimension(300, 250));

        JLabel title = new JLabel("🐾 StegoTrack");
        title.setForeground(Color.WHITE); // ✅ white text for visibility
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel roleLabel = new JLabel("Select Role:");
        roleLabel.setForeground(Color.WHITE);
        roleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        roleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        roleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));

        // Role dropdown
        String[] roles = {"Field Agent", "Supervisor", "Admin"};
        JComboBox<String> roleBox = new JComboBox<>(roles);
        roleBox.setMaximumSize(new Dimension(200, 30));
        roleBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        roleBox.setForeground(Color.BLACK);
        roleBox.setBackground(new Color(211, 211, 211));

        // Custom renderer for dropdown items
        roleBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(
                    JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {

                JLabel label = (JLabel) super.getListCellRendererComponent(
                        list, value, index, isSelected, cellHasFocus);

                label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                if (isSelected) {
                    label.setBackground(new Color(169, 169, 169));
                    label.setForeground(Color.BLACK);
                } else {
                    label.setBackground(new Color(211, 211, 211));
                    label.setForeground(Color.BLACK);
                }
                label.setOpaque(true);
                return label;
            }
        });

        JLabel passLabel = new JLabel("Enter Password:");
        passLabel.setForeground(Color.WHITE); // ✅ white text for visibility
        passLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        passLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        passLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));

        JPasswordField passwordField = new JPasswordField();
        passwordField.setMaximumSize(new Dimension(200, 30));
        passwordField.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton loginBtn = new JButton("Login");
        loginBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginBtn.setBackground(new Color(76, 175, 80)); // green
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setFocusPainted(false);
        loginBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        loginBtn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        loginBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        loginBtn.addActionListener(e -> {
            String selectedRole = (String) roleBox.getSelectedItem();
            String password = new String(passwordField.getPassword());

            if (password.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "⚠ Please enter a password", "Error", JOptionPane.ERROR_MESSAGE);
            } else if (selectedRole.equals("Admin") && !password.equals("admin123")) {
                JOptionPane.showMessageDialog(frame, "❌ Incorrect password for Admin", "Error", JOptionPane.ERROR_MESSAGE);
            } else if (selectedRole.equals("Supervisor") && !password.equals("super123")) {
                JOptionPane.showMessageDialog(frame, "❌ Incorrect password for Supervisor", "Error", JOptionPane.ERROR_MESSAGE);
            } else if (selectedRole.equals("Field Agent") && !password.equals("agent123")) {
                JOptionPane.showMessageDialog(frame, "❌ Incorrect password for Field Agent", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                frame.dispose();
                Dashboard dashboard = new Dashboard(selectedRole);
                dashboard.showUI();
            }
        });

        // Add components
        loginPanel.add(title);
        loginPanel.add(roleLabel);
        loginPanel.add(roleBox);
        loginPanel.add(passLabel);
        loginPanel.add(passwordField);
        loginPanel.add(Box.createVerticalStrut(20));
        loginPanel.add(loginBtn);

        panel.add(loginPanel, new GridBagConstraints());

        frame.setContentPane(panel);
        frame.setVisible(true);
    }
}
