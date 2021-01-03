import java.io.IOException;
import java.util.Scanner;

public class testCommit {
	public static void main(String args[])throws IOException {
		
        Scanner input = new Scanner(System.in);
        System.out.println("请输入Commit根目录路径：");
        String rootDirPath= input.next();
        System.out.println("请输入作者名：");
        String author= input.next();
        System.out.println("请输入提交者名：");
        String committer= input.next();
        System.out.println("请输入备注信息：");
        String message= input.next();

        Commit testCommit=new Commit(rootDirPath,author,committer,message);
        
        System.out.println("本次Commit的Value为："+testCommit.getValue());
        System.out.println("本次Commit的Key为："+testCommit.getKey());
	
	input.close();
	}

}
