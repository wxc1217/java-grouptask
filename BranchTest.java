import java.util.*;

public class BranchTest {
    // 测试部分
    public static void main(String[] args) {
        int end = 0;
        Hash.initsetting();
        Scanner input = new Scanner(System.in);
        while(end == 0){
            System.out.println("请输入当前操作（1.查找当前所有分支；2.查找当前分支；3.创建新分支;4.分支切换；5.查找当前分支commit id；6.提交commit后更新分支）：" );
            int i = input.nextInt();
            if(i == 1){
                Branch.branchseek();
            }else if(i == 2){
                Branch.branchcheck();
            }else if(i == 3){
                System.out.print("请输入分支名称：" );
                String bname = input.next();
                Branch.branchcreate(bname,"");
            }else if(i == 4){
                System.out.print("请输入切换分支名称：" );
                String bname2 = input.next();
                Branch.branchchange(bname2);
            }else if(i == 5){
                Branch.branchcommit();
            }else if(i == 6){
                System.out.print("请输入commit id：" );
                String commitid = input.next();
                Branch.commitupdate(commitid);
            }else{
                System.out.print("操作错误！" );
            }
            System.out.print("请选择是否继续（0.继续；1.停止）：" );
            int j = input.nextInt();
            if(j != 0){
                end = 1;
            }
        }
        input.close();
    }
}
