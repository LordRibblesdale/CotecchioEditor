package Interface;

import FileManager.AutoSaveFile;
import FileManager.Path;
import FileManager.SaveFile;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class SettingsFrame extends JFrame {
    private final String text;
    private final String text2;
    private final String[] locale = {"English", "Italiano"};
    private Timer autoSave;
    private JLabel timerInfo;
    private JLabel percentageInfo;
    private JSlider delay;
    private JSlider percentage;
    private JComboBox languages;
    private JCheckBox useLookAndFeel;
    private UserController ui;

    private static Insets insets = new Insets(5, 5, 5, 5);

    public SettingsFrame(UserController ui) {
        super(ui.getSettings().getResourceBundle().getString("settings"));
        text = ui.getSettings().getResourceBundle().getString("autoSaveText");
        text2 = ui.getSettings().getResourceBundle().getString("percentagePlays");
        setIconImage(new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("Data/cotecchio.png"))).getImage());
        this.ui = ui;

        setLayout(new GridBagLayout());

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = insets;
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridwidth = 2;
        add(timerInfo = new JLabel(text + " " + ui.getSettings().getRefreshSaveRate()/1000 + " " + ui.getSettings().getResourceBundle().getString("seconds")), constraints);

        constraints = new GridBagConstraints();
        constraints.insets = insets;
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridwidth = 2;
        add(delay = new JSlider(JSlider.HORIZONTAL), constraints);
        delay.setMinimum(15000);
        delay.setMaximum(150000);
        delay.setValue(ui.getSettings().getRefreshSaveRate());
        delay.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int range = delay.getMaximum() - delay.getMinimum();
                double valueAt = e.getPoint().x / (double) delay.getWidth();
                delay.setValue((int) (delay.getMinimum() + valueAt*range));
                ui.getSettings().setRefreshSaveRate(delay.getValue());

                validate();
                saveSettings();
            }
        });

        constraints.insets = insets;
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridwidth = 2;
        add(percentageInfo = new JLabel(text2 + " " + ui.getSettings().getPercentage()), constraints);

        constraints = new GridBagConstraints();
        constraints.insets = insets;
        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridwidth = 2;
        add(percentage = new JSlider(JSlider.HORIZONTAL), constraints);
        percentage.setMinimum(0);
        percentage.setMaximum(100);
        percentage.setPaintTicks(true);
        percentage.setSnapToTicks(true);
        percentage.setValue(ui.getSettings().getPercentage());
        percentage.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int range = percentage.getMaximum() - percentage.getMinimum();
                double valueAt = e.getPoint().x / (double) percentage.getWidth();
                percentage.setValue((int) (percentage.getMinimum() + valueAt*range));
                ui.getSettings().setPercentage(percentage.getValue());

                validate();
                saveSettings();
            }
        });

        constraints = new GridBagConstraints();
        constraints.insets = insets;
        constraints.gridx = 0;
        constraints.gridy = 4;
        add(new JLabel(ui.getSettings().getResourceBundle().getString("selectLanguage")), constraints);

        constraints = new GridBagConstraints();
        constraints.insets = insets;
        constraints.gridx = 1;
        constraints.gridy = 4;
        add(languages = new JComboBox(locale), constraints);

        constraints = new GridBagConstraints();
        constraints.insets = insets;
        constraints.gridx = 0;
        constraints.gridy = 5;
        add(new JLabel(ui.getSettings().getResourceBundle().getString("useLookAndFeel")), constraints);

        constraints = new GridBagConstraints();
        constraints.insets = insets;
        constraints.gridx = 1;
        constraints.gridy = 5;
        add(useLookAndFeel = new JCheckBox(), constraints);
        useLookAndFeel.setSelected(ui.getSettings().isUsingLookAndFeel());

       languages.addActionListener(e -> {
          JComboBox tmp = (JComboBox) e.getSource();

          switch ((String) Objects.requireNonNull(tmp.getSelectedItem())) {
             case "English":
                ui.getSettings().setLanguage("en");
                ui.getSettings().setCountry("UK");
                break;
             case "Italiano":
                ui.getSettings().setLanguage("it");
                ui.getSettings().setCountry("IT");
                break;
          }

          ui.setSettings(ui.getSettings());
          JOptionPane.showMessageDialog(ui, ui.getSettings().getResourceBundle().getString("restartToApply"));
       });

        autoSave = new Timer(ui.getSettings().getRefreshSaveRate(), actionEvent -> {
           try {
             if ((new File(ui.getSettings().getOpenedFile())).exists()) {
               new AutoSaveFile(ui);
             } else {
               int choice = JOptionPane.showConfirmDialog(ui,
                       ui.getSettings().getResourceBundle().getString("askSaveChanges"),
                       ui.getSettings().getResourceBundle().getString("askSave"),
                       JOptionPane.YES_NO_OPTION);

               if (choice == JOptionPane.OK_OPTION) {
                 new SaveFile(ui);
                 ui.setHasBeenSaved(true);
               }
             }
           } catch (IOException e) {
              ui.getStatus().setText(ui.getSettings().getResourceBundle().getString("errorAutoSaving")
                      + autoSave.getDelay()/1000
                      + " " + ui.getSettings().getResourceBundle().getString("seconds"));
           } catch (ClassNotFoundException e) {
             e.printStackTrace();
             JOptionPane.showMessageDialog(ui,
                 ui.getSettings().getResourceBundle().getString("errorReadingFile"),
                 "Error Settings 00_AutoSaveFile" + e.getStackTrace()[0].getLineNumber(),
                 JOptionPane.ERROR_MESSAGE);

           }
        });

        delay.addChangeListener(changeEvent -> {
            System.out.println(((JSlider) changeEvent.getSource()).getValue());
            autoSave.setDelay(((JSlider) changeEvent.getSource()).getValue());
            if (!ui.hasBeenSaved()) {
                autoSave.start();
            }
            ui.getSettings().setRefreshSaveRate(((JSlider) changeEvent.getSource()).getValue());
            timerInfo.setText(text + delay.getValue()/1000 + " " + ui.getSettings().getResourceBundle().getString("seconds"));

            saveSettings();
            validate();
        });

        percentage.addChangeListener(changeEvent -> {
            ui.getSettings().setPercentage(((JSlider) changeEvent.getSource()).getValue());
            percentageInfo.setText(text2 + percentage.getValue());

            saveSettings();
            validate();
        });

        useLookAndFeel.addActionListener(e -> {
            if (((JCheckBox) e.getSource()).isSelected()) {
                ui.getSettings().setUseLookAndFeel(true);
            } else {
                ui.getSettings().setUseLookAndFeel(false);
            }

            saveSettings();
            validate();
        });

        useLookAndFeel.setToolTipText(ui.getSettings().getResourceBundle().getString("warningPerformance"));

        setMinimumSize(new Dimension(300, 100));
        pack();
        setLocationRelativeTo(ui);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setVisible(false);
    }

    void startTimer() {
       autoSave.start();
    }
    void stopTimer() {
       autoSave.stop();
    }

    private void saveSettings() {
        ui.setSettings(ui.getSettings());
    }
}
