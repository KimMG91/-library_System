package libraryDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.JOptionPane;

public class BookDAO {

	private static BookDAO instance = new BookDAO();

	private String url = "jdbc:mariadb://14.42.124.85/library_db"; // 연결 문자열
	private String user = "hanul"; // 사용자 이름
	private String password = "910725"; // 사용자 비밀번호
	private String driver = "org.mariadb.jdbc.Driver";

	/*
	 * // 외부에서 객체 생성 못하게 기본 생성자 private 처리함 public BookDAO() {
	 * 
	 * }
	 */
	// 외부에서 처리하고자 하는 DB작업이 필요시에 하나의 instance만 리턴하도록 static 메서드로 인스턴스 리턴시킴
	public static BookDAO getInstance() {
		return instance;
	}

	// DB에 연결할때마다 conn을 리턴하는 메서드 정의
	private Connection getConnection() {
		Connection conn = null;
		try {
			Class.forName(driver);

			conn = DriverManager.getConnection(url, user, password);
			System.out.println("연결 성공");
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		return conn;
	}

	// Connection close() 메서드 정의
	private void closeConnection(Connection conn) {
		if (conn != null) {
			try {
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	// (Insert)도서를 추가하는 메서드 정의하기
	public int createBook(BookDTO bookDTO) {
		int rows = 0;

		// 회원번호, 도서명, 작가명, 출판사, 발행일
		String sql = "Insert into book_db Values ((Select MAX(a.booknum) + 1 from book_db a),?,?,?,?)";

		Connection conn = null;
		PreparedStatement pstmt = null;

		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, bookDTO.getTitle());
			pstmt.setString(2, bookDTO.getAuthor());
			pstmt.setString(3, bookDTO.getPublisher());
			pstmt.setString(4, bookDTO.getPubYear());

			rows = pstmt.executeUpdate();

			pstmt.close();
			conn.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return rows;
	}

	// (SELECT) 도서 읽기/인출하는 메서드 정의하기
	public String readBook(BookDTO bookDTO) {
		String result = "";
		String sql = "SELECT * FROM book_db";

		Connection conn = null;
		PreparedStatement pstmt = null;

		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(sql);

			pstmt.close();
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}

	// (UPDATE) 도서정보를 갱신하는 메서드 정의하기
	public int updateBook(BookDTO bookDTO) {
		int rows = 0;
		String sql = "UPDATE book_db SET title = ?, author = ?, publisher = ?, pubYear = ? WHERE bookNum = ?";
		Connection conn = null;
		PreparedStatement pstmt = null;

		try {
			// 데이터베이스 연결
			conn = getConnection();
			// 쿼리 준비
			pstmt = conn.prepareStatement(sql);

			// 쿼리 매개변수 설정
			pstmt.setString(1, bookDTO.getTitle());
			pstmt.setString(2, bookDTO.getAuthor());
			pstmt.setString(3, bookDTO.getPublisher());
			pstmt.setString(4, bookDTO.getPubYear());
			pstmt.setInt(5, bookDTO.getBookNum());

			// 쿼리 실행 및 결과 처리
			rows = pstmt.executeUpdate();

			if (rows == 0) {
				JOptionPane.showMessageDialog(null, "DB에서 수정된 데이터가 없습니다.", "오류", JOptionPane.WARNING_MESSAGE);
			} else if (rows == 1) {
				JOptionPane.showMessageDialog(null, "수정되었습니다.", "수정 완료", JOptionPane.INFORMATION_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(null, "DB에서 복수의 도서가 수정되었습니다.", "오류", JOptionPane.WARNING_MESSAGE);
			}
		} catch (SQLException e) {
			System.out.println("DB에서 도서 수정 중 오류 발생: " + e.getMessage());
		} finally {
			// 자원 정리
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return rows;
	}

	/*
	 * // (DELETE) 도서정보를 삭제하는 메서드 정의하기 public int deleteBook(BookDTO dto) throws
	 * SQLException { Connection conn = null; PreparedStatement pstmt = null; int
	 * result = 0; String sql = "DELETE FROM book_db WHERE bookNum = ?";
	 * 
	 * try { conn = getConnection(); pstmt = conn.prepareStatement(sql);
	 * pstmt.setInt(1, dto.getBookNum());
	 * 
	 * result = pstmt.executeUpdate();
	 * 
	 * } catch (Exception e) { System.out.println(e.getMessage()); } conn.close();
	 * return result; }
	 */
	// (DELETE) 도서정보를 삭제하는 메서드 정의하기
	public int deleteBook(BookDTO dto) throws SQLException {
		Connection conn = null;
		PreparedStatement pstmt = null;
		int result = 0;
		String sql = "DELETE FROM book_db WHERE bookNum = ?";

		try {
			conn = getConnection();

			// 삭제하기 전에 rental_history 테이블에서 해당 도서 번호를 참조하는 레코드를 먼저 삭제
			String deleteRentalHistorySql = "DELETE FROM rental_history WHERE bookNum = ?";
			PreparedStatement deleteRentalHistoryStmt = conn.prepareStatement(deleteRentalHistorySql);
			deleteRentalHistoryStmt.setInt(1, dto.getBookNum());
			deleteRentalHistoryStmt.executeUpdate();
			deleteRentalHistoryStmt.close();

			// book_db 테이블에서 도서 삭제
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, dto.getBookNum());
			result = pstmt.executeUpdate();

			if (result == 0) {
				JOptionPane.showMessageDialog(null, "DB에서 도서가 삭제되지 않았습니다.", "오류", JOptionPane.WARNING_MESSAGE);
			} else if (result == 1) {
				JOptionPane.showMessageDialog(null, "해당 도서가 삭제되었습니다.", "삭제 완료", JOptionPane.INFORMATION_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(null, "DB에서 복수의 도서 데이터가 삭제되었습니다.", "오류", JOptionPane.WARNING_MESSAGE);
			}

		} catch (Exception e) {
			System.out.println("DB에서 도서 삭제 중 오류 발생: " + e.getMessage());
		} finally {
			// 리소스 해제
			if (pstmt != null) {
				pstmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
		return result;
	}

	public boolean insertBook(BookDTO bookdto) {
		try (Connection conn = DriverManager.getConnection(url, user, password)) {
			// 중복 도서 검사
			String duplicateCheckSql = "SELECT COUNT(*) FROM book_db WHERE title = ? AND author = ?";
			PreparedStatement duplicateCheckStmt = conn.prepareStatement(duplicateCheckSql);
			duplicateCheckStmt.setString(1, bookdto.getTitle());
			duplicateCheckStmt.setString(2, bookdto.getAuthor());
			ResultSet duplicateCheckResult = duplicateCheckStmt.executeQuery();
			duplicateCheckResult.next();
			int duplicateCount = duplicateCheckResult.getInt(1);

			if (duplicateCount > 0) {
				System.out.println("이미 동일한 도서가 존재합니다.");
				return false;
			}

			duplicateCheckResult.close();
			duplicateCheckStmt.close();

			// 중복이 없을 경우 도서 추가
			// String insertSql = "INSERT INTO book_db VALUES ((SELECT MAX(a.bookNum) + 1
			// FROM book_db a),?,?,?,?)";
			String insertSql = "INSERT INTO book_db (title, author, publisher, pubYear) VALUES (?,?,?,?)";
			PreparedStatement insertStmt = conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS);
			insertStmt.setString(1, bookdto.getTitle());
			insertStmt.setString(2, bookdto.getAuthor());
			insertStmt.setString(3, bookdto.getPublisher());
			insertStmt.setString(4, bookdto.getPubYear());

			int rowsInserted = insertStmt.executeUpdate();

			// 자동 생성된 bookNum 값 가져오기
			try (ResultSet generatedKeys = insertStmt.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					int generatedBookNum = generatedKeys.getInt(1);
					bookdto.setBookNum(generatedBookNum);
				} else {
					throw new SQLException("AUTO_INCREMENT 칼럼과 같은 유일한 식별자를 얻지 못해서 book 데이터 생성 실패");
					// throw new SQLException("Creating book failed, no ID obtained.");
				}
			}

			insertStmt.close();

			// memberNum , bookNum, RentDt, ReturnDt, state
			String firstRentSql = "INSERT INTO rental_history VALUES (?,?,NOW(),NOW() + INTERVAL 1 SECOND,'반납완료')";
			PreparedStatement firstRentPstmt = conn.prepareStatement(firstRentSql);
			firstRentPstmt.setInt(1, 1);
			firstRentPstmt.setInt(2, bookdto.getBookNum());
			int firstRentSuccess = firstRentPstmt.executeUpdate();

			firstRentPstmt.close();

			return ((rowsInserted > 0) && (firstRentSuccess > 0));

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}

	}

	// 책 목록 전체 리스트 불러오는 메서드
	// 모두 public으로 Open 해줘야함
	public ArrayList<BookDTO> getAllBook() {
		Connection conn = getConnection();
		String sql = "SELECT * FROM book_db";
		ResultSet rs = null;
		Statement stmt = null;
		BookDTO dto = null;
		ArrayList<BookDTO> bookList = new ArrayList<>();

		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);

			while (rs.next()) {
				dto = new BookDTO();

				dto.setBookNum(rs.getInt("bookNum"));
				dto.setTitle(rs.getString("title"));
				dto.setAuthor(rs.getString("author"));
				dto.setPublisher(rs.getString("publisher"));
				dto.setPubYear(rs.getString("pubYear"));

				bookList.add(dto);
			}

			rs.close();
			stmt.close();
			conn.close();

		} catch (Exception e) {
			System.out.println("모든 도서정보를 DB에서 불러오는 과정에서 오류 발생: " + e.getMessage());
			e.printStackTrace();
		}

		return bookList;
	}

	// 어디서 쓰는지???
	// 책제목을 조회시 리스트 가져오는 메서드 작성.
	public ArrayList<BookDTO> getBookSearch(String keyword) {

		Connection conn = getConnection();
		String sql = "SELECT * FROM book_db Where title like '%" + keyword + "%'";
		ResultSet rs = null;
		Statement pstmt = null;
		ArrayList<BookDTO> bookList = new ArrayList<>();

		try {
			pstmt = conn.prepareStatement(sql);
			// pstmt.setString(1, keyword);
			rs = pstmt.executeQuery(sql);

			while (rs.next()) {
				BookDTO dto = new BookDTO();

				dto.setBookNum(rs.getInt("BookNum"));
				dto.setTitle(rs.getString("title"));
				dto.setAuthor(rs.getString("author"));
				dto.setPublisher(rs.getString("publisher"));
				dto.setPubYear(rs.getString("pubyear"));

				bookList.add(dto);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 리소스해제
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
			} catch (Exception e) {
				System.out.println("getBookSearch메서드 리소스 해제 과정 오류 : " + e.getMessage());
				e.printStackTrace();
			}
		}

		System.out.println("검색된 책 리스트 " + bookList.size());
		return bookList;
	}

}
