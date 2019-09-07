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
        CotecchioDataArray data = ui.getData();

        ObjectInputStream input = new ObjectInputStream(new BufferedInputStream(new FileInputStream(ui.getSettings().getOpenedFile())));
        CotecchioDataArray tmp = (CotecchioDataArray) input.readObject();
        ui.setUpData();

        if (tmp.getSaveNumber() == data.getSaveNumber()) {
            saveMethod(ui, data);
        } else {
            int sel = JOptionPane.showConfirmDialog(ui, ui.getSettings().getResourceBundle().getString("askOverwriteAuto"),
                ui.getSettings().getResourceBundle().getString("overwrite"), JOptionPane.YES_NO_OPTION);

            if (sel == JOptionPane.YES_OPTION) {
                saveMethod(ui, data);
            } else {

            }
        }
    }

    private void saveMethod(UserController ui, CotecchioDataArray data) {
        data.setSaveNumber(data.getSaveNumber() +1);

        try {
            ObjectOutputStream output = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(ui.getSettings().getOpenedFile())));
            output.writeObject(data);
            output.close();
        } catch (IOException e1) {
            JOptionPane.showMessageDialog(ui, e1.getMessage(), "Exception in AutoSave Class", JOptionPane.ERROR_MESSAGE);
            e1.printStackTrace();
        }

        ui.saveRecentFile(ui.getSettings().getOpenedFile());
        ui.setHasBeenSaved(true);
    }
}
