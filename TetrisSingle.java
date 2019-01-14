import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.Graphics;
import java.awt.Color;
public class TetrisSingle extends JFrame implements ActionListener{
  private Container pane;
  private JLabel  score;
  private JLabel level;
  private JLabel heldPiece;
  private JLabel nextPiece;
  private startScreen screen;
  private JButton inst;
  private instructions instructScreen;
  private Timer timer;
  private JLabel  gameover;
  private gameOverScreen over;
  private JButton pause;
  private JButton play;
  private JButton start;
  private JButton restart;
  private BoardSingle  matrix;
  private JPanel sidebar;
  private predict predictor;
  private hold held;
  public TetrisSingle(){
    this.setTitle("Tetris");
    this.setSize(800,1000);
    this.setLocation(100,0);
    this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    start=new JButton("START");
    pause=new JButton("pause");
    play=new JButton("play");
    restart=new JButton("restart");
    predictor=new predict();
    held=new hold();
    screen=new startScreen("image.png");
    over=new gameOverScreen();
    instructScreen=new instructions("image.png");
    inst= new JButton("Instructions");
    // The timer is used to check if the game is over indicating whether the gameover method should be invoked
    timer=new Timer(10,this);
    timer.setActionCommand("gameover?");
    timer.start();


    pause.setFocusable(false);
    play.setFocusable(false);
    start.setFocusable(false);
    inst.setFocusable(false);
    restart.setFocusable(false);

    inst.addActionListener(this);
    start.addActionListener(this);
    restart.addActionListener(this);
    pause.addActionListener(this);
    play.addActionListener(this);

    pane = this.getContentPane();
    start.setPreferredSize(new Dimension(200, 100));
    inst.setPreferredSize(new Dimension(200, 50));
    pane.setLayout(new BorderLayout());

    score=new JLabel("Score:0 ");
    gameover=new JLabel("GAMEOVER");
    level=new JLabel("Level:1");
    heldPiece=new JLabel("Hold Piece");
    nextPiece=new JLabel("Next Piece");
    gameover.setFont(new Font("Serif",Font.PLAIN,100));
    gameover.setForeground(Color.RED);
    sidebar=new JPanel();

    score.setFont(new Font("Serif",Font.PLAIN,50));
    level.setFont(new Font("Serif",Font.PLAIN,55));
    heldPiece.setFont(new Font("Serif",Font.PLAIN,30));
    nextPiece.setFont(new Font("Serif",Font.PLAIN,30));
    matrix=new BoardSingle(this);
    pane.add(screen, BorderLayout.PAGE_START);
    GridBagConstraints gbc=new GridBagConstraints();
    gbc.gridy=1;
    gbc.weighty=1;

    gbc.anchor=GridBagConstraints.CENTER;
    screen.add(start, gbc);
    gbc.gridy=2;
    screen.add(inst, gbc);


    pane.add(matrix);
    pane.add(sidebar);
    matrix.setVisible(false);
    sidebar.setVisible(false);
    sidebar.add(restart);
    sidebar.add(play);
    sidebar.add(pause);
    sidebar.add(nextPiece);
    sidebar.add(predictor);
    sidebar.add(heldPiece);
    sidebar.add(held);
    sidebar.add(score);
    sidebar.add(level);
    sidebar.setBackground(Color.LIGHT_GRAY);

  }
  //The gameover method makes the matrix and sidebar invisible and adds the gameover label, score label and the start button to the pane.
  public void gameOver(){
    matrix.setVisible(false);
    sidebar.setVisible(false);
    predictor.setVisible(false);
    held.setVisible(false);
    pane.setLayout(new BorderLayout());
    pane.add(over, BorderLayout.PAGE_START);
    over.add(gameover);
    over.add(score);
    over.add(level);
    over.add(start);
  }
  public void actionPerformed(ActionEvent e){
    String s=e.getActionCommand();
    if(s.equals("gameover?")){
      try{
        if(matrix.end()){
          gameOver();

        }
      }catch(NullPointerException E){}
    }
    //The instructions button shows the instructions for new players unfamiliar with the game
    if(s.equals("Instructions")){
      screen.setVisible(false);
      GridBagConstraints gbc=new GridBagConstraints();
      gbc.gridy=1;
      start.setMinimumSize(new Dimension(200, 100));
      instructScreen.add(start,gbc);
      pane.add(instructScreen,BorderLayout.PAGE_START);
    }
    // The start button once pressed removes the start button and if the player is starting again the gameover label, and makes the matrix and sidebar visible, while also changing the panes layout.
    if(s.equals("START")){
	    pane.remove(over);
      pane.remove(instructScreen);
	    matrix.restart();
	    pane.setLayout(new GridLayout());
      pane.remove(start);
	    pane.remove(screen);
	    pane.remove(score);
      pane.remove(level);
      matrix.setVisible(true);
	    matrix.requestFocus();
      sidebar.add(nextPiece);
      sidebar.add(predictor);
      sidebar.add(heldPiece);
	    sidebar.add(held);
	    sidebar.add(score);
	    sidebar.add(level);
      predictor.setVisible(true);
      held.setVisible(true);
	    sidebar.setVisible(true);
    }

    // The pause button will pause the game once pressed
    if(s.equals("pause")){
	    matrix.pause();
    }
    //The restart button will restart the matrix and start the game from the beginning
    if(s.equals("restart")){
	    matrix.restart();
    }
    //the play button once pressed start the game if it is in a paused state
    else if(s.equals("play")){
	    matrix.play();
    }
  }
  //The getScore method returns the score Jlabel
  public JLabel getScore(){
    return score;
  }

  public predict getNext(){
    return predictor;
  }

  public hold getHold(){
    return held;
  }

  public JLabel getLevel(){
    return level;
  }

  public static void main(String[] args){
    TetrisSingle t=new TetrisSingle();
    t.setVisible(true);
  }
}
