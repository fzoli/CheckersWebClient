package mill.http;

import java.awt.Point;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import mill.gui.controll.Controller;
import mill.http.core.Executor;
import mill.core.entity.GameChecker;
import mill.http.entity.GameInfo;
import mill.http.entity.Info;
import mill.http.entity.Message;
import mill.http.entity.Play;
import mill.http.entity.ValidityInfo;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

public class MessageChanger {

    private final Executor EXECUTOR;
    private final String FORM_SERVLET = "FormRequest";
    private final String XML_SERVLET = "XmlRequest";
    private final String EVENT_SERVLET = "EventRequest";
    
    public MessageChanger(Controller controller, String url, boolean isValidCert) {
        EXECUTOR = new Executor(controller, url, isValidCert);
    }
    
    public String getGameUrl() {
        return getUrl() + rulesServlet();
    }
    
    private String getUrl() {
        return EXECUTOR.getUrl();
    }
    
    private String rulesServlet() {
        return "flyordie/rules.htm";
    }
    
    public String getGameRule() {
        return EXECUTOR.getResponse(rulesServlet(), new HashMap());
    }
    
    public boolean testConnection() {
        Document doc = getXML("test");
        try {
            Element resp = doc.getDocumentElement();
            String message = resp.getAttribute("message");
            return message.equals("wellcome");
        }
        catch(Exception ex) {
            //ex.printStackTrace();
            return false;
        }
    }
    
