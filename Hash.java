import java.io.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;
import java.util.*;

public class Hash {
    public static String objectpath = "E:\\MyGit\\object";

    /* 将key与value保存 */
    public static void writetofile(String value, String key)
    {
        FileWriter writer;
        try {
            writer =new FileWriter(objectpath + "\\" + key);
            writer.write(value);
            writer.flush();
            writer.close();
            System.out.println("存储:" + key);
        } catch (IOException e) {
            System.out.println("存储失败！");
            e.printStackTrace();
        }
    }

    /* 读取文件内容 */
    public static String readFromTextFile(File filename) throws IOException{
        // 按行读取文件中的内容
        ArrayList<String> strArray = new ArrayList<String>();
        InputStreamReader reader = new InputStreamReader(new FileInputStream(filename));
        BufferedReader br = new BufferedReader(reader);
        String line = "";
        line = br.readLine();
        while(line != null) {
            strArray.add(line);
            line = br.readLine();
        }
        // 将ArrayList转化为String类型返回
        String listString = String.join(" ",strArray);
        return listString;
    }

    /* key-value文件读取 */
    public static String readObjectFromFile(String objectname)
    {
        File file = new File(objectpath + "\\" + objectname);
        String value = "";
        try {
            value = readFromTextFile(file);
        } catch (IOException e) {
            System.out.println("读取失败！");
            e.printStackTrace();
        }
        return value;
    }

    /*将key-value储存为文件*/
    public static void mystorage(String key, String value){
        File file = new File(objectpath + "\\" + key);
        //若对应键值不存在，说明文件已更新或未存储，则存储文件
        if(!file.exists()){
            writetofile(value,key);
        }
    }

    /* 依照输入流计算Hash值 */
    public static byte[] SHA1Checksum(InputStream is) throws Exception {
        // 用于计算hash值的文件缓冲区
        byte[] buffer = new byte[1024];
        // 使用SHA1哈希/摘要算法
        MessageDigest complete = MessageDigest.getInstance("SHA-1");
        int numRead=0;
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

    /* 计算文件的Hash值 */
    public static String Filehash(File file) {
        // 显示特定文件Hash值
        StringBuilder result = new StringBuilder();
        try {
            FileInputStream is = new FileInputStream(file);
            byte[] sha1 = SHA1Checksum(is);
            for (int i=0;i<sha1.length;i++) {
                result.append(Integer.toString((sha1[i]>>4)&0x0F, 16));
                result.append(Integer.toString(sha1[i]&0x0F, 16));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.toString();
    }

    /* 计算文件夹的Hash值 */
    public static String Directoryhash(String value) {
        // 显示特定文件Hash值
        StringBuilder result = new StringBuilder();
        try {
            ByteArrayInputStream is = new ByteArrayInputStream(value.getBytes());
            byte[] sha1 = SHA1Checksum(is);
            for (int i=0;i<sha1.length;i++) {
                result.append(Integer.toString((sha1[i]>>4)&0x0F, 16));
                result.append(Integer.toString(sha1[i]&0x0F, 16));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.toString();
    }

    /* 对当前文件夹进行遍历，储存文件及文件夹的key-value值 */
    public static String prKVstore(String path) throws IOException {
        // 对文件夹进行深度优先遍历
        File dir = new File(path);
        System.out.println("Tree " + dir.getName() + " :");
        // 采用ArrayList记录该文件下文件与文件夹的名称与Hash值
        ArrayList<String> files = new ArrayList<>();
        // 返回该文件夹中文件的抽象路径名数组
        File[] fs = dir.listFiles();
        String key = "";
        if(fs != null) {
            // 采用ArrayList对listFiles()的结果进行排序
            ArrayList<File> paths = new ArrayList<>();
            Collections.addAll(paths, fs);
            paths.sort(Comparator.naturalOrder());
            for(File f : paths) {
                // 若为文件
                if (f.isFile()) {
                    // 计算Hash值并储存
                    key = Filehash(f);
                    String tempvalue = readFromTextFile(f);
                    // 将其key-value存储
                    mystorage(key,tempvalue);
                    System.out.println("Blob " + f.getName() + " Hash值 " + key);
                }
                // 若为文件夹
                if (f.isDirectory()) {
                    key = prKVstore(f.toString());
                }
                // 将文件与文件夹的名称、Hash值记录在当前文件夹的ArrayList中
                files.add(f.getName());
                files.add(key);
            }
        }
        // 将ArrayList转化为String类型返回
        String value = String.join(" ", files);
        key = Directoryhash(value);
        // 存储key-value文件
        mystorage(key,value);
        System.out.println("Tree " + dir.getName() + " Hash值 " + key);
        return key;
    }
    public static String KVstore(String path) throws IOException {
        // 对文件夹进行深度优先遍历
        File dir = new File(path);
        // 采用ArrayList记录该文件下文件与文件夹的名称与Hash值
        ArrayList<String> files = new ArrayList<>();
        // 返回该文件夹中文件的抽象路径名数组
        File[] fs = dir.listFiles();
        String key = "";
        if(fs != null) {
            // 采用ArrayList对listFiles()的结果进行排序
            ArrayList<File> paths = new ArrayList<>();
            Collections.addAll(paths, fs);
            paths.sort(Comparator.naturalOrder());
            for(File f : paths) {
                // 若为文件
                if (f.isFile()) {
                    // 计算Hash值并储存
                    key = Filehash(f);
                    String tempvalue = readFromTextFile(f);
                    // 将其key-value存储
                    mystorage(key,tempvalue);
                }
                // 若为文件夹
                if (f.isDirectory()) {
                    key = KVstore(f.toString());
                }
                // 将文件与文件夹的名称、Hash值记录在当前文件夹的ArrayList中
                files.add(f.getName());
                files.add(key);
            }
        }
        // 将ArrayList转化为String类型返回
        String value = String.join(" ", files);
        key = Directoryhash(value);
        // 存储key-value文件
        mystorage(key,value);
        return key;
    }
}
