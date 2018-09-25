package Data;

import FileManager.Path;

import java.io.*;
import java.util.ArrayList;

public class Settings implements Serializable, Path {
    private static long serialVersionUID = 320L;

    private int refreshSaveRate;
    private String openedFile;
    private ArrayList<String> lastOpenedFiles;

    public Settings() {
        Settings tmp;
        try {
            ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(logPath)));
            Object object = in.readObject();
            if (object instanceof Settings) {
                tmp = (Settings) object;

                refreshSaveRate = tmp.getRefreshSaveRate();
                openedFile = tmp.getOpenedFile();
                lastOpenedFiles = tmp.getLastOpenedFiles();
            }
        } catch (IOException e) {
            refreshSaveRate = 60000;
            lastOpenedFiles = new ArrayList<>();
            openedFile = "";
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> getLastOpenedFiles() {
        return lastOpenedFiles;
    }

    public int getRefreshSaveRate() {
        return refreshSaveRate;
    }

    public String getOpenedFile() {
        return openedFile;
    }

    public void setLastOpenedFiles(ArrayList<String> lastOpenedFiles) {
        this.lastOpenedFiles = lastOpenedFiles;
    }

    public void setRefreshSaveRate(int refreshSaveRate) {
        this.refreshSaveRate = refreshSaveRate;
    }

    public void setOpenedFile(String openedFile) {
        this.openedFile = openedFile;
    }
}
