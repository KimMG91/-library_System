package library;

import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import libraryDB.MemberDTO;

import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;

public class Menu extends JFrame {

	/**
	 * Create the frame.
	 */

	// 일반회원 메뉴 생성자(Constructor) 시작
	public Menu(MemberDTO memberDTO) { // 로그인한 객체 파람으로 넘겨받기
		setTitle("메뉴");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 383, 383);
		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		JPanel panel = new JPanel();
		panel.setBounds(0, 0, 367, 344);
		contentPane.add(panel);
		panel.setLayout(null);


		//로고 이미지 경로 설정
		String imagePath = "src\\library\\images\\logo_transparent2.png";
		// 이미지 크기 조절
		int width = 300; // 원하는 가로 크기
		int height = 270; // 원하는 세로 크기
		ImageIcon originalImageIcon = new ImageIcon(imagePath);
		Image originalImage = originalImageIcon.getImage();
		Image resizedImage = originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
		ImageIcon resizedImageIcon = new ImageIcon(resizedImage);

		//라벨에 로고 이미지 넣기
		JLabel lblNewLabel = new JLabel(resizedImageIcon);
		lblNewLabel.setBounds(12, 10, 338, 139);
		//판넬에 로고 라벨 추가
		panel.add(lblNewLabel);
		
		
		// 내정보보기 VS 마이페이지 : 뭘로 통일할지~?!!!!!!!!!!!!!!!!!!!!!!!
		// 내정보 보기 버튼
		JButton btnNewButton = new JButton("내정보 보기");
		btnNewButton.setFont(new Font("굴림", Font.PLAIN, 15));
		btnNewButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				try {
					MyPage myPage = new MyPage(memberDTO); // 로그인한 객체 파람으로 넘겨주기
					myPage.setVisible(true);
					dispose();
				} catch (Exception e1) {
					System.out.println("내정보보기 버튼 오류 : " + e1.getMessage());
					e1.printStackTrace();
				}
			}
		});
		btnNewButton.setForeground(new Color(0, 0, 0));
		btnNewButton.setBounds(29, 159, 139, 32);
		panel.add(btnNewButton);

		// 도서검색 버튼
		JButton btnNewButton_1 = new JButton("도서검색");
		btnNewButton_1.setFont(new Font("굴림", Font.PLAIN, 15));
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				BookSearch bookSearch = new BookSearch();
				bookSearch.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE); // 팝업 창으로 띄우기
				bookSearch.setVisible(true);

			}
		});
		btnNewButton_1.setForeground(new Color(0, 0, 0));
		btnNewButton_1.setBounds(197, 159, 139, 32);
		panel.add(btnNewButton_1);

		// 다독자 순위 버튼
		JButton btnNewButton_1_1 = new JButton("다독자 순위");
		btnNewButton_1_1.setFont(new Font("굴림", Font.PLAIN, 15));
		btnNewButton_1_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				KingOfReader kingOfReader = new KingOfReader();
				kingOfReader.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE); // 팝업 창으로 띄우기
				kingOfReader.setVisible(true);
			}
		});
		btnNewButton_1_1.setForeground(new Color(0, 0, 0));
		btnNewButton_1_1.setBounds(197, 277, 139, 23);
		panel.add(btnNewButton_1_1);

		// 인기도서 순위 버튼
		JButton btnNewButton_1_1_2 = new JButton("인기도서 순위");
		btnNewButton_1_1_2.setFont(new Font("굴림", Font.PLAIN, 15));
		btnNewButton_1_1_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				BestOfBook bestOfBook = new BestOfBook();
				bestOfBook.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE); // 팝업 창으로 띄우기
				bestOfBook.setVisible(true);
			}
		});
		btnNewButton_1_1_2.setForeground(Color.BLACK);
		btnNewButton_1_1_2.setBounds(197, 244, 139, 23);
		panel.add(btnNewButton_1_1_2);

		// 미니게임 버튼
		JButton btnNewButton_1_1_1 = new JButton("미니게임");
		btnNewButton_1_1_1.setFont(new Font("굴림", Font.PLAIN, 15));
		btnNewButton_1_1_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Quiz quiz = new Quiz();
				quiz.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE); // 팝업 창으로 띄우기
				quiz.setVisible(true);
			}
		});
		btnNewButton_1_1_1.setForeground(new Color(0, 0, 0));
		btnNewButton_1_1_1.setBounds(197, 311, 139, 23);
		panel.add(btnNewButton_1_1_1);

		//로그아웃 버튼
		JButton logOutButton = new JButton("로그아웃");
		logOutButton.setFont(new Font("굴림", Font.PLAIN, 16));
		logOutButton.setForeground(new Color(0, 0, 0));
		logOutButton.setBounds(29, 302, 117, 32);
		logOutButton.addActionListener(e -> {
			// 로그아웃 버튼이 클릭되었을 때 수행할 동작을 여기에 작성
			Login login = new Login();
			login.setVisible(true); // 로그인 화면을 보여줌
			dispose(); // 일반회원 메뉴 창 닫음
		});
		panel.add(logOutButton);

		// 화면 가운데로 정렬
		setLocationRelativeTo(null);

	}// 일반회원 메뉴 생성자(Constructor) 끝
	
	
	/*
	 * public static void main(String[] args) { EventQueue.invokeLater(new
	 * Runnable() { public void run() { try { Menu frame = new Menu();
	 * frame.setVisible(true); } catch (Exception e) { e.printStackTrace(); } } });
	 * }
	 */
}
