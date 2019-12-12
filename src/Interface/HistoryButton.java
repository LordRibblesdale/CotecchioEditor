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
    private String file;

    HistoryButton(UserController ui, String file) {
        this.ui = ui;
        this.file = file;

        putValue(Action.NAME, ui.getSettings().getResourceBundle().getString("historyCotecchio"));
        putValue(Action.LARGE_ICON_KEY, new ImageIcon(HistoryButton.class.getResource("Book.png")));
        putValue(Action.SMALL_ICON, new ImageIcon(HistoryButton.class.getResource("Book.png")));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
      if (!ui.hasBeenSaved()) {
        int result = JOptionPane.showConfirmDialog(ui.getFrame(),
            ui.getSettings().getResourceBundle().getString("saveBeforeClosing"),
            ui.getSettings().getResourceBundle().getString("exitConfirmation"),
            JOptionPane.YES_NO_OPTION);

        if (result == JOptionPane.YES_OPTION) {
          new SaveFile(ui).actionPerformed(null);

          File f = new File(Path.history + file);

          if (f.exists()) {
            ObjectInputStream ois;
            try {
              ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(f)));
              System.out.println(f.getPath());
              Object obj = ois.readObject();
              ois.close();
              if (obj instanceof ArrayList<?>) {
                new HistoryDialog(ui, ui.getSettings().getResourceBundle().getString("generalHistory"), file, (ArrayList<HistoryData>) obj);
              } else if (obj == null) {
                new HistoryDialog(ui, ui.getSettings().getResourceBundle().getString("generalHistory"), file, null);
              }
            } catch (IOException | ClassNotFoundException ex) {
              ex.printStackTrace();
            }
          } else {
            new HistoryDialog(ui, ui.getSettings().getResourceBundle().getString("generalHistory"), file, null);
          }
        }
      } else {
        File f = new File(Path.history + file);

        if (f.exists()) {
          ObjectInputStream ois;
          try {
            ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(f)));
            System.out.println(f.getPath());
            Object obj = ois.readObject();
            ois.close();
            if (obj instanceof ArrayList<?>) {
              new HistoryDialog(ui, ui.getSettings().getResourceBundle().getString("generalHistory"), file, (ArrayList<HistoryData>) obj);
            } else if (obj == null) {
              new HistoryDialog(ui, ui.getSettings().getResourceBundle().getString("generalHistory"), file, null);
            }
          } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
          }
        } else {
          new HistoryDialog(ui, ui.getSettings().getResourceBundle().getString("generalHistory"), file, null);
        }
      }
    }
}
