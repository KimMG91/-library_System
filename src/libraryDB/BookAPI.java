package libraryDB;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;

import org.json.JSONArray;
import org.json.JSONObject;

public class BookAPI {
	public static void main(String[] args) {
		
		BookDTO bookDTO = new BookDTO();
		
		try {
			// API 엔드포인트와 인증키를 적절하게 수정해주세요.
			String apiEndpoint = "https://www.nl.go.kr/seoji/SearchApi.do";
			String apiKey = "e69b574d6ace79973d10ec76faa266cb57416843908fccf1e9f2e95b60e9bedb";

			// API 호출을 위한 URL 생성
			String apiUrl = apiEndpoint + "?cert_key=" + apiKey
					+ "&result_style=json&page_no=1280&page_size=50&start_publish_date=20180801&end_publish_date=20230721";

			URL url = new URL(apiUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");

			// API 응답 읽기
			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			StringBuilder response = new StringBuilder();

			while ((line = reader.readLine()) != null) {
				response.append(line);
			}
			reader.close();

			// 예시로 응답 데이터 출력
			/*
			 * for (int i = 0; i < 1; i++) {
			 * 
			 * System.out.println(response.toString());
			 * 
			 * }
			 */

			// 응답 데이터를 JSON 파싱
			// (이 부분은 중앙도서관 API의 응답 형식에 맞춰 구현해야!!!)
			JSONObject root = new JSONObject(response.toString());
			JSONArray docsArray = root.getJSONArray("docs");
			String author;
			String publishDate; // 형식 : YYYYMMDD
			String formattedPubDate; // 형식 : YYYY-MM-DD 데이터베이스랑 일치
			// 각 책정보 중에서 필요한 필드값 추출
			System.out.println(docsArray.length());
			for (int i = 0; i < docsArray.length(); i++) {
				JSONObject docsObject = docsArray.getJSONObject(i);

				// 제목
				String title = docsObject.getString("TITLE");
				bookDTO.setTitle(title);

				// 저자
				String authorData = docsObject.getString("AUTHOR");

				// "AUTHOR" key의 value는 "저자 : ***" or "지은이: ***" 형태
				// "***" 즉, 저자명만 추출하기
				String[] parts = authorData.split(": ");
				// System.out.println(parts.length); 
				// System.out.println(parts[0]); //"저자" or "지은이"
				// System.out.println(parts[1]); 
				if (parts.length >= 2) {
					author = parts[1];
				}else {
					author = authorData;
				}
				bookDTO.setAuthor(author);
				
				// 출판사
				String publisher = docsObject.getString("PUBLISHER");
				bookDTO.setPublisher(publisher);

				// 발행일
				if (docsObject.getString("PUBLISH_PREDATE") == "") {
					publishDate = docsObject.getString("REAL_PUBLISH_DATE");
				} else if (docsObject.getString("PUBLISH_PREDATE") != null) {
					publishDate = docsObject.getString("PUBLISH_PREDATE");
				} else {
					publishDate = "";
				}
				// 발행일 형식 바꾸기
				SimpleDateFormat originalFormat = new SimpleDateFormat("yyyyMMdd");
				SimpleDateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");

				try {
					Date date = originalFormat.parse(publishDate);
					formattedPubDate = targetFormat.format(date);
					bookDTO.setPubYear(formattedPubDate);
					
					//test용 콘솔에 출력
					//System.out.println("책제목 : " + title + "\n저자 : " + author + "\n출판사 : " + publisher + "\n출판일 : "
					//		+ formattedPubDate);
					//System.out.println(bookDTO);
					BookDAO bookDAO = new BookDAO();
					bookDAO.insertBook(bookDTO);
				} catch (ParseException e) {
					System.out.println(e.getMessage());
					//e.printStackTrace();
				}

			}
			// System.out.println(response.toString());

			conn.disconnect();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
