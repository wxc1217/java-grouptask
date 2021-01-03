import java.io.FileOutputStream;
import java.io.FileinputStream;
import java.lang.*;

public class RollBack{
    private String rootTreeKey;
    private final String rollBackPath;

    //无参构造方法,回滚路径常量默认为Hash类提供的默认工作路径；
    public RollBack(){
        rollBackPath=Hash.objectpath;
    }

    //以给定commit key、给定储存路径为参数的构造方法
    public RollBack(String givenCommitKey,String givenPath)throws Exception{
        
        //根据给定路径，构成回滚仓库储存路径；
        rollBackPath=givenPath + "\\.RollBackRepository";
        
        //检查给定文件夹路径下是否已有，有则删除；
        File[] folder = new File(givenPath).listFiles();
        for (File f: folder){
            if (!f.getName().equals(".RollBackRepository"))
                deleteFolder(f);
        }
        
        //根据给定commit key构造Commit对象，使用访问器得到根目录tree的key；
        Commit rollBackCommit=new Commit(givenCommitKey);
        rootTreeKey=rollBackCommit.getRootTreeKey();

        //解析根目录Tree的Key，依据该tree对象所代表的文件夹内的子文件与子文件夹名称以及对应的blob/tree key进行恢复;
        recoverRollBack(rootTreeKey,rollBackPath);

        //更新HEAD指针;
        rollBackCommit.updateHEAD(rootTreeKey);
    }


    //对tree对象所代表的文件夹内的子文件与子文件夹名称以及对应的blob/tree key进行恢复;
    private void recoverRollBack(String rootTreeKey, String rollBackPath) throws Exception{
        String rootTreeKeyPath = rollBackPath +"\\"+ rootTreeKey;
        File treeNode = new File(rootTreeKeyPath);
        InputStreamReader isr = new InputStreamReader(new FileInputStream(treeNode));
        BufferedReader br = new BufferedReader(isr);
        String line = br.readLine();
        while(line != null){
            String[] list = line.split(" ");
            if(list[0].equals("Blob")){
                FileInputStream is = new FileInputStream(filePath+"\\"+list[1]);
                FileOutputStream os = new FileOutputStream(rollBackPath+"\\"+list[2]);
                byte[] buffer = new byte[1024];
                int numRead = 0;
                while (numRead != -1) {
                    numRead = is.read(buffer);
                    if (numRead > 0)
                        os.write(buffer, 0, numRead);
                }
                is.close();
                os.close();
            }
            else if(list[0].equals("Tree")){
                File f = new File(rollBackPath + "\\" + list[2]);
                if (!f.exists())
                    f.mkdir();
                // 递归dfs
                recoverRollBack(list[1], rollBackPath + "\\" + list[2]);  
            }
            line = br.readLine();
        }
    }

        //清除已有的文件夹的方法
        private void deleteFolder(File f) throws Exception {
            if (!f.exists()) {
                throw new Exception("No folder!");
            }
            File[] files = f.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        deleteFolder(file);
                    } else {
                        file.delete();
                    }
                }
            }
            f.delete();
        }
    
        //展示当前历史Commit的日志；用于回滚前查询
        public void printCommitLog()throws IOException{
            Commit temp=new Commit();
            String presentCommit=temp.readHEAD();//从HEAD文件中读取当前Commit的key
            Commit pCommit=new Commit(presentCommit);

            System.out.println(pCommit.getKey());
            System.out.println(pCommit.getValue());

            String previousCommit = pCommit.getPreviousCommit();

            while(previousCommit != null){ //递归遍历

                Commit cCommit = new Commit(previousCommit);

                System.out.println(cCommit.getKey());
                System.out.println(cCommit.getValue());
                
                previousCommit = cCommit.getParent();
        }

        //访问器方法
        public String getRootTreeKey(){
            return this.rootTreeKey;
        }

}