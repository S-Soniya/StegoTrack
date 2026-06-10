import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Dashboard {
    private String role;
    public static boolean OFFLINE_MODE = true; // Default enabled

    public Dashboard(String role) {
        this.role = role;
    }

    public void showUI() {
        JFrame frame = new JFrame("Dashboard - " + role);
        frame.setSize(600, 420);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setUndecorated(true); // Custom styling

        // Load background image
        Image backgroundImage = null;
        try {
            backgroundImage = ImageIO.read(new File("D:/WildSteg/Tiger.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Image finalBackgroundImage = backgroundImage;

        // Background panel
        JPanel bgPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (finalBackgroundImage != null) {
                    g.drawImage(finalBackgroundImage, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        bgPanel.setLayout(new BorderLayout());

        // 🔹 Custom Title Bar
        JPanel titleBar = new JPanel(new BorderLayout());
        titleBar.setBackground(new Color(0, 0, 0, 150));
        titleBar.setPreferredSize(new Dimension(frame.getWidth(), 38));

        JLabel titleLabel = new JLabel("   🐾 StegoTrack - Dashboard (" + role + ")");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));

        // Minimize button
        JButton minBtn = new JButton("—");
        styleTitleButton(minBtn, new Color(80, 80, 80));
        minBtn.addActionListener(e -> frame.setState(Frame.ICONIFIED));

        // Close button
        JButton closeBtn = new JButton("✖");
        styleTitleButton(closeBtn, new Color(200, 0, 0));
        closeBtn.addActionListener(e -> System.exit(0));

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        btnPanel.setOpaque(false);
        btnPanel.add(minBtn);
        btnPanel.add(closeBtn);

        titleBar.add(titleLabel, BorderLayout.WEST);
        titleBar.add(btnPanel, BorderLayout.EAST);

        // 🔹 Main Panel (Transparent, not white)
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setOpaque(false); // ✅ Transparent background (so Tiger.jpg shows)
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        mainPanel.setMaximumSize(new Dimension(400, 300));

        JLabel heading = new JLabel("Welcome, " + role + "!");
        heading.setFont(new Font("Segoe UI", Font.BOLD, 24));
        heading.setForeground(Color.WHITE); // ✅ White text for visibility
        heading.setAlignmentX(Component.CENTER_ALIGNMENT);

        JCheckBox offlineToggle = new JCheckBox("Offline Mode");
        offlineToggle.setSelected(true);
        offlineToggle.setForeground(Color.WHITE); // ✅ White for readability
        offlineToggle.setOpaque(false);
        offlineToggle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        offlineToggle.setAlignmentX(Component.CENTER_ALIGNMENT);
        offlineToggle.addActionListener(e -> OFFLINE_MODE = offlineToggle.isSelected());

        JButton openBtn = new JButton("🚀 Open App");
        styleActionButton(openBtn, new Color(76, 175, 80), new Color(56, 142, 60));
        openBtn.addActionListener(e -> {
            frame.dispose();
            if (role.equalsIgnoreCase("Field Agent")) {
                new SenderApp(role).createUI();
            } else if (role.equalsIgnoreCase("Supervisor")) {
                new ReceiverApp(role).createUI();
            } else if (role.equalsIgnoreCase("Admin")) {
                new AdminPanel().showUI();
            } else {
                JOptionPane.showMessageDialog(null, "Admin panel coming soon!");
            }
        });

        JButton backBtn = new JButton("← Back to Login");
        styleActionButton(backBtn, new Color(100, 149, 237), new Color(65, 105, 225));
        backBtn.addActionListener(e -> {
            frame.dispose();
            new LoginPage().createUI();
        });

        JButton exitBtn = new JButton("❌ Exit");
        styleActionButton(exitBtn, new Color(220, 53, 69), new Color(180, 30, 45));
        exitBtn.addActionListener(e -> System.exit(0));

        // Add components to main panel
        mainPanel.add(heading);
        mainPanel.add(Box.createVerticalStrut(15));
        mainPanel.add(offlineToggle);
        mainPanel.add(Box.createVerticalStrut(25));
        mainPanel.add(openBtn);
        mainPanel.add(Box.createVerticalStrut(12));
        mainPanel.add(backBtn);
        mainPanel.add(Box.createVerticalStrut(12));
        mainPanel.add(exitBtn);

        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setOpaque(false);
        centerWrapper.add(mainPanel);

        bgPanel.add(titleBar, BorderLayout.NORTH);
        bgPanel.add(centerWrapper, BorderLayout.CENTER);

        frame.setContentPane(bgPanel);
        frame.setVisible(true);
    }

    // 🔹 Helper to style title bar buttons
    private void styleTitleButton(JButton button, Color bgColor) {
        button.setForeground(Color.WHITE);
        button.setBackground(bgColor);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(45, 35));
        button.setOpaque(true);

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });
    }

    // 🔹 Helper to style main action buttons
    private void styleActionButton(JButton button, Color normalColor, Color hoverColor) {
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setBackground(normalColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 15));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setMaximumSize(new Dimension(180, 42));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(hoverColor);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(normalColor);
            }
        });
    }
}
