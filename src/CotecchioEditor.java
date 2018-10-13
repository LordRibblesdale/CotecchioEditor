import Interface.UserController;

import javax.swing.*;

public class CotecchioEditor implements FileManager.Path {
   private static UserController ui;

   public static void main(String[] args) {
      try {
         UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
         e.printStackTrace();
      }

      ui = new UserController();
   }
}
