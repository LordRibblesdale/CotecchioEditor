package FileManager;

import java.text.SimpleDateFormat;
import java.util.Date;

public interface Path {
   String base = System.getProperty("user.dir") + "\\";
   String defaultName = "Player.bin";
   String defaultGameName = base + (new SimpleDateFormat("yyyy-MM-dd HH.mm")).format(new Date()) + ".bin";
   String path = base + defaultName;
   String logPath = base + "log.txt";
}
