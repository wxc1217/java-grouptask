# JAVA课程项目设计文档

## 一、项目基本内容

- 基于git原理开发一个简单的版本管理工具
- 命令行工具
- 参考git实现原理，实现Config、Init、Commit、Rollback、Log、Branch等核心功能。



## 二、基本功能实现

### 1. HASH类

- 定义HASH类，用于计算待存储文件及文件夹的hash值。
- 生成对应的文件对计算所得的key-value值进行存储；其中：
  - 对文件内容求hash值记为key；
  - 文件内容记为value

### 2.1 BLOB类

- 定义BLOB类，当遍历文件夹时，遇到文件类型则转化为blob并保存。
- 在类内分别实现了文件检索和新增文件的操作：
  - 文件检索：通过给定的hash值（文件名）查找对应的文件内容，从而实现文件检索。
    - 实现思路是在给定文件夹路径下生成对应的object文件夹用于存储键值对文件；
    - 检索时，按照给定的文件名生成路径直接实现检索。
  - 新增文件：若用户在本地新增了文件，则在原有object文件夹内生成对应键值对文件进行存储
    - 实现思路是获得新增文件的路径，将文件读入；
    - 之后调用hash类接口，生成对应的键值对存储文件。
    - 最后重新调用键值对生成函数，更新键值对的存储。



### 2.2 TREE类

- 定义TREE类，该类中存储的是其目录下的所有子文件名、子文件哈希值及子文件夹的哈希值。
- TREE类的检索和新增操作暂未实现。



### 3. COMMIT类

- COMMIT类功能要求
  - 给定一个工作区目录，生成对应的commit
  - 新生成commit前，查找HEAD：
    - HEAD暂无，本次即为初次commit；生成commit并添加
    - HEAD存在，将根目录的tree key与HEAD指向的前次commit的tree key进行比较，不同时（即文件发生了变动）生成commit并添加
  - 存储指向当前最新commit的HEAD指针，（原指向前次提交的commit；
- Commit文件内容
  - tree -本次提交的根目录tree key
  - parent -提交前的前次commit key
  - author -作者信息
  - committer -提交commit者信息
  - 自定义备注（供用户自行输入类似示例中“This is the second commit."的句子）
- HEAD文件内容
  - 当前指向的commit所对应的根目录tree key
- Commit类设计
  - 数据域
    - private String key
    - private String value
    - private String presentcommit
      ……（若分拆则继续添加）
  - 构造器：参数是给定工作区目录
    - value是前述的commit文件内容，五个说明；
    - key根据value生成hash值；
    - presentcommit是当前HEAD中所记录的前次commit key。
  - 成员域
    - getPresentcommit，即从HEAD中获取前次commit key；
    - HEAD相关方法，createHead()，即新建HEAD文件；
    - updateHead()，即将提交后的commit key写入HEAD等；
    - value构成中涉及的相关get方法，getAuthor()，getCommitter()，getUserInput()；
    - returnKey()、returnValue()、returnPresentcommit()等；

### 4. Branch类

- Branch类功能实现

  - 获取当前分支，显示当前所有分支的文件名；将所有分支文件存放在专门的文件夹，每次通过遍历该文件夹显示当前分支。
  - 显示每个分支的最新commit id。每个分支文件内容为当前的最新commit id，如果更新了提交，直接更改该分支文件的内容。
  - 通过名为head的文件实现head指针功能，该文件保存当前分支的文件名，当切换分支时，直接更改head文件内容。

- Branch类设计

  - 数据域：

    - Private Commit lastcommit;
    - Private String branchname;
    - Private String branchaddress；

  - 方法域

    - 构造方法：

      ```
      Branch(String branchname, Commit lastcommit){
      	将输入的文件名复制给当前文件名，更新最后一次commit；
      	若此次无commit，Branch中的commit可暂时为空；
          }
      ```

    - 存储方法

      ```
      branchsave(){
      	在地址branchaddress的位置创建名为branchname的文件，文件内容为lastcommit；
          }
      ```

    - 所有branch显示方法

      ```
      branchseek(){
      	遍历branch文件夹，显示其中所有文件名，及为当前所有分支；
          }
      ```

    - 当前branch显示方法

      ```
      branchcheck(){
      	打开head文件，显示head文件内容，即为当前分支名；
      	}
      ```

    - 切换分支

      ```
      branchchange(String branchname){
      	在分支文件夹里查询是否存在名为branchname文件：
      	若存在，更新head文件；
      	若不存在，显示给用户，或直接新建一个分支，储存并交换；
          }
      ```

      

