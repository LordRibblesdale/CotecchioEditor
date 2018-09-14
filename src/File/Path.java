package File;

public interface Path {
   String base = System.getProperty("user.dir") + "\\";
   String defaultName = "Player.bin";
   String path = base + defaultName;
}
