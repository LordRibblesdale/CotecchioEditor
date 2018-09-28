package Update;

import Interface.UserController;
import org.kohsuke.github.GHRelease;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import java.awt.*;
import java.io.*;
import java.net.URISyntaxException;

public class UpdateRepo {
   public UpdateRepo(UserController ui, int current) {
      try {
         File homeDir = new File(System.getProperty("user.home"));
         File propertyFile = new File(homeDir, ".github");

         if (!propertyFile.exists()) {
            PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(propertyFile)));
            out.write("oauth=b1a5f0a21c8bb4a1fea49d9e9cbf9982e657551d");
            out.close();
         }

         GitHub git = GitHub.connect();
         GHRepository rep = git.getRepository("LordRibblesdale/CotecchioEditor");
         GHRelease rel = rep.getLatestRelease();

         try {
            if (Integer.valueOf(rel.getTagName()) > current) {
               Font font = UIManager.getDefaults().getFont("Label.font");

               // from StackOverflow
               StringBuilder style = new StringBuilder("font-family:" + font.getFamily() + ";");
               style.append("font-weight:").append(font.isBold() ? "bold" : "normal").append(";");
               style.append("font-size:").append(font.getSize()).append("pt;");

               JEditorPane ep = new JEditorPane("text/html", "<html><body style=\"" + style + "\">"
                       + ui.getSettings().getResourceBundle().getString("newUpdate")
                       + "<p>" + ui.getSettings().getResourceBundle().getString("newVersion")
                       + " " + rel.getTagName() + "\n" + rel.getBody() + "</p>"
                       +"<a href=\"" + rel.getHtmlUrl() + "\">"
                       + ui.getSettings().getResourceBundle().getString("clickUpdate") + "</a>"
                       + "</body></html>");

               ep.addHyperlinkListener(new HyperlinkListener() {
                  @Override
                  public void hyperlinkUpdate(HyperlinkEvent e)
                  {
                     if (e.getEventType().equals(HyperlinkEvent.EventType.ACTIVATED)) {
                        try {
                           Desktop.getDesktop().browse(e.getURL().toURI());
                        } catch (IOException | URISyntaxException e1) {
                           e1.printStackTrace();
                        }
                     }
                  }
               });
               ep.setEditable(false);
               ep.setBackground(ui.getBackground());

               JOptionPane.showMessageDialog(ui, ep);
            }
         } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(ui, ui.getSettings().getResourceBundle().getString("errorReadingUpdate"));
         }
      } catch (IOException e) {
         e.printStackTrace();
      }
   }
}
