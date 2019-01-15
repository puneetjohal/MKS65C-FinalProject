import java.util.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;

public class pipe{
  private Path path1, path2;
  private File f;
  public pipe(int pid_in, int pid_out){
    path1 = Paths.get("" + pid_in);
    path2 = Paths.get("" + pid_out);
    System.out.println(path1);
    System.out.println(path2);
    f = new File("" + pid_out);
  }
  public boolean hasNext(){
    boolean next = false;
    return next;
  }
  public String nextLine(){
    String input = null;
    try{
      input = new String(Files.readAllBytes(path1));
    }catch(Exception e){
    }
    return input;
  }
  public void writeOut(String output){
    try{
      //Files.write(path2, output.getBytes("UTF-8"));
      FileWriter out = new FileWriter(f);
      out.write(output);
      out.flush();
      out.close();
      System.out.println("Sent message: " + output);
    }catch(Exception e){
    }
  }
}
