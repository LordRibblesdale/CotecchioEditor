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
   private JTabbedPane tab;
   private JScrollPane scrollPane;
   private JList<String> list;
   private ListSelectionListener l = new ListSelectionListener() {
      @Override
      public void valueChanged(ListSelectionEvent e) {
         if (tab != null) {
            tab.setSelectedIndex(((JList<String>) e.getSource()).getSelectedIndex());
            validate();
         }
      }
   };

   PanelList(UserController ui) {
      super("PanelList");
      setIconImage(new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("Data/cotecchio.png"))).getImage());

      this.ui = ui;
      updateList();

      add(new JLabel("This is an experimental feature"), BorderLayout.PAGE_END);

      addWindowListener(new WindowAdapter() {
         @Override
         public void windowClosing(WindowEvent e) {
            ui.getShowList().setState(false);
         }
      });

      setSize(new Dimension(ui.getWidth()/2, ui.getHeight()));
      setLocation(ui.getX() - ui.getWidth()/2, ui.getY());
      setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
      setVisible(false);
   }

   void updateList() {
      if (tab == null) {
         tab = ui.getTabs();
      }

      if (tab != null) {
         if (scrollPane != null) {
            remove(scrollPane);
         }

         ArrayList<String> strings = new ArrayList<>();

         for (int i = 0; i < tab.getTabCount(); i++) {
            strings.add(tab.getTitleAt(i));
         }

         String[] strings1 = new String[strings.size()];

         for (int i = 0; i < strings1.length; i++) {
            strings1[i] = strings.get(i);
         }

         list = new JList<>(strings1);
         list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
         list.addListSelectionListener(l);
         scrollPane = new JScrollPane(list);
         scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

         add(scrollPane);

         validate();
      }
   }
}
