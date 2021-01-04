package test.kh.swing;

import java.awt.Color;
import java.awt.Container;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashSet;

import javax.swing.*;

import org.json.simple.JSONObject;
//https://www.dhlottery.co.kr/common.do?method=getLottoNumber&drwNo=
//{
//"totSellamnt":59292005000,  // ��÷��
//"returnValue":"success",   // ȸ�� ����
//"drwNoDate":"2005-01-22",   // ��÷��
//"firstWinamnt":1567271167,
//"drwtNo6":42,
//"drwtNo4":33,
//"firstPrzwnerCo":9,      // 1�� ��÷�� ���
//"drwtNo5":41,
//"bnusNo":43,
//"firstAccumamnt":0,     // 1�� ��÷��
//"drwNo":112,    // ȸ��
//"drwtNo2":29,  // 2���� ��÷��ȣ
//"drwtNo3":30,   // 3���� ��÷��ȣ
//"drwtNo1":26  // 1���� ��÷��ȣ
//}

public class Lotto extends JFrame implements MouseListener, KeyListener {
	final int WIDTH_BTN = 60;
	final int WIDTH_GAP = 15;
	private final int MAX_CNT = 6;
	private final int MAX_CNT_W_BN = 7;
	// �ζ� ��÷ ��ȣ ǥ�� - ��ư : label�� �����Ͽ� �����. ���۶�� �׷��� ��ĥ�ϴ� ����.
	// 6+1
	private MyButton[] btn = new MyButton[MAX_CNT_W_BN];

	// �ζ� ��÷ Ȯ���� ��ȣ �Է� - JTextField
	private JTextField[] txt = new JTextField[MAX_CNT];

	// ȸ�� �Է� - JTextField
	private JTextField turnTxt = new JTextField();
	// ȸ��Ȯ�� - JButton
	private JButton checkBtn = new JButton("Ȯ��");

	// ���� JLabel
	private JLabel infoLbl = new JLabel();

	// ���� ����
	JLabel rankLabel = new JLabel("");
// ȸ�� ����, ��� ���� ǥ���� ��
	JLabel turnLabel = new JLabel("");

