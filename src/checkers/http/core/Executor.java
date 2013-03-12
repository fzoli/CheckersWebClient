package checkers.http.core;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Set;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import checkers.gui.controll.Controller;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class Executor {

    private final String URL;
    private final HttpClient HTTP_CLIENT;
    private final Controller CONTROLLER;
    private boolean isConnected = false;
    private Map<Integer, HttpPost> posts = new HashMap<Integer, HttpPost>();
    
    public Executor(Controller controller, String url, boolean isValidCert) {
        URL = url;
        HTTP_CLIENT = getThreadSafeClient();
        HTTP_CLIENT.getParams().setParameter("http.connection.timeout", 1000);
        CONTROLLER = controller;
        if (!isValidCert) wrapHttpClient();
    }
    
    public void abort(int reqId) {
        if (reqId > -1) {
            HttpPost p = posts.get(reqId);
            if (p != null && !p.isAborted()) p.abort();
        }
    }
    
    private DefaultHttpClient getThreadSafeClient() {
        ThreadSafeClientConnManager cm = new ThreadSafeClientConnManager();
        cm.setDefaultMaxPerRoute(15);
        cm.setMaxTotal(15);
        return new DefaultHttpClient(cm);
    }
    
    public String getUrl() {
        return URL;
    }
    
    public boolean isConnected() {
        return isConnected;
    }
    
    private void wrapHttpClient() {
        try {
            /* Ez a legszigorúbb ellenőrzés, de mivel ehhez kell cacert file, csak a saját szerverem lenne megbízható.
             * URL path = getClass().getResource("keystore");
             * HttpClientWrapper.wrapHttpClient(HTTP_CLIENT, true, path, "nincsen");*/
            HttpClientWrapper.wrapHttpClient(HTTP_CLIENT); //így minden SSL kapcsolat megbízhatóvá válik
        }
        catch(Exception ex) {
            //ex.printStackTrace();
        }
    }

    private UrlEncodedFormEntity createFormEntity(Map<String, String> map) {
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        Set<String> keys = map.keySet();
        for (String k : keys) {
            nameValuePairs.add(new BasicNameValuePair(k, map.get(k)));
        }
        try {
            return new UrlEncodedFormEntity(nameValuePairs, "utf-8");
        }
        catch (UnsupportedEncodingException ex) {
            //ex.printStackTrace();
            return null;
        }
    }
    
    private InputStream execute(String servlet, Map<String, String> map, int reqId) {
        isConnected = true;
        UrlEncodedFormEntity entity = createFormEntity(map);
        HttpPost post = new HttpPost(URL + servlet);
        if (reqId > -1) posts.put(reqId, post);
        post.setEntity(entity);
        try {
            HttpResponse response = HTTP_CLIENT.execute(post);
            return getResponseStream(response);
        }
        catch(IOException ex) {
            isConnected = false;
            if (!post.isAborted()) CONTROLLER.receiveMessage("ConnectError");
            //ex.printStackTrace();
            return null;
        }
    }
    
    private InputStream getResponseStream(HttpResponse httpResponse) {
        HttpEntity httpEntity = httpResponse.getEntity();
        try {
            return httpEntity.getContent();
        }
        catch(IOException ex) {
            //ex.printStackTrace();
            return null;
        }
    }
    
    public String getResponse(String servlet, Map<String, String> map) {
        return getResponse(servlet, map, -1);
    }
    
    public String getResponse(String servlet, Map<String, String> map, int reqId) {
        InputStream stream = execute(servlet, map, reqId);
        if (!isConnected) return null;
        DataInputStream dis = new DataInputStream(stream);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte data;
        try {
            while ((data = dis.readByte()) != -1) {
                baos.write(data);
            }
        } catch(Exception ex) {}
        byte[] response = baos.toByteArray();
        try {
            return new String(response, "utf-8");
        }
        catch(UnsupportedEncodingException ex) {
            return null;
        }
    }
    
    public Document getXML(String servlet, Map<String, String> map, int reqId) {
        InputStream response = execute(servlet, map, reqId);
        if (!isConnected) return null;
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(response);
            doc.getDocumentElement().normalize();
            return doc;
        }
        catch (UnsupportedEncodingException ex) {}
        catch(ParserConfigurationException ex) {}
        catch(IOException ex) {}
        catch(SAXException ex) {}
        return null;
    }
    
}