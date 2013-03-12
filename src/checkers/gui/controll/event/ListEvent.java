package checkers.gui.controll.event;

import checkers.gui.controll.Controller;
import checkers.http.MessageChanger;

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