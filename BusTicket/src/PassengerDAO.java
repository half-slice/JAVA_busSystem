import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;


public class PassengerDAO {
	   // ������ ���̽� URL����
	   String jdbcUrl = "jdbc:mysql://localhost/busticketdb?";
	   
	   Connection conn;
	   
	   String id = "root"; 
	   String pw = "1234";   
	   
	   PreparedStatement pstmt;
	   ResultSet rs;
	   
	   Vector<String> items = null;
	   String sql;
	   // DB���� �޼ҵ�
	   public void connectDB() {
	      try {
	         Class.forName("com.mysql.cj.jdbc.Driver");
	         conn = DriverManager.getConnection(jdbcUrl,id,pw);
	      }
	      catch(Exception e){
	         e.printStackTrace();
	      }
	   }
	   // DB���� �޼ҵ� 
	   public void closeDB() {
		   try {
		   pstmt.close();
		   conn.close();
		   }
		   catch (SQLException e) {
			   e.printStackTrace();
		   }
	   }
	   // �޺��ڽ��� ������ ��ȣ ����� ���� ���� ����
	   public Vector<String> getItems(){
	      return items;
	   }
	   // ��ü ����� ������ ���� �޼ҵ�
	   public ArrayList<Passenger> allPassenger() {
	      connectDB();
	      sql = "select * from passenger";
	      // ��ü �˻� �����͸� �����ϱ� ���� ArrayList
	      ArrayList<Passenger> datas = new ArrayList<Passenger>();
	      
	      // �� ���� ��ȣ �޺��ڽ� �����͸� ���� ���� �ʱ�ȭ
	      items = new Vector<String>();
	      items.add("����ȣ ����");
	      
	      try {
	         pstmt = conn.prepareStatement(sql);
	         rs = pstmt.executeQuery();
	      // �˻��� ������ �� ��ŭ ������ ���� Passenger ��ü�� ����� �̸� �ٽ� ArrayList�� �߰���
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
	   
	   // ���ο� ���� ����ϴ� �޼ҵ�
	   public boolean insertPassenger(Passenger passenger) {
	      connectDB();
	      
	      // num�� �ڵ����� �÷��̹Ƿ� ���� �Է����� �ʴ´�
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
	   
	   // ������ �� ���� ��ȣ�� �ش��ϴ� �� ������ ������ ���� �޼ҵ� 
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
	   
	// ������ ������ �� ������ ������Ʈ �ϴ� �޼ҵ�
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
	// ������ ���� �����ϴ� �޼ҵ�
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
	
	// ���� ���� �޼ҵ�
	public Passenger DeptSortPassenger(Passenger passenger) {
		connectDB();
		// ��������
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