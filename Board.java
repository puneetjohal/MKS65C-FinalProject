import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;
public class Board extends JPanel implements ActionListener, KeyListener{

    //The score JLabel is modified from this class by using the get method in parent class
    private JLabel score;
    private JLabel curLevel;
    private predict next;
    private hold held;
    public int newScore;

    //Timer makes each piece move down at a fixed rate
    private Timer timer;
    private int level;
    private int time;
    private int clearedLines;
    private int linesToClear;

    //Keeps track of where each shape is after it reaches the bottom and finishes moving
    private int[][] coordTable;

    //These keep track of what shape is currently moving, if it is currently moving,  and where it is on the board
    private int[][][] curShape;
    private int xcor;
    private int ycor;
    private int orientation;
    private boolean moving;

    //Used when speeding up the block so that it moves a smaller distance, but more frequently
    private int displacement;

    //Used to call the functions needed to get the shapes and their dimensions
    private Tetrimino t;

    private pipe network;

    //Scanner for reading from named pipe, Filewriter for writing to pipe
    /*
    private int pid;
    private File f;
    private Scanner in;
    private FileWriter out;
*/

    //Creates the actual game
    public Board(Tetris parent, int pid) {
    	score = parent.getScore();
    	next = parent.getNext();
    	held = parent.getHold();
    	curLevel = parent.getLevel();
    	t = new Tetrimino();
    	setBackground(Color.WHITE);
    	newScore = 0;
    	coordTable = new int[20][10];

    	//Calls actionPerformed
    	time = 300;
    	level = 1;
    	clearedLines=0;
    	linesToClear=5;
    	timer=new Timer(time, this);
    	timer.start();
    	xcor = 4;
    	ycor = 0;
    	orientation = 0;
    	displacement = 0;
    	moving = false;
    	addKeyListener(this);
    	setFocusable(true);
    	setFocusTraversalKeysEnabled(false);

      //this.pid = pid;
      network = new pipe(pid + 10, pid);
    }

    //Creates all the shapes currently on the board and the shape being moved
    public void paint(Graphics g){
    	super.paintComponent(g);
    	int row = xcor*40;
    	int col = ycor*40+displacement;
    	int ori = orientation;

      try{
        System.out.println(Integer.parseInt(network.nextLine()));
      }catch(Exception e){
      }

    	if(!moving){
        if(curShape == null){
          curShape = t.randGen();
        	next.setShape(t.randGen());
    	  }
  	    curShape = next.getShape();
  	    next.setShape(t.randGen());
  	    moving = true;
      }

    	//Creates the shape that is moving
    	for(int i=0; i<t.getLen(curShape, ori); i++){
    	    for(int j=0; j<t.getWid(curShape, ori); j++){
      		if(t.getSquare(curShape,ori,i,j) == 1){
      		    g.setColor(t.getCol(curShape));
      		    g.fillRect(row,col,40,40);
      		    g.setColor(Color.BLACK);
      		    g.drawRect(row,col,40,40);
      		}
          row += 40;
        }
    	  col += 40;
    	  row = xcor*40;
    	}

    	//Creates the shapes that are not moving and still on the board based on the numbers on the coordTable which correspond to its original shape
    	for(int y=0; y<20; y++){
    	    for(int x=0; x<10; x++){
    		      if(coordTable[y][x] > 0){
    		          g.setColor(t.getCol(coordTable[y][x]));
          		    g.fillRect(x*40,y*40,40,40);
          		    g.setColor(Color.BLACK);
          		    g.drawRect(x*40,y*40,40,40);
          		    g.drawRect((row*40)+1,(col*40)+1,40,40);
          		}
    	    }
    	}
    	int y = ycor;
    	int x = xcor;
    	while(tryMoveDown(y)){
    	    y++;
    	}
    	for(int i=0; i<t.getLen(curShape, orientation); i++){
    	    for(int j=0; j<t.getWid(curShape, orientation); j++){
            if(t.getSquare(curShape,ori,i,j) == 1){
    		          g.drawRect(x*40,(y-1)*40,40,40);
                }
    		x++;
    	    }
    	    x=xcor;
    	    y++;
    	}

    	//If the shape isn't speeding up, it moves normally
    	if(displacement == 0){
    	    ycor++;
    	}
    }
    public void actionPerformed(ActionEvent e){

	//If the shape can't move down anymore, then it stops
	if(curShape != null){
	    if(!tryMoveDown(ycor)){
		moving = false;
	    }
	}

	//When a shape isn't moving anymore, store its location on the coordTable with its unique number
	if(!moving  && curShape != null){
	    int ori = orientation;
	    int tempx = xcor;
	    for(int i=0; i<t.getLen(curShape, ori); i++){
		for(int j=0; j<t.getWid(curShape, ori); j++){
		    if(t.getSquare(curShape,ori,i,j) == 1){
			coordTable[ycor-1][xcor] = t.getNum(curShape);
		    }
		    xcor++;
		}
		ycor++;
		xcor = tempx;
	    }
	    xcor = 4;
	    ycor = 0;
	    orientation = 0;
	}

	//Check if there is a full line
	isFilled();

	//If the game isn't over, then keep painting. This makes sure the game doesn't end whenever a new piece spawns
	if(!end()){
	    repaint();
	}
    }

