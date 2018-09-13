import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.ArrayList;

public class PlayerUI extends JPanel {
   private ArrayList<JLabel> labels = new ArrayList<>(9);
   private ArrayList<JSpinner> insert = new ArrayList<>(5);
   private JTextField name;

   private static String[] editable = {
           "Nome: ",
           "Punteggio: ",
           "Pelliccioni: ",
           "Cappotti: ",
           "Partite giocate: ",
           "Partite vinte: "
   };

   PlayerUI(Player player, UserInterface ui) {
      String[] labelStrings = {
              "Nome: ",
              "Punteggio: ",
              "Pelliccioni: ",
              "Cappotti: ",
              "Partite giocate: ",
              "Partite vinte: ",
              "Partite perse: ",
              "Media Pelliccioni: ",
              "Media Cappotti: "
      };

      String[] playerStrings = {
              player.getName(),
              String.valueOf(player.getScore()),
              String.valueOf(player.getPelliccions()),
              String.valueOf(player.getCappottens()),
              String.valueOf(player.getTotalPlays()),
              String.valueOf(player.getTotalWins()),
              String.valueOf((player.getTotalPlays() - player.getTotalWins())),
              String.valueOf((player.getPelliccions() / (float) player.getTotalPlays())),
              String.valueOf((player.getCappottens() / (float) player.getTotalPlays()))
      };

      name = new JTextField(playerStrings[0]);
      name.getDocument().addDocumentListener(new DocumentListener() {
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
            labels.get(0).setText(labelStrings[0] + name.getText());
            ui.setHasBeenSaved(false);
            validate();
         }
      });

      for (int i = 0; i < labelStrings.length; i++) {
         labels.add(new JLabel(labelStrings[i] + playerStrings[i]));
      }

      for (int i = 0; i < 5; i++) {
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
               break;
         }

         insert.add(new JSpinner(snm));
         insert.get(insert.size()-1).addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
               for (int i = 0; i < 5; i++) {
                  if (e.getSource() == insert.get(i)) {
                     labels.get(i+1).setText(editable[i+1] + insert.get(i).getValue());
                     labels.get(6).setText(labelStrings[6] + ((Integer) insert.get(3).getValue() - (Integer) insert.get(4).getValue()));
                     labels.get(7).setText(labelStrings[7] + ((Integer) insert.get(1).getValue() / (float) ((Integer) insert.get(3).getValue())));
                     labels.get(8).setText(labelStrings[8] + ((Integer) insert.get(2).getValue() / (float) ((Integer) insert.get(3).getValue())));

                     SpinnerNumberModel snm = new SpinnerNumberModel();
                     snm.setValue(insert.get(4).getValue());
                     snm.setMinimum(0);
                     snm.setMaximum((Integer) insert.get(3).getValue());
                     insert.get(4).setModel(snm);
                  }
               }

               validate();

               ui.setHasBeenSaved(false);
            }
         });

         if (i == 4) {
            snm.setMaximum((Integer) insert.get(i-1).getValue());
            insert.get(i).setModel(snm);
         }
      }
   }

   public JPanel generatePanel() {
      JPanel ui = new JPanel();
      JPanel labelList = new JPanel(new GridLayout(0, 1));
      JPanel editList = new JPanel(new GridLayout(0, 1));

      for (JLabel data : labels) {
         labelList.add(data);
      }

      JPanel tmp = new JPanel();
      tmp.add(new JLabel(editable[0]));
      tmp.add(name);
      editList.add(tmp);

      for (int i = 0; i < 5; i++) {
         tmp = new JPanel();
         tmp.add(new JLabel(editable[i+1]));
         tmp.add(insert.get(i));

         editList.add(tmp);
      }

      ui.add(labelList);
      ui.add(editList);

      return ui;
   }

   public JTextField getJTextName() {
      return name;
   }

   public ArrayList<JSpinner> getInsert() {
      return insert;
   }
}
