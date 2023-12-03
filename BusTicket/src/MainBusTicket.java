import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

//등록하기탭 클래스 이름 EronllTab
class EnrollTab extends JPanel implements ActionListener{
	
	JPanel mainP = new JPanel();
	
	JPanel datePanel = new JPanel();
	JPanel locationPanel = new JPanel();	
	JPanel timePanel = new JPanel();
	JPanel seatPanel = new JPanel();
	JPanel namePanel = new JPanel();
	JPanel buyPanel = new JPanel();
	JPanel moneyPanel = new JPanel();

	//날짜 정하기	
	JTextField yearField = new JTextField(5);
	JTextField monthField = new JTextField(2);
	JTextField dayField = new JTextField(2);
	JLabel yearLabel = new JLabel("년");
	JLabel monthLabel = new JLabel("월");
	JLabel dayLabel = new JLabel("일");
	
	//출발,도착지 
	JTextField startField = new JTextField(10);
	JTextField arriveField = new JTextField(10);
	JLabel symbolLabel = new JLabel("~");
	JLabel startLabel = new JLabel("출발");
	JLabel arriveLabel = new JLabel("도착");

	//오전, 오후
	JRadioButton[] timeRadio = new JRadioButton[3];
	String[] timeArray = {"오전","오후",""};
	ButtonGroup bg = new ButtonGroup();
	
	//좌석수
	JLabel seatCountLabel = new JLabel("좌석 수 : ");
	JTextField seatCountField = new JTextField(3);
	JLabel seatMoneyLabel = new JLabel("ㆍ1좌석당 요금은 2000원");
	
	//이름
	JLabel nameLabel = new JLabel("이름 : ");
	JTextField nameField = new JTextField(5);
	
	//예매버튼
	JButton buyBtn = new JButton("예매하기");

	//돈
	JTextField totalMoneyField = new JTextField(10);
	JTextField plusMoneyField = new JTextField(10);
	JLabel totalMoneyLabel = new JLabel("거스름돈 : ");
	JLabel textMoneyLabel = new JLabel("<==");
	JButton plusBtn = new JButton("충전하기");
	
	//메세지 출력
	JLabel message = new JLabel();
	
	ArrayList<Passenger> datas = new ArrayList<Passenger>();
	
	String msg = "";
	
	//데이터베이스 연동 클래스 인스턴스 생성
	PassengerDAO S_db = new PassengerDAO();
	
	//학생정보 출력을 위한 학생 인스턴스 생성
	Passenger passenger;
	
	
	public EnrollTab() {	
		//메인패널 배치관리자 등록
		mainP.setLayout(new GridLayout(8,2,10,30));
		
		//날짜
		datePanel.add(yearField);
		datePanel.add(yearLabel);
		datePanel.add(monthField);
		datePanel.add(monthLabel);
		datePanel.add(dayField);
		datePanel.add(dayLabel);
		
		//출발지, 도착지 
		locationPanel.add(startLabel);
		locationPanel.add(startField);
		locationPanel.add(symbolLabel);
		locationPanel.add(arriveLabel);
		locationPanel.add(arriveField);
		
		//오전 오후 정하기
		for(int i=0; i<timeRadio.length; i++) {
			timeRadio[i] = new JRadioButton(timeArray[i]);
			bg.add(timeRadio[i]);
			if(i<=1) {	//숨겨진 버튼은 보이지 않게 함
				timePanel.add(timeRadio[i]);
			}
		}
		
		//좌석 수
		seatPanel.add(seatCountLabel);
		seatPanel.add(seatCountField);
		seatPanel.add(seatMoneyLabel);
		
		
		//이름 적기
		namePanel.add(nameLabel);
		namePanel.add(nameField);
		
		//예매 버튼
		buyPanel.add(buyBtn);
		
		//예매버튼 이벤트 등록
		buyBtn.addActionListener(this);

		//돈
		totalMoneyField.setEditable(false);
		totalMoneyField.setText("0");
		plusMoneyField.setText("0");
		moneyPanel.add(totalMoneyLabel);
		moneyPanel.add(totalMoneyField);
		moneyPanel.add(textMoneyLabel);
		moneyPanel.add(plusMoneyField);
		moneyPanel.add(plusBtn);
		
		plusBtn.addActionListener(this);
		
		//메인패널에 패널들 넣기
		mainP.add(datePanel);
		mainP.add(locationPanel);
		mainP.add(timePanel);
		mainP.add(seatPanel);
		mainP.add(namePanel);
		mainP.add(buyPanel);
		mainP.add(moneyPanel);
		mainP.add(message);
		
		add(mainP);
	}
	
