import java.util.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;

public class pipe{
  private Path path1, path2;
  public pipe(int pid_in, int pid_out){
    path1 = Paths.get("" + pid_in);
    path2 = Paths.get("" + pid_out);
    System.out.println(path1);
    System.out.println(path2);
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
      Files.write(path2, output.getBytes("UTF-8"));
    }catch(Exception e){
    }
  }
}
