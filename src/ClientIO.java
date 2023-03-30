import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientIO {

    public Socket sock;
    public BufferedReader sin;
    public PrintWriter sout;

    boolean closing = false;

    public ClientIO (String ip, int port) {
        connect(ip, port);

        new Thread(() -> {
            while (true) {
                try {
                    String message = sin.readLine();
                    writeMsg(message);
                } catch (Exception e) {
                    if (!closing)
                        Main.gui.showError("Could not read from server.");
                    break;
                }
            }

            if (!closing)
                Main.closeConnection();
        }).start();
    }

    public void connect(String ip, int port) {
        try {
            sock = new Socket(ip, port);
            sin = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            sout = new PrintWriter(sock.getOutputStream(), true);
        } catch (IOException e) {
            Main.gui.showError("Could not connect to server.");
            return;
        }

        clearChat();
        Main.gui.inpField.setEditable(true);
        writeMsg("# Connected to " + ip + ":" + port + "");
    }

    public void send(String message) {
        sout.println(message);
    }

    public void clearChat() {
        Main.gui.chatArea.setText("");
    }

    public void writeMsg(String message) {
        Main.gui.chatArea.append(message + "\n");
    }
}
