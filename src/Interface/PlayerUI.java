package Interface;

import Data.Player;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;

class PlayerUI extends JPanel {
   private final String[] LABEL_STRINGS;
   private final String[] EDITABLE;

   private String usernameText;
   private JTable table;

   private ArrayList<JLabel> labels = new ArrayList<>();
   private ArrayList<JSpinner> insert = new ArrayList<>();
   private JTextField name, username;
   private UserController ui;
   private DocumentListener dl = new DocumentListener() {
      @Override
      public void insertUpdate(DocumentEvent e) {
         change(e);
      }

      @Override
      public void removeUpdate(DocumentEvent e) {
         change(e);
      }

      @Override
      public void changedUpdate(DocumentEvent e) {
         change(e);
      }

      private void change(DocumentEvent e) {
         ui.getTabs().setTitleAt(ui.getTabs().getSelectedIndex(), name.getText());

         if (e.getDocument() == name.getDocument()) {
            labels.get(0).setText(LABEL_STRINGS[0] + name.getText());
         } else {
            labels.get(1).setText(LABEL_STRINGS[1] + username.getText());
            usernameText = username.getText();
         }

         ui.setHasBeenSaved(false);
         ui.getListPlayers().updateList();
         validate();
      }
   };

   private DecimalFormat df = new DecimalFormat("##.##");

