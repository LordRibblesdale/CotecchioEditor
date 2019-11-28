package FileManager;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public interface Path {
   String base = System.getProperty("user.dir") + File.separator;
   String history = base + "CDA_History" + File.separator;
   String defaultName = "Data.cda";
   String defaultGameName = base + (new SimpleDateFormat("yyyy-MM-dd HH.mm")).format(new Date()) + ".bin";
   String path = base + defaultName;
   String logPath = base + "log.txt";
   String setPath = base + "settings.set";
}
