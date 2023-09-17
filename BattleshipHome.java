/**
 * This is a battleship home GUI where you select the difficulty of the battleship bot.
 *
 * @author Veloan Balendran
 * @date 19-Jan-2023
 */

import javax.swing.*;
import java.awt.Color;
import java.awt.Font;
import javax.swing.SwingConstants;
import java.awt.GridLayout;
import java.io.*;
import java.util.Random;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.BorderFactory;
import java.awt.GraphicsEnvironment;

public class BattleshipHome extends JFrame implements ActionListener{

  private JLabel background, title;
  private JButton playButton, homeButton;
  private JButton[] difficultyButtons;

  // colors 
  private Color lBlue = new Color(10,255,255);
  private Color dBlue = new Color(0, 201, 255);
  private Color green = new Color(80, 200, 120);
  private Color dGreen = new Color(0, 163, 108);
  private Color lBlack = new Color(52, 52, 52);
  private Color black = new Color(0, 0, 0);
  private Color yellow = new Color(251, 236, 93);
  private Color dYellow = new Color(255, 192, 0);
  private Color orange = new Color(255, 95, 31);
  private Color dOrange = new Color (255, 165, 0);
  private Color red = new Color(215, 0, 64);
  private Color dRed = new Color (136, 8, 8);

  // fonts
  private Font t = new Font("SansSerif", Font.BOLD, 50);
  private Font m = new Font("Comic Sans MS", Font.PLAIN, 30);
  private Font s = new Font("Comic Sans MS", Font.PLAIN, 18);

  public BattleshipHome(){
    setSize(600, 600);
    this.setLayout(null);

    // title setup
    title = new JLabel("Battleship");
    title.setFont(t);
    title.setSize(300, 60);
    title.setLocation(150, 50);
    title.setBackground(Color.WHITE);
    title.setOpaque(true);
    title.setHorizontalAlignment(JLabel.CENTER);
    this.add(title);
    
    // playButton setup
    playButton = new JButton("Play");
    playButton.setBounds(250, 200, 100, 50);
    playButton.setBackground(lBlue);
    playButton.setOpaque(true);
    playButton.setFocusable(false);
    playButton.setBorder(BorderFactory.createLineBorder(dBlue, 5));
    playButton.setFont(m);
    playButton.addActionListener(this);
    this.add(playButton);

    difficultyButtons = new JButton[5];
    for(int i = 0; i < 5; i++){
      // setup for all 5 difficulty of buttons
      difficultyButtons[i] = new JButton("");
      difficultyButtons[i].setSize(150, 50);
      difficultyButtons[i].setOpaque(true);
      difficultyButtons[i].setFocusable(false);
      difficultyButtons[i].setFont(s);
      difficultyButtons[i].addActionListener(this);
      difficultyButtons[i].setVisible(false);
      difficultyButtons[i].setForeground(Color.WHITE);
      this.add(difficultyButtons[i]);
    }
    
    // setting text on difficulty buttons
    difficultyButtons[0].setText("Beginner");
    difficultyButtons[1].setText("Intermediate");
    difficultyButtons[2].setText("Advanced");
    difficultyButtons[3].setText("Expert");
    difficultyButtons[4].setText("Impossible");

    // setting location of difficulty buttons
    difficultyButtons[0].setLocation(50, 200);
    difficultyButtons[1].setLocation(225, 200);
    difficultyButtons[2].setLocation(400, 200);
    difficultyButtons[3].setLocation(125, 300);
    difficultyButtons[4].setLocation(325, 300);

    // setting colours for easy button
    difficultyButtons[0].setBackground(green);
    difficultyButtons[0].setBorder(BorderFactory.createLineBorder(dGreen, 5));

    // setting colours for intermediate button
    difficultyButtons[1].setBackground(yellow);
    difficultyButtons[1].setBorder(BorderFactory.createLineBorder(dYellow, 5));

    // setting colours for advanced button
    difficultyButtons[2].setBackground(dOrange);
    difficultyButtons[2].setBorder(BorderFactory.createLineBorder(orange, 5));

    // setting colours for expert button
    difficultyButtons[3].setBackground(red);
    difficultyButtons[3].setBorder(BorderFactory.createLineBorder(dRed, 5));

    // setting colours for impossible button
    difficultyButtons[4].setBackground(lBlack);
    difficultyButtons[4].setBorder(BorderFactory.createLineBorder(black, 5));

    // homeButton setup
    homeButton = new JButton(new ImageIcon("home.png"));
    homeButton.setLocation(20, 30);
    homeButton.setSize(75, 75);
    homeButton.addActionListener(this);
    homeButton.setFocusable(false);
    homeButton.setOpaque(true);
    homeButton.setBackground(Color.WHITE);
    homeButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 5));
    this.add(homeButton);

    // background image setup
    background = new JLabel(new ImageIcon("background.jpg"));
    background.setBounds(0,0, 600, 600);
    this.add(background);

    this.setUndecorated(true); // remove min max and close buttons
    setVisible(true);
    
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    if (e.getSource() == playButton){
      playButton.setVisible(false); // hide playButton
      for(int i = 0; i < 5; i++){
        difficultyButtons[i].setVisible(true); // show all difficulty buttons
      }
    }
    else if(e.getSource() == homeButton){
      this.dispose(); // dispose this
      new homeScreen(); // open home screen
    }
    else{
      for(int i = 0; i < 5; i++){
        if(e.getSource() == difficultyButtons[i]){
          new Battleship(difficultyButtons[i].getText()); // make a battleship game passing in the difficulty
          this.dispose(); // dispose this
        }
      }
    }
 } 
}