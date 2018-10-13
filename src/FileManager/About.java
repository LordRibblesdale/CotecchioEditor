package FileManager;

import Interface.UserController;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class About extends AbstractAction {
   private UserController ui;

   public About(UserController ui) {
      this.ui = ui;

      putValue(Action.NAME, ui.getSettings().getResourceBundle().getString("about"));
   }

   @Override
   public void actionPerformed(ActionEvent e) {
      StringBuilder info = new StringBuilder("Creatore di elenco giocatori per Cotecchio");
      info.append("\nCreator: LordRibblesdale");
      JOptionPane.showMessageDialog(ui, info, ui.getSettings().getResourceBundle().getString("about"), JOptionPane.INFORMATION_MESSAGE);
   }
}
