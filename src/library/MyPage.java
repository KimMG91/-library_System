package library;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

import libraryDB.BookDTO;
import libraryDB.MemberDAO;
import libraryDB.MemberDTO;
import libraryDB.RentalHistoryDAO;
import javax.swing.JSeparator;

public class MyPage extends JFrame {

    private JPanel contentPane;
    private JTable nowRentalsTable;
    private JTable totalRentalsTable;
    RentalHistoryDAO rentalHistory = new RentalHistoryDAO();

    /**
     * Launch the application.
     */

    /**
     * Create the frame.
     *
     * @throws Exception
     */
    // 마이페이지 생성자(Constructor) 시작
    public MyPage(MemberDTO memberDTO) throws Exception { // 로그인한 객체 파람으로 넘겨받기
        // 내정보보기 VS 마이페이지 : 뭘로 통일할지~?!!!!!!!!!!!!!!!!!!!!!!!
        setTitle("마이페이지");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1006, 646);
        setLocationRelativeTo(null); // 창을 가운데로 위치시키기
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        setContentPane(contentPane);
        contentPane.setLayout(null);

        JPanel panel = new JPanel();
        panel.setBounds(0, 0, 990, 607);
        contentPane.add(panel);
        panel.setLayout(null);

        // 특정 회원의 대여현황 띄우기
        // 대여현황 제목 라벨
        JLabel lbl_NowRental = new JLabel("대여현황 : " + rentalHistory.countNowRental(memberDTO.getMemberNum()));
        lbl_NowRental.setFont(new Font("굴림", Font.PLAIN, 18));
        lbl_NowRental.setHorizontalAlignment(SwingConstants.CENTER);
        lbl_NowRental.setBounds(27, 10, 933, 31);
        panel.add(lbl_NowRental);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(27, 51, 933, 107);
        panel.add(scrollPane);

        // 대여현황 띄우는 테이블
        nowRentalsTable = new JTable();
        nowRentalsTable.setFont(new Font("굴림", Font.PLAIN, 18));
        DefaultTableModel nowRentals = new DefaultTableModel();

        nowRentals.addColumn("도서번호");
        nowRentals.addColumn("도서명");
        nowRentals.addColumn("저자명");
        nowRentals.addColumn("대여일");
        nowRentals.addColumn("반납예정일");
        //nowRentalsTable.setModel(nowRentals);
       // nowRentalsTable.getColumnModel().getColumn(0).setPreferredWidth(40);

        // 대여현황 DB에서 불러오는 메서드 호출
        ArrayList<BookDTO> rentalBookList = rentalHistory.getNowRentals(memberDTO.getMemberNum());
        // System.out.println(rentalBookList);
        for (BookDTO rental : rentalBookList) {
            nowRentals.addRow(new Object[]{rental.getBookNum(), rental.getTitle(), rental.getAuthor(),
                    rental.getRentDt(), rental.getDueDate(rental.getRentDt())});
        }
        nowRentalsTable.setModel(nowRentals);
        nowRentalsTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        nowRentalsTable.setRowHeight(30);
        scrollPane.setViewportView(nowRentalsTable);

        // 특정 회원의 누적 대여목록 띄우기

        // 누적 대여목록 제목 라벨
        JLabel lbl_TotalRentBook = new JLabel("누적 대여기록 : " + rentalHistory.countTotalRentals(memberDTO.getMemberNum()));
        lbl_TotalRentBook.setFont(new Font("굴림", Font.PLAIN, 18));
        lbl_TotalRentBook.setHorizontalAlignment(SwingConstants.CENTER);
        lbl_TotalRentBook.setBounds(27, 168, 933, 26);
        panel.add(lbl_TotalRentBook);

        JScrollPane scrollPane_1 = new JScrollPane();
        scrollPane_1.setBounds(27, 204, 933, 145);
        panel.add(scrollPane_1);

        // 누적 대여목록 띄우는 테이블
        totalRentalsTable = new JTable();
        totalRentalsTable.setFont(new Font("굴림", Font.PLAIN, 18));
        scrollPane_1.setViewportView(totalRentalsTable);
        DefaultTableModel totalRentals = new DefaultTableModel();

        totalRentals.addColumn("도서번호");
        totalRentals.addColumn("도서명");
        totalRentals.addColumn("저자명");
        totalRentals.addColumn("대여일");
        totalRentals.addColumn("반납일");
        //totalRentalsTable.setModel(totalRentals);
        //totalRentalsTable.getColumnModel().getColumn(0).setPreferredWidth(40);

        // 누적 대여목록 DB에서 불러오는 메서드 호출
        ArrayList<BookDTO> totalRentalsList = rentalHistory.getTotalRentals(memberDTO.getMemberNum());
        for (BookDTO rental : totalRentalsList) {
            totalRentals.addRow(new Object[]{rental.getBookNum(), rental.getTitle(), rental.getAuthor(),
                    rental.getRentDt(), rental.getReturnDt()});
        }
        totalRentalsTable.setModel(totalRentals);
        scrollPane_1.setViewportView(totalRentalsTable);
        totalRentalsTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        totalRentalsTable.setRowHeight(30);

