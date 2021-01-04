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
//"totSellamnt":59292005000,  // 당첨금
//"returnValue":"success",   // 회차 여부
//"drwNoDate":"2005-01-22",   // 추첨일
//"firstWinamnt":1567271167,
//"drwtNo6":42,
//"drwtNo4":33,
//"firstPrzwnerCo":9,      // 1등 당첨자 명수
//"drwtNo5":41,
//"bnusNo":43,
//"firstAccumamnt":0,     // 1등 당첨금
//"drwNo":112,    // 회차
//"drwtNo2":29,  // 2번쨰 당첨번호
//"drwtNo3":30,   // 3번쨰 당첨번호
//"drwtNo1":26  // 1번쨰 당첨번호
//}

public class Lotto extends JFrame implements MouseListener, KeyListener {
	final int WIDTH_BTN = 60;
	final int WIDTH_GAP = 15;
	private final int MAX_CNT = 6;
	private final int MAX_CNT_W_BN = 7;
	// 로또 당첨 번호 표시 - 버튼 : label이 불편하여 사용함. 동글라미 그려서 색칠하는 형태.
	// 6+1
	private MyButton[] btn = new MyButton[MAX_CNT_W_BN];

	// 로또 당첨 확인할 번호 입력 - JTextField
	private JTextField[] txt = new JTextField[MAX_CNT];

	// 회차 입력 - JTextField
	private JTextField turnTxt = new JTextField();
	// 회차확인 - JButton
	private JButton checkBtn = new JButton("확인");

	// 정보 JLabel
	private JLabel infoLbl = new JLabel();

	// 순위 문구
	JLabel rankLabel = new JLabel("");
// 회차 정보, 경고 문구 표시할 라벨
	JLabel turnLabel = new JLabel("");

	public Lotto() {
		super("로또확인 프로그램");
		// 처음 화면을 구성함.
		init();

		// 이벤트 등록
		event();

		// 화면에 보여주기
		setSize(600, 500);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	private void init() {
		int w = 30; // 초기 시작하는 x위치
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
//			txt[i].setBounds(w, 120, WIDTH_BTN, WIDTH_BTN); // 위치 설정
			txt[i].setHorizontalAlignment(JTextField.CENTER); // 커서 중앙정렬
//			w += WIDTH_BTN + WIDTH_GAP;
			c.add(txt[i]); // 추가

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
		checkBtn.addMouseListener(this); // this - 현재 동작하고 있는 이 객체
		turnTxt.addKeyListener(this);
	}

	private void showResult() {
		super.setTitle("결과화면보기");
		try {
			Integer.parseInt(turnTxt.getText());
		} catch (Exception e) {
			turnLabel.setText("찾는 회차의 숫자를 입력해주세요");
			turnTxt.setText("");
			return;
		}
		turnLabel.setText("");
		JsonReader jr = new JsonReader();
		JSONObject jo = jr.connectionUrlToJson(turnTxt.getText());
		String[] right = new String[MAX_CNT]; // 로또번호 맞는지 입력한 것 저장
		int nCnt = 0; // 맞는 숫자 개수
		int bCnt = 0; // 맞는 숫자 개수(보너스 번호. 2등)
		if (jo == null) {
			infoLbl.setText("찾는 회차의 정보가 없습니다.");
			turnTxt.setText("");
			return;
		}
		if (jo.get("returnValue").equals("fail")) {
			infoLbl.setText(turnTxt.getText() + "찾는 회차의 정보가 없습니다.");
			turnTxt.setText("");
			return;
		}
		infoLbl.setText(turnTxt.getText() + "회차의 정보입니다.");
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
		// 로또 번호 창에 숫자 말고 다른 문자 입력했을 떄
		for (int i = 0; i < MAX_CNT; i++) {
			if (txt[i].getText().equals("")) { // 로또 번호 쓰지 않고 회차만 적었을 때
				try {
					right[i] = txt[i].getText();
					Integer.parseInt(right[i]);
				} catch (Exception e) {
					turnLabel.setText("");
					return; // return쓰면 아예 메소드를 나가버린다.
				}
			}
			if (!txt[i].getText().equals("")) { // 로또 번호에 숫자 말고 문자 입력했을 때
				try {
					right[i] = txt[i].getText();
					Integer.parseInt(right[i]);
				} catch (Exception e) {
					turnLabel.setText("숫자만 입력해주세요");
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

		// 로또 번호 범위
		if (Integer.parseInt(txt[0].getText()) < 1 || Integer.parseInt(txt[0].getText()) > 45
				|| Integer.parseInt(txt[1].getText()) < 1 || Integer.parseInt(txt[1].getText()) > 45
				|| Integer.parseInt(txt[2].getText()) < 1 || Integer.parseInt(txt[2].getText()) > 45
				|| Integer.parseInt(txt[3].getText()) < 1 || Integer.parseInt(txt[3].getText()) > 45
				|| Integer.parseInt(txt[4].getText()) < 1 || Integer.parseInt(txt[4].getText()) > 45
				|| Integer.parseInt(txt[5].getText()) < 1 || Integer.parseInt(txt[5].getText()) > 45) {
			turnLabel.setText("로또 번호의 범위는 1~45 입니다.");
			return;
		}

		// 중복 입력 방지
		ArrayList<Integer> checkList = new ArrayList<Integer>();
		for (int i = 0; i < txt.length; i++) {
			checkList.add(Integer.parseInt(txt[i].getText()));
		}

		HashSet<Integer> hash = new HashSet<Integer>(checkList);
		ArrayList<Integer> checkedList = new ArrayList<Integer>(hash);
		if (checkedList.size() < 6) {
			turnLabel.setText("중복 입력된 로또 번호가 있습니다.");
			return;
		}

		for (int i = 0; i < MAX_CNT; i++) {
			for (int j = 0; j < right.length; j++) {
				if (right[j].equals(btn[i].getText())) { // 텍스트랑 버튼이랑 비교하는거니까 텍스트의 j와 버튼의 i를 비교해야한다
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
			rankLabel.setText("축하합니다 1등입니다!");
		} else if (nCnt == 5 && bCnt == 1) {
			rankLabel.setText("축하합니다 2등입니다!");

		} else if (nCnt == 5) {
			rankLabel.setText("축하합니다 3등입니다!");

		} else if (nCnt == 4) {
			rankLabel.setText("축하합니다 4등입니다!");

		} else if (nCnt == 3) {
			rankLabel.setText("축하합니다 5등입니다!");

		} else {
			rankLabel.setText("다음 기회를 노려보세요.");
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			// 회차 정보 결과확인
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
