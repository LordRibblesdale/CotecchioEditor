package Update;

import Interface.UserController;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.*;
import java.nio.file.Files;
import java.util.ArrayList;

import static Update.Status.CANCELLED;
import static Update.Status.COMPLETED;
import static Update.Status.DOWNLOADING;

public class UpdateRepo {
   private UserController ui;
   private Timer t = null;

   public UpdateRepo(UserController ui, int current) {
      this.ui = ui;

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
         jsonBody = jsonBody.substring(0, jsonBody.indexOf("\"") -4);

         ArrayList<String> replace = new ArrayList<>();

         int index;
         while (jsonBody.contains("\\r\\n")) {
            index = jsonBody.indexOf("\\r\\n");
            replace.add(jsonBody.substring(0, index));
            replace.add("\n");

            jsonBody = jsonBody.substring(index+4);
         }

         try {
            if (Integer.valueOf(jsonVersion) > current) {
               StringBuilder update = new StringBuilder()
                   .append(ui.getSettings().getResourceBundle().getString("newUpdate"))
                   .append("\n")
                   .append(ui.getSettings().getResourceBundle().getString("newVersion"))
                   .append(" ")
                   .append(jsonVersion)
                   .append("\n");

               for (String s : replace) {
                  update.append(s);
               }

               update.append(jsonBody)
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

               if (option != -1) {
                  if (choice[option] == choice[0]) {
                     downloadUpdate(json, jsonVersion);
                  } else if (choice[option] == choice[1]) {
                     try {
                        Desktop.getDesktop().browse(new URI(downloadUrl));
                     } catch (IOException | URISyntaxException e1) {
                        e1.printStackTrace();
                     }
                  }
               }
            }

            ui.getStatus().setText(ui.getSettings().getResourceBundle().getString("updateChecked"));
         } catch (MalformedURLException | NumberFormatException ex) {
            ui.getStatus().setText(ui.getSettings().getResourceBundle().getString("errorReadingUpdate"));
         }
      } catch (IOException e) {
         ui.getStatus().setText(ui.getSettings().getResourceBundle().getString("errorReadingUpdate"));

         if (e instanceof UnknownHostException) {
            ui.getStatus().setText(ui.getSettings().getResourceBundle().getString("checkConnection"));
         }
      }
   }

   private void downloadUpdate(String json, String jsonVersion) throws MalformedURLException {
      json = json.substring(json.lastIndexOf("https://github.com/LordRibblesdale/CotecchioEditor/releases/download/"));
      json = json.substring(0, json.indexOf("\""));
      System.out.println(json);

      JFrame status = new JFrame("Download");
      JPanel panel = new JPanel(new GridLayout(0, 1));
      JLabel label = new JLabel(ui.getSettings().getResourceBundle().getString("downloadStatus"));
      JLabel size = new JLabel();
      JLabel speed = new JLabel();

      ProgressRenderer bar = new ProgressRenderer();

      Download update = new Download(new URL(json), jsonVersion, true);

      final float[] oldDownload = {0};
      final float[] newDownload = {0};
      final int[] num = {0};

      t = new Timer(500, new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            System.out.println(num[0]++);
            String statusString;

            switch (update.getStatus()) {
               case DOWNLOADING:
                  statusString = ui.getSettings().getResourceBundle().getString("downloadingStatus");
                  break;
               case COMPLETED:
                  statusString = ui.getSettings().getResourceBundle().getString("completedStatus");
                  break;
               default:
                  statusString = "ERROR";
            }

            label.setText(ui.getSettings().getResourceBundle().getString("downloadStatus") + " " +  statusString);

            newDownload[0] = ((float) update.getDownloaded() / 1000000);
            size.setText((newDownload[0] + "MB"));

            speed.setText(((newDownload[0] - oldDownload[0]) * 1000 / 30 + "KB/s"));
            oldDownload[0] = newDownload[0];

            bar.setValue((int) update.getProgress());

            status.validate();

            if (update.getStatus() == COMPLETED) {
               status.dispose();
               if (t != null) {
                  t.stop();
               }

               File downloaded = new File(update.getName());
               String newRename = downloaded.getName().substring(0, downloaded.getName().length()-4);
               downloaded.renameTo(new File(newRename)); //TODO Fix here

               /*
               ArrayList<File> oldVersionFiles = new ArrayList<>(2);
               oldVersionFiles.add(new File("settings.set"));

               for (File file : oldVersionFiles) {
                  try {
                     Files.copy(file.toPath(), new FileOutputStream(file.getName() + ".old." + jsonVersion));
                  } catch (IOException e1) {
                     e1.printStackTrace();
                  }
               }
               */

               try {
                  Files.copy((new File("settings.set")).toPath(), new FileOutputStream((new File("settings.set")).getName() + ".old." + jsonVersion));
               } catch (IOException e1) {
                  e1.printStackTrace();
               }


               int c = JOptionPane.showConfirmDialog(ui, ui.getSettings().getResourceBundle().getString("updateRestart"),
                       ui.getSettings().getResourceBundle().getString("updateDownloaded"),
                       JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);

               if (c == JOptionPane.OK_OPTION) {
                  try {
                     Runtime.getRuntime().exec("java -jar " + newRename);
                     System.exit(0);
                  } catch (IOException e1) {
                     e1.printStackTrace();
                  }
               }
            } else if (update.getStatus() == CANCELLED) {
               if (t != null) {
                  t.stop();
               }

               try {
                  System.out.println(update.getName());
                  Files.delete((new File(update.getName())).toPath());
               } catch (IOException e1) {
                  e1.printStackTrace();
               }
            }
         }
      });

      t.start();

      panel.add(label);
      panel.add(size);
      panel.add(speed);

      status.addWindowListener(new WindowAdapter() {
         @Override
         public void windowClosing(WindowEvent e) {
            status.dispose();
            update.stop();
         }
      });

      status.setSize(new Dimension(300, 300));
      status.setResizable(false);
      status.add(panel);
      status.add(bar, BorderLayout.PAGE_END);

      status.setLocationRelativeTo(ui);
      status.setVisible(true);
   }
}
