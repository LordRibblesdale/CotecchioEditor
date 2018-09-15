package Export;

import Data.Player;
import Interface.UserInterface;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.util.ArrayList;


public class ExportXls extends AbstractAction {
   private UserInterface ui;

   public ExportXls(UserInterface ui) {
      this.ui = ui;

      putValue(Action.NAME, "Export as MSExcel");
   }

   @Override
   public void actionPerformed(ActionEvent e) {
      if (!ui.hasBeenSaved()) {
         int choice = JOptionPane.showConfirmDialog(ui, "Do you want to save before exporting?", "Save?", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);

         if (choice == JOptionPane.OK_OPTION) {
            ui.askForSaving(e);
            export();
         } else if (choice == JOptionPane.NO_OPTION) {
            export();
         }
      }
   }

   private void export() {
      ArrayList<Player> players = ui.getPlayers();
      String paper = getPaper(players);

      String path = getFile();

      if (path != null) {
         try {
            write(path, paper);
         } catch (FileNotFoundException e3) {
            int choice = JOptionPane.showConfirmDialog(ui, "Do you want to overwrite the file?", "Overwrite?", JOptionPane.DEFAULT_OPTION);

            if (choice == JOptionPane.OK_OPTION) {
               try {
                  write(path, paper);
               } catch (IOException e1) {
                  e1.printStackTrace();
               }
            }
         } catch (IOException e1) {
            JOptionPane.showMessageDialog(ui, "Error writing file", "Error I/O", JOptionPane.ERROR_MESSAGE);
            e1.printStackTrace();
         }
      } else {
         try {
            throw new IOException();
         } catch (IOException e1) {
            JOptionPane.showMessageDialog(ui, "Error buffering file", "Error I/O", JOptionPane.ERROR_MESSAGE);
            e1.printStackTrace();
         }
      }
   }

   private String getFile() {
      JFileChooser fileChooser = new JFileChooser();
      fileChooser.setAcceptAllFileFilterUsed(false);
      fileChooser.addChoosableFileFilter(new XlsFilter());

      int res = fileChooser.showSaveDialog(ui);

      if (res == JFileChooser.APPROVE_OPTION) {
         return fileChooser.getSelectedFile().getPath() + ".xls";
      } else {
         JOptionPane.showMessageDialog(ui, "Error loading file", "Error I/O", JOptionPane.ERROR_MESSAGE);
      }

      return null;
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

      return paper.toString();
   }

   private void write(String path, String paper) throws IOException{
      PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(path)));
      out.write(paper);
      out.close();
   }
}
