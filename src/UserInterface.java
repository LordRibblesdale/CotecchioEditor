import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

public class UserInterface extends JFrame {
   static private String programName = "Cotecchio Editor - ";
   static private String version = "Build 0 Alpha 2.0";
   private GridLayout mainLayout;
   private JPanel mainPanel;
   private JMenuBar menu;
   private JMenu file, about;

   private boolean hasBeenSaved = true;
   private SaveFile saveButton;

   private JTabbedPane tabs;
   private ArrayList<Player> players;
   private ArrayList<PlayerUI> pUI;

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

      addWindowListener(new WindowAdapter() {
         @Override
         public void windowClosing(WindowEvent e) {
            super.windowClosing(e);

            if (!hasBeenSaved()) {

            }
         }
      });

      setMinimumSize(new Dimension(500, 500));
      setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
      setLocationRelativeTo(null);
      setVisible(true);
   }

   public boolean hasBeenSaved() {
      return hasBeenSaved;
   }

   public void setHasBeenSaved(boolean hasBeenSaved) {
      this.hasBeenSaved = hasBeenSaved;

      if (hasBeenSaved) {
         saveButton.setEnabled(false);
      } else {
         saveButton.setEnabled(true);
      }

      validate();
   }

   public void initialise() {
      if (tabs != null) {
         remove(tabs);
         setHasBeenSaved(true);
      }

      tabs = new JTabbedPane();
      add(tabs);
      validate();

      players = new ArrayList<>();
      pUI = new ArrayList<>();

      players.add(new Player("New Player", 0, 0, 0, 0, 0));
      pUI.add(new PlayerUI(players.get(players.size()-1), UserInterface.this));
      tabs.addTab("New Player", pUI.get(pUI.size()-1).generatePanel());
      validate();
   }

   public JTabbedPane getTabs() {
      return tabs;
   }
}