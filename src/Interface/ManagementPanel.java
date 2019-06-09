package Interface;

import Data.Game;
import Data.Player;
import FileManager.SaveFile;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class ManagementPanel extends JPanel implements PageList {
  class EditPanel extends JPanel {
    private JButton addTab, removeTab;

    private JTabbedPane tabs = null;
    private ArrayList<Player> players = null;
    private ArrayList<PlayerUI> pUI;
    private JPanel bottomPanel;

    private UserController ui;

    EditPanel(UserController ui, ArrayList<Player> players) {
      super();

      this.ui = ui;

      initialise(players);

      bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
      bottomPanel.add(addTab = new JButton(ui.getSettings().getResourceBundle().getString("addTab")));
      bottomPanel.add(removeTab = new JButton(ui.getSettings().getResourceBundle().getString("removeTab")));

      addTab.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          players.add(new Player(ui.getSettings().getResourceBundle().getString("newPlayer0"),
                  ui.getSettings().getResourceBundle().getString("newPlayer1"), 0, 0, 0, 0, 0));
          pUI.add(new PlayerUI(players.get(players.size()-1), ui));
          tabs.addTab(ui.getSettings().getResourceBundle().getString("newPlayer0"), pUI.get(pUI.size()-1).generatePanel());

          if (tabs.getTabCount() > 1) {
            removeTab.setEnabled(true);
          }

          if (ui.getListPlayers() == null) {
            ui.setListPlayers(new PanelList(ui));
          }

          ui.getListPlayers().updateList();
          validate();
        }
      });

      removeTab.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          if (!ui.hasBeenSaved()) {
            Object[] choice = {ui.getSettings().getResourceBundle().getString("yes"),
                    ui.getSettings().getResourceBundle().getString("no"),
                    ui.getSettings().getResourceBundle().getString("goBack")};
            int sel = JOptionPane.showOptionDialog(ui,
                    ui.getSettings().getResourceBundle().getString("notSavedTab"),
                    ui.getSettings().getResourceBundle().getString("saveFile"),
                    JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null,
                    choice, choice[0]);

            if (choice[sel] == choice[0]) {
              new SaveFile(ui).actionPerformed(null);
            } else if (choice[sel] == choice[1]) {
              tabs.remove(tabs.getSelectedIndex());

              if (tabs.getTabCount() == 1) {
                removeTab.setEnabled(false);
              }
            }
          } else {
            tabs.remove(tabs.getSelectedIndex());

            if (tabs.getTabCount() == 1) {
              removeTab.setEnabled(false);
            }

            ui.setHasBeenSaved(false);
          }

          validate();
        }
      });

      addTab.setEnabled(false);
      removeTab.setEnabled(false);

    }

    void initialise(ArrayList<Player> players) {
      if (tabs != null) {
        remove(tabs);
      }

      tabs = new JTabbedPane();
      add(tabs);
      validate();

      pUI = new ArrayList<>();

      if (players == null) {
        players = new ArrayList<>();

        players.add(new Player(ui.getSettings().getResourceBundle().getString("newPlayer0"),
                ui.getSettings().getResourceBundle().getString("newPlayer1"),
                0, 0, 0, 0, 0));
        pUI.add(new PlayerUI(players.get(players.size()-1), ui));
        tabs.addTab(ui.getSettings().getResourceBundle().getString("newPlayer0"), pUI.get(pUI.size()-1).generatePanel());
      } else {
        this.players = players;

        for (Player p : players) {
          pUI.add(new PlayerUI(p, ui));
          tabs.addTab(pUI.get(pUI.size()-1).getJTextName().getText(), pUI.get(pUI.size()-1).generatePanel());
        }

        if (tabs.getTabCount() > 1) {
          removeTab.setEnabled(true);
        }
      }

      addTab.setEnabled(true);

      ui.makeVisibleButtons();
    }

    void askForInitialisation(ArrayList<Player> players) {
      //TODO: increase security
      initialise(players);
    }

    ArrayList<Player> getPlayers() {
      for (int i = 0; i < players.size(); i++) {
        players.set(i, new Player(pUI.get(i).getJTextName().getText(),
                pUI.get(i).getUsername().getText(),
                (Integer) (pUI.get(i).getInsert().get(0).getValue()),
                (Integer) (pUI.get(i).getInsert().get(1).getValue()),
                (Integer) (pUI.get(i).getInsert().get(2).getValue()),
                (Integer) (pUI.get(i).getInsert().get(3).getValue()),
                (Integer) (pUI.get(i).getInsert().get(4).getValue())));
      }

      return players;
    }

    void setPlayers(ArrayList<Player> players) {
      this.players = players;
    }

    ArrayList<Object> getUsernames() {
      ArrayList<Object> usernames = new ArrayList<>();

      for (Player p : players) {
        usernames.add(p.getUsername());
      }

      return usernames;
    }

    JTabbedPane getTabs() {
      return tabs;
    }
  }

  class CalendarPanel extends JPanel {
    private UserController ui;

    private ArrayList<Game> games;

    CalendarPanel(UserController ui, ArrayList<Game> games) {
      super();

      this.ui = ui;
      this.games = games;
    }
  }

  class MainPagePanel extends JPanel {
    private JButton calendarButton, editButton;
    private GridBagLayout layout;

    MainPagePanel() {
      super();

      setLayout(layout = new GridBagLayout());
      add(calendarButton = new JButton());
      add(editButton = new JButton());
    }
  }

  private UserController ui;

  private MainPagePanel main;
  private EditPanel editPanel;
  private CalendarPanel calendarPanel;

  private CardLayout primaryLayout;
  private GridLayout mainLayout; //??

  ManagementPanel(UserController ui) {
    super();

    this.ui = ui;

    setLayout(primaryLayout = new CardLayout());

    add(new JPanel(mainLayout = new GridLayout(0, 1)), "BACK");
  }

  void setNextPage(String next, ArrayList<Player> players, ArrayList<Game> games) {
    if (next.equals("CALENDAR")) {
      if (calendarPanel == null) {
        calendarPanel = new CalendarPanel(ui, games);
      } else {
        primaryLayout.show(calendarPanel, next);
      }
    } else if (next.equals("EDIT")) {
      if (editPanel == null) {
        editPanel = new EditPanel(ui, players);
      }
    }
  }

  public MainPagePanel getMain() {
    return main;
  }

  public EditPanel getEditPanel() {
    return editPanel;
  }

  public CalendarPanel getCalendarPanel() {
    return calendarPanel;
  }
}
