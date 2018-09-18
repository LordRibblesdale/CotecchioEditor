package Game;

import Interface.UserController;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class GameStarter extends AbstractAction {
   private UserController ui;

   public GameStarter(UserController ui) {
      this.ui = ui;

      putValue(Action.NAME, "Start lobby");
   }

   @Override
   public void actionPerformed(ActionEvent e) {
      try {
         new GameProgress(ui, ui.getPlayers());
      } catch (Exception e1) {
         JOptionPane.showMessageDialog(ui, "Process closed. Contact developer!");
         e1.printStackTrace();
      }
   }
}
