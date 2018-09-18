package Game;

import FileManager.Path;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.*;

public class SaveGameFile extends AbstractAction implements FileManager.Path {
   private GameProgress ui;

   public SaveGameFile(GameProgress ui) {
      this.ui = ui;

      putValue(Action.NAME, "Save...");
   }

   @Override
   public void actionPerformed(ActionEvent e) {
      File file = new File(FileManager.Path.defaultGameName);

      if (!file.exists()) {
         try {
            if (file.createNewFile()) {
               saveThread(FileManager.Path.defaultGameName);
               JOptionPane.showMessageDialog(ui, "This game is saved in " + Path.defaultGameName);
            } else {
               JOptionPane.showMessageDialog(ui, "Error creating file", "Error I/O", JOptionPane.ERROR_MESSAGE);
            }
         } catch (IOException e1) {
            e1.printStackTrace();
         }
      } else {
         int sel = JOptionPane.showConfirmDialog(ui, "Do you want to overwrite?", "Overwrite?", JOptionPane.YES_NO_OPTION);

         if (sel == JOptionPane.YES_OPTION) {
            saveThread(FileManager.Path.defaultGameName);
         } else {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setAcceptAllFileFilterUsed(false);
            fileChooser.addChoosableFileFilter(new FileManager.BinFilter());

            int res = fileChooser.showSaveDialog(ui);

            if (res == JFileChooser.APPROVE_OPTION) {
               saveThread(fileChooser.getSelectedFile().getPath());
            } else {
               JOptionPane.showMessageDialog(ui, "Error saving file", "Error I/O", JOptionPane.ERROR_MESSAGE);
            }
         }
      }

      if (e == null) {
         ui.dispose();
      }
   }

   private void saveThread(String path) {
      try {
         ObjectOutputStream output = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(path)));
         output.writeObject(ui);
         output.close();
      } catch (IOException e1) {
         e1.printStackTrace();
      }
   }
}
