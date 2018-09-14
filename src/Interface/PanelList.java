package Interface;

import Interface.UserInterface;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class PanelList extends JFrame {
   private UserInterface ui;
   private JTabbedPane tab;
   private JScrollPane scrollPane;
   private JList<String> list;

   PanelList(UserInterface ui) {
      super("Interface.PanelList");

      list = new JList<>();
      updateList();

      list.addListSelectionListener(new ListSelectionListener() {
         @Override
         public void valueChanged(ListSelectionEvent e) {
            tab.setSelectedIndex(((JList<String>) e.getSource()).getSelectedIndex());
            validate();
         }
      });

      scrollPane = new JScrollPane(list);
      add(scrollPane);
      add(new JLabel("This is an experimental feature"), BorderLayout.PAGE_END);

      addWindowListener(new WindowAdapter() {
         @Override
         public void windowClosing(WindowEvent e) {
            super.windowClosing(e);

            ui.getShowList().setState(false);
         }
      });

      setSize(new Dimension(ui.getWidth()/2, ui.getHeight()));
      setLocation(ui.getX() - ui.getWidth()/2, ui.getY());
      setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
      setVisible(false);
   }

   public void updateList() {
      if (tab != null) {
         tab = ui.getTabs();
      }

      list.removeAll();

      if (tab != null) {
         for (int i = 0; i < tab.getTabCount(); i++) {
            list.add(new JLabel(tab.getTitleAt(i)));
         }
      }

      repaint();
   }
}
