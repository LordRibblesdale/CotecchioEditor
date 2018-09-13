import javax.swing.*;
import java.awt.event.ActionEvent;

public class About extends AbstractAction {
   private UserInterface ui;
   private static String info = "Creatore di elenco per Cotecchio\nLordRibblesdale";

   About(UserInterface ui) {
      this.ui = ui;

      putValue(Action.NAME, "About...");
   }

   @Override
   public void actionPerformed(ActionEvent e) {
      JOptionPane.showMessageDialog(ui, info, "About", JOptionPane.INFORMATION_MESSAGE);
   }
}
