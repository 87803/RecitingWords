package function;

import java.util.ArrayList;
import frame.Fun1Frame;
import main.Main;
import frame.DialogFrame;
import util.*;

public class Fun1SetQuestion implements Runnable{
	private int curProcess=1;
	private int wrongAnsNum=0;
	private int offset=0;
	private int vocabularyWordsNum;
	private int rightAnswerChoice;
	private int userAnswerChoice;
	private final int limitedTime=5;//本次挑战限时
	private String theRightAnswerWords;
	private ArrayList<String> answeredRightWords=new ArrayList<String>();//用于暂存用户答对的单词，待挑战结束后转入
	private ArrayList<String> answeredWrongWords=new ArrayList<String>();//暂存用户答错的单词
	private int[] randomNum=new int[4];
	public Fun1Frame theFrame;
	
	public Fun1SetQuestion(Fun1Frame theFrame){//传入调用窗口
		this.theFrame=theFrame;
		vocabularyWordsNum=UserInfo.curVocabularyWords.size();
		if(vocabularyWordsNum<4) {
			new DialogFrame(Main.MainWindow,"提示",true,"已学完所有单词，可以更上一层楼了！",1);
			theFrame.dispose();
			return;
		}
		if(UserInfo.curVocabularyWords.get(0).split("\\s+").length>=3)//偏移量，当词典每行开头有空格时，会拆分出三项
			offset=1;
		theFrame.updateInfoArea(wrongAnsNum,curProcess);
		new CountDown(this,limitedTime).start();
		setQuestion();
	}
	
	private void setQuestion(){
		if(curProcess>20) {
			questionEnd(0);
			return;
		}
		else if(wrongAnsNum==2) {
			questionEnd(1);
			return;
		}
		else if(vocabularyWordsNum-answeredRightWords.size()-answeredWrongWords.size()<5) {//剩余单词不超过4个，提前结束
			curProcess=-1;	//置-1便于区分
			questionEnd(2);
			return;
		}

		getFourRandomNum();
		rightAnswerChoice=(int) (Math.random()*4);//先随机确定一个选项为正确
		theRightAnswerWords=UserInfo.curVocabularyWords.get(randomNum[rightAnswerChoice]);
		theFrame.setQA(theRightAnswerWords.split("\\s+")[0+offset], 
				UserInfo.curVocabularyWords.get(randomNum[0]).split("\\s+")[1+offset], 
				UserInfo.curVocabularyWords.get(randomNum[1]).split("\\s+")[1+offset], 
				UserInfo.curVocabularyWords.get(randomNum[2]).split("\\s+")[1+offset], 
				UserInfo.curVocabularyWords.get(randomNum[3]).split("\\s+")[1+offset]);
	}
	public void isAnswerRight(int i) {
		Fun1Frame.isListening=false;
		if(i==rightAnswerChoice) {
			answeredRightWords.add(theRightAnswerWords);//答对临时存放
		}
		else {
			wrongAnsNum++;
			answeredWrongWords.add(theRightAnswerWords);
		}
		userAnswerChoice=i;
		curProcess++;
		new Thread(this).start();
	}

	void getFourRandomNum(){//用于得到4个不同的随机数，分别指向不同的单词
		int i=0;
		while(i<4) {
			randomNum[i]=(int)(Math.random()*vocabularyWordsNum);
			boolean different=true;
			System.out.println(randomNum[i]);
			for(int j=0;j<i;j++) 
				if(randomNum[i]==randomNum[j])//已经有相同的,重新生成
					different=false;
			if(answeredRightWords.contains(UserInfo.curVocabularyWords.get(randomNum[i]))||
					answeredWrongWords.contains(UserInfo.curVocabularyWords.get(randomNum[i])))
				different=false;	//如果生成的单词已经在本次挑战中答对或答错，也不再出现
			if(different==true)
				i++;
		}
	}
	
	public void questionEnd(int dialogInfo) {
		CountDown.isRun=false;
		UserInfo.studyTime+=limitedTime*60-CountDown.time;	//更新学习时间
		UserInfo.allUsers.set(UserInfo.curUserIndex,UserInfo.userName+
				"@&@"+UserInfo.studyTime+"@&@"+UserInfo.curVocabularyName);//更新学习时间
		
		for(int i=0,j=answeredWrongWords.size();i<j;i++) //成功与否都执行
			if(!UserInfo.notMasteredWords.contains(answeredWrongWords.get(i))) {
				UserInfo.notMasteredWords.add(answeredWrongWords.get(i));//答错的直接放入未掌握
				UserInfo.masteredInFun1.remove(answeredWrongWords.get(i));
				UserInfo.masteredInFun2.remove(answeredWrongWords.get(i));
			}
		
		if(curProcess>20&&wrongAnsNum<2||curProcess==-1) {//只有挑战成功才执行
			for(int i=0,j=answeredRightWords.size();i<j;i++) {
				String temp=answeredRightWords.get(i);
				if(UserInfo.notMasteredWords.contains(temp))//如果已经是未掌握单词，则不执行以下操作
					continue;
				if(UserInfo.masteredInFun2.contains(temp)) {//在功能2中已答对
					UserInfo.masteredInFun2.remove(temp);	//从功能2移除
					UserInfo.masteredWords.add(temp);	//加入已掌握
					UserInfo.curVocabularyWords.remove(temp);//从词库中移除，后续不再出现
				}else if(!UserInfo.masteredInFun1.contains(temp)) {//没在功能2答对
					UserInfo.masteredInFun1.add(temp);//加入功能1中
				}
			}
			FileOpe.updateUserFileWhenSuccess();
		}else {//挑战失败
			FileOpe.updateUserFileWhenFail();
		}
		theFrame.dispose();
		switch(dialogInfo) {
			case 0:new DialogFrame(Main.MainWindow,"挑战结束",true,"恭喜，挑战成功！",1);break;
			case 1:new DialogFrame(Main.MainWindow,"挑战结束",true,"很遗憾，挑战失败，请再接再厉！",1);break;
			case 2:new DialogFrame(Main.MainWindow,"挑战结束",true,"Wow，你已答完了词库所有单词，快去中-英挑战进一步掌握吧！",1);break;
			case 3:new DialogFrame(Main.MainWindow,"挑战结束",true,"很遗憾，时间到了，下次加快速度哦！",1);break;
		}
	}

	@Override
	public void run() {
		if(rightAnswerChoice==userAnswerChoice) 
			theFrame.setShowResult(true,userAnswerChoice);
		else 
			theFrame.setShowResult(false,userAnswerChoice);
		theFrame.updateInfoArea(wrongAnsNum,curProcess);
		setQuestion(); 
		Fun1Frame.isListening=true;
	}
}
