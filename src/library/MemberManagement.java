package library;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import libraryDB.BookDAO;
import libraryDB.BookDTO;
import libraryDB.MemberDAO;
import libraryDB.MemberDTO;
import libraryDB.RentalHistoryDAO;

public class MemberManagement extends JFrame {

   private JPanel contentPane;
   private JTextField memberIdTF;
   private JTextField memberNameTF;
   private JTextField memberPwTF;
   private JTextField memberPhoneNumTF;
   private JTextField textFieldSearch;
   private JTable table;

   /**
    * Create the frame.
    */
   public MemberManagement() {
      MemberDTO selectedMember = new MemberDTO();
      setTitle("회원관리");
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      setBounds(100, 100, 681, 803);
      setLocationRelativeTo(null); // 창을 가운데로 위치시키기
      contentPane = new JPanel();
      contentPane.setToolTipText("");
      contentPane.setBackground(new Color(255, 255, 255));
      contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

      setContentPane(contentPane);
      contentPane.setLayout(null);

      // 회원번호 라벨
      JLabel MemberNumLB = new JLabel("회원번호");
      MemberNumLB.setFont(new Font("굴림", Font.PLAIN, 20));
      MemberNumLB.setBounds(91, 403, 83, 36);
      MemberNumLB.setHorizontalAlignment(JLabel.CENTER);
      contentPane.add(MemberNumLB);

      // 회원번호 (수정불가)
      JLabel memberNumLA = new JLabel("");
      memberNumLA.setFont(new Font("굴림", Font.BOLD, 19));
      memberNumLA.setBounds(225, 403, 319, 36);
      contentPane.add(memberNumLA);

      // 이름 라벨
      JLabel memberNameLB = new JLabel("이름");
      memberNameLB.setFont(new Font("굴림", Font.PLAIN, 20));
      memberNameLB.setHorizontalAlignment(SwingConstants.CENTER);
      memberNameLB.setBounds(78, 449, 106, 33);
      contentPane.add(memberNameLB);

      // 비밀 텍스트필드
      memberPwTF = new JTextField();
      memberPwTF.setFont(new Font("굴림", Font.PLAIN, 16));
      memberPwTF.setColumns(10);
      memberPwTF.setBounds(225, 531, 319, 27);
      contentPane.add(memberPwTF);

      // 아이디 라벨
      JLabel memberIdLB = new JLabel("아이디");
      memberIdLB.setFont(new Font("굴림", Font.PLAIN, 20));
      memberIdLB.setHorizontalAlignment(SwingConstants.CENTER);
      memberIdLB.setBounds(78, 488, 106, 33);
      contentPane.add(memberIdLB);

      // 아이디 (수정불가)
      JLabel memberIDLA = new JLabel("");
      memberIDLA.setFont(new Font("굴림", Font.BOLD, 19));
      memberIDLA.setBounds(225, 485, 319, 36);
      contentPane.add(memberIDLA);

      // 비밀번호 라벨
      JLabel memberPwLB = new JLabel("비밀번호");
      memberPwLB.setFont(new Font("굴림", Font.PLAIN, 20));
      memberPwLB.setHorizontalAlignment(JLabel.CENTER);
      memberPwLB.setBounds(91, 519, 83, 63);
      contentPane.add(memberPwLB);

      // 이름 텍스트필드
      memberNameTF = new JTextField();
      memberNameTF.setFont(new Font("굴림", Font.PLAIN, 16));
      memberNameTF.setBounds(225, 453, 319, 27);
      contentPane.add(memberNameTF);
      memberNameTF.setColumns(10);

      // 전화번호 라벨
      JLabel memberPhoneNumLB = new JLabel("전화번호");
      memberPhoneNumLB.setFont(new Font("굴림", Font.PLAIN, 20));
      memberPhoneNumLB.setHorizontalAlignment(SwingConstants.CENTER);
      memberPhoneNumLB.setBounds(91, 568, 83, 49);
      contentPane.add(memberPhoneNumLB);

      // 전화번호 텍스트필드
      memberPhoneNumTF = new JTextField();
      memberPhoneNumTF.setFont(new Font("굴림", Font.PLAIN, 16));
      memberPhoneNumTF.setColumns(10);
      memberPhoneNumTF.setBounds(225, 575, 319, 27);
      contentPane.add(memberPhoneNumTF);

      // 전화번호 힌트 라벨
      JLabel phoneNumHintLabel = new JLabel();
      phoneNumHintLabel.setForeground(new Color(255, 0, 0));
      phoneNumHintLabel.setBounds(225, 596, 319, 36); // 힌트 표시 위치와 크기 조정
      contentPane.add(phoneNumHintLabel);

      // 전화번호 중복 검사 버튼
      JButton memberPhoneNumBtn = new JButton("중복검사");
      memberPhoneNumBtn.setFont(new Font("굴림", Font.PLAIN, 15));
      memberPhoneNumBtn.setBounds(556, 577, 97, 27);
      contentPane.add(memberPhoneNumBtn);
      memberPhoneNumBtn.addActionListener(new ActionListener() {

         @Override
         public void actionPerformed(ActionEvent e) {
            String phoneNumber = memberPhoneNumTF.getText();
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

      // 생년월일 라벨
      JLabel memberBirthLB = new JLabel("생년월일");
      memberBirthLB.setHorizontalAlignment(SwingConstants.CENTER);
      memberBirthLB.setFont(new Font("굴림", Font.PLAIN, 20));
      memberBirthLB.setBounds(91, 627, 83, 49);
      contentPane.add(memberBirthLB);

      // 생년월일 (수정불가)
      JLabel memberBirthLA = new JLabel("");
      memberBirthLA.setFont(new Font("굴림", Font.BOLD, 19));
      memberBirthLA.setBounds(225, 631, 319, 36);
      contentPane.add(memberBirthLA);

      // 수정버튼
      JButton modifyBookbtn = new JButton("수정");
      modifyBookbtn.setFont(new Font("굴림", Font.BOLD, 18));
      modifyBookbtn.setBounds(493, 689, 74, 38);
      contentPane.add(modifyBookbtn);
      modifyBookbtn.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {

            // 회원선택 안된 경우 에러창
            String memberNumStr = memberNumLA.getText();
            if (memberNumStr.isEmpty()) {
               JOptionPane.showMessageDialog(null, "회원이 선택되지 않았습니다.", "오류", JOptionPane.WARNING_MESSAGE);
               return;
            } else {
               // 나머지 텍스트 필드가 비어있는지 확인
               String memberPw = memberPwTF.getText();
               String memberName = memberNameTF.getText();
               String memberPhNum = memberPhoneNumTF.getText();
               // System.out.println("수정 전 selectedMember : " + selectedMember);

               if (memberPw.isEmpty() || memberName.isEmpty() || memberPhNum.isEmpty()) {
                  JOptionPane.showMessageDialog(null, "빈 필드가 있습니다. 모든 필드를 채워주세요.", "입력 오류",
                        JOptionPane.ERROR_MESSAGE);
                  return;

               } else { // 빈 필드가 없을 경우 텍스트 필드 값을 DTO에 저장
                  selectedMember.setPassword(memberPw);
                  selectedMember.setName(memberName);
                  selectedMember.setPhoneNum(memberPhNum);
                  // System.out.println("수정 후 selectedMember : " + selectedMember);

                  // memberNum으로 DB 검색하여 "대여중"인 회원은 수정 불가능하게 함
                  RentalHistoryDAO rentalHistoryDAO = new RentalHistoryDAO();
                  String state = rentalHistoryDAO.getRentalStateOfMember(selectedMember.getMemberNum());
                  if (state.equals("대여중")) {
                     JOptionPane.showMessageDialog(null, "대여중인 회원은 수정불가!!", "오류", JOptionPane.WARNING_MESSAGE);
                     return;
                  } else {
                     try {
                        // MemberDAO를 생성하여 데이터베이스에 연결
                        MemberDAO dao = new MemberDAO();
                        // MemberDAO의 updateMember 메서드를 호출하여 데이터베이스를 업데이트
                        dao.updateMember(selectedMember);

                        dispose();
                        MemberManagement memberManagementt = new MemberManagement();
                        memberManagementt.setVisible(true);
                     } catch (Exception ex) {
                        // 업데이트가 실패했을 경우, 오류 메시지를 출력하거나 필요한 작업을 수행할 수 있습니다.
                        System.out.println("업데이트 실패 : " + ex.getMessage());
                     }
                  }

               }
            }
         }
      });

      // 삭제 버튼
      JButton delBtn = new JButton("삭제");
      delBtn.setFont(new Font("굴림", Font.BOLD, 18));
      delBtn.setBounds(579, 689, 74, 38);
      delBtn.setBackground(new Color(255, 115, 115)); // 배경색을 빨간색으로 설정
      contentPane.add(delBtn);
      delBtn.addActionListener(new ActionListener() {

         @Override
         public void actionPerformed(ActionEvent e) {
            MemberDAO dao = new MemberDAO();

            // MemberNum 라벨이 비어있는지 확인 : 회원이 선택되지 않은 경우
            if (memberNumLA.getText().isEmpty()) {

               // 선택되지 않았음을 알리는 메세지 출력
               JOptionPane.showMessageDialog(null, "회원이 선택되지 않았습니다.", "오류", JOptionPane.WARNING_MESSAGE);
            } // if block close
            else { // 회원이 선택된 경우
               // 책을 대여중에는 회원삭제 불가능
               // memberNum으로 DB 검색하여 "대여중"인 회원은 수정 불가능하게 함
               RentalHistoryDAO rentalHistoryDAO = new RentalHistoryDAO();
               String state = rentalHistoryDAO.getRentalStateOfMember(selectedMember.getMemberNum());
               if (state.equals("대여중")) {
                  JOptionPane.showMessageDialog(null, "대여중인 회원은 삭제불가!!", "오류", JOptionPane.WARNING_MESSAGE);
                  return;
               } else {
                  // 삭제 확인 메시지 박스 띄우기
                  int result = JOptionPane.showConfirmDialog(null,
                        memberNameTF.getText() + " 회원을 DB에서 정말 삭제하시겠습니까?", "삭제 확인", JOptionPane.YES_NO_OPTION);
                  // 사용자가 "예"를 선택한 경우
                  if (result == JOptionPane.YES_OPTION) {

                     try {
                        dao.deleteMember(selectedMember.getMemberNum());
                        System.out.println("삭제 완료!");
                        // 삭제 되었습니다. 메시지 표시
                        dispose();
                        MemberManagement management = new MemberManagement();
                        management.setVisible(true);
                     } catch (Exception e1) {
                        System.out.println("삭제 실패 : " + e1.getMessage());
                        dispose();
                     }

                  } else {
                     // 사용자가 "아니오"를 선택한 경우 또는 창을 닫은 경우
                     System.out.println("삭제 취소되었습니다.");
                  }
               }
            }

         }
      });

      // 대여 / 반납 버튼
      JButton bookRentalBtn = new JButton("대여 / 반납");
      bookRentalBtn.setFont(new Font("굴림", Font.BOLD, 18));
      bookRentalBtn.setBounds(342, 689, 139, 38);
      contentPane.add(bookRentalBtn);
      bookRentalBtn.addActionListener(new ActionListener() {

         @Override
         public void actionPerformed(ActionEvent e) {
            // 기존 창 닫고 대여 / 반납 클래스 객체 생성 호출
            // 회원선택 안된 경우 에러창
            String memberNumStr = memberNumLA.getText();
            if (memberNumStr.isEmpty()) {
               JOptionPane.showMessageDialog(null, "회원이 선택되지 않았습니다.", "오류", JOptionPane.WARNING_MESSAGE);
               return;
            } else {
               dispose();
               try {
                  BookRentReturn bookrentreturn = new BookRentReturn(selectedMember);
                  bookrentreturn.setVisible(true);
               } catch (Exception e1) {
                  // TODO Auto-generated catch block
                  e1.printStackTrace();
               }
            }
         }
      });

      // 뒤로가기 버튼
      JButton backBtn = new JButton("뒤로가기");
      backBtn.setFont(new Font("굴림", Font.BOLD, 18));
      backBtn.setBounds(12, 689, 112, 54);
      // 배경색을 투명하게 설정
      backBtn.setOpaque(false);
      backBtn.setContentAreaFilled(false);
      // 경계선 없애기
      backBtn.setBorderPainted(false);
      contentPane.add(backBtn);

      backBtn.addActionListener(new ActionListener() {

         @Override
         public void actionPerformed(ActionEvent e) {

            dispose();

         }
      });

      // 회원목록 라벨
      JLabel lblNewLabel = new JLabel("회원 목록");
      lblNewLabel.setFont(new Font("굴림", Font.BOLD, 24));
      lblNewLabel.setHorizontalAlignment(JLabel.CENTER);
      lblNewLabel.setBounds(12, 16, 118, 40);
      contentPane.add(lblNewLabel);

      // 검색버튼 옆에 검색창 텍스트필드
      textFieldSearch = new JTextField("회원명을 입력하세요.");
      textFieldSearch.setFont(new Font("굴림", Font.PLAIN, 16));
      textFieldSearch.setForeground(Color.GRAY); // 힌트 텍스트 색상 설정
      textFieldSearch.setColumns(10);
      textFieldSearch.setBounds(142, 16, 364, 36);
      contentPane.add(textFieldSearch);
      textFieldSearch.addFocusListener(new FocusListener() {

         @Override
         public void focusLost(FocusEvent e) {
            // 입력란에서 포커스를 잃었을 때, 입력값이 비어있는 경우 힌트 텍스트를 다시 표시하고 색상을 회색으로 변경
            if (textFieldSearch.getText().isEmpty()) {
               textFieldSearch.setText("회원명을 입력하세요.");
               textFieldSearch.setForeground(Color.GRAY);
            }
         }

         @Override
         public void focusGained(FocusEvent e) {
            // 입력란에 포커스를 얻었을 때, 힌트 텍스트가 있는 경우 텍스트를 지우고 색상을 원래대로 변경
            if (textFieldSearch.getText().equals("회원명을 입력하세요.")) {
               textFieldSearch.setText("");
               textFieldSearch.setForeground(Color.BLACK);
               textFieldSearch.setFont(new Font("굴림", Font.PLAIN, 16));

               // 검색 전 테이블 셀 너비 설정
               TableColumnModel columnModel = table.getColumnModel();
               columnModel.getColumn(0).setPreferredWidth(110); // 회원번호
               columnModel.getColumn(1).setPreferredWidth(180); // 이름
               columnModel.getColumn(2).setPreferredWidth(180); // 아이디
               columnModel.getColumn(3).setPreferredWidth(180); // 비밀번호
               columnModel.getColumn(4).setPreferredWidth(220); // 전화번호
               columnModel.getColumn(5).setPreferredWidth(220); // 생년월일
            }
         }
      });

      textFieldSearch.addKeyListener(new KeyAdapter() {
         @Override
         public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
               // 엔터 키가 눌렸을 때 동작할 내용
               // filterTableBySearchValue(textFieldSearch.getText().trim());
               tableModelForAllMembers(textFieldSearch.getText().trim());

               // 검색 전 테이블 셀 너비 설정
               TableColumnModel columnModel = table.getColumnModel();
               columnModel.getColumn(0).setPreferredWidth(110); // 회원번호
               columnModel.getColumn(1).setPreferredWidth(180); // 이름
               columnModel.getColumn(2).setPreferredWidth(180); // 아이디
               columnModel.getColumn(3).setPreferredWidth(180); // 비밀번호
               columnModel.getColumn(4).setPreferredWidth(220); // 전화번호
               columnModel.getColumn(5).setPreferredWidth(220); // 생년월일

            }
         }
      });

