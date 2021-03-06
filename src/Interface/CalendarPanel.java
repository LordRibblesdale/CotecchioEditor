package Interface;

import Data.Game;
import Data.Player;
import Data.PlayerStateGame;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

class CalendarPanel extends JPanel {
  private JButton addGame, removeGame, removeAll, editGame;
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
    bottomPanel.add(removeAll = new JButton(ui.getSettings().getResourceBundle().getString("removeAllGames")));

    table = new JTable(modelTable = new ProgramTable(ui, ui.getData().getGame().toArray(new Game[0])));
    table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
    table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    table.setAutoCreateRowSorter(true);
    scrollPane = new JScrollPane(table);
    scrollPane.setBorder(BorderFactory.createTitledBorder(ui.getSettings().getResourceBundle().getString("matchList")));

    //TODO create new extended JTable class
    table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
      @Override
      public void valueChanged(ListSelectionEvent e) {
        if (table.getSelectedRow() == -1) {
          removeGame.setEnabled(false);
          editGame.setEnabled(false);
        } else {
          removeGame.setEnabled(true);

          boolean isEqual = true;
          for (PlayerStateGame p : ui.getData().getGame().get(ui.getGameIndex()).getResults()) {
            isEqual = false;
            for (Player pl : ui.getPlayers()) {
              if (p.getUsername().equals(pl.getUsername())) {
                isEqual = true;
              }
            }

            if (!isEqual) {
              break;
            }
          }

          if (!isEqual) {
            editGame.setEnabled(false);
          } else {
            editGame.setEnabled(true);
          }
        }
      }
    });

    table.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        super.mouseClicked(e);

        if (e.getClickCount() == 2) {
          new MatchInfoPanel(ui, ui.getData().getGame().get(ui.getGameIndex()));
        }
      }
    });

    addGame.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (ui.getPlayers().size() < 3) {
          JOptionPane.showMessageDialog(ui.getFrame(),
              ui.getSettings().getResourceBundle().getString("notEnoughPlayersText"),
              ui.getSettings().getResourceBundle().getString("notEnoughPlayersTitle"),
              JOptionPane.ERROR_MESSAGE);
        } else {
          new MatchDialog(ui);
        }
      }
    });

    removeGame.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        int result = JOptionPane.showConfirmDialog(ui.getFrame(),
            ui.getSettings().getResourceBundle().getString("askDeletingGame"),
            ui.getSettings().getResourceBundle().getString("warning"),
            JOptionPane.YES_NO_OPTION);

        if (result == JOptionPane.YES_OPTION) {
          for (PlayerStateGame p : ui.getData().getGame().get(ui.getGameIndex()).getResults()) {
            for (Player p1 : ui.getPlayers()) {
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

          ui.getEditPanel().initialise();

          ui.getData().getGame().remove(ui.getGameIndex());
          ui.getAbstractTable().removeProgram(ui.getGameIndex());

          table.clearSelection();
          ui.setUpData(false);

          removeGame.setEnabled(false);

          ui.setHasBeenSaved(false);
        }
      }
    });

    removeAll.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        ui.getData().getGame().clear();
        modelTable.removeAll();

        table.clearSelection();
        ui.setUpData(false);

        removeGame.setEnabled(false);

        ui.setHasBeenSaved(false);
      }
    });

    editGame.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        new MatchDialog(ui, ui.getData().getGame().get(ui.getGameIndex()));
      }
    });

    removeGame.setEnabled(false);
    editGame.setEnabled(false);

    add(bottomPanel, BorderLayout.PAGE_END);
    add(scrollPane);
  }

  void setModelTable(ProgramTable modelTable) {
    this.modelTable = modelTable;
  }

  ProgramTable getModelTable() {
    return modelTable;
  }

  JTable getTable() {
    return table;
  }

  JButton getRemoveGame() {
    return removeGame;
  }
}
