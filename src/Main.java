import com.formdev.flatlaf.FlatDarkLaf;

public class Main {

    public static ClientGUI gui;
    public static ClientIO io;

    public static void main(String[] args) {
        System.setProperty("apple.awt.application.appearance", "system");
        FlatDarkLaf.setup();

        gui = new ClientGUI("Chat Client");
    }

    public static void openConnection(String ip, int port) {
        io = new ClientIO(ip, port);
    }

    public static void closeConnection() {
        io.closing = true;
        try {
            io.sout.close();
            io.sock.close();
        } catch (Exception e) {
            gui.showError("Could not close socket.");
        }
        io.clearChat();
        gui.inpField.setEditable(false);
        io.writeMsg("# Please connect to a server...");
        io = null;
    }
}
