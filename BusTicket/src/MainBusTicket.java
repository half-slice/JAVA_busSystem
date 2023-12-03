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

//����ϱ��� Ŭ���� �̸� EronllTab
class EnrollTab extends JPanel implements ActionListener{
	
	JPanel mainP = new JPanel();
	
	JPanel datePanel = new JPanel();
	JPanel locationPanel = new JPanel();	
	JPanel timePanel = new JPanel();
	JPanel seatPanel = new JPanel();
	JPanel namePanel = new JPanel();
	JPanel buyPanel = new JPanel();
	JPanel moneyPanel = new JPanel();

	//��¥ ���ϱ�	
	JTextField yearField = new JTextField(5);
	JTextField monthField = new JTextField(2);
	JTextField dayField = new JTextField(2);
	JLabel yearLabel = new JLabel("��");
	JLabel monthLabel = new JLabel("��");
	JLabel dayLabel = new JLabel("��");
	
	//���,������ 
	JTextField startField = new JTextField(10);
	JTextField arriveField = new JTextField(10);
	JLabel symbolLabel = new JLabel("~");
	JLabel startLabel = new JLabel("���");
	JLabel arriveLabel = new JLabel("����");

	//����, ����
	JRadioButton[] timeRadio = new JRadioButton[3];
	String[] timeArray = {"����","����",""};
	ButtonGroup bg = new ButtonGroup();
	
	//�¼���
	JLabel seatCountLabel = new JLabel("�¼� �� : ");
	JTextField seatCountField = new JTextField(3);
	JLabel seatMoneyLabel = new JLabel("��1�¼��� ����� 2000��");
	
	//�̸�
	JLabel nameLabel = new JLabel("�̸� : ");
	JTextField nameField = new JTextField(5);
	
	//���Ź�ư
	JButton buyBtn = new JButton("�����ϱ�");

	//��
	JTextField totalMoneyField = new JTextField(10);
	JTextField plusMoneyField = new JTextField(10);
	JLabel totalMoneyLabel = new JLabel("�Ž����� : ");
	JLabel textMoneyLabel = new JLabel("<==");
	JButton plusBtn = new JButton("�����ϱ�");
	
	//�޼��� ���
	JLabel message = new JLabel();
	
	ArrayList<Passenger> datas = new ArrayList<Passenger>();
	
	String msg = "";
	
	//�����ͺ��̽� ���� Ŭ���� �ν��Ͻ� ����
	PassengerDAO S_db = new PassengerDAO();
	
