import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.ArrayList;

public class PlayerUI extends JPanel {
   private ArrayList<JLabel> labels = new ArrayList<>(9);
   private ArrayList<JTextField> insert = new ArrayList<>(6);

   private static String[] editable = {
           "Nome: ",
           "Punteggio:",
           "Pelliccioni:",
           "Cappotti:",
           "Partite giocate:",
           "Partite vinte:"
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

      for (int i = 0; i < labelStrings.length; i++) {
         labels.add(new JLabel(labelStrings[i] + playerStrings[i]));
      }

      for (int i = 0; i < 6; i++) {
         insert.add(new JTextField(playerStrings[i]));
         insert.get(insert.size()-1).getDocument().addDocumentListener(new DocumentListener() {
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
               for (int i = 0; i < 6; i++) {
                  if (insert.get(i).getDocument() == e.getDocument()) {
                     labels.get(i).setText(editable[i] + insert.get(i).getText());
                  }
               }

               if (e.getDocument() == insert.get(0).getDocument()) {
                  ui.getTabs().setTitleAt(ui.getTabs().getSelectedIndex(), insert.get(0).getText());
                  validate();
               }

               validate();

               ui.setHasBeenSaved(false);
            }
         });
      }
   }

   public JPanel generatePanel() {
      JPanel ui = new JPanel();
      JPanel labelList = new JPanel(new GridLayout(0, 1));
      JPanel editList = new JPanel(new GridLayout(0, 1));

      for (JLabel data : labels) {
         labelList.add(data);
      }

      for (int i = 0; i < 6; i++) {
         JPanel tmp = new JPanel();
         tmp.add(new JLabel(editable[i]));
         tmp.add(insert.get(i));

         editList.add(tmp);
      }

      ui.add(labelList);
      ui.add(editList);

      return ui;
   }
}
