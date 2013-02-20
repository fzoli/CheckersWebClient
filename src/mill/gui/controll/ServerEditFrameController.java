package mill.gui.controll;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import javax.swing.SwingWorker;
import mill.core.InputValidator;
import mill.core.ServerRegistry;
import mill.core.entity.ServerInfo;
import mill.database.table.Server;
import mill.gui.view.ServerEditFrame;
import mill.gui.view.core.Core;
import mill.gui.view.core.MyFrame;
import mill.gui.view.core.StatusBar;
import mill.http.MessageChanger;

public class ServerEditFrameController extends ChildFrameController {

    private ServerEditFrame frServer;
    private ServerRegistry servers;
    private ServerInfo si;
    
    public ServerEditFrameController(ChildController controller, ServerRegistry servers) {
        super(controller);
        this.servers = servers;
        initFrame();
    }
    
    @Override
    protected MyFrame getFrame() {
        return frServer;
    }

    @Override
    public void receiveMessage(String message) {
        if (message.equals("ValueChanged")) inputChanged();
        else if (message.equals("Fill")) fillFrame();
        else if (message.equals("AddServer")) doRequest(ADD);
        else if (message.equals("CreateServer")) doRequest(CREATE);
        else if (message.equals("DeletteServer")) deletteServer();
    }

    private void done() {
        frServer.done();
        updateFrameCbModel();
    }
    
    private void setErrorMessage(int code) {
        String error = "";
        switch (code) {
            case 0:
                error = "Sikertelen kapcsolódás!";
                break;
            case 1:
                error = "Már létezik ilyen című szerver!";
                break;
            case 2:
                error = "A szerver nem törölhető.";
                break;
        }
        frServer.getStatusBar().setError(error);
    }
    
    private final static int ADD = 1;
    private final static int CREATE = 2;
    
    private boolean doRequest(final int code) {
        ServerInfo i = getServerInfo();
        frServer.getStatusBar().setProgress("Kapcsolat tesztelése...");
        frServer.disableButton();
        final MessageChanger mc = new MessageChanger(this, servers.createServerUrl(i), !i.isSelfSigned());
        SwingWorker sw = new SwingWorker() {
            
            @Override
            protected Object doInBackground() throws Exception {
                boolean test = mc.testConnection();
                boolean mod = isServerUrlModified();
                if (mod && !test) {
                    setErrorMessage(0);
                    frServer.enableButton();
                }
                else {
                    frServer.getStatusBar().reset();
                    switch (code) {
                        case ADD:
                            addServer();
                            break;
                        case CREATE:
                            createServer();
                            break;
                    }
                }
                return null;
            }
            
        };
        sw.execute();
        return true;
    }
    
    private void addServer() {
        boolean success = servers.addServer(getServerInfo());
        if (success) {
            frServer.getStatusBar().setMessage("Sikeres hozzáadás.");
            done();
        }
        else setErrorMessage(1);
    }
    
    private ServerInfo getServerInfo() {
        return frServer.createServerInfo();
    }
    
    private void createServer() {
        boolean success = servers.editServer(getServerInfo());
        if (success) {
            frServer.getStatusBar().setMessage("Sikeres módosítás.");
            done();
        }
        else setErrorMessage(1);
    }
    
    private void deletteServer() {
        boolean answer = Core.showConfirmDialog(frServer, "Biztos, hogy törli a szervert?");
        if (answer) {
            if (servers.deleteServer(getServerInfo().getDomain())) {
                frServer.getStatusBar().setMessage("Törölve.");
                done();
            }
            else setErrorMessage(2);
        }
    }
    
    private void fillFrame() {
        List<Server> sl = servers.getServerList();
        si = servers.getServerInfo(frServer.getSelectedServerIndex());
        frServer.fill(si);
    }
    
    private void inputChanged() {
        setCheckbox();
        setButtonAndURL();
    }
    
    private void setButtonAndURL() {
        ServerInfo i = getServerInfo();
        String domain = i.getDomain();
        String port = i.getPort();
        String path = i.getPath();
        StatusBar sb = frServer.getStatusBar();
        if (InputValidator.isDomainValid(domain) && InputValidator.isPortValid(port) && InputValidator.isPathValid(path)) {
            if (!servers.equals(si, i) || frServer.getMode() != frServer.CREATE) frServer.enableButton();
            else frServer.disableButton();
            sb.setMessage("URL: " + servers.createServerUrl(domain, port, path, i.isSecure()));
        }
        else {
            frServer.disableButton();
            sb.reset();
        }
    }
    
    private boolean isServerUrlModified() {
        ServerInfo i = getServerInfo();
        return !servers.urlEquals(si, i);
    }
    
    private void setCheckbox() {
        ServerInfo i = getServerInfo();
        boolean secure = i.isSecure();
        boolean selfSigned = i.isSelfSigned();
        if (!secure) frServer.getSelfSignedCb().setSelected(false);
        if (selfSigned) {
            frServer.getSecureCb().setSelected(true);
            frServer.getSelfSignedCb().setSelected(true);
        }
    }
    
    private void initFrame() {
        frServer = new ServerEditFrame(this, getOwnerFrame());
        frServer.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                sendMessage("ServerEditClose");
                frServer.dispose();
            }
            
        });
        updateFrameCbModel();
        frServer.setVisible(true);
    }
    
    private void updateFrameCbModel() {
        String[] s = servers.getServerNames();
        frServer.updateModel(s);
        if (s.length == 0) frServer.disableEditToolbar();
        else frServer.enableEditToolbar();
    }
    
}