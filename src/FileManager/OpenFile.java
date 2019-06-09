package FileManager;

import Data.Player;
import Interface.UserController;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.util.ArrayList;

public class OpenFile extends AbstractAction implements Path {
   private UserController ui;
   private ArrayList<Player> players;
   private ObjectInputStream input;
   private ProgressMonitor pm;
   private String path;

   public OpenFile(UserController ui) {
      this.ui = ui;

      putValue(Action.NAME, ui.getSettings().getResourceBundle().getString("load"));
      putValue(Action.LARGE_ICON_KEY, new ImageIcon(OpenFile.class.getResource("Open24.gif")));
      putValue(Action.SMALL_ICON, new ImageIcon(OpenFile.class.getResource("Open16.gif")));
      putValue(Action.SHORT_DESCRIPTION, ui.getSettings().getResourceBundle().getString("load"));
   }

   @Override
   public void actionPerformed(ActionEvent e) {
      ArrayList<?> tmp2;
      try {
         if (ui.getSettings().getOpenedFile().equals("")) {
            this.path = getFile();
         } else {
            int choice = JOptionPane.showConfirmDialog(ui, ui.getSettings().getResourceBundle().getString("openLastFile"),
                    ui.getSettings().getResourceBundle().getString("openRecent"), JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.INFORMATION_MESSAGE);

            switch (choice) {
               case JOptionPane.YES_OPTION:
                  this.path = ui.getSettings().getOpenedFile();
                  break;
               case JOptionPane.NO_OPTION:
                  this.path = getFile();
                  break;
               case JOptionPane.CANCEL_OPTION:
                  this.path = null;
                  break;
            }
         }

         if (this.path != null) {
            input = new ObjectInputStream(new BufferedInputStream(new FileInputStream(this.path)));
            Object tmp = input.readObject();
            input.close();

            if (tmp instanceof ArrayList<?>) {
               tmp2 = (ArrayList<?>) tmp;
               players = new ArrayList<>();

               for (Object o : tmp2) {
                  if (!(o instanceof Player)) {
                     throw new Exception();
                  } else {
                     players.add((Player) o);
                  }
               }

               ui.saveRecentFile(path);
               ui.prepareForInitialisation(players);
            }
         }
      } catch (FileNotFoundException e3) {
         JOptionPane.showMessageDialog(
                 ui,
                 ui.getSettings().getResourceBundle().getString("errorInsertingNameFile"),
                 "Error I/O",
                 JOptionPane.ERROR_MESSAGE);
      } catch (IOException | ClassNotFoundException e1) {
         try {
            input.close();

            DecompressibleInputStream in = new DecompressibleInputStream(new BufferedInputStream(new FileInputStream(this.path)));
            Object tmp = in.readObject();
            in.close();

            if (tmp instanceof  ArrayList<?>) {
               tmp2 = (ArrayList<?>) tmp;
               players = new ArrayList<>();

               for (Object o : tmp2) {
                  if (o instanceof Player) {
                     Player p = (Player) o;

                     players.add(new Player(p.getName(), p.getUsername(), p.getScore(), p.getPelliccions(), p.getCappottens(), p.getTotalPlays(), p.getTotalWins()));
                  }
               }

               ui.saveRecentFile(this.path);
               ui.prepareForInitialisation(players);
               JOptionPane.showMessageDialog(ui, ui.getSettings().getResourceBundle().getString("conversionMessage"),
                       ui.getSettings().getResourceBundle().getString("conversionCompleted"), JOptionPane.WARNING_MESSAGE);
               new SaveFile(ui).actionPerformed(e);
            }
         } catch (IOException | ClassNotFoundException e2) {
            e2.printStackTrace();
            JOptionPane.showMessageDialog(ui,
                    ui.getSettings().getResourceBundle().getString("errorReadingFile"),
                    "Error I/O 01_OpenFile" + e2.getStackTrace()[0].getLineNumber(),
                    JOptionPane.ERROR_MESSAGE);
         }
      } catch (Exception e2) {
         JOptionPane.showMessageDialog(
                 ui,
                 ui.getSettings().getResourceBundle().getString("errorReadingFile"),
                 "Error I/O 02_OpenFile" + e2.getStackTrace()[0].getLineNumber(),
                 JOptionPane.ERROR_MESSAGE);
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
