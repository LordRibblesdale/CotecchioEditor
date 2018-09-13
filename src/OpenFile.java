import javax.swing.*;
import java.awt.event.ActionEvent;

public class OpenFile extends AbstractAction {
   private UserInterface ui;

   OpenFile(UserInterface ui) {
      this.ui = ui;

      putValue(Action.NAME, "Load...");
   }

   @Override
   public void actionPerformed(ActionEvent e) {

   }
}
