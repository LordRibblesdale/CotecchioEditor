package FileManager;

import Interface.UserController;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class NewFile extends AbstractAction {
   private UserController ui;

   public NewFile(UserController ui) {
      this.ui = ui;

      putValue(Action.NAME, ui.getSettings().getResourceBundle().getString("new"));
      putValue(Action.LARGE_ICON_KEY, new ImageIcon(NewFile.class.getResource("New.png")));
      putValue(Action.SMALL_ICON, new ImageIcon(NewFile.class.getResource("New.png")));
      putValue(Action.SHORT_DESCRIPTION, ui.getSettings().getResourceBundle().getString("new"));
   }

   @Override
   public void actionPerformed(ActionEvent e) {
      Object[] choice = {
              ui.getSettings().getResourceBundle().getString("save"),
              ui.getSettings().getResourceBundle().getString("discard"),
              ui.getSettings().getResourceBundle().getString("goBack")
      };

      if (ui.hasBeenSaved()) {
         ui.prepareForInitialisation(null, true);
      } else {
         int selection = JOptionPane.showOptionDialog(ui.getFrame(), ui.getSettings().getResourceBundle().getString("askSaveChanges"),
                 ui.getSettings().getResourceBundle().getString("saveFile"),
                 JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null,
                 choice, choice[0]);

         if (choice[selection] == choice[0]) {
            ui.askForSaving(e);
            ui.prepareForInitialisation(null, true);
         } else if (choice[selection] == choice[1]) {
            ui.prepareForInitialisation(null, true);
         }
      }
   }
}
