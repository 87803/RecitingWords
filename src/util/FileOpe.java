package util;

import java.io.*;
import java.util.HashSet;
import frame.MainFrame;
import java.util.ArrayList;

public class FileOpe{
	private static String userDataPath="./src/userData/";	//程序运行目录下的用户信息文件夹
	private static String vocabularyFilePath="./src/vocabularyFile/"; //词库文件夹
	private static String curUserMasteredWordsFilePath;
	private static String curUserMasteredInFun1FilePath;
	private static String curUserMasteredInFun2FilePath;
	public static String curUserNotMasteredWordsFilePath;//该路径还在功能3中被使用，设为public
	public static String allUsersPath=userDataPath + "AllUsers.txt";//所有用户信息文件
	
	static {	//类加载时只运行一次，用于创建程序必要文件夹和文件
		File toCreat=new File("./src");
		if(!toCreat.exists()) 
			toCreat.mkdir();
		toCreat=new File(userDataPath);
		if(!toCreat.exists()) 
			toCreat.mkdir();
		toCreat=new File(vocabularyFilePath);
		if(!toCreat.exists()) 
			toCreat.mkdir();
		toCreat=new File(allUsersPath);
		if(!toCreat.exists()) {
			try {
				toCreat.createNewFile();
			} catch (IOException e) {e.printStackTrace();}
		}
	}
	
	public static void createUserFile() {//创建用户的相关文件（如果没有），并且调用初始化函数
		//该函数在首次打开程序或者切换用户/词库时调用
		curUserMasteredWordsFilePath=userDataPath+UserInfo.userName+"@&@"+UserInfo.curVocabularyName+"@&@"+"已掌握单词.txt";
		curUserNotMasteredWordsFilePath=userDataPath+UserInfo.userName+"@&@"+UserInfo.curVocabularyName+"@&@"+"未掌握单词.txt";
		curUserMasteredInFun1FilePath=userDataPath+UserInfo.userName+"@&@"+UserInfo.curVocabularyName+"@&@"+"Fun1.txt";
		curUserMasteredInFun2FilePath=userDataPath+UserInfo.userName+"@&@"+UserInfo.curVocabularyName+"@&@"+"Fun2.txt";
		File toCreat=new File(curUserMasteredInFun1FilePath);
		if(!toCreat.exists()) {//存在功能1答对的单词
			try {
				toCreat.createNewFile();
			} catch (IOException e) {e.printStackTrace();}
		}
		toCreat=new File(curUserMasteredInFun2FilePath);
		if(!toCreat.exists()) {//存在功能2答对的单词
			try {
				toCreat.createNewFile();
			} catch (IOException e) {e.printStackTrace();}
		}
		toCreat=new File(curUserMasteredWordsFilePath);
		if(!toCreat.exists()) {//存已掌握的单词
			try {
				toCreat.createNewFile();
			} catch (IOException e) {e.printStackTrace();}
		}
		toCreat=new File(curUserNotMasteredWordsFilePath);
		if(!toCreat.exists()) {//存未掌握的单词
			try {
				toCreat.createNewFile();
			} catch (IOException e) {e.printStackTrace();}
		}
		init_UserFile();
	}
	private static void init_UserFile() {	//初始化，从文件加载信息
		UserInfo.curVocabularyWords.clear();//先清空上个用户/词库的信息
		UserInfo.masteredWords.clear();
		UserInfo.notMasteredWords.clear();
		UserInfo.masteredInFun1.clear();
		UserInfo.masteredInFun2.clear();
		readFile(UserInfo.curVocabularyWords,vocabularyFilePath+UserInfo.curVocabularyName);
		readFile(UserInfo.masteredWords,curUserMasteredWordsFilePath);
		readFile(UserInfo.notMasteredWords,curUserNotMasteredWordsFilePath);
		readFile(UserInfo.masteredInFun1,curUserMasteredInFun1FilePath);
		readFile(UserInfo.masteredInFun2,curUserMasteredInFun2FilePath);
		UserInfo.numOfVocabularyWords=UserInfo.curVocabularyWords.size();
		for(int i=0,j=UserInfo.masteredWords.size();i<j;i++) {
			UserInfo.curVocabularyWords.remove(UserInfo.masteredWords.get(i));//从词库中移除已掌握单词，不在功能1/2显示
		}
	}
	public static String init_GetInfoFromFile() {//仅在程序新打开时调用,并返回成功与否信息
		FileOpe.readFile(UserInfo.allUsers,FileOpe.allUsersPath);//加载所有用户 
		try {
			UserInfo.curUserIndex=Integer.parseInt(UserInfo.allUsers.get(0));//UserInfo.curUser=UserInfo.allUsers.get(Integer.parseInt(UserInfo.allUsers.get(0)));//得到上次登录的用户
			String[] temp=UserInfo.allUsers.get(UserInfo.curUserIndex).split("@&@");
			UserInfo.userName=temp[0];
			UserInfo.studyTime=Integer.parseInt(temp[1]);
			UserInfo.curVocabularyName=temp[2];
			if(!FileOpe.isVocabularyExist(UserInfo.curVocabularyName)) {
				return "<html><body style='text-align:center;'>您上次学习的词典文件无法找到<br />请重新选择词典学习("+UserInfo.curVocabularyName+")</body></html>";
				//打开设置窗口
			}
			else {
				FileOpe.createUserFile();
				MainFrame.updateInfoArea();
				return null;
			}
			
		}catch(Exception e) {
			if(UserInfo.allUsers.size()==0) 
				return "当前没有任何用户，请创建新用户";//new DialogFrame(Main.MainWindow,"错误",true,"当前没有任何用户，请创建新用户");
			else {
				UserInfo.allUsers.clear();	//用于清空allUser的错误内容
				FileOpe.writeFile(UserInfo.allUsers,FileOpe.allUsersPath);
				return "读取用户信息文件失败，请重新创建新用户";//new DialogFrame(Main.MainWindow,"错误",true,"读取用户信息文件失败，请重新创建新用户");
			}
		}
	}
	