### 5. Rollback类

- 回滚功能要求

  - 给定一个commit，恢复对应文件夹并替换原先工作区的根目录；

    即，查看当前分支的commit历史 ，找到某次历史commit对应的根目录Tree对象，恢复成一个文件夹；

    - Commit value里面存了前一次commit的key（链表遍历）；
    - 根目录tree恢复成文件夹后，可以直接替换原先工作区的根目录；
    - 根据commit key查询得到commit的value；
    - 从commit value中解析得到根目录tree的key
    - 恢复(path)：

    - 根据tree的key查询得到value

    - 解析value中的每一条记录，即这个tree对象所代表的文件夹内的子文件与子文件夹名称以及对应的blob/tree key
      - 对于blob，在path中创建文件，命名为相应的文件名，写入blob的value
      - 对于tree，在path中创建文件夹，命名为相应的文件夹名，递归调用恢复(path+文件夹名)
    - 更新HEAD指针

- 回滚类设计

  - 数据域：

    - Private Tree rootDirTree; 
    - private String rootDirTreeKey;

  - 方法域

    - 构造方法

      ```
      public Rollback(String theGivenCommitKey,String theGivenPath){
      	解析commit对应根目录tree的key；
      	复原根目录Tree对应的文件夹；
      	更新HEAD指针；
          }
      ```

    - 解析commit对应根目录的treekey

      ```
      解析commit对应根目录tree的key（String theGivenCommitKey）{
      	根据commit key查询得到commit的value；
      	//调用Commit类方法；
      	从commit value中解析得到根目录tree的key;
      	返回根目录tree的key;
          }
      ```

    - 复原根目录对应的文件夹

      ```
      复原根目录Tree对应的文件夹（String rootDirTreeKey）throws IOException{
      	根据tree的key查询得到value;//调用Tree类方法；
      	解析value,获取子文件、子文件夹名称、对应的blob/tree key；
      	-对于blob，在theGivenpath中创建文件，命名为相应的文件名，写入blob的value；
      	//调用Blob类构造方法
      	-对于tree，在theGivenpath中创建文件夹，命名为相应的文件夹名，递归调用恢复(path+文件夹名)
      	//调用Tree类构造方法
          }
      ```

    - 其他

      定义一个静态方法，打印历史commit日志信息，以供用户查看并选取回滚目标。



### 6.Diff类

- Diff类实现功能

  - 实现cat方法

    - 获取特定Commit的根目录Hash值，此处默认为当前分支最新一次Commit。
    - 按行读取根目录Tree文件的内容，若为“Blob”，将其文件名与目标文件名对比，一致则返回该文件Hash值；若为“Tree”，则递归处理。
    - 依照文件Hash值读取并显示文件内容

    

### 7.命令行设计文档

- 输入对应的命令，调用相应的类接口实现相应的功能。
  - `git config`获得用户名、日期及附加信息等
  - `git init` 初始化仓库
  - `git commit` 实现对每一次修改的提交
  - `git branch + 分支名称`
  - `git checkout +分支名称` 切换至该分支
  - `git log` 实现查看所有commit历史
  - `git rollback` 实现分支回滚
  - `git clone`实现仓库的克隆
  - `git cat` 实现对应的文件查找
- 对于不同的用户要求，使用`String[] args`获得输入流。
- 按照用户输入，匹配对应函数，调用相应的接口；
  - 若命令输入错误，函数库中无此命令，则返回提示信息；
  - 若命令输入正确，则调用相应的函数，执行相应操作。根据main函数的参数传递，调用不同的函数实现对应功能。



### 8.Others

- 提示用户输入用户名、当前日期、附加信息
- 将上述信息分别存储为文件写入磁盘，以供后续调用。