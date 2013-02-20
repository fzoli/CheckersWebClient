package mill.gui.controll.event;

import javax.swing.SwingWorker;
import mill.gui.controll.Controller;
import mill.http.MessageChanger;

public abstract class Event extends SwingWorker<Object, Object> {

    private final Controller CONTROLLER;
    private final MessageChanger MC;
    private boolean aborted = false;
    private boolean disposed = false;
    
    public Event(Controller controller, MessageChanger mc) {
        CONTROLLER = controller;
        MC = mc;
    }

    public void sendMessage(String message) {
        CONTROLLER.receiveMessage(message);
    }

    public void dispose() {
        disposed = true;
    }
    
    public MessageChanger getMessageChanger() {
        return MC;
    }
    
    @Override
    protected Object doInBackground() throws Exception {
        while(!disposed) {
            event();
            Thread.sleep(1);
        }
        return null;
    }
    
    protected abstract void event();
    
}