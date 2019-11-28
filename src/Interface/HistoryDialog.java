package Interface;

import Data.HistoryData;
import Data.InsertionValues;
import FileManager.ImgFilter;
import FileManager.Path;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;

public class HistoryDialog extends JDialog implements Path {
  class InsertionDialog extends JDialog implements InsertionValues {
    private JPanel buttons;
    private JButton abort, add;

    private JTextArea txt = null;
    private JLabel img = null;

    /*
    private JButton addImg = null;

    private ActionListener listener = new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {

      }
    };
     */

    InsertionDialog(UserController ui, JDialog dialog, int type) {
      super(dialog, true);
      setLayout(new BorderLayout());

      buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));

      switch (type) {
        case TEXT:
          setTitle(
              ui.getSettings().getResourceBundle().getString("addDialogTitle")
                  + ui.getSettings().getResourceBundle().getString("addTextDialog"));

          txt = new JTextArea();
          txt.setLineWrap(true);
          txt.setWrapStyleWord(true);

          add(txt);

          break;
        case IMAGE:
          setTitle(
              ui.getSettings().getResourceBundle().getString("addDialogTitle")
                  + ui.getSettings().getResourceBundle().getString("addImageDialog"));

          File location = getFile();

          if (location != null) {
            img = new JLabel(FileSystemView.getFileSystemView().getSystemIcon(location));

            add(img);
          }

          break;
      }

      setMinimumSize(new Dimension(
          (int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth()*0.4),
          (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight()*0.50)));
      setLocationRelativeTo(ui.getFrame());
      setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
      setVisible(true);
    }
  }

  private ArrayList<HistoryData> components;
  private UserController ui;
  private JPanel buttons;
  private JSplitPane splitPane;
  private JButton addText, addImage, remove, removeAll, close;
  private JList<String> list;
  private JPanel panel;

  private static final String EXT = ".sdc";
  private String file;

  HistoryDialog(UserController ui, String title, String file, ArrayList<HistoryData> components) {
    super(ui.getFrame(), title, true);
    this.file = file;
    this.ui = ui;
    this.components = components;

    setLayout(new BorderLayout());
    buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    buttons.add(close = new JButton(ui.getSettings().getResourceBundle().getString("goBack")));
    buttons.add(new JSeparator(JSeparator.VERTICAL));
    buttons.add(remove = new JButton(ui.getSettings().getResourceBundle().getString("removeComponent")));
    buttons.add(removeAll = new JButton(ui.getSettings().getResourceBundle().getString("removeAllComponents")));
    buttons.add(new JSeparator(JSeparator.VERTICAL));
    buttons.add(addImage = new JButton(ui.getSettings().getResourceBundle().getString("addImageButton")));
    buttons.add(addText = new JButton(ui.getSettings().getResourceBundle().getString("addTextButton")));

    close.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        int choice = JOptionPane.showConfirmDialog(
            HistoryDialog.this,
            ui.getSettings().getResourceBundle().getString("askSaveChanges"),
            ui.getSettings().getResourceBundle().getString("askSave"),
            JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE
        );

        switch (choice) {
          case JOptionPane.YES_OPTION:
            File folder = new File(Path.history);
            File file1 = new File(folder.getPath() + file + EXT);

            if(folder.exists()) {
              if (folder.isDirectory()) {
                ObjectOutputStream ois;

                try {
                  ois = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(file1)));

                  ois.writeObject(components);
                  ois.close();
                } catch (IOException ex) {
                  ex.printStackTrace();
                }
              }
            } else {
              if (folder.mkdir()) {
                ObjectOutputStream ois;

                try {
                  ois = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(file1)));

                  ois.writeObject(components);
                  ois.close();
                } catch (IOException ex) {
                  ex.printStackTrace();
                }

              } else {
                System.out.println("ERROR HistoryData - Close ActionListener");
              }
            }

            HistoryDialog.this.dispose();
            break;
          case JOptionPane.NO_OPTION:
            HistoryDialog.this.dispose();
            break;
        }
      }
    });

    addImage.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        new InsertionDialog(ui, HistoryDialog.this, InsertionValues.IMAGE);
      }
    });

    addText.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        new InsertionDialog(ui, HistoryDialog.this, InsertionValues.TEXT);
      }
    });

    add(buttons, BorderLayout.PAGE_END);

    if (components != null) {
      list = new JList<>(createList());
    } else {
      list = new JList<>();
    }
    panel = new JPanel(new GridLayout(0, 1));

    splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, panel, list);
    splitPane.setDividerLocation(HistoryDialog.this.getWidth()*0.75);

    if (components != null) {
      for (HistoryData component : components) {
        JLabel obj = null;

        if (component.getComponent() instanceof Image) {
          obj = new JLabel((Icon) component.getComponent());
        } else if (component.getComponent() instanceof String) {
          obj = new JLabel((String) component.getComponent());
        }

        panel.add(obj != null ? obj : new JLabel(ui.getSettings().getResourceBundle().getString("errorLoadingFile")));
      }
    }

    setMinimumSize(new Dimension(
        (int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth()*0.35),
        (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight()*0.65)));
    setLocationRelativeTo(ui.getFrame());
    setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    setVisible(true);
  }

  public JPanel getPanel() {
    return panel;
  }

  public String[] createList() {
    ArrayList<String> list = new ArrayList<>();

    for (HistoryData component : components) {
      list.add(component.getLabel());
    }

    return list.toArray(new String[0]);
  }

  private File getFile() {
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setAcceptAllFileFilterUsed(false);
    fileChooser.addChoosableFileFilter(new ImgFilter());

    int res = fileChooser.showOpenDialog(ui.getFrame());

    if (res == JFileChooser.APPROVE_OPTION) {
      return fileChooser.getSelectedFile();
    } else if (res != JFileChooser.CANCEL_OPTION) {
      JOptionPane.showMessageDialog(ui.getFrame(), ui.getSettings().getResourceBundle().getString("errorLoadingFile"), "Error I/O", JOptionPane.ERROR_MESSAGE);
    }

    return null;
  }
}
