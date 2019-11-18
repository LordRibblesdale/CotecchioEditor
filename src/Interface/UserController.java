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

public class UserController {
  /* TODO:
  * Il problema principale del presente UserController è di gestire contemporaneamente sia JFrame sia richieste da
  *   altre classi.
  * In generale, sarà da correggere a tale problema creando una nuova classe che, unicamente lei, potrà gestire
  *   sia richieste al JFrame sia richieste da altre classi senza integrare l'interfaccia.
  * In questo modo sarà possibile aumentare la sicurezza dell'intero programma, facendo accedere solamente ai metodi
  *   necessari, poiché al momento, accedendo a UserController, è possibile modificare una grande varietà di
  *   impostazioni e dati.
  * Lo stesso problema intercorre in tutte le altre classi, ove esistono metodi che richiamano l'oggetto stesso, e non
  *   l'azione che deve eseguire.
  */

  private MainFrame frame;
  private String page;

  private CotecchioDataArray data;
  private boolean hasBeenSaved = true;

  private PanelList listPlayers = null;

  private Settings settings;
  private SettingsFrame settingsFrame;

  private static final int BASE_RELEASE = 800;
  private static final int RELEASE = BASE_RELEASE + 2;
  private static final String PROGRAM_NAME = "Cotecchio Editor - ";
  private static final String VERSION = "Release Candidate " + RELEASE;

  public UserController() {
    settings = new Settings();

    frame = new MainFrame(UserController.this, PROGRAM_NAME + VERSION, BASE_RELEASE, RELEASE);
    settingsFrame = new SettingsFrame(UserController.this, settings.getResourceBundle().getLocale().getLanguage());

    new UpdateRepo(UserController.this, RELEASE);
  }

  public MainFrame getFrame() {
    return frame;
  }

  public boolean hasBeenSaved() {
    return hasBeenSaved;
  }

  public void setHasBeenSaved(boolean hasBeenSaved) {
    this.hasBeenSaved = hasBeenSaved;

    if (hasBeenSaved) {
      frame.getMenu().getSaveButton().setEnabled(false);
      getListPlayers().updateList();
      frame.setTitle(PROGRAM_NAME + VERSION);
      settingsFrame.stopTimer();
      frame.getSaveStatus().setText("Saved @ " + new SimpleDateFormat("HH.mm.ss").format(new Date()));
    } else {
      frame.getMenu().getSaveButton().setEnabled(true);
      frame.setTitle(PROGRAM_NAME + VERSION + " - *" + getSettings().getResourceBundle().getString("changesNotSaved"));
      settingsFrame.startTimer();
    }

    frame.revalidate();
    frame.repaint();
  }

  public void prepareForInitialisation(CotecchioDataArray data, boolean isFirstCreation) {
    if (data == null) {
      this.data = new CotecchioDataArray();
    } else {
      this.data = data;

      checkDifferentUsernames();
    }

    int mpIndex = 0;

    if (frame.getMainPanel() != null) {
      mpIndex = frame.getMainPanel().getEditPanel().getTabs().getSelectedIndex();

      for (Component c : frame.getContentPane().getComponents()) {
        if (c == frame.getMainPanel()) {
          frame.getContentPane().remove(c);
        } else if (c == frame.getMainPanel().getBottomPanel()) {
          frame.getContentPane().remove(c);
        }
      }

      frame.getContentPane().revalidate();
      frame.getContentPane().repaint();
    }

    if (isFirstCreation) {
      page = PageList.SELECTION;
    }

    frame.setMainPanel(new ManagementPanel(this));
    frame.add(frame.getMainPanel());

    frame.getMainPanel().getEditPanel().askForInitialisation();

    if (listPlayers == null) {
      listPlayers = new PanelList(UserController.this);
    }

    listPlayers.updateList();
    setHasBeenSaved(true);

    if (!isFirstCreation) {
      frame.getMainPanel().setNextPage(page);
      frame.getMainPanel().getEditPanel().getTabs().setSelectedIndex(mpIndex);
    }

    frame.validate();
  }

