import java.util.*;
import java.nio.*;

public class pipe{
  private nio.Path path1, path2;
  public pipe(int pid_in, int pid_out){
    path1 = nio.Paths.get("" + pid_in);
    path2 = nio.Paths.get("" + pid_out);
  }
  public boolean hasNext(){
    boolean next = false;
    return next;
  }
  public String nextLine(){
    String input = new String(nio.Files.readAllBytes(path1));
    return input;
  }
  public void writeOut(String output){

  }
}
