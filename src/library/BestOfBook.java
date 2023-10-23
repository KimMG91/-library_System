package library;

import java.awt.EventQueue;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.FlowLayout;
import java.awt.BorderLayout;
import javax.swing.JList;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class BestOfBook extends JFrame {

	private JPanel contentPane;
	private JList<String> list;

	/**
	 * Launch the application.
	 */
	//테스트 완료 후 메인 닫기!!!!!!!!!!!!!!!
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					BestOfBook frame = new BestOfBook();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	//생성자 시작
	public BestOfBook() {
		setTitle("인기도서 순위");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 347, 379);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));

		JPanel panel = new JPanel();
		contentPane.add(panel);
		panel.setLayout(null);

		//제목 라벨
		JLabel lblRanking = new JLabel("인기도서 순위 TOP 10");
		lblRanking.setHorizontalAlignment(SwingConstants.CENTER);
		lblRanking.setFont(new Font("굴림", Font.BOLD, 20));
		lblRanking.setBounds(10, 10, 299, 30);
		panel.add(lblRanking);

		//인기도서 순위 띄우는 리스트
		list = new JList<String>();
		list.setFont(new Font("굴림", Font.PLAIN, 20));
		list.setBounds(0, 50, 321, 280);
		panel.add(list);

		// DB에서 인기도서 순위를 불러오는 메서드 호출
		loadData();

		// 화면 가운데로 정렬
		setLocationRelativeTo(null);
		
		 // JList 가운데 정렬
        DefaultListCellRenderer renderer = (DefaultListCellRenderer) list.getCellRenderer();
        renderer.setHorizontalAlignment(SwingConstants.CENTER);
	} //생성자 끝

	// DB에서 인기도서 순위를 불러오는 메서드
	private void loadData() {
		try {
			String url = "jdbc:mariadb://14.42.124.85/library_db"; 
			String user = "hanul";
			String password = "910725";
			Connection conn = DriverManager.getConnection(url,user,password);
			Statement stmt = conn.createStatement();

			String sqlQuery = "SELECT bd.title " +
                    "FROM book_db bd " +
                    "JOIN (" +
                    "    SELECT bookNum, SUM(1) as cumulative_ranking " +
                    "    FROM rental_history " +
                    "    GROUP BY bookNum " +
                    ") rh ON bd.bookNum = rh.bookNum " +
                    "ORDER BY rh.cumulative_ranking DESC LIMIT 10"; //상위 10개 결과만 불러오기
			
			ResultSet resultSet = stmt.executeQuery(sqlQuery);

			DefaultListModel<String> model = new DefaultListModel<>();
			int rank = 1;
            while (resultSet.next()) {
                String bookTitle = resultSet.getString("title");
                // 순위와 도서 제목을 합쳐서 리스트모델에 추가
                model.addElement(rank + "위 " + bookTitle);
                rank++;
            }
			//리스트에 리스트모델 추가
            list.setModel(model);

            // 리소스 해제
            resultSet.close();
            stmt.close();
            conn.close();
		} catch (Exception e) {
			System.out.println("DB에서 인기도서 순위를 불러오는 과정에서 오류 발생: " + e.getMessage());
			e.printStackTrace();
		}
	}
}