    //Checks if a piece has space to move down or if it's at the bottom of the screen
    private boolean tryMoveDown(int y){
	if(stopPiece(y) || t.getLen(curShape, orientation) + y > 20){
	    return false;
	}
	return true;
    }

    private void instantDrop(){
	while(tryMoveDown(ycor)){
	    ycor++;
	}
    }

    private void holdPiece(){
	if(held.getShape() == null){
	    held.setShape(curShape);
	    curShape = next.getShape();
	    next.setShape(t.randGen());
	    ycor = 0;
	    xcor = 4;
	    orientation = 0;
	}else{
	    int[][][] temp = curShape;
	    curShape = held.getShape();
	    held.setShape(temp);
	    ycor = 0;
	    xcor = 4;
	    orientation = 0;
	}
    }

    //Every time an arrow key is released, call the respective function
    //Every time space is pressed, call the speedUp function
    public void keyReleased(KeyEvent e){
	if(e.getKeyCode() == KeyEvent.VK_UP){
	    rotateR();
	}
	if(e.getKeyCode() == KeyEvent.VK_DOWN){
	    rotateL();
	}
	if(e.getKeyCode() == KeyEvent.VK_RIGHT){
	    moveR();
	}
	if(e.getKeyCode() == KeyEvent.VK_LEFT){
	    moveL();
	}
	if(e.getKeyCode() == KeyEvent.VK_SPACE){
	    speedUp();
	}
	if(e.getKeyCode() == KeyEvent.VK_SHIFT){
	    holdPiece();
	}
    }
    public void keyTyped(KeyEvent e){
    }
    public void keyPressed(KeyEvent e){
	if(e.getKeyCode() == KeyEvent.VK_ENTER){
	    instantDrop();
	}
    }

    //First checks if the shape won't collide with any other pieces or exit the board after its rotation by checking the coordTable and the shape's current xcor and ycor
    private void rotateR(){
	boolean canRotate = true;
	int wid = t.getWid(curShape, (orientation+1) % t.getOris(curShape));
	int len = t.getLen(curShape, (orientation+1) % t.getOris(curShape));
	if(wid + xcor < 11 && len + ycor < 21){
	    int ori = (orientation+1) % t.getOris(curShape);
	    for(int y = ycor; y<t.getLen(curShape, ori)+ycor; y++){
		for(int x = xcor; x<t.getWid(curShape, ori)+xcor; x++){
		    if(coordTable[y][x] >= 1 || (y < 18 && coordTable[y+1][x] >= 1)){
			canRotate = false;
		    }
		}
	    }

	    //If the shape can be rotated, then its orentation is changed
	    if(canRotate){
		orientation = ori;
	    }
	}
    }

    //First checks if the shape won't collide with any other pieces or exit the board after its rotation by checking the coordTable and the shape's current xcor and ycor
    private void rotateL(){
	boolean canRotate = true;
	int wid = t.getWid(curShape, ((orientation+t.getOris(curShape)-1) % t.getOris(curShape)));
	int len = t.getLen(curShape, ((orientation+t.getOris(curShape)-1) % t.getOris(curShape)));
	if(wid + xcor < 11 && len + ycor < 21){
	    int ori = (orientation+t.getOris(curShape)-1) % t.getOris(curShape);
	    for(int y = ycor; y<t.getLen(curShape, ori)+ycor; y++){
		for(int x = xcor; x<t.getWid(curShape, ori)+xcor; x++){
		    if(coordTable[y][x] >= 1 || (y < 18 && coordTable[y+1][x] >= 1)){
			canRotate = false;
		    }
		}
	    }

	    //If the shape can be rotated, then its orentation is changed
	    if(canRotate){
		orientation = ori;
	    }
	}

    }

    //Checks if the shape can move sideways without colliding with other pieces by checking the coordTable and the shape's current xcor and ycor
    private void moveR(){
	boolean canMove = true;
	if(t.getWid(curShape, orientation) + xcor < 10){
	    for(int y=ycor; y<ycor + t.getLen(curShape, orientation); y++){
		for(int x=xcor; x<xcor + t.getWid(curShape, orientation); x++){
		    if(coordTable[y][x+1] >= 1){
		        canMove = false;
		    }
		}
	    }

	    //If the shape can move, the its position is changed
	    if(canMove){
		xcor++;
	    }
	}
    }

    //Checks if the shape can move sideways without colliding with other pieces by checking the coordTable and the shape's current xcor and ycor
    private void moveL(){
	boolean canMove = true;
	if(xcor - t.getWid(curShape, orientation) >= -(t.getWid(curShape, orientation))+1){
	    for(int y=ycor; y< ycor + t.getLen(curShape, orientation); y++){
		for(int x=xcor; x<xcor+t.getWid(curShape, orientation); x++){
		    if(coordTable[y][x-1] >= 1){
			canMove = false;
		    }
		}
	    }

	    //If the shape can move, then its position is changed
	    if(canMove){
		xcor--;
	    }
	}
    }

