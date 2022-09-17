package frame;

import javax.swing.*;
import java.awt.event.*;
import main.Main;
import util.GUIUtil;
import java.awt.Color;

public class DialogFrame extends JDialog implements ActionListener,WindowListener{
	private static final long serialVersionUID = -448427570166110594L;
	
	private JPanel contentPanel = new JPanel();;
	private JLabel errorInfoText = new JLabel("",JLabel.CENTER);
	private JButton OKButton = new JButton("OK");
	private JButton reStartButton = new JButton("再来一次");
	
	private int function=0;
	private JFrame owner;
	
	public DialogFrame(JFrame owner,String title,boolean modal,String displayInfo) {
		super(owner,title,modal);
		this.owner=owner;
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		GUIUtil.toCenter(this,380,230);
		
		this.add(contentPanel);
		contentPanel.setLayout(null);
		contentPanel.add(errorInfoText);
		contentPanel.add(OKButton);
		
		errorInfoText.setText(displayInfo);
		errorInfoText.setBounds(10, 10, 348, 125);
		OKButton.setBounds(265, 162, 93, 23);
		OKButton.setBackground(Color.WHITE);
		
		this.addWindowListener(this);
		OKButton.addActionListener(this);
		
		this.setVisible(true);	
	}
	public DialogFrame(JFrame owner,String title,boolean modal,String displayInfo,int func) {
		super(owner,title,modal);//func参数传入调用该窗口的功能号
		this.owner=owner;
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		GUIUtil.toCenter(this,380,230);
		
		this.add(contentPanel);
		contentPanel.setLayout(null);
		contentPanel.add(errorInfoText);
		contentPanel.add(OKButton);
		contentPanel.add(reStartButton);
		
		errorInfoText.setText(displayInfo);
		errorInfoText.setBounds(10, 10, 348, 125);
		OKButton.setText("确定");
		OKButton.setBounds(68, 162, 93, 23);
		reStartButton.setBounds(197, 162, 93, 23);
		OKButton.setBackground(Color.WHITE);
		reStartButton.setBackground(Color.WHITE);
		
		reStartButton.addActionListener(this);
		OKButton.addActionListener(this);
		this.addWindowListener(this);
		
		this.function=func;
		this.setVisible(true);
	}
	
	public void actionPerformed(ActionEvent e) {	//点击确定关闭窗口
		if(e.getSource()==OKButton)
			this.dispose();
		else if(e.getSource()==reStartButton) {
			this.dispose();	//关闭窗口也必须在后面代码前面，不然这个窗口会还在
			if(function==1)
				Main.MainWindow.toFun1();
			else if(function==2)
				Main.MainWindow.toFun2();
		}
	}

	public void windowClosed(WindowEvent e) {	//窗口被关闭时打开设置界面
		if(function==0) 
			new SettingDialog(owner,"设置",true);
	}
	
	@Override
	public void windowOpened(WindowEvent e) {}
	public void windowClosing(WindowEvent e) {}
	public void windowIconified(WindowEvent e) {}
	public void windowDeiconified(WindowEvent e) {}
	public void windowActivated(WindowEvent e) {}
	public void windowDeactivated(WindowEvent e) {}
}
