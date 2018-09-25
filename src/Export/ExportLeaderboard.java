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

      putValue(Action.NAME, "Export Top Players");
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

      Object[] choices = {"Export in XLS", "Print leaderboard", "Do nothing"};
      String list = generateList(topPlayers);
      int choice = JOptionPane.showOptionDialog(ui, "Classifica generale in anteprima: \n" + list, "Export?",
              JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, choices, choices[0]);
      System.out.println(choice);

      if (choices[choice] == choices[0]) {
         new ExportXlsLeaderboard(ui, topPlayers);
      } else if (choices[choice] == choices[1]) {
         new PrintThreadLeaderboard(ui, topPlayers);
      }
   }

   private String generateList(ArrayList<Player> players) {
      StringBuilder tmp = new StringBuilder();

      for (Player p : players) {
         tmp.append(p);
         tmp.append("\n");
      }

      return tmp.toString();
   }
}
