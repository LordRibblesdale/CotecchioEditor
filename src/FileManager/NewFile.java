package FileManager;

import Interface.UserController;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class NewFile extends AbstractAction {
   private UserController ui;

   public NewFile(UserController ui) {
      this.ui = ui;

      putValue(Action.NAME, "New");
   }

   @Override
   public void actionPerformed(ActionEvent e) {
      Object[] choice = {ui.getSettings().getResourceBundle().getString("save"),
              ui.getSettings().getResourceBundle().getString("discard"),
              ui.getSettings().getResourceBundle().getString("goBack")};

      if (ui.hasBeenSaved()) {
         ui.initialise();
      } else {
         int selection = JOptionPane.showOptionDialog(ui, ui.getSettings().getResourceBundle().getString("askSaveChanges"),
                 ui.getSettings().getResourceBundle().getString("saveFile"),
                 JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null,
                 choice, choice[0]);

         if (choice[selection] == choice[0]) {
            ui.askForSaving(e);
         } else if (choice[selection] == choice[1]) {
            ui.initialise();
         }
      }
   }
}
