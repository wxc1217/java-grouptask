import java.util.Scanner;

public class CommitTest{
    public static void main(String[] args) {
        // 首次运行时须运行此部分，避免出错
        // Hash.initsetting();
        // Branch.branchcreate("main","");
        // Branch.branchchange("main");
        try {
            // 提示用户输入文件夹路径名
            System.out.println("请输入文件夹路径：" );
            Scanner input = new Scanner(System.in);
            String rootDirPath = input.next();
            System.out.println("请输入作者名：");
            String author= input.next();
            System.out.println("请输入提交者名：");
            String committer= input.next();
            System.out.println("请输入备注信息：");
            String message= input.next();
            input.close();
            Commit testCommit = Commit.createCommit(rootDirPath, author, committer, message);
            System.out.println("本次Commit的Value为：");
            System.out.println(testCommit.getValue());
            System.out.println("本次Commit的Key为："+testCommit.getKey());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
