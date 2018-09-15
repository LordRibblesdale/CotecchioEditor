package File;

import Data.Player;
import Interface.UserInterface;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.util.ArrayList;

public class SaveFile extends AbstractAction implements Path {
   private UserInterface ui;

   public SaveFile(UserInterface ui) {
      this.ui = ui;

      putValue(Action.NAME, "Save...");
   }

   @Override
   public void actionPerformed(ActionEvent e) {
      File file = new File(Path.path);
      System.out.println(Path.path);

      if (!file.exists()) {
         try {
            if (file.createNewFile()) {
               saveThread(Path.path);
            } else {
               JOptionPane.showMessageDialog(ui, "Error creating file", "Error I/O", JOptionPane.ERROR_MESSAGE);
            }
         } catch (IOException e1) {
            e1.printStackTrace();
         }
      } else {
         int sel = JOptionPane.showConfirmDialog(ui, "Do you want to overwrite?", "Overwrite?", JOptionPane.YES_NO_OPTION);

         if (sel == JOptionPane.YES_OPTION) {
            saveThread(Path.path);
         } else {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setAcceptAllFileFilterUsed(false);
            fileChooser.addChoosableFileFilter(new BinFilter());

            int res = fileChooser.showSaveDialog(ui);

            if (res == JFileChooser.APPROVE_OPTION) {
               saveThread(fileChooser.getSelectedFile().getPath());
            } else {
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
         e1.printStackTrace();
      }
   }
}
