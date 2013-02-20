package mill.database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import mill.database.entity.UserData;
import mill.database.entity.UserPasswordData;
import mill.database.table.Checker;
import mill.database.table.Log;
import mill.database.table.Play;
import mill.database.table.Server;
import mill.database.table.User;
import mill.database.table.UserPassword;
import mill.gui.controll.play.entity.GameData;
import mill.gui.controll.play.entity.GameKey;
import mill.gui.controll.play.entity.GameLogData;
import mill.core.entity.GameChecker;
import mill.gui.controll.play.entity.DateInfo;
import mill.gui.controll.play.entity.DateKey;
import org.apache.commons.codec.digest.DigestUtils;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

public class Executor {
    
    public static boolean isUserLogEnabled(String user) {
        return false;
    }
    
    public static Integer getLastGameTime(GameKey gk) {
        Play play = getPlay(gk);
        if (play == null) return 0;
        Map<String, Object> args = new HashMap<String, Object>();
        String query = "select max(time) from Log where play = :play";
        args.put("play", play);
        return (Integer) getFirstObject(getQueryList(query, args));
    }
    
    public static boolean addPlay(GameKey gk) {
        if (getPlay(gk) == null)
            return addObject(new Play(getServer(gk.getServer()), getUser(gk.getUser()), gk.getName(), gk.getStartDate()));
        else return false;
    }
    
    public static GameData getGameData(GameKey gk) {
        Play play = getPlay(gk);
        if (play == null) return null;
        List<Log> logs = getLogs(play);
        List<GameLogData> logDatas = new ArrayList<GameLogData>();
        for (Log log : logs)
            logDatas.add(new GameLogData(getGameCheckers(log), log.getMessage(), log.getTime()));
        return new GameData(logDatas);
    }
    
    public static boolean addGameLog(GameKey gk, GameLogData gld) {
        Play play = getPlay(gk);
        if (play == null) return false;
        Log log = new Log(play, gld.getMessage(), gld.getTime());
        addObject(log);
        List<GameChecker> checkers = gld.getCheckers();
        for (GameChecker c : checkers) {
            Checker checker = new Checker(log, c.getType(), c.getPlayer(), c.getRow(), c.getCol());
            addObject(checker);
        }
        return true;
    }
    
    public static boolean delGame(GameKey gk) {
        return delGame(getPlayes(gk));
    }
    
    private static boolean delGame(List<Play> playes) {
        try {
            for (Play play : playes) {
                if (play == null) break;
                List<Log> logs = getLogs(play);
                for (Log log : logs)
                    delLog(log);
                delObject(play);
            }
        } catch(Exception ex) {}
        return true;
    }
    
    private static void delLog(Log log) {
        List<Checker> checkers = getCheckers(log);
        for (Checker c : checkers)
            delObject(c);
        delObject(log);
    }
    
    private static List<GameChecker> getGameCheckers(Log log) {
        List<Checker> checkers = getCheckers(log);
        List<GameChecker> gameCheckers = new ArrayList<GameChecker>();
        for (Checker c : checkers)
            gameCheckers.add(new GameChecker(c.getRow(), c.getCol(), c.getPlayer(), c.getType()));
        return gameCheckers;
    }
    
    private static List<Checker> getCheckers(Log log) {
        Map<String, Object> args = new HashMap<String, Object>();
        String query = "from Checker where log = :log";
        args.put("log", log);
        return getQueryList(query, args);
    }
    
    private static List<Log> getLogs(Play play) {
        Map<String, Object> args = new HashMap<String, Object>();
        String query = "from Log where play = :play";
        args.put("play", play);
        return getQueryList(query, args);
    }
    
    private static Play getPlay(GameKey gk) {
        Map<String, Object> args = new HashMap<String, Object>();
        String query = "from Play where name = :name and start = :start and server = :server and user = :user";
        args.put("name", gk.getName());
        args.put("start", gk.getStartDate());
        args.put("server", getServer(gk.getServer()));
        args.put("user", getUser(gk.getUser()));
        return (Play) getFirstObject(getQueryList(query, args));
    }
    
