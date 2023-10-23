package libraryDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JOptionPane;

public class RentalHistoryDAO {
	private static MemberDAO memberDAO = new MemberDAO();

	private String url = "jdbc:mariadb://14.42.124.85/library_db";
	private String user = "hanul";
	private String pass = "910725";

	// 외부에서 처리하는 작업이 필요시에 하나의 instance만 리턴하도록.
	public static MemberDAO getInstance() {
		return memberDAO;
	};

	// DB에 연결할때마다 conn을 리턴하는 메서드 정의
	private Connection getConnection() {
		Connection conn = null;

		try {
			Class.forName("org.mariadb.jdbc.Driver");
			conn = DriverManager.getConnection(url, user, pass);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
	}

	// 관리자가 회원의 대여를 위해 도서 검색하는 메서드 : 현재 도서 대여가능 여부(대여중/대여가능)출력
	public ArrayList<BookDTO> searchBookToRent(String whereSql) {
		Connection conn = getConnection();
		PreparedStatement pstm = null;
		ResultSet rs = null;
		ArrayList<BookDTO> bookList = new ArrayList<>();

		String sql = "SELECT\r\n"
				+ "    book_db.bookNum,\r\n"
				+ "    book_db.title,\r\n"
				+ "    book_db.author,\r\n"
				+ "    book_db.publisher,\r\n"
				+ "    rental_history.state,\r\n"
				+ "    recent_rentals.rentDt,\r\n"
				+ "    rental_history.returnDt\r\n"
				+ "FROM\r\n"
				+ "    book_db\r\n"
				+ "LEFT JOIN (\r\n"
				+ "    SELECT\r\n"
				+ "        bookNum,\r\n"
				+ "        MAX(rentDt) AS rentDt\r\n"
				+ "    FROM\r\n"
				+ "        rental_history\r\n"
				+ "    GROUP BY\r\n"
				+ "        bookNum\r\n"
				+ ") recent_rentals ON book_db.bookNum = recent_rentals.bookNum\r\n"
				+ "LEFT JOIN rental_history ON book_db.bookNum = rental_history.bookNum AND rental_history.rentDt = recent_rentals.rentDt\r\n"
				+ "WHERE\r\n"
				+ "    " + whereSql 
				+ "    AND rental_history.rentDt = (\r\n"
				+ "        SELECT\r\n"
				+ "            MAX(rentDt)\r\n"
				+ "        FROM\r\n"
				+ "            rental_history AS rh\r\n"
				+ "        WHERE\r\n"
				+ "            rh.bookNum = book_db.bookNum\r\n"
				+ "    );";

		try {
			pstm = conn.prepareStatement(sql);
			rs = pstm.executeQuery();

			while (rs.next()) {

				BookDTO book = new BookDTO();
				book.setBookNum(rs.getInt("bookNum"));
				book.setTitle(rs.getString("title"));
				book.setAuthor(rs.getString("author"));
				book.setPublisher(rs.getString("publisher"));
				book.setState(rs.getString("state"));

				bookList.add(book);
			}

			rs.close();
			pstm.close();
			conn.close();
		} catch (

		SQLException e) {
			e.printStackTrace();
		}

		return bookList;
	}
	
	//도서의 대여상태("대여중" or "반납완료")를 DB에서 불러오는 메서드
	public String getRentalStateOfBook(int bookNum) {
		Connection conn = getConnection();
		PreparedStatement pstm = null;
		ResultSet rs = null;
		String sql = "SELECT bookNum, state " + "FROM rental_history "
				+ "WHERE bookNum = ? AND rentDt = (SELECT MAX(rentDt) FROM rental_history WHERE bookNum = ?);";
		String state = "";

		try {
			pstm = conn.prepareStatement(sql);
			pstm.setInt(1, bookNum);
			pstm.setInt(2, bookNum);
			rs = pstm.executeQuery();

			if (rs.next()) {
				state = rs.getString("state");
			} else {
				System.out.println("해당 도서의 대여상태를 불러오지 못했습니다.");
			}
			rs.close();
			pstm.close();
			conn.close();
		} catch (SQLException e) {
			System.out.println("대여상태를 불러오는 과정에서 오류발생 : " + e.getMessage());
		}
		return state;
	}
	
	//회원의 대여상태("대여중" or "반납완료")를 DB에서 불러오는 메서드
	public String getRentalStateOfMember(int memberNum) {
		Connection conn = getConnection();
		PreparedStatement pstm = null;
		ResultSet rs = null;
		String sql = "SELECT memberNum, state " + "FROM rental_history "
				+ "WHERE memberNum = ? AND rentDt = (SELECT MAX(rentDt) FROM rental_history WHERE memberNum = ?);";
		String state = "";
		
		try {
			pstm = conn.prepareStatement(sql);
			pstm.setInt(1, memberNum);
			pstm.setInt(2, memberNum);
			rs = pstm.executeQuery();
			
			if (rs.next()) {
				state = rs.getString("state");
			}else {
				System.out.println("대여한 적이 없는 회원입니다. 수정 가능");
				state = "";
			}
			
			rs.close();
			pstm.close();
			conn.close();
		} catch (SQLException e) {
			System.out.println("회원의 대여상태를 불러오는 과정에서 오류발생 : " + e.getMessage());
		}
		return state;
	}

	// 대여 메서드
	// 회원번호(member_db 기본key)와 도서번호(book_db 기본key)를 받아서 rental_history에 입력
	public void rentBook(int memberNum, int bookNum) throws Exception {
		int rs = 0;
		Connection conn = getConnection();
		PreparedStatement pstmt = null;
		String sql = "Insert into rental_history Values (" + memberNum + "," + bookNum + ",NOW(),NULL,'대여중')";

		try {
			pstmt = conn.prepareStatement(sql);

			rs = pstmt.executeUpdate(); // SQL 문 실행 및 결과 반환

			pstmt.close();
			conn.close();
			if (rs > 0) {
				BookDTO book = new BookDTO();
				JOptionPane.showMessageDialog(null, "대여가 완료되었습니다.\n" + "반납예정일은 "
						+ book.getDueDate(new Timestamp(System.currentTimeMillis())) + "입니다.");
			} else {
				JOptionPane.showMessageDialog(null, "대여에 실패하였습니다.");
			}

		} catch (Exception e) {
			System.out.println("대여과정에서 오류 발생 : " + e.getMessage());
		}

	}

	// 반납 메서드
	// 회원번호(member_db 기본key)와 도서번호(book_db 기본key)를 받아서 rental_history에서 데이터를 찾아서
	// 상태(state)값 바꿈
	public void returnBook(int memberNum, int bookNum) {
		Connection conn = getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT * FROM rental_history WHERE memberNum = ? AND bookNum = ?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, memberNum);
			pstmt.setInt(2, bookNum);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				if (rs.getString("state").equals("대여중")) {// 대여중인 경우
					String updateSql = "UPDATE rental_history SET state = ?, returnDt = NOW() WHERE memberNum = ?"
							+ " AND bookNum = ?";
					PreparedStatement updatePstmt = conn.prepareStatement(updateSql);
					updatePstmt.setString(1, "반납완료");
					updatePstmt.setInt(2, memberNum);
					updatePstmt.setInt(3, bookNum);
					if (updatePstmt.executeUpdate() > 0) {
						JOptionPane.showMessageDialog(null, "반납이 완료되었습니다.");
					} else {
						JOptionPane.showMessageDialog(null, "반납에 실패하였습니다.");
					}
					updatePstmt.close();
				}
			} else {
				JOptionPane.showMessageDialog(null, "해당 회원이 해당 도서를 대여한 기록이 없습니다.");
			}
			rs.close();
			pstmt.close();
		} catch (Exception e) {
			System.out.println("도서 반납 메서드의 오류 : " + e.getMessage());
		}
	}

	// 특정 회원의 현재 대여도서 세는 메서드
	public String countNowRental(int memberNum) throws Exception {
		String result = "";

		Connection conn = getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		// 특정 회원이 현재 대여중인 도서 권수를 세는 SQL문
		String sql = "SELECT COUNT(*) FROM rental_history WHERE memberNum = ? AND state = ?";

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, memberNum);
			pstmt.setString(2, "대여중");
			rs = pstmt.executeQuery();
			if (rs.next()) {
				int count = rs.getInt(1);
				if (count > 0) {
					result = "현재 총 " + count + "권 대여중";
				} else if (count == 0) {
					result = "현재 대여중인 책 없음";
				} else {
					result = "대여중인 도서 세는 과정에 오류 발생";
				}
			}

			rs.close();
			pstmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			result = "대여중인 도서 세는 과정의 오류 : " + e.getMessage();
		}

		return result;
	}

	// 특정 회원의 대여현황을 DB에서 끌어오는 메서드
	public ArrayList<BookDTO> getNowRentals(int memberNum) {
		Connection conn = getConnection();
		PreparedStatement pstm = null;
		ResultSet rs = null;
		int row = 0;
		ArrayList<BookDTO> nowRentalList = new ArrayList<>();
		String sql = "SELECT book_db.bookNum, book_db.title, book_db.author, rental_history.rentDt\r\n"
				+ "FROM rental_history\r\n" + "INNER JOIN book_db ON rental_history.bookNum = book_db.bookNum\r\n"
				+ "WHERE rental_history.memberNum = ? AND rental_history.state = ?";
		try {
			pstm = conn.prepareStatement(sql);
			pstm.setInt(1, memberNum);
			pstm.setString(2, "대여중");
			rs = pstm.executeQuery();
			row = pstm.executeUpdate();

			while (rs.next()) {

				BookDTO book = new BookDTO();
				book.setBookNum(rs.getInt("bookNum"));
				book.setTitle(rs.getString("title"));
				book.setAuthor(rs.getString("author"));

				// DATETIME 타입인 rentDt 값을 가져온다.
				Timestamp rentDt = rs.getTimestamp("rentDt");

				book.setRentDt(rentDt);
				book.getDueDate(rentDt);

				nowRentalList.add(book);
			}
			rs.close();
			pstm.close();
			conn.close();
		} catch (

		SQLException e) {
			System.out.println("대여현황 다운로드 오류 발생 : " + e.getMessage());
			// e.printStackTrace();
		}
		return nowRentalList;
	}

	// 특정 회원의 누적 대여도서(횟수 아니고, 도서 권수)를 세는 메서드
	public String countTotalRentals(int memberNum) throws Exception {
		String result = "";

		Connection conn = getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT memberNum, COUNT(DISTINCT bookNum) AS bookCount\r\n" // bookNum은 중복되지 않게
				+ "FROM rental_history\r\n" + "WHERE memberNum = ?\r\n" + "GROUP BY memberNum";
		int bookCount = 0;

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, memberNum);
			rs = pstmt.executeQuery();

			// 결과가 있다면 처리
			if (rs.next()) {
				bookCount = rs.getInt("bookCount");
				if (bookCount > 0) {
					result = "지금까지 총 " + bookCount + "권 대여";
				} else if (bookCount == 0) {
					result = "대여 기록 없음";
				}
			} else { // 결과가 없다면
				result = "대여 기록 없음";
			}

			rs.close();
			pstmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			result = "총 대여도서 세는 과정의 오류 : " + e.getMessage();
		}

		return result;
	}

	// 누적 대여기록 DB에서 끌어오는 메서드
	public ArrayList<BookDTO> getTotalRentals(int memberNum) {
		Connection conn = getConnection();
		PreparedStatement pstm = null;
		ResultSet rs = null;
		ArrayList<BookDTO> totalRentalsList = new ArrayList<>();
		String sql = "SELECT book_db.bookNum, book_db.title, book_db.author, rental_history.returnDt,\r\n"
				+ "       MAX(rental_history.rentDt) AS rentDt\r\n"
				+ "FROM book_db\r\n"
				+ "INNER JOIN rental_history ON book_db.bookNum = rental_history.bookNum\r\n"
				+ "WHERE rental_history.memberNum IN (\r\n"
				+ "    SELECT memberNum\r\n"
				+ "    FROM rental_history\r\n"
				+ "    WHERE rental_history.memberNum = ?\r\n"
				+ ")\r\n"
				+ "GROUP BY book_db.bookNum, book_db.title, book_db.author;";
		try {
			pstm = conn.prepareStatement(sql);
			pstm.setInt(1, memberNum);
			rs = pstm.executeQuery();
			
				while (rs.next()) {

					BookDTO book = new BookDTO();
					book.setBookNum(rs.getInt("bookNum"));
					book.setTitle(rs.getString("title"));
					book.setAuthor(rs.getString("author"));

					// DATETIME 타입인 rentDt,returnDt 값을 가져온다.
					Timestamp rentDt = rs.getTimestamp("rentDt");
					// System.out.println("rentDt : " + rentDt);
					book.setRentDt(rentDt);

					// 대여중인 도서는 returnDt 값이 null
					// 대여중인 도서는 기본값 즉 공백을 띄우도록 하고,
					// null값이 아닌 즉 반납완료된 도서만 반납일 값을 set해줌
					Timestamp returnDt = rs.getTimestamp("returnDt");
					if (returnDt != null) {
						book.setReturnDt(returnDt);

					}
					/*
					 * System.out.println( "도서명: " + book.getTitle() + "/ 대여일: " + book.getRentDt()
					 * + "/ 반납일: " + book.getReturnDt()); 
					 */
					totalRentalsList.add(book);
				}
			

			rs.close();
			pstm.close();
			conn.close();
		} catch (SQLException e) {
			System.out.println("누적 대여기록 다운로드 오류 발생 : " + e.getMessage());
			// e.printStackTrace();
		}

		return totalRentalsList;
	}

}