	public static void updateUserFileWhenSuccess() {	//挑战成功，存档
		writeFile(UserInfo.masteredWords,curUserMasteredWordsFilePath);
		writeFile(UserInfo.masteredInFun1,curUserMasteredInFun1FilePath);
		writeFile(UserInfo.masteredInFun2,curUserMasteredInFun2FilePath);
		writeFile(UserInfo.notMasteredWords,curUserNotMasteredWordsFilePath);
		writeFile(UserInfo.allUsers,FileOpe.allUsersPath);
	}
	public static void updateUserFileWhenFail() {	//挑战失败，存档
		writeFile(UserInfo.notMasteredWords,curUserNotMasteredWordsFilePath);
		writeFile(UserInfo.allUsers,FileOpe.allUsersPath);
	}
	
	public static void deleteUserFile(String toDeleteUserName){//用于删除用户时删除相关文件
		File udp=new File(userDataPath);
		String[] str=udp.list();	//用户目录下的文件列表
		for(int i=0,j=str.length;i<j;i++) {
			if(toDeleteUserName.equals((str[i].split("@&@"))[0])) {//用于得到文件列表每个文件前面的文件名
				new File(userDataPath+str[i]).delete();
			}
		}
	}

	
	public static void readFile(ArrayList<String> au,String filePath){//读文件到集合中
		try {
			/*FileInputStream fis =new FileInputStream(vocabularyFilePath+vocaName);
			InputStreamReader isr = new InputStreamReader( fis ,"UTF-8");//这部分代码用于解决词典文件读取中文乱码问题，进行转码
			BufferedReader br=new BufferedReader(isr);*/	//新解决方法直接在Eclipse中修改默认编码为UTF-8，不需转码
			au.clear();//读之前先清空，避免历史信息留在其中
			BufferedReader br=new BufferedReader(new FileReader(filePath));
			while(true) {
				String temp=br.readLine();
				if(temp==null)
					break;
				au.add(temp);
			}
			br.close();
		} catch (IOException e) {e.printStackTrace();} 
	}
	
	public static void readFile(HashSet<String> hs,String filePath){//读文件到哈希表中
		try {
			hs.clear();//读之前先清空，避免历史信息留在其中
			BufferedReader br=new BufferedReader(new FileReader(filePath));
			while(true) {
				String temp=br.readLine();
				if(temp==null)
					break;
				hs.add(temp);
			}
			br.close();
		} catch (IOException e) {e.printStackTrace();} 
	}
	
	public static void writeFile(ArrayList<String> au,String filePath){//集合数据写入文件
		try {
			PrintStream ps = new PrintStream(filePath);
			for(int i=0,j=au.size();i<j;i++) {
				ps.println(au.get(i));
			}
			ps.close();
		} catch (FileNotFoundException e) {e.printStackTrace();}	
	}
	
	public static void writeFile(HashSet<String> hs,String filePath){//哈希表数据写入文件
		try {
			PrintStream ps = new PrintStream(filePath);
			java.util.Iterator<String> ite=hs.iterator();//用于遍历哈希表
			while(ite.hasNext()) {
				ps.println(ite.next());
			}
			ps.close();
		} catch (FileNotFoundException e) {e.printStackTrace();}	
	}
	
	public static void getAllVocabulary(ArrayList<String> AU) {//用于得到词典文件列表
		File vfp=new File(vocabularyFilePath);
		String[] str=vfp.list();
		for(int i=0,j=str.length;i<j;i++) {
			AU.add(str[i]);
		}
	}
	
	public static boolean isVocabularyExist(String vocaName) {
		return new File(vocabularyFilePath+vocaName).exists();	//用于返回词典文件是否存在
	}
}
