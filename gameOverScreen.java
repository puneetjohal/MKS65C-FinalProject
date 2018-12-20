import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import javax.imageio.ImageIO;
import java.io.IOException;
public class gameOverScreen  extends JPanel{
  private Image backgroundImg;
  public gameOverScreen(){
    try {
      backgroundImg = ImageIO.read(new File("gameover.png"));
    } catch (IOException e) {}
    setBackground(new Color(64, 51, 43, 123));
    setPreferredSize(new Dimension(800,1000));
    setLayout(new FlowLayout());
  }
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    g.drawImage(backgroundImg, 0, 0, null);
    setOpaque(true);
  }

}
