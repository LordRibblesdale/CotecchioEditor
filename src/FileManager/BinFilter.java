package FileManager;

import javax.swing.filechooser.FileFilter;
import java.io.File;

public class BinFilter extends FileFilter {

   @Override
   public boolean accept(File f) {
      String s = f.getName();

      int i = s.lastIndexOf(".");

      if (i != -1) {
         String e = s.substring(i+1);

         return e.equalsIgnoreCase("bin");
      }

      return f.isDirectory();
   }

   @Override
   public String getDescription() {
      return "*.bin";
   }
}