      // 검색버튼
      JButton searchBtn = new JButton();
      searchBtn.setBounds(518, 16, 49, 39);
      // 배경색을 투명하게 설정
      searchBtn.setOpaque(false);
      searchBtn.setContentAreaFilled(false);
      // 경계선 없애기
      searchBtn.setBorderPainted(false);
      contentPane.add(searchBtn);

      String imagePath = "src\\library\\images\\search.png";

      // 이미지 파일 로드
      ImageIcon icon = new ImageIcon(imagePath);
      Image image = icon.getImage();

      // 이미지 크기 조절
      int desiredWidth = 35; // 원하는 가로 크기
      int desiredHeight = 30; // 원하는 세로 크기
      Image scaledImage = image.getScaledInstance(desiredWidth, desiredHeight, Image.SCALE_SMOOTH);

      // 조절된 이미지로 ImageIcon 생성
      ImageIcon resizedIcon = new ImageIcon(scaledImage);
      searchBtn.setIcon(resizedIcon);
      contentPane.add(searchBtn);

      searchBtn.addActionListener(new ActionListener() {

         @Override
         public void actionPerformed(ActionEvent e) {
            // TODO Auto-generated method stub
            // 버튼 클릭 시 동작할 내용
            tableModelForAllMembers(textFieldSearch.getText().trim());

            // 검색 전 테이블 셀 너비 설정
            TableColumnModel columnModel = table.getColumnModel();
            columnModel.getColumn(0).setPreferredWidth(110); // 회원번호
            columnModel.getColumn(1).setPreferredWidth(180); // 이름
            columnModel.getColumn(2).setPreferredWidth(180); // 아이디
            columnModel.getColumn(3).setPreferredWidth(180); // 비밀번호
            columnModel.getColumn(4).setPreferredWidth(220); // 전화번호
            columnModel.getColumn(5).setPreferredWidth(220); // 생년월일

            System.out.println("검색버튼 눌림");
         }
      });

