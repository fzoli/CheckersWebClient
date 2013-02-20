package mill.gui.controll.list;

import javax.swing.SwingWorker;
import mill.gui.controll.Controller;
import mill.http.MessageChanger;
import mill.http.entity.Info;

public class ListDownloader extends SwingWorker {

    private final Controller C;
    private final MessageChanger MC;
    private Info info;

    public ListDownloader(Controller c, MessageChanger mc) {
        C = c;
        MC = mc;
    }

    public Info getInfo() {
        return info;
    }

    @Override
    protected Object doInBackground() throws Exception {
        info = MC.getInfo();
        C.receiveMessage("ListDownloaded");
        return null;
    }

};