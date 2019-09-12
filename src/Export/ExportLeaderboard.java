package Export;

import Data.Player;
import Interface.UserController;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collections;

public class ExportLeaderboard extends AbstractAction {
   private UserController ui;

   public ExportLeaderboard(UserController ui) {
      this.ui = ui;

      putValue(Action.NAME, ui.getSettings().getResourceBundle().getString("exportTopPlayers"));
   }

   @Override
   public void actionPerformed(ActionEvent e) {
      ArrayList<Player> topPlayers = new ArrayList<>();
      ArrayList<Player> players = ui.getPlayers();

      for (Player p : players) {
         if (p.getTotalPlays() >= 20) {
            topPlayers.add(p);
         }
      }

      Collections.sort(topPlayers);

      Object[] choices = {ui.getSettings().getResourceBundle().getString("exportXLS"),
              ui.getSettings().getResourceBundle().getString("printLeaderboard"),
              ui.getSettings().getResourceBundle().getString("doNothing")};
      String list = generateList(topPlayers);
      int choice = JOptionPane.showOptionDialog(ui,  ui.getSettings().getResourceBundle().getString("leaderboardPreview")+ " \n" + list,
              "Export?",
              JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, choices, choices[0]);
      System.out.println(choice);

      if (choices[choice] == choices[0]) {
         new ExportXlsLeaderboard(ui, topPlayers);
      } else if (choices[choice] == choices[1]) {
         new PrintThreadLeaderboard(ui, topPlayers);
      }
   }

   public String generateList(ArrayList<Player> players) {
      StringBuilder tmp = new StringBuilder();

      for (Player p : players) {
         tmp.append(p);
         tmp.append("; ");
      }

      return tmp.toString();
   }
}
