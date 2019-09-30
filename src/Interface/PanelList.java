package Interface;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Objects;

public class PanelList extends JFrame {
   private UserController ui;
   private JScrollPane scrollPane;
   private JList<String> list;
   private ListSelectionListener l = new ListSelectionListener() {
      @Override
      public void valueChanged(ListSelectionEvent e) {
         if (ui.getTabs() != null) {
            if (((JList<String>) e.getSource()).getSelectedIndex() != -1) {
               ui.getTabs().setSelectedIndex(((JList<String>) e.getSource()).getSelectedIndex());
            } else {
               ui.getTabs().setSelectedIndex(0);
            }
         }
      }
   };

   PanelList(UserController ui) {
      super(ui.getSettings().getResourceBundle().getString("panelList"));
      setIconImage(new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("Data/cotecchio.png"))).getImage());

      this.ui = ui;
      updateList();

      addWindowListener(new WindowAdapter() {
         @Override
         public void windowClosing(WindowEvent e) {
            ui.getShowList().setState(false);
         }
      });

      setSize(new Dimension(ui.getWidth()/2, ui.getHeight()));
      setLocation(ui.getX() - ui.getWidth()/2, ui.getY());
      setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
      setAlwaysOnTop(true);
      setVisible(false);
   }

   void updateList() {
      if (ui.getTabs() != null) {
         ArrayList<String> strings = new ArrayList<>();

         for (int i = 0; i < ui.getPlayers().size(); i++) {
            strings.add(ui.getPlayers().get(i).getName());
         }

         if (scrollPane != null) {
            list.setModel(new DefaultComboBoxModel<>(strings.toArray(new String[0])));
         } else {
            list = new JList<>(strings.toArray(new String[0]));
            list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
            list.addListSelectionListener(l);
            scrollPane = new JScrollPane(list);
            scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

            add(scrollPane);
         }

         validate();
      }
   }
}
