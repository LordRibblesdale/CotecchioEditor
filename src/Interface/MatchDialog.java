package Interface;

import Data.Game;
import Data.Player;
import Data.PlayerStateGame;
import com.github.lgooddatepicker.components.DatePicker;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

class MatchDialog extends JDialog {
  private JPanel master;
  private JPanel bottom;
  private JButton back, save;
  private JSpinner playersInGame;
  private ArrayList<SinglePanel> players;

  private final int WIDTH = 343;

  private final int MINIMUM = 3;
  private int MAXIMUM = 8;
  private int currentIndex;
  private boolean isEditing;
  private Game game;

  MatchDialog(UserController ui) {
    super(ui, ui.getSettings().getResourceBundle().getString("addGame"), true);
    setLayout(new BorderLayout());

    isEditing = false;
    master = new JPanel();
    master.setLayout(new BoxLayout(master, BoxLayout.Y_AXIS));
    bottom = new JPanel();

    players = new ArrayList<>(MINIMUM);

    currentIndex = players.size();

    JPanel p = new JPanel();
    p.add(new JLabel(ui.getSettings().getResourceBundle().getString("playersNumber")));
    p.add(playersInGame = new JSpinner(new SpinnerNumberModel(MINIMUM, MINIMUM, MAXIMUM, 1)));
    master.add(p);

    for (int i = 0; i < MINIMUM; i++) {
      players.add(new SinglePanel(ui));
      master.add(players.get(i));
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

        pack();
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
        DatePicker calendar = new DatePicker();
        JButton add = new JButton(ui.getSettings().getResourceBundle().getString("save"));
        JButton exit = new JButton(ui.getSettings().getResourceBundle().getString("goBack"));
        JLabel hoursTxt = new JLabel(ui.getSettings().getResourceBundle().getString("hoursText"));
        JSpinner hours = new JSpinner(new SpinnerNumberModel(0, 0, 24, 1));
        JLabel minutesText = new JLabel(ui.getSettings().getResourceBundle().getString("minutesText"));
        JSpinner minutes = new JSpinner(new SpinnerNumberModel(0, 0, 60, 5));
        JDialog dialog = new JDialog(MatchDialog.this, true);
        dialog.setLayout(new GridLayout(0, 1));

        calendar.setText(ui.getSettings().getResourceBundle().getString("selectDate"));

        minutes.addChangeListener(new ChangeListener() {
          @Override
          public void stateChanged(ChangeEvent e) {
            if ((Integer) ((JSpinner) e.getSource()).getValue() == 60) {
              if ((Integer) hours.getValue() + 1 < 24) {
                hours.setValue((Integer) hours.getValue() + 1);
              }

              minutes.setValue(0);

              validate();
            }
          }
        });

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
                  p2.setScore(p2.getScore() + p.getPoints());
                  p2.setTotalPlays(p2.getTotalPlays() + 1);
                  p2.setCappottens(p2.getCappottens() + p.getCappottens());
                  p2.setPelliccions(p2.getPelliccions() + p.getPelliccions());

                  if (p.getPoints() == 10) {
                    p2.setTotalWins(p2.getTotalWins() + 1);
                  } else if (p.getPoints() == 0) {
                    p2.setTotalLost(p2.getTotalLost() + 1);
                  }
                }
              }
            }

            ui.getEditPanel().initialise();

            tmp = new Game(list, calendar.getDate(), hands,
                ((60*(Integer) hours.getModel().getValue() + (Integer) minutes.getModel().getValue())),
                true, false);

            ui.getData().getGame().add(tmp);
            ui.getAbstractTable().addProgram(tmp);

            ui.getTable().clearSelection();
            ui.setHasBeenSaved(false);

            MatchDialog.this.dispose();
          }
        });

        exit.addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
            dialog.dispose();
          }
        });

        dialog.add(calendar);
        dialog.add(hoursTxt);
        dialog.add(hours);
        dialog.add(minutesText);
        dialog.add(minutes);
        dialog.add(exit);
        dialog.add(add);
        dialog.setMinimumSize(new Dimension(160, 100));
        dialog.pack();
        dialog.setLocationRelativeTo(ui);
        dialog.setVisible(true);
        dialog.requestFocus();
      }
    });

    setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    setMinimumSize(new Dimension(WIDTH, 200));
    setResizable(false);
    pack();
    setLocationRelativeTo(ui);
    setVisible(true);
  }

  MatchDialog(UserController ui, Game game) {
    super(ui, ui.getSettings().getResourceBundle().getString("addGame"), true);
    setLayout(new BorderLayout());

    isEditing = true;
    this.game = game;
    master = new JPanel();
    master.setLayout(new BoxLayout(master, BoxLayout.Y_AXIS));
    bottom = new JPanel();

    players = new ArrayList<>(game.getResults().size());

    currentIndex = game.getResults().size();
    //TODO Fix EditGame

    JPanel p = new JPanel();
    p.add(new JLabel(ui.getSettings().getResourceBundle().getString("playersNumber")));
    p.add(playersInGame = new JSpinner(new SpinnerNumberModel(game.getResults().size(), MINIMUM, MAXIMUM, 1)));
    master.add(p);

    for (int i = 0; i < currentIndex; i++) {
      players.add(new SinglePanel(ui, game.getResults().get(i), true));
      master.add(players.get(i));
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

        pack();
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
        DatePicker calendar = new DatePicker();
        calendar.setDate(game.getDate());
        JButton add = new JButton(ui.getSettings().getResourceBundle().getString("save"));
        JButton exit = new JButton(ui.getSettings().getResourceBundle().getString("goBack"));
        JLabel hoursTxt = new JLabel(ui.getSettings().getResourceBundle().getString("hoursText"));
        JSpinner hours = new JSpinner(new SpinnerNumberModel(game.getTime()/60, 0, 24, 1));
        JLabel minutesText = new JLabel(ui.getSettings().getResourceBundle().getString("minutesText"));
        JSpinner minutes = new JSpinner(new SpinnerNumberModel(game.getTime() - (game.getTime()/60)*60, 0, 60, 5));
        JDialog dialog = new JDialog(MatchDialog.this, true);
        dialog.setLayout(new GridLayout(0, 1));

        calendar.setText(ui.getSettings().getResourceBundle().getString("selectDate"));

        minutes.addChangeListener(new ChangeListener() {
          @Override
          public void stateChanged(ChangeEvent e) {
            if ((Integer) ((JSpinner) e.getSource()).getValue() == 60) {
              if ((Integer) hours.getValue() + 1 < 24) {
                hours.setValue((Integer) hours.getValue() + 1);
              }

              minutes.setValue(0);

              validate();
            }
          }
        });

        add.addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
            for (PlayerStateGame p : ui.getData().getGame().get(ui.getGameIndex()).getResults()) {
              for (Player p1 : ui.getData().getPlayers()) {
                if (p.getUsername().equals(p1.getUsername())) {
                  p1.setScore(p1.getScore() - p.getPointsEndGame());
                  p1.setCappottens(p1.getCappottens() - p.getCappottensTaken());
                  p1.setPelliccions(p1.getPelliccions() - p.getPelliccionsTaken());
                  p1.setTotalPlays(p1.getTotalPlays() - 1);

                  if (p.getPointsEndGame() == 10) {
                    p1.setTotalWins(p1.getTotalWins() - 1);
                  } else if (p.getPointsEndGame() == 0) {
                    p1.setTotalLost(p1.getTotalLost() - 1);
                  }
                }
              }
            }

            Game tmp;
            ArrayList<PlayerStateGame> list = new ArrayList<>(players.size());
            byte hands = 0;

            for (SinglePanel p : players) {
              list.add(new PlayerStateGame(p.getPlayer(), p.getPoints(), p.getPelliccions(), p.getCappottens()));
              hands += (p.getPelliccions() + p.getCappottens());

              for (Player p2 : ui.getPlayers()) {
                if (p2.getUsername().equals(p.getPlayer())) {
                  p2.setScore(p2.getScore() + p.getPoints());
                  p2.setTotalPlays(p2.getTotalPlays() + 1);
                  p2.setCappottens(p2.getCappottens() + p.getCappottens());
                  p2.setPelliccions(p2.getPelliccions() + p.getPelliccions());

                  if (p.getPoints() == 10) {
                    p2.setTotalWins(p2.getTotalWins() + 1);
                  } else if (p.getPoints() == 0) {
                    p2.setTotalLost(p2.getTotalLost() + 1);
                  }
                }
              }
            }

            ui.getEditPanel().initialise();

            tmp = new Game(list, calendar.getDate(), hands,
                60*(Integer) hours.getModel().getValue() + (Integer) minutes.getModel().getValue(),
                true, false);

            ui.getData().getGame().set(ui.getGameIndex(), tmp);
            ui.getAbstractTable().editProgram(ui.getGameIndex(), tmp);

            ui.getTable().clearSelection();
            ui.getCalendarPanel().getRemoveGame().setEnabled(false);
            ui.setUpData(false);
            ui.setHasBeenSaved(false);

            MatchDialog.this.dispose();
          }
        });

        exit.addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
            dialog.dispose();
          }
        });

        dialog.add(calendar);
        dialog.add(hoursTxt);
        dialog.add(hours);
        dialog.add(minutesText);
        dialog.add(minutes);
        dialog.add(exit);
        dialog.add(add);
        dialog.setMinimumSize(new Dimension(160, 100));
        dialog.pack();
        dialog.setLocationRelativeTo(ui);
        dialog.setVisible(true);
        dialog.requestFocus();
      }
    });

    setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    setMinimumSize(new Dimension(WIDTH, 200));
    setResizable(false);
    pack();
    setLocationRelativeTo(ui);
    setVisible(true);
  }
}
