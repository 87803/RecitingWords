package frame;

import java.awt.Font;
import javax.swing.*;
import main.Main;
import util.FileOpe;
import util.GUIUtil;
import util.UserInfo;
import java.util.ArrayList;
import java.awt.event.*;
import java.awt.Color;
import java.awt.Desktop;
import java.io.*;

public class Fun3Frame extends JFrame implements ActionListener{
	private static final long serialVersionUID = 8999563181583585479L;

	private JPanel contentPane= new JPanel();
	private JLabel engLabel = new JLabel("Word",JLabel.CENTER);
	private JLabel chiLabel = new JLabel("单词",JLabel.CENTER);
	private JLabel wordsNum = new JLabel("未掌握单词数：0/null");
	private JLabel linkLabel = new JLabel("<html><a href='#'>➢点击此处在文件中快速查看所有未掌握单词</a></html>");
	private JButton toLastButton = new JButton("上一个");
	private JButton toNextButton = new JButton("下一个");
	private int pointer=0;
	private ArrayList<String> al=new ArrayList<String>(UserInfo.notMasteredWords);
	
	public Fun3Frame() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		GUIUtil.toCenter(this);
		getContentPane().add(contentPane);
		contentPane.setLayout(null);
		setSize(600,380);
		contentPane.add(engLabel);
		contentPane.add(chiLabel);
		contentPane.add(wordsNum);
		contentPane.add(toLastButton);
		contentPane.add(toNextButton);
		contentPane.add(linkLabel);
		
		engLabel.setBounds(137, 57, 303, 59);
		chiLabel.setBounds(125, 152, 319, 28);
		wordsNum.setBounds(15, 31, 159, 28);
		toLastButton.setBounds(172, 242, 100, 33);
		toNextButton.setBounds(294, 242, 100, 33);
		toLastButton.setBackground(Color.WHITE);
		toNextButton.setBackground(Color.WHITE);
		linkLabel.setBounds(15, 274, 266, 66);
		
		engLabel.setFont(new Font("Times New Roman", Font.BOLD, 38));
		chiLabel.setFont(new Font("华文中宋", Font.PLAIN, 25));
		wordsNum.setFont(GUIUtil.font_text);
		toLastButton.setFont(GUIUtil.font_button);
		toNextButton.setFont(GUIUtil.font_button);
		linkLabel.setFont(new Font("华文中宋", Font.PLAIN, 13));
		
		this.setVisible(true);
		if(al.size()==0) {
			new DialogFrame(Main.MainWindow,"提示",true,"没有未掌握单词，继续加油！",3);
			this.dispose();//这里注意先后顺序，否则主窗口也会被关闭
		}
		else 
			setDisplayWord();
		
		toNextButton.addActionListener(this);
		toLastButton.addActionListener(this);
		this.addWindowListener(new WindowAdapter() { //用于监听当前窗口，关闭后回到主窗口
	        public void windowClosed(WindowEvent e){
	        	Main.MainWindow.setVisible(true);
	        }
	    });
		linkLabel.addMouseListener(new MouseAdapter() {  
			public void mouseClicked(MouseEvent e) {  
				try {
					Desktop.getDesktop().open(new File(FileOpe.curUserNotMasteredWordsFilePath));
				} catch (IOException e1) {}//用默认程序打开文件
	        }  
	    });  
	}
	
	private void setDisplayWord() {
		String str=al.get(pointer);
		String[] temp=str.split("\\s+");
		int offset=0;
		if(temp.length>=3)
			offset=1;
		engLabel.setText(temp[0+offset]);
		chiLabel.setText(temp[1+offset]);
		int x=pointer+1;
		wordsNum.setText("未掌握单词数："+x+"/"+al.size());
	}

	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==toNextButton&&pointer<al.size()-1) {
			pointer++;
			setDisplayWord();
		}
		else if(e.getSource()==toLastButton&&pointer>0) {
			pointer--;
			setDisplayWord();
		}
	}
}
