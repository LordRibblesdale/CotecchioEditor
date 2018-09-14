package Interface;

import Data.Player;
import Edit.Search;
import File.About;
import File.NewFile;
import File.OpenFile;
import File.SaveFile;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class UserInterface extends JFrame {
   private static final String programName = "Cotecchio Editor - ";
   private static final String version = "Build 1 Alpha 1.0";
   private GridLayout mainLayout;
   private JPanel mainPanel;
   private JPanel buttonPanel;
   private JMenuBar menu;
   private JMenu file, edit, about;
   private JButton addTab, removeTab;
   private JCheckBoxMenuItem showList;

   private boolean hasBeenSaved = true;
   private SaveFile saveButton;
   private Search search;

   private JTabbedPane tabs = null;
   private ArrayList<Player> players = null;
   private ArrayList<PlayerUI> pUI;

   private JToolBar toolBar;
   private PanelList listPlayers = null;

   public UserInterface() {
      super(programName + version);

      mainPanel = new JPanel(mainLayout = new GridLayout(0, 10));

      menu = new JMenuBar();
      menu.add(file = new JMenu("File"));
      file.add(new NewFile(UserInterface.this));
      file.add(new OpenFile(UserInterface.this));
      file.add(saveButton = new SaveFile(UserInterface.this));
      saveButton.setEnabled(false);

      toolBar = new JToolBar(SwingConstants.VERTICAL);
      toolBar.add(search = new Search(UserInterface.this));
      search.setEnabled(false);
      add(toolBar, BorderLayout.LINE_END);

      menu.add(edit = new JMenu("Edit"));
      edit.add(search);
      edit.add(showList = new JCheckBoxMenuItem("Show players list"));
      showList.addItemListener(new ItemListener() {
         @Override
         public void itemStateChanged(ItemEvent e) {
            if (listPlayers != null) {
               if (((JCheckBoxMenuItem) e.getSource()).getState()) {
                  listPlayers.setVisible(true);
               } else {
                  listPlayers.setVisible(false);
               }
            } else {
               listPlayers = new PanelList(UserInterface.this);
               listPlayers.updateList();
               listPlayers.setVisible(true);
            }

            validate(); //??
         }
      });

      menu.add(about = new JMenu("About"));
      about.add(new About(UserInterface.this));

      setJMenuBar(menu);

      buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
      buttonPanel.add(addTab = new JButton("Add Tab"));
      buttonPanel.add(removeTab = new JButton("Remove this Tab"));

      addTab.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            players.add(new Player("New Player", 0, 0, 0, 0, 0));
            pUI.add(new PlayerUI(players.get(players.size()-1), UserInterface.this));
            tabs.addTab("New Player", pUI.get(pUI.size()-1).generatePanel());

            if (tabs.getTabCount() > 1) {
               removeTab.setEnabled(true);
            }

            //listPlayers.updateList();   //TODO Fix here

            validate();
         }
      });

      removeTab.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            if (!hasBeenSaved()) {
               Object[] choice = {"Yes", "No", "Go back"};
               int sel = JOptionPane.showOptionDialog(UserInterface.this, "This tab has not been saved.\nDo you want to save?",
                       "Save file?", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null,
                       choice, choice[0]);

               if (choice[sel] == choice[0]) {

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
            super.windowClosing(e);
            /*
            if (!hasBeenSaved()) {

            }
            */
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
         setHasBeenSaved(true);
      }

      tabs = new JTabbedPane();
      add(tabs);
      validate();

      players = new ArrayList<>();
      pUI = new ArrayList<>();

      players.add(new Player("New Player", 0, 0, 0, 0, 0));
      pUI.add(new PlayerUI(players.get(players.size()-1), UserInterface.this));
      tabs.addTab("New Player", pUI.get(pUI.size()-1).generatePanel());

      addTab.setEnabled(true);
      search.setEnabled(true);

      validate();
   }

   public void initialise(ArrayList<Player> players) {
      if (tabs != null) {
         remove(tabs);
         setHasBeenSaved(true);
      }

      tabs = new JTabbedPane();
      add(tabs);
      validate();

      this.players = players;
      pUI = new ArrayList<>();

      for (Player p : players) {
         pUI.add(new PlayerUI(p, UserInterface.this));
         tabs.addTab(pUI.get(pUI.size()-1).getJTextName().getText(), pUI.get(pUI.size()-1).generatePanel());
      }

      addTab.setEnabled(true);

      if (tabs.getTabCount() > 1) {
         removeTab.setEnabled(true);
      }

      validate();
   }

   public JTabbedPane getTabs() {
      return tabs;
   }

   public ArrayList<Player> getPlayers() {
      for (int i = 0; i < players.size(); i++) {
         players.set(i, new Player(pUI.get(i).getJTextName().getText(),
                 (Integer) (pUI.get(i).getInsert().get(0).getValue()),
                 (Integer) (pUI.get(i).getInsert().get(1).getValue()),
                 (Integer) (pUI.get(i).getInsert().get(2).getValue()),
                 (Integer) (pUI.get(i).getInsert().get(3).getValue()),
                 (Integer) (pUI.get(i).getInsert().get(4).getValue())));
      }

      return players;
   }

   public JCheckBoxMenuItem getShowList() {
      return showList;
   }
}