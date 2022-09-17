package frame;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import util.GUIUtil;
import function.*;
import main.Main;

public class Fun2Frame extends JFrame implements KeyListener{
	private static final long serialVersionUID = 2223332879112339972L;
	
	private JPanel contentPane = new JPanel();
	private JLabel theWord = new JLabel("单词",JLabel.CENTER);
	private JLabel displayProcess = new JLabel("进度：1/20");
	private JLabel displayWroNum = new JLabel("错题数：0/2");
	private JLabel displayRemTime = new JLabel("剩余时间：");
	private JLabel showResult = new JLabel("",JLabel.CENTER);
	private JLabel userAnswer = new JLabel("Answer",JLabel.CENTER);
	
	public static boolean isListening=true;
	private Fun2SetQuestion Q;
	
	public Fun2Frame() {
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		GUIUtil.toCenter(this);
		this.add(contentPane);
		contentPane.setLayout(null);
		
		contentPane.add(theWord);
		contentPane.add(displayWroNum);
		contentPane.add(displayProcess);
		contentPane.add(displayRemTime);
		contentPane.add(showResult);
		contentPane.add(userAnswer);
		
		theWord.setBounds(10, 32, 568, 60);
		displayWroNum.setBounds(23, 113, 116, 20);
		displayProcess.setBounds(23, 93, 116, 20);
		displayRemTime.setBounds(23, 133, 131, 20);
		userAnswer.setBounds(10, 163, 568, 60);
		showResult.setBounds(142, 133, 298, 20);
		
		
		theWord.setFont(GUIUtil.font_title);
		userAnswer.setFont(GUIUtil.font_eng);
		displayWroNum.setFont(GUIUtil.font_text);
		displayProcess.setFont(GUIUtil.font_text);
		displayRemTime.setFont(GUIUtil.font_text);
		showResult.setFont(GUIUtil.font_text);
		
		this.addKeyListener(this);
		this.addWindowListener(new WindowAdapter() {	//用于监听当前窗口，关闭后回到主窗口
	         public void windowClosed(WindowEvent e){
	        	 CountDown.isRun=false;//停止倒计时
	        	 MainFrame.updateInfoArea();
	        	 Main.MainWindow.setVisible(true);
	         }
	    });
		
		Q=new Fun2SetQuestion(this);
		this.setVisible(true);
	}
	public void updateInfoArea(int w,int p) {
		displayProcess.setText("进度："+p+"/20");
		displayWroNum.setText("错题数："+w+"/2");
	}
	public void updateInfoArea(int t) {
		displayRemTime.setText("剩余时间："+t/60+":"+String.format("%02d", t%60));
	}
	public void setShowResult(boolean isAnsTrue) {
		if(isAnsTrue) {
			showResult.setText("恭喜，回答正确！");
			userAnswer.setForeground(Color.green);	
		}
		else {
			showResult.setText("回答错误...");
			userAnswer.setForeground(Color.red);
		}
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {}
		userAnswer.setForeground(Color.black);
		showResult.setText("");
	}
	/*public void notShowResult() {已经与setShowResult函数合并
		userAnswer.setForeground(Color.black);
		showResult.setText("");
	}*/
	
	public void setChineseMeaning(String chMean) {
		theWord.setText(chMean);
	}
	public void setAnswerArea(String ansAreaText) {
		userAnswer.setText(ansAreaText);
	}

	public void keyTyped(KeyEvent e) {
		if(isListening)
			Q.userInput(e.getKeyChar());
	}
	@Override
	public void keyPressed(KeyEvent e) {}
	public void keyReleased(KeyEvent e) {}
}
