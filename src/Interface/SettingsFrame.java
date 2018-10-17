package Interface;

import FileManager.AutoSaveFile;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.Objects;

public class SettingsFrame extends JFrame {
    private final String text;
    private final String[] locale = {"English", "Italiano"};
    private Timer autoSave;
    private JLabel timerInfo;
    private JSlider delay;
    private JComboBox languages;
    private JCheckBox useLookAndFeel;
    private UserController ui;

    private static Insets insets = new Insets(5, 5, 5, 5);

    public SettingsFrame(UserController ui) {
        super(ui.getSettings().getResourceBundle().getString("settings"));
        text = ui.getSettings().getResourceBundle().getString("autoSaveText");
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
        delay.setMinimum(30000);
        delay.setMaximum(300000);
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

        constraints = new GridBagConstraints();
        constraints.insets = insets;
        constraints.gridx = 0;
        constraints.gridy = 2;
        add(new JLabel(ui.getSettings().getResourceBundle().getString("selectLanguage")), constraints);

        constraints = new GridBagConstraints();
        constraints.insets = insets;
        constraints.gridx = 1;
        constraints.gridy = 2;
        add(languages = new JComboBox(locale), constraints);

        constraints = new GridBagConstraints();
        constraints.insets = insets;
        constraints.gridx = 0;
        constraints.gridy = 3;
        add(new JLabel(ui.getSettings().getResourceBundle().getString("useLookAndFeel")), constraints);

        constraints = new GridBagConstraints();
        constraints.insets = insets;
        constraints.gridx = 1;
        constraints.gridy = 3;
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
              new AutoSaveFile(ui.getSettings().getOpenedFile(), ui.getPlayers());
              ui.setHasBeenSaved(true);
           } catch (IOException e) {
              ui.getStatus().setText(ui.getSettings().getResourceBundle().getString("errorAutoSaving")
                      + autoSave.getDelay()/1000
                      + " " + ui.getSettings().getResourceBundle().getString("seconds"));
           }
        });

        delay.addChangeListener(changeEvent -> {
            autoSave.setDelay(((JSlider) changeEvent.getSource()).getValue());
            if (!ui.hasBeenSaved()) {
                autoSave.start();
            }
            ui.getSettings().setRefreshSaveRate(((JSlider) changeEvent.getSource()).getValue());
            timerInfo.setText(text + delay.getValue()/1000 + " " + ui.getSettings().getResourceBundle().getString("seconds"));

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
