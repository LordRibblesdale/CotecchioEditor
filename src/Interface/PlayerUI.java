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
   private static int LABELS_LENGTH = 11;
   private static int INSERT_LABELS = 5;
   private final String[] LABEL_STRINGS;
   private final String[] EDITABLE;

   private String usernameText;
   private JTable table;

   private ArrayList<JLabel> labels = new ArrayList<>(LABELS_LENGTH);
   private ArrayList<JSpinner> insert = new ArrayList<>(INSERT_LABELS);
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

      String[] PLAYER_STRINGS = new String[]{
              player.getName(),
              player.getUsername(),
              String.valueOf(player.getScore()),
              String.valueOf(player.getPelliccions()),
              String.valueOf(player.getCappottens()),
              String.valueOf(player.getTotalPlays()),
              String.valueOf(player.getTotalWins()),
              String.valueOf((player.getTotalPlays() - player.getTotalWins())),
              df.format((player.getPelliccions() / (float) player.getTotalPlays())),
              df.format((player.getCappottens() / (float) player.getTotalPlays())),
              df.format((player.getScore() / (float) player.getTotalPlays()))
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
              ui.getSettings().getResourceBundle().getString("average")
      };

      EDITABLE = new String[] {
              ui.getSettings().getResourceBundle().getString("name"),
              ui.getSettings().getResourceBundle().getString("username"),
              ui.getSettings().getResourceBundle().getString("score"),
              ui.getSettings().getResourceBundle().getString("pelliccions"),
              ui.getSettings().getResourceBundle().getString("cappottens"),
              ui.getSettings().getResourceBundle().getString("totalPlays"),
              ui.getSettings().getResourceBundle().getString("totalWins"),
      };


      df.setRoundingMode(RoundingMode.DOWN);

      name = new JTextField(PLAYER_STRINGS[0]);
      name.getDocument().addDocumentListener(dl);

      username = new JTextField(PLAYER_STRINGS[1]);
      username.getDocument().addDocumentListener(dl);

      for (int i = 0; i < LABEL_STRINGS.length; i++) {
         labels.add(new JLabel(LABEL_STRINGS[i] + PLAYER_STRINGS[i]));
      }

      for (int i = 0; i < INSERT_LABELS; i++) {
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
         }

         insert.add(new JSpinner(snm));
         insert.get(insert.size()-1).addChangeListener(new ChangeListener() { //TODO Optimise code here
            @Override
            public void stateChanged(ChangeEvent e) {
               for (int i = 0; i < INSERT_LABELS; i++) {
                  if (e.getSource() == insert.get(i)) {
                     SpinnerNumberModel snm = new SpinnerNumberModel();

                     if ((Integer) insert.get(3).getValue() < (Integer) insert.get(4).getValue()) {
                        snm.setValue(insert.get(3).getValue());
                     } else {
                        snm.setValue(insert.get(4).getValue());
                     }

                     snm.setMinimum(0);
                     snm.setMaximum((Integer) insert.get(3).getValue());
                     insert.get(4).setModel(snm);

                     labels.get(i+2).setText(EDITABLE[i+2] + insert.get(i).getValue());
                     labels.get(6).setText(LABEL_STRINGS[6] + insert.get(4).getValue());
                     labels.get(7).setText(LABEL_STRINGS[7] + ((Integer) insert.get(3).getValue() - (Integer) insert.get(4).getValue()));
                     labels.get(8).setText(LABEL_STRINGS[8] + df.format((Integer) insert.get(1).getValue() / (float) ((Integer) insert.get(3).getValue())));
                     labels.get(9).setText(LABEL_STRINGS[9] + df.format((Integer) insert.get(2).getValue() / (float) ((Integer) insert.get(3).getValue())));
                     labels.get(10).setText(LABEL_STRINGS[10] + df.format((Integer) insert.get(0).getValue() / (float) ((Integer) insert.get(3).getValue())));
                  }
               }

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

      table = new JTable(new ProgramTable(PlayerUI.this.ui, PlayerUI.this.ui.getUserData(usernameText)));
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

      for (int i = 0; i < 5; i++) {
         tmp = new JPanel(new GridLayout(1, 2));
         tmp.add(new JLabel(EDITABLE[i+2]));
         tmp.add(insert.get(i));

         editList.add(tmp);
      }

      ui.add(labelList);
      ui.add(editList);
      ui.add(scrollPane);

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

   void repaintTable() {
      table.setModel(new ProgramTable(PlayerUI.this.ui, PlayerUI.this.ui.getUserData(usernameText)));
      ((ProgramTable) table.getModel()).fireTableDataChanged();
   }
}