	public void clearField() {
		yearField.setText("");
		monthField.setText("");
		dayField.setText("");
		startField.setText("");
		arriveField.setText("");
		timeRadio[2].setSelected(true);
		seatCountField.setText("");
		nameField.setText("");
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		int totalmoney = Integer.parseInt(totalMoneyField.getText());	//현재 잔고
		
		//예매버튼을 누르면 정보를 DB에 저장
		if(obj == buyBtn) {
			//잘못된 문자있는지 확인
			try {
				int year = Integer.parseInt(yearField.getText());	//년도
				int month =Integer.parseInt(monthField.getText());	//월
				int day = Integer.parseInt(dayField.getText());	//일
				int date = Integer.parseInt(yearField.getText() + monthField.getText() + dayField.getText());	//날짜
				int seatCount = Integer.parseInt(seatCountField.getText());
				String start = startField.getText();	//출발지 변수
				String arrive = arriveField.getText();	//도착지 변수
				String time = "";
				String name = nameField.getText();
				for(int i=0; i<timeRadio.length; i++) {
					if(timeRadio[i].isSelected()) {
						time = timeRadio[i].getText();	//시간
					}
				}
				//요금은 좌석당 2000원
				if(totalmoney >= 2000*Integer.parseInt(seatCountField.getText())){
					//년도는 1000을 넘겨야하고 / 월은 1과 12사이 / 일은 1과 31사이
					if(1000<=year && year<10000 && 1<=month && month<=12 && 1<=day && day<=31) {
						//정보에 누락이 있다면 메세지로 알려줌
						if(start.isEmpty() || arrive.isEmpty() || time=="" || name.isEmpty()) {
							message.setText(msg + "버스표 예매에 필요한 정보가 누락되어있습니다");
						}
						else {
							passenger = new Passenger();	
							passenger.setName(name);	//이름
							//월,일이 한자리 수면 앞에 0을 붙여줌 (예 : 2020.1.1 => 2020.01.01)
							if(monthField.getText().length() < 2 && dayField.getText().length() < 2) {
							date = Integer.parseInt(year + "0" + month + "0" + day);
							}
							else if(monthField.getText().length() < 2) {
								date = Integer.parseInt(year + "0" + month + day);
							}
							else if(dayField.getText().length() < 2) {
								date = Integer.parseInt(year + month + "0" + day);
							}
							passenger.setDate(date);
							//passenger.setYear(year);	//년도
							//passenger.setMonth(month);	//월
							//passenger.setDay(day);	//일
							passenger.setTime(time);	//시간
							passenger.setStart(start);	//출발
							passenger.setArrive(arrive);	//도착
							passenger.setSeatCount(seatCount);	//좌석수
							//데이터베이스에 등록
							if(S_db.insertPassenger(passenger)) {
								message.setText(msg + "예매되었습니다.");
								totalMoneyField.setText(String.valueOf(totalmoney - 2000*passenger.getSeatCount() ));//현재잔고에서 빼준다
								clearField();
							}
							else {
								message.setText(msg + "예매에 실패했습니다.");
							}
						}
					}
					else {
						message.setText(msg + "알맞은 날짜를 기입하여주십시오.");
					}
				}					
				else{
					message.setText(msg + "현재 금액이 부족합니다.");
				}		
			}
			catch(Exception ae) {
				message.setText(msg + "정보입력칸에 잘못된 문자가 입력되었습니다.");
			}
		}
	
		//돈 충전 버튼
		else if(obj == plusBtn) {
			try {

			}
			catch(Exception ae) {
				message.setText("오류");
			}
			try {
				int plusmoney = Integer.parseInt(plusMoneyField.getText());	//충전할 돈
				//충전할 돈이 0을 이상이어야 됨
				if(plusmoney >= 0 ) {
					totalmoney = totalmoney + plusmoney;	//현재 잔고에 추가
					totalMoneyField.setText(String.valueOf(totalmoney));	
					plusMoneyField.setText("0");
				}
				else {
					message.setText(msg + "알맞은 금액을 입력하여 주십시오");	//음수를 넣으면 오류 출력
				}	
			}
			catch(Exception ae) {
				message.setText(msg + "정상적인 금액을 입력하여 주십시오");
			}
		}	
	}
}



//조회 탭---------------------------------------------------
class LookTab extends JPanel implements ActionListener{
	// 판넬 생성
	JPanel main = new JPanel();
	JPanel jp1 = new JPanel();
	JPanel jp2 = new JPanel();
	JPanel combopanel = new JPanel();
	JPanel jp3 = new JPanel();
	JPanel insertPanel1 = new JPanel();
	JPanel insertPanel2 = new JPanel();
	JPanel insertPanel = new JPanel();
	