    //Checks if the current piece needs to be stopped because there is another piece under it
    private boolean stopPiece(int y){
	int tempY=y+t.getLen(curShape, orientation)-1;
	int i = 0;
	for(int x=xcor; x<t.getWid(curShape, orientation) + xcor; x++){

	    //Checks if the current shape is the L or J piece, in which case the empty spaces need to be taken into account
	    int testY = t.getLen(curShape, orientation)-1;
	    int curSquare = t.getSquare(curShape, orientation, testY, i);
	    if(curSquare == 0 && t.getSquare(curShape, orientation, testY-1, i) == 0){
		tempY--;
	    }

	    //Checks if the space under the piece is empty by using the coordTable and the piece's current xcor and ycor
	    if(tempY < 20 && (curSquare == 0 && coordTable[tempY-1][x] >= 1 || curSquare == 1 && coordTable[tempY][x] >= 1)){
		return true;
	    }

	    //If the shape was an L or J piece, then after the empty spaces are taken into account they can be treated as a normal piece
	    if(tempY < y+t.getLen(curShape, orientation)-1){
		tempY++;
	    }
	    i++;
	}
	return false;
    }

    //Start from the end of the board and if there is a full line of shapes, call clearRow to clear that row and keep checking the rows above
    private void isFilled(){
	ArrayList<Integer> rows = new ArrayList<Integer> ();
	for(int y=19; y>=0; y--){
	    boolean filled = true;
	    for(int x=0; x<10; x++){
		if(coordTable[y][x] == 0){
		    filled = false;
		}
	    }
	    if(filled){
		rows.add((Integer)(y));
		filled = false;
	    }
	}
	if(rows.size()>0){
	    clearRow(rows);
	}
    }

    //Get rid of the shapes in the full row and call moveDown to move down all the shapes in the row above
    public void clearRow(ArrayList<Integer> rows){
	int lines = rows.size();
	int row = 0;
	for(int y=0; y<lines; y++){
	    row = (int)(rows.get(y));
	    for(int x=0; x<10; x++){
		coordTable[row][x] = 0;
	    }
	    moveDown(row);
	}

  network.writeOut("" + lines);

	int multiplier = 0;
	if(lines == 1){
	    multiplier = 40;
	}
	if(lines == 2){
	    multiplier = 100;
	}
	if(lines == 3){
	    multiplier = 300;
	}
	if(lines == 4){
	    multiplier = 1200;
	}
	clearedLines += lines;
	//Add 10 points to the score for every row deleted
	newScore += multiplier * level;
	if(lines == 2 || lines == 3){
	    newScore -= 40*level;
	    clearedLines -= 1;
	}
	if(lines == 4){
	    newScore -= 100*level;
	    clearedLines -= 2;
	}
	if(clearedLines >= linesToClear){
	    level++;
	    linesToClear += 5;
	}
	score.setText("Score:"+String.valueOf(newScore));
	curLevel.setText("Level:"+String.valueOf(level));
    }

    //Shift all of the values on the coordTable above the deleted row down one row
    public void moveDown(int y){
	while(y>0){
	    for(int x=0; x<10; x++){
		coordTable[y][x] = coordTable[y-1][x];
	    }
	    y--;
	}
    }

    //First checks if the piece can be moved down two spaces without colliding with another piece
    public void speedUp(){
	if(ycor + t.getLen(curShape, orientation) < 20){
	    boolean canMove = true;
	    for(int y=ycor+2; y<ycor + 2  + t.getLen(curShape, orientation); y++){
		for(int x=xcor; x<xcor + t.getWid(curShape, orientation); x++){
		    if(y<20 && coordTable[y][x] >= 1){
			canMove = false;
		    }
		}
	    }

	    //If the piece can move, then move it down half a space 4 times at double the normal speed
	    if(canMove){
		for(int i=0; i<5; i++){
		    try{
			if(i>1){
			    Thread.sleep(75);
			}
		    }catch(InterruptedException e){
			Thread.currentThread().interrupt();
		    }
		    repaint();
		    displacement += 20;
		}
		displacement = 0;
	    }
	}
    }

    public void restart(){
    	repaint();
    	revalidate();
    	coordTable = new int[20][10];
    	score.setText("Score:0");
    	curLevel.setText("Level:1");
    	newScore = 0;
    	timer.start();
    	xcor = 4;
    	ycor = 0;
    	orientation = 0;
    	displacement = 0;
    	level = 1;
    	linesToClear = 5;
    	clearedLines = 0;
    	moving = false;
    	next.setShape(null);
    	held.setShape(null);
    	curShape = null;
    }

    //Check if the player lost and if they did then end the game
    public boolean end(){
    	if(coordTable[0][4] >= 1 && coordTable[1][4] >= 1){
    	    timer.stop();
          network.writeOut("" + 0);
    	    return true;
    	}
    	return false;
    }
}
