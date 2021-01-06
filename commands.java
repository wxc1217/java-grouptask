import java.io.*;
import java.util.Scanner;

public class commands extends Hash {

    private commands() throws IOException{
        initsetting();
    }

    public static void commandLine(String[] args) throws Exception {
        if(args[0].equals("config")){
            others config = new others();
        }
        else if (args[0].equals("init")){
            init();
        }
        else if (args[0].equals("commit")){
            // 提交一次commit
            System.out.println("请依次输入根目录路径、作者信息、提交者信息、备注信息");
            Scanner input = new Scanner(System.in);
            String path = input.next();
            String Author = input.next();
            String Commitor = input.next();
            String appends = input.next();

            Commit Commit1 = Commit.createCommit(path, Author, Commitor, appends);
            if(Commit1.getKey() != null){
                String srcaddress = "E:\\MyGit\\src" + "\\" + Branch.branchcheck();
                RollBack rb = new RollBack(Commit1.getKey(),srcaddress);
                System.out.println("本次Commit的Value为：");
                System.out.println(Commit1.getValue());
                System.out.println("本次Commit的Key为："+Commit1.getKey());
                System.out.println("Commit 完成");
            }
        }
        else if (args[0].equals("log")){
            // 查看提交记录
            RollBack.printCommitLog();
        }
        else if (args[0].equals("rollback")){

            System.out.println("请输入对应的commit id及对应的文件路径");
            Scanner input1 = new Scanner(System.in);
            String commitid = input1.next();
            String givenpath = input1.next();
            if(commitid != null){
                String srcaddress = "E:\\MyGit\\src" + "\\" + Branch.branchcheck();
                RollBack rb = new RollBack(commitid,srcaddress);
                RollBack rb1 = new RollBack(commitid,givenpath);
            }


        }
        else if (args[0].equals("clone")){
            System.out.println("请输入对应的文件夹路径:");
            Scanner input1 = new Scanner(System.in);
            String commitid = Branch.branchcommit();
            String givenpath = input1.next();
            if(commitid != null){
                RollBack rb = new RollBack(commitid,givenpath);
            }else {
                System.out.println("当前分支无提交记录！");
            }
        }
        else if (args[0].equals("branch")){
            if (args.length==1){
                Branch.branchcheck();
            }
            else{
                Branch.branchcreate(args[1],"");//分支commit id初始化为null
            }
        }
        else if (args[0].equals("checkout")){
            Branch.branchchange(args[1]);
            System.out.println("分支切换成功！");
        }else{
            System.out.println("请输入正确的指令！");

        }
    }

    private static void init() throws Exception {
        Hash.initsetting();
        System.out.println("初始化成功！");
    }

    public static void main(String[] args) throws Exception{
        //
        // System.out.println("-");
        commands task = new commands();

        if(args.length == 0){
            System.out.println("请输入指令！");
        }else{
            task.commandLine(args);
        }

    }
}