	JPanel datePanel = new JPanel();
	JPanel locationPanel = new JPanel();	
	JPanel timePanel = new JPanel();
	JPanel seatPanel = new JPanel();
	JPanel namePanel = new JPanel();
	JPanel buyPanel = new JPanel();
	JPanel moneyPanel = new JPanel();
	
	
	// 제일 위 조회하기 라벨
	JLabel jl = new JLabel("하고 싶은 행동 선택");
	JLabel jl2 = new JLabel("고객 번호");
	//결과창 TextArea
	JTextArea resultArea = new JTextArea(15,55);
	JScrollPane scroll = new JScrollPane (resultArea,
	JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	
	//고객번호를 관리할 콤보박스 생성
	JComboBox code_combobox;
	
	ArrayList<Passenger> datas = new ArrayList<Passenger>();
	
	String msg = "";
	
	//날짜 정하기	
	JTextField yearField = new JTextField(5);
	JTextField monthField = new JTextField(2);
	JTextField dayField = new JTextField(2);
	JLabel yearLabel = new JLabel("년");
	JLabel monthLabel = new JLabel("월");
	JLabel dayLabel = new JLabel("일");
		
	//출발,도착지 
	JTextField startField = new JTextField(10);
	JTextField arriveField = new JTextField(10);
	JLabel symbolLabel = new JLabel("~");
	JLabel startLabel = new JLabel("출발");
	JLabel arriveLabel = new JLabel("도착");

	JRadioButton[] timeRadio = new JRadioButton[3];
	String[] timeArray = {"오전","오후",""};
	ButtonGroup bg = new ButtonGroup();
	
	//좌석수
	JLabel seatCountLabel = new JLabel("좌석 수 : ");
	JTextField seatCountField = new JTextField(3);
	
	//이름
	JLabel nameLabel = new JLabel("이름 : ");
	JTextField nameField = new JTextField(5);
	
	//데이터베이스 연동 클래스 인스턴스 생성
	PassengerDAO S_db = new PassengerDAO();
	
	//학생정보 출력을 위한 학생 인스턴스 생성
	Passenger passenger;
	
	
	//조회 수정 삭제 버튼
	JButton button1 = new JButton(" 조회하기 ");
	JButton button2 = new JButton(" 수정하기 ");
	JButton button3 = new JButton(" 반환하기 ");
	
	public LookTab() {
		// 메인 판넬의 Layout
		main.setLayout(new BorderLayout(20,20));
		jp2.setLayout(new BorderLayout(20,20));
		insertPanel.setLayout(new GridLayout(2,0,5,5));
		// 콤보박스 초기화
		code_combobox = new JComboBox();
		
		//날짜
		datePanel.add(yearField);
		datePanel.add(yearLabel);
		datePanel.add(monthField);
		datePanel.add(monthLabel);
		datePanel.add(dayField);
		datePanel.add(dayLabel);
				
		//출발지, 도착지 
		locationPanel.add(startLabel);
		locationPanel.add(startField);
		locationPanel.add(symbolLabel);
		locationPanel.add(arriveLabel);
		locationPanel.add(arriveField);
		
		//오전 오후 정하기
		for(int i=0; i<timeRadio.length; i++) {
			timeRadio[i] = new JRadioButton(timeArray[i]);
			bg.add(timeRadio[i]);
			if(i<=1) {	//숨겨진 버튼은 보이지 않게 함
				timePanel.add(timeRadio[i]);
			}
		}
				
		//좌석 수
		seatPanel.add(seatCountLabel);
		seatPanel.add(seatCountField);
				
		// 이름 적기
		namePanel.add(nameLabel);
		namePanel.add(nameField);
		
		// 텍스트 필드들을 판넬에 넣기
		insertPanel1.add(datePanel);
		insertPanel1.add(locationPanel);
		insertPanel2.add(timePanel);
		insertPanel2.add(seatPanel);
		insertPanel2.add(namePanel);
		insertPanel.add(insertPanel1);
		insertPanel.add(insertPanel2);
		
		// 판넬에 요소들 담기
		jp1.add(jl);
		combopanel.add(jl2);
		combopanel.add(code_combobox);
		jp2.add(combopanel,BorderLayout.NORTH);
		jp2.add(scroll,BorderLayout.CENTER);
		jp2.add(insertPanel,BorderLayout.SOUTH);
		jp3.add(button1);
		jp3.add(button2);
		jp3.add(button3);
		
		// 메인 판넬에 판넬들 추가
		main.add(jp1,BorderLayout.NORTH);
		main.add(jp2,BorderLayout.CENTER);
		main.add(jp3,BorderLayout.SOUTH);
		
		add(main);
		
		// 버튼에 ActionListener 추가
		button1.addActionListener(this);
		button2.addActionListener(this);
		button3.addActionListener(this);
		refreshData();
		
	}
	public void clearField() {
		yearField.setText("");
		monthField.setText("");
		dayField.setText("");
		timeRadio[2].setSelected(true);
		startField.setText("");
		arriveField.setText("");
		seatCountField.setText("");
		nameField.setText("");
	}
	
	//전체 데이터 출력
	public void refreshData() {
		resultArea.setText("\n\n");
		
		resultArea.append("고객번호\t이름\t날짜\t시간\t출발지\t도착지\t좌석 수\n");
		datas = S_db.allPassenger();
		
		//데이터 변경시 콤보박스 데이터 갱신
		code_combobox.setModel(new DefaultComboBoxModel(S_db.getItems()));
		
		if(datas != null) {
			//ArrayList의 전체 데이터를 형식에 맞춰 출력
			for(Passenger p : datas) {
				StringBuffer sb = new StringBuffer();
				sb.append(p.getNum() + "\t");
				sb.append(p.getName() + "\t");
				sb.append(p.getDate() + "\t");
				sb.append(p.getTime() + "\t");
				sb.append(p.getStart() + "\t");
				sb.append(p.getArrive() + "\t");
				sb.append(p.getSeatCount() + "\n");
				
				resultArea.append(sb.toString());
			}
		}	
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();	//객체의 변수값을 가져오는 거인듯

 		//조회 버튼을 클릭한 경우
 		if(obj == button1) {
 			String s = (String)code_combobox.getSelectedItem();
 			if(!s.contentEquals("고객번호 선택")) {
 				passenger = S_db.selectPassenger(Integer.parseInt(s));
 				if(passenger != null) {
 					jl.setText(msg + "고객님의 예매 정보를 가져왔습니다!");
 					yearField.setText(String.valueOf(passenger.getDate()).substring(0,4));
 					monthField.setText(String.valueOf(passenger.getDate()).substring(4,6));
 					dayField.setText(String.valueOf(passenger.getDate()).substring(6));
 					for(int i=0; i<timeRadio.length; i++) {
						if(passenger.getTime() == timeRadio[i].getText()) {
							timeRadio[i].setSelected(true);	//시간
						}
					}
 					startField.setText(passenger.getStart());
 					arriveField.setText(passenger.getArrive());
 					seatCountField.setText(String.valueOf(passenger.getSeatCount()));
 					nameField.setText(passenger.getName());
 				}
 				else {
 					jl.setText(msg + "고객 번호가 검색되지 않았습니다!");
 				}
 			}
 			else{
 				//결과 데이터 출력
 				refreshData();
 			}
 		}
 		//수정 버튼을 클릭한 경우
 		else if(obj == button2) {
 			//----------------------------------------------
 			//잘못된 문자있는지 확인
			try {
				int year = Integer.parseInt(yearField.getText());	//년도
				int month =Integer.parseInt(monthField.getText());	//월
				int day = Integer.parseInt(dayField.getText());	//일
				int date = Integer.parseInt(yearField.getText() + monthField.getText() + dayField.getText());	//날짜
				int seatCount = Integer.parseInt(seatCountField.getText());
				String start = startField.getText();	//출발지 변수
				String arrive = arriveField.getText();	//도착지 변수
				String time = "";
				String name = nameField.getText();
				for(int i=0; i<timeRadio.length; i++) {
					if(timeRadio[i].isSelected()) {
						time = timeRadio[i].getText();	//시간
					}
				}			
				System.out.println("asdf");
				//년도는 1000을 넘겨야하고 / 월은 1과 12사이 / 일은 1과 31사이
				if(1000<=year && year<10000 && 1<=month && month<=12 && 1<=day && day<=31) {
					//정보에 누락이 있다면 메세지로 알려줌
					if(start.isEmpty() || arrive.isEmpty() || time=="" || name.isEmpty()) {
						jl.setText(msg + "예매수정에 필요한 정보가 누락되어있습니다");
					}
					else {
						passenger = new Passenger();	
						passenger.setName(name);	//이름
						//월,일이 한자리 수면 앞에 0을 붙여줌 (예 : 2020.1.1 => 2020.01.01)
						if(monthField.getText().length() < 2 && dayField.getText().length() < 2) {
							date = Integer.parseInt(year + "0" + month + "0" + day);
						}
						else if(monthField.getText().length() < 2) {
							date = Integer.parseInt(year + "0" + month + day);
						}
						else if(dayField.getText().length() < 2) {
							date = Integer.parseInt(year + month + "0" + day);
						}
						passenger.setDate(date);
						//passenger.setYear(year);	//년도
						//passenger.setMonth(month);	//월
						//passenger.setDay(day);	//일
						passenger.setTime(time);	//시간
						passenger.setStart(start);	//출발
						passenger.setArrive(arrive);	//도착
						passenger.setSeatCount(seatCount);	//좌석수
						passenger.setNum(Integer.parseInt((String)code_combobox.getSelectedItem()));
						//데이터베이스에 등록
						if(S_db.updatepassenger(passenger)) {
							jl.setText(msg + "예매 정보를 수정했습니다!");
							clearField();
						}
						else {
							jl.setText(msg + "예매 정보 수정에 실패했습니다.");
						}
					}
				}
				else {
					jl.setText(msg + "알맞은 날짜를 기입하여주십시오.");
				}
				//결과데이터 출력
				refreshData();				
			}
			catch(Exception ae) {
				jl.setText(msg + "정보수정입력칸에 잘못된 문자가 입력되었습니다.");
			}
 		}	
 			
 			//---------------------------------------
			/*
 			int year = Integer.parseInt(yearField.getText());	//년도
			int month =Integer.parseInt(monthField.getText());	//월
			int day = Integer.parseInt(dayField.getText());	//일
			int date = Integer.parseInt(yearField.getText() + monthField.getText() + dayField.getText());	//날짜
			if(1000<=year && year<10000 && 1<=month && month<=12 && 1<=day && day<=31) {
				passenger = new Passenger();
				passenger.setName(nameField.getText());	//이름
				//월,일이 한자리 수면 앞에 0을 붙여줌 (예 : 2020.1.1 => 2020.01.01)
				if(monthField.getText().length() < 2 && dayField.getText().length() < 2) {
					date = Integer.parseInt(yearField.getText() + "0" + monthField.getText() + "0" + dayField.getText());
				}
				else if(monthField.getText().length() < 2) {
					date = Integer.parseInt(yearField.getText() + "0" + monthField.getText() + dayField.getText());
					}
				else if(dayField.getText().length() < 2) {
					date = Integer.parseInt(yearField.getText() + monthField.getText() + "0" + dayField.getText());
					}
				passenger.setDate(date);
				//passenger.setYear(year);	//년도
				//passenger.setMonth(month);	//월
				//passenger.setDay(day);	//일
				for(int i=0; i<timeRadio.length; i++) {
					if(timeRadio[i].isSelected()) {
						passenger.setTime(timeRadio[i].getText());	//시간
					}
				}
				passenger.setStart(startField.getText());	//출발
				passenger.setArrive(arriveField.getText());	//도착
				passenger.setSeatCount(Integer.parseInt(seatCountField.getText()));	//좌석수
				passenger.setNum(Integer.parseInt((String)code_combobox.getSelectedItem()));
				if(S_db.updatepassenger(passenger)) {
					jl.setText(msg + "예매 정보를 수정했습니다!");
				}
				else {
					jl.setText(msg + "예매 정보 수정에 실패 했습니다!");
				}
				clearField();
				
			}
			else {
				jl.setText(msg + "알맞은 날짜를 기입하여주십시오.");
			}
			//결과데이터 출력
			refreshData();
 		}
		*/
			
 		//삭제 버튼을 클릭한 경우
 		else if(obj == button3) {
 			String s = (String)code_combobox.getSelectedItem();
 			
 			if(s.contentEquals("전체")) {
 				jl.setText(msg + "전체 삭제는 되지 않습니다!");
 			}
 			else {
 				if(S_db.deletepassenger(Integer.parseInt(s))) {
 					jl.setText(msg + "표가 정상적으로 반환되었습니다!");
 					clearField();
 				}
 				else {
 					jl.setText(msg + "표가 정상적으로 반환되지않았습니다!");
 				}
 			}
 			//결과 데이터 출력
 			refreshData();
 		}
	}
}

class MainTab extends JFrame{
	public MainTab() {
		JTabbedPane jtab = new JTabbedPane();
		
		EnrollTab etab = new EnrollTab(); 
		LookTab ltab = new LookTab();
		
		jtab.addTab("등록하기",etab);
		jtab.addTab("조회하기", ltab);
		
		getContentPane().add(jtab);
		
		setVisible(true);
		setTitle("BusTicketing");
		setSize(600,650);
	}
}

public class MainBusTicket {
	public static void main(String[] args) {
		new MainTab();
	}
}