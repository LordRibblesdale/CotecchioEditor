package Data;

import FileManager.Path;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;

public class Settings implements Serializable, Path {
    private static long serialVersionUID = 320L;

    private int refreshSaveRate;
    private String openedFile;

    public Settings() {
        Settings tmp;
        try {
            ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(setPath)));
            Object object = in.readObject();
            in.close();

            if (object instanceof Settings) {
                tmp = (Settings) object;

                refreshSaveRate = tmp.getRefreshSaveRate();
                openedFile = tmp.getOpenedFile();
            }
        } catch (IOException e) {
            refreshSaveRate = 60000;
            openedFile = "";

            try {
                ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(setPath)));
                out.writeObject(Settings.this);
                out.close();
            } catch (IOException e1) {
                JOptionPane.showMessageDialog(null, "Error saving settings file", "Error I/O", JOptionPane.ERROR_MESSAGE);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public int getRefreshSaveRate() {
        return refreshSaveRate;
    }

    public String getOpenedFile() {
        return openedFile;
    }

    public void setRefreshSaveRate(int refreshSaveRate) {
        this.refreshSaveRate = refreshSaveRate;
    }

    public void setOpenedFile(String openedFile) {
        this.openedFile = openedFile;
    }
}
