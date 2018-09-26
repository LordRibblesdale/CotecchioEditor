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

class PrintThreadLeaderboard {
   private UserController ui;

   PrintThreadLeaderboard(UserController ui, ArrayList<Player> topPlayers) {
      this.ui = ui;

      print(topPlayers);
   }

   private void print(ArrayList<Player> topPlayers) {
      String paper = getPaper(topPlayers);

      String path = System.getProperty("java.io.tmpdir") + "/CotecchioTop.xls";
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

      paper.append("Graduatoria Cotecchio\n\n");
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
