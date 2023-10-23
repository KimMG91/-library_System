package library;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import libraryDB.MemberDAO;
import libraryDB.MemberDTO;

import javax.swing.JTextField;
import java.awt.Font;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class MemberDelete extends JFrame {

	private JPanel contentPane;

	/**
	 * Create the frame.
	 */
	public MemberDelete(MemberDTO selectedMember) {

		setTitle("회원삭제 (관리자)");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 373, 104);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JPanel panel = new JPanel();
		panel.setBounds(0, 0, 357, 65);
		contentPane.add(panel);
		panel.setLayout(null);

		JButton btn_MemberDelete = new JButton("삭제");
		btn_MemberDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// 선택된 회원 정보가 null이 아니라면
				if (selectedMember != null) {
					try {
						// 회원 삭제 메서드 호출
						boolean isDeleted = MemberDAO.getInstance().deleteMember(selectedMember.getMemberNum());
						if (isDeleted) {
							System.out.println("회원 삭제가 완료되었습니다.");
							// 삭제 성공 시 회원 목록 화면으로 돌아가도록 처리
							dispose();
							MemberManagement memberManagement = new MemberManagement();
							memberManagement.setVisible(true);
						} else {
							System.out.println("회원 삭제에 실패하였습니다.");
						}
					} catch (Exception ex) {
						ex.printStackTrace();
						System.out.println("회원 삭제 중 오류가 발생하였습니다: " + ex.getMessage());
					}
				}
			}
		});

		btn_MemberDelete.setFont(new Font("굴림", Font.PLAIN, 24));
		btn_MemberDelete.setBounds(46, 21, 100, 30);
		panel.add(btn_MemberDelete);

		JButton btn_cancel = new JButton("취소");
		btn_cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// 취소 버튼을 누르면 회원 목록 화면으로 돌아가도록 처리
				dispose();
				MemberManagement memberManagement = new MemberManagement();
				memberManagement.setVisible(true);
			}
		});
		btn_cancel.setFont(new Font("굴림", Font.PLAIN, 24));
		btn_cancel.setBounds(218, 21, 100, 30);
		panel.add(btn_cancel);

		// 화면 가운데로 정렬
		setLocationRelativeTo(null);
	}
}
