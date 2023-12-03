import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;


public class PassengerDAO {
	   // 데이터 베이스 URL설정
	   String jdbcUrl = "jdbc:mysql://localhost/busticketdb?";
	   
	   Connection conn;
	   
	   String id = "root"; 
	   String pw = "1234";   
	   
	   PreparedStatement pstmt;
	   ResultSet rs;
	   
	   Vector<String> items = null;
	   String sql;
	   // DB연결 메소드
	   public void connectDB() {
	      try {
	         Class.forName("com.mysql.cj.jdbc.Driver");
	         conn = DriverManager.getConnection(jdbcUrl,id,pw);
	      }
	      catch(Exception e){
	         e.printStackTrace();
	      }
	   }
	   // DB종료 메소드 
	   public void closeDB() {
		   try {
		   pstmt.close();
		   conn.close();
		   }
		   catch (SQLException e) {
			   e.printStackTrace();
		   }
	   }
	   // 콤보박스의 고객관리 번호 목록을 위한 벡터 리턴
	   public Vector<String> getItems(){
	      return items;
	   }
	   // 전체 목록을 가지고 오는 메소드
	   public ArrayList<Passenger> allPassenger() {
	      connectDB();
	      sql = "select * from passenger";
	      // 전체 검색 데이터를 전달하기 위한 ArrayList
	      ArrayList<Passenger> datas = new ArrayList<Passenger>();
	      
	      // 고객 관리 번호 콤보박스 데이터를 위한 벡터 초기화
	      items = new Vector<String>();
	      items.add("고객번호 선택");
	      
	      try {
	         pstmt = conn.prepareStatement(sql);
	         rs = pstmt.executeQuery();
	      // 검색된 데이터 수 만큼 루프를 돌며 Passenger 객체를 만들고 이를 다시 ArrayList에 추가함
	         while(rs.next()) {
	        	Passenger s = new Passenger();
	            s.setNum(rs.getInt("num"));
	            s.setName(rs.getString("name"));
	            s.setDate(rs.getInt("date"));
	            s.setTime(rs.getString("time"));
	            s.setStart(rs.getString("start"));
	            s.setArrive(rs.getString("arrive"));
	            s.setSeatCount(rs.getInt("seatCount"));
	            datas.add(s);
	            items.add(String.valueOf(rs.getInt("num")));//
	         }
	      }
	      catch(SQLException e) {
	         e.printStackTrace();
	         return null;
	      }
	      finally {
	         closeDB();
	      }
	      return datas;
	   }
	   
	   // 새로운 고객을 등록하는 메소드
	   public boolean insertPassenger(Passenger passenger) {
	      connectDB();
	      
	      // num은 자동증가 컬럼이므로 직접 입력하지 않는다
	      sql="insert into passenger(name,date,time,start,arrive,seatCount) values(?,?,?,?,?,?)";
	      try {
	         pstmt = conn.prepareStatement(sql);
	         pstmt.setString(1, passenger.getName());
	         pstmt.setInt(2, passenger.getDate());
	         pstmt.setString(3, passenger.getTime());
	         pstmt.setString(4, passenger.getStart());
	         pstmt.setString(5, passenger.getArrive());
	         pstmt.setInt(6, passenger.getSeatCount());
	         pstmt.executeUpdate();
	      }
	      catch(SQLException e) {
	         e.printStackTrace();
	         return false;
	      }
	      finally {
	         closeDB();
	      }
	      return true;
	   }
	   
	   // 선택한 고객 관리 번호에 해당하는 고객 정보를 가지고 오는 메소드 
	   public Passenger selectPassenger(int num) {	
			connectDB();
			sql = "select * from passenger where num=?";
			Passenger s = null;
			
			try {
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, num);
				rs = pstmt.executeQuery();
				rs.next();
				s = new Passenger();
				s.setNum(rs.getInt("num"));
				s.setName(rs.getString("name"));
				s.setDate(rs.getInt("date"));
				s.setTime(rs.getString("time"));
				s.setStart(rs.getString("start"));
				s.setArrive(rs.getString("arrive"));
				s.setSeatCount(rs.getInt("seatCount"));
			}
			catch(SQLException e) {
				e.printStackTrace();
				return null;
			}
			finally { // create database
				closeDB();
			}
			return s;	
	}
	   
	// 수정한 정보로 고객 정보를 업데이트 하는 메소드
	public  boolean updatepassenger(Passenger passenger) {
		connectDB();
		sql = "update passenger set name=?, date=?, time=?, start=?, arrive=?, seatCount=? where num=?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, passenger.getName());
		    pstmt.setInt(2, passenger.getDate());
	        pstmt.setString(3, passenger.getTime());
		    pstmt.setString(4, passenger.getStart());
		    pstmt.setString(5, passenger.getArrive());
	        pstmt.setInt(6, passenger.getSeatCount());
	        pstmt.setInt(7, passenger.getNum());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		finally {
			closeDB();
		}
		return true;
	}
	// 선택한 고객을 삭제하는 메소드
	public boolean deletepassenger(int num) {
		connectDB();
		sql = "delete from passenger where num=?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, num);
			pstmt.executeUpdate();
		}
		catch(SQLException e) {
			e.printStackTrace();
			return false;
		}
		finally {
			closeDB();
		}
		return true;
	}
	
	// 고객별 정렬 메소드
	public Passenger DeptSortPassenger(Passenger passenger) {
		connectDB();
		// 오름차순
		sql = "select * form passenger order by Name, time asc;";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		finally {
			closeDB();
		}
		return null;
	}
}