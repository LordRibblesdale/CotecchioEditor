package Interface;

import Data.CotecchioDataArray;
import Data.Game;
import Data.Player;
import Export.ExportLeaderboard;
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
    private CotecchioDataArray data;
    private ArrayList<PlayerUI> pUI;
    private JPanel bottomPanel;

    private UserController ui;

    EditPanel(UserController ui, CotecchioDataArray data) {
      super();

      this.ui = ui;

      bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
      bottomPanel.add(addTab = new JButton(ui.getSettings().getResourceBundle().getString("addTab")));
      bottomPanel.add(removeTab = new JButton(ui.getSettings().getResourceBundle().getString("removeTab")));

      this.data = new CotecchioDataArray();
      initialise(data);

      addTab.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          data.getPlayers().add(new Player(ui.getSettings().getResourceBundle().getString("newPlayer0"),
                  ui.getSettings().getResourceBundle().getString("newPlayer1"), 0, 0, 0, 0, 0));
          pUI.add(new PlayerUI(data.getPlayers().get(data.getPlayers().size()-1), ui));
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

      add(bottomPanel, BorderLayout.PAGE_END);
    }

    void initialise(CotecchioDataArray data) {
      if (tabs != null) {
        remove(tabs);
      }

      tabs = new JTabbedPane();
      add(tabs);
      validate();

      pUI = new ArrayList<>();

      if (data.getPlayers() == null) {
        data.setPlayers(new ArrayList<>());

        data.getPlayers().add(new Player(ui.getSettings().getResourceBundle().getString("newPlayer0"),
                ui.getSettings().getResourceBundle().getString("newPlayer1"),
                0, 0, 0, 0, 0));
        pUI.add(new PlayerUI(data.getPlayers().get(data.getPlayers().size()-1), ui));
        tabs.addTab(ui.getSettings().getResourceBundle().getString("newPlayer0"), pUI.get(pUI.size()-1).generatePanel());
      } else {
        this.data.setPlayers(data.getPlayers());

        for (Player p : data.getPlayers()) {
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

    void askForInitialisation(CotecchioDataArray data) {
      //TODO: increase security
      initialise(data);
    }

    ArrayList<Player> getPlayers() {
      for (int i = 0; i < data.getPlayers().size(); i++) {
        data.getPlayers().set(i, new Player(pUI.get(i).getJTextName().getText(),
                pUI.get(i).getUsername().getText(),
                (Integer) (pUI.get(i).getInsert().get(0).getValue()),
                (Integer) (pUI.get(i).getInsert().get(1).getValue()),
                (Integer) (pUI.get(i).getInsert().get(2).getValue()),
                (Integer) (pUI.get(i).getInsert().get(3).getValue()),
                (Integer) (pUI.get(i).getInsert().get(4).getValue())));
      }

      return data.getPlayers();
    }

    CotecchioDataArray getData() {
      for (int i = 0; i < data.getPlayers().size(); i++) {
        data.getPlayers().set(i, new Player(pUI.get(i).getJTextName().getText(),
                pUI.get(i).getUsername().getText(),
                (Integer) (pUI.get(i).getInsert().get(0).getValue()),
                (Integer) (pUI.get(i).getInsert().get(1).getValue()),
                (Integer) (pUI.get(i).getInsert().get(2).getValue()),
                (Integer) (pUI.get(i).getInsert().get(3).getValue()),
                (Integer) (pUI.get(i).getInsert().get(4).getValue())));
      }

      return data;
    }


    void setPlayers(ArrayList<Player> players) {
      this.data.getPlayers().size();
      this.data.getPlayers().addAll(players);
    }

    ArrayList<Object> getUsernames() {
      ArrayList<Object> usernames = new ArrayList<>();

      for (Player p : data.getPlayers()) {
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
    private SpringLayout layout;
    private JLabel leaderboard;

    private UserController ui;

    MainPagePanel(UserController ui, String list) {
      super();

      this.ui = ui;

      setLayout(layout = new SpringLayout());
      add(leaderboard = new JLabel(list));
      add(calendarButton = new JButton(new CalendarButton(ui)));
      add(editButton = new JButton(new TabbedListButton(ui)));

      setUpLayout();
    }

    private void setUpLayout() {
      layout.putConstraint(SpringLayout.NORTH, leaderboard,
              5,
              SpringLayout.NORTH, ui);
      layout.putConstraint(SpringLayout.WEST, leaderboard,
              5,
              SpringLayout.WEST, ui);

      layout.putConstraint(SpringLayout.EAST, ui,
              -5,
              SpringLayout.EAST, calendarButton);
      layout.putConstraint(SpringLayout.NORTH, calendarButton,
              5,
              SpringLayout.NORTH, ui);

      layout.putConstraint(SpringLayout.NORTH, editButton,
              5,
              SpringLayout.SOUTH, calendarButton);
      layout.putConstraint(SpringLayout.EAST, ui,
              -5,
              SpringLayout.EAST, editButton);
    }
  }

  private UserController ui;

  private MainPagePanel main;
  private EditPanel editPanel;
  private CalendarPanel calendarPanel;

  private CardLayout primaryLayout;
  private GridLayout mainLayout; //??

  ManagementPanel(UserController ui, CotecchioDataArray data) {
    super();

    this.ui = ui;

    main = new MainPagePanel(ui, (new ExportLeaderboard(ui)).generateList(data.getPlayers()));
    editPanel = new EditPanel(ui, data);
    calendarPanel = new CalendarPanel(ui, data.getGame());

    setLayout(primaryLayout = new CardLayout());

    add(main, "BACK");
  }

  void setNextPage(String next, CotecchioDataArray data) {
    if (next.equals("CALENDAR")) {
      if (calendarPanel == null) {
        calendarPanel = new CalendarPanel(ui, data.getGame());
      } else {
        primaryLayout.show(calendarPanel, next);
      }
    } else if (next.equals("EDIT")) {
      if (editPanel == null) {
        editPanel = new EditPanel(ui, data);
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
