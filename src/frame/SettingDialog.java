package frame;

import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;
import util.FileOpe;
import util.GUIUtil;
import java.awt.AWTEvent;
import util.UserInfo;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.FocusListener;
import java.awt.Color;

public class SettingDialog extends JDialog implements ActionListener,KeyListener,FocusListener {
	private static final long serialVersionUID = -3992537749863863720L;
	
	private JPanel contentPanel = new JPanel();
	private JLabel lblNewLabel = new JLabel("设置",JLabel.CENTER);
	private JLabel Label_1 = new JLabel("用户");
	private JLabel Label_2 = new JLabel("词库");
	private JLabel infoLabel = new JLabel("",JLabel.CENTER);
	private JButton DeleteButton = new JButton("删除该用户");
	private JButton cancelButton = new JButton("取消");
	private JButton OKButton = new JButton("确定");
	private JButton newUserButton = new JButton("新用户");
	private JButton getVocButton = new JButton("刷新列表");
	private JTextField textNewUser = new JTextField();
	private JComboBox<String> selectUser = new JComboBox<String>();
	private JComboBox<String> selectVoc = new JComboBox<String>();
	
	public SettingDialog(JFrame owner,String title,boolean modal) {
		super(owner,title,modal);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.add(contentPanel);
		contentPanel.setLayout(null);
		GUIUtil.toCenter(this,380,230);
		
		contentPanel.add(lblNewLabel);
		contentPanel.add(DeleteButton);
		contentPanel.add(selectUser);
		contentPanel.add(selectVoc);
		contentPanel.add(cancelButton);
		contentPanel.add(OKButton);
		contentPanel.add(getVocButton);
		contentPanel.add(infoLabel);
		contentPanel.add(textNewUser);
		contentPanel.add(newUserButton);
		contentPanel.add(Label_1);
		contentPanel.add(Label_2);
		
		textNewUser.setBounds(96, 79, 145, 21);
		textNewUser.setColumns(10);
		lblNewLabel.setBounds(10, 10, 348, 23);
		DeleteButton.setBounds(32, 162, 97, 23);
		DeleteButton.setBackground(Color.WHITE);
		selectUser.setBounds(96, 43, 145, 23);
		selectUser.setBackground(Color.WHITE);
		selectVoc.setBounds(96, 110, 145, 23);
		selectVoc.setBackground(Color.WHITE);
		cancelButton.setBounds(244, 162, 97, 23);
		cancelButton.setBackground(Color.WHITE);
		OKButton.setBounds(139, 162, 97, 23);
		OKButton.setBackground(Color.WHITE);
		infoLabel.setBounds(10, 137, 348, 15);
		newUserButton.setBounds(251, 43, 92, 23);
		newUserButton.setBackground(Color.WHITE);
		getVocButton.setBounds(251, 110, 92, 23);
		getVocButton.setBackground(Color.WHITE);
		Label_1.setBounds(57, 47, 29, 15);
		Label_2.setBounds(57, 114, 29, 15);
		textNewUser.setVisible(false);

		textNewUser.addKeyListener(this);
		textNewUser.addFocusListener(this);
		cancelButton.addActionListener(this);
		OKButton.addActionListener(this);
		getVocButton.addActionListener(this);
		DeleteButton.addActionListener(this);
		newUserButton.addActionListener(this);
		
		setUserList();
		setVocList();
		if(UserInfo.userName!=null) {
			selectUser.setSelectedItem(UserInfo.userName);
		}
		if(UserInfo.curVocabularyName!=null) {
			selectVoc.setSelectedItem(UserInfo.curVocabularyName);
		}
		this.enableEvents(AWTEvent.WINDOW_EVENT_MASK);//用于阻止窗口关闭https://blog.csdn.net/b_l_east/article/details/84058267
		this.setVisible(true);	
	}
	
