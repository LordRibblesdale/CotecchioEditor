package Interface;

import Data.HistoryData;
import FileManager.Path;
import FileManager.SaveFile;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.util.ArrayList;

public class HistoryButton extends AbstractAction {
    private UserController ui;

    HistoryButton(UserController ui) {
        this.ui = ui;

        putValue(Action.NAME, ui.getSettings().getResourceBundle().getString("historyCotecchio"));
        putValue(Action.LARGE_ICON_KEY, new ImageIcon(HistoryButton.class.getResource("Book.png")));
        putValue(Action.SMALL_ICON, new ImageIcon(HistoryButton.class.getResource("Book.png")));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String fileName = "history.sdc";
        File f = new File(Path.history + fileName);

        if (f.exists()) {
            ObjectInputStream ois = null;

            try {
                ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(f)));
                Object obj = ois.readObject();
                ois.close();

                if (obj instanceof ArrayList<?>) {
                    new HistoryDialog(ui, ui.getSettings().getResourceBundle().getString("generalHistory"), fileName, (ArrayList<HistoryData>) obj);
                }
            } catch (IOException | ClassNotFoundException ex) {
                ex.printStackTrace();
            }
        } else {
            new HistoryDialog(ui, ui.getSettings().getResourceBundle().getString("generalHistory"), fileName, null);
        }
    }
}
