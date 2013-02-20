package mill.gui.controll.play.entity;

import java.text.SimpleDateFormat;
import java.util.Date;

public class GameKey implements Comparable {
    
    private String name, server, user;
    private DateInfo dateInfo;
    private DateKey dateKey;
    private Date startDate;
    private int type;

    public static final int SERVER = 0;
    public static final int USER = 1;
    public static final int PLAY = 2;
    public static final int DATE = 3;
    public static final int KEY = 4;

    public GameKey(String server) {
        this(null, null, server, null, SERVER);
    }
    
    public GameKey(String user, String server) {
        this(null, user, server, null, USER);
    }
    
    public GameKey(String name, String user, String server) {
        this(name, user, server, null, PLAY);
    }
    
    public GameKey(String name, String user, String server, DateKey dateKey) {
        this(name, user, server, null, DATE);
        this.dateKey = dateKey;
    }
    
    public GameKey(String name, String user, String server, Date startDate) {
        this(name, user, server, startDate, KEY);
        if (startDate != null) dateInfo = new DateInfo(startDate);
    }

    private GameKey(String name, String user, String server, Date startDate, int type) {
        this.name = name;
        this.server = server;
        this.user = user;
        this.startDate = startDate;
        this.type = type;
    }

    @Override
    public boolean equals(Object obj) {
        return compareTo(obj) == 0;
    }
    
    @Override
    public int compareTo(Object o) {
        try {
            GameKey gk = (GameKey) o;
            if (gk.getType() != getType()) return -1;
            boolean b = true;
            b &= gk.getServer().equals(getServer());
            if (getType() == KEY) b &= gk.getStartDate().equals(getStartDate());
            if (getType() == DATE) b &= gk.getDateKey().compareTo(getDateKey()) == 0;
            if (getType() != SERVER) b &= gk.getUser().equals(getUser());
            if (getType() != SERVER && getType() != USER) b &= gk.getName().equals(getName());
            return b ? 0 : -1;
        }
        catch(Exception ex) {
            return -1;
        }
    }

    public String getName() {
        return name;
    }

    public String getServer() {
        return server;
    }

    public String getUser() {
        return user;
    }

    public Date getStartDate() {
        return startDate;
    }
    
    public DateInfo getDateInfo() {
        return dateInfo;
    }
    
    public DateKey getDateKey() {
        return dateKey;
    }
    
    @Override
    public String toString() {
        switch (getType()) {
            case SERVER:
                return getServer();
            case USER:
                return getUser();
            case PLAY:
                return getName();
            case DATE:
                return getDateKey().toString();
            case KEY:
                return getTimeString(startDate);
            default:
                return super.toString();
        }
    }
    
    public int getType() {
        return type;
    }
    
    private static String getTimeString(Date date) {
        if (date != null) {
            SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
            return df.format(date);
        }
        return "";
    }
    
}