	private void setVocList() {
		selectVoc.removeAllItems();//先清空所有选项，不然越刷新越多
		ArrayList<String> allVocabulary=new ArrayList<String>();
		FileOpe.getAllVocabulary(allVocabulary);
		for(int i=0,j=allVocabulary.size();i<j;i++) {
			selectVoc.addItem(allVocabulary.get(i));
		}
		if(allVocabulary.size()==0)
			infoLabel.setText("没有词典文件，请放在src/vocabularyFile目录后刷新");
	}
	private void setUserList() {
		selectUser.removeAllItems();//先清空所有选项，不然越刷新越多
		for(int i=1,j=UserInfo.allUsers.size();i<j;i++) {
			String[] temp2=UserInfo.allUsers.get(i).split("@&@");
			selectUser.addItem(temp2[0]);//默认选中用户
		}
	}

	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==cancelButton) {
			if(UserInfo.userName==null||UserInfo.curVocabularyName==null) 
				infoLabel.setText("必须选择用户和词典！");
			else 
				this.dispose();
		}else if(e.getSource()==OKButton) {
			UserInfo.userName=(String) selectUser.getSelectedItem();
			UserInfo.curVocabularyName=(String) selectVoc.getSelectedItem();
			//System.out.println(MainFrame.userName);
			//System.out.println(MainFrame.curVocabularyName);
			if(UserInfo.userName==null||UserInfo.curVocabularyName==null)
				infoLabel.setText("必须选择用户和词典！");
			else {	//修改成功
				FileOpe.createUserFile();
				for(int i=1,j=UserInfo.allUsers.size();i<j;i++) {	//更新配置文件，修改上次登录用户，以及用户使用的词典
					String[] temp2=UserInfo.allUsers.get(i).split("@&@");
					if(UserInfo.userName.equals(temp2[0])) {
						UserInfo.allUsers.set(0,i+"");
						UserInfo.allUsers.set(i,UserInfo.userName+"@&@"+temp2[1]+"@&@"+UserInfo.curVocabularyName);//修改第i行
						UserInfo.curUserIndex=i;
						UserInfo.studyTime=Integer.parseInt(temp2[1]);
						FileOpe.writeFile(UserInfo.allUsers,FileOpe.allUsersPath);
						break;
					}
				}
				MainFrame.updateInfoArea();
				this.dispose();
			}
			
		}else if(e.getSource()==DeleteButton) {
			String toDelete=(String) selectUser.getSelectedItem();//得到当前选择的用户名
			for(int i=1,j=UserInfo.allUsers.size();i<j;i++) {
				String[] temp2=UserInfo.allUsers.get(i).split("@&@");//在allUsers文件中找到该用户并删除
				if(toDelete.equals(temp2[0])) {	//找到
					UserInfo.allUsers.remove(UserInfo.allUsers.get(i));
					FileOpe.writeFile(UserInfo.allUsers,FileOpe.allUsersPath);//重新写入allUsers文件
					FileOpe.deleteUserFile(toDelete);//删除该用户的相关文件
					setUserList();
					break;
				}
			}
			
		}else if(e.getSource()==getVocButton) {
			setVocList();
		}else if(e.getSource()==newUserButton) {
			textNewUser.setVisible(true);
			textNewUser.setText("在此输入新用户名后回车");
		}
	}
	protected void processWindowEvent(WindowEvent e) {
		if (e.getID() == WindowEvent.WINDOW_CLOSING&&(UserInfo.userName==null||UserInfo.curVocabularyName==null||UserInfo.allUsers.size()<=1)) {
			infoLabel.setText("必须选择用户和词典！");
			return; //直接返回，阻止默认动作，阻止窗口关闭
		}
		super.processWindowEvent(e); //该语句会执行窗口事件的默认动作(如：隐藏)
	}

	public void keyTyped(KeyEvent e) {
		if(e.getKeyChar()==KeyEvent.VK_ENTER) {
			String toNew=textNewUser.getText();
			boolean isNew=true;
			if(UserInfo.allUsers.isEmpty()) {	//文件为空，初始化第一行
				UserInfo.allUsers.add("1");
			}
			for(int i=1,j=UserInfo.allUsers.size();i<j;i++) {
				String[] temp2=UserInfo.allUsers.get(i).split("@&@");
				if(toNew.equals(temp2[0])) {
					isNew=false;
					infoLabel.setText("该用户已存在！");
					break;
				}
			}
			if(isNew) {
				UserInfo.allUsers.add(toNew+"@&@"+0+"@&@null");
				FileOpe.writeFile(UserInfo.allUsers,FileOpe.allUsersPath);
				infoLabel.setText("用户添加成功！");
				setUserList();
				textNewUser.setVisible(false);
			}
		}
	}

	public void focusGained(FocusEvent e) {
		if(textNewUser.getText().equals("在此输入新用户名后回车"))
			textNewUser.setText("");
	}

	public void focusLost(FocusEvent e) {
		if(textNewUser.getText().equals(""))
			textNewUser.setText("在此输入新用户名后回车");
	}
	
	public void keyPressed(KeyEvent e) {}
	public void keyReleased(KeyEvent e) {}
}
