import Interface.UserInterface;

import javax.swing.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class CotecchioEditor implements File.Path {
   private static UserInterface ui;
   public static void main(String[] args) {
      try {
         ui = new UserInterface();
      } catch (Exception e) {
         JOptionPane.showMessageDialog(ui, e.getMessage(), "Exception", JOptionPane.ERROR_MESSAGE);
         try {
            e.printStackTrace(new PrintWriter(new BufferedWriter(new FileWriter(logPath))));
            JOptionPane.showMessageDialog(ui, "Log file has been saved in " + logPath + "\nSend it to the developer!", "Log saved", JOptionPane.INFORMATION_MESSAGE);
         } catch (IOException e1) {
            JOptionPane.showMessageDialog(ui, "Error saving log file", "Error I/O", JOptionPane.ERROR_MESSAGE);
         }
      }
   }
}
