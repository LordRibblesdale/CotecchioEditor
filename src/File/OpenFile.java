package File;

import Data.Player;
import Interface.UserInterface;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.util.ArrayList;

public class OpenFile extends AbstractAction implements Path {
   private UserInterface ui;
   private ArrayList<Player> players;
   private ObjectInputStream input;

   public OpenFile(UserInterface ui) {
      this.ui = ui;

      putValue(Action.NAME, "Load...");
   }

   @Override
   public void actionPerformed(ActionEvent e) {
      try {
         String path = getFile();

         if (path != null) {
            input = new ObjectInputStream(new BufferedInputStream(new FileInputStream(path)));
            Object tmp = input.readObject();
            input.close();

            if (tmp instanceof ArrayList<?>) {
               ArrayList<?> tmp2 = (ArrayList<?>) tmp;

               for (Object o : tmp2) {
                  if (!(o instanceof Player)) {
                     throw new ClassCastException();
                  }
               }

               players = (ArrayList<Player>) tmp2;

               ui.initialise(players);
            }
         }
      } catch (FileNotFoundException e3) {
         JOptionPane.showMessageDialog(ui, "Error inserting name file", "Error I/O", JOptionPane.ERROR_MESSAGE);
      } catch (IOException | ClassNotFoundException e1) {
         JOptionPane.showMessageDialog(ui, "Error loading file", "Error I/O", JOptionPane.ERROR_MESSAGE);
      } catch (ClassCastException e2) {
         JOptionPane.showMessageDialog(ui, "Error reading file", "Error I/O", JOptionPane.ERROR_MESSAGE);
      }
   }

   private String getFile() {
      JFileChooser fileChooser = new JFileChooser();
      fileChooser.setAcceptAllFileFilterUsed(false);
      fileChooser.addChoosableFileFilter(new BinFilter());

      int res = fileChooser.showOpenDialog(ui);

      if (res == JFileChooser.APPROVE_OPTION) {
         return fileChooser.getSelectedFile().getPath();
      } else {
         JOptionPane.showMessageDialog(ui, "Error loading file", "Error I/O", JOptionPane.ERROR_MESSAGE);
      }

      return null;
   }
}
