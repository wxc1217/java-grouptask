 //回滚：

/*
 #回滚功能要求：

—给定一个commit，恢复对应文件夹并替换原先工作区的根目录；
    即，查看当前分支的commit历史 ，找到某次历史commit对应的根目录Tree对象，恢复成一个文件夹；
        *Commit value里面存了前一次commit的key（链表遍历）；
        *根目录tree恢复成文件夹后，可以直接替换原先工作区的根目录；

 +根据commit key查询得到commit的value；
 +从commit value中解析得到根目录tree的key
 +恢复(path)：
    根据tree的key查询得到value
    解析value中的每一条记录，即这个tree对象所代表的文件夹内的子文件与子文件夹名称以及对应的blob/tree key
        -对于blob，在path中创建文件，命名为相应的文件名，写入blob的value
        -对于tree，在path中创建文件夹，命名为相应的文件夹名，递归调用恢复(path+文件夹名)
 +更新HEAD指针
*/


/*
 #回滚-RollBack类设计：

public class RollBack{
    数据域：
    Private Tree rootDirTree;
    private String rootDirTreeKey;
    ……

    方法域：
    
    构造方法（String theGivenCommitKey,String theGivenPath）{
        解析commit对应根目录tree的key；
        复原根目录Tree对应的文件夹；
        更新HEAD指针；
    }


    解析commit对应根目录tree的key（String theGivenCommitKey）{
        根据commit key查询得到commit的value；//调用Commit类方法；
        从commit value中解析得到根目录tree的key;
        返回根目录tree的key;
    }

    复原根目录Tree对应的文件夹（String rootDirTreeKey）throws IOException{
        根据tree的key查询得到value;//调用Tree类方法；
        解析value,获取子文件、子文件夹名称、对应的blob/tree key；
        -对于blob，在theGivenpath中创建文件，
            命名为相应的文件名，写入blob的value；//调用Blob类构造方法
        -对于tree，在theGivenpath中创建文件夹，
            命名为相应的文件夹名，递归调用恢复(path+文件夹名)//调用Tree类构造方法
    }
}
*/

/*
其他：
    1.Commit类会根据回滚功能要求再做修正；
    2.考虑到应用，是否有必要给所有类建包？
*/
