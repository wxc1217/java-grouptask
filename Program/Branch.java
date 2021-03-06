import java.io.File;
import java.util.*;

public class Branch extends Hash{
    private static String lastcommit;
    private static String branchname;
    private static String branchaddress = "E:\\MyGit\\branches";
    private static String headaddress = "E:\\MyGit";
    private static String srcaddress = "E:\\MyGit\\src";


    /* 构造方法 */
    public Branch(String branchname, String lastcommit){
        this.branchname = branchname;
        this.lastcommit = lastcommit;
    }

    /* Branch存储方法 */
    public static void branchsave(){
        Hash.writetofile(lastcommit, branchname, branchaddress);
    }

    /* 提交commit后更新分支内容 */
    public static void commitupdate(String commit){
        String bname = branchcheck();
        if(bname != ""){
            Hash.writetofile(commit, bname, branchaddress);
        }
    }

    /* 查找当前分支最新提交 */
    public static String branchcommit(){
        String bname = branchcheck();
        if(bname != ""){
            String commitid = Hash.readObjectFromFile(bname, branchaddress);
            return commitid;
        }else{
            return null;
        }

    }

    /* Branch创建方法 */
    public static void branchcreate(String name,String commit){
        Branch newbranch = new Branch(name, commit);
        newbranch.branchsave();
        // 在远程仓库中生成以分支名称为名的文件夹
        File file1 = new File(srcaddress + "\\" + name);
        if(!file1.exists()){  //如果文件夹不存在
            file1.mkdir();  //创建文件夹
        }
    }

    /* 所有Branch显示方法 */
    public static void branchseek(){
        File dir = new File(branchaddress);
        File[] fs = dir.listFiles();
        if(fs != null) {
            // 采用ArrayList对listFiles()的结果进行排序
            ArrayList<File> paths = new ArrayList<>();
            Collections.addAll(paths, fs);
            paths.sort(Comparator.naturalOrder());
            for(File f : paths) {
                System.out.println(f.getName());
            }
        }else{
            System.out.println("当前不存在分支！");
        }
    }

    /* 当前Branch显示方法 */
    public static String branchcheck(){
        File head = new File(headaddress + "\\" + "head");
        String name = "";
        if(head.exists()){
            name = Hash.readObjectFromFile("head", headaddress);
            //System.out.println("present branch is: " + name);
        }else{
            System.out.println("当前不存在分支！");
        }
        return name;
    }

    /* Branch切换方法 */
    public static void branchchange(String branchname){
        File branch = new File(branchaddress + "\\" + branchname);
        if(branch.exists()){
            Hash.writetofile(branchname, "head", headaddress);
        } else{
            branchcreate(branchname,"");
            Hash.writetofile(branchname, "head", headaddress);
        }
    }
}

