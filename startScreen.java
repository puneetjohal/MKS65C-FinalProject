import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import javax.imageio.ImageIO;
import java.io.IOException;
public class startScreen  extends JPanel{
  private Image backgroundImg;
  private JLabel logo;
  public GridBagConstraints gbc;
  public startScreen(String fileName){
    try {
          backgroundImg = ImageIO.read(new File(fileName));
    } catch (IOException e) {}
    setPreferredSize(new Dimension(800,1000));
    setLayout(new GridBagLayout());
    ImageIcon img=new ImageIcon("tetris-logo.png");
    logo=new JLabel(img);
    gbc=new GridBagConstraints();
    gbc.gridy= 0;
    gbc.anchor=GridBagConstraints.NORTH;
    this.add(logo,gbc);


  }

  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    g.drawImage(backgroundImg, 0, 0, null);
    setOpaque(true);
  }

}
