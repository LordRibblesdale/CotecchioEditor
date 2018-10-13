package Interface;

import Data.Player;
import Data.Settings;
import Edit.Search;
import Export.ExportLeaderboard;
import Export.ExportXls;
import Export.PrintThread;
import FileManager.*;
import Game.GameStarter;
import Game.OpenGameFile;
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
   private static final String VERSION = "Build 5 Beta 3.0";
   private static final int RELEASE = 530;
   private GridLayout mainLayout;
   private JPanel mainPanel;
   private JPanel buttonPanel;
   private JMenuBar menu;
   private JMenu file, edit, about, export, game;
   private JButton addTab, removeTab;
   private JCheckBoxMenuItem showList;
   private JLabel saveStatus;

   private boolean hasBeenSaved = true;
   private SaveFile saveButton;
   private Search search;
   private PrintThread print;
   private GameStarter start;
   private OpenGameFile openGame;

   private JTabbedPane tabs = null;
   private ArrayList<Player> players = null;
   private ArrayList<PlayerUI> pUI;

   private JToolBar toolBar;
   private PanelList listPlayers = null;

   private Settings settings;
   private SettingsFrame settingsFrame;

   public UserController() {
      super(PROGRAM_NAME + VERSION);
      setIconImage(new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("Data/cotecchio.png"))).getImage());

      settings = new Settings();
      settingsFrame = new SettingsFrame(UserController.this);

      mainPanel = new JPanel(mainLayout = new GridLayout(0, 10));

      menu = new JMenuBar();
      menu.add(file = new JMenu("File"));
      file.add(new NewFile(UserController.this));
      file.add(new OpenFile(UserController.this));
      file.add(saveButton = new SaveFile(UserController.this));
      file.add(new JSeparator());
      file.add(export = new JMenu(getSettings().getResourceBundle().getString("export")));
      file.add(print = new PrintThread(UserController.this));
      export.add(new ExportXls(UserController.this));
      export.add(new ExportLeaderboard(UserController.this));

      export.setEnabled(false);
      print.setEnabled(false);
      saveButton.setEnabled(false);

      toolBar = new JToolBar(SwingConstants.VERTICAL);
      toolBar.add(search = new Search(UserController.this));
      search.setEnabled(false);
      add(toolBar, BorderLayout.LINE_END);

      menu.add(edit = new JMenu(getSettings().getResourceBundle().getString("edit")));
      edit.add(search);
      edit.add(showList = new JCheckBoxMenuItem(getSettings().getResourceBundle().getString("showPlayersList")));
      edit.add(new JSeparator());
      edit.add(new SettingsButton(UserController.this));
      showList.addItemListener(new ItemListener() {
         @Override
         public void itemStateChanged(ItemEvent e) {
            if (listPlayers != null) {
               listPlayers.updateList();
               if (((JCheckBoxMenuItem) e.getSource()).getState()) {
                  listPlayers.setVisible(true);
               } else {
                  listPlayers.setVisible(false);
               }
            } else {
               listPlayers = new PanelList(UserController.this);
               listPlayers.updateList();
               listPlayers.setVisible(true);
            }

            validate();
         }
      });

      menu.add(game = new JMenu(getSettings().getResourceBundle().getString("game")));
      game.add(start = new GameStarter(UserController.this));
      game.add(openGame = new OpenGameFile(UserController.this));
      openGame.setEnabled(false);
      start.setEnabled(false);

      menu.add(about = new JMenu(getSettings().getResourceBundle().getString("about")));
      about.add(new About(UserController.this));

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
         saveButton.setEnabled(false);
         setTitle(PROGRAM_NAME + VERSION);
         settingsFrame.stopTimer();
         saveStatus.setText("Saved @ " + new SimpleDateFormat("HH.mm.ss").format(new Date()));
      } else {
         saveButton.setEnabled(true);
         setTitle(PROGRAM_NAME + VERSION + " - *" + getSettings().getResourceBundle().getString("changesNotSaved"));
         settingsFrame.startTimer();
      }

      validate();
   }

   public void initialise() {
      if (tabs != null) {
         remove(tabs);
      }

      tabs = new JTabbedPane();
      add(tabs);
      validate();

      players = new ArrayList<>();
      pUI = new ArrayList<>();

      players.add(new Player(getSettings().getResourceBundle().getString("newPlayer0"),
              getSettings().getResourceBundle().getString("newPlayer1"),
              0, 0, 0, 0, 0));
      pUI.add(new PlayerUI(players.get(players.size()-1), UserController.this));
      tabs.addTab(getSettings().getResourceBundle().getString("newPlayer0"), pUI.get(pUI.size()-1).generatePanel());

      addTab.setEnabled(true);
      search.setEnabled(true);
      export.setEnabled(true);
      print.setEnabled(true);
      start.setEnabled(true);
      openGame.setEnabled(true);

      if (listPlayers == null) {
         listPlayers = new PanelList(UserController.this);
      }

      listPlayers.updateList();

      setHasBeenSaved(true);

      validate();
   }

   public void initialise(ArrayList<Player> players) {
      if (tabs != null) {
         remove(tabs);
      }

      tabs = new JTabbedPane();
      add(tabs);
      validate();

      this.players = players;
      pUI = new ArrayList<>();

      for (Player p : players) {
         pUI.add(new PlayerUI(p, UserController.this));
         tabs.addTab(pUI.get(pUI.size()-1).getJTextName().getText(), pUI.get(pUI.size()-1).generatePanel());
      }

      addTab.setEnabled(true);
      search.setEnabled(true);
      export.setEnabled(true);
      print.setEnabled(true);
      start.setEnabled(true);
      openGame.setEnabled(true);

      if (tabs.getTabCount() > 1) {
         removeTab.setEnabled(true);
      }

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
      return showList;
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
         JOptionPane.showMessageDialog(UserController.this, e.getMessage(), "Exception", JOptionPane.ERROR_MESSAGE);
      }
   }

   public void saveRecentFile(String dir) {
      settings.setOpenedFile(dir);

      setSettings(settings);
   }

   public void askForSaving(ActionEvent e) {
      new SaveFile(UserController.this).actionPerformed(e);
   }
}