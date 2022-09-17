package util;
import java.awt.Component;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.GraphicsEnvironment;

public class GUIUtil {
	public static Font font_text = new Font("微软雅黑", Font.BOLD , 15);
	public static Font font_eng = new Font("Times New Roman", Font.BOLD, 30);
	public static Font font_button = new Font("黑体", Font.PLAIN, 20);
	public static Font font_title = new Font("华文中宋", Font.BOLD, 33);
	public static Font font_buttonfun1 = new Font("华文中宋", Font.PLAIN, 16);
	
	public static void toCenter(Component Comp) {
		Comp.setSize(600,380);
		GraphicsEnvironment ge=
				GraphicsEnvironment.getLocalGraphicsEnvironment();
		Rectangle rec=
				ge.getDefaultScreenDevice().getDefaultConfiguration().getBounds();
		Comp.setLocation(((int)rec.getWidth()-Comp.getWidth())/2,
						 ((int)rec.getHeight()-Comp.getHeight())/2);
	}
	
	public static void toCenter(Component Comp,int length,int height) {
		Comp.setSize(length,height);
		GraphicsEnvironment ge=
				GraphicsEnvironment.getLocalGraphicsEnvironment();
		Rectangle rec=
				ge.getDefaultScreenDevice().getDefaultConfiguration().getBounds();
		Comp.setLocation(((int)rec.getWidth()-Comp.getWidth())/2,
						 ((int)rec.getHeight()-Comp.getHeight())/2);
	}
}
