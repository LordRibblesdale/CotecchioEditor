import Interface.UserController;

import javax.swing.*;

public class CotecchioEditor implements FileManager.Path {
   public static void main(String[] args) {
      SwingUtilities.invokeLater(new Runnable(){
         public void run() {
            new UserController();
         }
      });
   }
}
