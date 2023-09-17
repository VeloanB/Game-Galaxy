/**
 * This is a 2-player Tic Tac Toe game with a big twist. The grid is a 9 x 9 grid, but really is a 3 x 3 x 9 grid. The big twist is that the small square the user chooses corresponds to the big square where the opponent is forced to play. If the forced square has been won or drawn, the player can go anywhere. Green means you can go there, red means you cannot. Get three big squares in a row to win!
 *
 * @author Veloan Balendran
 * @date 19-Jan-2023
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.GridBagLayout;
import javax.swing.*;


public class UltTicTacToe extends JFrame implements ActionListener {
  
  private JPanel turnPanel, backPanel;
  private JPanel[] gamePanel;
  private JLabel turnLabel, endText;
  private JLabel[] bigSquares;
  private JButton[][] board;
  private JButton homeButton1, homeButton2, playAgain, backButton;
  private JFrame endFrame;
  private Font f = new Font("SansSerif", Font.BOLD, 10);
  private Font b = new Font("SansSerif", Font.BOLD, 100);
  private Font m = new Font("SansSerif", Font.BOLD, 20);
  private String turn;
  private int validSquare;
  boolean playing;

  
  public UltTicTacToe() {
    setSize(600, 600);
    this.setLayout(null);

    turn = "X";
    validSquare = -1;
    playing = true;

    // panel to hold turn 
    turnPanel = new JPanel();
    turnPanel.setBounds(250, 30, 100, 50);
    // setup turnLabel
    turnLabel = new JLabel(turn + "'s turn");
    turnLabel.setFont(m);
    turnPanel.add(turnLabel);
    this.add(turnPanel); 

    // panel to hold all 9 subpanels
    backPanel = new JPanel();
    GridLayout layout = new GridLayout(3,3);
    backPanel.setBounds(100, 100, 400, 400);
    layout.setHgap(10);
    layout.setVgap(10);
    backPanel.setLayout(layout);
    this.add(backPanel);

    // setup gamePanel
    gamePanel = new JPanel[9];
    for (int i = 0; i<9; i++){
      gamePanel[i] = new JPanel();
      gamePanel[i].setLayout((new GridLayout(3,3)));
    }

    // setup all game buttons on board
    board = new JButton[9][9]; //array of 81 buttons
    for (int i = 0; i < 9; i++) { //loop through all 81 buttons
      for(int j = 0; j < 9; j++){
        board[i][j] = new JButton(""); //make a new button with empty string
        board[i][j].addActionListener(this); //listen for button to be pressed
        board[i][j].setFont(f);
        board[i][j].setBackground(Color.GREEN); 
        gamePanel[i].add(board[i][j]); //add button to the board
      }
      backPanel.add(gamePanel[i]);
    }

    // setup bigSquares
    bigSquares = new JLabel[9];
    for (int i=0;i<9;i++){
      bigSquares[i] = new JLabel("");
      bigSquares[i].setFont(b);
    }

    // setup endText
    endText = new JLabel("");
    endText.setBounds(0, 100, 600, 300);
    endText.setHorizontalAlignment(SwingConstants.CENTER);
    endFrame = new JFrame();
    endFrame.setLayout(null);
    endFrame.setSize(600, 600);
    endFrame.add(endText);
    endText.setFont(m);
    endFrame.setUndecorated(true);
    endFrame.setVisible(false);

    // setup homeButton 1
    homeButton1 = new JButton(new ImageIcon("home.png"));
    homeButton1.setLocation(20, 30);
    homeButton1.setSize(75, 75);
    homeButton1.addActionListener(this);
    homeButton1.setFocusable(false);
    homeButton1.setOpaque(true);
    homeButton1.setBackground(Color.WHITE);
    homeButton1.setBorder(BorderFactory.createLineBorder(Color.BLACK, 5));
    this.add(homeButton1);

    // setup homeButton 2
    homeButton2 = new JButton(new ImageIcon("home.png"));
    homeButton2.setLocation(20, 30);
    homeButton2.setSize(75, 75);
    homeButton2.addActionListener(this);
    homeButton2.setFocusable(false);
    homeButton2.setOpaque(true);
    homeButton2.setBackground(Color.WHITE);
    homeButton2.setBorder(BorderFactory.createLineBorder(Color.BLACK, 5));
    endFrame.add(homeButton2);

    // setup playAgain button
    playAgain = new JButton(new ImageIcon("playAgain.png"));
    playAgain.setBorder(BorderFactory.createLineBorder(Color.BLACK, 5));
    playAgain.setBounds(100, 350, 208, 75);
    playAgain.setFocusable(false);
    playAgain.addActionListener(this);
    endFrame.add(playAgain);

    // setup backButton
    backButton = new JButton(new ImageIcon("backButton.png"));
    backButton.setLocation(350, 350);
    backButton.setSize(150, 75);
    backButton.addActionListener(this);
    backButton.setFocusable(false);
    backButton.setOpaque(true);
    backButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
    endFrame.add(backButton);    
    
    this.setUndecorated(true); // remove min max and close buttons
    setVisible(true); //make the window visible
  }

  
  @Override
  public void actionPerformed(ActionEvent e) {
    if(e.getSource() == homeButton1 || e.getSource() == homeButton2){
      // dispose of game and go back to home
      this.dispose();
      endFrame.dispose();
      new homeScreen();
    }
    else if(e.getSource() == playAgain){
      // dispose of this game and create new game instance
      this.dispose();
      endFrame.dispose();
      new UltTicTacToe();
    }
    else if (e.getSource() == backButton){
      // dispose of end screen
      endFrame.dispose();
    }
    else{
      // iterate through board
      for (int i = 0; i < 9; i++) {
        for (int j = 0; j<9; j++){
          if (e.getSource() == board[i][j] && board[i][j].getText().equals("") && isValidMove(i) && checkSquareWin(i) == "no win" && playing) { 
            board[i][j].setText(turn); // update square text to turn
            updateSquare(i); 
            if(checkGameWin()){ // if game win
              playing = false;
            }
            else if(checkGameTie()){ // if game tie
              playing = false;
            }
            setTurn(); // switch turn
            turnLabel.setText(turn + "'s turn"); // change text to turn
            setValidSquare(j); // update the valid square
            showValidSquare(); // show the visual valid squares
          }
        }
      }
    }
  }

  
  private void setTurn(){
    // flip flops from x to o and o to x
    if (turn == "X"){
      turn = "O";
    }
    else {
      turn = "X";
    }
  }
  

  private boolean isValidMove(int square){
    // if player clicks in a valid square
    if (validSquare == -1){
      return true;
    }
    else{
      if(square == validSquare){
        return true;
      }
      return false;
    }
  }

  
  private void setValidSquare(int pos){
    // updates valid squares
    if(checkSquareWin(pos) != "no win" || isSquareFull(pos)){ // if square has been won or drawn
      validSquare = -1;
    }
    else{
      validSquare = pos;
    }
  }

  
  private void showValidSquare(){
    // updates valid square visual with colours
    Color r = Color.RED;
    // if valid square is -1, everything is green
    if (validSquare == -1){
      r = Color.GREEN;
    }
    
    // iterate through board
    for (int i = 0; i < 9; i++){
      for (int j = 0; j<9; j++){
        if(i == validSquare){ // if i is the valid big square
          board[i][j].setBackground(Color.GREEN); // everything in the square is green
        }
        else{ // if i is not the valid square
          board[i][j].setBackground(r); // everything in the square is red
        }
      }
    }  
  }

  
  private String checkSquareWin(int square){
    for(int i = 0; i<3; i++){
      // horizontal wins
      if (board[square][i*3].getText() == board[square][i*3+1].getText() && board[square][i*3].getText() == board[square][i*3+2].getText() && board[square][i*3].getText() != ""){
        return turn;
      }
      // vertical wins
      if (board[square][i].getText() == board[square][i+3].getText() && board[square][i+6].getText() == board[square][i+3].getText() && board[square][i].getText() != ""){
        return turn;
      }
    }
    
    // diag top left to bot right
    if(board[square][0].getText() == board[square][4].getText() && board[square][0].getText() == board[square][8].getText() && board[square][4].getText() != ""){
      return turn;
    }
    
    // diag top right to bot left
    if(board[square][2].getText() == board[square][4].getText() && board[square][2].getText() == board[square][6].getText() && board[square][4].getText() != ""){
      return turn;
    }
    return "no win";  
  }

  
  private boolean isSquareFull(int square){
    // checks if small square is full
    int counter = 0;
    for(int i = 0; i < 9; i++){
      if (board[square][i].getText() != ""){
        counter++;
      }
    }
    if (counter == 9){
      return true;
    }
    else {
      return false;
    }
  }

  
  private void updateSquare(int square){
    // removes buttons from big square and updates as the owner of square or draw
    if(checkSquareWin(square) != "no win" || isSquareFull(square)){
      gamePanel[square].removeAll();
      gamePanel[square].revalidate();
      gamePanel[square].repaint();
      gamePanel[square].setLayout(new GridBagLayout());
      gamePanel[square].add(bigSquares[square]);
      bigSquares[square].setText(turn);
      if(checkSquareWin(square) == "no win"){
        bigSquares[square].setText("-");
      }
    }
  }

  
  private boolean checkGameWin(){
    for(int i = 0; i < 3; i++){
      // check big horizontal game wins
      if(bigSquares[i*3].getText() == bigSquares[i*3+1].getText() && bigSquares[i*3].getText() == bigSquares[i*3+2].getText() && bigSquares[i*3].getText() != "" && bigSquares[i*3].getText() != "-"){
        setupEndScreen(1);
        return true;
      }

      // check big vertial game win
      if(bigSquares[i].getText() == bigSquares[i+3].getText() && bigSquares[i].getText() == bigSquares[i+6].getText() && bigSquares[i].getText() != "" && bigSquares[i].getText() != "-"){
      setupEndScreen(1);
        return true;
      }
    }

    // check big diag game win from top left to bot right
    if(bigSquares[0].getText() == bigSquares[4].getText() && bigSquares[0].getText() == bigSquares[8].getText() && bigSquares[4].getText() != "" && bigSquares[0].getText() != "-"){
      setupEndScreen(1);
      return true;
    }

    // check big diag game win from top right to bot left
    if(bigSquares[2].getText() == bigSquares[4].getText() && bigSquares[2].getText() == bigSquares[6].getText() && bigSquares[4].getText() != "" && bigSquares[2].getText() != "-"){
      setupEndScreen(1);
      return true;
    }
    return false;  
  }

  
  private boolean checkGameTie(){
    // check if the game is a tie
    for(int i = 0; i < 9; i++){
      if (bigSquares[i].getText() == ""){
        return false;
      }
    }
    setupEndScreen(2);
    return true;
  }

  
  private void setupEndScreen(int winCon){
    // show end screen
    endFrame.setVisible(true);
    endFrame.toFront();
    if(winCon == 1){ // winner
      endText.setText(turn + " wins! Press back to see the final board.");
    }
    else{ // draw
      endText.setText("The game was a draw. Press back to see the final board.");
    }
  }
} // end class