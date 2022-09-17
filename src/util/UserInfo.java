package util;

import java.util.ArrayList;
import java.util.HashSet;

public class UserInfo {
	public static int curUserIndex;	//指向当前用户在AllUser.txt文件中的第几行，便于后面定位
	public static int numOfVocabularyWords;	//当前词库单词数
	public static int studyTime;	//当前用户总学习时间
	public static String userName,curVocabularyName;	//当前用户用户名、使用的词典名
	
	public static ArrayList<String> allUsers=new ArrayList<String>();//所有用户，来自AllUser.txt
	public static ArrayList<String> curVocabularyWords=new ArrayList<String>();//当前词库所有单词
	public static ArrayList<String> masteredWords=new ArrayList<String>();//已掌握单词
	public static HashSet<String> notMasteredWords=new HashSet<String>();//未掌握单词，查询为主，用哈希表
	public static HashSet<String> masteredInFun1=new HashSet<String>();//在功能1答对的单词
	public static HashSet<String> masteredInFun2=new HashSet<String>();//在功能2答对的单词
}