        // 개인정보 출력

        // 이름 정보 (수정불가)
        JLabel lbl_name = new JLabel("이름");
        lbl_name.setFont(new Font("굴림", Font.PLAIN, 18));
        lbl_name.setBounds(27, 356, 196, 27);
        panel.add(lbl_name);

        JLabel lbl_nameValue = new JLabel();
        lbl_nameValue.setFont(new Font("굴림", Font.PLAIN, 18));
        lbl_nameValue.setBounds(235, 356, 178, 27);
        panel.add(lbl_nameValue);
        lbl_nameValue.setText(memberDTO.getName());

        // 생년월일 정보 (수정불가)
        JLabel lbl_birthday = new JLabel("생년월일");
        lbl_birthday.setFont(new Font("굴림", Font.PLAIN, 18));
        lbl_birthday.setBounds(27, 382, 196, 27);
        panel.add(lbl_birthday);

        JLabel lbl_birthdayValue = new JLabel();
        lbl_birthdayValue.setFont(new Font("굴림", Font.PLAIN, 18));
        lbl_birthdayValue.setBounds(235, 382, 178, 27);
        panel.add(lbl_birthdayValue);
        lbl_birthdayValue.setText(memberDTO.getBirthday());

        // 아이디 정보 (수정불가)
        JLabel lbl_id = new JLabel("아이디");
        lbl_id.setFont(new Font("굴림", Font.PLAIN, 18));
        lbl_id.setBounds(27, 408, 196, 26);
        panel.add(lbl_id);

        JLabel lbl_idValue = new JLabel();
        lbl_idValue.setFont(new Font("굴림", Font.PLAIN, 18));
        lbl_idValue.setBounds(235, 408, 178, 27);
        panel.add(lbl_idValue);
        lbl_idValue.setText(memberDTO.getId());

        // 변경할 비밀번호 입력칸 (수정 가능)
        JLabel lbl_password = new JLabel("변경할 비밀번호");
        lbl_password.setFont(new Font("굴림", Font.PLAIN, 18));
        lbl_password.setBounds(27, 483, 196, 24);
        panel.add(lbl_password);

        JPasswordField pwCharArr1_1 = new JPasswordField();
        pwCharArr1_1.setFont(new Font("굴림", Font.PLAIN, 18));
        pwCharArr1_1.setColumns(10);
        pwCharArr1_1.setBounds(235, 480, 178, 31);
        panel.add(pwCharArr1_1);

        // 변경할 비밀번호 확인칸
        JLabel lbl_password_1 = new JLabel("비밀번호 확인");
        lbl_password_1.setFont(new Font("굴림", Font.PLAIN, 18));
        lbl_password_1.setBounds(27, 518, 196, 24);
        panel.add(lbl_password_1);

        JPasswordField pwCharArr1_2 = new JPasswordField();
        pwCharArr1_2.setFont(new Font("굴림", Font.PLAIN, 18));
        pwCharArr1_2.setColumns(10);
        pwCharArr1_2.setBounds(235, 516, 178, 28);
        panel.add(pwCharArr1_2);

        // 비밀번호 힌트 라벨
        JLabel passwordHintLabel = new JLabel();
        passwordHintLabel.setFont(new Font("굴림", Font.PLAIN, 15));
        passwordHintLabel.setBounds(423, 488, 246, 21);
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
                char[] pwCharArr1 = pwCharArr1_1.getPassword();
                String pwInput1 = new String(pwCharArr1);
                char[] pwCharArr2 = pwCharArr1_2.getPassword();
                String pwInput2 = new String(pwCharArr2);

