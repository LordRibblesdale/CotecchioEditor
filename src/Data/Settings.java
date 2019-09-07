package Data;

import FileManager.Path;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

public class Settings implements Serializable, Path {
    private static long serialVersionUID = 710L;

    private int refreshSaveRate;
    private String openedFile;
    private String language;
    private String country;
    private int percentage;
    private boolean useLookAndFeel;
    private transient ResourceBundle resourceBundle;

    public Settings() {
       Settings tmp;
       try {
          ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(setPath)));
          Object object = in.readObject();
          in.close();
          if (object instanceof Settings) {
             tmp = (Settings) object;

             refreshSaveRate = tmp.getRefreshSaveRate();
             openedFile = tmp.getOpenedFile();
             useLookAndFeel = tmp.isUsingLookAndFeel();
             language = tmp.getLanguage();
             country = tmp.getCountry();
             percentage = tmp.getPercentage();
             Locale locale = new Locale(language, country);
             resourceBundle = ResourceBundle.getBundle("MessagesBundle", locale);
          }
       } catch (ClassNotFoundException e) {
          e.printStackTrace();
       } catch (IOException e) {
          refreshSaveRate = 30000;
          openedFile = "";
          language = "it";
          country = "IT";
          percentage = 20;
          useLookAndFeel = true;
          Locale locale = new Locale(language, country);
          resourceBundle = ResourceBundle.getBundle("MessagesBundle", locale);
          try {
             ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(setPath)));
             out.writeObject(Settings.this);
             out.close();
          } catch (IOException e1) {
             JOptionPane.showMessageDialog(null, "Error saving settings file", "Error I/O", JOptionPane.ERROR_MESSAGE);
          }
       }

       System.out.println(language + country);
   }

   public int getRefreshSaveRate() {
       return refreshSaveRate;
   }

   public String getOpenedFile() {
       return openedFile;
   }

   public void setRefreshSaveRate(int refreshSaveRate) {
       this.refreshSaveRate = refreshSaveRate;
   }

   public void setOpenedFile(String openedFile) {
       this.openedFile = openedFile;
   }

   private String getCountry() {
       return country;
   }

   private String getLanguage() {
       return language;
   }

   public void setCountry(String country) {
       this.country = country;
   }

   public void setLanguage(String language) {
      this.language = language;
   }

   public boolean isUsingLookAndFeel() {
      return useLookAndFeel;
   }

   public void setUseLookAndFeel(boolean useLookAndFeel) {
      this.useLookAndFeel = useLookAndFeel;
   }

   public ResourceBundle getResourceBundle() {
      return resourceBundle;
   }

   public void setResourceBundle(ResourceBundle resourceBundle) {
      this.resourceBundle = resourceBundle;
   }

   public int getPercentage() {
      return percentage;
   }

   public void setPercentage(int percentage) {
      this.percentage = percentage;
   }
}
