package function;

import java.util.ArrayList;
import frame.DialogFrame;
import frame.Fun2Frame;
import main.Main;
import util.FileOpe;
import util.UserInfo;

public class Fun2SetQuestion implements Runnable{
	private int curProcess=1;
	private int wrongAnsNum=0;
	private int offset=0;
	private int vocabularyWordsNum;
	private int userAnswerPointer;
	private int theWordLength;
	private int displayLetterLoc1;
	private int displayLetterLoc2;
	private final int limitedTime=10;//本次挑战限时
	private String theRightAnswerWords;
	private String theWord;
	private ArrayList<String> answeredRightWords=new ArrayList<String>();//用于暂存用户答对的单词，待挑战结束后转入
	private ArrayList<String> answeredWrongWords=new ArrayList<String>();//暂存用户答错的单词
	private StringBuffer userAnswer;
	public Fun2Frame theFrame;
	
	public Fun2SetQuestion(Fun2Frame theFrame){
		this.theFrame=theFrame;
		vocabularyWordsNum=UserInfo.curVocabularyWords.size();
		if(vocabularyWordsNum<2) {
			new DialogFrame(Main.MainWindow,"提示",true,"已学完所有单词，可以更上一层楼了！",2);
			theFrame.dispose();
			return;
		}
		if(UserInfo.curVocabularyWords.get(0).split("\\s+").length>=3)//偏移量，当词典每行开头有空格时，会拆分出三项
			offset=1;
		theFrame.updateInfoArea(wrongAnsNum,curProcess);
		new CountDown(this,limitedTime).start();
		setQuestion();
	}
	
	void setQuestion(){
		if(curProcess>20) {
			questionEnd(0);
			return;
		}
		else if(wrongAnsNum==2) {
			questionEnd(1);
			return;
		}
		if(vocabularyWordsNum-answeredRightWords.size()-answeredWrongWords.size()<2) {//剩余单词不超过1个，提前结束
			curProcess=-1;	//置-1便于区分
			questionEnd(2);
			return;
		}
		boolean different=false;
		int theQuestionWord=0;
		while(!different) {
			theQuestionWord=(int)(Math.random()*vocabularyWordsNum);//随机从词典取一个单词的序号
			if(!answeredRightWords.contains(UserInfo.curVocabularyWords.get(theQuestionWord))&&
					!answeredWrongWords.contains(UserInfo.curVocabularyWords.get(theQuestionWord)))
				different=true;
		}
		theRightAnswerWords=UserInfo.curVocabularyWords.get(theQuestionWord);
		theWord=theRightAnswerWords.split("\\s+")[0+offset];
		String theChinese=theRightAnswerWords.split("\\s+")[1+offset];
		theFrame.setChineseMeaning(theChinese);
		theWordLength=theWord.length();
		displayLetterLoc1=(int)(Math.random()*theWordLength);//如果两个要显示的位置相同则只显示一个字母
		displayLetterLoc2=(int)(Math.random()*theWordLength);
		
		if(UserInfo.notMasteredWords.contains(theRightAnswerWords)){//如果该单词是未掌握单词
			displayLetterLoc1=-1;//不提示任何字符
			displayLetterLoc2=-1;
		}
		userAnswer=new StringBuffer(theWord);
		userAnswerPointer=0;
		for(int i=0,j=userAnswer.length();i<j;i++) {
			if(i!=displayLetterLoc1&&i!=displayLetterLoc2)
				userAnswer.setCharAt(i, '□');
		}
		theFrame.setAnswerArea(userAnswer.toString());
	}
	
