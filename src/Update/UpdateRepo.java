package Update;

import Interface.UserController;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;

public class UpdateRepo {

   public UpdateRepo(UserController ui, int current) {
      String url = "https://api.github.com/repos/lordribblesdale/CotecchioEditor/releases/latest";
      String downloadUrl = "https://github.com/lordribblesdale/CotecchioEditor/releases/latest";
      String json = null;
      String jsonVersion = null;
      String jsonBody = null;

      // from StackOverflow
      CloseableHttpClient httpClient = HttpClientBuilder.create().build();
      HttpGet request = new HttpGet(url);
      request.addHeader("content-type", "application/json");
      try {
         HttpResponse result = httpClient.execute(request);
         json = EntityUtils.toString(result.getEntity(), "UTF-8");
         jsonVersion = json;
         jsonVersion = json.substring(jsonVersion.indexOf("tag_name\":\"") + 11);
         jsonVersion = jsonVersion.substring(0 , jsonVersion.indexOf("\",\""));

         jsonBody = json;
         jsonBody = json.substring(jsonBody.indexOf("\"body\":\"") + 8);
         jsonBody = jsonBody.substring(0, jsonBody.indexOf("\""));
         jsonBody = jsonBody.replaceAll("\\r\\n", "<p></p>");
         //TODO fix here

         try {
            if (Integer.valueOf(jsonVersion) > current) {
               Font font = UIManager.getDefaults().getFont("Label.font");

               // from StackOverflow
               StringBuilder style = new StringBuilder("font-family:" + font.getFamily() + ";");
               style.append("font-weight:").append(font.isBold() ? "bold" : "normal").append(";");
               style.append("font-size:").append(font.getSize()).append("pt;");

               JEditorPane ep = new JEditorPane("text/html", "<html><body style=\"" + style + "\">"
                       + ui.getSettings().getResourceBundle().getString("newUpdate")
                       + "<p>" + ui.getSettings().getResourceBundle().getString("newVersion")
                       + " " + jsonVersion + "</p>" + "<p>" + jsonBody + "</p>"
                       +"<a href=\"" + downloadUrl + "\">"
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
      } catch (IOException e) {
         e.printStackTrace();

         ui.getStatus().setText(ui.getSettings().getResourceBundle().getString("errorReadingUpdate"));
      }
   }
}
