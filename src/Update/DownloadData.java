package Update;

import FileManager.OpenFile;
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

import static Update.Status.*;

public class DownloadData {
   private UserController ui;
   private Timer t = null;

   public DownloadData(UserController ui) {
      this.ui = ui;

      String downloadUrl = "https://github.com/LordRibblesdale/CotecchioEditor/raw/master/Data/Data.cda";

      try {
         downloadUpdate(downloadUrl, "");
      } catch (MalformedURLException e) {
         e.printStackTrace();
      }
   }

   private void downloadUpdate(String json, String jsonVersion) throws MalformedURLException {
      ProgressRenderer bar = new ProgressRenderer(ui,
          json,
          ui.getSettings().getResourceBundle().getString("downloadStatus"));

      Download update = new Download(new URL(json), jsonVersion, false);

      final float[] oldDownload = {0};
      final float[] newDownload = {0};
      final int[] num = {0};

      t = new Timer(500, new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
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

            newDownload[0] = ((float) update.getDownloaded() / 1000000);
            bar.setNote(statusString + " - " + ((newDownload[0] + "MB") + " - " + (((newDownload[0] - oldDownload[0]) * 1000 / 30 + "KB/s"))));

            oldDownload[0] = newDownload[0];

            bar.setProgress((int) update.getProgress());

            if (update.getStatus() == COMPLETED) {
               bar.close();
               update.stop();

               if (t != null) {
                  t.stop();
               }

               if (update.isExecutable()) {
                  File downloaded = new File(update.getName());
                  String newRename = downloaded.getName().substring(0, downloaded.getName().length()-4);
                  downloaded.renameTo(new File(newRename)); //TODO Fix here

                  ArrayList<File> oldVersionFiles = new ArrayList<>(2);
                  oldVersionFiles.add(new File("settings.set"));

                  for (File file : oldVersionFiles) {
                     try {
                        Files.copy(file.toPath(), new FileOutputStream(file.getName() + ".old." + jsonVersion));
                     } catch (IOException e1) {
                        e1.printStackTrace();
                     }
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
               } else {
                  if ((update.getName().substring(update.getName().lastIndexOf("."))).equals(".cda")) {
                     int c = JOptionPane.showConfirmDialog(ui, ui.getSettings().getResourceBundle().getString("openDownloadedFile"),
                         ui.getSettings().getResourceBundle().getString("downloadCompleted"),
                         JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);

                     if (c == JOptionPane.OK_OPTION) {
                        if (ui.hasBeenSaved()) {
                           ui.getSettings().setOpenedFile((new File(update.getName())).getPath());
                           (new OpenFile(ui)).actionPerformed(new ActionEvent(ui, 0, ""));   //TODO Fix all actionPerformed
                        } else {
                           JOptionPane.showMessageDialog(ui, ui.getSettings().getResourceBundle().getString("playerListSaved")
                               + " "
                               + ui.getSettings().getOpenedFile()
                               + ui.getSettings().getResourceBundle().getString("playerListSaved_uiNotSaved"));

                        }
                     }
                  }
               }
            } else if (update.getStatus() == CANCELLED || bar.isCanceled()) {
               if (t != null) {
                  t.stop();
                  update.stop();
               }

               try {
                  System.out.println(update.getName());

                  synchronized (DownloadData.this) {
                    DownloadData.this.wait(1000);
                  }

                  Files.delete((new File(update.getName())).toPath());
               } catch (IOException | InterruptedException e1) {
                  e1.printStackTrace();
               }
            }
         }
      });

      t.start();
   }
}
