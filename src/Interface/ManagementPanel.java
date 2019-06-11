package Interface;

import Data.CotecchioDataArray;
import Data.Player;
import Export.ExportLeaderboard;
import FileManager.SaveFile;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

public class ManagementPanel extends JPanel implements PageList {
  class EditPanel extends JPanel {
    private JButton addTab, removeTab;

    private JTabbedPane tabs = null;
    private ArrayList<PlayerUI> pUI;
    private JPanel bottomPanel;

    private UserController ui;

    EditPanel(UserController ui) {
      super();

      this.ui = ui;

      bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
      bottomPanel.add(addTab = new JButton(ui.getSettings().getResourceBundle().getString("addTab")));
      bottomPanel.add(removeTab = new JButton(ui.getSettings().getResourceBundle().getString("removeTab")));

      initialise();

      addTab.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          ui.getData().getPlayers().add(new Player(ui.getSettings().getResourceBundle().getString("newPlayer0"),
                  ui.getSettings().getResourceBundle().getString("newPlayer1"), 0, 0, 0, 0, 0));
          pUI.add(new PlayerUI(ui.getData().getPlayers().get(ui.getData().getPlayers().size()-1), ui));
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

    void initialise() {
      if (tabs != null) {
        remove(tabs);
      }

      tabs = new JTabbedPane();
      add(tabs);
      validate();

      pUI = new ArrayList<>();

      if (ui.getData().getPlayers().isEmpty()) {
        ui.getData().getPlayers().add(new Player(ui.getSettings().getResourceBundle().getString("newPlayer0"),
                ui.getSettings().getResourceBundle().getString("newPlayer1"),
                0, 0, 0, 0, 0));
        pUI.add(new PlayerUI(ui.getData().getPlayers().get(ui.getData().getPlayers().size()-1), ui));
        tabs.addTab(ui.getSettings().getResourceBundle().getString("newPlayer0"), pUI.get(pUI.size()-1).generatePanel());
      } else {
        this.ui.getData().setPlayers(ui.getData().getPlayers());

        for (Player p : ui.getData().getPlayers()) {
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
      initialise();
    }

    ArrayList<Player> getPlayers() {
      for (int i = 0; i < ui.getData().getPlayers().size(); i++) {
        ui.getData().getPlayers().set(i, new Player(pUI.get(i).getJTextName().getText(),
                pUI.get(i).getUsername().getText(),
                (Integer) (pUI.get(i).getInsert().get(0).getValue()),
                (Integer) (pUI.get(i).getInsert().get(1).getValue()),
                (Integer) (pUI.get(i).getInsert().get(2).getValue()),
                (Integer) (pUI.get(i).getInsert().get(3).getValue()),
                (Integer) (pUI.get(i).getInsert().get(4).getValue())));
      }

      return ui.getData().getPlayers();
    }

    CotecchioDataArray getData() {
      for (int i = 0; i < ui.getData().getPlayers().size(); i++) {
        ui.getData().getPlayers().set(i, new Player(pUI.get(i).getJTextName().getText(),
                pUI.get(i).getUsername().getText(),
                (Integer) (pUI.get(i).getInsert().get(0).getValue()),
                (Integer) (pUI.get(i).getInsert().get(1).getValue()),
                (Integer) (pUI.get(i).getInsert().get(2).getValue()),
                (Integer) (pUI.get(i).getInsert().get(3).getValue()),
                (Integer) (pUI.get(i).getInsert().get(4).getValue())));
      }

      return ui.getData();
    }

    void setData(CotecchioDataArray data) {
      ui.setData(data);
    }

    void setPlayers(ArrayList<Player> players) {
      this.getData().getPlayers().clear();
      this.getData().getPlayers().addAll(players);
    }

    ArrayList<Object> getUsernames() {
      ArrayList<Object> usernames = new ArrayList<>();

      for (Player p : ui.getData().getPlayers()) {
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

    CalendarPanel(UserController ui) {
      super();

      this.ui = ui;
      //this.games = ui.getData().getGame();
    }
  }

  class MainPagePanel extends JPanel {
    private JButton backButton, calendarButton, editButton;
    private JScrollPane leaderboard;
    private JPanel bottomPanel;

    private UserController ui;

    MainPagePanel(UserController ui) {
      super();
      setLayout(new BorderLayout());

      this.ui = ui;

      try {
        leaderboard = new JScrollPane(new JLabel(setUpText()));
      } catch (FileNotFoundException e) {
        e.printStackTrace();
      } finally {

      }

      add(leaderboard);

      bottomPanel = new JPanel();
      backButton = new JButton(ui.getSettings().getResourceBundle().getString("goBack"));
      calendarButton = new JButton(new CalendarButton(ui));
      editButton = new JButton(new TabbedListButton(ui));
      bottomPanel.add(backButton);
      bottomPanel.add(calendarButton);
      bottomPanel.add(editButton);

      add(bottomPanel, BorderLayout.PAGE_END);
    }

    String setUpText() throws FileNotFoundException {
      StringBuilder text = new StringBuilder();
      Scanner s = new Scanner(new BufferedInputStream(new FileInputStream(new File((Objects.requireNonNull(getClass().getClassLoader().getResource("Data/messageIT"))).getPath()))));

      while (s.hasNext()) {
        text.append(s.nextLine());
      }

      return text.toString() +
              (new ExportLeaderboard(ui)).generateList(ui.getPlayers());
    }
  }

  private UserController ui;

  private MainPagePanel main;
  private EditPanel editPanel;
  private CalendarPanel calendarPanel;

  private CardLayout primaryLayout;

  ManagementPanel(UserController ui) {
    super();

    this.ui = ui;

    editPanel = new EditPanel(ui);
    calendarPanel = new CalendarPanel(ui);
    main = new MainPagePanel(ui);

    setLayout(primaryLayout = new CardLayout());

    add(main, "BACK");
  }

  void setNextPage(String next) {
    if (next.equals("CALENDAR")) {
      if (calendarPanel == null) {
        calendarPanel = new CalendarPanel(ui);
      } else {
        primaryLayout.show(calendarPanel, next);
      }
    } else if (next.equals("EDIT")) {
      if (editPanel == null) {
        editPanel = new EditPanel(ui);
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
