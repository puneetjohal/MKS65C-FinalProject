import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.Graphics;
import java.awt.Color;
public class Tetris extends JFrame implements ActionListener{
  private Container pane;
  private JLabel score;
  private JLabel level;
  private JLabel heldPiece;
  private JLabel nextPiece;
  private Timer timer;
  private JLabel  gameover;
  private JLabel  win;
  private gameOverScreen over;
  private Board  matrix;
  private JPanel sidebar;
  private predict predictor;
  private hold held;
  public Tetris(int pid){
    this.setTitle("Tetris");
    this.setSize(800,1000);
    this.setLocation(100,0);
    this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    predictor=new predict();
    held=new hold();
    over=new gameOverScreen();
    // The timer is used to check if the game is over indicating whether the gameover method should be invoked
    timer=new Timer(10,this);
    timer.setActionCommand("gameover?");
    timer.start();

    pane = this.getContentPane();

    score=new JLabel("Score:0 ");
    gameover=new JLabel("YOULOSE");
    win=new JLabel("YOUWIN");
    level=new JLabel("Level:1");
    heldPiece=new JLabel("Hold Piece");
    nextPiece=new JLabel("Next Piece");
    gameover.setFont(new Font("Serif",Font.PLAIN,100));
    gameover.setForeground(Color.RED);
    win.setFont(new Font("Serif",Font.PLAIN,100));
    win.setForeground(Color.RED);
    sidebar=new JPanel();

    score.setFont(new Font("Serif",Font.PLAIN,50));
    level.setFont(new Font("Serif",Font.PLAIN,55));
    heldPiece.setFont(new Font("Serif",Font.PLAIN,30));
    nextPiece.setFont(new Font("Serif",Font.PLAIN,30));
    matrix=new Board(this, pid);

    pane.add(matrix);
    matrix.restart();
    sidebar.add(nextPiece);
    sidebar.add(predictor);
    sidebar.add(heldPiece);
    sidebar.add(held);
    sidebar.add(score);
    sidebar.add(level);
    sidebar.setBackground(Color.LIGHT_GRAY);
    pane.add(sidebar);
    pane.setLayout(new GridLayout());
    matrix.requestFocus();
    matrix.setVisible(true);
    matrix.requestFocus();
    predictor.setVisible(true);
    held.setVisible(true);
    sidebar.setVisible(true);

  }
  //The gameover method makes the matrix and sidebar invisible and adds the gameover label, score label and the start button to the pane.
  public void gameOver(){
    matrix.setVisible(false);
    sidebar.setVisible(false);
    predictor.setVisible(false);
    held.setVisible(false);
    matrix = null;
    pane.setLayout(new BorderLayout());
    pane.add(over, BorderLayout.PAGE_START);
    over.add(gameover);
    over.add(score);
    over.add(level);
  }
  public void gameOverWin(){
    matrix.setVisible(false);
    sidebar.setVisible(false);
    predictor.setVisible(false);
    held.setVisible(false);
    matrix = null;
    pane.setLayout(new BorderLayout());
    pane.add(over, BorderLayout.PAGE_START);
    over.add(win);
    over.add(score);
    over.add(level);
  }
  public void actionPerformed(ActionEvent e){
    String s=e.getActionCommand();
    if(s.equals("gameover?")){
      try{
        if(matrix.end()){
          if(matrix.win() == 0){
            gameOver();
          }else{
            gameOverWin();
          }
        }
      }catch(NullPointerException E){}
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
    Tetris t=new Tetris(Integer.parseInt(args[0]));
    t.setVisible(true);
  }
}
