import java.io.IOException;
import java.util.Scanner;

public class testRollBack{
    
    public static void main(String[] args) throws Exception {
        
        RollBack testRollBack_1=new RollBack();
        testRollBack_1.printCommitLog();
        
        Scanner input = new Scanner(System.in);
        System.out.println("请输入希望存放RollBack文件夹的路径：");
        String rollBackPath= input.next();
        System.out.println("请输入希望回滚的CommitKey：");
        String givenCommitKey = input.next();
        
        RollBack testRollBack_2 = new RollBack(givenCommitKey,rollBackPath);
        System.out.println("所解析出的根目录Tree对象Key是："+testRollBack_2.getRootTreeKey());

        input.close();
    }
}