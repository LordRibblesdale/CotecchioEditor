package FileManager;

import Interface.UserController;

import javax.swing.*;

public class SaveAsFile extends SaveFile {

   public SaveAsFile(UserController ui) {
      super(ui);

      putValue(Action.NAME, ui.getSettings().getResourceBundle().getString("saveWithName"));
      putValue(Action.SHORT_DESCRIPTION, ui.getSettings().getResourceBundle().getString("saveWithName"));
   }
}