	//�л����� ����� ���� �л� �ν��Ͻ� ����
	Passenger passenger;
	
	
	public EnrollTab() {	
		//�����г� ��ġ������ ���
		mainP.setLayout(new GridLayout(8,2,10,30));
		
		//��¥
		datePanel.add(yearField);
		datePanel.add(yearLabel);
		datePanel.add(monthField);
		datePanel.add(monthLabel);
		datePanel.add(dayField);
		datePanel.add(dayLabel);
		
		//�����, ������ 
		locationPanel.add(startLabel);
		locationPanel.add(startField);
		locationPanel.add(symbolLabel);
		locationPanel.add(arriveLabel);
		locationPanel.add(arriveField);
		
		//���� ���� ���ϱ�
		for(int i=0; i<timeRadio.length; i++) {
			timeRadio[i] = new JRadioButton(timeArray[i]);
			bg.add(timeRadio[i]);
			if(i<=1) {	//������ ��ư�� ������ �ʰ� ��
				timePanel.add(timeRadio[i]);
			}
		}
		
		//�¼� ��
		seatPanel.add(seatCountLabel);
		seatPanel.add(seatCountField);
		seatPanel.add(seatMoneyLabel);
		
		
		//�̸� ����
		namePanel.add(nameLabel);
		namePanel.add(nameField);
		
		//���� ��ư
		buyPanel.add(buyBtn);
		
		//���Ź�ư �̺�Ʈ ���
		buyBtn.addActionListener(this);

		//��
		totalMoneyField.setEditable(false);
		totalMoneyField.setText("0");
		plusMoneyField.setText("0");
		moneyPanel.add(totalMoneyLabel);
		moneyPanel.add(totalMoneyField);
		moneyPanel.add(textMoneyLabel);
		moneyPanel.add(plusMoneyField);
		moneyPanel.add(plusBtn);
		
		plusBtn.addActionListener(this);
		
		//�����гο� �гε� �ֱ�
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
		int totalmoney = Integer.parseInt(totalMoneyField.getText());	//���� �ܰ�
		
		//���Ź�ư�� ������ ������ DB�� ����
		if(obj == buyBtn) {
			//�߸��� �����ִ��� Ȯ��
			try {
				int year = Integer.parseInt(yearField.getText());	//�⵵
				int month =Integer.parseInt(monthField.getText());	//��
				int day = Integer.parseInt(dayField.getText());	//��
				int date = Integer.parseInt(yearField.getText() + monthField.getText() + dayField.getText());	//��¥
				int seatCount = Integer.parseInt(seatCountField.getText());
				String start = startField.getText();	//����� ����
				String arrive = arriveField.getText();	//������ ����
				String time = "";
				String name = nameField.getText();
				for(int i=0; i<timeRadio.length; i++) {
					if(timeRadio[i].isSelected()) {
						time = timeRadio[i].getText();	//�ð�
					}
				}
				//����� �¼��� 2000��
				if(totalmoney >= 2000*Integer.parseInt(seatCountField.getText())){
					//�⵵�� 1000�� �Ѱܾ��ϰ� / ���� 1�� 12���� / ���� 1�� 31����
					if(1000<=year && year<10000 && 1<=month && month<=12 && 1<=day && day<=31) {
						//������ ������ �ִٸ� �޼����� �˷���
						if(start.isEmpty() || arrive.isEmpty() || time=="" || name.isEmpty()) {
							message.setText(msg + "����ǥ ���ſ� �ʿ��� ������ �����Ǿ��ֽ��ϴ�");
						}
						else {
							passenger = new Passenger();	
							passenger.setName(name);	//�̸�
							//��,���� ���ڸ� ���� �տ� 0�� �ٿ��� (�� : 2020.1.1 => 2020.01.01)
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
							//passenger.setYear(year);	//�⵵
							//passenger.setMonth(month);	//��
							//passenger.setDay(day);	//��
							passenger.setTime(time);	//�ð�
							passenger.setStart(start);	//���
							passenger.setArrive(arrive);	//����
							passenger.setSeatCount(seatCount);	//�¼���
							//�����ͺ��̽��� ���
							if(S_db.insertPassenger(passenger)) {
								message.setText(msg + "���ŵǾ����ϴ�.");
								totalMoneyField.setText(String.valueOf(totalmoney - 2000*passenger.getSeatCount() ));//�����ܰ��� ���ش�
								clearField();
							}
							else {
								message.setText(msg + "���ſ� �����߽��ϴ�.");
							}
						}
					}
					else {
						message.setText(msg + "�˸��� ��¥�� �����Ͽ��ֽʽÿ�.");
					}
				}					
				else{
					message.setText(msg + "���� �ݾ��� �����մϴ�.");
				}		
			}
			catch(Exception ae) {
				message.setText(msg + "�����Է�ĭ�� �߸��� ���ڰ� �ԷµǾ����ϴ�.");
			}
		}
	
		//�� ���� ��ư
		else if(obj == plusBtn) {
			try {

			}
			catch(Exception ae) {
				message.setText("����");
			}
			try {
				int plusmoney = Integer.parseInt(plusMoneyField.getText());	//������ ��
				//������ ���� 0�� �̻��̾�� ��
				if(plusmoney >= 0 ) {
					totalmoney = totalmoney + plusmoney;	//���� �ܰ� �߰�
					totalMoneyField.setText(String.valueOf(totalmoney));	
					plusMoneyField.setText("0");
				}
				else {
					message.setText(msg + "�˸��� �ݾ��� �Է��Ͽ� �ֽʽÿ�");	//������ ������ ���� ���
				}	
			}
			catch(Exception ae) {
				message.setText(msg + "�������� �ݾ��� �Է��Ͽ� �ֽʽÿ�");
			}
		}	
	}
}



