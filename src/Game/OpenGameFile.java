package Game;

import FileManager.BinFilter;
import FileManager.Path;
import Interface.UserController;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.*;

public class OpenGameFile extends AbstractAction implements Path {
   private UserController ui;
   private GameProgress game;
   private ObjectInputStream input;

   public OpenGameFile(UserController ui) {
      this.ui = ui;

      putValue(Action.NAME, ui.getSettings().getResourceBundle().getString("loadLobby"));
   }

   @Override
   public void actionPerformed(ActionEvent e) {
      try {
         String path = getFile();

         if (path != null) {
            input = new ObjectInputStream(new BufferedInputStream(new FileInputStream(path)));
            Object tmp = input.readObject();
            input.close();

            if (tmp instanceof GameProgress) {
               game = (GameProgress) tmp;
               new GameProgress(ui, ui.getPlayers(), game.getPlayers(), game.getLog(), game.getUsernames());
            }
         }
      } catch (FileNotFoundException e3) {
         JOptionPane.showMessageDialog(ui, ui.getSettings().getResourceBundle().getString("errorInsertingNameFile"), "Error I/O", JOptionPane.ERROR_MESSAGE);
      } catch (IOException | ClassNotFoundException e1) {
         JOptionPane.showMessageDialog(ui, e1.getMessage(), "Error I/O", JOptionPane.ERROR_MESSAGE);
      } catch (ClassCastException e2) {
         JOptionPane.showMessageDialog(ui, ui.getSettings().getResourceBundle().getString("errorReadingFile"), "Error I/O", JOptionPane.ERROR_MESSAGE);
      } catch (Exception e1) {
         JOptionPane.showMessageDialog(ui, ui.getSettings().getResourceBundle().getString("processClosed"));
         e1.printStackTrace();
      }
   }

   private String getFile() {
      JFileChooser fileChooser = new JFileChooser();
      fileChooser.setAcceptAllFileFilterUsed(false);
      fileChooser.addChoosableFileFilter(new BinFilter());

      int res = fileChooser.showOpenDialog(ui);

      if (res == JFileChooser.APPROVE_OPTION) {
         return fileChooser.getSelectedFile().getPath();
      } else if (res != JFileChooser.CANCEL_OPTION) {
         JOptionPane.showMessageDialog(ui, ui.getSettings().getResourceBundle().getString("errorLoadingFile"), "Error I/O", JOptionPane.ERROR_MESSAGE);
      }

      return null;
   }
}
