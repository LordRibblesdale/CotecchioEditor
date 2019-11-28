package FileManager;

import Interface.UserController;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.event.ActionEvent;
import java.io.File;
import java.net.URISyntaxException;

public class NewFile extends AbstractAction {
   private UserController ui;

   public NewFile(UserController ui) {
      this.ui = ui;

      putValue(Action.NAME, ui.getSettings().getResourceBundle().getString("new"));
      try {
         putValue(Action.LARGE_ICON_KEY, FileSystemView.getFileSystemView().getSystemIcon(new File(NewFile.class.getResource("New.png").toURI())));
         putValue(Action.SMALL_ICON, FileSystemView.getFileSystemView().getSystemIcon(new File(NewFile.class.getResource("New.png").toURI())));
      } catch (URISyntaxException e) {
         e.printStackTrace();
      }
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
