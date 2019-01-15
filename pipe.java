import java.util.*;
import java.io.*;

public class pipe{
  private OutputStreamWriter out;
  private BufferedReader in;
  private File f1, f2;
  public pipe(int pid_in, int pid_out){
    f1 = new File("" + pid_in);
    f2 = new File("" + pid_out);
    try{
      in = new BufferedReader(new FileReader(f1));
      out = new OutputStreamWriter(new FileOutputStream(f2));
    }catch(Exception e){
    }
  }
  public boolean hasNext(){
    boolean next = false;
    try{
      next = in.ready();
    }catch(Exception e){
    }
    return next;
  }
  public String nextLine(){
    String input = null;
    try{
      input = in.readLine();
    }catch(Exception e){
    }
    return input;
  }
  public void writeOut(String output){
    try{
      out.write(output);
      out.flush();
    }catch(Exception e){
    }
  }
}
