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

class CalendarPanel extends JPanel {

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
    table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
    table.setAutoCreateRowSorter(true);
    scrollPane = new JScrollPane(table);
    scrollPane.setBorder(BorderFactory.createTitledBorder(ui.getSettings().getResourceBundle().getString("matchList")));

    table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
      @Override
      public void valueChanged(ListSelectionEvent e) {
        if (table.getSelectedRow() == -1) {
          removeGame.setEnabled(false);
          editGame.setEnabled(false);
        } else {
          removeGame.setEnabled(true);
          editGame.setEnabled(true);
        }

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
          new MatchDialog(ui);
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

          ui.getData().getGame().remove(ui.getGameIndex());
          modelTable.removeProgram(ui.getGameIndex());

          ui.getEditPanel().initialise();

          table.clearSelection();
          removeGame.setEnabled(false);

          ui.setHasBeenSaved(false);
        }
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
