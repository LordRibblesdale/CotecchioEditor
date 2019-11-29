package Interface;

import Data.HistoryData;
import Data.InsertionValues;
import FileManager.ImgFilter;
import FileManager.Path;

import javax.swing.*;
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
    private ImageIcon imageIcon = null;

    /*
    private JButton addImg = null;

    private ActionListener listener = new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {

      }
    };
     */

    InsertionDialog(UserController ui, HistoryDialog dialog, int type) {
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
            img = new JLabel(imageIcon = new ImageIcon(location.getPath()));
            add(img);
          }

          break;
      }

      buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
      buttons.add(abort = new JButton(ui.getSettings().getResourceBundle().getString("goBack")));
      buttons.add(add = new JButton(ui.getSettings().getResourceBundle().getString(
          txt != null ? "addTextButton" : "addImageButton")));

      abort.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          InsertionDialog.this.dispose();
        }
      });

      add.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          dialog.getArrayComponents().add(new HistoryData(
              txt != null ? txt.getText() : imageIcon,
              ui.getSettings().getResourceBundle().getString(txt != null ? "text" : "image")));

          dialog.askRepaint();
          InsertionDialog.this.dispose();
        }
      });

      add(buttons, BorderLayout.PAGE_END);

      setMinimumSize(new Dimension(
          (int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth()*0.4),
          (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight()*0.5)));
      setLocationRelativeTo(ui.getFrame());
      setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
      setVisible(true);
    }
  }

  private ArrayList<HistoryData> components;
  private UserController ui;
  private JPanel buttons;
  private JScrollPane scrollPane;
  private JSplitPane splitPane;
  private JButton addText, addImage, remove, removeAll, close;
  private JList<String> list;
  private JPanel panel;

  private static final String EXT = ".sdc";
  private String file;
  private boolean hasBeenModified = false;

  HistoryDialog(UserController ui, String title, String file, ArrayList<HistoryData> components) {
    super(ui.getFrame(), title, true);
    this.file = file;
    this.ui = ui;
    this.components = components != null ? components : new ArrayList<>();

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
        if (hasBeenModified) {
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
    scrollPane = new JScrollPane(panel);
    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

    splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scrollPane, list);
    splitPane.setDividerLocation((double) HistoryDialog.this.getWidth()*0.75);

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

    add(splitPane);

    setMinimumSize(new Dimension(
        (int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth()*0.35),
        (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight()*0.65)));
    setLocationRelativeTo(ui.getFrame());
    setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    setVisible(true);
  }

  void askRepaint() {
    for (Component c : splitPane.getComponents()) {
      if (c == scrollPane) {
        splitPane.remove(c);
      }
    }

    panel = new JPanel(new FlowLayout());

    for (HistoryData hd : components) {
      if (hd.getComponent() instanceof String) {
        panel.add(new JLabel((String) hd.getComponent()));
      } else if (hd.getComponent() instanceof ImageIcon) {
        panel.add(new JLabel((ImageIcon) hd.getComponent()));
      }
    }

    scrollPane = new JScrollPane(panel);
    splitPane.add(scrollPane, 0);

    list.setModel(new DefaultComboBoxModel<>(createList()));
    validate();

    hasBeenModified = true;
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

  public ArrayList<HistoryData> getArrayComponents() {
    return components;
  }
}
