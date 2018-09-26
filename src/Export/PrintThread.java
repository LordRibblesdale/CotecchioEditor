package Export;

import Data.Player;
import Interface.UserController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class PrintThread extends AbstractAction {
   private UserController ui;

   public PrintThread(UserController ui) {
      this.ui = ui;

      putValue(Action.NAME, ui.getSettings().getResourceBundle().getString("print"));
   }

   @Override
   public void actionPerformed(ActionEvent e) {
      JOptionPane.showMessageDialog(ui, ui.getSettings().getResourceBundle().getString("experimentalFeature"),
              ui.getSettings().getResourceBundle().getString("featureUnderWork"), JOptionPane.INFORMATION_MESSAGE);

      if (!ui.hasBeenSaved()) {
         int choice = JOptionPane.showConfirmDialog(ui, ui.getSettings().getResourceBundle().getString("saveBeforePrinting"),
                 ui.getSettings().getResourceBundle().getString("askSave"), JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);

         if (choice == JOptionPane.OK_OPTION) {
            ui.askForSaving(e);
            print();
         } else if (choice == JOptionPane.NO_OPTION) {
            print();
         }
      } else  {
         print();
      }
   }

   private void print() {
      ArrayList<Player> players = ui.getPlayers();
      String paper = getPaper(players);

      String path = System.getProperty("java.io.tmpdir") + "/Cotecchio.xls";
      System.out.println(path);

      try {
         write(path, paper);
         Desktop.getDesktop().print(new File(path));
      } catch (IOException e1) {
         JOptionPane.showMessageDialog(ui, ui.getSettings().getResourceBundle().getString("errorWritingFile"), "Error I/O", JOptionPane.ERROR_MESSAGE);
         e1.printStackTrace();
      }
   }

   private String getPaper(ArrayList<Player> players) {
      StringBuilder paper = new StringBuilder();
      DecimalFormat df = new DecimalFormat();
      df.setRoundingMode(RoundingMode.DOWN);

      paper.append("Lista Giocatori Cotecchio\n\n");
      paper.append("Nome Cognome\tPunteggio\tPartite\tW-L\tMedia\tPelliccioni\tMedia P.\tCappotti\tMedia C.\n");

      for (Player p : players) {
         paper.append(p.getName()).append("\t")
                 .append(p.getScore()).append("\t")
                 .append(p.getTotalPlays()).append("\t")
                 .append(p.getTotalWins()).append("w-").append(p.getTotalPlays() - p.getTotalWins()).append("l\t")
                 .append(df.format(p.getScore() / (float) p.getTotalPlays())).append("\t")
                 .append(p.getPelliccions()).append("\t")
                 .append(df.format(p.getPelliccions() / (float) p.getTotalPlays())).append("\t")
                 .append(p.getCappottens()).append("\t")
                 .append(df.format(p.getCappottens() / (float) p.getTotalPlays()));
         paper.append("\n");
      }

      return paper.toString();
   }

   private void write(String path, String paper) throws IOException{
      PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(path)));
      out.write(paper);
      out.close();
   }
}
