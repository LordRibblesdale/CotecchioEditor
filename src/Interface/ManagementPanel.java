package Interface;

import Data.CotecchioDataArray;
import Data.Game;
import Data.Player;
import Data.PlayerStateGame;
import Export.ExportLeaderboard;
import FileManager.SaveFile;
import net.sourceforge.jdatepicker.impl.DateComponentFormatter;
import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.UtilDateModel;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;

public class ManagementPanel extends JPanel implements PageList {
  class EditPanel extends JPanel {
    private JButton addTab, removeTab;

    private JTabbedPane tabs = null;
    private ArrayList<PlayerUI> pUI;
    private JPanel bottomPanel;

    private UserController ui;

    EditPanel(UserController ui) {
      super();
      setLayout(new BorderLayout());

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
          ui.setHasBeenSaved(false);
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
      validate();

      ui.makeVisibleButtons();
    }

    void askForInitialisation() {
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

    ArrayList<String> getUsernames() {
      ArrayList<String> usernames = new ArrayList<>();

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
    class MatchDialog extends JDialog {
      class SinglePanel extends JPanel {
        private JComboBox<String> players;
        private JSpinner points, pelliccions, cappottens;

        SinglePanel(UserController ui) {
          super(new GridLayout(0, 2));

          add(players = new JComboBox<>(ui.getUsernames().toArray(new String[0])));
          add(new JSeparator());
          add(new JLabel(ui.getSettings().getResourceBundle().getString("pointsMatch")));
          add(points = new JSpinner(new SpinnerNumberModel(0, 0, 10, 1)));
          add(new JLabel(ui.getSettings().getResourceBundle().getString("pelliccions")));
          add(pelliccions = new JSpinner(new SpinnerNumberModel(0, 0, 100, 1)));
          add(new JLabel(ui.getSettings().getResourceBundle().getString("cappottens")));
          add(cappottens = new JSpinner(new SpinnerNumberModel(0, 0, 100, 1)));
        }

        String getPlayer() {
          return (String) players.getSelectedItem();
        }

        int getPoints() {
          return (Integer) points.getValue();
        }

        int getPelliccions() {
          return (Integer) pelliccions.getValue();
        }

        int getCappottens() {
          return (Integer) cappottens.getValue();
        }

      }

      private JPanel master;
      private JPanel bottom;
      private JButton back, save;
      private JSpinner playersInGame;
      private ArrayList<SinglePanel> players;
      private JDatePickerImpl date;

      private final int MINIMUM = 3;
      private final int MAXIMUM = 8;

      MatchDialog(UserController ui) {
        super(ui, true);
        setLayout(new BorderLayout());

        master = new JPanel(new GridLayout(0, 1));
        bottom = new JPanel();

        players = new ArrayList<>(MINIMUM);

        for (int i = 0; i < MINIMUM; i++) {
          players.add(new SinglePanel(ui));
        }

        master.add(playersInGame = new JSpinner(new SpinnerNumberModel(MINIMUM, MINIMUM, MAXIMUM, 1)));
        master.add(date = new JDatePickerImpl(new JDatePanelImpl(new UtilDateModel()), new DateComponentFormatter()));

        for (SinglePanel p : players) {
          master.add(p);
        }

        add(master);

        bottom.add(back = new JButton(ui.getSettings().getResourceBundle().getString("goBack")));
        bottom.add(save = new JButton(ui.getSettings().getResourceBundle().getString("save")));

        add(bottom, BorderLayout.PAGE_END);

        playersInGame.addChangeListener(new ChangeListener() {
          @Override
          public void stateChanged(ChangeEvent e) {
            int num = (Integer) ((JSpinner)(e.getSource())).getValue();
            int value = players.size() - num;

            if (value < 0) {
              value = Math.abs(value);

              while (value != 0) {
                players.remove(value-1);
                value--;
              }
            } else if (value > 0) {
              while (value != 0) {
                players.add(new SinglePanel(ui));
                value--;
              }
            }

            validate();
          }
        });

        back.addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
            MatchDialog.this.dispose();
          }
        });

        save.addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
            Game tmp;
            ArrayList<PlayerStateGame> list = new ArrayList<>(players.size());

            for (SinglePanel p : players) {
              list.add(new PlayerStateGame(p.getPlayer(), p.getPoints(), p.getPelliccions(), p.getCappottens()));

              for (Player p2 : ui.getPlayers()) {
                if (p2.getUsername().equals(p.getPlayer())) {
                  p2.setScore(p2.getScore() + p.getPoints());
                  p2.setTotalPlays(p2.getTotalPlays() + 1);
                  p2.setCappottens(p2.getCappottens() + p.getCappottens());
                  p2.setPelliccions(p2.getPelliccions() + p.getPelliccions());

                  if (p.getPoints() == 10) {
                    p2.setTotalWins(p2.getTotalWins() + 1);
                  }
                }
              }
            }

            getEditPanel().initialise();

            tmp = new Game(list, (Date) date.getModel().getValue());

            ui.getData().getGame().add(tmp);
          }
        });

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setMinimumSize(new Dimension(325, 600));
        setLocationRelativeTo(ui);
        setVisible(true);
      }
    }

    private JButton addGame, removeGame;
    private JScrollPane scrollPane;
    private JTable table;
    private ProgramTable modelTable;
    private JPanel bottomPanel;

    private UserController ui;

    CalendarPanel(UserController ui) {
      super();
      setLayout(new BorderLayout());

      this.ui = ui;

      bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
      bottomPanel.add(addGame = new JButton(ui.getSettings().getResourceBundle().getString("addGame")));
      bottomPanel.add(removeGame = new JButton(ui.getSettings().getResourceBundle().getString("removeGame")));

      table = new JTable(modelTable);
      scrollPane = new JScrollPane(table);
      scrollPane.setBorder(BorderFactory.createTitledBorder(ui.getSettings().getResourceBundle().getString("matchList")));

      addGame.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          if (ui.getPlayers().size() < 3) {
            JOptionPane.showMessageDialog(ui, "Test", "Test", JOptionPane.ERROR_MESSAGE);
            //TODO here language
          } else {
            new MatchDialog(ui);
          }
        }
      });

      removeGame.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          /*
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
          */
        }
      });

      //addGame.setEnabled(false);
      removeGame.setEnabled(false);

      add(bottomPanel, BorderLayout.PAGE_END);
      add(scrollPane);
    }
  }

  class MainPagePanel extends JPanel {
    private JScrollPane leaderboard;
    private JEditorPane textArea;

    private UserController ui;

    MainPagePanel(UserController ui) {
      super();
      setLayout(new BorderLayout());

      this.ui = ui;

      try {
        leaderboard = new JScrollPane(textArea = new JEditorPane());
        textArea.setContentType("text/html");
        textArea.setText(setUpText());
        textArea.setEditable(false);
      } catch (FileNotFoundException e) {
        e.printStackTrace();
        leaderboard = new JScrollPane(new JLabel(new ExportLeaderboard(ui).generateList(ui.getPlayers())));
      }

      add(leaderboard);
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

  private JPanel bottomPanel;
  private JButton backButton, calendarButton, editButton;

  private CardLayout primaryLayout;

  ManagementPanel(UserController ui) {
    super();

    this.ui = ui;

    editPanel = new EditPanel(ui);
    calendarPanel = new CalendarPanel(ui);
    main = new MainPagePanel(ui);

    setLayout(primaryLayout = new CardLayout());

    add(main, "BACK");

    bottomPanel = new JPanel();
    backButton = new JButton(ui.getSettings().getResourceBundle().getString("goBack"));
    backButton.setEnabled(false);
    calendarButton = new JButton(new CalendarButton(ui));
    editButton = new JButton(new TabbedListButton(ui));
    bottomPanel.add(backButton);
    bottomPanel.add(calendarButton);
    bottomPanel.add(editButton);

    backButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        ui.askForNextPage("BACK");
      }
    });

    ui.add(bottomPanel, BorderLayout.PAGE_END);
  }

  void setNextPage(String next) { //TODO: optimise here
    switch (next) {
      case "CALENDAR":
        add(calendarPanel, "CALENDAR");
        backButton.setEnabled(true);
        break;
      case "EDIT":
        add(editPanel, "EDIT");
        backButton.setEnabled(true);
        break;
      case "BACK":

        backButton.setEnabled(false);
        break;
    }

    primaryLayout.show(ManagementPanel.this, next);
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
