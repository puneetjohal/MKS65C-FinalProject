import java.util.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;

public class pipe{
  private Path path;
  private File f;
  public pipe(int pid_in, int pid_out){
    path = Paths.get("" + pid_in);
    f = new File("" + pid_out);
  }
  public String nextLine(){
    String input = null;
    try{
      input = new String(Files.readAllBytes(path));
    }catch(Exception e){
    }
    return input;
  }
  public void writeOut(String output){
    try{
      FileWriter out = new FileWriter(f);
      out.write(output);
      out.flush();
      out.close();
    }catch(Exception e){
    }
  }
}
