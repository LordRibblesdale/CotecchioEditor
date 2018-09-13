import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

public class OpenFile extends AbstractAction implements Path {
   private UserInterface ui;
   private ArrayList<Player> players;
   private ObjectInputStream input;

   OpenFile(UserInterface ui) {
      this.ui = ui;

      putValue(Action.NAME, "Load...");
   }

   @Override
   public void actionPerformed(ActionEvent e) {
      try {
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
      } catch (IOException | ClassNotFoundException e1) {
         e1.printStackTrace();
      } catch (ClassCastException e2) {
         JOptionPane.showMessageDialog(ui, "Error loading file", "Error I/O", JOptionPane.ERROR_MESSAGE);
      }
   }
}
