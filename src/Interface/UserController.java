package Interface;

import Data.Player;
import Edit.Search;
import Export.ExportLeaderboard;
import Export.ExportXls;
import Export.PrintThread;
import FileManager.About;
import FileManager.NewFile;
import FileManager.OpenFile;
import FileManager.SaveFile;
import Game.GameStarter;
import Game.OpenGameFile;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class UserController extends JFrame {
   private static final String programName = "Cotecchio Editor - ";
   private static final String version = "Build 3 Beta 1.0";
   private GridLayout mainLayout;
   private JPanel mainPanel;
   private JPanel buttonPanel;
   private JMenuBar menu;
   private JMenu file, edit, about, export, game;
   private JButton addTab, removeTab;
   private JCheckBoxMenuItem showList;

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

   public UserController() {
      super(programName + version);

      mainPanel = new JPanel(mainLayout = new GridLayout(0, 10));

      menu = new JMenuBar();
      menu.add(file = new JMenu("File"));
      file.add(new NewFile(UserController.this));
      file.add(new OpenFile(UserController.this));
      file.add(saveButton = new SaveFile(UserController.this));
      file.add(new JSeparator());
      file.add(export = new JMenu("Export..."));
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

      menu.add(edit = new JMenu("Edit"));
      edit.add(search);
      edit.add(showList = new JCheckBoxMenuItem("Show players list"));
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

            validate(); //??
         }
      });

      menu.add(game = new JMenu("Game"));
      game.add(start = new GameStarter(UserController.this));
      game.add(openGame = new OpenGameFile(UserController.this));
      openGame.setEnabled(false);
      start.setEnabled(false);

      menu.add(about = new JMenu("About"));
      about.add(new About(UserController.this));

      setJMenuBar(menu);

      buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
      buttonPanel.add(addTab = new JButton("Add Tab"));
      buttonPanel.add(removeTab = new JButton("Remove this Tab"));

      addTab.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            players.add(new Player("New Player", "newPlayer", 0, 0, 0, 0, 0));
            pUI.add(new PlayerUI(players.get(players.size()-1), UserController.this));
            tabs.addTab("New Player", pUI.get(pUI.size()-1).generatePanel());

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
               Object[] choice = {"Yes", "No", "Go back"};
               int sel = JOptionPane.showOptionDialog(UserController.this, "This tab has not been saved.\nDo you want to save?",
                       "Save file?", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null,
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
                       "Do you want to save before closing?", "Exit Confirmation",
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

      setMinimumSize(new Dimension(500, 500));
      setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
      setLocationRelativeTo(null);
      setVisible(true);
   }

   public boolean hasBeenSaved() {
      return hasBeenSaved;
   }

   public void setHasBeenSaved(boolean hasBeenSaved) {
      this.hasBeenSaved = hasBeenSaved;

      if (hasBeenSaved) {
         saveButton.setEnabled(false);
      } else {
         saveButton.setEnabled(true);
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

      players.add(new Player("New Player", "newPlayer", 0, 0, 0, 0, 0));
      pUI.add(new PlayerUI(players.get(players.size()-1), UserController.this));
      tabs.addTab("New Player", pUI.get(pUI.size()-1).generatePanel());

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

   public void askForSaving(ActionEvent e) {
      new SaveFile(UserController.this).actionPerformed(e);
   }
}