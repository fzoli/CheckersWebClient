package checkers.gui.controll.list;

import javax.swing.SwingWorker;
import checkers.gui.controll.Controller;
import checkers.http.MessageChanger;
import checkers.http.entity.Info;

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