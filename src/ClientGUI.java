import javax.swing.*;
import java.awt.*;

public class ClientGUI {

    JFrame mainWin = new JFrame();
    JMenuBar mainMenuBar = new JMenuBar();
    JMenu mainMenu = new JMenu("IO");
    JMenuItem connectItem = new JMenuItem("Connect...");
    JMenuItem disconnectItem = new JMenuItem("Disconnect");

    JPanel mainPanel = new JPanel();
    JTextArea inpPanel = new JTextArea();

    JTextArea chatArea = new JTextArea("# Please connect to a server...");
    JTextField inpField = new JTextField();
    JButton sendBtn = new JButton("Send");


    JFrame connectWin = new JFrame();
    JPanel connectPanel = new JPanel();
    JTextField ipField = new JTextField();
    JTextField portField = new JTextField();
    JButton connectBtn = new JButton("Connect");


    public ClientGUI(String title) {
        mainWin.setTitle(title);
        winit();
    }

    public void winit() {
        mainWin.setSize(600, 400);
        mainWin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainWin.setResizable(false);
        mainWin.setLocationRelativeTo(null);
        mainWin.setVisible(true);
        mainMenu.add(connectItem);
        mainMenu.add(disconnectItem);
        mainMenuBar.add(mainMenu);
        mainWin.setJMenuBar(mainMenuBar);

        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(chatArea);
        mainWin.add(mainPanel, BorderLayout.CENTER);

        chatArea.setEditable(false);

        inpPanel.setLayout(new BorderLayout());
        inpPanel.setPreferredSize(new Dimension(600, 40));
        inpPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        inpPanel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

        inpField.setPreferredSize(new Dimension(500, 40));
        inpField.setEditable(false);

        sendBtn.setPreferredSize(new Dimension(75, 40));
        sendBtn.setBackground(new Color(0x238636));
        sendBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        inpPanel.add(inpField, BorderLayout.WEST);
        inpPanel.add(sendBtn, BorderLayout.EAST);
        mainWin.add(inpPanel, BorderLayout.SOUTH);

        inpField.addActionListener(e -> {
            String message = inpField.getText();
            if (message != null) {
                Main.io.send(message);
                inpField.setText("");
            }
        });

        sendBtn.addActionListener(e -> {
            if (!inpField.isEditable() || inpField.getText().isEmpty())
                return;
            System.out.println(inpField.getText());
            String message = inpField.getText();
            if (message != null) {
                Main.io.send(message);
                inpField.setText("");
            }
        });

        connectItem.addActionListener(e -> {
            openConnect();
        });

        disconnectItem.addActionListener(e -> {
            if (Main.io != null)
                Main.closeConnection();
        });

        // Connect Menu

        connectWin.setTitle("Connect");
        connectWin.setSize(300, 160);
        connectWin.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        connectWin.setResizable(false);
        connectWin.setLocationRelativeTo(mainWin);

        connectPanel.setLayout(new BorderLayout());
        connectPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JLabel ipLabel = new JLabel("IP:");
        JLabel portLabel = new JLabel("Port:");

        ipField.setPreferredSize(new Dimension(200, 40));
        portField.setPreferredSize(new Dimension(200, 40));
        connectBtn.setPreferredSize(new Dimension(75, 40));

        JPanel ipPanel = new JPanel();
        ipPanel.setLayout(new BorderLayout());
        ipPanel.add(ipLabel, BorderLayout.WEST);
        ipPanel.add(ipField, BorderLayout.EAST);

        JPanel portPanel = new JPanel();
        portPanel.setLayout(new BorderLayout());
        portPanel.add(portLabel, BorderLayout.WEST);
        portPanel.add(portField, BorderLayout.EAST);

        JPanel btnPanel = new JPanel();
        btnPanel.setLayout(new BorderLayout());
        connectBtn.setBackground(new Color(0x238636));
        btnPanel.add(connectBtn, BorderLayout.CENTER);

        connectPanel.add(ipPanel, BorderLayout.NORTH);
        connectPanel.add(portPanel, BorderLayout.CENTER);
        connectPanel.add(btnPanel, BorderLayout.SOUTH);

        connectWin.add(connectPanel);

        connectBtn.addActionListener(e -> {
            if (ipField.getText().isEmpty() || portField.getText().isEmpty())
                return;

            String ip = ipField.getText();
            int port;
            try {
                port = Integer.parseInt(portField.getText());
            } catch (NumberFormatException ex) {
                connError("Port must be a number!");
                return;
            }
            Main.openConnection(ip, port);
            connectWin.setVisible(false);
        });
    }

    public void openConnect() {
        if (connectWin.isVisible())
            return;

        connectWin.setVisible(true);
    }

    public void showDialog(String message) {
        JOptionPane.showMessageDialog(mainWin, message, "Message", JOptionPane.INFORMATION_MESSAGE);
    }

    public void showError(String message) {
        JOptionPane.showMessageDialog(mainWin, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void connError(String message) {
        JOptionPane.showMessageDialog(connectWin, message, "Connection Error", JOptionPane.ERROR_MESSAGE);
    }
}