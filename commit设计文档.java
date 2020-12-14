//Commit实现：

/*

#Week2任务有关Commit实现的功能要求

—给定一个工作区目录，生成对应的commit；

—新生成commit前，查找HEAD，
HEAD暂无，本次即为初次commit；生成commit并添加
HEAD存在，将根目录的tree key与HEAD指向的前次commit的tree key进行比较，
不同时（即文件发生了变动）生成commit并添加

—存储指向当前最新commit的HEAD指针，（原指向前次提交的commit；

#Commit文件内容
tree -本次提交的根目录tree key
parent -提交前的前次commit key
author -作者信息
committer -提交commit者信息
自定义备注（供用户自行输入类似示例中“This is the second commit."的句子）

#HEAD文件内容
当前指向的commit所对应的根目录tree key

*/

/*

#Commit 类设计

—数据域

private String key
private String value
private String presentcommit
……（若分拆则继续添加）

—构造器：参数是给定工作区目录；
value是前述的Commit文件内容，五个说明；
key根据value生成哈希值；
presentcommit是当前HEAD中所记录的前次commit key；

—成员域
getPresentcommit，即从HEAD中获取前次commit key；
HEAD相关方法，createHead()，即新建HEAD文件；updateHead()，即将提交后的commit key写入HEAD等；
value构成中涉及的相关get方法，getAuthor()，getCommitter()，getUserInput()；
returnKey()、returnValue()、 returnPresentcommit()等；

*/