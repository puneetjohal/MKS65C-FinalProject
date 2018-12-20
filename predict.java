import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
public class predict extends JPanel implements ActionListener{
    private Timer timer;
    private int[][][] shape;
    private Tetrimino t;
    public predict(){
	setBackground(Color.WHITE);
	setPreferredSize(new Dimension(200,200));
	t = new Tetrimino();
	timer = new Timer(50,this);
	timer.start();
    }
    public void setShape(int[][][] shape){
	this.shape = shape;
    }
    public int[][][] getShape(){
	return shape;
    }
    public void paint(Graphics g){
	if(shape != null){
	    super.paintComponent(g);
	    int x=50;
	    int y=75;
	    for(int i=0; i<t.getLen(shape,0); i++){
		for(int j=0; j<t.getWid(shape,0); j++){
		    if(t.getSquare(shape,0,i,j) == 1){
			g.setColor(t.getCol(shape));
			g.fillRect(x,y,25,25);
			g.setColor(Color.BLACK);
			g.drawRect(x,y,25,25);
		    }
		    x+=25;
		}
		y+=25;
		x=50;
	    }
	}
    }
    public void actionPerformed(ActionEvent e){
	repaint();
    }
}
