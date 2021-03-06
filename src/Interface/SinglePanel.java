package Interface;

import Data.PlayerStateGame;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.List;

class SinglePanel extends JPanel {
  class ComboBoxToolTipRenderer extends DefaultListCellRenderer {
    List<String> tooltips;

    @Override
    public Component getListCellRendererComponent(JList list, Object value,
                                                  int index, boolean isSelected, boolean cellHasFocus) {

      JComponent comp = (JComponent) super.getListCellRendererComponent(list,
          value, index, isSelected, cellHasFocus);

      if (-1 < index && null != value && null != tooltips) {
        list.setToolTipText(tooltips.get(index));
      }
      return comp;
    }

    public void setTooltips(List<String> tooltips) {
      this.tooltips = tooltips;
    }
  }

  private UserController ui;
  private int index;

  private GridBagLayout layout;
  private GridBagConstraints constraints;
  private JComboBox<String> players;
  private JLabel[] label = new JLabel[4];
  private ComboBoxToolTipRenderer toolTip;
  private JSpinner points, pelliccions, cappottens;
  private FocusAdapter focusAdapter = new FocusAdapter() {
    @Override
    public void focusGained(FocusEvent e) {
      super.focusGained(e);

      if (((JFormattedTextField) e.getSource()).hasFocus()) {

        SwingUtilities.invokeLater(() -> ((JFormattedTextField) e.getSource()).selectAll());
      }
    }
  };

  SinglePanel(UserController ui) {
    super();
    this.ui = ui;

    setLayout(layout = new GridBagLayout());

    setLayout();

    toolTip = new ComboBoxToolTipRenderer();
    toolTip.setTooltips(ui.getNames());
    players.setRenderer(toolTip);

    ((JSpinner.DefaultEditor) points.getEditor()).getTextField().addFocusListener(focusAdapter);
    ((JSpinner.DefaultEditor) pelliccions.getEditor()).getTextField().addFocusListener(focusAdapter);
    ((JSpinner.DefaultEditor) cappottens.getEditor()).getTextField().addFocusListener(focusAdapter);

    setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
  }

  SinglePanel(UserController ui, int index) {
    this(ui);

    players.setSelectedIndex(index);
    this.index = index;
  }

  SinglePanel(UserController ui, PlayerStateGame game, boolean isEditable) {
    this(ui);

    setPoints(game);

    if (!isEditable) {
      disableAll();
    }
  }

  private void setLayout() {
    constraints = new GridBagConstraints();

    constraints.gridx = 0;
    constraints.gridy = 0;
    constraints.insets = new Insets(5, 5, 5, 5);
    add(label[0] = new JLabel(ui.getSettings().getResourceBundle().getString("player")), constraints);

    constraints.gridx = 0;
    constraints.gridy = 1;
    add(players = new JComboBox<>(ui.getUsernames().toArray(new String[0])), constraints);

    constraints.gridx = 1;
    constraints.gridy = 0;
    add(label[1] = new JLabel(ui.getSettings().getResourceBundle().getString("pointsMatch")), constraints);

    constraints.gridx = 1;
    constraints.gridy = 1;
    add(points = new JSpinner(new SpinnerNumberModel(0, 0, 10, 1)), constraints);

    constraints.gridx = 2;
    constraints.gridy = 0;
    add(label[2] = new JLabel(ui.getSettings().getResourceBundle().getString("pelliccions")), constraints);

    constraints.gridx = 2;
    constraints.gridy = 1;
    add(pelliccions = new JSpinner(new SpinnerNumberModel(0, 0, 100, 1)), constraints);

    constraints.gridx = 3;
    constraints.gridy = 0;
    add(label[3] = new JLabel(ui.getSettings().getResourceBundle().getString("cappottens")), constraints);

    constraints.gridx = 3;
    constraints.gridy = 1;
    add(cappottens = new JSpinner(new SpinnerNumberModel(0, 0, 100, 1)), constraints);
  }

  private void setPoints(PlayerStateGame game) {
    boolean isSet = false;
    for (int i = 0; i < players.getItemCount(); i++) {
      if (players.getModel().getElementAt(i).equals(game.getUsername())) {
        players.getModel().setSelectedItem(players.getModel().getElementAt(i));
        isSet = true;
        break;
      }
    }

    if (!isSet) {
      players.setModel(new DefaultComboBoxModel<>(new String[] {
          game.getUsername()
      }));
    }

    points.setValue(game.getPointsEndGame());
    pelliccions.setValue(game.getPelliccionsTaken());
    cappottens.setValue(game.getCappottensTaken());
  }

  private void disableAll() {
    players.setEnabled(false);
    points.setEnabled(false);
    pelliccions.setEnabled(false);
    cappottens.setEnabled(false);
  }

  String getPlayer() {
    return (String) players.getSelectedItem();
  }

  int getPoints() {
    return (Integer) points.getValue();
  }

  int getPelliccions() {
    return (Integer) pelliccions.getValue();
  }

  int getCappottens() {
    return (Integer) cappottens.getValue();
  }

  void setListener(ActionListener l) {
    players.addActionListener(l);
  }

  boolean isEqualUsername(String username) {
    return username.equals(players.getModel().getSelectedItem());
  }


}
