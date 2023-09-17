/**
 * This is an infinite wordle game where it randomly generates a word and the user has to try to guess the word in 6 or less guesses.
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
import java.util.Arrays;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.BorderFactory;


public class Wordle extends JFrame implements KeyListener, ActionListener{
  
  private int guesses;
  private String[] validWords;
  private char[] answer;
  private boolean gameOver;

  private JFrame endFrame;
  private JLabel title, endText;
  private JPanel titlePanel, gridPanel, keyboardPanel1, keyboardPanel2, keyboardPanel3; 
  private JLabel[][] gameGrid;
  private JLabel[] keyboardR1, keyboardR2, keyboardR3;
  private JButton homeButton1,homeButton2, playAgain, backButton;

  // fonts
  private Font t = new Font("SansSerif", Font.BOLD, 50);
  private Font m = new Font("SansSerif", Font.BOLD, 25);
  private Font s = new Font("SansSerif", Font.BOLD, 10);

  // array of letters in keyboard layout
  private final char KEYLETTERS[] = {'Q', 'W', 'E', 'R', 'T', 'Y', 'U', 'I', 'O', 'P', 'A', 'S', 'D', 'F', 'G', 'H', 'J', 'K', 'L', 'Z', 'X', 'C', 'V', 'B', 'N', 'M'};

  public Wordle(){
    guesses = 0;

    // intializes array of allowed guesses
    validWords = new String[12972];
    initializeValidWords();

    // intializes answer
    answer = new char[5];
    initAnswer();

    gameOver = false;

    setSize(600, 600);
    this.setLayout(null);

    // title setup
    titlePanel = new JPanel();
    title = new JLabel("Wordle");
    title.setFont(t);
    titlePanel.setBounds(200, 0, 200, 75);
    titlePanel.add(title);
    this.add(titlePanel); 

    // game panel setup
    gridPanel = new JPanel();
    GridLayout layout = new GridLayout(6,5);
    layout.setHgap(10);
    layout.setVgap(10);
    gridPanel.setLayout(layout);
    gridPanel.setBounds(150, 75, 300, 300);
    this.add(gridPanel);

    // game grid setup
    gameGrid = new JLabel[6][5];
    for (int i = 0; i < 6; i++){
      for (int j = 0; j < 5; j++){
        gameGrid[i][j] = new JLabel("");
        gameGrid[i][j].setOpaque(true);
        gameGrid[i][j].setBorder(BorderFactory.createLineBorder(Color.black));

        gameGrid[i][j].setHorizontalAlignment(SwingConstants.CENTER);
        gridPanel.add(gameGrid[i][j]);
      }
    }

    // first row of visual keyboard setup
    keyboardPanel1 = new JPanel();
    GridLayout layout2 = new GridLayout(1, 10);
    layout2.setHgap(10);
    keyboardPanel1.setLayout(layout2);
    keyboardPanel1.setSize(450, 45);
    keyboardPanel1.setLocation(100, 400);
    keyboardR1 = new JLabel[10];
    
    for (int i = 0; i < 10; i++){
      keyboardR1[i] = new JLabel("" + KEYLETTERS[i]);
      keyboardR1[i].setFont(s);
      keyboardR1[i].setOpaque(true);
      keyboardR1[i].setBackground(Color.WHITE);
      keyboardR1[i].setBorder(BorderFactory.createLineBorder(Color.black));
      keyboardR1[i].setHorizontalAlignment(SwingConstants.CENTER);
      keyboardPanel1.add(keyboardR1[i]);
    }
    this.add(keyboardPanel1);

    // 2nd row of visual keyboard setup
    keyboardPanel2 = new JPanel();
    GridLayout layout3 = new GridLayout(1, 9);
    layout3.setHgap(10);
    keyboardPanel2.setLayout(layout3);
    keyboardPanel2.setSize(405, 45);
    keyboardPanel2.setLocation(130, 450);
    keyboardR2 = new JLabel[9];
    
    for (int i = 0; i < 9; i++){
      keyboardR2[i] = new JLabel("" + KEYLETTERS[i+10]);
      keyboardR2[i].setFont(s);
      keyboardR2[i].setOpaque(true);
      keyboardR2[i].setBackground(Color.WHITE);
      keyboardR2[i].setBorder(BorderFactory.createLineBorder(Color.black));
      keyboardR2[i].setHorizontalAlignment(SwingConstants.CENTER);
      keyboardPanel2.add(keyboardR2[i]);
    }
    this.add(keyboardPanel2);

    // 3rd row of visual keyboard setup
    keyboardPanel3 = new JPanel();
    GridLayout layout4 = new GridLayout(1, 7);
    layout4.setHgap(10);
    keyboardPanel3.setLayout(layout4);
    keyboardPanel3.setSize(315, 45);
    keyboardPanel3.setLocation(180, 500);
    keyboardR3 = new JLabel[7];
    
    for (int i = 0; i < 7; i++){
      keyboardR3[i] = new JLabel("" + KEYLETTERS[i+19]);
      keyboardR3[i].setFont(s);
      keyboardR3[i].setOpaque(true);
      keyboardR3[i].setBackground(Color.WHITE);
      keyboardR3[i].setBorder(BorderFactory.createLineBorder(Color.black));
      keyboardR3[i].setHorizontalAlignment(SwingConstants.CENTER);
      keyboardPanel3.add(keyboardR3[i]);
    }
    this.add(keyboardPanel3);

    // end text setup
    endText = new JLabel("");
    endText.setBounds(0, 100, 600, 300);
    endText.setHorizontalAlignment(SwingConstants.CENTER);

    // end frame setup
    endFrame = new JFrame();
    endFrame.setLayout(null);
    endFrame.setSize(600, 600);
    endFrame.add(endText);
    endFrame.setUndecorated(true);
    endFrame.setVisible(false);
    
    // homeButton1 setup
    homeButton1 = new JButton(new ImageIcon("home.png"));
    homeButton1.setLocation(20, 30);
    homeButton1.setSize(75, 75);
    homeButton1.addActionListener(this);
    homeButton1.setFocusable(false);
    homeButton1.setOpaque(true);
    homeButton1.setBackground(Color.WHITE);
    homeButton1.setBorder(BorderFactory.createLineBorder(Color.BLACK, 5));
    this.add(homeButton1);

    // homeButton 2 setup
    homeButton2 = new JButton(new ImageIcon("home.png"));
    homeButton2.setLocation(20, 30);
    homeButton2.setSize(75, 75);
    homeButton2.addActionListener(this);
    homeButton2.setFocusable(false);
    homeButton2.setOpaque(true);
    homeButton2.setBackground(Color.WHITE);
    homeButton2.setBorder(BorderFactory.createLineBorder(Color.BLACK, 5));
    endFrame.add(homeButton2);

    // playAgain button setup
    playAgain = new JButton(new ImageIcon("playAgain.png"));
    playAgain.setBorder(BorderFactory.createLineBorder(Color.BLACK, 5));
    playAgain.setBounds(100, 350, 208, 75);
    playAgain.setFocusable(false);
    playAgain.addActionListener(this);
    endFrame.add(playAgain);

    // backButton setup
    backButton = new JButton(new ImageIcon("backButton.png"));
    backButton.setLocation(350, 350);
    backButton.setSize(150, 75);
    backButton.addActionListener(this);
    backButton.setFocusable(false);
    backButton.setOpaque(true);
    backButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
    endFrame.add(backButton);
    
    this.setUndecorated(true); // removes min max and close buttons
    addKeyListener(this); // listens to keys
    setVisible(true); 
  }

  
  private int firstOpen(){
    // checks for first open square in row
    for (int i = 0; i < 5; i++){
      if (gameGrid[guesses][i].getText() == ""){
        return i;
      }
    }
    // if no open squares returns 5
    return 5;
  }


  public void keyTyped(KeyEvent e){
    String temp = "";
    if (gameOver == false){
      if(firstOpen() != 5 && isLetter(e.getKeyChar())){ // checks if the user typed a letter
      gameGrid[guesses][firstOpen()].setText(("" + e.getKeyChar()).toUpperCase()); // sets first open Jlabels text to letter
      }
      if (e.getKeyChar() == '\u0008'){ // checks if user typed backspace
        if(firstOpen() != 0){
          gameGrid[guesses][firstOpen()-1].setText(""); // remove text from last typed letter
        }
      }
      if (e.getKeyChar() == '\n' && firstOpen() == 5){ // if user typed enter and row is full
        for(int i = 0; i < 5; i ++){
          temp += gameGrid[guesses][i].getText(); // temp word used to store all 5 letters
        }
        temp = temp.toLowerCase(); // makes temp all lower case letters
        
        if (bSearchValidWords(temp)){ // binary search valid words if the word exists
          updateColors(guess(temp)); // update Colors for guess
          guesses++; // guesses increases
          
        }
      }
    }
  }

  
  public void keyPressed(KeyEvent e){
  }

  
  public void keyReleased(KeyEvent e){
  }

  
  private boolean isLetter(char c) {
    // checks if character is a letter using ascii
    // https://stackoverflow.com/questions/4047808/what-is-the-best-way-to-tell-if-a-character-is-a-letter-or-number-in-java-withou
    if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')){
      return true;
    }
    return false;
  }

  
  private void initializeValidWords(){
    // read all lines in valid words and load into array
    try {
      FileReader fr = new FileReader("validWords.txt");
      BufferedReader br = new BufferedReader(fr);
      String temp = br.readLine();
      for (int i = 0; i < 12972; i++){
        validWords[i] = temp;
        temp = br.readLine();
      }
      br.close();
    }
    catch (Exception e){
    }
  }

  
  private boolean bSearchValidWords(String w){
    // binary search valid words to see if the word exists 
    int min = 0;
    int max = 12971;
    int mid;
    while (min <= max){
      mid = (min + max) / 2;
      if(w.compareTo(validWords[mid]) == 0){
        return true;
      }
      else if(w.compareTo(validWords[mid]) < 0){
        max = mid - 1; 
      }
      else {
        min = mid + 1;
      }
    }
    return false;
    
  }

  
  private void initAnswer(){
    // randomly chooses answer and intialises it into an array
    String temp;
    try {
      Random rand = new Random();
      int num = rand.nextInt(2309);
      FileReader fr = new FileReader("answers.txt");
      BufferedReader br = new BufferedReader(fr);
      temp = br.readLine();
      for (int i = 0; i < num; i++){
        temp = br.readLine();
      }
      br.close();
      for (int i = 0; i < 5; i++){
        answer[i] = temp.charAt(i);
      }
    }
    catch (Exception e){
    }
  }

  
  private char[] guess(String g){
    // takes guess and returns array of results
    char[] colors = {'-', '-', '-', '-', '-'};
    char[] tempWord = new char[5];
    char[] tempGuess = new char[5];
    for (int k = 0; k < 5; k++){
      // temp arrays since updating arrays
      tempWord[k] = answer[k]; 
      tempGuess[k] = g.charAt(k);
    }
    
    for(int i = 0; i < 5; i++){
      if (tempGuess[i] == tempWord[i]){ // if guess has same letter at same spot as answer
        updateGreen(tempGuess[i]); // update green in visual keyboard
        colors[i] = 'g'; 
        tempWord[i] = '!'; // removes letter from temp word so logic doesn't consider again
        
      }
    }
    
    for(int j = 0; j < 5; j++){
      for(int p = 0; p < 5; p++){
        if(tempGuess[j] == tempWord[p] && tempWord[p] != '!' && colors[j] == '-'){ // if letter exists in both guess and answer just at different positions
          updateYellow(tempGuess[j]); // update yellow in visual keyboard
          colors[j] = 'y'; 
          tempWord[p] = '!'; // removes letter from consideration
          tempGuess[j] = '@'; // removes letter from consieration
        }
      }
    }
    
    for(int i =0; i < 5; i++){
      if (colors[i] == '-'){ // if letter from guess does not exist answer
        updateGray(g.charAt(i)); // update visual keyboard gray
      }
    }
    
    setGameOver(colors); // sets game over if all answer and guess are same or user used all 6 guesses
    return colors;
  }

  
  private void updateColors(char[] c){
    // updates visual colors of a guess
    for(int i = 0; i < 5; i++){
      if (c[i] == 'g'){ // if green update board
        gameGrid[guesses][i].setBackground(Color.GREEN);
      }
      else if (c[i] == 'y'){ // if yellow update board
        gameGrid[guesses][i].setBackground(Color.YELLOW);
      }
      else{ // if gray update board
        gameGrid[guesses][i].setBackground(Color.GRAY);
      }
    }
  }

  
  private void updateGreen(char c){
    // update green on keyboard
    for (int j = 0; j < 26; j++){
      if (c == Character.toLowerCase(KEYLETTERS[j])){ // finds index of letter in keyboard
        if (j < 10){ // if first row
          if(keyboardR1[j].getBackground() != Color.GREEN){ // if not already green
            keyboardR1[j].setBackground(Color.GREEN);
          }
        }
        else if (j < 19){ // if second row
          if(keyboardR2[j-10].getBackground() != Color.GREEN){ // if not already green
            keyboardR2[j-10].setBackground(Color.GREEN);
          }
        }
        else{ // third row
          if(keyboardR3[j-19].getBackground() != Color.GREEN){ // if not already green
            keyboardR3[j-19].setBackground(Color.GREEN);
          }
        }
      } 
      
    }
  }

  
  private void updateYellow(char c){
    // update yellow on keyboard
    for (int j = 0; j < 26; j++){
      if (c == Character.toLowerCase(KEYLETTERS[j])){ // finds index of letter in keyboard
        if (j < 10){ // first row
          if(keyboardR1[j].getBackground() != Color.GREEN && keyboardR1[j].getBackground() != Color.YELLOW){ // if gray or white
            keyboardR1[j].setBackground(Color.YELLOW);
          }
        }
        else if (j < 19){ // seond row
          if(keyboardR2[j-10].getBackground() != Color.GREEN && keyboardR2[j-10].getBackground() != Color.YELLOW){ // if gray or white
            keyboardR2[j-10].setBackground(Color.YELLOW);
          }
        }
        else{ // third row
          if(keyboardR3[j-19].getBackground() != Color.GREEN && keyboardR3[j-19].getBackground() != Color.YELLOW){ // if gray or white
            keyboardR3[j-19].setBackground(Color.YELLOW);
          }
        }
      } 
      
    }
  }

  
  private void updateGray(char c){
    // update gray on keyboard
    for (int j = 0; j < 26; j++){
      if (c == Character.toLowerCase(KEYLETTERS[j])){ // finds index of letter
        if (j < 10){ // first row
          if(keyboardR1[j].getBackground() == Color.WHITE){ // if white
            keyboardR1[j].setBackground(Color.GRAY);
          }
        }
        else if (j < 19){ // second row
          if(keyboardR2[j-10].getBackground() == Color.WHITE){ // if white
            keyboardR2[j-10].setBackground(Color.GRAY);
          }
        }
        else{ // third row
          if(keyboardR3[j-19].getBackground() == Color.WHITE){ // if white
            keyboardR3[j-19].setBackground(Color.GRAY);
          }
        }
      } 
    }
  }

  
  private void setGameOver(char[] colors){
    // check if gameOver
    int counter = 0;
    int winCase = 0;

    // counts letters correct
    for (int i = 0; i < 5; i++){
      if (colors[i] == 'g'){
        counter++;
      }
    }
    
    gameOver =  ((counter == 5) || (guesses == 5));
    if (counter == 5){ // if all 5 letters correct
      winCase++;
    }
    if (gameOver){
      setupEndScreen(winCase); // 0 means lost, 1 means win
    }
  }

  
  private void setupEndScreen(int r){
    // show end screen
    endFrame.setVisible(true);
    endText.setFont(m);
    if (r == 1){
      endText.setText("<html>CONGRATULATIONS!<br/>YOU WIN!</html>");
    }
    else {
      endText.setText("<html>Better luck next time.<br/>The word was " + answerToString()+ "." + "</html>");
    }
    
    endFrame.toFront();
  }

  
  private String answerToString(){
    // returns answer array as a string
    String ans = "";
    for(int i =0; i < 5; i++){
      ans += answer[i];
    }
    return ans;
  }

  
  @Override
  public void actionPerformed(ActionEvent e) {
    if (e.getSource() == homeButton1 || e.getSource() == homeButton2){
      // go to hub screen and dispose of game
      this.dispose();
      endFrame.dispose();
      new homeScreen();
    }
    else if(e.getSource() == playAgain){
      // dispose of current game and create new instance
      endFrame.dispose();
      this.dispose();
      new Wordle();
    }
    else if (e.getSource() == backButton){
      // dispose of end screen
      endFrame.dispose();
    }
 }
}