                // 비번 길이검증
                if (isPasswordLengthValid(pwCharArr1)) {// 길이 조건을 만족할 경우

                    if (!pwInput1.equals(pwInput2)) {
                        passwordHintLabel.setText("두 비밀번호가 일치하지 않습니다.");
                    } else if (pwInput1.equals(memberDTO.getPassword())) {
                        passwordHintLabel.setText("기존 비밀번호와 중복됩니다.");
                    } else {
                        passwordHintLabel.setText("비밀번호가 일치하며, 조건을 만족합니다.");
                    }
                } else {// 길이 조건을 만족하지 않을 경우
                    passwordHintLabel.setText("비밀번호는 8자 이상 15자 이하여야 합니다.");
                }
            }
        };

        pwCharArr1_1.getDocument().addDocumentListener(documentListener);
        pwCharArr1_2.getDocument().addDocumentListener(documentListener);

        // 전화번호 정보 (수정불가)
        JLabel lbl_phoneNum = new JLabel("전화번호");
        lbl_phoneNum.setFont(new Font("굴림", Font.PLAIN, 18));
        lbl_phoneNum.setBounds(27, 435, 196, 21);
        panel.add(lbl_phoneNum);

        JLabel lbl_phoneValue = new JLabel();
        lbl_phoneValue.setFont(new Font("굴림", Font.PLAIN, 18));
        lbl_phoneValue.setBounds(235, 434, 178, 25);
        panel.add(lbl_phoneValue);
        lbl_phoneValue.setText(memberDTO.getPhoneNum());

        // 수정 버튼
        JButton btn_update = new JButton("\uBE44\uBC00\uBC88\uD638 \uBCC0\uACBD");
        btn_update.setFont(new Font("굴림", Font.PLAIN, 18));
        btn_update.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                MemberDAO dao = new MemberDAO();

                char[] pwCharArr1 = pwCharArr1_1.getPassword();
                String pwInput1 = new String(pwCharArr1);
                char[] pwCharArr2 = pwCharArr1_2.getPassword();
                String pwInput2 = new String(pwCharArr2);

                if (!pwInput1.equals(pwInput2)) {
                    JOptionPane.showMessageDialog(null, "두 비밀번호가 일치하지 않습니다.", "수정 실패", JOptionPane.ERROR_MESSAGE);
                } else if (pwInput1.equals(memberDTO.getPassword())) {
                    JOptionPane.showMessageDialog(null, "기존 비밀번호와 중복됩니다.", "수정 실패", JOptionPane.ERROR_MESSAGE);
                } else if (!isPasswordLengthValid(pwCharArr1)) {
                    JOptionPane.showMessageDialog(null, "비밀번호는 8자 이상 15자 이하여야 합니다.", "수정 실패", JOptionPane.ERROR_MESSAGE);
                } else {
                    memberDTO.setPassword(pwInput1);

                    try {
                        // DB의 회원정보 수정하는 메서드 호출
                        dao.updateMember(memberDTO);
                        JOptionPane.showMessageDialog(null, "다시 로그인 해주세요(✿◡‿◡)","재로그인 요청",JOptionPane.INFORMATION_MESSAGE);
                        dispose();
                        Login login = new Login();
                        login.setVisible(true);
                    } catch (Exception e1) {
                        System.out.println("updateMember 메서드 호출 오류 : " + e1.getMessage());
                        e1.getStackTrace();
                    }
                }
            }
        });
        btn_update.setBounds(682, 551, 158, 32);
        panel.add(btn_update);

        // 회원 탈퇴 버튼
        JButton btn_delete = new JButton("회원탈퇴");
        btn_delete.setFont(new Font("굴림", Font.PLAIN, 18));
        btn_delete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { // 회원탈퇴 버튼 클릭시
            	 MemberDAO memberDAO = new MemberDAO();
            	 
            	  int confirmResult = JOptionPane.showConfirmDialog(null, "정말 회원탈퇴 하시겠습니까?", "회원탈퇴", JOptionPane.YES_NO_OPTION);
                  if (confirmResult == JOptionPane.YES_OPTION) {
                      RentalHistoryDAO rentalHistoryDAO = new RentalHistoryDAO();
					// 회원탈퇴 처리를 위한 코드 추가
                      ArrayList<BookDTO> nowRentalList = rentalHistoryDAO.getNowRentals(memberDTO.getMemberNum()); // 현재 대여중인 도서 목록 가져오기
                      if (nowRentalList.size() > 0) {
                          // 현재 대여중인 도서가 있을 경우
                          JOptionPane.showMessageDialog(null, "현재 대여중인 도서가 있으므로 회원탈퇴가 불가능합니다.", "회원탈퇴 실패", JOptionPane.ERROR_MESSAGE);
                      } else {
                          // 현재 대여중인 도서가 없을 경우 회원탈퇴 처리
                          try {
							memberDAO.deleteMember(memberDTO.getMemberNum());
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} // 회원탈퇴 메서드 호출
                          JOptionPane.showMessageDialog(null, "회원탈퇴가 완료되었습니다.", "회원탈퇴 성공", JOptionPane.INFORMATION_MESSAGE);
                          dispose(); // 마이페이지 창 닫기
                          Login login = new Login(); // 로그인창 새창으로 띄우기
                          login.setVisible(true);
                      }
                  }
            	
               

            }
        });
        btn_delete.setBounds(852, 551, 108, 32);
        panel.add(btn_delete);

        // 뒤로 가기 버튼
        JButton btn_back = new JButton("뒤로 가기");
        btn_back.setFont(new Font("굴림", Font.PLAIN, 18));
        btn_back.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose(); // 마이페이지 창 닫기
                setLocationRelativeTo(null);
                Menu menu = new Menu(memberDTO);
                menu.setVisible(true);
            }
        });
        btn_back.setBounds(27, 551, 130, 32);
        panel.add(btn_back);
        
        JSeparator separator = new JSeparator();
        separator.setBounds(27, 470, 652, 2);
        panel.add(separator);

        // 화면 가운데로 정렬
        setLocationRelativeTo(null);

    } // 마이페이지 생성자(Constructor) 끝

    private boolean isPasswordLengthValid(char[] password) {
        // 비밀번호의 길이가 8자 이상 15자 이하인지 확인합니다.
        int length = password.length;
        return length >= 8 && length <= 15;
    }
}