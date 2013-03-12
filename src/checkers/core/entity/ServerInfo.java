package checkers.core.entity;

public class ServerInfo {

    private String domain, path;
    private String port;
    private boolean secure, selfSigned, visible;
    
    public ServerInfo(String domain, String port, String path, boolean secure, boolean selfSigned, boolean visible) {
        this.domain = domain;
        this.port = port;
        this.path = path;
        this.secure = secure;
        this.selfSigned = selfSigned;
        this.visible = visible;
    }

    public String getDomain() {
        return domain;
    }

    public String getPath() {
        return path;
    }

    public String getPort() {
        return port;
    }

    public int getPortNumber() {
        return Integer.parseInt(getPort());
    }
    
    public boolean isSecure() {
        return secure;
    }

    public boolean isSelfSigned() {
        return selfSigned;
    }

    public boolean isVisible() {
        return visible;
    }
    
}