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
import java.net.URI;
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
         jsonBody = jsonBody.substring(0, jsonBody.indexOf("\"") -8);

         try {
            if (Integer.valueOf(jsonVersion) > current) {
               StringBuilder update = new StringBuilder()
                       .append(ui.getSettings().getResourceBundle().getString("newUpdate"))
                       .append("\n")
                       .append(ui.getSettings().getResourceBundle().getString("newVersion"))
                       .append(" ")
                       .append(jsonVersion)
                       .append("\n")
                       .append(jsonBody)
                       .append("\n")
                       .append(ui.getSettings().getResourceBundle().getString("askDownloadUpdate"));

               Object[] choice = {
                       ui.getSettings().getResourceBundle().getString("downloadNow"),
                       ui.getSettings().getResourceBundle().getString("openLink"),
                       ui.getSettings().getResourceBundle().getString("doNothing")
               };

               int option = JOptionPane.showOptionDialog(ui, update.toString(),
                       ui.getSettings().getResourceBundle().getString("availableUpdate"),
                       JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, choice, choice[0]);

               if (choice[option] == choice[0]) {
                  downloadUpdate();
               } else if (choice[option] == choice[1]) {
                  try {
                     Desktop.getDesktop().browse(new URI(downloadUrl));
                  } catch (IOException | URISyntaxException e1) {
                     e1.printStackTrace();
                  }
               }
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

   private void downloadUpdate() {
      JFrame status = new JFrame("Download");
      ProgressRenderer bar = new ProgressRenderer();
      //status.add
   }
}
