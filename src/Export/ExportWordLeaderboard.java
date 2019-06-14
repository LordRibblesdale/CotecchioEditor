package Export;

import Data.Player;
import FileManager.SaveFile;
import Interface.UserController;
import org.apache.poi.xwpf.usermodel.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.SimpleFormatter;


public class ExportWordLeaderboard extends AbstractAction {
   private UserController ui;

   public ExportWordLeaderboard(UserController ui) {
      this.ui = ui;

      putValue(Action.NAME, ui.getSettings().getResourceBundle().getString("exportWord"));
      putValue(Action.SHORT_DESCRIPTION, ui.getSettings().getResourceBundle().getString("exportWord"));
      putValue(Action.LARGE_ICON_KEY, new ImageIcon(ExportXls.class.getResource("AlignLeft24.gif")));
      putValue(Action.SMALL_ICON, new ImageIcon(ExportXls.class.getResource("AlignLeft16.gif")));
   }

   @Override
   public void actionPerformed(ActionEvent e) {
      if (!ui.hasBeenSaved()) {
         int result = JOptionPane.showConfirmDialog(ui,
                 ui.getSettings().getResourceBundle().getString("saveBeforeClosing"),
                 ui.getSettings().getResourceBundle().getString("exitConfirmation"),
                 JOptionPane.YES_NO_OPTION);
         if (result == JOptionPane.YES_OPTION) {
            new SaveFile(ui).actionPerformed(null);
            //ui.setUpData();
            export(getWorthyPlayers());
         }
      } else {
         //ui.setUpData();
         export(getWorthyPlayers());
      }
   }

   private ArrayList<Player> getWorthyPlayers() {
      ArrayList<Player> tmp = new ArrayList<>();
      int max = 0;

      for (Player p : ui.getPlayers()) {
         if (p.getTotalPlays() > max) {
            max = p.getTotalPlays();
         }
      }

      for (Player p : ui.getPlayers()) {
         if (p.getTotalPlays() > max*0.2) {
            tmp.add(p);
         }
      }

      Arrays.sort(tmp.toArray(new Player[0]));

      return tmp;
   }

   private void export(ArrayList<Player> topPlayers) {
      String path = getFile();

      if (path != null) {
         try {
            XWPFDocument word = new XWPFDocument();
            FileOutputStream out = new FileOutputStream(new File(path));

            write(word, out, topPlayers);
         } catch (IOException e) {
            e.printStackTrace();
         }
      }
   }

   private String getFile() {
      JFileChooser fileChooser = new JFileChooser();
      fileChooser.setAcceptAllFileFilterUsed(false);
      fileChooser.addChoosableFileFilter(new DocFilter());

      int res = fileChooser.showSaveDialog(ui);

      if (res == JFileChooser.APPROVE_OPTION) {
         System.out.println(fileChooser.getSelectedFile().getName().substring(fileChooser.getSelectedFile().getName().length()-4));
         if (fileChooser.getSelectedFile().getName().substring(fileChooser.getSelectedFile().getName().length()-4).equals("docx")) {
            return fileChooser.getSelectedFile().getPath();
         } else {
            return fileChooser.getSelectedFile().getPath() + ".docx";
         }
      } else {
         JOptionPane.showMessageDialog(ui, ui.getSettings().getResourceBundle().getString("errorLoadingFile"), "Error I/O", JOptionPane.ERROR_MESSAGE);
      }

      return null;
   }

   private void write(XWPFDocument paper, FileOutputStream out, ArrayList<Player> top) throws IOException{
      XWPFParagraph setup = paper.createParagraph();
      XWPFRun title = setup.createRun();
      title.setText("COTECCHIO 2019");
      title.addBreak();
      title.setColor("ff3232");
      title.setFontSize(30);
      setup.setAlignment(ParagraphAlignment.CENTER);

      XWPFParagraph setup2 = paper.createParagraph();
      for (Player p : top) {
         XWPFRun player = setup2.createRun();
         player.setFontSize(25);
         player.setColor("000000");
         player.setText(p.getName().substring(0, p.getName().indexOf(" ")+1).toUpperCase()
                 + p.getName().substring(p.getName().indexOf(" ")+1, p.getName().indexOf(" ")+2)
                 + ".");
         player.addTab();
         player.setText(new DecimalFormat().format((p.getScore() / (float) p.getTotalPlays())));
         player.addBreak();
      }

      paper.write(out);
      out.close();
   }
}
