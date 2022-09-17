package main;

import frame.*;

//-Dfile.encoding=utf-8,用于生成exe文件时指定编码
public class Main {
	public static MainFrame MainWindow;	//便于其他窗口返回主页面
	public static void main(String[] args) {
		MainWindow = new MainFrame();
		MainWindow.setVisible(true);
	}
}
