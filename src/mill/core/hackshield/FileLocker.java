package mill.core.hackshield;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

public class FileLocker {

    private static File f;
    private static FileChannel channel;
    private static FileLock lock;
    private static boolean check;
    private static ShutdownHook shutdownHook;
    private static boolean exit = false;

    public FileLocker() {
        init();
    }

    private String getUserHome() {
        return new File(System.getProperty("user.home")).toURI().getPath();
    }

    private File getLockFile() {
        return new File(getUserHome() + "MillClient.lock");
    }

    private void init() {
        if (f == null) {
            check = true;
            try {
                f = getLockFile();
                if (f.canWrite()) { //LINUX fix
                    check = false;
                    return;
                }
                // ellenőrzi, létezik-e a lock fájl
                if (f.exists()) {
                    // ha létezik, megpróbálja törölni
                    f.delete();
                }
                // a fájl zárolásának megpróbálása
                channel = new RandomAccessFile(f, "rw").getChannel();
                lock = channel.tryLock();
                if (lock == null) {
                    // más alkalmazás által zárolt a fájl
                    channel.close();
                    check = false;
                }
                // beállítja, hogy az alkalmazás leállításakor a lock fájl törlődjön
                shutdownHook = new ShutdownHook();
                Runtime.getRuntime().addShutdownHook(shutdownHook);
                startCheckLockFile();
            } catch (IOException e) {
                throw new RuntimeException("Could not start process.", e);
            }
        }
    }

    public boolean check() {
        return check;
    }
    
    private void startCheckLockFile() {
        new Thread() {

            @Override
            public void run() {
                //amíg nem zárul be a program:
                while (!exit) {
                    try {
                        if (!f.exists()) { //ha törölték kézzel a lock fájlt, újrainicializálás:
                            f = null;
                            Runtime.getRuntime().removeShutdownHook(shutdownHook); //régi objektumok felszabadítása szükségtelen
                            init();
                            break;
                        }
                        Thread.sleep(500); // fél másodperc várakozás
                    }
                    catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            }
            
        }.start();
    }
    
    private class ShutdownHook extends Thread {

        @Override
        public void run() {
            exit = true;
            unlockFile();
        }
    }

    private void unlockFile() {
        // fájl felszabadítása és törlése
        try {
            if (lock != null) {
                lock.release();
                channel.close();
                f.delete();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}