import javax.swing.*;
import java.awt.*;

public class UserInterface extends JFrame {
   static private String programName = "Cotecchio Editor - ";
   static private String version = "Build 0 Alpha 1.0";
   private GridLayout mainLayout;
   private JPanel mainPanel;
   private JMenuBar menu;
   private JMenu file, about;

   private boolean hasBeenSaved = true;
   private SaveFile saveButton;

   UserInterface() {
      super(programName + version);

      mainPanel = new JPanel(mainLayout = new GridLayout(0, 10));

      menu = new JMenuBar();
      menu.add(file = new JMenu("File"));
      file.add(new NewFile(UserInterface.this));
      file.add(new OpenFile(UserInterface.this));
      file.add(saveButton = new SaveFile(UserInterface.this));
      saveButton.setEnabled(false);

      menu.add(about = new JMenu("About"));
      about.add(new About(UserInterface.this));

      setJMenuBar(menu);

      setMinimumSize(new Dimension(500, 500));
      setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
      setLocationRelativeTo(null);
      setVisible(true);
   }

   public boolean hasBeenSaved() {
      return hasBeenSaved;
   }

   public void initialiseUI() {

   }
}