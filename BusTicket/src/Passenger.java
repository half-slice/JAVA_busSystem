public class Passenger {
	//���� �¼� ������
	private int Num;	//�ε�����ȣ
	private String Name;	//�̸�
	private int Date;
	private int Year;	//�⵵	
	private int Month;	//��
	private int Day;	//��
	private String Time;	//�ð�
	private String Start;	//�����
	private String Arrive;	//������
	private int SeatCount;	//�¼���
	
	//�ε��� ��ȣ ����, ��ȣ ��������
	public int getNum() {
		return Num;
	}
	public void setNum(int num) {
		Num = num;
	}
	//�̸�
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	
	//��¥
	public int getDate() {
		return Date;
	}
	public void setDate(int date) {
		Date = date;
	} 
	
	//�⵵
	public int getYear() {
		return Year;
	}
	public void setYear(int year) {
		Year = year;
	}
	//��
	public int getMonth() {
		return Month;
	}
	public void setMonth(int month) {
		Month = month;
	}
	//��
	public int getDay() {
		return Day;
	}
	public void setDay(int day) {
		Day = day;
	}
	//�ð�
	public String getTime() {
		return Time;
	}
	public void setTime(String time) {
		Time = time;
	}
	//�����
	public String getStart() {
		return Start;
	}
	public void setStart(String start) {
		Start = start;
	}
	//������
	public String getArrive() {
		return Arrive;
	}
	public void setArrive(String arrive) {
		Arrive = arrive;
	}
	//�¼� ��
	public int getSeatCount() {
		return SeatCount;
	}
	public void setSeatCount(int seatcount) {
		SeatCount = seatcount;
	}
}
