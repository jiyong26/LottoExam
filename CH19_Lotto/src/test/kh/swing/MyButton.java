package test.kh.swing;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JButton;

public class MyButton extends JButton {
      private Color txtColor = Color.black;
	  private Color bgColor = Color.white;
	  public MyButton(String txt) {
		  super(txt);
		  setBorderPainted(false);   // 테두리선 없음
		  setOpaque(false);    // 뒷배경색이 나타남. 투명도 100%		  
	  }
	  	 
	public void setTxtColor(Color txtColor) {
		this.txtColor = txtColor;
	}	
	public void setBgColor(Color bgColor) {
		this.bgColor = bgColor;
	}

	@Override
	  public void paint(Graphics grps) {
		  getWidth();
		  grps.setColor(bgColor);    // setColor 다음에 이어서 나오는 부분에 지정할 색상.
		  grps.fillRoundRect(0, 0, getWidth(), getHeight(), 100, 100); //100대신 다른숫자해보기
		  
		  grps.setColor(txtColor);   // 여기서 setColor다시 지정하면.. 이 다음부터 나오는 부분에 지정할 색상.
		  grps.drawString(getText(), (int)getSize().getWidth()/2-8, (int)getSize().getWidth()/2+5);
		  
	  }
	  
}
