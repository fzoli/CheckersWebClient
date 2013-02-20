package mill.gui.controll.event;

import mill.gui.controll.Controller;
import mill.http.MessageChanger;

public class ListEvent extends Event {
    
    public ListEvent(Controller c, MessageChanger mc) {
        super(c, mc);
    }

    @Override
    protected void event() {
        getMessageChanger().waitListEvent();
        sendMessage("ListEvent");
    }

}