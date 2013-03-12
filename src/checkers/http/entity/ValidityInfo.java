package checkers.http.entity;

public class ValidityInfo {
    private boolean exists;
    private boolean valid;

    public ValidityInfo(boolean exists, boolean valid) {
        this.exists = exists;
        this.valid = valid;
    }

    public boolean isExists() {
        return exists;
    }

    public boolean isValid() {
        return valid;
    }
    
}