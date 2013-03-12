package checkers.database.table;

import java.io.Serializable;
import java.util.List;

public class Log implements Serializable {

    private long id;
    private Play play;
    private int time;
    private String message;

    public Log() {
        super();
    }

    public Log(Play play, String message, int time) {
        this.play = play;
        this.message = message;
        this.time = time;
    }
    
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Play getPlay() {
        return play;
    }

    public void setPlay(Play play) {
        this.play = play;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
}