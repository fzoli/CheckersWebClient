package mill.gui.controll.play.event;

import mill.gui.controll.Controller;
import mill.gui.controll.event.Event;
import mill.http.MessageChanger;

public class GameEventSignal extends Event {

    public GameEventSignal(Controller c, MessageChanger mc) {
        super(c, mc);
    }

    @Override
    protected void event() {
        getMessageChanger().waitPlayEvent();
        sendMessage("PlayEvent");
    }
    
}