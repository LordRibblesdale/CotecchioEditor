package Interface;

import Data.Player;
import Data.Settings;
import FileManager.*;
import Update.UpdateRepo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

import static FileManager.Path.setPath;

public class UserController extends JFrame {
   private static final String PROGRAM_NAME = "Cotecchio Editor - ";
   private static final String VERSION = "Build 6 Beta 1.0";
   private static final int RELEASE = 610;
   private GridLayout mainLayout;
   private JPanel mainPanel;
   private JPanel buttonPanel;
   private PersonalMenu menu;
   private JButton addTab, removeTab;
   private JLabel saveStatus;

   private boolean hasBeenSaved = true;

   private JTabbedPane tabs = null;
   private ArrayList<Player> players = null;
   private ArrayList<PlayerUI> pUI;

   private PersonalToolBar toolBar;
   private PanelList listPlayers = null;

   private Settings settings;
   private SettingsFrame settingsFrame;

   public UserController() {
      super(PROGRAM_NAME + VERSION);
      setIconImage(new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("Data/cotecchio.png"))).getImage());

      settings = new Settings();

      if (settings.isUsingLookAndFeel()) {
         try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
         } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            JOptionPane.showMessageDialog(
                    UserController.this,
                    e.getMessage(),
                    "LookAndFeel UserController00 V:" + RELEASE + " " + e.getStackTrace()[0].getLineNumber(),
                    JOptionPane.ERROR_MESSAGE);
         }
      }

      settingsFrame = new SettingsFrame(UserController.this);

      mainPanel = new JPanel(mainLayout = new GridLayout(0, 10));

      menu = new PersonalMenu(this);

      toolBar = new PersonalToolBar(SwingConstants.VERTICAL, this, menu);
      add(toolBar, BorderLayout.LINE_END);

      setJMenuBar(menu);

      buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
      buttonPanel.add(saveStatus = new JLabel());
      buttonPanel.add(addTab = new JButton(getSettings().getResourceBundle().getString("addTab")));
      buttonPanel.add(removeTab = new JButton(getSettings().getResourceBundle().getString("removeTab")));

      addTab.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            players.add(new Player(getSettings().getResourceBundle().getString("newPlayer0"),
                    getSettings().getResourceBundle().getString("newPlayer1"), 0, 0, 0, 0, 0));
            pUI.add(new PlayerUI(players.get(players.size()-1), UserController.this));
            tabs.addTab(getSettings().getResourceBundle().getString("newPlayer0"), pUI.get(pUI.size()-1).generatePanel());

            if (tabs.getTabCount() > 1) {
               removeTab.setEnabled(true);
            }

            if (listPlayers == null) {
               listPlayers = new PanelList(UserController.this);
            }

            listPlayers.updateList();
            validate();
         }
      });

      removeTab.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            if (!hasBeenSaved()) {
               Object[] choice = {getSettings().getResourceBundle().getString("yes"),
                       getSettings().getResourceBundle().getString("no"),
                       getSettings().getResourceBundle().getString("goBack")};
               int sel = JOptionPane.showOptionDialog(UserController.this,
                       getSettings().getResourceBundle().getString("notSavedTab"),
                       getSettings().getResourceBundle().getString("saveFile"),
                       JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null,
                       choice, choice[0]);

               if (choice[sel] == choice[0]) {
                  new SaveFile(UserController.this).actionPerformed(null);
               } else if (choice[sel] == choice[1]) {
                  tabs.remove(tabs.getSelectedIndex());

                  if (tabs.getTabCount() == 1) {
                     removeTab.setEnabled(false);
                  }
               }
            } else {
               tabs.remove(tabs.getSelectedIndex());

               if (tabs.getTabCount() == 1) {
                  removeTab.setEnabled(false);
               }

               setHasBeenSaved(false);
            }

            validate();
         }
      });

      addTab.setEnabled(false);
      removeTab.setEnabled(false);
      add(buttonPanel, BorderLayout.PAGE_END);

      addWindowListener(new WindowAdapter() {
         @Override
         public void windowClosing(WindowEvent e) {
            if (!hasBeenSaved()) {
               int result = JOptionPane.showConfirmDialog(UserController.this,
                       getSettings().getResourceBundle().getString("saveBeforeClosing"),
                       getSettings().getResourceBundle().getString("exitConfirmation"),
                       JOptionPane.YES_NO_CANCEL_OPTION);
               if (result == JOptionPane.YES_OPTION) {
                  new SaveFile(UserController.this).actionPerformed(null);
               } else if (result == JOptionPane.CANCEL_OPTION) {
                  UserController.this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
               } else {
                  UserController.this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
               }
            }
         }
      });

      setMinimumSize(new Dimension(650, 500));
      setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
      setLocationRelativeTo(null);
      setVisible(true);

      new UpdateRepo(UserController.this, RELEASE);
   }

   public boolean hasBeenSaved() {
      return hasBeenSaved;
   }

   public void setHasBeenSaved(boolean hasBeenSaved) {
      this.hasBeenSaved = hasBeenSaved;

      if (hasBeenSaved) {
         menu.getSaveButton().setEnabled(false);
         setTitle(PROGRAM_NAME + VERSION);
         settingsFrame.stopTimer();
         saveStatus.setText("Saved @ " + new SimpleDateFormat("HH.mm.ss").format(new Date()));
      } else {
         menu.getSaveButton().setEnabled(true);
         setTitle(PROGRAM_NAME + VERSION + " - *" + getSettings().getResourceBundle().getString("changesNotSaved"));
         settingsFrame.startTimer();
      }

      validate();
   }

   public void initialise(ArrayList<Player> players) {
      if (tabs != null) {
         remove(tabs);
      }

      tabs = new JTabbedPane();
      add(tabs);
      validate();

      pUI = new ArrayList<>();

      if (players == null) {
         players = new ArrayList<>();

         players.add(new Player(getSettings().getResourceBundle().getString("newPlayer0"),
                 getSettings().getResourceBundle().getString("newPlayer1"),
                 0, 0, 0, 0, 0));
         pUI.add(new PlayerUI(players.get(players.size()-1), UserController.this));
         tabs.addTab(getSettings().getResourceBundle().getString("newPlayer0"), pUI.get(pUI.size()-1).generatePanel());
      } else {
         this.players = players;

         for (Player p : players) {
            pUI.add(new PlayerUI(p, UserController.this));
            tabs.addTab(pUI.get(pUI.size()-1).getJTextName().getText(), pUI.get(pUI.size()-1).generatePanel());
         }

         if (tabs.getTabCount() > 1) {
            removeTab.setEnabled(true);
         }
      }

      addTab.setEnabled(true);
      menu.getSearch().setEnabled(true);
      menu.getExport().setEnabled(true);
      menu.getPrint().setEnabled(true);
      menu.getStart().setEnabled(true);
      menu.getOpenGame().setEnabled(true);
      menu.getExportXls().setEnabled(true);

      if (listPlayers == null) {
         listPlayers = new PanelList(UserController.this);
      }

      listPlayers.updateList();
      setHasBeenSaved(true);

      validate();
   }

   public JTabbedPane getTabs() {
      return tabs;
   }

   public ArrayList<Player> getPlayers() {
      for (int i = 0; i < players.size(); i++) {
         players.set(i, new Player(pUI.get(i).getJTextName().getText(),
                 pUI.get(i).getUsername().getText(),
                 (Integer) (pUI.get(i).getInsert().get(0).getValue()),
                 (Integer) (pUI.get(i).getInsert().get(1).getValue()),
                 (Integer) (pUI.get(i).getInsert().get(2).getValue()),
                 (Integer) (pUI.get(i).getInsert().get(3).getValue()),
                 (Integer) (pUI.get(i).getInsert().get(4).getValue())));
      }

      return players;
   }

   public void setPlayers(ArrayList<Player> players) {
      this.players = players;
   }

   public ArrayList<Object> getUsernames() {
      ArrayList<Object> usernames = new ArrayList<>();

      for (Player p : players) {
         usernames.add(p.getUsername());
      }

      return usernames;
   }

   JCheckBoxMenuItem getShowList() {
      return menu.getShowList();
   }

   PanelList getListPlayers() {
      return listPlayers;
   }

   public JLabel getStatus() {
      return saveStatus;
   }

   public SettingsFrame getSettingsFrame() {
      return settingsFrame;
   }

   public Settings getSettings() {
      return settings;
   }

   void setSettings(Settings settings) {
      this.settings = settings;
      saveStatus.setText("Saved @ " + new SimpleDateFormat("HH.mm.ss").format(new Date()));
      validate();

      try {
         ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(setPath)));
         out.writeObject(settings);
         out.close();
      } catch (IOException e) {
         e.printStackTrace();
         JOptionPane.showMessageDialog(
                 UserController.this,
                 e.getMessage(),
                 "SaveSettings UserController01 V:" + RELEASE + " " + e.getStackTrace()[0].getLineNumber(),
                 JOptionPane.ERROR_MESSAGE);
      }
   }

   public void saveRecentFile(String dir) {
      settings.setOpenedFile(dir);

      setSettings(settings);
   }

   public void askForSaving(ActionEvent e) {
      new SaveFile(UserController.this).actionPerformed(e);
   }

   void setListPlayers(PanelList listPlayers) {
      this.listPlayers = listPlayers;
   }

   int getRelease() {
      return RELEASE;
   }
}