      // 전체목록 버튼
      JButton btnNewButton = new JButton("전체목록");
      btnNewButton.setFont(new Font("굴림", Font.BOLD, 15));
      btnNewButton.setBounds(556, 13, 97, 42);
      // 배경색을 투명하게 설정
      btnNewButton.setOpaque(false);
      btnNewButton.setContentAreaFilled(false);
      // 경계선 없애기
      btnNewButton.setBorderPainted(false); // 버튼 경계선 제거
      contentPane.add(btnNewButton);

      btnNewButton.addActionListener(new ActionListener() {

         @Override
         public void actionPerformed(ActionEvent e) {
            textFieldSearch.setText(""); // 텍스트 필드 내용 지우기
            tableModelForAllMembers(textFieldSearch.getText().trim());
            System.out.println("전체목록 버튼 눌림");

            // 검색 전 테이블 셀 너비 설정
            TableColumnModel columnModel = table.getColumnModel();
            columnModel.getColumn(0).setPreferredWidth(110); // 회원번호
            columnModel.getColumn(1).setPreferredWidth(180); // 이름
            columnModel.getColumn(2).setPreferredWidth(180); // 아이디
            columnModel.getColumn(3).setPreferredWidth(180); // 비밀번호
            columnModel.getColumn(4).setPreferredWidth(220); // 전화번호
            columnModel.getColumn(5).setPreferredWidth(220); // 생년월일

         }
      });

