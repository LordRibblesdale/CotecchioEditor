package Update;

import Interface.UserController;
import org.kohsuke.github.GHRelease;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.HttpException;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import java.awt.*;
import java.io.*;
import java.net.URISyntaxException;
import java.net.UnknownHostException;

public class UpdateRepo {

   public UpdateRepo(UserController ui, int current) {
      try {
         File homeDir = new File(System.getProperty("user.home"));
         File propertyFile = new File(homeDir, ".github");

         PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(propertyFile)));
         String token = "2be635158aeb16302660bb0d9a14000679cde711";
         out.write("oauth=" + token);
         out.close();

         try {
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

               ui.getStatus().setText(ui.getSettings().getResourceBundle().getString("updateChecked"));
            } catch (NumberFormatException ex) {
               ui.getStatus().setText(ui.getSettings().getResourceBundle().getString("errorReadingUpdate"));
            }
         } catch (UnknownHostException ex) {
            ui.getStatus().setText(ui.getSettings().getResourceBundle().getString("noConnectionAvailable"));
         } catch (HttpException ex) {
            if (ex.getResponseCode() == 401) {
               JOptionPane.showMessageDialog(ui, ui.getSettings().getResourceBundle().getString("errorNotAuthorised"));
            }
         }
      } catch (IOException e) {
         e.printStackTrace();
      }
   }
}
