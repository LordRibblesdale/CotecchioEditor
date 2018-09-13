import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.util.ArrayList;

public class SaveFile extends AbstractAction implements Path {
   private UserInterface ui;
   private ArrayList<Player> playerList;
   private ObjectOutputStream output;

   SaveFile(UserInterface ui, ArrayList<Player> playerList) {
      this.ui = ui;

      putValue(Action.NAME, "Save...");
   }

   @Override
   public void actionPerformed(ActionEvent e) {
      playerList = ui.getPlayers();

      File file = new File(path);
      System.out.println(path);

      try {
         file.createNewFile();
      } catch (IOException e1) {
         e1.printStackTrace();
      }

      /*
      try {
         if (!file.createNewFile()) {
            JOptionPane.showMessageDialog(ui, "Error while creating file", "Error I/O", JOptionPane.ERROR_MESSAGE);
         }
      } catch (IOException e1) {
         e1.printStackTrace();
      }
      */

      try {
         output = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(path)));
         output.writeObject(playerList);
         output.close();
      } catch (IOException e1) {
         e1.printStackTrace();
      }
   }
}
