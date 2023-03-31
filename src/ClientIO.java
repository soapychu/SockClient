import java.io.*;
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
                    if (message == null) {
                        Main.gui.showError("Server terminated connection without warning!");
                        break;
                    }
                    processMsg(message);
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
            Main.gui.connError("Could not connect to server.");
            return;
        }

        clearChat();
        Main.gui.inpField.setEditable(true);
        processMsg("# Connected to " + ip + ":" + port + "");
    }

    public void send(String message) {
        sout.println(message);
    }

    public void clearChat() {
        Main.gui.chatArea.setText("");
    }

    public void processMsg(String message) {
        if (message.startsWith("i")) {
            Main.gui.showDialog(message.substring(2));
        } else if (message.startsWith("!")) {
            Main.gui.showError(message.substring(2));
        } else
            Main.gui.chatArea.append(message + "\n");
    }
}
