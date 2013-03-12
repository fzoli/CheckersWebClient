package checkers.gui.controll.play.event;

import checkers.gui.controll.Controller;
import checkers.gui.controll.event.Event;
import checkers.http.MessageChanger;

public class SignSender extends Event {

    public SignSender(Controller c, MessageChanger mc) {
        super(c, mc);
    }

    @Override
    protected void event() {
        getMessageChanger().waitImHereEvent();
    }
    
}