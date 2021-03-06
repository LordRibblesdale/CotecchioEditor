package FileManager;

import Data.CotecchioDataArray;
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

      putValue(Action.NAME, ui.getSettings().getResourceBundle().getString("save"));
      putValue(Action.LARGE_ICON_KEY, new ImageIcon(NewFile.class.getResource("Save.png")));
      putValue(Action.SMALL_ICON, new ImageIcon(NewFile.class.getResource("Save.png")));
      putValue(Action.SHORT_DESCRIPTION, ui.getSettings().getResourceBundle().getString("save"));
   }

   @Override
   public void actionPerformed(ActionEvent e) {
      File file;
      String dir;

      System.out.println(ui.getSettings().getOpenedFile());

      if (ui.getSettings().getOpenedFile().equals("") || !(new File(ui.getSettings().getOpenedFile())).exists()) {
         dir = Path.path;
      } else {
         dir = ui.getSettings().getOpenedFile();
      }

      file = new File(dir);

      if (!file.exists()) {
         try {
            if (file.createNewFile()) {
               saveThread(dir);
               JOptionPane.showMessageDialog(ui.getFrame(), ui.getSettings().getResourceBundle().getString("playerListSaved") + " " + dir);
            } else {
               JOptionPane.showMessageDialog(ui.getFrame(), ui.getSettings().getResourceBundle().getString("errorCreatingFile"), "Error I/O", JOptionPane.ERROR_MESSAGE);
            }
         } catch (IOException e1) {
            e1.printStackTrace();
            JOptionPane.showMessageDialog(ui.getFrame(), ui.getSettings().getResourceBundle().getString("errorCreatingFile"), "Error I/O" + e1.getStackTrace()[0].getLineNumber(), JOptionPane.ERROR_MESSAGE);
         }
      } else {
         int sel = JOptionPane.showConfirmDialog(ui.getFrame(), ui.getSettings().getResourceBundle().getString("askOverwrite"),
                 ui.getSettings().getResourceBundle().getString("overwrite"), JOptionPane.YES_NO_OPTION);

         if (sel == JOptionPane.YES_OPTION) {
            saveThread(dir);
         } else {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setAcceptAllFileFilterUsed(false);
            fileChooser.addChoosableFileFilter(new BinFilter());

            int res = fileChooser.showSaveDialog(ui.getFrame());

            if (res == JFileChooser.APPROVE_OPTION) {
               dir = fileChooser.getSelectedFile().getPath();

               if (dir.lastIndexOf(".") == -1) {
                  dir += "." + BinFilter.ext;
               }

               saveThread(dir);

            } else if (res != JFileChooser.CANCEL_OPTION) {
               JOptionPane.showMessageDialog(ui.getFrame(), ui.getSettings().getResourceBundle().getString("errorSavingFile"), "Error I/O", JOptionPane.ERROR_MESSAGE);
            }
         }
      }

      ui.getSettings().setOpenedFile(dir);

      if (e == null) {
         ui.getFrame().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      }
   }

   private void saveThread(String path) {
      ui.setUpData(false);

      CotecchioDataArray data = ui.getData();

      try {
         ObjectOutputStream output = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(path)));
         output.writeObject(data);
         output.close();
      } catch (IOException e1) {
         JOptionPane.showMessageDialog(ui.getFrame(), e1.getMessage(), "Exception", JOptionPane.ERROR_MESSAGE);
         e1.printStackTrace();
      }

      data.setSaveNumber(data.getSaveNumber() +1);

      ui.saveRecentFile(path);
      ui.setHasBeenSaved(true);
   }
}