	public Lotto() {
		super("�ζ�Ȯ�� ���α׷�");
		// ó�� ȭ���� ������.
		init();

		// �̺�Ʈ ���
		event();

		// ȭ�鿡 �����ֱ�
		setSize(600, 500);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	private void init() {
		int w = 30; // �ʱ� �����ϴ� x��ġ
		Container c = getContentPane();
		c.setLayout(null);
		for (int i = 0; i < btn.length; i++) {
			btn[i] = new MyButton("0" + (i + 1));
			btn[i].setBounds(30 + (WIDTH_BTN * i) + (WIDTH_GAP * i), 50, WIDTH_BTN, WIDTH_BTN);
//			btn[i].setBounds(w, 60, WIDTH_BTN, WIDTH_BTN);
//			w += WIDTH_BTN + WIDTH_GAP;
			c.add(btn[i]);
//			btn[6].setBgColor(Color.gray);
		}

		for (int i = 0; i < txt.length; i++) {
			txt[i] = new JTextField(i + 1);
			txt[i].setBounds(30 + (WIDTH_BTN * i) + (WIDTH_GAP * i), 120, WIDTH_BTN, WIDTH_BTN);
//			txt[i].setBounds(w, 120, WIDTH_BTN, WIDTH_BTN); // ��ġ ����
			txt[i].setHorizontalAlignment(JTextField.CENTER); // Ŀ�� �߾�����
//			w += WIDTH_BTN + WIDTH_GAP;
			c.add(txt[i]); // �߰�

		}
		turnTxt.setBounds(150, 300, 100, 40);
		c.add(turnTxt);
		checkBtn.setBounds(350, 300, 100, 40);
		c.add(checkBtn);
		infoLbl.setOpaque(true);
		infoLbl.setBounds(10, 10, 300, 40);
		infoLbl.setForeground(Color.RED);
		c.add(infoLbl);
		rankLabel.setBounds(40, 60 + 60 + 60 + 30 + 60 + 5, 300, 30);
		c.add(rankLabel);
		turnLabel.setOpaque(false);
		turnLabel.setBounds(25, 60 + 60 + 60 + 30, 300, 120);
		turnLabel.setForeground(Color.black);
		c.add(turnLabel);

	}

	private void event() {
		checkBtn.addMouseListener(this); // this - ���� �����ϰ� �ִ� �� ��ü
		turnTxt.addKeyListener(this);
	}

	private void showResult() {
		super.setTitle("���ȭ�麸��");
		try {
			Integer.parseInt(turnTxt.getText());
		} catch (Exception e) {
			turnLabel.setText("ã�� ȸ���� ���ڸ� �Է����ּ���");
			turnTxt.setText("");
			return;
		}
		turnLabel.setText("");
		JsonReader jr = new JsonReader();
		JSONObject jo = jr.connectionUrlToJson(turnTxt.getText());
		String[] right = new String[MAX_CNT]; // �ζǹ�ȣ �´��� �Է��� �� ����
		int nCnt = 0; // �´� ���� ����
		int bCnt = 0; // �´� ���� ����(���ʽ� ��ȣ. 2��)
		if (jo == null) {
			infoLbl.setText("ã�� ȸ���� ������ �����ϴ�.");
			turnTxt.setText("");
			return;
		}
		if (jo.get("returnValue").equals("fail")) {
			infoLbl.setText(turnTxt.getText() + "ã�� ȸ���� ������ �����ϴ�.");
			turnTxt.setText("");
			return;
		}
		infoLbl.setText(turnTxt.getText() + "ȸ���� �����Դϴ�.");
		turnTxt.setText("");
		for (int i = 0; i < MAX_CNT; i++) {
//			btn[i].setText((String)jo.get("drwtNo"+(i+1)));   
			String strNo = String.valueOf(jo.get("drwtNo" + (i + 1)));
			btn[i].setText(strNo);
			int a = Integer.parseInt(strNo);
			if (a > 40) {
				btn[i].setBgColor(Color.green);
			} else if (a > 30) {
				btn[i].setBgColor(Color.gray);
			} else if (a > 20) {
				btn[i].setBgColor(Color.red);
			} else if (a > 10) {
				btn[i].setBgColor(Color.blue);
			} else {
				btn[i].setBgColor(Color.orange);
			}
//			switch(Integer.parseInt(strNo)/10) {
//			case 0:
//				btn[i].setBgColor(Color.orange);
//				break;
//			case 1:
//				btn[i].setBgColor(Color.blue);
//				break;
//			case 2:
//				btn[i].setBgColor(Color.red);
//				break;	
//			case 3:
//				btn[i].setBgColor(Color.gray);
//				break;	
//			default:
//				btn[i].setBgColor(Color.green);
//				break;
//			}
		}
		btn[MAX_CNT].setText(String.valueOf(jo.get("bnusNo")));
		// �ζ� ��ȣ â�� ���� ���� �ٸ� ���� �Է����� ��
		for (int i = 0; i < MAX_CNT; i++) {
			if (txt[i].getText().equals("")) { // �ζ� ��ȣ ���� �ʰ� ȸ���� ������ ��
				try {
					right[i] = txt[i].getText();
					Integer.parseInt(right[i]);
				} catch (Exception e) {
					turnLabel.setText("");
					return; // return���� �ƿ� �޼ҵ带 ����������.
				}
			}
			if (!txt[i].getText().equals("")) { // �ζ� ��ȣ�� ���� ���� ���� �Է����� ��
				try {
					right[i] = txt[i].getText();
					Integer.parseInt(right[i]);
				} catch (Exception e) {
					turnLabel.setText("���ڸ� �Է����ּ���");
					txt[0].setText("");
					txt[1].setText("");
					txt[2].setText("");
					txt[3].setText("");
					txt[4].setText("");
					txt[5].setText("");
					return;
				}
			}
		}

		// �ζ� ��ȣ ����
		if (Integer.parseInt(txt[0].getText()) < 1 || Integer.parseInt(txt[0].getText()) > 45
				|| Integer.parseInt(txt[1].getText()) < 1 || Integer.parseInt(txt[1].getText()) > 45
				|| Integer.parseInt(txt[2].getText()) < 1 || Integer.parseInt(txt[2].getText()) > 45
				|| Integer.parseInt(txt[3].getText()) < 1 || Integer.parseInt(txt[3].getText()) > 45
				|| Integer.parseInt(txt[4].getText()) < 1 || Integer.parseInt(txt[4].getText()) > 45
				|| Integer.parseInt(txt[5].getText()) < 1 || Integer.parseInt(txt[5].getText()) > 45) {
			turnLabel.setText("�ζ� ��ȣ�� ������ 1~45 �Դϴ�.");
			return;
		}

		// �ߺ� �Է� ����
		ArrayList<Integer> checkList = new ArrayList<Integer>();
		for (int i = 0; i < txt.length; i++) {
			checkList.add(Integer.parseInt(txt[i].getText()));
		}

		HashSet<Integer> hash = new HashSet<Integer>(checkList);
		ArrayList<Integer> checkedList = new ArrayList<Integer>(hash);
		if (checkedList.size() < 6) {
			turnLabel.setText("�ߺ� �Էµ� �ζ� ��ȣ�� �ֽ��ϴ�.");
			return;
		}

		for (int i = 0; i < MAX_CNT; i++) {
			for (int j = 0; j < right.length; j++) {
				if (right[j].equals(btn[i].getText())) { // �ؽ�Ʈ�� ��ư�̶� ���ϴ°Ŵϱ� �ؽ�Ʈ�� j�� ��ư�� i�� ���ؾ��Ѵ�
					btn[i].setBgColor(Color.yellow);
					btn[i].setTxtColor(Color.blue);
					nCnt++;
				}
			}
		}
		for (int i = 0; i < MAX_CNT; i++) {
			if (right[i].equals(btn[6].getText())) {
				btn[6].setBgColor(Color.yellow);
				btn[6].setTxtColor(Color.blue);
				bCnt++;
			}
		}

		if (nCnt == 6) {
			rankLabel.setText("�����մϴ� 1���Դϴ�!");
		} else if (nCnt == 5 && bCnt == 1) {
			rankLabel.setText("�����մϴ� 2���Դϴ�!");

		} else if (nCnt == 5) {
			rankLabel.setText("�����մϴ� 3���Դϴ�!");

		} else if (nCnt == 4) {
			rankLabel.setText("�����մϴ� 4���Դϴ�!");

		} else if (nCnt == 3) {
			rankLabel.setText("�����մϴ� 5���Դϴ�!");

		} else {
			rankLabel.setText("���� ��ȸ�� ���������.");
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			// ȸ�� ���� ���Ȯ��
			showResult();
		}

	}

	@Override
	public void keyPressed(KeyEvent e) {

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {

	}

	@Override
	public void mousePressed(MouseEvent arg0) {

	}

	@Override
	public void mouseExited(MouseEvent arg0) {

	}

	@Override
	public void mouseEntered(MouseEvent arg0) {

	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		showResult();

	}

}
