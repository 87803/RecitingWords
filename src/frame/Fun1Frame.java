package frame;

import javax.swing.*;
import function.CountDown;
import main.Main;
import javax.swing.JLabel;
import java.awt.event.*;
import util.*;
import java.awt.Color;
import function.*;

public class Fun1Frame extends JFrame implements ActionListener{
	private static final long serialVersionUID = 5144110030488541471L;
	
	private JPanel contentPane = new JPanel();
	private JPanel theWordArea = new JPanel();
	private JLabel displayWord = new JLabel("theWord");
	private JLabel displayProcess = new JLabel("进度：1/20");
	private JLabel displayWroNum = new JLabel("错题数：0/2");
	private JLabel displayRemTime = new JLabel("剩余时间：");
	private JLabel showResult = new JLabel("",JLabel.CENTER);
	private JButton buttonA = new JButton("A.");
	private JButton buttonB = new JButton("B.");
	private JButton buttonC = new JButton("C.");
	private JButton buttonD = new JButton("D.");
	
	public static boolean isListening=true;
	private Fun1SetQuestion Q;
	
	public Fun1Frame() {
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);    //设置该参数使得该窗口关闭后释放内存https://blog.csdn.net/success_lee/article/details/26597097
		GUIUtil.toCenter(this);
		this.add(contentPane);
		contentPane.setLayout(null);
		contentPane.add(theWordArea);
		contentPane.add(buttonA);
		contentPane.add(buttonB);
		contentPane.add(buttonC);
		contentPane.add(buttonD);
		contentPane.add(displayRemTime);
		contentPane.add(showResult);
		contentPane.add(displayProcess);
		contentPane.add(displayWroNum);
		
		//以下是单词显示区
		theWordArea.setBounds(80, 38, 428, 45);
		theWordArea.add(displayWord);
		
		buttonA.setBackground(Color.WHITE);
		buttonA.setBounds(79, 200, 180, 38);
		buttonB.setBackground(Color.WHITE);
		buttonB.setBounds(328, 200, 180, 38);
		buttonC.setBackground(Color.WHITE);
		buttonC.setBounds(79, 266, 180, 38);
		buttonD.setBackground(Color.WHITE);
		buttonD.setBounds(328, 266, 180, 38);
		displayRemTime.setBounds(23, 133, 116, 20);
		displayWroNum.setBounds(23, 113, 116, 20);
		showResult.setBounds(136, 145, 298, 20);
		displayProcess.setBounds(23, 93, 116, 20);
		
		buttonA.setFont(GUIUtil.font_buttonfun1);
		buttonB.setFont(GUIUtil.font_buttonfun1);
		buttonC.setFont(GUIUtil.font_buttonfun1);
		buttonD.setFont(GUIUtil.font_buttonfun1);
		displayWord.setFont(GUIUtil.font_eng);
		displayRemTime.setFont(GUIUtil.font_text);
		displayWroNum.setFont(GUIUtil.font_text);
		displayProcess.setFont(GUIUtil.font_text);
		showResult.setFont(GUIUtil.font_text);

		buttonA.addActionListener(this);
		buttonB.addActionListener(this);
		buttonC.addActionListener(this);
		buttonD.addActionListener(this);
		this.addWindowListener(new WindowAdapter() { //用于监听当前窗口，关闭后回到主窗口
	         public void windowClosed(WindowEvent e){
	        	 CountDown.isRun=false;//停止倒计时
	        	 MainFrame.updateInfoArea();
	        	 Main.MainWindow.setVisible(true);
	         }
	    });
		
		Q=new Fun1SetQuestion(this);
		this.setVisible(true);
	}
	
	public void updateInfoArea(int w,int p) {
		displayProcess.setText("进度："+p+"/20");
		displayWroNum.setText("错题数："+w+"/2");
	}
	public void updateInfoArea(int t) {
		displayRemTime.setText("剩余时间："+t/60+":"+String.format("%02d", t%60));
	}
	
	public void setQA(String Q,String A,String B,String C,String D){//设置问题和答案区，传入问题和四个选项
		displayWord.setText(Q);
		buttonA.setText(A);
		buttonB.setText(B);
		buttonC.setText(C);
		buttonD.setText(D);
	}
	
	public void setShowResult(boolean isAnsTrue,int userChoice) {
		JButton[] a=new JButton[]{buttonA,buttonB,buttonC,buttonD};
		if(isAnsTrue) {
			showResult.setText("恭喜，回答正确！");
			a[userChoice].setBackground(Color.green);	
		}
		else {
			showResult.setText("回答错误...");
			a[userChoice].setBackground(Color.red);
		}
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {}
		a[userChoice].setBackground(Color.white);	
		showResult.setText("");
	}
	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==buttonA && isListening) {
			Q.isAnswerRight(0);
		}else if(e.getSource()==buttonB && isListening) {
			Q.isAnswerRight(1);
		}else if(e.getSource()==buttonC && isListening) {
			Q.isAnswerRight(2);
		}else if(e.getSource()==buttonD && isListening) {
			Q.isAnswerRight(3);
		}
	}	
}