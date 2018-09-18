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

      putValue(Action.NAME, "Search");
   }

   @Override
   public void actionPerformed(ActionEvent e) {
      tab = ui.getTabs();

      String result = JOptionPane.showInputDialog(ui, "Insert name to search: ", previousRes);

      if (result != null) {
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
