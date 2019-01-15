import java.util.*;
import java.io.*;

public class pipe{
  private FileWriter out;
  private Scanner in;
  private File f1, f2;
  public pipe(int pid_in, int pid_out){
    f1 = new File("" + pid_in);
    f2 = new File("" + pid_out);
  }
  public boolean hasNext(){
    boolean next = false;
    try{
      in = new Scanner(f1);
      next = in.hasNext();
      in.close();
    }catch(Exception e){
    }
    return next;
  }
  public String nextLine(){
    String input = null;
    try{
      in = new Scanner(f1);
      input = in.nextLine();
      in.close();
    }catch(Exception e){
    }
    return input;
  }
  public void writeOut(String output){
    try{
      out = new FileWriter(f2);
      out.write(output);
      out.flush();
      out.close();
    }catch(Exception e){
    }
  }
}
