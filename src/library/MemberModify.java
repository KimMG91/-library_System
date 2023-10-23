package library;

import com.toedter.calendar.JDateChooser;

import libraryDB.BookDAO;
import libraryDB.BookDTO;
import libraryDB.MemberDAO;
import libraryDB.MemberDTO;

import java.awt.EventQueue;
import java.awt.Image;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class MemberModify extends JFrame {

	private JPanel contentPane;


	String imagePath = "src\\library\\images\\훈민정음.jpg";
	// 이미지 크기 조절
	int width = 545; // 원하는 가로 크기
	int height = 326; // 원하는 세로 크기
	ImageIcon originalImageIcon = new ImageIcon(imagePath);
	Image originalImage = originalImageIcon.getImage();
	Image resizedImage = originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);

	// 조절된 이미지로 ImageIcon 생성
	ImageIcon resizedImageIcon = new ImageIcon(resizedImage);


	/**
	 * Create the frame.
	 */

	// 신규회원버튼으로 생성시
	public MemberModify(MemberDTO selectedMember) {
		super("창2");
		setTitle("\uD68C\uC6D0\uC218\uC815");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 523, 569);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);

		JPanel panel = new JPanel();
		panel.setBounds(0, 0, 507, 530);
		contentPane.setLayout(null);
		contentPane.add(panel);
		panel.setLayout(null);

		JLabel lbl_name = new JLabel("성명");
		lbl_name.setBounds(26, 31, 48, 23);
		panel.add(lbl_name);

		JLabel lbl_birthday = new JLabel("생년월일");
		lbl_birthday.setBounds(26, 100, 91, 23);
		panel.add(lbl_birthday);

		JLabel lblNewLabel_3 = new JLabel("회원 ID");
		lblNewLabel_3.setBounds(26, 169, 60, 23);
		panel.add(lblNewLabel_3);

		JLabel lblNewLabel_5 = new JLabel("비밀번호");
		lblNewLabel_5.setBounds(26, 238, 70, 23);
		panel.add(lblNewLabel_5);

		JLabel lblNewLabel_5_1 = new JLabel("비밀번호 확인");
		lblNewLabel_5_1.setBounds(26, 307, 91, 23);
		panel.add(lblNewLabel_5_1);

		JLabel lbl_phoneNum = new JLabel("전화번호");
		lbl_phoneNum.setBounds(26, 382, 81, 23);
		panel.add(lbl_phoneNum);

		TextField textField_name = new TextField();
		textField_name.setBounds(168, 31, 159, 23);
		panel.add(textField_name);
		textField_name.setText(selectedMember.getName());

		JLabel lbl_birthdayValue = new JLabel();
		lbl_birthdayValue.setBounds(168, 101, 159, 21);
		panel.add(lbl_birthdayValue);
		lbl_birthdayValue.setText(selectedMember.getBirthday());

		JButton btnNewButton_2 = new JButton("뒤로가기");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MemberManagement memberManagement = new MemberManagement();
				memberManagement.setVisible(true);
				dispose();
			}
		});
		btnNewButton_2.setBounds(134, 497, 97, 23);
		panel.add(btnNewButton_2);

		JLabel lbl_id = new JLabel();
		lbl_id.setBounds(168, 169, 159, 23);
		panel.add(lbl_id);
		lbl_id.setText(selectedMember.getId());
		JLabel hintLabel = new JLabel();
		hintLabel.setBounds(168, 194, 266, 20); // 힌트 표시 위치와 크기 조정
		panel.add(hintLabel);

		TextField textField_phoneNum = new TextField();
		textField_phoneNum.setBounds(168, 382, 159, 23);
		panel.add(textField_phoneNum);
		textField_phoneNum.setText(selectedMember.getPhoneNum());

		JPasswordField password1_1 = new JPasswordField();
		password1_1.setBounds(168, 239, 159, 21);
		panel.add(password1_1);
		password1_1.setText(selectedMember.getPassword());

		JPasswordField password1_2 = new JPasswordField();
		password1_2.setBounds(168, 308, 159, 21);
		panel.add(password1_2);
		password1_2.setText(selectedMember.getPassword());

		JLabel passwordHintLabel = new JLabel();
		passwordHintLabel.setBounds(165, 339, 239, 20); // 힌트 표시 위치와 크기 조정
		panel.add(passwordHintLabel);

		// DocumentListener를 사용하여 입력된 비밀번호 값이 변경될 때마다 힌트를 설정하는 로직
		DocumentListener documentListener = new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				comparePasswords();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				comparePasswords();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				comparePasswords();
			}

			private void comparePasswords() {
				char[] password1 = password1_1.getPassword();
				char[] password2 = password1_2.getPassword();

				if (passwordMatches(password1, password2)) {
					if (isPasswordLengthValid(password1)) {
						// 비밀번호가 일치하고 길이 조건을 만족할 경우 힌트를 설정합니다.
						passwordHintLabel.setText("비밀번호가 일치하며, 조건을 만족합니다.");
						handleValidatedPassword(new String(password1));
					} else {
						// 비밀번호가 일치하지만 길이 조건을 만족하지 않을 경우 힌트를 설정합니다.
						passwordHintLabel.setText("비밀번호는 8자 이상 15자 이하여야 합니다.");
					}
				} else {
					// 비밀번호가 일치하지 않을 경우 틀렸다는 메시지를 설정합니다.
					passwordHintLabel.setText("비밀번호가 일치하지 않습니다.");
				}
			}

			private void handleValidatedPassword(String password) {
				// 검증이 완료된 비밀번호 값을 이용하여 다른 로직을 처리하는 코드를 작성합니다.
				// 예: 회원가입 데이터 처리 등
				System.out.println("검증 완료된 비밀번호: " + password);
			}

			private boolean isPasswordLengthValid(char[] password) {
				// 비밀번호의 길이가 8자 이상 15자 이하인지 확인합니다.
				int length = password.length;
				return length >= 8 && length <= 15;
			}

			private boolean passwordMatches(char[] password1, char[] password2) {
				// 입력된 두 비밀번호가 같은지를 확인합니다.
				return new String(password1).equals(new String(password2));
			}
		};

		password1_1.getDocument().addDocumentListener(documentListener);
		password1_2.getDocument().addDocumentListener(documentListener);

		JLabel phoneNumHintLabel = new JLabel();
		phoneNumHintLabel.setBounds(165, 413, 239, 20); // 힌트 표시 위치와 크기 조정
		panel.add(phoneNumHintLabel);

		JButton phoneNumButton = new JButton("중복검사");
		phoneNumButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String phoneNumber = textField_phoneNum.getText();
				MemberDAO memberDAO = new MemberDAO();
				selectedMember.setPhoneNum(phoneNumber);
				try {
					String result = memberDAO.insertPhoneNum(selectedMember);
					phoneNumHintLabel.setText(result); // 힌트 패널에 결과 설정
				} catch (Exception ex) {
					System.out.println("전화번호 검사 중 오류가 발생하였습니다: " + ex.getMessage());
				}
			}
		});
		phoneNumButton.setBounds(337, 382, 97, 23);
		panel.add(phoneNumButton);

		JLabel alertLabel = new JLabel();
		alertLabel.setBounds(83, 463, 412, 23); // 알람 표시 위치와 크기 조정
		panel.add(alertLabel);

		JButton btnNewButton_3 = new JButton("확인");
		btnNewButton_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				// bookNumTF에서 값을 가져와서 int 타입으로 변환
				selectedMember.setId(lbl_id.getText());
				selectedMember.setPassword(new String(password1_1.getPassword()));
				selectedMember.setName(textField_name.getText());
				selectedMember.setPhoneNum(textField_phoneNum.getText());

				try {
					// BookDAO를 생성하여 데이터베이스에 연결
					MemberDAO dao = new MemberDAO();
					// BookDAO의 updateBook 메서드를 호출하여 데이터베이스를 업데이트
					dao.updateMember(selectedMember);
					System.out.println("수정완료 !");
					JOptionPane.showMessageDialog(null, "수정 되었습니다.", "수정 완료", JOptionPane.INFORMATION_MESSAGE);
					dispose();
				} catch (Exception ex) {
					// 업데이트가 실패했을 경우, 오류 메시지를 출력하거나 필요한 작업을 수행할 수 있습니다.
					System.out.println("업데이트 실패" + ex.getMessage());
				}
			}
		});
		btnNewButton_3.setBounds(268, 497, 97, 23);
		panel.add(btnNewButton_3);

		// 화면 가운데로 정렬
		setLocationRelativeTo(null);
	}

}
