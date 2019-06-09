package Edit;

import Interface.UserController;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class Search extends AbstractAction {
   private JTabbedPane tab;
   private UserController ui;
   private static int i = 0;
   private String previousRes = "";

   public Search(UserController ui) {
      this.ui = ui;

      putValue(Action.NAME, ui.getSettings().getResourceBundle().getString("search"));
      putValue(Action.SHORT_DESCRIPTION, ui.getSettings().getResourceBundle().getString("search"));
      putValue(Action.LARGE_ICON_KEY, new ImageIcon(Search.class.getResource("Find24.gif")));
      putValue(Action.SMALL_ICON, new ImageIcon(Search.class.getResource("Find16.gif")));

   }

   @Override
   public void actionPerformed(ActionEvent e) {
      tab = ui.getTabs();

      String result = JOptionPane.showInputDialog(ui, ui.getSettings().getResourceBundle().getString("insertName"), previousRes);

      if (result != null && tab != null) {
         if (!previousRes.equals(result)) {
            i = 0;
         }

         for ( ; i < tab.getTabCount(); i++) {
            if (tab.getTitleAt(i).contains(result) || tab.getTitleAt(i).equalsIgnoreCase(result)) {
               tab.setSelectedIndex(i);
            }
         }

         previousRes = result;
      }
   }
}
