import javax.swing.*;
import java.awt.*;

public class ClientGUI {

    //-- Constructing the Main Window --//
    JTextArea chatArea = new JTextArea("# Please connect to a server...") {
        {
            setEditable(false);
            setLineWrap(true);
            setWrapStyleWord(true);
        }
    };

    JPanel mainPanel = new JPanel() {
        {
            setLayout(new BorderLayout());
            add(new JScrollPane(chatArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER), BorderLayout.CENTER);
        }
    };

    JTextField inpField = new JTextField() {
        {
            setEditable(false);
            addActionListener(e -> {
                if (Main.io != null)
                    Main.io.send(getText());
                setText("");
            });
        }
    };

    JButton sendBtn = new JButton("Send") {
        {
            setPreferredSize(new Dimension(75, 40));
            setBackground(new Color(0x238636));
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            addActionListener(e -> {
                if (!inpField.isEditable() || inpField.getText().isEmpty())
                    return;
                if (Main.io != null)
                    Main.io.send(inpField.getText());
                inpField.setText("");
            });
        }
    };

    JPanel inpPanel = new JPanel() {
        {
            setLayout(new BorderLayout());
            setPreferredSize(new Dimension(600, 40));
            setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            add(inpField, BorderLayout.CENTER);
            add(sendBtn, BorderLayout.EAST);
        }
    };

    JFrame mainWin = new JFrame() {
        {
            setMinimumSize(new Dimension(600, 400));
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setJMenuBar(new JMenuBar() {
                {
                    add(new JMenu("Server") {
                        {
                            add(new JMenuItem("Connect") {
                                {
                                    addActionListener(e -> {
                                        openConnect();
                                    });
                                }
                            });
                            add(new JMenuItem("Disconnect") {
                                {
                                    addActionListener(e -> {
                                        if (Main.io != null)
                                            Main.closeConnection();
                                    });
                                }
                            });
                        }
                    });
                }
            });
            add(mainPanel, BorderLayout.CENTER);
            add(inpPanel, BorderLayout.SOUTH);
        }
    };


    //-- Constructing the Connect Window --//
    JTextField ipField = new JTextField() { { setText("localhost"); }};
    JTextField portField = new JTextField() { { setText("6342"); }};
    JButton connectBtn = new JButton("Connect") {
        {
            addActionListener(e -> {
                if (Main.io != null)
                    Main.closeConnection();
                Main.io = new ClientIO(ipField.getText(), Integer.parseInt(portField.getText()));
                connectWin.dispose();
            });
        }
    };
    JPanel connectPanel = new JPanel() {
        {
            setLayout(new GridLayout(3, 1));
            add(new JPanel() {{ add(new JLabel("IP:"), BorderLayout.EAST); add(ipField, BorderLayout.WEST); }});
            add(new JPanel() {{ add(new JLabel("Port:"), BorderLayout.EAST); add(portField, BorderLayout.WEST); }});
            add(new JPanel() {{ add(connectBtn); }});
        }
    };

    JFrame connectWin = new JFrame("Connect") {
        {
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setMinimumSize(new Dimension(200, 100));
            setResizable(false);
            setLocationRelativeTo(null);
            add(connectPanel);
        }
    };

    //-- Constructor and Methods --//
    public ClientGUI(String title) {
        mainWin.setTitle(title);
        mainWin.setVisible(true);
    }

    public void openConnect() {
        if (connectWin.isVisible())
            return;

        connectWin.setVisible(true);
    }

    public void showDialog(String message) {
        JOptionPane.showMessageDialog(mainWin, message, "Information", JOptionPane.INFORMATION_MESSAGE);
    }

    public void showError(String message) {
        JOptionPane.showMessageDialog(mainWin, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void connError(String message) {
        JOptionPane.showMessageDialog(connectWin, message, "Connection Error", JOptionPane.ERROR_MESSAGE);
    }
}