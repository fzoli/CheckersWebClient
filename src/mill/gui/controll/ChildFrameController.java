package mill.gui.controll;

import mill.gui.view.core.MyFrame;

public abstract class ChildFrameController extends ChildController {
    
    private final ChildController CONTROLLER;
    
    public ChildFrameController(ChildController controller) {
        super(controller);
        CONTROLLER = controller;
    }
    
    protected MyFrame getOwnerFrame() {
        return CONTROLLER.getFrame();
    }
    
}