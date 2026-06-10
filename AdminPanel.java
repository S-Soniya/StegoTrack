import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.List;  // Add this import
import java.util.ArrayList;  // Add this import

public class AdminPanel {
    private JFrame frame;
    private JCheckBox offlineToggle;
    private JLabel imageLabel; // For species photo display

    public void showUI() {
        frame = new JFrame("Admin Panel");
        frame.setSize(900, 600);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setUndecorated(true);

        ImageBackgroundPanel bgPanel = new ImageBackgroundPanel("D:/WildSteg/Tiger.jpg");
        bgPanel.setLayout(new BorderLayout());

        JLabel title = new JLabel("Admin Panel", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(Color.WHITE);
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        bgPanel.add(title, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel();
        centerPanel.setOpaque(false);
        centerPanel.setLayout(new GridLayout(5, 2, 20, 20));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JButton toggleBtn = createModernButton("System Settings");
        toggleBtn.addActionListener(e -> showSettings());

        JButton speciesBtn = createModernButton("Species Tracker");
        speciesBtn.addActionListener(e -> showSpeciesTracker());

        JButton newsBtn = createModernButton("Wildlife News & Alerts");
        newsBtn.addActionListener(e -> showNewsAndAlerts());

        JButton missionsBtn = createModernButton("Upcoming Research/Missions");
        missionsBtn.addActionListener(e -> showUpcomingMissions());

        // Adding new buttons for Polls/Surveys and Species Alert System
        JButton pollsBtn = createModernButton("Admin Polls/Surveys");
        pollsBtn.addActionListener(e -> showPollsAndSurveys());

        JButton alertBtn = createModernButton("Species Alert System");
        alertBtn.addActionListener(e -> showSpeciesAlertSystem());

        centerPanel.add(toggleBtn);
        centerPanel.add(speciesBtn);
        centerPanel.add(newsBtn);
        centerPanel.add(missionsBtn);
        centerPanel.add(pollsBtn);  // New button for Polls/Surveys
        centerPanel.add(alertBtn);  // New button for Species Alert System

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setOpaque(false);

        JButton backBtn = new JButton("Back to Dashboard");
        styleModernButton(backBtn, new Color(70, 130, 180));
        backBtn.addActionListener(e -> {
            frame.dispose();
            new Dashboard("Admin").showUI();
        });

        JButton exitBtn = new JButton("Exit");
        styleModernButton(exitBtn, new Color(180, 50, 50));
        exitBtn.setPreferredSize(backBtn.getPreferredSize());
        exitBtn.addActionListener(e -> System.exit(0));

        bottomPanel.add(backBtn);
        bottomPanel.add(exitBtn);

        bgPanel.add(centerPanel, BorderLayout.CENTER);
        bgPanel.add(bottomPanel, BorderLayout.SOUTH);

        frame.setContentPane(bgPanel);
        frame.setVisible(true);
    }

    private void showPollsAndSurveys() {
        // Example Polls and Surveys Section
        List<String> pollsList = new ArrayList<>();
        pollsList.add("Poll 1: Should we increase conservation efforts for tigers?");
        pollsList.add("Survey: What is the most urgent issue facing wildlife conservation?");

        JPanel pollsPanel = new JPanel();
        pollsPanel.setLayout(new BoxLayout(pollsPanel, BoxLayout.Y_AXIS));
        pollsPanel.setBackground(new Color(240, 240, 240));
        pollsPanel.setBorder(BorderFactory.createTitledBorder("Admin Polls & Surveys"));

        for (String poll : pollsList) {
            JLabel pollLabel = new JLabel(poll);
            pollLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            pollLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            pollsPanel.add(pollLabel);
        }

        JScrollPane scrollPane = new JScrollPane(pollsPanel);
        JOptionPane.showMessageDialog(frame, scrollPane, "Admin Polls & Surveys", JOptionPane.PLAIN_MESSAGE);
    }

    private void showSpeciesAlertSystem() {
        // Example Species Alert System Section
        List<String> alertList = new ArrayList<>();
        alertList.add("Alert: Poaching activity near Bengal Tiger habitats!");
        alertList.add("Alert: Habitat destruction reported in the snow leopard's region.");

        JPanel alertPanel = new JPanel();
        alertPanel.setLayout(new BoxLayout(alertPanel, BoxLayout.Y_AXIS));
        alertPanel.setBackground(new Color(240, 240, 240));
        alertPanel.setBorder(BorderFactory.createTitledBorder("Species Alert System"));

        for (String alert : alertList) {
            JLabel alertLabel = new JLabel(alert);
            alertLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            alertLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            alertPanel.add(alertLabel);
        }

        JScrollPane scrollPane = new JScrollPane(alertPanel);
        JOptionPane.showMessageDialog(frame, scrollPane, "Species Alert System", JOptionPane.PLAIN_MESSAGE);
    }

    private void showSpeciesTracker() {
        String[] columnNames = {
            "Species Name", "Scientific Name", "Status", "Count", "Photos Shared", "Last Reported"
        };

        Object[][] data = {
            {"Bengal Tiger", "Panthera tigris tigris", "Endangered", 12, 34, "2025-08-03"},
            {"Indian Pangolin", "Manis crassicaudata", "Critically Endangered", 3, 9, "2025-07-30"},
            {"Asiatic Lion", "Panthera leo persica", "Endangered", 5, 15, "2025-07-28"},
            {"Snow Leopard", "Panthera uncia", "Vulnerable", 7, 18, "2025-07-25"},
            {"Mountain Gorilla", "Gorilla beringei beringei", "Endangered", 4, 10, "2025-08-01"},
	    {"Indian Vulture", "Gyps indicus", "Critically Endangered", 8, 12, "2025-07-29"},
            {"Indian Rhinoceros", "Rhinoceros unicornis", "Vulnerable", 6, 14, "2025-07-27"},
            {"Red Panda", "Ailurus fulgens", "Endangered", 11, 20, "2025-07-26"},
            {"Black Softshell Turtle", "Nilssonia nigricans", "Critically Endangered", 2, 6, "2025-07-24"},
            {"Indian Star Tortoise", "Geochelone elegans", "Vulnerable", 9, 13, "2025-07-23"},
            {"Nilgiri Tahr", "Nilgiritragus hylocrius", "Endangered", 15, 25, "2025-07-22"},
    };

        JTable table = new JTable(data, columnNames);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(24);
        
        // Set auto-resize mode and preferred column widths
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.getColumnModel().getColumn(0).setPreferredWidth(150);  // Species Name
        table.getColumnModel().getColumn(1).setPreferredWidth(200);  // Scientific Name
        table.getColumnModel().getColumn(2).setPreferredWidth(120);  // Status
        table.getColumnModel().getColumn(3).setPreferredWidth(60);   // Count
        table.getColumnModel().getColumn(4).setPreferredWidth(100);  // Photos Shared
        table.getColumnModel().getColumn(5).setPreferredWidth(120);  // Last Reported

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        // Initialize imageLabel
        imageLabel = new JLabel("Select a species to view image");
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        imageLabel.setVerticalAlignment(JLabel.CENTER);
        imageLabel.setPreferredSize(new Dimension(300, 250));
        imageLabel.setBorder(BorderFactory.createTitledBorder("Species Photo"));

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(imageLabel, BorderLayout.EAST);

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow >= 0) {
                    String speciesName = table.getValueAt(selectedRow, 0).toString();
                    String filename = "species_photos/" + speciesName.replace(" ", "_") + ".jpg";
                    File imgFile = new File(filename);
                    if (imgFile.exists()) {
                        try {
                            BufferedImage img = ImageIO.read(imgFile);
                             // Resize image proportionally to fit within 300x250 max size
                            int imageWidth = img.getWidth();
                            int imageHeight = img.getHeight();
                            double aspectRatio = (double) imageWidth / imageHeight;
                            int newWidth = 280;
                            int newHeight = (int) (newWidth / aspectRatio);
                            if (newHeight > 240) {
                                newHeight = 240;
                                newWidth = (int) (newHeight * aspectRatio);
                            }
                            Image scaledImage = img.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
                            imageLabel.setIcon(new ImageIcon(scaledImage));
                            imageLabel.setText(null); // Remove the "No image" text
                        } catch (IOException ex) {
                            imageLabel.setText("Image load error.");
                        }
                    } else {
                        imageLabel.setIcon(null);
                        imageLabel.setText("No image found.");
                    }
                }
            }
        });

        JOptionPane.showMessageDialog(frame, panel, "Endangered Species Tracker", JOptionPane.PLAIN_MESSAGE);
    }

    private void showNewsAndAlerts() {
        // News & Alerts Section
        List<String> newsList = new ArrayList<>();
        newsList.add("BREAKING: New Conservation Efforts in the Amazon Rainforest!");
        newsList.add("ALERT: Poaching Incidents Reported Near India-Nepal Border.");
        newsList.add("NEWS: African Elephant Population Stabilizes After Decades of Decline.");
        newsList.add("UPDATE: Conservation Technology Reduces Wildlife Tracking Costs by 50%.");

        JPanel newsPanel = new JPanel();
        newsPanel.setLayout(new BoxLayout(newsPanel, BoxLayout.Y_AXIS));
        newsPanel.setBackground(new Color(240, 240, 240));
        newsPanel.setBorder(BorderFactory.createTitledBorder("Wildlife News & Alerts"));

        for (String news : newsList) {
            JLabel newsLabel = new JLabel(news);
            newsLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            newsLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            newsPanel.add(newsLabel);
        }

        JScrollPane scrollPane = new JScrollPane(newsPanel);
        JOptionPane.showMessageDialog(frame, scrollPane, "Wildlife News & Alerts", JOptionPane.PLAIN_MESSAGE);
    }

    private void showUpcomingMissions() {
        // Missions Section
        List<String> missionsList = new ArrayList<>();
        missionsList.add("2025-08-10: Elephant Tracking Mission in Kenya");
        missionsList.add("2025-08-15: Habitat Restoration Workshop in Madagascar");
        missionsList.add("2025-08-20: Poaching Awareness Program in India");
        missionsList.add("2025-08-25: Marine Species Survey in the Galapagos");

        JPanel missionsPanel = new JPanel();
        missionsPanel.setLayout(new BoxLayout(missionsPanel, BoxLayout.Y_AXIS));
        missionsPanel.setBackground(new Color(240, 240, 240));
        missionsPanel.setBorder(BorderFactory.createTitledBorder("Upcoming Research & Missions"));

        for (String mission : missionsList) {
            JLabel missionLabel = new JLabel(mission);
            missionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            missionLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            missionsPanel.add(missionLabel);
        }

        JScrollPane scrollPane = new JScrollPane(missionsPanel);
        JOptionPane.showMessageDialog(frame, scrollPane, "Upcoming Research & Missions", JOptionPane.PLAIN_MESSAGE);
    }

    private void showSettings() {
        if (offlineToggle == null) {
            offlineToggle = new JCheckBox("Enable Offline Mode");
            offlineToggle.setSelected(Dashboard.OFFLINE_MODE);
            offlineToggle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            offlineToggle.addActionListener(e -> Dashboard.OFFLINE_MODE = offlineToggle.isSelected());
        }

        JOptionPane.showMessageDialog(frame, offlineToggle, "System Settings", JOptionPane.PLAIN_MESSAGE);
    }

    private JButton createModernButton(String text) {
        JButton btn = new JButton(text);
        styleModernButton(btn, new Color(211, 211, 211));
        return btn;
    }

    private void styleModernButton(JButton btn, Color bg) {
        final Color buttonBg = bg;
        btn.setBackground(buttonBg);
        btn.setForeground(Color.BLACK);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        btn.setPreferredSize(new Dimension(btn.getPreferredSize().width + 20, 40));
        btn.setBorderPainted(false);

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                btn.setBackground(buttonBg.darker());
                btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }
            public void mouseExited(MouseEvent evt) {
                btn.setBackground(buttonBg);
                btn.setCursor(Cursor.getDefaultCursor());
            }
        });

        btn.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
            @Override
            protected void installDefaults(AbstractButton b) {
                super.installDefaults(b);
                b.setRolloverEnabled(true);
            }
        });
    }

    class ImageBackgroundPanel extends JPanel {
        private BufferedImage bgImage;
        public ImageBackgroundPanel(String imagePath) {
            try {
                bgImage = ImageIO.read(new File(imagePath));
            } catch (Exception e) {
                System.err.println("Error loading background image: " + imagePath);
            }
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
