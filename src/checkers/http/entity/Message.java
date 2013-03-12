package checkers.http.entity;

public class Message {
    
    private boolean success;
    private String message;

    public Message(String message, boolean success) {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
    
}