    public ValidityInfo validateUser(String user, String password) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("user", user);
        map.put("password", password);
        Document doc = getXML("validateUser", map);
        Element resp = doc.getDocumentElement();
        boolean exists = Boolean.parseBoolean(resp.getAttribute("userExists"));
        boolean valid = Boolean.parseBoolean(resp.getAttribute("validPassword"));
        return new ValidityInfo(exists, valid);
    }
    
    public boolean isConnected() {
        return EXECUTOR.isConnected();
    }
    
    public boolean isUrl(String url) {
        return getUrl().equals(url);
    }
    
    public Message addPlay(String name, String password) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("name", name);
        map.put("password", password);
        boolean success = false;
        String message = "";
        try {
            Document doc = getXML("createGame", map);
            Element resp = doc.getDocumentElement();
            message = resp.getAttribute("message");
            success = Boolean.parseBoolean(resp.getAttribute("success"));
        }
        catch(Exception ex) {}
        return new Message(message, success);
    }

    public boolean joinPlay(String name, String password) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("game_name", name);
        map.put("game_password", password);
        boolean success = false;
        try {
            execute("join_game", map);
            success = isPlayerInGame();
        }
        catch(Exception ex) {}
        return success;
    }
    
    public void leavePlay() {
        getXML("exitGame");
    }
    
    public void moveChecker(Point from, Point to) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("fromRow", Integer.toString(from.x));
        map.put("fromCol", Integer.toString(from.y));
        map.put("toRow", Integer.toString(to.x));
        map.put("toCol", Integer.toString(to.y));
        getXML("move", map);
    }
    
    public Info getInfo() {
        Document doc = getXML("getPlayList");
        if (!isConnected()) return null;
        Element resp = doc.getDocumentElement();
        String user = resp.getAttribute("user");
        String lastAction = resp.getAttribute("lastAction");
        int timeout = Integer.parseInt(resp.getAttribute("timeout"));
        boolean isPlayerInGame = Boolean.parseBoolean(resp.getAttribute("isPlayerInGame"));
        boolean isAdmin = Boolean.parseBoolean(resp.getAttribute("isAdmin"));
        Info info = new Info(getUrl(), user, lastAction, timeout, isPlayerInGame, isAdmin);
        NodeList playes = resp.getElementsByTagName("play");
        for (int i = 0; i < playes.getLength(); i++) {
            NamedNodeMap attrs = playes.item(i).getAttributes();
            String name = getAttrValue(attrs, "name");
            String owner = getAttrValue(attrs, "owner");
            String state = getAttrValue(attrs, "state");
            boolean isProtected = Boolean.parseBoolean(getAttrValue(attrs, "protected"));
            int playerNumber = Integer.parseInt(getAttrValue(attrs, "playerNumber"));
            info.addPlay(new Play(name, owner, state, playerNumber, isProtected));
        }
        return info;
    }
    
    public GameInfo getGameInfo() {
        Document doc = getXML("getPlayData");
        NamedNodeMap data = doc.getElementsByTagName("data").item(0).getAttributes();
        boolean isPlayerInGame = Boolean.parseBoolean(getAttrValue(data, "isPlayerInGame"));
        boolean isAbleStartStop = Boolean.parseBoolean(getAttrValue(data, "isAbleStartStop"));
        boolean isGameFinished = Boolean.parseBoolean(getAttrValue(data, "isGameFinished"));
        boolean isGameStarted = Boolean.parseBoolean(getAttrValue(data, "isGameStarted"));
        boolean isGameRunning = Boolean.parseBoolean(getAttrValue(data, "isGameRunning"));
        Date startDate = createStartDate(getAttrValue(data, "startTime"));
        String gamePending = getAttrValue(data, "gamePending");
        String message = getAttrValue(data, "message");
        GameInfo info = new GameInfo(message, gamePending, startDate, isPlayerInGame, isGameFinished, isAbleStartStop, isGameRunning, isGameStarted);
        NodeList checkers = doc.getElementsByTagName("checker");
        for (int i = 0; i < checkers.getLength(); i++) {
            NamedNodeMap attrs = checkers.item(i).getAttributes();
            int row = Integer.parseInt(getAttrValue(attrs, "row"));
            int col = Integer.parseInt(getAttrValue(attrs, "col"));
            int player = Integer.parseInt(getAttrValue(attrs, "player"));
            String type = getAttrValue(attrs, "type");
            info.addChecker(new GameChecker(row, col, player, type));
        }
        return info;
    }
    
    private Date createStartDate(String startTime) {
        if (startTime.equals("null")) return null;
        return new Date(Long.parseLong(startTime));
    }
    
    private String getAttrValue(NamedNodeMap map, String attr) {
        return map.getNamedItem(attr).getNodeValue();
    }
    
    //0: siker
    //1: jelszó hiba
    //2: kapcsolódás hiba
    public int signIn(String id, String password, boolean secured) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("id", id);
        map.put("password", password);
        map.put("secured", Boolean.toString(secured));
        execute("sign_in", map, 0);
        if (!isConnected()) return 2;
        return isSignedInUser(id) ? 0 : 1;
    }
    
    public void abortSignIn() {
        EXECUTOR.abort(0);
    }
    
    public void signOut() {
        execute("sign_out");
    }
    
    public void startStopPlay() {
        getXML("start_stop");
    }

    public void giveUpPlay() {
        getXML("give_up");
    }
    
    public void delPlay(String name) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("game_name", name);
        execute("remove_game", map);
    }
    
    public void resetPlay(String name) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("game_name", name);
        execute("clear_game", map);
    }
    
    public void waitListEvent() {
        waitEvent("waitListEvent");
    }
    
    public void waitPlayEvent() {
        waitEvent("waitGameEvent");
    }
    
    public void waitImHereEvent() {
        waitEvent("ImHere");
    }
    
    private String getSignedInUserId() {
        return getInfo().getUser();
    }
    
    private boolean isPlayerInGame() {
        return getInfo().isPlayerInGame();
    }
    
    private boolean isSignedInUser(String id) {
        return getSignedInUserId().toUpperCase().equals(id.toUpperCase());
    }
    
    private Document getXML(String action) {
        return getXML(action, new HashMap<String, String>());
    }
    
    private Document getXML(String action, Map<String, String> map) {
        return getXML(action, map, -1);
    }
    
    private Document getXML(String action, Map<String, String> map, int reqId) {
        map.put("action", action);
        return EXECUTOR.getXML(XML_SERVLET, map, reqId);
    }
    
    private String execute(String action) {
        return execute(action, new HashMap<String, String>());
    }
    
    private String execute(String action, Map<String, String> map) {
        return execute(action, map, -1);
    }
    
    private String execute(String action, Map<String, String> map, int reqId) {
        map.put(action, "");
        return EXECUTOR.getResponse(FORM_SERVLET, map, reqId);
    }

    private void waitEvent(String action) {
        waitEvent(action, -1);
    }
    
    private void waitEvent(String action, int reqId) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("action", action);
        EXECUTOR.getResponse(EVENT_SERVLET, map, reqId);
    }
    
}