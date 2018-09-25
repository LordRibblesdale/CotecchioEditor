package FileManager;

import Data.Player;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class AutoSaveFile {
    public AutoSaveFile(String dir, ArrayList<Player> players) {
        save(dir, players);
    }

    private void save(String dir, ArrayList<Player> players) {
        try {
            ObjectOutputStream output = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(dir)));
            output.writeObject(players);
            output.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

    }
}
