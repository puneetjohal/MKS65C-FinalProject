import java.util.*;
import java.io.*;

public class pipe{
  private FileWriter out;
  private Scanner in;
  private File f1, f2;
  public pipe(int pid_in, int pid_out){
    f1 = new File(pid_in);
    f2 = new File(pid_out);
  }
  public boolean hasNext(){
    in = new Scanner(f1);
    boolean next = in.hasNext();
    in.close();
    return next;
  }
  public String nextLine(){
    in = new Scanner(f1);
    String input = in.nextLine();
    in.close();
    return input;
  }
  public void writeOut(String output){
    out = new FileWriter(f2);
    out.write(output);
    out.flush();
    out.close();
  }
}
