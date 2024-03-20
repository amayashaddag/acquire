package view.menu;

import javax.swing.*;

import app.launcher.App;
import control.game.GameController;
import model.game.Player;
import view.assets.Fonts;
import view.assets.MenuInterfaceMessages;
import view.frame.Form;
import view.frame.GameFrame;
import view.game.GameView;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;

public class MenuView extends Form {
    public final static int BUTTON_WIDTH = 700;
    public final static int BUTTON_HEIGHT = 100;
    public final static int VERTICAL_SPACE = 150;
    public final static int HORIZONTAL_SPACE = 50;

    public MenuView() {
        setLayout(new BorderLayout());

        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("Acquire", SwingConstants.CENTER);
        titleLabel.setFont(Fonts.TITLE_FONT);
        titlePanel.add(titleLabel);

        titleLabel.setFont(Fonts.TITLE_FONT);
        titlePanel.add(Box.createRigidArea(new Dimension(0, VERTICAL_SPACE)));

        add(titlePanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));

        Dimension buttonSize = new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT);

        JButton playButton = new JButton(MenuInterfaceMessages.PLAY);
        playButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        playButton.setFont(Fonts.BOLD_PARAGRAPH_FONT);
        playButton.setMaximumSize(buttonSize);
        buttonPanel.add(playButton);

        JButton optionsButton = new JButton(MenuInterfaceMessages.OPTIONS);
        optionsButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        optionsButton.setFont(Fonts.BOLD_PARAGRAPH_FONT);
        optionsButton.setMaximumSize(buttonSize);
        buttonPanel.add(optionsButton);

        JButton quitButton = new JButton(MenuInterfaceMessages.QUIT);
        quitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        quitButton.setFont(Fonts.BOLD_PARAGRAPH_FONT);
        quitButton.setMaximumSize(buttonSize);
        buttonPanel.add(quitButton);

        buttonPanel.add(Box.createRigidArea(new Dimension(HORIZONTAL_SPACE, VERTICAL_SPACE)));
        add(buttonPanel, BorderLayout.SOUTH);

        playButton.addActionListener(e -> {

        });

        optionsButton.addActionListener(e -> {

        });

        quitButton.addActionListener(e -> {

            System.exit(0);
        });
    }

    @Override
    public void setOn(GameFrame g) {
        g.getContentPane().removeAll();
        g.add(this);
        g.repaint();
        g.revalidate();
    }

}
