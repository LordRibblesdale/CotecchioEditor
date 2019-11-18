package Interface;

import FileManager.SaveFile;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Objects;

public class MainFrame extends JFrame {
  private UserController ui;

  private int BASE_RELEASE;
  private int RELEASE;

  private ManagementPanel mainPanel;

  private PersonalMenu menu;
  private PersonalToolBar toolBar;

  private JLabel saveStatus;

  public MainFrame(UserController ui, String title, int BASE_RELEASE, int RELEASE) {
    super(title);
    this.ui = ui;
    this.BASE_RELEASE = BASE_RELEASE;
    this.RELEASE = RELEASE;

    setIconImage(new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("Data/cotecchio.png"))).getImage());
    setMinimumSize(new Dimension(
        (int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth()*0.4),
        (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight()*0.5)));
    setLayout(new BorderLayout());

    if (ui.getSettings().isUsingLookAndFeel()) {
      try {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
        JOptionPane.showMessageDialog(
            MainFrame.this,
            e.getMessage(),
            "LookAndFeel UserController00 V:" + RELEASE + " " + e.getStackTrace()[0].getLineNumber(),
            JOptionPane.ERROR_MESSAGE);
      }
    }

    menu = new PersonalMenu(ui);
    setJMenuBar(menu);

    toolBar = new PersonalToolBar(SwingConstants.VERTICAL, ui, menu);
    add(toolBar, BorderLayout.LINE_END);

    add(saveStatus = new JLabel(), BorderLayout.PAGE_END);

    addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent e) {
        if (!ui.hasBeenSaved()) {
          int result = JOptionPane.showConfirmDialog(MainFrame.this,
              ui.getSettings().getResourceBundle().getString("saveBeforeClosing"),
              ui.getSettings().getResourceBundle().getString("exitConfirmation"),
              JOptionPane.YES_NO_CANCEL_OPTION);
          if (result == JOptionPane.YES_OPTION) {
            new SaveFile(ui).actionPerformed(null);
          } else if (result == JOptionPane.CANCEL_OPTION) {
            setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
          } else {
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
          }
        }
      }
    });


    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    setLocationRelativeTo(null);
    setVisible(true);
  }

  public PersonalMenu getMenu() {
    return menu;
  }

  public JLabel getSaveStatus() {
    return saveStatus;
  }

  public ManagementPanel getMainPanel() {
    return mainPanel;
  }

  public void setMainPanel(ManagementPanel mainPanel) {
    this.mainPanel = mainPanel;
  }
}
