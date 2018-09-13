import javax.swing.*;
import java.awt.event.ActionEvent;

public class NewFile extends AbstractAction {
   private UserInterface ui;
   private Object[] choice = {"Save", "Discard", "Go Back"};

   NewFile(UserInterface ui) {
      this.ui = ui;

      putValue(Action.NAME, "New");
   }

   @Override
   public void actionPerformed(ActionEvent e) {
      if (ui.hasBeenSaved()) {
         ui.initialise();
      } else {
         int selection = JOptionPane.showOptionDialog(ui, "Do you want to save changes?", "Save file?",
                 JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null,
                 choice, choice[0]);

         if (choice[selection] == choice[0]) {

         } else if (choice[selection] == choice[1]) {
            ui.initialise();
         }
      }
   }
}
