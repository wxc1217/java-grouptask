import java.io.*;
import java.lang.*;
import java.util.Scanner;

public class others {
    public static String username;
    public static String date;
    public static String appendix;

    public others(){
        String MessagePath = "E:\\MyGit\\messages";
        File file1 = new File(MessagePath);
        if(!file1.exists()){  //如果文件夹不存在
            file1.mkdir();  //创建文件夹
        }
        System.out.println("please enter username: ");
        Scanner input = new Scanner(System.in);
        username = input.next();
        System.out.println("please enter the current date: ");
        date = input.next();
        System.out.println("please enter additional information: ");
        appendix = input.next();
        Hash.writetofile(username,"username",MessagePath);
        Hash.writetofile(date,"date",MessagePath);
        Hash.writetofile(appendix,"appendix",MessagePath);

    }
}
