package checkers.core;

import java.util.ArrayList;
import java.util.List;
import checkers.core.entity.ServerInfo;
import checkers.database.Executor;
import checkers.database.table.Server;

public class ServerRegistry {
    
    private static List<Server> servers;

    public ServerRegistry() {
        load();
    }
    
    public boolean addServer(ServerInfo i) {
        Server s = new Server(i.getDomain(), i.getPortNumber(), i.getPath(), i.isSecure(), !i.isSelfSigned());
        boolean success = Executor.addServer(s);
        if (success) load();
        return success;
    }
    
    public boolean editServer(ServerInfo i) {
        Server s = getServer(i.getDomain());
        s.setDomain(i.getDomain());
        s.setPort(i.getPortNumber());
        s.setPath(i.getPath());
        s.setHttps(i.isSecure());
        s.setValidCert(!i.isSelfSigned());
        s.setVisible(i.isVisible());
        boolean success = Executor.updateServer(s);
        if (!success) load();
        return success;
    }
    
    public boolean deleteServer(String domain) {
        Server s = getServer(domain);
        boolean success = Executor.deleteServer(s);
        if (success) load();
        return success;
    }
    
    public Server getServer(String domain) {
        for (Server s : servers) {
            if (s.getDomain().equals(domain))
                return s;
        }
        return null;
    }
    
    public List<Server> getServerList() {
        return servers;
    }
    
    public ServerInfo getServerInfo(int index) {
        return getServerInfo(servers.get(index));
    }
    
    private ServerInfo getServerInfo(Server s) {
        return new ServerInfo(
                s.getDomain(), 
                Integer.toString(s.getPort()), 
                s.getPath(), 
                s.isHttps(), 
                !s.isValidCert(), 
                s.isVisible());
    }
    
    public boolean equals(ServerInfo s1, ServerInfo s2) {
        boolean answer = true;
        if (s1 == null || s2 == null) return false;
        answer &= urlEquals(s1, s2);
        answer &= s1.isSecure() == s2.isSecure();
        answer &= s1.isSelfSigned() == s2.isSelfSigned();
        answer &= s1.isVisible() == s2.isVisible();
        return answer;
    }
    
    public boolean urlEquals(ServerInfo s1, ServerInfo s2) {
        boolean answer = true;
        if (s1 == null || s2 == null) return false;
        answer &= s1.getDomain().equals(s2.getDomain());
        answer &= s1.getPort().equals(s2.getPort());
        answer &= s1.getPath().equals(s2.getPath());
        return answer;
    }
    
    public String[] getServerNames() {
        int size = servers.size();
        String[] names = new String[size];
        for (int i = 0; i < size; i++) {
            names[i] = servers.get(i).getDomain();
        }
        return names;
    }
    
    public String[] getVisibleServerNames() {
        List<Server> visibles = getVisibleServers();
        int size = visibles.size();
        String[] names = new String[size];
        for (int i = 0; i < size; i++) {
            names[i] = visibles.get(i).getDomain();
        }
        return names;
    }
    
    private List<Server> getVisibleServers() {
        List<Server> l = new ArrayList<Server>();
        for (Server s : servers) {
            if (s.isVisible()) l.add(s);
        }
        return l;
    }
    
    public String getServerUrl(int index) {
        Server s = getVisibleServers().get(index);
        return getServerUrl(s);
    }
    
    public static String getServerUrl(Server s) {
        return createServerUrl(s.getDomain(), s.getPort(), s.getPath(), s.isHttps());
    }
    
    public static String createServerUrl(String domain, int port, String path, boolean isHttps) {
        String prot = isHttps ? "https" : "http";
        return prot + "://" + domain + ":" + port + "/" + path + "/";
    }
    
    public static String createServerUrl(ServerInfo info) {
        return createServerUrl(info.getDomain(), info.getPort(), info.getPath(), info.isSecure());
    }
    
    public static String createServerUrl(String domain, String port, String path, boolean isHttps) {
        return createServerUrl(domain, Integer.parseInt(port), path, isHttps);
    }
    
    public Server getServerFromUrl(String url) {
        for (Server s : servers) {
            if (url.contains(s.getDomain()))
                return s;
        }
        return null;
    }
    
    public boolean isValidCert(int index) {
        return getVisibleServers().get(index).isValidCert();
    }
    
    private void load() {
        servers = Executor.getServers();
    }
    
}