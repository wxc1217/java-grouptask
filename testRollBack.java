import java.io.IOException;
import java.util.Scanner;

public class testRollBack{
    
    public static void main(String[] args) throws Exception {
        
        RollBack testRollBack_1=new RollBack();
        testRollBack_1.printCommitLog();
        
        Scanner input = new Scanner(System.in);
        System.out.println("������ϣ�����RollBack�ļ��е�·����");
        String rollBackPath= input.next();
        System.out.println("������ϣ���ع���CommitKey��");
        String givenCommitKey = input.next();
        
        RollBack testRollBack_2 = new RollBack(givenCommitKey,rollBackPath);
        System.out.println("���������ĸ�Ŀ¼Tree����Key�ǣ�"+testRollBack_2.getRootTreeKey());

        input.close();
    }
}