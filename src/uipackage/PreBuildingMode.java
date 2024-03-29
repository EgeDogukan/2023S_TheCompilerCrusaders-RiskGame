package uipackage;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PreBuildingMode extends JFrame {

    JComboBox<String> numberOfPlayerComboBox;
    JComboBox<String> numberOfCompComboBox;
    JButton nextButton;
    int numberOfPlayer;
    int numberOfCompPlayer;

    public PreBuildingMode() throws Exception {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        this.setSize(1920,1080);
        this.setLayout(null);
        JLabel sJLabel = new JLabel(new ImageIcon("login2.png"));
        this.setContentPane(sJLabel); // Set a background image

        Font customFont = new Font("Serif", Font.BOLD, 20); // A custom font

        JLabel numberOfPlayerJLabel = new JLabel("Number of Human Player");
        numberOfPlayerJLabel.setBounds(500, 60, 300, 50);
        numberOfPlayerJLabel.setFont(customFont);
        numberOfPlayerJLabel.setForeground(Color.BLACK);

        JLabel numberOfCompJLabel = new JLabel("Number of Computer Player");
        numberOfCompJLabel.setBounds(850, 60, 300, 50);
        numberOfCompJLabel.setFont(customFont);
        numberOfCompJLabel.setForeground(Color.BLACK);

        String[] options = {"1", "2", "3", "4", "5", "6"};
        numberOfPlayerComboBox = new JComboBox<>(options);
        numberOfPlayerComboBox.setBounds(500, 100, 210, 70);
        numberOfPlayerComboBox.setFont(customFont);
        numberOfPlayerComboBox.setBorder(new LineBorder(Color.BLACK, 2));

        String[] options2 = {"0", "1", "2"};
        numberOfCompComboBox = new JComboBox<>(options2);
        numberOfCompComboBox.setBounds(850, 100, 210, 70);
        numberOfCompComboBox.setFont(customFont);
        numberOfCompComboBox.setBorder(new LineBorder(Color.BLACK, 2));

        ImageIcon icon = new ImageIcon("nextStageButton.png"); // A custom image for the button
        nextButton = new JButton(icon);
        nextButton.setBounds(630, 200, 300, 100);
        nextButton.setBorderPainted(false); 
        nextButton.setContentAreaFilled(false); 

        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setNumberOfPlayer(Integer.parseInt((String) numberOfPlayerComboBox.getSelectedItem()));
                setNumberOfCompPlayer(Integer.parseInt((String) numberOfCompComboBox.getSelectedItem()));
                dispose();
            }
        });

        this.add(numberOfPlayerJLabel);
        this.add(numberOfCompJLabel);
        this.add(numberOfPlayerComboBox);
        this.add(numberOfCompComboBox);
        this.add(nextButton);
        this.setVisible(true);
    }

    public int getNumberOfPlayer() {
        return numberOfPlayer;
    }

    public void setNumberOfPlayer(int numberOfPlayer) {
        this.numberOfPlayer = numberOfPlayer;
    }

    public int getNumberOfCompPlayer() {
        return numberOfCompPlayer;
    }

    public void setNumberOfCompPlayer(int numberOfCompPlayer) {
        this.numberOfCompPlayer = numberOfCompPlayer;
    }

}