    private static List<Play> getGamePlayes(GameKey gk) {
        Map<String, Object> args = new HashMap<String, Object>();
        String query = "from Play where name = :name and server = :server and user = :user";
        args.put("name", gk.getName());
        args.put("server", getServer(gk.getServer()));
        args.put("user", getUser(gk.getUser()));
        return getQueryList(query, args);
    }
    
    private static List<Play> getPlayes(GameKey gk) {
        Map<String, Object> args = new HashMap<String, Object>();
        List<Play> playes = new ArrayList<Play>();;
        String query;
        switch(gk.getType()) {
            case GameKey.KEY:
                playes.add(getPlay(gk));
                break;
            case GameKey.USER:
                query = "from Play where server = :server and user = :user";
                args.put("server", getServer(gk.getServer()));
                args.put("user", getUser(gk.getUser()));
                playes = getQueryList(query, args);
                break;
            case GameKey.PLAY:
                playes = getGamePlayes(gk);
                break;
            case GameKey.DATE:
                List<Play> games = getGamePlayes(gk);
                DateKey dk = gk.getDateKey();
                DateInfo di;
                switch (dk.getType()) {
                    case DateKey.YEAR:
                        for (Play p : games) {
                            di = new DateInfo(p.getStart());
                            if (di.getYear() == dk.getYear())
                                playes.add(p);
                        }
                        break;
                    case DateKey.MONTH:
                        for (Play p : games) {
                            di = new DateInfo(p.getStart());
                            if (di.getYear() == dk.getYear() && di.getMonth() == dk.getMonth())
                                playes.add(p);
                        }
                        break;
                    case DateKey.DAY:
                        for (Play p : games) {
                            di = new DateInfo(p.getStart());
                            if (di.getYear() == dk.getYear() && di.getMonth() == dk.getMonth() && di.getDay() == dk.getDay())
                                playes.add(p);
                        }
                        break;
                }
                break;
        }
        return playes;
    }
    
    public static void setUserPassVisibility(String user, boolean showPassword) {
        User u = getUser(user);
        if (u == null) return;
        u.setShowPassword(showPassword);
        updateObject(u);
    }
    
    public static void setUserLog(String user, boolean enableLog) {
        User u = getUser(user);
        if (u == null) return;
        u.setEnableLog(enableLog);
        if (!enableLog) u.setShowPassword(false);
        updateObject(u);
    }
    
    public static boolean isHashEquals(String value, String hash) {
            String s = encrypt(value);
            return s.equals(hash);
    }
    
    public static List<GameKey> getGameKeyList() {
        List<Play> playes = getPlayList();
        List<GameKey> games = new ArrayList<GameKey>();
        for (Play p : playes) {
            games.add(new GameKey(p.getName(), p.getUser().getName(), p.getServer().getDomain(), p.getStart()));
        }
        return games;
    }
    
    public static List<UserData> getUserDataList() {
        List<UserData> udl = new ArrayList<UserData>();
        List<User> users = getUserList();
        for (User u : users) {
            Map<String, UserPasswordData> updm = getPasswordDataMap(u.getName());
            udl.add(new UserData(u.getName(), updm, u.isShowPassword(), u.isEnableLog()));
        }
        return udl;
    }
    
    private static Map<String, UserPasswordData> getPasswordDataMap(String name) {
        Map<String, UserPasswordData> updm = new HashMap<String, UserPasswordData>();
        List<UserPassword> upl = getPasswordList(name);
        for (UserPassword up : upl)
            updm.put(up.getServer().getDomain(), new UserPasswordData(up.getPassword(), up.getPasswordLength()));
        return updm;
    }
    
    private static List<UserPassword> getPasswordList(String name) {
        Map<String, Object> args = new HashMap<String, Object>();
        String query = "from UserPassword where user = :user";
        args.put("user", getUser(name));
        return getQueryList(query, args);
    }
    
    private static List<Play> getPlayList() {
        return getQueryList("from Play play");
    }
    
