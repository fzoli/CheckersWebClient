package checkers.gui.view;

import java.io.Reader;
import java.io.StringReader;
import javax.swing.JFrame;
import checkers.gui.controll.Controller;
import checkers.gui.view.core.ChildFrame;
import org.lobobrowser.html.UserAgentContext;
import org.lobobrowser.html.gui.HtmlPanel;
import org.lobobrowser.html.parser.DocumentBuilderImpl;
import org.lobobrowser.html.parser.InputSourceImpl;
import org.lobobrowser.html.test.SimpleHtmlRendererContext;
import org.lobobrowser.html.test.SimpleUserAgentContext;
import org.w3c.dom.Document;

public class GameRulesFrame extends ChildFrame {

    private HtmlPanel panel;
    
    public GameRulesFrame(Controller controller, JFrame owner) {
        super(controller, owner);
    }

    public void updateHtmlCode(String code, String url) {
        UserAgentContext ucontext = new SimpleUserAgentContext();
        SimpleHtmlRendererContext rcontext = new SimpleHtmlRendererContext(panel, ucontext);
        DocumentBuilderImpl dbi = new DocumentBuilderImpl(ucontext, rcontext);
        Reader reader = new StringReader(code);
        try {
            Document document = dbi.parse(new InputSourceImpl(reader, url));
            panel.setDocument(document, rcontext);
        }
        catch(Exception ex) {}
    }
    
    @Override
    protected void initComponents() {
        panel = new HtmlPanel();
        getContentPane().add(panel);
        setTitle("Játékszabály");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(600, 400);
        setVisible(true);
    }
    
}