   PlayerUI(Player player, UserController ui) {
      this.ui = ui;
      this.usernameText = player.getUsername();
      this.table = new JTable(new ProgramTable(PlayerUI.this.ui, PlayerUI.this.ui.getUserData(usernameText)));

      String[] PLAYER_STRINGS = new String[]{
              player.getName(),
              player.getUsername(),
              String.valueOf(player.getScore()),
              String.valueOf(player.getPelliccions()),
              String.valueOf(player.getCappottens()),
              String.valueOf(player.getTotalPlays()),
              String.valueOf(player.getTotalWins()),
              String.valueOf(player.getTotalLost()),
              df.format((player.getPelliccions() / (float) player.getTotalPlays())),
              df.format((player.getCappottens() / (float) player.getTotalPlays())),
              df.format((player.getScore() / (float) player.getTotalPlays())),
              df.format(player.getPelliccions() / (float) sumHands()),
              df.format(player.getCappottens() / (float) sumHands()),
              "0",   //TODO fix Pellicciosity
              sumTime(),
              df.format(100*(player.getTotalWins() / (float) player.getTotalPlays())) + "%",
              df.format(100*(player.getTotalLost() / (float) player.getTotalPlays())) + "%"
      };

      LABEL_STRINGS = new String[] {
              ui.getSettings().getResourceBundle().getString("name"),
              ui.getSettings().getResourceBundle().getString("username"),
              ui.getSettings().getResourceBundle().getString("score"),
              ui.getSettings().getResourceBundle().getString("pelliccions"),
              ui.getSettings().getResourceBundle().getString("cappottens"),
              ui.getSettings().getResourceBundle().getString("totalPlays"),
              ui.getSettings().getResourceBundle().getString("totalWins"),
              ui.getSettings().getResourceBundle().getString("totalLoss"),
              ui.getSettings().getResourceBundle().getString("pellAverage"),
              ui.getSettings().getResourceBundle().getString("cappAverage"),
              ui.getSettings().getResourceBundle().getString("average"),
              ui.getSettings().getResourceBundle().getString("pelliccionsPerHand"),
              ui.getSettings().getResourceBundle().getString("cappottensPerHand"),
              ui.getSettings().getResourceBundle().getString("pellicciosity"),
              ui.getSettings().getResourceBundle().getString("playTime"),
              ui.getSettings().getResourceBundle().getString("winrate"),
              ui.getSettings().getResourceBundle().getString("lossrate")
      };

      EDITABLE = new String[] {
              ui.getSettings().getResourceBundle().getString("name"),
              ui.getSettings().getResourceBundle().getString("username"),
              ui.getSettings().getResourceBundle().getString("score"),
              ui.getSettings().getResourceBundle().getString("pelliccions"),
              ui.getSettings().getResourceBundle().getString("cappottens"),
              ui.getSettings().getResourceBundle().getString("totalPlays"),
              ui.getSettings().getResourceBundle().getString("totalWins"),
              ui.getSettings().getResourceBundle().getString("totalLoss")
      };


      df.setRoundingMode(RoundingMode.DOWN);

      name = new JTextField(PLAYER_STRINGS[0]);
      name.getDocument().addDocumentListener(dl);

      username = new JTextField(PLAYER_STRINGS[1]);
      username.getDocument().addDocumentListener(dl);

      for (int i = 0; i < LABEL_STRINGS.length; i++) {
         labels.add(new JLabel(LABEL_STRINGS[i] + PLAYER_STRINGS[i]));
      }

      for (int i = 0; i < EDITABLE.length-2; i++) {
         SpinnerNumberModel snm = new SpinnerNumberModel();
         snm.setMinimum(0);

         switch (i) {
            case 0:
               snm.setValue(player.getScore());
               break;
            case 1:
               snm.setValue(player.getPelliccions());
               break;
            case 2:
               snm.setValue(player.getCappottens());
               break;
            case 3:
               snm.setValue(player.getTotalPlays());
               break;
            case 4:
               snm.setValue(player.getTotalWins());
               snm.setMaximum((Integer) insert.get(i-1).getValue());
               break;
            case 5:
               snm.setValue(player.getTotalLost());
               snm.setMaximum((Integer) insert.get(i-2).getValue() - (Integer) insert.get(i-1).getValue());
               break;
         }

         insert.add(new JSpinner(snm));
         insert.get(insert.size()-1).addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
               for (int i = 0; i < EDITABLE.length-2; i++) {
                  if (e.getSource() == insert.get(i)) {
                     labels.get(i+2).setText(EDITABLE[i+2] + insert.get(i).getValue());
                  }
               }

               SpinnerNumberModel snm = new SpinnerNumberModel();

               if ((Integer) insert.get(3).getValue() - (Integer) insert.get(5).getValue() < (Integer) insert.get(4).getValue()) {
                  snm.setValue((Integer) insert.get(3).getValue() - (Integer) insert.get(5).getValue());
               } else {
                  snm.setValue(insert.get(4).getValue());
               }

               snm.setMinimum(0);
               snm.setMaximum((Integer) insert.get(3).getValue() - (Integer) insert.get(5).getValue());
               insert.get(4).setModel(snm);

               snm = new SpinnerNumberModel();

               if ((Integer) insert.get(3).getValue() - (Integer) insert.get(4).getValue() < (Integer) insert.get(5).getValue()) {
                  snm.setValue((Integer) insert.get(3).getValue() - (Integer) insert.get(4).getValue());
               } else {
                  snm.setValue(insert.get(5).getValue());
               }

               snm.setMinimum(0);
               snm.setMaximum((Integer) insert.get(3).getValue() - (Integer) insert.get(4).getValue());
               insert.get(5).setModel(snm);

               int sumHands = sumHands();

               labels.get(6).setText(LABEL_STRINGS[6] + insert.get(4).getValue());
               labels.get(7).setText(LABEL_STRINGS[7] + (insert.get(5).getValue()));
               labels.get(8).setText(LABEL_STRINGS[8] + df.format((Integer) insert.get(1).getValue() / (float) ((Integer) insert.get(3).getValue())));
               labels.get(9).setText(LABEL_STRINGS[9] + df.format((Integer) insert.get(2).getValue() / (float) ((Integer) insert.get(3).getValue())));
               labels.get(10).setText(LABEL_STRINGS[10] + df.format((Integer) insert.get(0).getValue() / (float) ((Integer) insert.get(3).getValue())));
               labels.get(11).setText(LABEL_STRINGS[11] + df.format((Integer) insert.get(1).getValue() / (float) sumHands));
               labels.get(12).setText(LABEL_STRINGS[12] + df.format((Integer) insert.get(2).getValue() / (float) sumHands));
               labels.get(13).setText(LABEL_STRINGS[13] + /*df.format(0)*/ "NULL");   //TODO fix Pellicciosity
               labels.get(14).setText(LABEL_STRINGS[14] + sumTime());
               labels.get(15).setText(LABEL_STRINGS[15] + df.format(100*((Integer) insert.get(4).getValue() / (float) ((Integer) insert.get(3).getValue()))) + "%");
               labels.get(16).setText(LABEL_STRINGS[16] + df.format(100*((Integer) insert.get(5).getValue() / (float) ((Integer) insert.get(3).getValue()))) + "%");

               validate();

               ui.setHasBeenSaved(false);
            }
         });
      }
   }

   JPanel generatePanel() {
      JPanel ui = new JPanel(new GridLayout(1, 3));
      JPanel tmp;
      JPanel labelList = new JPanel(new GridLayout(0, 1));
      JPanel editList = new JPanel(new GridLayout(0, 1));

      JScrollPane scrollPane = new JScrollPane(table);
      scrollPane.setBorder(BorderFactory.createTitledBorder(PlayerUI.this.ui.getSettings().getResourceBundle().getString("matchList")));

      for (JLabel data : labels) {
         labelList.add(data);
      }

      for (int i = 0; i < 2; i++) {
         tmp = new JPanel();
         tmp.add(new JLabel(EDITABLE[i]));

         switch (i) {
            case 0:
               tmp.add(name);
               break;
            case 1:
               tmp.add(username);
               break;
         }

         editList.add(tmp);
      }

      for (int i = 0; i < EDITABLE.length-2; i++) {
         tmp = new JPanel(new GridLayout(1, 2));
         tmp.add(new JLabel(EDITABLE[i+2]));
         tmp.add(insert.get(i));

         editList.add(tmp);
      }

      ui.add(labelList);
      ui.add(editList);
      ui.add(scrollPane);

      repaintTable();

      return ui;
   }

   JTextField getJTextName() {
      return name;
   }

   JTextField getUsername() {
      return username;
   }

   ArrayList<JSpinner> getInsert() {
      return insert;
   }

   private String sumTime() {
      int i = 0;

      if (table.getSelectedRow() != -1) {
         for (int j = 0; j < table.getModel().getRowCount(); j++) {
            System.out.println(((ProgramTable) table.getModel()).getTime(j));
            i += ((ProgramTable) table.getModel()).getTime(j);
         }
      }

      return (i/60) + "h " + (i - i/60) + "m";
   }

   private int sumHands() {
      int i = 0;

      if (table.getModel().getRowCount() != -1) {
         for (int j = 0; j < table.getModel().getRowCount(); j++) {
            i += (Byte) table.getModel().getValueAt(j, 1);
            // 1 = Hand, refers to ProgramTable
         }
      }

      return i;
   }

   void repaintTable() {
      table.setModel(new ProgramTable(PlayerUI.this.ui, PlayerUI.this.ui.getUserData(usernameText)));
      ((ProgramTable) table.getModel()).fireTableDataChanged();
   }
}