    private static List<User> getUserList() {
        return getQueryList("from User user");
    }
    
    public static List<Server> getServers() {
        return getQueryList("from Server server");
    }
    
    public static boolean addUser(String username, boolean showPassword) {
        return addObject(new User(username, showPassword));
    }
    
    public static boolean setUserPassword(String server, String user, String password) {
        UserPassword up = getUserPassword(server, user);
        if (up == null) {
            User u = getUser(user);
            if (u == null) return true;
            up = new UserPassword(getServer(server), u, encrypt(password), password.length());
            return addObject(up);
        } else {
            up.setPassword(encrypt(password));
            up.setPasswordLength(password.length());
            return updateObject(up);
        }
    }
    
    private static UserPassword getUserPassword(String server, String user) {
        Map<String, Object> args = new HashMap<String, Object>();
        String query = "from UserPassword where server = :srvr and user = :usr";
        args.put("srvr", getServer(server));
        args.put("usr", getUser(user));
        return (UserPassword) getFirstObject(getQueryList(query, args));
    }
    
    private static User getUser(String name) {
        Map<String, Object> args = new HashMap<String, Object>();
        String query = "from User where lower(name) = lower(:name)";
        args.put("name", name);
        return (User) getFirstObject(getQueryList(query, args));
    }
    
    private static Server getServer(String domain) {
        Map<String, Object> args = new HashMap<String, Object>();
        String query = "from Server where domain = :domain";
        args.put("domain", domain);
        return (Server) getFirstObject(getQueryList(query, args));
    }
    
    public static boolean addServer(Server s) {
        return addObject(s);
    }
    
    public static boolean updateServer(Server s) {
        return updateObject(s);
    }
    
    public static boolean deleteServer(Server s) {
        if (!isServerRemoveable(s)) return false;
        prepareServerDelete(s);
        return delObject(s);
    }
    
    private static void prepareServerDelete(Server s) {
        Session session = createSession();
        String hql = "delete from UserPassword where server = :server";
        Query query = session.createQuery(hql);
        query.setParameter("server", s);
        query.executeUpdate();
    }
    
    private static boolean isServerRemoveable(Server s) {
        Criteria criteria = createSession().createCriteria(Play.class);
        criteria.setProjection(Projections.rowCount());
        criteria.add(Restrictions.eq("server", s));
        return ((Integer)criteria.list().get(0)).intValue() == 0; 
    }
    
    private static final int ADD = 0;
    private static final int EDIT = 1;
    private static final int DELETE = 2;
    
    private static boolean addObject(Object object) {
        return manageObject(object, ADD);
    }

    private static boolean updateObject(Object object) {
        return manageObject(object, EDIT);
    }
    
    private static boolean delObject(Object object) {
        return manageObject(object, DELETE);
    }
    
    private static boolean manageObject(Object object, int code) {
        Session sess = createSession();
        Transaction t = sess.beginTransaction();
        
        switch (code) {
            case ADD:
                sess.save(object);
                break;
            case EDIT:
                sess.update(object);
                break;
            case DELETE:
                sess.delete(object);
        }
        
        try {
            t.commit();
        }
        catch (HibernateException ex) {
            t.rollback();
            return false;
        }
        sess.close();
        return true;
    }
    
    private static List getQueryList(String q) {
        Map<String, Object> map = new HashMap<String, Object>();
        return getQueryList(q, map);
    }
    
    private static List getQueryList(String q, Map<String, Object> args) {
        Session sess = createSession();
        Query query = sess.createQuery(q);
        Set<String> keys = args.keySet();
        for (String s : keys)
            query.setParameter(s, args.get(s));
        return query.list();
    }
    
    private static Session createSession() {
        SessionFactory factory = HibernateUtil.getSessionFactory();
        return factory.openSession();
    }
    
    private static String encrypt(String str) {
        if (str == null) return null;
        return DigestUtils.sha256Hex(str);
    }
    
    private static Object getFirstObject(List l) {
        if (l.isEmpty()) return null;
        return l.get(0);
    }
    
}