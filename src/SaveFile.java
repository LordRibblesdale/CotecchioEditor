import javax.swing.*;
import java.awt.event.ActionEvent;

public class SaveFile extends AbstractAction {
   private UserInterface ui;

   SaveFile(UserInterface ui) {
      this.ui = ui;

      putValue(Action.NAME, "Save...");
   }

   @Override
   public void actionPerformed(ActionEvent e) {

   }
}
