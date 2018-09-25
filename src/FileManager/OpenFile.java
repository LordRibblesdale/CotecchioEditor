package FileManager;

import Data.Player;
import Interface.UserController;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.util.ArrayList;

public class OpenFile extends AbstractAction implements Path {
   private UserController ui;
   private ArrayList<Player> players;
   private ObjectInputStream input;
   private ProgressMonitor pm;
   private String path;

   class DecompressibleInputStream extends ObjectInputStream {
      DecompressibleInputStream(InputStream in) throws IOException {
         super(in);
      }

      //
      protected ObjectStreamClass readClassDescriptor() throws IOException, ClassNotFoundException {
         ObjectStreamClass resultClassDescriptor = super.readClassDescriptor(); // initially streams descriptor
         Class localClass = Class.forName(resultClassDescriptor.getName()); // the class in the local JVM that this descriptor represents.
         if (localClass == null) {
            System.out.println("No local class for " + resultClassDescriptor.getName());
            return resultClassDescriptor;
         }
         ObjectStreamClass localClassDescriptor = ObjectStreamClass.lookup(localClass);
         if (localClassDescriptor != null) { // only if class implements serializable
            final long localSUID = localClassDescriptor.getSerialVersionUID();
            final long streamSUID = resultClassDescriptor.getSerialVersionUID();
            if (streamSUID != localSUID) { // check for serialVersionUID mismatch.
               final StringBuffer s = new StringBuffer("Overriding serialized class version mismatch: ");
               s.append("local serialVersionUID = ").append(localSUID);
               s.append(" stream serialVersionUID = ").append(streamSUID);
               Exception e = new InvalidClassException(s.toString());
               System.out.println("Potentially Fatal Deserialization Operation. " + e);
               resultClassDescriptor = localClassDescriptor; // Use local class descriptor for deserialization
            }
         }
         return resultClassDescriptor;
      }
   }

   public OpenFile(UserController ui) {
      this.ui = ui;

      putValue(Action.NAME, "Load...");
   }

   @Override
   public void actionPerformed(ActionEvent e) {
      ArrayList<?> tmp2;
      try {
         this.path = getFile();

         if (this.path != null) {
            input = new ObjectInputStream(new BufferedInputStream(new FileInputStream(this.path)));
            Object tmp = input.readObject();
            input.close();

            if (tmp instanceof ArrayList<?>) {
               tmp2 = (ArrayList<?>) tmp;
               players = new ArrayList<>();

               for (Object o : tmp2) {
                  if (!(o instanceof Player)) {
                     throw new Exception();
                  } else {
                     players.add((Player) o);
                  }
               }

               ui.initialise(players);
            }
         }
      } catch (FileNotFoundException e3) {
         JOptionPane.showMessageDialog(ui, "Error inserting name file", "Error I/O", JOptionPane.ERROR_MESSAGE);
      } catch (IOException | ClassNotFoundException e1) {
         try {
            input.close();

            DecompressibleInputStream in = new DecompressibleInputStream(new BufferedInputStream(new FileInputStream(this.path)));
            Object tmp = in.readObject();
            in.close();

            if (tmp instanceof  ArrayList<?>) {
               tmp2 = (ArrayList<?>) tmp;
               players = new ArrayList<>();

               for (Object o : tmp2) {
                  if (o instanceof Player) {
                     Player p = (Player) o;

                     players.add(new Player(p.getName(), p.getUsername(), p.getScore(), p.getPelliccions(), p.getCappottens(), p.getTotalPlays(), p.getTotalWins()));
                  }
               }

               ui.initialise(players);
               JOptionPane.showMessageDialog(ui, "The file loaded has been converted from an older version. The list will be now saved...",
                       "Conversion completed", JOptionPane.WARNING_MESSAGE);
               new SaveFile(ui).actionPerformed(e);
            }
         } catch (IOException | ClassNotFoundException e2) {
            e2.printStackTrace();
         }
      } catch (Exception e2) {
         JOptionPane.showMessageDialog(ui, "Error reading file", "Error I/O", JOptionPane.ERROR_MESSAGE);
      }
   }

   private String getFile() {
      JFileChooser fileChooser = new JFileChooser();
      fileChooser.setAcceptAllFileFilterUsed(false);
      fileChooser.addChoosableFileFilter(new BinFilter());

      int res = fileChooser.showOpenDialog(ui);

      if (res == JFileChooser.APPROVE_OPTION) {
         return fileChooser.getSelectedFile().getPath();
      } else if (res != JFileChooser.CANCEL_OPTION) {
         JOptionPane.showMessageDialog(ui, "Error loading file", "Error I/O", JOptionPane.ERROR_MESSAGE);
      }

      return null;
   }
}
