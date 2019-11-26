package Interface;

import Data.CotecchioDataArray;
import Data.Player;
import FileManager.SaveFile;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Comparator;

class EditPanel extends JPanel {
  private JButton addTab, removeTab, resetValues, history;

  private JTabbedPane tabs = null;
  private ArrayList<PlayerUI> pUI;
  private JPanel bottomPanel;

  private UserController ui;

  EditPanel(UserController ui) {
    super();
    setLayout(new BorderLayout());

    this.ui = ui;

    bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    bottomPanel.add(history = new JButton(ui.getSettings().getResourceBundle().getString("history")));
    bottomPanel.add(new JSeparator(SwingConstants.VERTICAL));
    bottomPanel.add(addTab = new JButton(ui.getSettings().getResourceBundle().getString("addTab")));
    bottomPanel.add(removeTab = new JButton(ui.getSettings().getResourceBundle().getString("removeTab")));
    bottomPanel.add(resetValues = new JButton(ui.getSettings().getResourceBundle().getString("resetValues")));

    initialise();

    addTab.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        ui.getData().getPlayers().add(new Player(ui.getSettings().getResourceBundle().getString("newPlayer0"),
            ui.getSettings().getResourceBundle().getString("newPlayer1"), 0, 0, 0, 0, 0, 0));
        pUI.add(new PlayerUI(ui.getData().getPlayers().get(ui.getData().getPlayers().size()-1), ui));
        tabs.addTab(ui.getSettings().getResourceBundle().getString("newPlayer0"), pUI.get(pUI.size()-1).generatePanel());

        if (tabs.getTabCount() > 1) {
          removeTab.setEnabled(true);
        }

        if (ui.getListPlayers() == null) {
          ui.setListPlayers(new PanelList(ui));
        }

        ui.getListPlayers().updateList();
        ui.setHasBeenSaved(false);
        tabs.setSelectedIndex(tabs.getTabCount()-1);
        validate();
      }
    });

    removeTab.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (!ui.hasBeenSaved()) {
          Object[] choice = {ui.getSettings().getResourceBundle().getString("yes"),
              ui.getSettings().getResourceBundle().getString("no"),
              ui.getSettings().getResourceBundle().getString("goBack")};
          int sel = JOptionPane.showOptionDialog(ui.getFrame(),
              ui.getSettings().getResourceBundle().getString("notSavedTab"),
              ui.getSettings().getResourceBundle().getString("saveFile"),
              JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null,
              choice, choice[0]);

          if (choice[sel] == choice[0]) {
            tabs.remove(tabs.getSelectedIndex());
            ui.getPlayers().remove(tabs.getSelectedIndex()+1);
            pUI.remove(tabs.getSelectedIndex()+1);

            new SaveFile(ui).actionPerformed(null);

            if (tabs.getTabCount() == 1) {
              removeTab.setEnabled(false);
            }

            ui.getListPlayers().updateList();
            ui.setHasBeenSaved(false);
          } else if (choice[sel] == choice[1]) {
            tabs.remove(tabs.getSelectedIndex());
            ui.getPlayers().remove(tabs.getSelectedIndex()+1);
            pUI.remove(tabs.getSelectedIndex()+1);

            if (tabs.getTabCount() == 1) {
              removeTab.setEnabled(false);
            }

            ui.getListPlayers().updateList();
          }
        } else {
          tabs.remove(tabs.getSelectedIndex());
          ui.getPlayers().remove(tabs.getSelectedIndex());
          pUI.remove(tabs.getSelectedIndex());

          if (tabs.getTabCount() == 1) {
            removeTab.setEnabled(false);
          }

          ui.getListPlayers().updateList();
          ui.setHasBeenSaved(false);
        }

        validate();
      }
    });

    resetValues.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        Object[] sel = {
            ui.getSettings().getResourceBundle().getString("noneButton"),
            ui.getSettings().getResourceBundle().getString("singleTabButton"),
            ui.getSettings().getResourceBundle().getString("allTabsButton")
        };

        Object choice = JOptionPane.showInputDialog(ui.getFrame(),
            ui.getSettings().getResourceBundle().getString("resetConfirm"),
            ui.getSettings().getResourceBundle().getString("resetTitle"),
            JOptionPane.INFORMATION_MESSAGE, null, sel, sel[0]);

        if (choice == sel[1]) {
          ui.getPUI().get(ui.getTabs().getSelectedIndex()).resetPoints();
        } else if (choice == sel[2]) {
          for (PlayerUI p : ui.getPUI()) {
            p.resetPoints();
          }
        }
      }
    });

    addTab.setEnabled(false);
    removeTab.setEnabled(false);

    add(bottomPanel, BorderLayout.PAGE_END);
  }

  void initialise() {
    if (tabs != null) {
      for (Component c : getComponents()) {
        if (c == tabs) {
          remove(c);
        }
      }

      revalidate();
      repaint();
    }

    tabs = new JTabbedPane();
    add(tabs);

    pUI = new ArrayList<>();

    if (ui.getData().getPlayers().isEmpty()) {
      ui.getData().getPlayers().add(new Player(ui.getSettings().getResourceBundle().getString("newPlayer0"),
          ui.getSettings().getResourceBundle().getString("newPlayer1"),
          0, 0, 0, 0, 0, 0));
      pUI.add(new PlayerUI(ui.getData().getPlayers().get(ui.getData().getPlayers().size()-1), ui));
      tabs.addTab(ui.getSettings().getResourceBundle().getString("newPlayer0"), pUI.get(pUI.size()-1).generatePanel());
    } else {
      this.ui.getData().setPlayers(ui.getData().getPlayers());

      for (Player p : ui.getData().getPlayers()) {
        pUI.add(new PlayerUI(p, ui));
        tabs.addTab(pUI.get(pUI.size()-1).getJTextName().getText(), pUI.get(pUI.size()-1).generatePanel());
      }

      if (tabs.getTabCount() > 1) {
        removeTab.setEnabled(true);
      }
    }

    addTab.setEnabled(true);
    validate();

    ui.makeVisibleButtons();
  }

  void askForInitialisation() {
    //TODO: increase security
    initialise();
  }

  void setUpData(boolean isFirstCreation) {
    for (int i = 0; i < ui.getData().getPlayers().size(); i++) {
      ui.getData().getPlayers().set(i, new Player(pUI.get(i).getJTextName().getText(),
          pUI.get(i).getUsername().getText(),
          (Integer) (pUI.get(i).getInsert().get(0).getValue()),
          (Integer) (pUI.get(i).getInsert().get(1).getValue()),
          (Integer) (pUI.get(i).getInsert().get(2).getValue()),
          (Integer) (pUI.get(i).getInsert().get(3).getValue()),
          (Integer) (pUI.get(i).getInsert().get(4).getValue()),
          (Integer) (pUI.get(i).getInsert().get(5).getValue())));
    }

    ui.getPlayers().sort(Comparator.comparing(Player::getName));
    ui.prepareForInitialisation(ui.getData(), isFirstCreation);
  }

  void setData(CotecchioDataArray data) {
    ui.setData(data);
  }

  void setPlayers(ArrayList<Player> players) {
    ui.getData().getPlayers().clear();
    ui.getData().getPlayers().addAll(players);
  }

  ArrayList<String> getUsernames() {
    ArrayList<String> usernames = new ArrayList<>();

    for (Player p : ui.getData().getPlayers()) {
      usernames.add(p.getUsername());
    }

    return usernames;
  }

  ArrayList<String> getNames() {
    ArrayList<String> usernames = new ArrayList<>();

    for (Player p : ui.getData().getPlayers()) {
      usernames.add(p.getName());
    }

    return usernames;
  }

  JTabbedPane getTabs() {
    return tabs;
  }

  ArrayList<PlayerUI> getpUI() {
    return pUI;
  }
}
