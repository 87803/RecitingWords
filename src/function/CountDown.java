package function;

public class CountDown extends Thread{
	private Fun1SetQuestion f1SQ;
	private Fun2SetQuestion f2SQ;
	public static int time;
	public static boolean isRun = true;
	
	public CountDown(Fun1SetQuestion f1SQ,int t) {//传入函数调用者和倒计时分钟数
		this.f1SQ=f1SQ;
		this.f2SQ=null;
		time=t*60;		
	}
	public CountDown(Fun2SetQuestion f2SQ,int t) {//传入函数调用者和倒计时分钟数
		this.f2SQ=f2SQ;
		time=t*60;
	}
	public void run() {
		isRun=true;
		if(f2SQ==null) {
			while(isRun && time>=0) {
				f1SQ.theFrame.updateInfoArea(time);
				time--;
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {e.printStackTrace();}
			}
			if(time<=0)
				f1SQ.questionEnd(3);
		}
		else {
			while(isRun && time>=0) {
				f2SQ.theFrame.updateInfoArea(time);
				time--;
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {e.printStackTrace();}
			}
			if(time<=0)
				f2SQ.questionEnd(3);
		}
	}
}