  void checkDifferentUsernames() {
    if (data.getGame().size() != 0) {
      ArrayList<String> usernames = new ArrayList<>();

      for (Game g : data.getGame()) {
        if (g.isEditable()) {
          for (PlayerStateGame ps : g.getResults()) {
            boolean isPresent = false;

            for (Player p : getPlayers()) {
              if (ps.getUsername().equals(p.getUsername())) {
                isPresent = true;
                break;
              }
            }

            if (!isPresent) {
              boolean isInArray = false;
              for (String s : usernames) {
                if (s.equals(ps.getUsername())) {
                  isInArray = true;
                  break;
                }
              }

              if (!isInArray) {
                usernames.add(ps.getUsername());
              }
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
              frame,
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
            for (Game g : getData().getGame()) {
              for (PlayerStateGame ps : g.getResults()) {
                if (ps.getUsername().equals(s)) {
                  g.setEditable(false);
                }
              }
            }
          }
        }

        if (frame.getMainPanel() != null) {
          frame.getMainPanel().getCalendarPanel().setModelTable(new ProgramTable(UserController.this, UserController.this.getData().getGame().toArray(new Game[0])));
          frame.getMainPanel().getCalendarPanel().getModelTable().fireChanges();
        }

        setHasBeenSaved(false);
      }
    }
  }

  int getGameIndex() {
    return (Integer) getTable().getValueAt(getTable().getSelectedRow(), 0) -1;
  }

  void askForNextPage(String next) {
    frame.getMainPanel().setNextPage(next);
  }

  void makeVisibleButtons() {
    frame.getMenu().getSearch().setEnabled(true);
    frame.getMenu().getExport().setEnabled(true);
    frame.getMenu().getSaveAsButton().setEnabled(true);
    frame.getMenu().getExportWord().setEnabled(true);
    frame.getMenu().getResetButton().setEnabled(true);
    frame.getMenu().getShowList().setEnabled(true);

    frame.validate();
  }

  JTable getTable() {
    return frame.getMainPanel().getCalendarPanel().getTable();
  }

  EditPanel getEditPanel() {
    return frame.getMainPanel().getEditPanel();
  }

  CalendarPanel getCalendarPanel() {
    return frame.getMainPanel().getCalendarPanel();
  }

  ProgramTable getAbstractTable() {
    return frame.getMainPanel().getCalendarPanel().getModelTable();
  }

  public JTabbedPane getTabs() {
    return frame.getMainPanel().getEditPanel().getTabs();
  }

  ArrayList<PlayerUI> getPUI() {
    return frame.getMainPanel().getEditPanel().getpUI();
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
    frame.getMainPanel().getEditPanel().setUpData(isFirstCreation);
  }

  public void setData(CotecchioDataArray data) {
    this.data = data;
  }

  public ArrayList<Player> getPlayers() {
    return getData().getPlayers();
  }

  public void setPlayers(ArrayList<Player> players) {
    frame.getMainPanel().getEditPanel().setPlayers(players);
  }

  public ArrayList<String> getUsernames() {
    return frame.getMainPanel().getEditPanel().getUsernames();
  }

  ArrayList<String> getNames() {
    return frame.getMainPanel().getEditPanel().getNames();
  }

  JCheckBoxMenuItem getShowList() {
    return frame.getMenu().getShowList();
  }

  PanelList getListPlayers() {
    return listPlayers;
  }

  public JLabel getStatus() {
    return frame.getSaveStatus();
  }

  public SettingsFrame getSettingsFrame() {
    return settingsFrame;
  }

  public Settings getSettings() {
    return settings;
  }

  void setSettings(Settings settings) {
    this.settings = settings;
    frame.getSaveStatus().setText("Saved @ " + new SimpleDateFormat("HH.mm.ss").format(new Date()));
    frame.validate();

    try {
      ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(setPath)));
      out.writeObject(settings);
      out.close();
    } catch (IOException e) {
      e.printStackTrace();
      JOptionPane.showMessageDialog(
              frame,
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

  void setPage(String page) {
    this.page = page;
  }
}