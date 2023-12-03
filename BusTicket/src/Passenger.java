public class Passenger {
	//버스 좌석 정보들
	private int Num;	//인덱스번호
	private String Name;	//이름
	private int Date;
	private int Year;	//년도	
	private int Month;	//월
	private int Day;	//일
	private String Time;	//시간
	private String Start;	//출발지
	private String Arrive;	//도착지
	private int SeatCount;	//좌석수
	
	//인덱스 번호 설정, 번호 가져오기
	public int getNum() {
		return Num;
	}
	public void setNum(int num) {
		Num = num;
	}
	//이름
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	
	//날짜
	public int getDate() {
		return Date;
	}
	public void setDate(int date) {
		Date = date;
	} 
	
	//년도
	public int getYear() {
		return Year;
	}
	public void setYear(int year) {
		Year = year;
	}
	//월
	public int getMonth() {
		return Month;
	}
	public void setMonth(int month) {
		Month = month;
	}
	//일
	public int getDay() {
		return Day;
	}
	public void setDay(int day) {
		Day = day;
	}
	//시간
	public String getTime() {
		return Time;
	}
	public void setTime(String time) {
		Time = time;
	}
	//출발지
	public String getStart() {
		return Start;
	}
	public void setStart(String start) {
		Start = start;
	}
	//도착지
	public String getArrive() {
		return Arrive;
	}
	public void setArrive(String arrive) {
		Arrive = arrive;
	}
	//좌석 수
	public int getSeatCount() {
		return SeatCount;
	}
	public void setSeatCount(int seatcount) {
		SeatCount = seatcount;
	}
}
