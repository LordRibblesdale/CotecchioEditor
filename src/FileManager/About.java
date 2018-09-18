package FileManager;

import Interface.UserController;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class About extends AbstractAction {
   private UserController ui;
   private static String info = "Creatore di elenco per Cotecchio\nLordRibblesdale";

   public About(UserController ui) {
      this.ui = ui;

      putValue(Action.NAME, "About...");
   }

   @Override
   public void actionPerformed(ActionEvent e) {
      JOptionPane.showMessageDialog(ui, info, "About", JOptionPane.INFORMATION_MESSAGE);
   }
}
