import java.io.*;
import java.security.MessageDigest;
import java.util.*;
import java.io.File;
        import java.io.FileInputStream;
import java.io.InputStream;


public class hash{
    public static void main(String[] args) {
        try {
            //提示用户输入文件夹路径名
            System.out.println("请输入文件夹路径：" );
            Scanner input = new Scanner(System.in);
            String path = input.next();
            //对文件夹进行深度优先遍历
            Map<String,String> mmap = new HashMap<>();
            mmap = dfs(path);
            System.out.println(mmap.get("-40-76-80-34-464-78-7134-3f-f6-43-15-144d-1f-3223-10"));//按照该给定哈希值寻找对应的文件内容
            File p = new File(path);
            p = add(path);
            mmap.put(showhash(p),readFromTextFile(p));//新建插入文件
            dfs(path);//重新显示列表，可以看出已成功新增文件
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static  File add(String path){
        System.out.println("请输入想要插入的文件路径、文件名称、文件内容：" );
        Scanner input1 = new Scanner(System.in);
        String path1 = input1.next();
        String name = input1.next();
        String content = input1.next();

        File p = new File(path);
        p = writeTXT(path1,name,content);
        return p;
    };

    public static byte[] SHA1Checksum(InputStream is) throws Exception {
        // 用于计算hash值的文件缓冲区
        byte[] buffer = new byte[1024];
        // 使用SHA1哈希/摘要算法
        MessageDigest complete = MessageDigest.getInstance("SHA-1");
        int numRead = 0;
        do {
            // 读出numRead字节到buffer中
            numRead = is.read(buffer);
            if (numRead > 0) {
                // 根据buffer[0:numRead]的内容更新hash值
                complete.update(buffer, 0, numRead);
            }
            // 文件已读完，退出循环
        } while (numRead != -1);
        // 关闭输入流
        is.close();
        // 返回SHA1哈希值
        return complete.digest();
    }

    public static String showhash(File file) {
        //显示特定文件Hash值
        StringBuilder result = new StringBuilder();
        try {
            FileInputStream is = new FileInputStream(file);
            byte[] sha1 = SHA1Checksum(is);
            for (byte i : sha1) {
                result.append(Integer.toString(i, 16));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.toString();
    }


    public static String readFromTextFile(File filename) throws IOException{
        ArrayList<String> strArray = new ArrayList<String>();

        InputStreamReader reader = new InputStreamReader(new FileInputStream(filename));
        BufferedReader br = new BufferedReader(reader);
        String line = "";
        line = br.readLine();
        while(line != null) {
            strArray.add(line);
            line = br.readLine();
        }
        String listString = String.join(" ",strArray);
        return listString;
    }


    public static Map<String,String> dfs(String path) throws IOException {
        //对文件夹进行深度优先遍历
        File dir = new File(path);
        System.out.println("directory " + dir.getName() + " :");
        //返回该文件夹中文件的抽象路径名数组
        File[] fs = dir.listFiles();
        Map<String,String> mymap = new HashMap<>();

        if(fs != null) {
            //采用ArrayList对listFiles()的结果进行排序
            ArrayList<File> paths = new ArrayList<>();
            Collections.addAll(paths, fs);
            paths.sort(Comparator.naturalOrder());
            for(File f : paths) {
                if (f.isFile()) {
                    mymap.put(showhash(f),readFromTextFile(f));
                    System.out.println("file " + f.getName() + " Hash值 " + showhash(f));
                }
            }
        }
        return mymap;
    }

    public static File writeTXT(String path,String title,String content){
        File writename = new File(path);
        try {
            // 防止文件建立或读取失败，用catch捕捉错误并打印，也可以throw
            /* 写入Txt文件 */
            // 相对路径，如果没有则要建立一个新的output。txt文件
            if(!writename.exists()){
                writename.mkdirs();
            }
            writename = new File(path+"\\"+title+".txt");// 相对路径，如果没有则要建立一个新的output。txt文件
            writename.createNewFile(); // 创建新文件
            BufferedWriter out = new BufferedWriter(new FileWriter(writename));
            out.write(content); // \r\n即为换行
            out.flush(); // 把缓存区内容压入文件
            out.close(); // 最后记得关闭文件


        } catch (Exception e) {
            e.printStackTrace();
        }
        return writename;
    }


}
