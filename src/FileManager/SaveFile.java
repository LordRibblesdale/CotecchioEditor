package FileManager;

import Data.Player;
import Interface.UserController;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.util.ArrayList;

public class SaveFile extends AbstractAction implements Path {
   private UserController ui;

   public SaveFile(UserController ui) {
      this.ui = ui;

      putValue(Action.NAME, "Save...");
   }

   @Override
   public void actionPerformed(ActionEvent e) {
      File file;
      String dir;

      if (ui.getSettings().getOpenedFile().equals("")) {
         dir = Path.path;
      } else {
         dir = ui.getSettings().getOpenedFile();
      }

      file = new File(dir);

      if (!file.exists()) {
         try {
            if (file.createNewFile()) {
               saveThread(dir);
               JOptionPane.showMessageDialog(ui, "Players list is saved in " + dir);
            } else {
               JOptionPane.showMessageDialog(ui, "Error creating file", "Error I/O", JOptionPane.ERROR_MESSAGE);
            }
         } catch (IOException e1) {
            e1.printStackTrace();
         }
      } else {
         int sel = JOptionPane.showConfirmDialog(ui, "Do you want to overwrite?", "Overwrite?", JOptionPane.YES_NO_OPTION);

         if (sel == JOptionPane.YES_OPTION) {
            saveThread(dir);
         } else {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setAcceptAllFileFilterUsed(false);
            fileChooser.addChoosableFileFilter(new BinFilter());

            int res = fileChooser.showSaveDialog(ui);

            if (res == JFileChooser.APPROVE_OPTION) {
               saveThread(fileChooser.getSelectedFile().getPath());
            } else if (res != JFileChooser.CANCEL_OPTION) {
               JOptionPane.showMessageDialog(ui, "Error saving file", "Error I/O", JOptionPane.ERROR_MESSAGE);
            }
         }
      }

      if (e == null) {
         ui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      }
   }

   private void saveThread(String path) {
      ArrayList<Player> playerList = ui.getPlayers();

      try {
         ObjectOutputStream output = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(path)));
         output.writeObject(playerList);
         output.close();
      } catch (IOException e1) {
         JOptionPane.showMessageDialog(ui, e1.getMessage(), "Exception", JOptionPane.ERROR_MESSAGE);
         e1.printStackTrace();
      }

      ui.saveRecentFile(path);
      ui.setHasBeenSaved(true);
   }
}
