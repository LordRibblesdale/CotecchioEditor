package Game;

import Data.Player;
import FileManager.SaveFile;
import Interface.UserController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

class GameProgress extends JFrame implements Serializable, Points {
   private static final long serialVersionUID = 310L;

   class PlayerPanel extends JPanel implements Comparable<PlayerPanel> {
      private JLabel username;
      private JSpinner points;
      private JRadioButton pelliccion, cappott;
      private ButtonGroup bg;
      private int pointsInGame = 0, cappottensInGame = 0, pelliccionsInGame = 0;

      PlayerPanel(String username) {
         this.username = new JLabel(username);
         points = new JSpinner(new SpinnerNumberModel(0, -16, 16, 1));
         pelliccion = new JRadioButton("Pelliccione?");
         cappott = new JRadioButton("Cappotto?");
         bg = new ButtonGroup();

         bg.add(pelliccion);
         bg.add(cappott);

         /*
         pelliccion.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
               if (e.getStateChange() == ItemEvent.SELECTED) {
                  if ((Integer) points.getValue() + 6 < 16 && !cappott.isSelected()) {
                     points.setValue((Integer) points.getValue() + 6);
                  }
               } else {
                  if ((Integer) points.getValue() - 6 > 0 && !cappott.isSelected()) {
                     points.setValue((Integer) points.getValue() - 6);
                  } else {
                     points.setValue(0);
                  }
               }

               validate();

            }
         });
         */

         cappott.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
               if (e.getStateChange() == ItemEvent.SELECTED) {
                  setCapPoints(username);
               }
            }
         });

         add(this.username);
         add(points);

         JPanel btns = new JPanel(new GridLayout(0, 1));
         btns.add(pelliccion);
         btns.add(cappott);
         add(btns);
      }

      JSpinner getPoints() {
         return points;
      }

      JRadioButton getPelliccion() {
         return pelliccion;
      }

      JRadioButton getCappott() {
         return cappott;
      }

      JLabel getUsername() {
         return username;
      }

      private ButtonGroup getBg() {
         return bg;
      }

      int getPointsInGame() {
         return pointsInGame;
      }

      private void setPointsInGame(int pointsInGame) {
         this.pointsInGame = pointsInGame;
      }

      int getCappottensInGame() {
         return cappottensInGame;
      }

      private void setCappottensInGame(int cappottensInGame) {
         this.cappottensInGame = cappottensInGame;
      }

      int getPelliccionsInGame() {
         return pelliccionsInGame;
      }

      private void setPelliccionsInGame(int pelliccionsInGame) {
         this.pelliccionsInGame = pelliccionsInGame;
      }

      @Override
      public int compareTo(PlayerPanel o) {
         return Integer.compare(this.getPointsInGame(), o.getPointsInGame());
      }
   }

   private ArrayList<PlayerPanel> players;
   private ArrayList<Player> playerArrayList;
   private JTextArea log;
   private JScrollPane scrollPane;
   private JButton next, reset;
   private JPanel bottom, btn;
   private ButtonGroup bg;
   private transient UserController ui;

   private transient ActionListener nextL = new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
         StringBuilder s = new StringBuilder();

         for (PlayerPanel pl : players) {
            for (Player p : playerArrayList) {
               if (p.getUsername().equals(pl.getUsername().getText())) {
                  if (pl.getPelliccion().isSelected()) {
                     p.setPelliccions(p.getPelliccions()+1);
                     pl.setPelliccionsInGame(pl.getPelliccionsInGame()+1);
                  }

                  if (pl.getCappott().isSelected()) {
                     p.setCappottens(p.getCappottens()+1);
                     pl.setCappottensInGame(pl.getCappottensInGame()+1);
                  }
               }
            }

            pl.setPointsInGame(pl.getPointsInGame() + (Integer) pl.getPoints().getValue());

            s.append(pl.getUsername().getText()).append(" @ ").append(pl.getPointsInGame()).append(", ").append(pl.getPelliccionsInGame()).append("P & ").append(pl.getCappottensInGame()).append("C\t");

            if (pl.getPointsInGame() >= 101) {
               isGameOver = true;
            }
         }

         log.append(s.toString() + "\n");

         for (PlayerPanel pl : players) {
            pl.getPoints().setValue(0);
            pl.getBg().clearSelection();
         }

         bg.clearSelection();

         if (isGameOver) { //TODO improve code here
            int pos = 0;
            ui.setPlayers(playerArrayList);

            Collections.sort(players);

            for (PlayerPanel pl : players) {
               for (Player player : playerArrayList) {
                  //TODO fix here
                  if (pl.getUsername().getText().equals(player.getUsername())) {
                     if (pos > 0 && pl.getPointsInGame() == players.get(pos-1).getPointsInGame()) {
                        switch (players.size()) {
                           case 3:
                              player.setScore(player.getScore() + THREE[pos-1]);
                              break;
                           case 4:
                              player.setScore(player.getScore() + FOUR[pos-1]);
                              break;
                           case 5:
                              player.setScore(player.getScore() + FIVE[pos-1]);
                              break;
                           case 6:
                              player.setScore(player.getScore() + SIX[pos-1]);
                              break;
                        }
                     } else {
                        switch (players.size()) {
                           case 3:
                              player.setScore(player.getScore() + THREE[pos]);
                              break;
                           case 4:
                              player.setScore(player.getScore() + FOUR[pos]);
                              break;
                           case 5:
                              player.setScore(player.getScore() + FIVE[pos]);
                              break;
                           case 6:
                              player.setScore(player.getScore() + SIX[pos]);
                              break;
                        }
                     }

                     player.setTotalPlays(player.getTotalPlays()+1);
                     player.setTotalWins(pos++ == 0 ? player.getTotalWins()+1 : player.getTotalWins());
                  }
               }
            }

            ui.setPlayers(playerArrayList);

            JOptionPane.showMessageDialog(GameProgress.this, "Game over, results have been written.");

            int choice = JOptionPane.showConfirmDialog(GameProgress.this, "Do you want to save?", "Save?",
                    JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);

            switch (choice) {
               case JOptionPane.OK_OPTION:
                  new SaveFile(ui).actionPerformed(e);
                  break;
            }
         }
      }
   };

   private transient WindowAdapter windowAdapter = new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent e) {
         if (!isGameOver) {
            int sel = JOptionPane.showConfirmDialog(GameProgress.this, "The game is still playing.\nDo you want to save before closing?", "Save?",
                    JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null);

            switch (sel) {
               case JOptionPane.OK_OPTION:
                  (new SaveGameFile(GameProgress.this)).actionPerformed(null);
                  break;
               case JOptionPane.NO_OPTION:
                  dispose();
                  break;
               case JOptionPane.CANCEL_OPTION:
                  setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            }
         } else {
            dispose();
         }
      }
   };

   private transient ActionListener resetL = new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
         reset();
      }
   };

   private boolean isGameOver = false;

   GameProgress(UserController ui, ArrayList<Player> playerArrayList) {
      super("Game Point Simulator");
      this.ui = ui;

      log = new JTextArea();
      log.setEditable(false);
      log.setFont(Font.getFont("Times New Roman", GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts()[0]).deriveFont(16f));
      scrollPane = new JScrollPane(log);
      add(scrollPane);

      players = new ArrayList<>();
      this.playerArrayList = playerArrayList;
      bottom = new JPanel();

      ArrayList<Object> usernames = ui.getUsernames();

      if (usernames.size() < 3) {
         JOptionPane.showMessageDialog(ui, "There aren't enough players to play simulation", "Too few players", JOptionPane.WARNING_MESSAGE);
         dispose();
      } else {
         ArrayList<Object> max = new ArrayList<>();
         for (int i = 3; i <= usernames.size() && i < 8; i++) {
            max.add(i);
         }

         Object num = JOptionPane.showInputDialog(ui, "How many players?", "Game Points Simulator",
                 JOptionPane.INFORMATION_MESSAGE, null, max.toArray(), max.toArray()[0]);


         for (int i = 0; i < (Integer) num; i++) {
            Object user = JOptionPane.showInputDialog(ui, "Insert username player", "Choose username",
                    JOptionPane.INFORMATION_MESSAGE, null, usernames.toArray(), usernames.toArray()[0]);
            players.add(new PlayerPanel((String) user));
            bottom.add(players.get(i));
            bottom.add(new JSeparator(SwingConstants.VERTICAL));
            usernames.remove(user);
         }

         fixButtons(bg = new ButtonGroup(), players);

         btn = new JPanel(new GridLayout(0, 1));
         btn.add(reset = new JButton("Reset Points"));
         btn.add(next = new JButton("Next match"));

         next.addActionListener(nextL);

         reset.addActionListener(resetL);

         bottom.add(btn);
         add(bottom, BorderLayout.PAGE_END);
         setSize(new Dimension(1200, 720));

         addWindowListener(windowAdapter);

         setLocationRelativeTo(ui);
         setVisible(true);
      }
   }

   GameProgress(UserController ui, ArrayList<Player> playerArrayList, ArrayList<PlayerPanel> players,
                JTextArea log, ArrayList<Object> usernames) {
      super("Game Point Simulator");
      this.ui = ui;

      this.log = new JTextArea(log.getText());
      this.log.setEditable(false);
      this.log.setFont(Font.getFont("Times New Roman", GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts()[0]).deriveFont(16f));
      scrollPane = new JScrollPane(this.log);
      add(scrollPane);

      this.players = new ArrayList<>();
      this.playerArrayList = playerArrayList;

      for (Player p : this.playerArrayList) {
         for (PlayerPanel pl : players) {
            if (p.getUsername().equals(pl.getUsername().getText())) {
               p.setCappottens(p.getCappottens() + pl.getCappottensInGame());
               p.setPelliccions(p.getPelliccions() + pl.getPelliccionsInGame());
            }
         }
      }

      bottom = new JPanel();

      System.out.println(usernames);

      for (Object s : usernames) {
         this.players.add(new PlayerPanel((String) s));
         this.players.get(this.players.size()-1).setPointsInGame(players.get(this.players.size()-1).getPointsInGame());
         this.players.get(this.players.size()-1).setPelliccionsInGame(players.get(this.players.size()-1).getPelliccionsInGame());
         this.players.get(this.players.size()-1).setCappottensInGame(players.get(this.players.size()-1).getCappottensInGame());
         bottom.add(this.players.get(this.players.size()-1));
         bottom.add(new JSeparator(SwingConstants.VERTICAL));
      }

      fixButtons(bg = new ButtonGroup(), this.players);

      btn = new JPanel(new GridLayout(0, 1));
      btn.add(reset = new JButton("Reset Points"));
      btn.add(next = new JButton("Next match"));

      next.addActionListener(nextL);
      reset.addActionListener(resetL);

      bottom.add(btn);
      add(bottom, BorderLayout.PAGE_END);
      setSize(new Dimension(1200, 720));

      addWindowListener(windowAdapter);

      setLocationRelativeTo(ui);
      setVisible(true);
   }


   private void setCapPoints(String usr) {
      for (PlayerPanel p : players) {
         if (!p.username.getText().equals(usr)) {
            p.getPoints().setValue(16);
         } else {
            p.getPoints().setValue(-16);
         }
      }

      validate();
   }

   private void reset() {
      for (PlayerPanel pl : players) {
         pl.getPoints().setValue(0);
         pl.getBg().clearSelection();
      }

      validate();
   }

   private void fixButtons(ButtonGroup bg, ArrayList<PlayerPanel> players) {
      for (PlayerPanel p : players) {
         bg.add(p.getCappott());
         bg.add(p.getPelliccion());
      }
   }

   JTextArea getLog() {
      return log;
   }

   ArrayList<Object> getUsernames() {
      ArrayList<Object> tmp = new ArrayList<>();

      for (PlayerPanel pl : players) {
         tmp.add(pl.getUsername().getText());
      }

      return tmp;
   }

   ArrayList<PlayerPanel> getPlayers() {
      return players;
   }
}
