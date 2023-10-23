package libraryDB;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BookDTO {

	final int RENTAL_PERIOD = 5;
	private int bookNum; // 도서번호 => 대여목록에 연동(foreign key)
	private String title; // 도서명
	private String author; // 작가명
	private String publisher; // 출판사
	private String pubYear; // 출판일 <--- YYYY-MM-DD 형식
	private String state; // 상태 (대여중, 대여가능) <--- 대여목록에서 끌어올 값
	private Timestamp rentDt; // 도서 대여 날짜 <--- 대여목록에서 끌어올 값
	private Timestamp returnDt; // 도서 반납 날짜 <--- 대여목록에서 끌어올 값
	// private String dueDate = getDueDate(rentDt);

	public Timestamp getDueDate(Timestamp rentDt) {

		// Calendar 객체를 생성하고 대여일을 설정합니다.
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(rentDt);

		// 대여기간을 더합니다.
		calendar.add(Calendar.DAY_OF_MONTH, RENTAL_PERIOD);// 도서 반납 예정일 : rentDt + RENTAL_PERIOD

		// 더해진 날짜를 얻어옵니다.
		Timestamp dueDate = new Timestamp(calendar.getTime().getTime());

		return dueDate;
	}

	// DB에 이미지저장하면 비효율적이므로 이미지는 따로 물리적 폴더에 저장하고 DB에는 이미지 경로만 저장하는 게 일반적

	// private boolean available; // 도서 대여 상태 : 대여가능, 대여중 -> 대여목록에 기록
	// private String field; //도서 분야 //과학,사회,역사,수필,여행,외국어,......

	// private int rentalNum; //도서 누적 대여된 횟수 => 인기도서랭킹에 활용
}
