import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import javax.imageio.ImageIO;
import java.io.IOException;
public class instructions  extends JPanel{
  private Image backgroundImg;
  private JTextArea textArea;
  public GridBagConstraints gbc;
  public instructions(String fileName){
    try {
          backgroundImg = ImageIO.read(new File(fileName));
    } catch (IOException e) {}
    setPreferredSize(new Dimension(800,1000));
    setLayout(new GridBagLayout());
    gbc=new GridBagConstraints();
    gbc.gridy= 0;
    gbc.anchor=GridBagConstraints.CENTER;

    textArea = new JTextArea(700, 700);
    textArea.setFont(new Font("Serif",Font.PLAIN,14));
    textArea.setText("The pieces can be moved horizontally using the left and right arrow keys.\n The pieces can be rotated using the up and down arrow keys.\nThe space key can be used to make the piece drop faster.\n The enter key can be used to instantly drop down pieces\nThe goal is to fill a row of the board using the pieces\n For each row filled the player gains a certain amount of points based on the number of lines cleared.\nIn order for a player to advance a level the player must clear five more rows than they cleared in the previous level\n(5 to get to the second level, 10 to get to the third and so on)\nThe game ends when a piece cannot be placed inside of the board.");
    textArea.setEditable(false);
    this.add(textArea,gbc);


  }

  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    g.drawImage(backgroundImg, 0, 0, null);
    setOpaque(true);
  }

}