      // 회원목록 테이블 리스트
      JScrollPane scrollPane = new JScrollPane();
      scrollPane.setBounds(12, 69, 641, 283);
      contentPane.add(scrollPane);

      table = new JTable();
      scrollPane.setViewportView(table);
      table.setModel(new DefaultTableModel(
            new Object[][] { { null, null, null, null, null, null }, { null, null, null, null, null, null },
                  { null, null, null, null, null, null }, { null, null, null, null, null, null },
                  { null, null, null, null, null, null }, { null, null, null, null, null, null },
                  { null, null, null, null, null, null }, { null, null, null, null, null, null },
                  { null, null, null, null, null, null }, },
            new String[] { "\uD68C\uC6D0\uBC88\uD638", "\uC774\uB984", "\uC544\uC774\uB514",
                  "\uBE44\uBC00\uBC88\uD638", "\uC804\uD654\uBC88\uD638", "\uC0DD\uB144\uC6D4\uC77C" }));

      // ListSelectionModel.SINGLE_SELECTION 하나의 행만 선택가능
      table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

      // tableModelForAllMembers() 메서드를 호출하여 테이블에 표시할 데이터 GET
      DefaultTableModel model = tableModelForAllMembers();
      table.setModel(model);

      // 검색 전 테이블 셀 너비 설정
      TableColumnModel columnModel = table.getColumnModel();
      columnModel.getColumn(0).setPreferredWidth(110); // 회원번호
      columnModel.getColumn(1).setPreferredWidth(180); // 이름
      columnModel.getColumn(2).setPreferredWidth(180); // 아이디
      columnModel.getColumn(3).setPreferredWidth(180); // 비밀번호
      columnModel.getColumn(4).setPreferredWidth(220); // 전화번호
      columnModel.getColumn(5).setPreferredWidth(220); // 생년월일