	public void userInput(char inputChar) {
		if((inputChar>=97&&inputChar<=122)||(inputChar>=65&&inputChar<=90)) {
			while(userAnswerPointer==displayLetterLoc1||userAnswerPointer==displayLetterLoc2) {
				userAnswerPointer++;
			}
			if(userAnswerPointer<theWordLength) {
				userAnswer.setCharAt(userAnswerPointer++, inputChar);
			System.out.println("userAnswerPointer:"+userAnswerPointer);
			System.out.println("theWordLength:"+theWordLength);
			theFrame.setAnswerArea(userAnswer.toString());
			}
			if(userAnswer.lastIndexOf("□")==-1) {
				Fun2Frame.isListening=false;
				isAnswerRight();
			}
		}
		else if(inputChar==8) {//退格键
			userAnswerPointer--;
			while(userAnswerPointer==displayLetterLoc1||userAnswerPointer==displayLetterLoc2) {
				userAnswerPointer--;
			}
			if(userAnswerPointer>=0) {
				userAnswer.setCharAt(userAnswerPointer, '□');
				theFrame.setAnswerArea(userAnswer.toString());
			}
			else userAnswerPointer=0;
		}
	}
	
	void isAnswerRight() {
		if(theWord.equals(userAnswer.toString())) {
			answeredRightWords.add(theRightAnswerWords);
		}
		else {
			wrongAnsNum++;
			answeredWrongWords.add(theRightAnswerWords);
		}
		curProcess++;
		new Thread(this).start();
	}
	public void questionEnd(int dialogInfo) {
		CountDown.isRun=false;
		UserInfo.studyTime+=limitedTime*60-CountDown.time;	//更新学习时间
		UserInfo.allUsers.set(UserInfo.curUserIndex,UserInfo.userName+
				"@&@"+UserInfo.studyTime+"@&@"+UserInfo.curVocabularyName);//更新学习时间
		
		for(int i=0,j=answeredWrongWords.size();i<j;i++) //成功与否都执行
			if(!UserInfo.notMasteredWords.contains(answeredWrongWords.get(i))){
				UserInfo.notMasteredWords.add(answeredWrongWords.get(i));//答错的直接放入未掌握
				UserInfo.masteredInFun1.remove(answeredWrongWords.get(i));
				UserInfo.masteredInFun2.remove(answeredWrongWords.get(i));
			}
		
		if(curProcess>20&&wrongAnsNum<2||curProcess==-1) {//只有挑战成功才执行
			for(int i=0,j=answeredRightWords.size();i<j;i++) {
				String temp=answeredRightWords.get(i);
				if(UserInfo.masteredInFun1.contains(temp)) {//在功能1中已答对
					UserInfo.masteredInFun1.remove(temp);	//从功能1移除
					UserInfo.masteredWords.add(temp);	//加入已掌握
					UserInfo.curVocabularyWords.remove(temp);//从词库中移除，后续不再出现
				}else if(!UserInfo.masteredInFun2.contains(temp) &&	//之前没在2中答对才放入
						!UserInfo.notMasteredWords.contains(temp)) {//没在功能1答对并且不是未掌握单词
					UserInfo.masteredInFun2.add(temp);//加入功能2中
				}
				else {//如果在未掌握单词中
					UserInfo.notMasteredWords.remove(temp);//从中移除
					UserInfo.masteredWords.add(temp);	//加入已掌握
				}
			}
			FileOpe.updateUserFileWhenSuccess();
		}else {//挑战失败
			FileOpe.updateUserFileWhenFail();
		}
		theFrame.dispose();
		switch(dialogInfo) {
		case 0:new DialogFrame(Main.MainWindow,"挑战结束",true,"恭喜，挑战成功！",2);break;
		case 1:new DialogFrame(Main.MainWindow,"挑战结束",true,"很遗憾，挑战失败，请再接再厉！",2);break;
		case 2:new DialogFrame(Main.MainWindow,"挑战结束",true,"Wow，你已答完了词库所有单词，快去英-中挑战进一步掌握吧！",2);break;
		case 3:new DialogFrame(Main.MainWindow,"挑战结束",true,"很遗憾，时间到了，下次加快速度哦！",2);break;
		}
	}

	public void run() {
		if(theWord.equals(userAnswer.toString())) 
			theFrame.setShowResult(true);
		else 
			theFrame.setShowResult(false);
		//try {	这部分已移到setShowResult函数中，避免再写个notShowResult函数
		//	Thread.sleep(500);
		//} catch (InterruptedException e) {}
		//theFrame.notShowResult();
		theFrame.updateInfoArea(wrongAnsNum,curProcess);
		setQuestion(); 
		Fun2Frame.isListening=true;
	}
}	
