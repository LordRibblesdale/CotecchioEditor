package Update;

import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Observable;

public class Download implements Runnable, Status {
   private static final int MAX_BUFFER_SIZE = 4096;

   private URL url;
   private String version;
   private String name;
   private int size;
   private int downloaded;
   private int status;

   public Download(URL url, String version) {
      this.url = url;
      this.version = version;
      name = null;

      size = -1;
      downloaded = 0;
      status = DOWNLOADING;

      download();
   }

   public URL getUrl() {
      return url;
   }

   public int getSize() {
      return size;
   }

   public int getStatus() {
      return status;
   }

   public float getProgress() {
      return ((float) downloaded / size) * 100;
   }

   public int getDownloaded() {
      return downloaded;
   }

   public void pause() {
      status = PAUSED;
   }

   public void stop() {
      status = CANCELLED;

      Thread thread = new Thread(this);
      thread.interrupt();
   }

   public void resume() {
      status = DOWNLOADING;
   }

   public String getName() {
      return name;
   }

   private void error() {
      status = ERROR;
   }

   private void download() {
      Thread thread = new Thread(this);
      thread.start();
   }

   private String getFileName(URL url) {
      String fileName = url.getFile();
      fileName = fileName.substring(fileName.lastIndexOf("/") + 1);
      fileName += version + ".jar.tmp";
      name = fileName;
      return fileName;
   }

   @Override
   public void run() {
      RandomAccessFile file = null;
      InputStream stream = null;

      try {
         HttpURLConnection connection = (HttpURLConnection) url.openConnection();
         connection.setRequestProperty("Range", "bytes=" + downloaded + "-");
         connection.connect();

         if (connection.getResponseCode() / 100 != 2) {
            error();
         }

         if (connection.getContentLength() < 1) {
            error();
         }

         if (size == -1) {
            size = connection.getContentLength();
         }

         file = new RandomAccessFile(getFileName(url), "rw");
         file.seek(downloaded);

         stream = connection.getInputStream();

         while (status == DOWNLOADING) {
            byte buffer[];

            if (size - downloaded > MAX_BUFFER_SIZE) {
               buffer = new byte[MAX_BUFFER_SIZE];
            } else {
               buffer = new byte[size - downloaded];
            }

            int read = stream.read(buffer);
            if (read == -1) {
               break;
            }

            file.write(buffer, 0, read);
            downloaded += read;
         }

         if (status == DOWNLOADING) {
            status = COMPLETED;
         }
      } catch (IOException e) {
         e.printStackTrace();
         error();
      } finally {
         if (file != null) {
            try {
               file.close();
            } catch (IOException e) {
               e.printStackTrace();
            }
         }

         if (stream != null) {
            try {
               stream.close();
            } catch (IOException e) {
               e.printStackTrace();
            }
         }
      }
   }
}
