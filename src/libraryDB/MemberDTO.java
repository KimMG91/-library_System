package libraryDB;

import java.sql.Date;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor

public class MemberDTO {
	
	final int RENTAL_PERIOD = 5;
	private int memberNum; //회원 번호
	private String id; // 아이디 
	private String password; //비밀번호
	private String name; //이름
	private String phoneNum; // 전화번호
	private String birthday; //생년월일 <--- YYYY-MM-DD 형식
	private String isManager; //관리자 여부(Y/N)
	//private String state; // 대여중, 대여가능 -> 대여목록에 기록
	
	//private Date returnDate; // 도서 반납일
	//private int overdueTimes; // 도서 연체 횟수 //3번 연체 시 강퇴

}
