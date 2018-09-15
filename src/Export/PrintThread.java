package Export;

import Data.Player;
import Interface.UserInterface;

import java.awt.*;
import java.awt.print.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;

class Page implements Printable {
   private ArrayList<String> data;
   private int x = 50, y = 50, i = 0;


   Page(ArrayList<Player> players) {
      data = getPaper(players);
   }

   @Override
   public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
      if (pageIndex > 0) {
         return NO_SUCH_PAGE;
      }

      Graphics2D g2 = (Graphics2D) graphics;
      g2.translate(pageFormat.getImageableX(), pageFormat.getImageableY());

      for (String s : data) {
         g2.drawString(s, x, y);

         if (x < pageFormat.getWidth() - 50) {
            x += 2*s.length() +10;
         } else {
            x = 50;
         }

         if (i++ == 8) {
            i = 0;
            y += 8;
         }
      }

      return PAGE_EXISTS;
   }

   private ArrayList<String> getPaper(ArrayList<Player> players) {
      DecimalFormat df = new DecimalFormat("##.##");
      df.setRoundingMode(RoundingMode.DOWN);

      final String[] labelStrings = {
              "Nome",
              "Punteggio",
              "Partite",
              "Media",
              "W-L",
              "Pelliccioni",
              "Media P.",
              "Cappotti",
              "Media C."
      };

      ArrayList<String> data = new ArrayList<>();

      //TODO improve code here
      data.add("Graduatoria Cotecchio");

      data.addAll(Arrays.asList(labelStrings));

      for (Player player : players) {
         final String[] playerStrings = {
                 player.getName(),
                 String.valueOf(player.getScore()),
                 String.valueOf(player.getTotalPlays()),
                 df.format((player.getScore() / (float) player.getTotalPlays())),
                 String.valueOf(player.getTotalWins() + (player.getTotalPlays() - player.getTotalWins())),
                 String.valueOf(player.getPelliccions()),
                 df.format((player.getPelliccions() / (float) player.getTotalPlays())),
                 String.valueOf(player.getCappottens()),
                 df.format((player.getCappottens() / (float) player.getTotalPlays()))
         };

         data.addAll(Arrays.asList(playerStrings));
      }

      return data;
   }
}


public class PrintThread extends AbstractAction {
   private UserInterface ui;

   public PrintThread(UserInterface ui) {
      this.ui = ui;

      putValue(Action.NAME, "Print...");
   }

   @Override
   public void actionPerformed(ActionEvent e) {
      JOptionPane.showMessageDialog(ui, "This is an experimental feature", "Feature under work", JOptionPane.INFORMATION_MESSAGE);

      if (!ui.hasBeenSaved()) {
         int choice = JOptionPane.showConfirmDialog(ui, "Do you want to save before printing?", "Save?", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);

         if (choice == JOptionPane.OK_OPTION) {
            ui.askForSaving(e);
            print();
         } else if (choice == JOptionPane.NO_OPTION) {
            print();
         }
      }
   }

   private void print() {
      PrinterJob job = PrinterJob.getPrinterJob();

      job.setPrintable(new Page(ui.getPlayers()));

      if (job.printDialog()) {
         try {
            job.print();
         } catch (PrinterException e1) {
            JOptionPane.showMessageDialog(ui, "Error printing page", "Error printer", JOptionPane.ERROR_MESSAGE);
         }
      }
   }
}
