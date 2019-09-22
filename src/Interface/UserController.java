package Interface;

import Data.*;
import FileManager.*;
import Update.UpdateRepo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

import static FileManager.Path.setPath;

public class UserController extends JFrame {
  private static final String PROGRAM_NAME = "Cotecchio Editor - ";
  private static final String VERSION = "Build 7 Beta 2.0";
  private static final int RELEASE = 720;
  private ManagementPanel mainPanel;
  private PersonalMenu menu;
  private JLabel saveStatus;
  private String page;

  private CotecchioDataArray data;
  private boolean hasBeenSaved = true;

  private PersonalToolBar toolBar;
  private PanelList listPlayers = null;

  private Settings settings;
  private SettingsFrame settingsFrame;

  public UserController() {
    super(PROGRAM_NAME + VERSION);
    setIconImage(new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("Data/cotecchio.png"))).getImage());
    setMinimumSize(new Dimension(800, 600));
    setLayout(new BorderLayout());

    settings = new Settings();

    if (settings.isUsingLookAndFeel()) {
      try {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
        JOptionPane.showMessageDialog(
                UserController.this,
                e.getMessage(),
                "LookAndFeel UserController00 V:" + RELEASE + " " + e.getStackTrace()[0].getLineNumber(),
                JOptionPane.ERROR_MESSAGE);
      }
    }

    settingsFrame = new SettingsFrame(UserController.this);

    menu = new PersonalMenu(this);
    setJMenuBar(menu);

    toolBar = new PersonalToolBar(SwingConstants.VERTICAL, this, menu);
    add(toolBar, BorderLayout.LINE_END);

    add(saveStatus = new JLabel(), BorderLayout.PAGE_END);

    addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent e) {
        if (!hasBeenSaved()) {
          int result = JOptionPane.showConfirmDialog(UserController.this,
                  getSettings().getResourceBundle().getString("saveBeforeClosing"),
                  getSettings().getResourceBundle().getString("exitConfirmation"),
                  JOptionPane.YES_NO_CANCEL_OPTION);
          if (result == JOptionPane.YES_OPTION) {
            new SaveFile(UserController.this).actionPerformed(null);
          } else if (result == JOptionPane.CANCEL_OPTION) {
            UserController.this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
          } else {
            UserController.this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
          }
        }
      }
    });

    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    setLocationRelativeTo(null);
    setVisible(true);

    new UpdateRepo(UserController.this, RELEASE);
  }

  public boolean hasBeenSaved() {
    return hasBeenSaved;
  }

  public void setHasBeenSaved(boolean hasBeenSaved) {
    this.hasBeenSaved = hasBeenSaved;

    if (hasBeenSaved) {
      menu.getSaveButton().setEnabled(false);
      setTitle(PROGRAM_NAME + VERSION);
      settingsFrame.stopTimer();
      saveStatus.setText("Saved @ " + new SimpleDateFormat("HH.mm.ss").format(new Date()));
    } else {
      menu.getSaveButton().setEnabled(true);
      setTitle(PROGRAM_NAME + VERSION + " - *" + getSettings().getResourceBundle().getString("changesNotSaved"));
      settingsFrame.startTimer();
    }

    revalidate();
    repaint();
  }

  public void prepareForInitialisation(CotecchioDataArray data, boolean isFirstCreation) {
    if (data == null) {
      this.data = new CotecchioDataArray();
    } else {
      this.data = data;

      checkDifferentUsernames();
    }

    int mpIndex = 0;

    if (mainPanel != null) {
      mpIndex = mainPanel.getEditPanel().getTabs().getSelectedIndex();

      for (Component c : getContentPane().getComponents()) {
        if (c == mainPanel) {
          getContentPane().remove(c);
        } else if (c == mainPanel.getBottomPanel()) {
          getContentPane().remove(c);
        }
      }

      getContentPane().revalidate();
      getContentPane().repaint();
    }

    if (isFirstCreation) {
      page = PageList.SELECTION;
    }

    mainPanel = new ManagementPanel(this);
    add(mainPanel);

    mainPanel.getEditPanel().askForInitialisation();

    if (listPlayers == null) {
      listPlayers = new PanelList(UserController.this);
    }

    listPlayers.updateList();
    setHasBeenSaved(true);

    if (!isFirstCreation) {
      mainPanel.setNextPage(page);
      mainPanel.getEditPanel().getTabs().setSelectedIndex(mpIndex);
    }

    validate();
  }

  void checkDifferentUsernames() {
    if (data.getGame().size() != 0) {
      ArrayList<String> usernames = new ArrayList<>();

      for (Game g : data.getGame()) {
        if (!g.isSetByPass()) {
          for (PlayerStateGame ps : g.getResults()) {
            boolean isPresent = false;

            for (Player p : getPlayers()) {
              if (ps.getUsername().equals(p.getUsername())) {
                isPresent = true;
              }
            }

            if (!isPresent) {
              usernames.add(ps.getUsername());
            }
          }
        }
      }

      if (usernames.size() != 0) {
        for (String s : usernames) {
          StringBuilder string = new StringBuilder()
              .append(getSettings().getResourceBundle().getString("fixNeededUsernameText"))
              .append(" (")
              .append(s)
              .append(")")
              .append("\n")
              .append("\n")
              .append(getSettings().getResourceBundle().getString("fixNeededUsernameText2"));

          Object o = JOptionPane.showInputDialog(
              UserController.this,
              string.toString(),
              getSettings().getResourceBundle().getString("fixUsernameTitle") + s,
              JOptionPane.INFORMATION_MESSAGE,
              null,
              getData().getPlayers().toArray(),
              getData().getPlayers().toArray()[0]
          );

          if (o != null) {
            for (Game g : getData().getGame()) {
              for (PlayerStateGame ps : g.getResults()) {
                if (ps.getUsername().equals(s)) {
                  ps.setUsername(((Player) o).getUsername());
                }
              }
            }
          } else {
            string = new StringBuilder()
                .append(getSettings().getResourceBundle().getString("setNonEditableGameText"))
                .append("\n")
                .append(getSettings().getResourceBundle().getString("setNonEditableGameText2"))
                .append("\n")
                .append(getSettings().getResourceBundle().getString("setNonEditableGameText3"));

            int choice = JOptionPane.showConfirmDialog(
                UserController.this,
                string,
                getSettings().getResourceBundle().getString("fixUsernameTitle") + s,
                JOptionPane.YES_NO_OPTION
                );

            switch (choice) {
              case JOptionPane.YES_OPTION:
                for (Game g : getData().getGame()) {
                  g.setEditable(false);
                }
                break;
              case JOptionPane.NO_OPTION:
                for (Game g : getData().getGame()) {
                  g.setByPass(true);
                }
                break;
            }
          }
        }

        if (mainPanel != null) {
          mainPanel.getCalendarPanel().setModelTable(new ProgramTable(UserController.this, UserController.this.getData().getGame().toArray(new Game[0])));
          mainPanel.getCalendarPanel().getModelTable().fireChanges();
        }

        setHasBeenSaved(false);
      }
    }
  }

  int getGameIndex() {
    return (Integer) getTable().getValueAt(getTable().getSelectedRow(), 0) -1;
  }

  void askForNextPage(String next) {
    mainPanel.setNextPage(next);
  }

  void makeVisibleButtons() {
    menu.getSearch().setEnabled(true);
    menu.getExport().setEnabled(true);
    menu.getSaveAsButton().setEnabled(true);
    //menu.getPrint().setEnabled(true);
    //menu.getStart().setEnabled(true);
    //menu.getOpenGame().setEnabled(true);
    //menu.getExportXls().setEnabled(true);
    menu.getExportWord().setEnabled(true);
    //menu.getUploadButton().setEnabled(true);

    validate();
  }

  JTable getTable() {
    return mainPanel.getCalendarPanel().getTable();
  }

  EditPanel getEditPanel() {
    return mainPanel.getEditPanel();
  }

  CalendarPanel getCalendarPanel() {
    return mainPanel.getCalendarPanel();
  }

  ProgramTable getAbstractTable() {
    return mainPanel.getCalendarPanel().getModelTable();
  }

  public JTabbedPane getTabs() {
    return mainPanel.getEditPanel().getTabs();
  }

  ArrayList<PlayerUI> getPUI() {
    return mainPanel.getEditPanel().getpUI();
  }

  public CotecchioDataArray getData() {
    return data;
  }

  Game[] getUserData(String username) {
    ArrayList<Game> tmp = new ArrayList<>();

    for (Game a : data.getGame()) {
      for (int i = 0; i < a.getResults().size(); i++) {
        if (a.getResults().get(i).getUsername().equals(username)) {
          tmp.add(a);
        }
      }
    }

    return tmp.toArray(new Game[0]);
  }

  public void setUpData(boolean isFirstCreation) {
    mainPanel.getEditPanel().setUpData(isFirstCreation);
  }

  public void setData(CotecchioDataArray data) {
    this.data = data;
  }

  public ArrayList<Player> getPlayers() {
    return getData().getPlayers();
  }

  public void setPlayers(ArrayList<Player> players) {
    mainPanel.getEditPanel().setPlayers(players);
  }

  public ArrayList<String> getUsernames() {
    return mainPanel.getEditPanel().getUsernames();
  }

  ArrayList<String> getNames() {
    return mainPanel.getEditPanel().getNames();
  }

  JCheckBoxMenuItem getShowList() {
    return menu.getShowList();
  }

  PanelList getListPlayers() {
    return listPlayers;
  }

  public JLabel getStatus() {
    return saveStatus;
  }

  public SettingsFrame getSettingsFrame() {
    return settingsFrame;
  }

  public Settings getSettings() {
    return settings;
  }

  void setSettings(Settings settings) {
    this.settings = settings;
    saveStatus.setText("Saved @ " + new SimpleDateFormat("HH.mm.ss").format(new Date()));
    validate();

    try {
      ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(setPath)));
      out.writeObject(settings);
      out.close();
    } catch (IOException e) {
      e.printStackTrace();
      JOptionPane.showMessageDialog(
              UserController.this,
              e.getMessage(),
              "SaveSettings UserController01 V:" + RELEASE + " " + e.getStackTrace()[0].getLineNumber(),
              JOptionPane.ERROR_MESSAGE);
    }
  }

  public void saveRecentFile(String dir) {
    settings.setOpenedFile(dir);

    setSettings(settings);
  }

  public void askForSaving(ActionEvent e) {
    new SaveFile(UserController.this).actionPerformed(e);
  }

  void setListPlayers(PanelList listPlayers) {
    this.listPlayers = listPlayers;
  }

  int getRelease() {
    return RELEASE;
  }

  String getPage() {
    return page;
  }

  void setPage(String page) {
    this.page = page;
  }
}