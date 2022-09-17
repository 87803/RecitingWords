package frame;

import java.awt.event.*;//监听键盘输入
import javax.swing.*;
import main.Main;
import java.awt.Color;
import util.*;
import java.text.DecimalFormat;

public class MainFrame extends JFrame implements ActionListener{
	private static final long serialVersionUID = 4228894775541350815L;
	
	private static JPanel contentPane = new JPanel();;
	private static JPanel theTitleArea = new JPanel();
	private static JLabel titleLabel = new JLabel("单词速记");
	private static JButton ButtonOfFun1 = new JButton("英-中挑战");
	private static JButton ButtonOfFun2 = new JButton("中-英挑战");
	private static JButton ButtonOfReview = new JButton("复习未掌握单词");
	private static JButton ButtonOfSetting = new JButton("用户和词库设置");
	private static JLabel displayUserName = new JLabel("欢迎回来，null！");
	private static JLabel displayMasteredWordsNum = new JLabel("已掌握单词：0/null");
	private static JLabel displayStudyTime = new JLabel("学习时间：0.00分钟");
	private static JLabel displayVocabulary = new JLabel("词库：null");
	private static JLabel displayFun1Words = new JLabel("英-中挑战进度：0.00%");
	private static JLabel displayFun2Words = new JLabel("英-中挑战进度：0.00%");
	
	static DecimalFormat df  = new DecimalFormat("0.0");//保留两位小数，在显示学习时间时用
	
	public MainFrame() {
		setTitle("单词速记By ZJX");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GUIUtil.toCenter(this);
		getContentPane().add(contentPane);
		contentPane.setLayout(null);
		
		contentPane.add(theTitleArea);
		contentPane.add(ButtonOfFun1);
		contentPane.add(ButtonOfFun2);
		contentPane.add(ButtonOfReview);
		contentPane.add(ButtonOfSetting);
		contentPane.add(displayUserName);
		contentPane.add(displayMasteredWordsNum);
		contentPane.add(displayStudyTime);
		contentPane.add(displayVocabulary);
		contentPane.add(displayFun1Words);
		contentPane.add(displayFun2Words);
		theTitleArea.add(titleLabel);
		
		theTitleArea.setBounds(201, 10, 160, 59);
		ButtonOfFun1.setBackground(Color.WHITE);
		ButtonOfFun1.setBounds(198, 89, 174, 38);
		ButtonOfFun2.setBackground(Color.WHITE);
		ButtonOfFun2.setBounds(198, 147, 174, 38);
		ButtonOfReview.setBackground(Color.WHITE);
		ButtonOfReview.setBounds(198, 203, 174, 38);
		ButtonOfSetting.setBackground(Color.WHITE);
		ButtonOfSetting.setBounds(198, 256, 174, 38);
		displayUserName.setBounds(19, 60, 160, 20);
		displayStudyTime.setBounds(19, 85, 184, 20);
		displayMasteredWordsNum.setBounds(19, 110, 160, 20);
		displayFun1Words.setBounds(19, 135, 184, 20);
		displayFun2Words.setBounds(19, 160, 184, 20);
		displayVocabulary.setBounds(19, 185, 160, 20);
		
		titleLabel.setFont(GUIUtil.font_title);
		ButtonOfFun1.setFont(GUIUtil.font_button);
		ButtonOfFun2.setFont(GUIUtil.font_button);
		ButtonOfReview.setFont(GUIUtil.font_button);
		ButtonOfSetting.setFont(GUIUtil.font_button);
		displayVocabulary.setFont(GUIUtil.font_text);
		displayStudyTime.setFont(GUIUtil.font_text);
		displayMasteredWordsNum.setFont(GUIUtil.font_text);
		displayUserName.setFont(GUIUtil.font_text);
		displayFun1Words.setFont(GUIUtil.font_text);
		displayFun2Words.setFont(GUIUtil.font_text);
		
		ButtonOfFun1.addActionListener(this);
		ButtonOfFun2.addActionListener(this);
		ButtonOfReview.addActionListener(this);
		ButtonOfSetting.addActionListener(this);

		this.setVisible(true);
		String str=FileOpe.init_GetInfoFromFile();
		if(str!=null)
			new DialogFrame(Main.MainWindow,"错误",true,str);
	}
	
	//用于跳转窗口
	public void toFun1() {
		new Fun1Frame(); //打开新界面
		this.setVisible(false);//隐藏主界面
	}
	
	public void toFun2() {
		new Fun2Frame(); //打开新界面
		this.setVisible(false);//隐藏主界面
	}
	public void toFun3() {
		new Fun3Frame(); //打开新界面
		this.setVisible(false);//隐藏主界面
	}
	//更新界面用户面板信息
	public static void updateInfoArea() {
		displayUserName.setText("欢迎回来，"+UserInfo.userName+"!");
		displayMasteredWordsNum.setText("已掌握单词："+UserInfo.masteredWords.size()+"/"+UserInfo.numOfVocabularyWords);
		displayStudyTime.setText("学习时间："+ df.format(UserInfo.studyTime/60.0)+"分钟");
		displayVocabulary.setText("词库："+ UserInfo.curVocabularyName.replace(".txt",""));
		if(UserInfo.numOfVocabularyWords!=0) {
			displayFun1Words.setText("英-中挑战进度："+df.format((UserInfo.masteredWords.size()+
				UserInfo.masteredInFun1.size())*100/UserInfo.numOfVocabularyWords)+"%");
			displayFun2Words.setText("中-英挑战进度："+df.format((UserInfo.masteredWords.size()+
				UserInfo.masteredInFun2.size())*100/UserInfo.numOfVocabularyWords)+"%");
		}	
	}
	//监听
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==MainFrame.ButtonOfFun1) 
			toFun1();
		else if(e.getSource()==MainFrame.ButtonOfFun2) 
			toFun2();
		else if(e.getSource()==MainFrame.ButtonOfReview) 
			toFun3();
		else if(e.getSource()==MainFrame.ButtonOfSetting) 
			new SettingDialog(this,"设置",true);
	}
}