//��ȸ ��---------------------------------------------------
class LookTab extends JPanel implements ActionListener{
	// �ǳ� ����
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
	
	
	// ���� �� ��ȸ�ϱ� ��
	JLabel jl = new JLabel("�ϰ� ���� �ൿ ����");
	JLabel jl2 = new JLabel("�� ��ȣ");
	//���â TextArea
	JTextArea resultArea = new JTextArea(15,55);
	JScrollPane scroll = new JScrollPane (resultArea,
	JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	
	//����ȣ�� ������ �޺��ڽ� ����
	JComboBox code_combobox;
	
	ArrayList<Passenger> datas = new ArrayList<Passenger>();
	
	String msg = "";
	
	//��¥ ���ϱ�	
	JTextField yearField = new JTextField(5);
	JTextField monthField = new JTextField(2);
	JTextField dayField = new JTextField(2);
	JLabel yearLabel = new JLabel("��");
	JLabel monthLabel = new JLabel("��");
	JLabel dayLabel = new JLabel("��");
		
	//���,������ 
	JTextField startField = new JTextField(10);
	JTextField arriveField = new JTextField(10);
	JLabel symbolLabel = new JLabel("~");
	JLabel startLabel = new JLabel("���");
	JLabel arriveLabel = new JLabel("����");

	JRadioButton[] timeRadio = new JRadioButton[3];
	String[] timeArray = {"����","����",""};
	ButtonGroup bg = new ButtonGroup();
	
	//�¼���
	JLabel seatCountLabel = new JLabel("�¼� �� : ");
	JTextField seatCountField = new JTextField(3);
	
	//�̸�
	JLabel nameLabel = new JLabel("�̸� : ");
	JTextField nameField = new JTextField(5);
	
	//�����ͺ��̽� ���� Ŭ���� �ν��Ͻ� ����
	PassengerDAO S_db = new PassengerDAO();
	
	//�л����� ����� ���� �л� �ν��Ͻ� ����
	Passenger passenger;
	
	
	//��ȸ ���� ���� ��ư
	JButton button1 = new JButton(" ��ȸ�ϱ� ");
	JButton button2 = new JButton(" �����ϱ� ");
	JButton button3 = new JButton(" ��ȯ�ϱ� ");
	
	public LookTab() {
		// ���� �ǳ��� Layout
		main.setLayout(new BorderLayout(20,20));
		jp2.setLayout(new BorderLayout(20,20));
		insertPanel.setLayout(new GridLayout(2,0,5,5));
		// �޺��ڽ� �ʱ�ȭ
		code_combobox = new JComboBox();
		
		//��¥
		datePanel.add(yearField);
		datePanel.add(yearLabel);
		datePanel.add(monthField);
		datePanel.add(monthLabel);
		datePanel.add(dayField);
		datePanel.add(dayLabel);
				
		//�����, ������ 
		locationPanel.add(startLabel);
		locationPanel.add(startField);
		locationPanel.add(symbolLabel);
		locationPanel.add(arriveLabel);
		locationPanel.add(arriveField);
		
		//���� ���� ���ϱ�
		for(int i=0; i<timeRadio.length; i++) {
			timeRadio[i] = new JRadioButton(timeArray[i]);
			bg.add(timeRadio[i]);
			if(i<=1) {	//������ ��ư�� ������ �ʰ� ��
				timePanel.add(timeRadio[i]);
			}
		}
				
		//�¼� ��
		seatPanel.add(seatCountLabel);
		seatPanel.add(seatCountField);
				
		// �̸� ����
		namePanel.add(nameLabel);
		namePanel.add(nameField);
		
		// �ؽ�Ʈ �ʵ���� �ǳڿ� �ֱ�
		insertPanel1.add(datePanel);
		insertPanel1.add(locationPanel);
		insertPanel2.add(timePanel);
		insertPanel2.add(seatPanel);
		insertPanel2.add(namePanel);
		insertPanel.add(insertPanel1);
		insertPanel.add(insertPanel2);
		
		// �ǳڿ� ��ҵ� ���
		jp1.add(jl);
		combopanel.add(jl2);
		combopanel.add(code_combobox);
		jp2.add(combopanel,BorderLayout.NORTH);
		jp2.add(scroll,BorderLayout.CENTER);
		jp2.add(insertPanel,BorderLayout.SOUTH);
		jp3.add(button1);
		jp3.add(button2);
		jp3.add(button3);
		
		// ���� �ǳڿ� �ǳڵ� �߰�
		main.add(jp1,BorderLayout.NORTH);
		main.add(jp2,BorderLayout.CENTER);
		main.add(jp3,BorderLayout.SOUTH);
		
		add(main);
		
		// ��ư�� ActionListener �߰�
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
	
	//��ü ������ ���
	public void refreshData() {
		resultArea.setText("\n\n");
		
		resultArea.append("����ȣ\t�̸�\t��¥\t�ð�\t�����\t������\t�¼� ��\n");
		datas = S_db.allPassenger();
		
		//������ ����� �޺��ڽ� ������ ����
		code_combobox.setModel(new DefaultComboBoxModel(S_db.getItems()));
		
		if(datas != null) {
			//ArrayList�� ��ü �����͸� ���Ŀ� ���� ���
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
		Object obj = e.getSource();	//��ü�� �������� �������� ���ε�

 		//��ȸ ��ư�� Ŭ���� ���
 		if(obj == button1) {
 			String s = (String)code_combobox.getSelectedItem();
 			if(!s.contentEquals("����ȣ ����")) {
 				passenger = S_db.selectPassenger(Integer.parseInt(s));
 				if(passenger != null) {
 					jl.setText(msg + "������ ���� ������ �����Խ��ϴ�!");
 					yearField.setText(String.valueOf(passenger.getDate()).substring(0,4));
 					monthField.setText(String.valueOf(passenger.getDate()).substring(4,6));
 					dayField.setText(String.valueOf(passenger.getDate()).substring(6));
 					for(int i=0; i<timeRadio.length; i++) {
						if(passenger.getTime() == timeRadio[i].getText()) {
							timeRadio[i].setSelected(true);	//�ð�
						}
					}
 					startField.setText(passenger.getStart());
 					arriveField.setText(passenger.getArrive());
 					seatCountField.setText(String.valueOf(passenger.getSeatCount()));
 					nameField.setText(passenger.getName());
 				}
 				else {
 					jl.setText(msg + "�� ��ȣ�� �˻����� �ʾҽ��ϴ�!");
 				}
 			}
 			else{
 				//��� ������ ���
 				refreshData();
 			}
 		}
 		//���� ��ư�� Ŭ���� ���
 		else if(obj == button2) {
 			//----------------------------------------------
 			//�߸��� �����ִ��� Ȯ��
			try {
				int year = Integer.parseInt(yearField.getText());	//�⵵
				int month =Integer.parseInt(monthField.getText());	//��
				int day = Integer.parseInt(dayField.getText());	//��
				int date = Integer.parseInt(yearField.getText() + monthField.getText() + dayField.getText());	//��¥
				int seatCount = Integer.parseInt(seatCountField.getText());
				String start = startField.getText();	//����� ����
				String arrive = arriveField.getText();	//������ ����
				String time = "";
				String name = nameField.getText();
				for(int i=0; i<timeRadio.length; i++) {
					if(timeRadio[i].isSelected()) {
						time = timeRadio[i].getText();	//�ð�
					}
				}			
				System.out.println("asdf");
				//�⵵�� 1000�� �Ѱܾ��ϰ� / ���� 1�� 12���� / ���� 1�� 31����
				if(1000<=year && year<10000 && 1<=month && month<=12 && 1<=day && day<=31) {
					//������ ������ �ִٸ� �޼����� �˷���
					if(start.isEmpty() || arrive.isEmpty() || time=="" || name.isEmpty()) {
						jl.setText(msg + "���ż����� �ʿ��� ������ �����Ǿ��ֽ��ϴ�");
					}
					else {
						passenger = new Passenger();	
						passenger.setName(name);	//�̸�
						//��,���� ���ڸ� ���� �տ� 0�� �ٿ��� (�� : 2020.1.1 => 2020.01.01)
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
						//passenger.setYear(year);	//�⵵
						//passenger.setMonth(month);	//��
						//passenger.setDay(day);	//��
						passenger.setTime(time);	//�ð�
						passenger.setStart(start);	//���
						passenger.setArrive(arrive);	//����
						passenger.setSeatCount(seatCount);	//�¼���
						passenger.setNum(Integer.parseInt((String)code_combobox.getSelectedItem()));
						//�����ͺ��̽��� ���
						if(S_db.updatepassenger(passenger)) {
							jl.setText(msg + "���� ������ �����߽��ϴ�!");
							clearField();
						}
						else {
							jl.setText(msg + "���� ���� ������ �����߽��ϴ�.");
						}
					}
				}
				else {
					jl.setText(msg + "�˸��� ��¥�� �����Ͽ��ֽʽÿ�.");
				}
				//��������� ���
				refreshData();				
			}
			catch(Exception ae) {
				jl.setText(msg + "���������Է�ĭ�� �߸��� ���ڰ� �ԷµǾ����ϴ�.");
			}
 		}	
 			
 			//---------------------------------------
			/*
 			int year = Integer.parseInt(yearField.getText());	//�⵵
			int month =Integer.parseInt(monthField.getText());	//��
			int day = Integer.parseInt(dayField.getText());	//��
			int date = Integer.parseInt(yearField.getText() + monthField.getText() + dayField.getText());	//��¥
			if(1000<=year && year<10000 && 1<=month && month<=12 && 1<=day && day<=31) {
				passenger = new Passenger();
				passenger.setName(nameField.getText());	//�̸�
				//��,���� ���ڸ� ���� �տ� 0�� �ٿ��� (�� : 2020.1.1 => 2020.01.01)
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
				//passenger.setYear(year);	//�⵵
				//passenger.setMonth(month);	//��
				//passenger.setDay(day);	//��
				for(int i=0; i<timeRadio.length; i++) {
					if(timeRadio[i].isSelected()) {
						passenger.setTime(timeRadio[i].getText());	//�ð�
					}
				}
				passenger.setStart(startField.getText());	//���
				passenger.setArrive(arriveField.getText());	//����
				passenger.setSeatCount(Integer.parseInt(seatCountField.getText()));	//�¼���
				passenger.setNum(Integer.parseInt((String)code_combobox.getSelectedItem()));
				if(S_db.updatepassenger(passenger)) {
					jl.setText(msg + "���� ������ �����߽��ϴ�!");
				}
				else {
					jl.setText(msg + "���� ���� ������ ���� �߽��ϴ�!");
				}
				clearField();
				
			}
			else {
				jl.setText(msg + "�˸��� ��¥�� �����Ͽ��ֽʽÿ�.");
			}
			//��������� ���
			refreshData();
 		}
		*/
			
 		//���� ��ư�� Ŭ���� ���
 		else if(obj == button3) {
 			String s = (String)code_combobox.getSelectedItem();
 			
 			if(s.contentEquals("��ü")) {
 				jl.setText(msg + "��ü ������ ���� �ʽ��ϴ�!");
 			}
 			else {
 				if(S_db.deletepassenger(Integer.parseInt(s))) {
 					jl.setText(msg + "ǥ�� ���������� ��ȯ�Ǿ����ϴ�!");
 					clearField();
 				}
 				else {
 					jl.setText(msg + "ǥ�� ���������� ��ȯ�����ʾҽ��ϴ�!");
 				}
 			}
 			//��� ������ ���
 			refreshData();
 		}
	}
}

class MainTab extends JFrame{
	public MainTab() {
		JTabbedPane jtab = new JTabbedPane();
		
		EnrollTab etab = new EnrollTab(); 
		LookTab ltab = new LookTab();
		
		jtab.addTab("����ϱ�",etab);
		jtab.addTab("��ȸ�ϱ�", ltab);
		
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