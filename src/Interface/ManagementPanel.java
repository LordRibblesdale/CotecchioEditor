package Interface;

import Data.CotecchioDataArray;
import Data.Game;
import Data.Player;
import Data.PlayerStateGame;
import Export.ExportLeaderboard;
import FileManager.SaveFile;
import com.github.lgooddatepicker.components.DatePicker;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.*;
import java.util.List;

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
          tabs.setSelectedIndex(tabs.getTabCount()-1);
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

    void setUpData() {
      for (int i = 0; i < ui.getData().getPlayers().size(); i++) {
        ui.getData().getPlayers().set(i, new Player(pUI.get(i).getJTextName().getText(),
                pUI.get(i).getUsername().getText(),
                (Integer) (pUI.get(i).getInsert().get(0).getValue()),
                (Integer) (pUI.get(i).getInsert().get(1).getValue()),
                (Integer) (pUI.get(i).getInsert().get(2).getValue()),
                (Integer) (pUI.get(i).getInsert().get(3).getValue()),
                (Integer) (pUI.get(i).getInsert().get(4).getValue())));
      }
    }

    void setData(CotecchioDataArray data) {
      ui.setData(data);
    }

    void setPlayers(ArrayList<Player> players) {
      ui.getData().getPlayers().clear();
      ui.getData().getPlayers().addAll(players);
    }

    ArrayList<String> getUsernames() {
      ArrayList<String> usernames = new ArrayList<>();

      for (Player p : ui.getData().getPlayers()) {
        usernames.add(p.getUsername());
      }

      return usernames;
    }

    ArrayList<String> getNames() {
      ArrayList<String> usernames = new ArrayList<>();

      for (Player p : ui.getData().getPlayers()) {
        usernames.add(p.getName());
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
        class ComboBoxToolTipRenderer extends DefaultListCellRenderer {
          List<String> tooltips;

          @Override
          public Component getListCellRendererComponent(JList list, Object value,
                                                        int index, boolean isSelected, boolean cellHasFocus) {

            JComponent comp = (JComponent) super.getListCellRendererComponent(list,
                    value, index, isSelected, cellHasFocus);

            if (-1 < index && null != value && null != tooltips) {
              list.setToolTipText(tooltips.get(index));
            }
            return comp;
          }

          public void setTooltips(List<String> tooltips) {
            this.tooltips = tooltips;
          }
        }

        private JComboBox<String> players;
        private ComboBoxToolTipRenderer toolTip;
        private JSpinner points, pelliccions, cappottens;

        SinglePanel(UserController ui) {
          super(new GridLayout(0, 2));

          add(players = new JComboBox<>(ui.getUsernames().toArray(new String[0])));
          toolTip = new ComboBoxToolTipRenderer();
          toolTip.setTooltips(ui.getNames());
          players.setRenderer(toolTip);

          add(new JSeparator());
          add(new JLabel(ui.getSettings().getResourceBundle().getString("pointsMatch")));
          add(points = new JSpinner(new SpinnerNumberModel(0, 0, 10, 1)));
          add(new JLabel(ui.getSettings().getResourceBundle().getString("pelliccions")));
          add(pelliccions = new JSpinner(new SpinnerNumberModel(0, 0, 100, 1)));
          add(new JLabel(ui.getSettings().getResourceBundle().getString("cappottens")));
          add(cappottens = new JSpinner(new SpinnerNumberModel(0, 0, 100, 1)));
        }

        SinglePanel(UserController ui, PlayerStateGame playerStateGame) {
          super(new GridLayout(0, 2));

          add(players = new JComboBox<>(ui.getUsernames().toArray(new String[0])));
          toolTip = new ComboBoxToolTipRenderer();
          toolTip.setTooltips(ui.getNames());
          players.setRenderer(toolTip);

          players.setSelectedIndex(ui.getUsernames().indexOf(playerStateGame.getUsername()));

          add(new JSeparator());
          add(new JLabel(ui.getSettings().getResourceBundle().getString("pointsMatch")));
          add(points = new JSpinner(new SpinnerNumberModel(playerStateGame.getPointsEndGame(), 0, 10, 1)));
          add(new JLabel(ui.getSettings().getResourceBundle().getString("pelliccions")));
          add(pelliccions = new JSpinner(new SpinnerNumberModel(playerStateGame.getPelliccionsTaken(), 0, 100, 1)));
          add(new JLabel(ui.getSettings().getResourceBundle().getString("cappottens")));
          add(cappottens = new JSpinner(new SpinnerNumberModel(playerStateGame.getCappottensTaken(), 0, 100, 1)));
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

      private final int MINIMUM = 3;
      private int MAXIMUM = 8;
      private int currentIndex;

      private boolean isGameSet = false;
      private int index = -1;

      MatchDialog(UserController ui, Game game, int index) {
        super(ui, true);
        setLayout(new BorderLayout());

        isGameSet = game != null;
        this.index = index;

        MAXIMUM = game == null
                ? (MAXIMUM <= ui.getData().getPlayers().size()
                    ? MAXIMUM
                    : ui.getData().getPlayers().size())
                : game.getResults().size();
        master = new JPanel(new GridLayout(0, 1));
        bottom = new JPanel();

        if (game == null) {
          players = new ArrayList<>(MINIMUM);
        } else {
          players = new ArrayList<>(game.getResults().size());
        }

        currentIndex = players.size();

        if (game == null) {
          for (int i = 0; i < MINIMUM; i++) {
            players.add(new SinglePanel(ui));
          }
        } else {
          for (int i = 0; i < game.getResults().size(); i++) {
            players.add(new SinglePanel(ui, game.getResults().get(i)));
          }
        }

        master.add(playersInGame = new JSpinner(new SpinnerNumberModel(MINIMUM,
                game == null ? MINIMUM : players.size(),
                MAXIMUM,
                1)));

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

            if (currentIndex > num) {
              master.remove(players.get(num-1));
              players.remove(num-1);
            } else if (currentIndex < num) {
              players.add(new SinglePanel(ui));
              master.add(players.get(num-1));
            }

            currentIndex = num;

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
            //TODO language here

            DatePicker calendar = new DatePicker();
            JButton add = new JButton(ui.getSettings().getResourceBundle().getString("save"));
            JButton exit = new JButton(ui.getSettings().getResourceBundle().getString("goBack"));
            JDialog dialog = new JDialog(MatchDialog.this, true);
            dialog.setLayout(new GridLayout(0, 1));

            calendar.setText(ui.getSettings().getResourceBundle().getString("selectDate"));

            add.addActionListener(new ActionListener() {
              @Override
              public void actionPerformed(ActionEvent e) {
                Game tmp;
                ArrayList<PlayerStateGame> list = new ArrayList<>(players.size());
                byte hands = 0;

                for (SinglePanel p : players) {
                  list.add(new PlayerStateGame(p.getPlayer(), p.getPoints(), p.getPelliccions(), p.getCappottens()));
                  hands += (p.getPelliccions() + p.getCappottens());

                  for (Player p2 : ui.getPlayers()) {
                    if (p2.getUsername().equals(p.getPlayer())) {
                      System.out.println(p2.getUsername());

                      System.out.print("Da " + p2.getScore() + " a ");
                      p2.setScore(p2.getScore() + p.getPoints());
                      System.out.println(p2.getScore() + " (+" + p.getPoints());

                      System.out.print("Da " + p2.getTotalPlays() + " a ");
                      p2.setTotalPlays(p2.getTotalPlays() + 1);
                      System.out.println(p2.getTotalPlays() + " (+1)");

                      System.out.print("Da " + p2.getCappottens() + " a ");
                      p2.setCappottens(p2.getCappottens() + p.getCappottens());
                      System.out.println(p2.getCappottens() + " (+" + p.getCappottens());

                      System.out.print("Da " + p2.getPelliccions() + " a ");
                      p2.setPelliccions(p2.getPelliccions() + p.getPelliccions());
                      System.out.println(p2.getPelliccions() + " (+" + p.getPelliccions());

                      if (p.getPoints() == 10) {
                        p2.setTotalWins(p2.getTotalWins() + 1);
                      }
                    }
                  }
                }

                getEditPanel().initialise();

                if (!isGameSet) {
                  tmp = new Game(list, calendar.getDate(), hands, true);

                  ui.getData().getGame().add(tmp);
                  ui.getAbstractTable().addProgram(tmp);
                } else {

                }

                MatchDialog.this.dispose();
                ui.setHasBeenSaved(false);
              }
            });

            exit.addActionListener(new ActionListener() {
              @Override
              public void actionPerformed(ActionEvent e) {
                dialog.dispose();
              }
            });

            dialog.add(calendar);
            dialog.add(exit);
            dialog.add(add);
            dialog.setMinimumSize(new Dimension(150, 100));
            dialog.pack();
            dialog.setLocationRelativeTo(ui);
            dialog.setVisible(true);
            dialog.requestFocus();
          }
        });

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setMinimumSize(new Dimension(325, 600));
        setLocationRelativeTo(ui);
        setVisible(true);
      }
    }

    private JButton addGame, removeGame, editGame;
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
      bottomPanel.add(editGame = new JButton(ui.getSettings().getResourceBundle().getString("editGame")));
      bottomPanel.add(removeGame = new JButton(ui.getSettings().getResourceBundle().getString("removeGame")));

      table = new JTable(modelTable = new ProgramTable(ui, ui.getData().getGame().toArray(new Game[0])));
      scrollPane = new JScrollPane(table);
      scrollPane.setBorder(BorderFactory.createTitledBorder(ui.getSettings().getResourceBundle().getString("matchList")));

      table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
        @Override
        public void valueChanged(ListSelectionEvent e) {
          removeGame.setEnabled(true);
          editGame.setEnabled(true);
        }
      });

      addGame.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          if (ui.getPlayers().size() < 3) {
            JOptionPane.showMessageDialog(ui,
                    ui.getSettings().getResourceBundle().getString("notEnoughPlayersText"),
                    ui.getSettings().getResourceBundle().getString("notEnoughPlayersTitle"),
                    JOptionPane.ERROR_MESSAGE);
          } else {
            new MatchDialog(ui, null, -1);
          }
        }
      });

      removeGame.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          int result = JOptionPane.showConfirmDialog(ui,
                  ui.getSettings().getResourceBundle().getString("askDeletingGame"),
                  ui.getSettings().getResourceBundle().getString("warning"),
                  JOptionPane.YES_NO_OPTION);
          if (result == JOptionPane.YES_OPTION) {
            for (PlayerStateGame p : ui.getData().getGame().get(table.getSelectedRow()).getResults()) {
              for (Player p1 : ui.getData().getPlayers()) {
                if (p.getUsername().equals(p1.getUsername())) {
                  p1.setScore(p1.getScore() - p.getPointsEndGame());
                  p1.setCappottens(p1.getCappottens() - p.getCappottensTaken());
                  p1.setPelliccions(p1.getPelliccions() - p.getPelliccionsTaken());
                  p1.setTotalPlays(p1.getTotalPlays() - p.getPointsEndGame());

                  if (p.getPointsEndGame() == 10) {
                    p1.setTotalWins(p1.getTotalWins() - 1);
                  }
                }
              }
            }

            ui.getData().getGame().remove(table.getSelectedRow());
            modelTable.removeProgram(table.getSelectedRow());

            getEditPanel().initialise();
            ui.setHasBeenSaved(false);
          }
        }
      });

      editGame.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          new MatchDialog(ui, ui.getData().getGame().get(table.getSelectedRow()), table.getSelectedRow());
        }
      });

      removeGame.setEnabled(false);
      editGame.setEnabled(false);

      add(bottomPanel, BorderLayout.PAGE_END);
      add(scrollPane);
    }

    ProgramTable getModelTable() {
      return modelTable;
    }

    JTable getTable() {
      return table;
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

      leaderboard = new JScrollPane(textArea = new JEditorPane());
      textArea.setContentType("text/html");
      textArea.setText(setUpText());
      textArea.setEditable(false);

      add(leaderboard);
    }

    String setUpText() {
      StringBuilder text = new StringBuilder();
      Scanner s = new Scanner(new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("messageIT"))));

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

  EditPanel getEditPanel() {
    return editPanel;
  }

  CalendarPanel getCalendarPanel() {
    return calendarPanel;
  }
}
