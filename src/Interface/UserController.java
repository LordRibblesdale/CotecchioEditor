package Interface;

import Data.CotecchioDataArray;
import Data.Player;
import Data.Settings;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

import static FileManager.Path.setPath;

public class UserController extends JFrame {
  private static final String PROGRAM_NAME = "Cotecchio Editor - ";
  private static final String VERSION = "Build 6 Beta 1.0";
  private static final int RELEASE = 610;
  private ManagementPanel mainPanel;
  private PersonalMenu menu;
  private JLabel saveStatus;

  private CotecchioDataArray data;
  private boolean hasBeenSaved = true;

  private PersonalToolBar toolBar;
  private PanelList listPlayers = null;

  private Settings settings;
  private SettingsFrame settingsFrame;

  public UserController() {
    super(PROGRAM_NAME + VERSION);
    setIconImage(new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("Data/cotecchio.png"))).getImage());
    setMinimumSize(new Dimension(650, 500));

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

    validate();
  }

  public void prepareForInitialisation(CotecchioDataArray data) {
    if (data == null) {
      this.data = new CotecchioDataArray();
    }

    mainPanel = new ManagementPanel(this);
    add(mainPanel);

    mainPanel.getEditPanel().askForInitialisation();

    if (listPlayers == null) {
      listPlayers = new PanelList(UserController.this);
    }

    listPlayers.updateList();
    setHasBeenSaved(true);

    validate();
  }

  void askForNextPage(String next) {
    mainPanel.setNextPage(next);
  }

  void makeVisibleButtons() {
    menu.getSearch().setEnabled(true);
    menu.getExport().setEnabled(true);
    //menu.getPrint().setEnabled(true); //TODO HERE
    menu.getStart().setEnabled(true);
    menu.getOpenGame().setEnabled(true);
    menu.getExportXls().setEnabled(true);

    validate();
  }

  public JTabbedPane getTabs() {
    return mainPanel.getEditPanel().getTabs();
  }

  public CotecchioDataArray getData() {
    return data;
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
}