// Useful link used to perceive battleship algorithms: https://www.datagenetics.com/blog/december32011/ 

/**
 * This is a battleship player versus computer game with 5 difficulties which are: Beginner, Intermediate, Advanced, Expert and Impossible.
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
import javax.swing.BorderFactory;

public class Battleship extends JFrame implements ActionListener{ 

  private JLabel title, computerShotInfo, playerShotInfo, endText;
  private JButton[][] computerGrid;
  private JLabel[][] playerGrid;
  private JLabel[] cDesVisual, cCruiseVisual, cSubVisual, cBattleVisual, cCarrierVisual;
  private JPanel computerPanel, playerPanel, cDes, cCruise, cSub, cBattle, cCarrier;
  private int[][] playerShips, compShips, playerShots, compShots, possibilityMap, generationMap;
  private JFrame endFrame;
  private JButton homeButton1, homeButton2, playAgain, backButton;

  private String difficulty;
  // colours
  private Color blue = new Color(0, 150, 255);

  private boolean pAlive[] = {true, true, true, true, true};  // player Ships alive

  // fonts
  private Font t = new Font("SansSerif", Font.BOLD, 50);
  private Font s = new Font("SansSerif", Font.BOLD, 10);

  private int counter; // count ships in generations and set id
  
  private Random rand; // random class
  
  private int tempCord[]; // computer shot temp cord

  private int shots; // shots taken counter

  // ships alive for player and computer
  private int pShipsAlive;
  private int cShipsAlive;

  private int smallestShipSize = 2; // used for computer
  
  public Battleship(String dif){

    setSize(600, 600);
    this.setLayout(null);
    
    rand = new Random(); // initialize rand
    tempCord = new int[2]; // initialize tempCord
    counter = 1; // setup counter
    shots = 0; // setup initial shots

    // setup ships alive for player and computer
    pShipsAlive = 5;
    cShipsAlive = 5;

    difficulty = dif; // set difficulty to string passed into constructor

    // setup title
    title = new JLabel("Battleship");
    title.setFont(t);
    title.setSize(600, 75);
    title.setHorizontalAlignment(JLabel.CENTER);
    this.add(title);

    // setup playerShotInfo
    playerShotInfo = new JLabel("<html>Click the buttons to <br/>the left to fire!</html>");
    playerShotInfo.setSize(225, 100);
    playerShotInfo.setLocation(325, 100);
    this.add(playerShotInfo);

    // setup computerShotInfo
    computerShotInfo = new JLabel("Computer Shot Info");
    computerShotInfo.setSize(225, 100);
    computerShotInfo.setLocation(325, 325);
    this.add(computerShotInfo);

    // initialize visual of sunk ships 
    cDesVisual = new JLabel[2];
    cCruiseVisual = new JLabel[3];
    cSubVisual = new JLabel[3];
    cBattleVisual = new JLabel[4];
    cCarrierVisual = new JLabel[5];

    // setup computer destroyer
    cDes = new JPanel();
    cDes.setBounds(320, 200, 20, 40);
    cDes.setLayout(new GridLayout(2, 1));
    this.add(cDes);

    // setup computer cruiser
    cCruise = new JPanel();
    cCruise.setBounds(350, 200, 20, 60);
    cCruise.setLayout(new GridLayout(3, 1));
    this.add(cCruise);

    // setup computer submarine
    cSub = new JPanel();
    cSub.setBounds(380, 200, 20, 60);
    cSub.setLayout(new GridLayout(3, 1));
    this.add(cSub);

    // setup computer battleship
    cBattle = new JPanel();
    cBattle.setBounds(410, 200, 20, 80);
    cBattle.setLayout(new GridLayout(4, 1));
    this.add(cBattle);

    // setup computer carrier
    cCarrier = new JPanel();
    cCarrier.setBounds(440, 200, 20, 100);
    cCarrier.setLayout(new GridLayout(5, 1));
    this.add(cCarrier);


    for(int i=0; i < 17; i++){
      // iterate through 17 squares
      if (i < 2){ // setup destoryer squares
        cDesVisual[i] = new JLabel();
        cDes.add(cDesVisual[i]);
        cDes.setBackground(Color.GRAY);
        cDesVisual[i].setBorder(BorderFactory.createLineBorder(Color.BLACK));
      }
      else if (i < 5){ // setup cruiser squares
        cCruiseVisual[i-2] = new JLabel();
        cCruise.add(cCruiseVisual[i-2]);
        cCruise.setBackground(Color.GRAY);
        cCruiseVisual[i-2].setBorder(BorderFactory.createLineBorder(Color.BLACK));
      }
      else if (i < 8){ // setup submarine squares
        cSubVisual[i-5] = new JLabel();
        cSub.add(cSubVisual[i-5]);
        cSub.setBackground(Color.GRAY);
        cSubVisual[i-5].setBorder(BorderFactory.createLineBorder(Color.BLACK));
      }
      else if (i < 12){ // setup battleship squares
        cBattleVisual[i-8] = new JLabel();
        cBattle.add(cBattleVisual[i-8]);
        cBattle.setBackground(Color.GRAY);
        cBattleVisual[i-8].setBorder(BorderFactory.createLineBorder(Color.BLACK));
      }
      else { //setup carrier squares
        cCarrierVisual[i-12] = new JLabel();
        cCarrier.add(cCarrierVisual[i-12]);
        cCarrier.setBackground(Color.GRAY);
        cCarrierVisual[i-12].setBorder(BorderFactory.createLineBorder(Color.BLACK));
      }
    }

    GridLayout layout = new GridLayout(10,10); // 10 x 10 layout

    //setup playerPanel and initialize player grid
    playerGrid = new JLabel[10][10];
    playerPanel = new JPanel();
    playerPanel.setBounds(100, 325, 200, 200);
    playerPanel.setLayout(layout);

    //setup computerPanel and initialize computer grid
    computerGrid = new JButton[10][10];
    computerPanel = new JPanel();
    computerPanel.setBounds(100, 100, 200, 200);
    computerPanel.setLayout(layout);

    // initailize internal game grids
    playerShips = new int[10][10]; // locations of player ships
    compShips = new int[10][10]; // locations of comp ships
    playerShots = new int[10][10]; // locations of previous player shots
    compShots = new int[10][10]; // locations of previous computer shots
    possibilityMap = new int[10][10]; // line and surrounding logic grid
    generationMap = new int[10][10]; 
    
    for(int i =  0; i < 10; i++){
      for(int j = 0; j < 10; j++){
        // setup each square in playerGrid
        playerGrid[i][j] = new JLabel("");
        playerGrid[i][j].setBorder(BorderFactory.createLineBorder(Color.black));
        playerGrid[i][j].setOpaque(true);
        playerGrid[i][j].setBackground(blue);
        playerGrid[i][j].setFont(s);
        playerGrid[i][j].setHorizontalAlignment(JLabel.CENTER);
        playerPanel.add(playerGrid[i][j]);

        // setup each square in computer grid
        computerGrid[i][j] = new JButton("");
        computerGrid[i][j].setBackground(blue);
        computerGrid[i][j].addActionListener(this);
        computerGrid[i][j].setBorder(BorderFactory.createLineBorder(Color.black));
        computerPanel.add(computerGrid[i][j]);

        // initialize all internal grids to 0
        playerShips[i][j] = 0;
        compShips[i][j] = 0;
        playerShips[i][j] = 0;
        compShips[i][j] = 0;
        generationMap[i][j] = 0; // the greater the value, the more ships can generate
        possibilityMap[i][j] = 0; // 0 means unknown, -10 means miss, 10 means hit, 5 means found in this direction, 1 means possible, 100 means sunk
      }
    }

    if (difficulty.equals("Expert")){ 
      updateGenerationMap(); // setup initial values to generation map
    }

    // add grids to frame
    this.add(computerPanel);
    this.add(playerPanel);

    // generate all 5 player ships
    generateValidShip(5, playerShips);
    generateValidShip(4, playerShips);
    generateValidShip(3, playerShips);
    generateValidShip(3, playerShips);
    generateValidShip(2, playerShips);

    // generate all 5 computer ships
    generateValidShip(5, compShips);
    generateValidShip(4, compShips);
    generateValidShip(3, compShips);
    generateValidShip(3, compShips);
    generateValidShip(2, compShips);
    
    // setup endText
    endText = new JLabel("");
    endText.setBounds(0, 100, 600, 300);
    endText.setHorizontalAlignment(SwingConstants.CENTER);

    // setup endFrame
    endFrame = new JFrame();
    endFrame.setLayout(null);
    endFrame.setSize(600, 600);
    endFrame.add(endText);
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
    
    // setup  playAgain
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

    this.setUndecorated(true);
    setVisible(true); 

  }

  
  // GAME RUNNING FUNCTIONS

  @Override
  public void actionPerformed(ActionEvent e) {
    if(e.getSource() == homeButton1 || e.getSource() == homeButton2){
      // get rid of jframe and go back to main screen
      this.dispose();
      endFrame.dispose();
      new homeScreen();
    }
    else if(e.getSource() == playAgain){
      // make new instance of this battleship home screen
      this.dispose();
      endFrame.dispose();
      new BattleshipHome();
    }
    else if (e.getSource() == backButton){
      // get rid of end frame
      endFrame.dispose();
    }
    else{
      if (shots < 100 && pShipsAlive != 0 && cShipsAlive != 0){
        for(int y = 0; y < 10; y++){
          for(int x = 0; x < 10; x++){
            if (e.getSource() == computerGrid[y][x] && playerShots[y][x] == 0){ // if position hasn't been shot at yet
              takeShot(x, y, compShips, playerShots); // player take shots at position (x,y)
              tempCord = getComputerShotCords(); // computer generates its cordinates (based on difficulty)
              shots++; // increase shots number
              takeShot(tempCord[0], tempCord[1], playerShips, compShots); // computer takes shot at generated position
            }
          }
        }
      }
    }
  }

  
  private void generateValidShip(int size, int board[][]){
    // creates a valid ship generation where a ship stays in the board and doesn't cross other ships
    boolean placed = false;
    while (placed == false){
      int direc = rand.nextInt(2); // 0 is horizontal 1 is vertical
      int x = rand.nextInt(10); // random x
      int y = rand.nextInt(10); // random y
      if (direc == 0 && x + size <= 10){ // if ship fits 
        if (spaceAvailible(x, y, size, direc, board)){ // if all spots where ship will generate clear
          for(int i = 0; i < size; i++){
            board[y][x+i] = counter;  
          }
          placed = true;
          counter++;
        }
      }
      else if (direc == 1 && y + size <= 10){ // if ship fits
        if (spaceAvailible(x, y, size, direc, board)){ // if all spots where ship will generate clear
          for(int i = 0; i < size; i++){
            placed = true;
            board[y+i][x] = counter;
            
          }
          counter++;
        }

      }
    }
    // show the players ships in gray on their grid
    if (board == playerShips){
      for(int i = 0; i < 10; i++){
        for(int j = 0; j < 10; j++){
          if (playerShips[i][j] != 0){
            playerGrid[i][j].setBackground(Color.GRAY); 
          }
        }
      }
    }
    
  }

  
  private boolean spaceAvailible(int x, int y, int size, int direc, int board[][]){
    // checks if there is space for a specific ship to fit
    for(int i = 0; i < size; i++){
      if (direc == 0){ // horizontal
        if (board[y][x+i] != 0){ // if square is not empty
          return false;
        }
      }
      else { // vertical
        if (board[y+i][x] != 0){ // if square is not empty
          return false;
        }
      }
    }
    return true;
  }

  
  private void takeShot(int x, int y, int gameBoard[][], int shotBoard[][]){
    // takes a shot 
    if(shotBoard[y][x] == 0){
      shotBoard[y][x] = 1;
      if (gameBoard == compShips){
        compShips[y][x] *= -1; // makes square negative if != 0 meaning will update hits
        computerGrid[y][x].setText("X"); // visual hit marker
        if (compShips[y][x] != 0){ // hit
          playerShotInfo.setForeground(Color.RED); // red text for shot info
          computerGrid[y][x].setForeground(Color.RED); // red visual 'X'
          updateShotInfo(x, y, gameBoard, sankShip(compShips[y][x]), true); // updates the player shot info
        }
        else{ // miss
          playerShotInfo.setForeground(Color.BLACK); // black text for shot info
          computerGrid[y][x].setForeground(Color.WHITE); // white visual 'X'
          updateShotInfo(x, y, gameBoard, -1, false); // updates player shot info
        }
        
      }
      else {
        playerShips[y][x] *= -1; // makes square negative if != 0 meaning will update hits
        playerGrid[y][x].setText("X"); // visual hit marker
        if(difficulty == "Expert"){
            updateGenerationMap();
        }
        if (playerShips[y][x] != 0){ // hit
          computerShotInfo.setForeground(Color.RED); // red text for shot info
          playerGrid[y][x].setForeground(Color.RED); // red visual 'X'
          possibilityMap[y][x] = 10; // updates possibility map as a hit
          if(difficulty == "Intermediate"){
            updateSurroundingHitSimple(x, y); // updates with no line logic
          }
          else if (difficulty == "Advanced" || difficulty == "Expert"){
            updateSurroundingHitAdv(x, y); // updates with line logic
          }
          updateShotInfo(x, y, gameBoard, sankShip(playerShips[y][x]), true); // update computer shot info
        }
        else{
          computerShotInfo.setForeground(Color.BLACK); // black text for shot info
          playerGrid[y][x].setForeground(Color.WHITE); // white visual 'X'
          updateShotInfo(x, y, gameBoard, -1, false); // update computer shot info
          possibilityMap[y][x] = -10; // updates possibility map as a miss
        }
      }
    }
  }

  
  private int sankShip(int shipId){
    // checks if a ship has been sunk and returns the id if it has been sunk
    shipId = shipId * -1; // shipId is passes as negative this reverses it
    if (shipId < 6){ // is a player ship
      // iterate through 10 x 10
      for(int x = 0; x < 10; x++){
        for(int y = 0; y < 10; y++){
          if (playerShips[y][x] == shipId){ // if part of the ship is positive 
            return -1; // means no sink
          }
        }
      }
      pShipsAlive -= 1;
      pAlive[shipId - 1] = false; // updates computer 
      updateSmallestShip(); // updates computer logic
      updateSinkPossibility(shipId); // updates possibilities around a sunk ship
    }
    else { // computer ships
      for(int x = 0; x < 10; x++){
        for(int y = 0; y < 10; y++){
          if (compShips[y][x] == shipId){ // if part of ship not hit
            return -1; // no sink
          }
        }
      }
      sinkShipVisual(shipId); // update player visual for sunk ship
      cShipsAlive -= 1;
    }
    isGameOver(); // checks if game over
    return shipId; // returns id of sunk ship
  }

  
  private void updateShotInfo(int x, int y, int board[][], int sink, boolean didHit){
    // updates shot info based on hit, miss and sinking
    String sinkTemp = ""; 
    String hitTemp = "miss";
    if(didHit){
      hitTemp = "hit";
    }
    if (board == playerShips){ // computer shot info
      if(sink != -1){ // if sunk ship
        sinkTemp = " and sank your " + shipIdToName(sink);
      }
      computerShotInfo.setText("<html>The computer fired at (" + (x+1) + ", " + (y+1) + ").<br/> The shot was a " + hitTemp + "<br/>" + sinkTemp + "<html>"); // set text for shot info
    }
    else{ // player shot info
      if(sink != -1){
        sinkTemp = " and you sank their " + shipIdToName(sink);
      }
      playerShotInfo.setText("<html>You fired at (" + (x+1) + ", " + (y+1) + ").<br/> The shot was a " + hitTemp + "<br/>" + sinkTemp + "<html>"); // set text for shot info
    }
    
  }

  
  private String shipIdToName(int shipId){
    // converts ship id to ship name
    String tempName = "Invalid";
    if(shipId > 5){ // subtract 5 from id if it is a computer ship
      shipId -= 5;
    }
    switch(shipId){
      case 5 -> tempName = "Destroyer";
      case 4 -> tempName = "Submarine";
      case 3 -> tempName = "Cruiser";
      case 2 -> tempName = "Battleship";
      case 1 -> tempName = "Carrier";
    }
    return tempName;
    
  }

  
  private void sinkShipVisual(int shipId){
    // shows visual when a ship has been sunk
    int length = 0;
    JLabel[] tempBoard;
    
    if(shipId == 10){ // destroyer
      length = 2;
      tempBoard = new JLabel[length];
      tempBoard = cDesVisual;
    }
    else if(shipId == 9){ // cruiser
      length = 3;
      tempBoard = new JLabel[length];
      tempBoard = cCruiseVisual;
    }
    else if (shipId == 8){ // submarine
      length = 3;
      tempBoard = new JLabel[length];
      tempBoard = cSubVisual;
    }
    else if (shipId == 7){ // battleship
      length = 4;
      tempBoard = new JLabel[length];
      tempBoard = cBattleVisual;
    }
    else { // carrier
      length = 5;
      tempBoard = new JLabel[length];
      tempBoard = cCarrierVisual;
    }

    for (int i = 0; i < length; i++){
      tempBoard[i].setOpaque(true);
      tempBoard[i].setBackground(Color.RED); // make the visual for the ship red
    }
      
  }

  
  private void isGameOver(){
    // checks if game is over
    if(cShipsAlive == 0){
      setupEndScreen(1); // player wins
    }
    else if (pShipsAlive == 0){
      showCompShips();
      setupEndScreen(2); // comp wins
    }
    
  }

  
  private void showCompShips(){
    // shows computer ships locations
    for(int x = 0; x < 10; x++){
      for(int y = 0; y< 10; y++){
        if(compShips[y][x] > 0){ // if has not been hit
          computerGrid[y][x].setBackground(Color.YELLOW); // set the colour of computer ship locations to yellow
        }
      }
    }
  }

  
  private void setupEndScreen(int winner){
    if (winner == 1){ // player wins
      endText.setText("<html>CONGRATULATIONS!<br/>You beat the " + difficulty + " mode in " + shots + " shots! </html>");
      
    }
    else { // computer wins
      endText.setText("<html>Better luck next time.<br/>You lost to the " + difficulty + " mode in " + shots + " shots!<br/>Press back to see the computer ships.</html>");
    }
    endFrame.setVisible(true);
    endFrame.toFront();
  }

  
  // COMPUTER INTELLIGENCE FUNCTIONS

  private int[] generateShotCords(){
    // generate shot cords based on difficulty
    int ans[] = new int[2];
    boolean isValid = false;
    int x;
    int y;
    while(!isValid){
      x = rand.nextInt(10);
      y = rand.nextInt(10);
      if (compShots[y][x] == 0){
        if(difficulty == "Advanced"){ // if advanced get random in checkerboard pattern
          if ((x + y) % 2 == 1){
            ans[0] = x;
            ans[1] = y;
            isValid = true;
          }
        }
        else { // if easy or intermediate random shot
          ans[0] = x;
          ans[1] = y;
          isValid = true;
        }
      }
    }
    return ans;
  }
  

  private void updateSurroundingHitSimple(int x, int y){
    // updates possibility map all around a hit to 1 if currently 0
    if(y > 0){
      if(possibilityMap[y-1][x] == 0){
        possibilityMap[y-1][x] = 1;
      }
    }
    if (y<9){
      if(possibilityMap[y+1][x] == 0){
        possibilityMap[y+1][x] = 1;
      }
    }
    if(x > 0){
      if(possibilityMap[y][x-1] == 0){
        possibilityMap[y][x-1] = 1;
      }
    }
    if (x<9){
      if(possibilityMap[y][x+1] == 0){
        possibilityMap[y][x+1] = 1;
      }
    }
  }
  

  private void updateSurroundingHitAdv(int x, int y){
    // updates possibility map all around a hit to 1 if currently 0 and finds lines and updates to 5 if sees a line
    if(y > 0){
      if(possibilityMap[y-1][x] == 0){ // updates square to above as possibility of 1
        possibilityMap[y-1][x] = 1;
      }
      else if (possibilityMap[y-1][x] == 10){ // if the above square is a hit
        // if space above, update square above 2nd hit on possibility map as 5
        if(y > 1){
          if (possibilityMap[y-2][x] == 0 || possibilityMap[y-2][x] == 1){ 
            possibilityMap[y-2][x] = 5;
          }
        }

        // if space below update square below original hit on possibility map as 5
        if(y < 9){
          if (possibilityMap[y+1][x] == 0 || possibilityMap[y+1][x] == 1){
            possibilityMap[y+1][x] = 5;
          }
        }
      }
      
    }
    
    if (y<9){
      if(possibilityMap[y+1][x] == 0){ // update below as possibility of 1
        possibilityMap[y+1][x] = 1;
      }
      else if (possibilityMap[y+1][x] == 10){ // if below square is a hit

        // if space above, update square above as 5 on possibility map
        if(y > 0){
          if (possibilityMap[y-1][x] == 0 || possibilityMap[y-1][x] == 1){
            possibilityMap[y-1][x] = 5;
          }
        }

        // if space below 2nd hit, update to 5 on possibility map
        if(y < 8){
          if (possibilityMap[y+2][x] == 0 || possibilityMap[y+2][x] == 1){
            possibilityMap[y+2][x] = 5;
          }
        }
      }
      
    }
    if(x > 0){
      if(possibilityMap[y][x-1] == 0){ // update left as possibility of 1
        possibilityMap[y][x-1] = 1;
      }
      else if (possibilityMap[y][x-1] == 10){ // if to the left is a hit

        // check if there is space to the leftmost hit then update possibility map to 5
        if(x > 1){
          if (possibilityMap[y][x-2] == 0 || possibilityMap[y][x-2] == 1){
            possibilityMap[y][x-2] = 5;
          }
        }

        // check if ther is space to the rightmost hit then update possibility map to 5
        if(x < 9){
          if (possibilityMap[y][x+1] == 0 || possibilityMap[y][x+1] == 1){
            possibilityMap[y][x+1] = 5;
          }
        }
      }
    }
    if (x<9){
      if(possibilityMap[y][x+1] == 0){ // update right as possibility of 1
        possibilityMap[y][x+1] = 1;
      }
      else if (possibilityMap[y][x+1] == 10){ // if square to the right is a hit
        // check if there is space to the orignial hit, then update possibility map to 5
        if(x > 0){
          if (possibilityMap[y][x-1] == 0 || possibilityMap[y][x-1] == 1){
            possibilityMap[y][x-1] = 5;
          }
        }

        // check if there is space to the right of the rightmost hit then update possibility map to 5 
        if(x < 8){
          if (possibilityMap[y][x+2] == 0 || possibilityMap[y][x+2] == 1){
            possibilityMap[y][x+2] = 5;
          }
        }
      }
    }
    
  }

  
  private void updateSinkPossibility(int shipId){
    shipId *= -1; // shipid is passed as negative converts to positive
    for(int x = 0; x < 10; x++){
      for(int y = 0; y < 10; y++){
        if(playerShips[y][x] == shipId){
          possibilityMap[y][x] = 100; // 100 means sunk
        }
      }
    }
    updateSinkSurrounding();
    
  }

  
  private void updateSinkSurrounding(){
    // completely clears posible and likely squares on possibility map
    for(int x = 0; x < 10; x++){
      for(int y = 0; y < 10; y++){
        if (possibilityMap[y][x] == 1 || possibilityMap[y][x] == 5){
          possibilityMap[y][x] = 0;
        }
      }
    }
    
    for(int x = 0; x < 10; x++){
      for(int y = 0; y < 10; y++){
        if (possibilityMap[y][x] == 10){ // if hits not affected by sinking of shit update surrounding
          if(difficulty == "Intermediate"){
            updateSurroundingHitSimple(x, y); // no line logic
          }
          else {
            updateSurroundingHitAdv(x, y); // line logic
          }
        }
      }
    }
  }

  
  private boolean isLikely(){
    // checks if there is a likely square in a line -- 5 on possibility map
    for(int x = 0; x<10; x++){
      for(int y = 0; y < 10; y++){
        if(possibilityMap[y][x] == 5){
          return true;
        }
      }
    }
    return false;
  }

  
  private boolean isPossible(){
    // checks if there is a possible square -- 1 on possibility map
    for(int x = 0; x < 10; x++){
      for(int y = 0; y < 10; y++){
        if(possibilityMap[y][x] == 1){
          return true;
        }
      }
    }
    return false;
  }

  
  private int[] generatePossible(){
    // finds random coordinate with value of 1 on possibility map
    int[] cords = new int[2];
    boolean valid = false;
    int x;
    int y;
    while(!valid){
      x = rand.nextInt(10);
      y = rand.nextInt(10);
      if(possibilityMap[y][x] == 1){
        cords[0] = x;
        cords[1] = y;
        valid = true;
      }
    }
    
    return cords;
  }

  
  private int[] generateLikely(){
    // returns random coordinate with value of 5 in possibility map
    int[] cords = new int[2];
    boolean valid = false;
    int x;
    int y;
    while(!valid){
      x = rand.nextInt(10);
      y = rand.nextInt(10);
      if(possibilityMap[y][x] == 5){
        cords[0] = x;
        cords[1] = y;
        valid = true;
      }
    }
    
    return cords;
  }


  private void updateSmallestShip(){
    // updates computers internal smallest ship
    for(int i = 4; i >= 0; i--){
      if(pAlive[i]) {
        if(i == 4){
          smallestShipSize = 2;
        }
        else if (i == 3 || i == 2){
          smallestShipSize = 3;
        }
        else if(i == 1){
          smallestShipSize = 4;
        }
        else{
          smallestShipSize = 5;
        }
        break;
      }
    }
    
    
  }

  
  private void updateGenerationMap(){
    // completely clear generation map
    for(int x = 0; x < 10; x++){
      for (int y = 0; y < 10; y++){
        generationMap[y][x] = 0;
      }
    }

    for(int x = 0; x < 10; x++){
      for(int y = 0; y < 10; y++){

        // checks all horizontal ways smallest ship can generate then adds 1 to all squares ship passes through
        if(x + smallestShipSize <= 10){
          if(spaceAvailible(x, y, smallestShipSize, 0, compShots)){
            for(int i = 0; i < smallestShipSize; i++){
              generationMap[y][x+i] += 1;
            }
          }
        }

        // checks all vertical ways smallest ship can generate then adds 1 to all squares ship passes through
        if (y + smallestShipSize <= 10){
          if(spaceAvailible(x, y, smallestShipSize, 1, compShots)){
            for(int i = 0; i < smallestShipSize; i++){
              generationMap[y+i][x] += 1;
            }
          }
        }  
      }
    }
  }

  
  private int findGenMapMax(){
    // find the highest value in the generation map
    int max = -1;
    for(int x = 0; x < 10; x++){
      for(int y = 0; y < 10; y++){
        if(generationMap[y][x] > max){
          max = generationMap[y][x];
        }
      }
    }
    return max;
  }

  
  private int[] getRandomGenMapMax(){
    // get a random coordinate where the generation map has the highest value
    int max = findGenMapMax();
    int[] cords = new int[2];
    boolean valid = false;
    int x;
    int y;
    while(!valid){
      x = rand.nextInt(10);
      y = rand.nextInt(10);
      if(generationMap[y][x] == max){
        cords[0] = x;
        cords[1] = y;
        valid = true;
      }
    }
    return cords;
  }

  
  private int[] generatePlayerShipCords(){
    // generate a random player ship coordinate
    int[] cords = new int[2];
    boolean valid = false;
    int x;
    int y;
    while(!valid){
      x = rand.nextInt(10);
      y = rand.nextInt(10);
      if(playerShips[y][x] > 0){
        cords[0] = x;
        cords[1] = y;
        valid = true;
      }
    }
    return cords;
    
  }

  
  private int[] getComputerShotCords(){
    // generates computer shot coordinates based on difficulty
    int[] cords = new int[2];
    
    if(difficulty == "Beginner"){
      cords = generateShotCords();  // completely random
    }
      
    else if(difficulty == "Intermediate"){

      if(isPossible()){ // if "possible squares", generate possibile cord
        cords = generatePossible();
      }
      else{ // else get random square
        cords = generateShotCords(); 
      }
      
    }
    else if (difficulty == "Advanced"){
      if (isLikely()){ // check if there is likely square, generate random likely
        cords = generateLikely();
      }
      else if(isPossible()){ // check if there is possible square, generate possible
        cords = generatePossible();
      }
      else{
        cords = generateShotCords(); // random cord in checkerboard pattern
      }
    }
    else if (difficulty == "Expert"){
      if (isLikely()){ // check if there is likely square, generate random likely
        cords = generateLikely(); 
      }
      else if(isPossible()){ // check if there is possible square, generate possible
        cords = generatePossible();
      }
      else{ // random cord with highest value in generation map
        cords = getRandomGenMapMax();
      }
      
    }
    else{ // impossible, generates exact value of player ships
      cords = generatePlayerShipCords();
    } 
    
    return cords;
  }
} // end of class