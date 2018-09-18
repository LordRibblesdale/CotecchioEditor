import Interface.UserController;

public class CotecchioEditor implements FileManager.Path {
   private static UserController ui;
   public static void main(String[] args) {
      ui = new UserController();
   }
}
