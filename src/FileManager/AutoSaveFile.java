package FileManager;

import Data.CotecchioDataArray;
import Data.Player;
import Interface.UserController;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;

public class AutoSaveFile {
    public AutoSaveFile(UserController ui) throws IOException, ClassNotFoundException {
        save(ui);
    }

    private void save(UserController ui) throws IOException, ClassNotFoundException {
        ui.setUpData(false);
        CotecchioDataArray data = ui.getData();

        ObjectInputStream input = new ObjectInputStream(new BufferedInputStream(new FileInputStream(ui.getSettings().getOpenedFile())));
        CotecchioDataArray tmp = (CotecchioDataArray) input.readObject();

        if (tmp.getSaveNumber() == data.getSaveNumber()) {
            saveMethod(ui, data, ui.getSettings().getOpenedFile());
        } else {
            int sel = JOptionPane.showConfirmDialog(ui, ui.getSettings().getResourceBundle().getString("askOverwriteAuto"),
                ui.getSettings().getResourceBundle().getString("overwrite"), JOptionPane.YES_NO_OPTION);

            if (sel == JOptionPane.YES_OPTION) {
                saveMethod(ui, data, ui.getSettings().getOpenedFile());
            } else {
                String dir = getDir(ui);

                if (dir != null) {
                    saveMethod(ui, data, dir);
                }
            }
        }
    }

    private void saveMethod(UserController ui, CotecchioDataArray data, String location) {
        try {
            ObjectOutputStream output = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(location)));
            output.writeObject(data);
            output.close();
        } catch (IOException e1) {
            JOptionPane.showMessageDialog(ui, e1.getMessage(), "Exception in AutoSave Class", JOptionPane.ERROR_MESSAGE);
            e1.printStackTrace();
        }

        data.setSaveNumber(data.getSaveNumber() +1);

        ui.saveRecentFile(location);
        ui.setHasBeenSaved(true);
    }

    private String getDir(UserController ui) {
        String dir;
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.addChoosableFileFilter(new BinFilter());

        int res = fileChooser.showSaveDialog(ui);

        if (res == JFileChooser.APPROVE_OPTION) {
            dir = fileChooser.getSelectedFile().getPath();

            if (dir.lastIndexOf(".") == -1) {
                dir += "." + BinFilter.ext;
            }
        } else {
            dir = null;
        }

        return dir;
    }
}
