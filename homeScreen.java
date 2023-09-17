/**
 *This is a home screen where different games can be selected to be played. Current games are Battleship, Wordle and Ultimate Tic Tac Toe.
 *
 * @author Veloan Balendran
 * @date 19-Jan-2023
 */

import javax.swing.*;
import java.awt.Color;
import java.awt.Font;
import javax.swing.SwingConstants;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class homeScreen extends JFrame implements ActionListener{

  private JLabel title, galaxy;
  private JButton wordle, UltTicTacToe, battleship;

  // fonts
  private Font t = new Font("SansSerif", Font.BOLD, 50);
  private Font m = new Font("Comic Sans MS", Font.PLAIN, 30);
  private Font s = new Font("Comic Sans MS", Font.PLAIN, 17);

  // colours
  private Color blue = new Color(30,144,255);
  private Color dBlue = new Color(0,0,128);
  private Color dGreen = new Color(0, 163, 108);
  private Color green = new Color(11, 218, 81);
  private Color red = new Color(210, 43, 43);
  private Color dRed = new Color(136, 8, 8);
  

  public homeScreen(){
    setSize(600, 600);
    this.setLayout(null);
    
    // title
    title = new JLabel("Game Galaxy");
    title.setForeground(Color.WHITE);
    title.setFont(t);
    title.setSize(600, 75);
    title.setHorizontalAlignment(JLabel.CENTER);
    this.add(title);

    // wordle game button 
    wordle = new JButton("Wordle");
    wordle.setFocusable(false);
    wordle.setSize(200, 100);
    wordle.setLocation(200, 400);
    wordle.addActionListener(this);
    wordle.setFont(m);
    wordle.setForeground(Color.WHITE);
    wordle.setBackground(green);
    wordle.setBorder(BorderFactory.createLineBorder(dGreen, 10));
    this.add(wordle);

    // Ultimate Tic Tac Toe Button
    UltTicTacToe = new JButton("Ultimate Tic Tac Toe");
    UltTicTacToe.setFocusable(false);
    UltTicTacToe.setSize(200, 100);
    UltTicTacToe.setLocation(200, 250);
    UltTicTacToe.addActionListener(this);
    UltTicTacToe.setFont(s);
    UltTicTacToe.setForeground(Color.WHITE);
    UltTicTacToe.setBackground(red);
    UltTicTacToe.setBorder(BorderFactory.createLineBorder(dRed, 10));
    this.add(UltTicTacToe);

    // battleship game button
    battleship = new JButton("Battleship");
    battleship.setFocusable(false);
    battleship.setSize(200, 100);
    battleship.setLocation(200, 100);
    battleship.setFont(m);
    battleship.addActionListener(this);
    battleship.setForeground(Color.WHITE);
    battleship.setBackground(blue);
    battleship.setBorder(BorderFactory.createLineBorder(dBlue, 10));
    this.add(battleship);

    // background image
    galaxy = new JLabel(new ImageIcon("galaxy.jpg"));
    galaxy.setBounds(0,0, 600, 600);
    this.add(galaxy);

    setVisible(true); 
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    
  }

  
  @Override
  public void actionPerformed(ActionEvent e) {
    this.dispose();
    if (e.getSource() == wordle){
      // open wordle
      new Wordle();
    }
    else if (e.getSource() == UltTicTacToe){
      // open ultimate tic tac toe
      new UltTicTacToe();
    }
    else if (e.getSource() == battleship){
      // open battleship
      new BattleshipHome();
    }
 } 
}