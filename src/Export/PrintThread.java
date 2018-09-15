package Export;

import Data.Player;
import Interface.UserInterface;

import java.awt.*;
import java.awt.print.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

class Page implements Printable {
   private String data;

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

      g2.drawString(data, 100, 100);

      return PAGE_EXISTS;
   }

   private String getPaper(ArrayList<Player> players) {
      StringBuilder paper = new StringBuilder();

      paper.append("Graduatoria Cotecchio\n\n");
      paper.append("Nome Cognome\tPunteggio\tPartite\tW-L\tMedia\tPelliccioni\tMedia P.\tCappotti\tMedia C.\n");

      for (Player p : players) {
         paper.append(p.getName()).append("\t")
                 .append(p.getScore()).append("\t")
                 .append(p.getTotalPlays()).append("\t")
                 .append(p.getTotalWins()).append("w-").append(p.getTotalPlays() - p.getTotalWins()).append("l\t")
                 .append(p.getScore() / (float) p.getTotalPlays() %.2f).append("\t")
                 .append(p.getPelliccions()).append("\t")
                 .append(p.getPelliccions() / (float) p.getTotalPlays() %.2f).append("\t")
                 .append(p.getCappottens()).append("\t")
                 .append(p.getCappottens() / (float) p.getTotalPlays() %.2f);
         paper.append("\n");
      }

      System.out.println(paper);

      return paper.toString();
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
