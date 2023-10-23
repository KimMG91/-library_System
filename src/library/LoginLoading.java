package library;

import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LoginLoading extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginLoading frame = new LoginLoading();
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
	public LoginLoading() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 544, 342);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		JPanel panel = new JPanel();
		panel.setBounds(0, 0, 528, 303);
		contentPane.add(panel);

		String gifPath = "src\\library\\images\\library_-_49375 (540p).gif"; // GIF 애니메이션
																											// 파일의 경로로
																											// 수정

		ImageIcon gifIcon = new ImageIcon(gifPath);
		panel.setLayout(null);
		JLabel gifLabel = new JLabel(gifIcon);
		gifLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				gifIcon.setImageObserver(null);

				Login uesr = new Login(); // 회원가입창으로 넘어갈수있는 키

				uesr.setVisible(true);
				dispose(); // 현재 창을 닫음
			}
		});
		gifLabel.setBounds(0, 0, 528, 303);
		gifLabel.setPreferredSize(new Dimension(223, 281));
		panel.add(gifLabel);

		// 화면 가운데로 정렬
		setLocationRelativeTo(null);

	}

}
