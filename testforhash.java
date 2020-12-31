import java.util.Scanner;

public class testforhash {
    public static void main(String[] args) {
        int end = 0;
        Scanner input = new Scanner(System.in);
        while (end == 0) {
            System.out.println("请输入当前操作（1.存储文件夹及其中文件与子文件夹的key-value；2.给出key寻找value；）：");
            int i = input.nextInt();
            if (i == 1) {
                savekeyvalue();
            } else if (i == 2) {
                getvalue();
            } else {
                System.out.println("操作选择错误");
            }
            System.out.println("请选择是否继续（0.继续；1.停止）：");
            int j = input.nextInt();
            if (j != 0) {
                end = 1;
            }
        }
        input.close();
    }

    /* 存储文件夹及其中文件与子文件夹的key-value */
    public static void savekeyvalue() {
        try {
            // 提示用户输入文件夹路径名
            System.out.println("请输入文件夹路径：");
            Scanner input3 = new Scanner(System.in);
            String path = input3.next();
            // 遍历文件夹，存储key-value
            Hash.KVstore(path);
            Hash.prKVstore(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* 给出key寻找value */
    public static void getvalue() {
        try {
            // 提示用户输入寻找的key值
            System.out.println("请输入文件key值：");
            Scanner input4 = new Scanner(System.in);
            String key = input4.next();
            System.out.println(Hash.readObjectFromFile(key));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
