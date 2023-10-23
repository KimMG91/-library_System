package libraryDB;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JOptionPane;

import library.Menu;
import libraryDB.MemberDTO;

public class MemberDAO {

	private static MemberDAO memberDAO = new MemberDAO();
	private String phoneNum;

	private String url = "jdbc:mariadb://14.42.124.85/library_db";
	private String user = "hanul";
	private String pass = "910725";

	/*
	 * // 외부 객체 생성 못하게 생성자 생성하고 private 걸어놓기 private MemberDAO() { }
	 */

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

	// 회원가입(등록) 시 아이디 중복검사.
	public String insertId(MemberDTO dto) throws Exception {
		String result = "";

		Connection conn = getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT COUNT(*) FROM member_db WHERE id = ?";

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dto.getId());
			rs = pstmt.executeQuery();
			if (rs.next()) {
				int count = rs.getInt(1);
				if (count > 0) {
					result = "중복된 아이디입니다.";
				} else {
					result = "중복된 아이디가 없습니다.";
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			result = "아이디 검사 중 오류가 발생하였습니다: " + e.getMessage();
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (pstmt != null) {
				pstmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		}

		return result;
	}

	// phoneNum 변수에 "-"를 제외한 숫자만 저장하는 메서드 추가
	public void setPhoneNum(String phoneNum) {
		// 전화번호에서 "-"를 제외한 숫자만 추출하여 저장
		this.phoneNum = phoneNum.replaceAll("[^\\d]", "");

	}

	// 회원가입(등록) 시 전화번호부 중복검사
	public String insertPhoneNum(MemberDTO dto) throws Exception {
		String result = "";

		Connection conn = getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT COUNT(*) FROM member_db WHERE phoneNum = ?";

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dto.getPhoneNum());
			rs = pstmt.executeQuery();

			if (rs.next()) {
				int count = rs.getInt(1);

				if (count > 0) {
					result = "중복된 전화번호입니다.";

				} else {
					result = "중복된 전화번호가 없습니다.";
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			result = "전화번호 검사 중 오류가 발생하였습니다: " + e.getMessage();
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (pstmt != null) {
				pstmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		}

		return result;
	}

	// 비밀번호 넣는거
	// private static String password;
	// public static String getPassword() {
	// return password;
	// }
	public String insertPassword(MemberDTO dto) throws Exception {
		String result = "";
		Connection conn = getConnection();
		PreparedStatement pstmt = null;
		String sql = "INSERT INTO member_db (password) VALUES (?)";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dto.getPassword());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (pstmt != null) {
				pstmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		}

		return result;
	}

	// 회원 추가하는 메서드
	public String insertMember(MemberDTO dto) throws Exception {
		int result = 0;
		Connection conn = getConnection();
		// PreparedStatement 객체를 사용할게요.
		String sql = "Insert into " + "member_db Values (" + "(Select MAX(a.memberNum) + 1 from member_db a)"
				+ ",?,?,?,?,?,'N')";
		PreparedStatement pstmt = conn.prepareStatement(sql);

		pstmt.setString(1, dto.getId());
		pstmt.setString(2, dto.getPassword());
		pstmt.setString(3, dto.getName());
		pstmt.setString(4, removeHyphens(dto.getPhoneNum())); // 하이픈 제거 후 저장
		pstmt.setString(5, dto.getBirthday());

		result = pstmt.executeUpdate(); // SQL 문 실행 및 결과 반환

		pstmt.close();
		conn.close();

		if (result > 0) {
			return "회원 등록이 완료되었습니다.";
		} else {
			return "회원 등록에 실패하였습니다.";
		}

	}

	// 로그인 시 입력받은 값과 일치하는 아이디&비밀번호 DB에서 찾기
	public MemberDTO login(String id, String password) throws Exception {

		Connection conn = getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT * FROM member_db WHERE id = ?";
		MemberDTO dto = null;

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				// 아이디 존재
				String storedPassword = rs.getString("password");
				if (storedPassword.equals(password)) {
					// 로그인 성공
					dto = new MemberDTO(); // 객체 초기화
					dto.setMemberNum(rs.getInt("memberNum"));
					dto.setId(rs.getString("id"));
					dto.setPassword(rs.getString("password"));
					dto.setName(rs.getString("name"));
					dto.setPhoneNum(rs.getString("phoneNum"));
					dto.setBirthday(rs.getString("birthday"));
					dto.setIsManager(rs.getString("isManager"));

					//System.out.println("로그인 성공");
					// System.out.println("로그인 성공 시 DB에서 받은 객체 정보 : " + dto);
				} else {
					// 비밀번호 틀림
					System.out.println("비밀번호가 일치하지 않습니다.");
				}
			} else {
				// 아이디 없음
				System.out.println("아이디가 존재하지 않습니다.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("로그인 중 오류가 발생하였습니다: " + e.getMessage());
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (pstmt != null) {
				pstmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		}

		return dto;
	}

	// login(String id, String password) 메서드와 중복 기능하는 게 아닌지 체크할 것!!!!!!!!!!
	// DB에서 해당 아이디 존재하는지 개수 세는 메서드
	public boolean checkIdExist(String id) throws Exception {
		Connection conn = getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "SELECT COUNT(*) FROM member_db WHERE id = ?";

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				int count = rs.getInt(1);
				return count > 0;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (pstmt != null) {
				pstmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
		return false;
	}

	// 모든 멤버정보 반환리스트
	public ArrayList<MemberDTO> getAllMember() {
		Connection conn = getConnection();
		String sql = "SELECT * FROM member_db";
		ResultSet rs = null;
		Statement stmt = null;
		ArrayList<MemberDTO> memberList = new ArrayList<>();

		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);

			while (rs.next()) {
				MemberDTO dto = new MemberDTO();
				dto.setMemberNum(rs.getInt("memberNum"));
				dto.setId(rs.getString("id"));
				dto.setName(rs.getString("name"));
				dto.setPassword(rs.getString("password"));
				dto.setPhoneNum(rs.getString("phoneNum"));
				dto.setBirthday(rs.getString("birthday"));

				memberList.add(dto);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (stmt != null) {
					stmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return memberList;
	}

	// 전화번호 인증해서 DB에 똑같은 정보 가져오기
	public MemberDTO getMemberByPhoneNumber(String phoneNum) {
		MemberDTO memberDTO = null;

		try {
			// DB 연결
			Connection conn = getConnection();

			String phoneNumWithoutHyphen = removeHyphens(phoneNum);
			// 쿼리 실행
			String sql = "SELECT * FROM member_db WHERE phoneNum = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, phoneNum);
			ResultSet rs = stmt.executeQuery();

			// 결과 처리
			if (rs.next()) {
				memberDTO = new MemberDTO();
				memberDTO.setMemberNum(rs.getInt("memberNum"));
				memberDTO.setId(rs.getString("id"));
				memberDTO.setName(rs.getString("name"));
				memberDTO.setPassword(rs.getString("password"));
				memberDTO.setPhoneNum(rs.getString("phoneNum"));
				memberDTO.setBirthday(rs.getString("birthday"));
			}

			// 리소스 해제
			rs.close();
			stmt.close();
			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return memberDTO;
	}

	// 하이픈 제거 메서드 추가
	private String removeHyphens(String phoneNum) {
		return phoneNum.replaceAll("-", "");
	}

	// 회원의 관리자여부("N" or "Y")를 DB에서 불러오는 메서드
	public String whetherManagerOrNot(int memberNum) {
		Connection conn = getConnection();
		PreparedStatement pstm = null;
		ResultSet rs = null;
		String sql = "SELECT memberNum, isManager " + "FROM member_db " + "WHERE memberNum = ?;";
		String state = "";

		try {
			pstm = conn.prepareStatement(sql);
			pstm.setInt(1, memberNum);
			rs = pstm.executeQuery();

			if (rs.next()) {
				state = rs.getString("isManager");
			} else {
				System.out.println("해당 회원의 관리자여부를 불러오지 못했습니다.");
			}
			rs.close();
			pstm.close();
			conn.close();
		} catch (SQLException e) {
			System.out.println("회원의 관리자여부를 불러오는 과정에서 오류발생 : " + e.getMessage());
		}
		return state;
	}

	// (SELECT) 회원 읽기/선택하는 메서드 정의하기
	public ArrayList<MemberDTO> readmember(String searchText) {
		Connection conn = getConnection();
		String sql = "SELECT " + searchText + " FROM member_db";
		ResultSet rs = null;
		Statement stmt = null;
		ArrayList<MemberDTO> memberList = new ArrayList<>();

		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);

			while (rs.next()) {
				MemberDTO dto = new MemberDTO();

				dto.setMemberNum(rs.getInt("memberNum"));
				dto.setId(rs.getString("id"));
				dto.setPassword(rs.getString("password"));
				dto.setName(rs.getString("name"));
				dto.setPhoneNum(rs.getString("phoneNum"));
				dto.setBirthday(rs.getString("birthday"));
				dto.setIsManager(rs.getString("isManager"));

				memberList.add(dto);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// 리소스 해제
			try {
				if (rs != null)
					rs.close();
				if (stmt != null)
					stmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return memberList;
	}

	// 회원 삭제하는 메서드
	public boolean deleteMember(int memberNum) throws Exception {
		boolean success = false;

		Connection conn = null;
		PreparedStatement pstmt = null;
		String memberSQL = "DELETE FROM member_db WHERE memberNum = ?";

		try {
			conn = getConnection();

			// 삭제하기 전에 rental_history 테이블에서 해당 도서 번호를 참조하는 레코드를 먼저 삭제
			String rentalSQL = "DELETE FROM rental_history WHERE memberNum = ?";
			PreparedStatement rentalPstmt = conn.prepareStatement(rentalSQL);
			rentalPstmt.setInt(1, memberNum);
			rentalPstmt.executeUpdate();
			rentalPstmt.close();

			// member_db 테이블에서 회원 삭제
			pstmt = conn.prepareStatement(memberSQL);
			pstmt.setInt(1, memberNum);
			int rowsAffected = pstmt.executeUpdate();

			if (rowsAffected == 0) {
				// 삭제 실패
				JOptionPane.showMessageDialog(null, "회원정보가 삭제되지 않았습니다.", "오류", JOptionPane.WARNING_MESSAGE);
			} else if (rowsAffected == 1) {
				// 삭제 성공
				JOptionPane.showMessageDialog(null, "회원정보가 삭제되었습니다.", "완료", JOptionPane.INFORMATION_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(null, "복수의 회원 데이터가 삭제되었습니다.", "오류", JOptionPane.WARNING_MESSAGE);
			}

		} catch (SQLException e) {
			System.out.println("DB에서 회원정보 삭제 중 오류 발생 : " + e.getMessage());
			e.printStackTrace();
		} finally {
			if (pstmt != null) {
				pstmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		}

		return success;
	}

	// (UPDATE) 멤버정보를 갱신하는 메서드 정의하기
	public int updateMember(MemberDTO memberDTO) {
		int rows = 0;
		String sql = "UPDATE member_db SET password = ?, name = ?, phoneNum = ? WHERE memberNum = ?";
		Connection conn = null;
		PreparedStatement pstmt = null;
		System.out.println(memberDTO);
		try {
			// 데이터베이스 연결
			conn = getConnection();
			// 쿼리 준비
			pstmt = conn.prepareStatement(sql);

			// 쿼리 매개변수 설정
			pstmt.setString(1, memberDTO.getPassword());
			System.out.println("memberDTO.getName() : " + memberDTO.getName());
			pstmt.setString(2, memberDTO.getName());
			pstmt.setString(3, memberDTO.getPhoneNum());
			System.out.println("memberDTO.getMemberNum() : " + memberDTO.getMemberNum());
			pstmt.setInt(4, memberDTO.getMemberNum());

			// 쿼리 실행 및 결과 처리
			rows = pstmt.executeUpdate();

			if (rows == 0) {
				JOptionPane.showMessageDialog(null, "DB에서 수정된 데이터가 없습니다.", "오류", JOptionPane.WARNING_MESSAGE);
			} else if (rows == 1) {
				JOptionPane.showMessageDialog(null, "수정되었습니다.", "수정 완료", JOptionPane.INFORMATION_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(null, "DB에서 복수의 회원이 수정되었습니다.", "오류", JOptionPane.WARNING_MESSAGE);
			}
		} catch (Exception e) {
			System.out.println("DB에서 회원정보 수정 중 오류 발생: " + e.getMessage());
			e.printStackTrace();
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

	// 회원명을 조회시 리스트 가져오는 메서드 작성.
	public ArrayList<MemberDTO> getMemberSearch(String keyword) {

		Connection conn = getConnection();
		String sql = "SELECT * FROM member_db Where name like '%" + keyword + "%'";
		ResultSet rs = null;
		Statement pstmt = null;
		ArrayList<MemberDTO> memberList = new ArrayList<>();

		try {
			pstmt = conn.prepareStatement(sql);
			// pstmt.setString(1, keyword);
			rs = pstmt.executeQuery(sql);

			while (rs.next()) {
				MemberDTO dto = new MemberDTO();

				dto.setMemberNum(rs.getInt("MemberNum"));
				dto.setName(rs.getString("Name"));
				dto.setId(rs.getString("Id"));
				dto.setPassword(rs.getString("password"));
				dto.setPhoneNum(rs.getString("phonenum"));
				dto.setBirthday(rs.getString("Birthday"));

				memberList.add(dto);
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
				e.printStackTrace();
			}
		}

		System.out.println("검색된 회원 리스트 " + memberList.size());
		return memberList;
	}
}