package mill.database.table;

import java.io.Serializable;

public class Server implements Serializable {
    
    private int id, port;
    private String domain, path;
    private boolean https, validCert, visible;

    public Server() {
        this(null, 0, null);
    }

    public Server(String domain, int port, String path) {
        this(domain, port, path, false, false);
    }
    
    public Server(String domain,int port, String path, boolean https, boolean validCert) {
        this.domain = domain;
        this.port = port;
        this.path = path;
        this.https = https;
        this.validCert = validCert;
        this.visible = true;
    }

    public boolean isVisible() {
        return visible;
    }
    
    public void setVisible(boolean visible) {
        this.visible = visible;
    }
    
    public boolean isValidCert() {
        return validCert || !https;
    }
    
    public void setValidCert(boolean validCert) {
        this.validCert = validCert;
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
    
    public boolean isHttps() {
        return https;
    }

    public void setHttps(boolean https) {
        this.https = https;
    }
    
}