      // 테이블 행 높이 설정
      table.setRowHeight(35); // 행의 높이를 35픽셀로 설정

      // 선택한 셀을 이중 클릭하여 수정되지 않도록 셀 에디터를 비활성화
      table.setDefaultEditor(Object.class, null);

      // 테이블에 MouseListener 등록
      // 테이블에서 더블클릭으로 선택된 행의 데이터를 텍스트 필드에 매핑
      table.addMouseListener(new MouseAdapter() {
         @Override
         public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2) {

               // 선택한 행의 인덱스를 가져옴
               int selectedRow = table.getSelectedRow();

               // 선택한 행의 인덱스가 유효한 경우에만 아래 코드를 실행
               if (selectedRow >= 0) {
                  // JTable의 모델을 DefaultTableModel로 형변환하여 가져옴
                  DefaultTableModel model = (DefaultTableModel) table.getModel();

                  int memberNum = (int) model.getValueAt(selectedRow, 0);
                  String memberName = model.getValueAt(selectedRow, 1).toString();
                  String memberId = model.getValueAt(selectedRow, 2).toString();
                  String memberPw = model.getValueAt(selectedRow, 3).toString();
                  String memberPhoneNum = model.getValueAt(selectedRow, 4).toString();
                  String memberBirth = model.getValueAt(selectedRow, 5).toString();

                  // 선택한 셀의 데이터를 JTextField에 매핑합니다.
                  memberNumLA.setText(Integer.toString(memberNum));
                  memberNameTF.setText(memberName);
                  memberIDLA.setText(memberId);
                  memberPwTF.setText(memberPw);
                  memberPhoneNumTF.setText(memberPhoneNum);
                  memberBirthLA.setText(memberBirth);

                  // 선택된 행의 정보를 selectedMember 객체에 저장
                  selectedMember.setMemberNum(memberNum);
                  selectedMember.setName(memberName);
                  selectedMember.setId(memberId);
                  selectedMember.setPassword(memberPw);
                  selectedMember.setPhoneNum(memberPhoneNum);
                  selectedMember.setBirthday(memberBirth);

               }
            }

         }

      });

      // 선택 버튼
      JButton choice = new JButton("선택");
      choice.setFont(new Font("굴림", Font.BOLD, 18));
      choice.setBounds(556, 362, 97, 36);
      contentPane.add(choice);

      // 선택 버튼과 연결된 ActionListener 등록
      choice.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            // 테이블에서 선택된 행의 인덱스 GET
            int selectedRow = table.getSelectedRow();

            // 선택한 행의 인덱스가 유효한 경우에만 아래 코드를 실행
            if (selectedRow >= 0) {
               // JTable의 모델을 DefaultTableModel로 형변환하여 가져옴
               DefaultTableModel model = (DefaultTableModel) table.getModel();

               int memberNum = (int) model.getValueAt(selectedRow, 0);
               String memberName = model.getValueAt(selectedRow, 1).toString();
               String memberId = model.getValueAt(selectedRow, 2).toString();
               String memberPw = model.getValueAt(selectedRow, 3).toString();
               String memberPhoneNum = model.getValueAt(selectedRow, 4).toString();
               String memberBirth = model.getValueAt(selectedRow, 5).toString();

               // 선택한 셀의 데이터를 JTextField에 매핑합니다.
               memberNumLA.setText(Integer.toString(memberNum));
               memberNameTF.setText(memberName);
               memberIDLA.setText(memberId);
               memberPwTF.setText(memberPw);
               memberPhoneNumTF.setText(memberPhoneNum);
               memberBirthLA.setText(memberBirth);

               // 선택된 행의 정보를 selectedMember 객체에 저장
               selectedMember.setMemberNum(memberNum);
               selectedMember.setName(memberName);
               selectedMember.setId(memberId);
               selectedMember.setPassword(memberPw);
               selectedMember.setPhoneNum(memberPhoneNum);
               selectedMember.setBirthday(memberBirth);
            }
         }
      });
   }

   // 모든 책리스트
   DefaultTableModel tableModelForAllMembers() {
      // DefaultTableModel 객체를 생성
      DefaultTableModel model = new DefaultTableModel();

      // 모델에 컬럼이름 추가
      model.addColumn("회원번호");
      model.addColumn("이름");
      model.addColumn("아이디");
      model.addColumn("비밀번호");
      model.addColumn("전화번호");
      model.addColumn("생년월일");

      try {
         MemberDAO dao = new MemberDAO();
         // BookDAO를 이용하여 데이터베이스로부터 모든 책 정보 GET
         ArrayList<MemberDTO> memberList = dao.getAllMember();

         for (MemberDTO dto : memberList) {
            // 각 도서 데이터를 모델에 행으로 추가
            model.addRow(new Object[] { dto.getMemberNum(), dto.getName(), dto.getId(), dto.getPassword(),
                  dto.getPhoneNum(), dto.getBirthday() });
         }
      } catch (Exception e) {
         System.out.println("책 정보를 가져오는 동안 오류가 발생했습니다." + e.getMessage());
         e.printStackTrace();
      }

      // 완성된 모델을 반환
      return model;

   }

   void tableModelForAllMembers(String keyword) {
      DefaultTableModel model = new DefaultTableModel();

      model.addColumn("회원번호");
      model.addColumn("이름");
      model.addColumn("아이디");
      model.addColumn("비밀번호");
      model.addColumn("전화번호");
      model.addColumn("생년월일");

      try {
         MemberDAO dao = new MemberDAO();
         ArrayList<MemberDTO> memberList = dao.getMemberSearch(keyword);
         for (MemberDTO dto : memberList) {
            model.addRow(new Object[] { dto.getMemberNum(), dto.getName(), dto.getId(), dto.getPassword(),
                  dto.getPhoneNum(), dto.getBirthday() });
         }
      } catch (Exception e) {
         System.out.println("회원 정보를 가져오는 동안 오류가 발생했습니다: " + e.getMessage());
      }

      this.table.setModel(model);
   }

}