package mill.core.hackshield;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.SwingWorker;
import mill.gui.controll.Controller;

public class HackShield {
    
    private final Controller CONTROLLER;
    private final int START_PORT = 10001;
    private ServerSocket server;
    
    public HackShield(Controller controller) {
        CONTROLLER = controller;
        initSocketServer();
    }
    
    private String getUserName() {
        return System.getProperty("user.name");
    }
    
    private void initSocketServer() {
        if (new FileLocker().check()) {
            server = createSocketServer(START_PORT);
            startReceiveMessageFromTheAnotherApplication();
        }
        else {
            sendMessageToTheAnotherApplication(START_PORT);
            System.exit(0);
        }
    }
    
    public void dispose() {
        try {
            server.close();
        }
        catch (IOException ex) {}
    }

    public void sendMessageToTheAnotherApplication(int port) {
        boolean equals = false;
        try {
            InetAddress host = InetAddress.getLocalHost();
            Socket s = new Socket(host.getHostName(), port);
            
            ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
            oos.writeObject(getUserName());
            
            ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
            equals = (Boolean) ois.readObject();
            
            oos.close();
            ois.close();
            s.close();
        }
        catch (Exception ex) {
            //ex.printStackTrace();
        }
        if (!equals && port < 10101) sendMessageToTheAnotherApplication(++port);
    }
    
    private ServerSocket createSocketServer(int port) {
        try {
            return new ServerSocket(port);
        }
        catch(IOException ex) {
            return createSocketServer(++port);
        }
    }
    
    private void startReceiveMessageFromTheAnotherApplication() {
        SwingWorker sw = new SwingWorker() {

            @Override
            protected Object doInBackground() throws Exception {
                while (!server.isClosed()) {
                    try {
                        Socket s = server.accept();
                        
                        ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
                        String senderUsername = (String) ois.readObject();
                        boolean equals = senderUsername.equals(getUserName());
                        
                        ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
                        oos.writeObject(equals);
                        
                        ois.close();
                        oos.close();
                        s.close();
                        
                        if (equals) CONTROLLER.receiveMessage("DuplicatedApplication");
                    } catch (IOException ex) {
                        //ex.printStackTrace();
                    }
                }
                return null;
            }
        };
        sw.execute();
    }
    
}