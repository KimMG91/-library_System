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
import libraryDB.RentalHistoryDAO;

public class BookManagement extends JFrame {

   private JPanel contentPane;
   private JTextField authorTF;
   private JTable table;
   private JTextField publisherTF;
   private JTextField pubYearTF;
   private JTextField textFieldSearch;
   private JTextField titleTF;


   /**
    * Create the frame.
    */

   public BookManagement() {

      setTitle("도서관리");
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      setBounds(100, 100, 681, 730);
      setLocationRelativeTo(null); // 창을 가운데로 위치시키기
      contentPane = new JPanel();
      contentPane.setToolTipText("");
      contentPane.setBackground(new Color(255, 255, 255));
      contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

      setContentPane(contentPane);
      contentPane.setLayout(null);

      // 도서번호 라벨
      JLabel bookNumLB = new JLabel("\uB3C4\uC11C\uBC88\uD638");
      bookNumLB.setFont(new Font("굴림", Font.PLAIN, 20));
      bookNumLB.setBounds(91, 394, 83, 36);
      bookNumLB.setHorizontalAlignment(JLabel.CENTER);
      contentPane.add(bookNumLB);

      // 도서번호 (수정불가)
      JLabel bookNumLA = new JLabel("");
      bookNumLA.setFont(new Font("굴림", Font.BOLD, 19));
      bookNumLA.setBounds(225, 394, 319, 36);
      contentPane.add(bookNumLA);

      // 도서명 라벨
      JLabel titleLB = new JLabel("도서명");
      titleLB.setFont(new Font("굴림", Font.PLAIN, 20));
      titleLB.setHorizontalAlignment(SwingConstants.CENTER);
      titleLB.setBounds(78, 433, 106, 33);
      contentPane.add(titleLB);

      // 도서명 텍스트필드 (수정가능)
      titleTF = new JTextField();
      titleTF.setFont(new Font("굴림", Font.PLAIN, 16));
      titleTF.setColumns(10);
      titleTF.setBounds(225, 431, 319, 27);
      contentPane.add(titleTF);

      // 저자명 라벨
      JLabel authorLB = new JLabel("저자명");
      authorLB.setFont(new Font("굴림", Font.PLAIN, 20));
      authorLB.setHorizontalAlignment(SwingConstants.CENTER);
      authorLB.setBounds(78, 468, 106, 33);
      contentPane.add(authorLB);

      // 저자명 텍스트필드 (수정가능)
      authorTF = new JTextField();
      authorTF.setFont(new Font("굴림", Font.PLAIN, 16));
      authorTF.setBounds(225, 466, 319, 27);
      contentPane.add(authorTF);
      authorTF.setColumns(10);

      // 출판사 라벨
      JLabel publisherLB = new JLabel("\uCD9C\uD310\uC0AC");
      publisherLB.setFont(new Font("굴림", Font.PLAIN, 20));
      publisherLB.setHorizontalAlignment(JLabel.CENTER);
      publisherLB.setBounds(91, 496, 83, 49);
      contentPane.add(publisherLB);

      // 출판사 텍스트필드 (수정가능)
      publisherTF = new JTextField();
      publisherTF.setFont(new Font("굴림", Font.PLAIN, 16));
      publisherTF.setColumns(10);
      publisherTF.setBounds(225, 503, 319, 27);
      contentPane.add(publisherTF);

      // 출판연도 라벨
      JLabel pubYearLB = new JLabel("출판일");
      pubYearLB.setFont(new Font("굴림", Font.PLAIN, 20));
      pubYearLB.setHorizontalAlignment(SwingConstants.CENTER);
      pubYearLB.setBounds(91, 535, 83, 49);
      contentPane.add(pubYearLB);

      // 출판연도 텍스트필드 (수정가능)
      pubYearTF = new JTextField();
      pubYearTF.setFont(new Font("굴림", Font.PLAIN, 16));
      pubYearTF.setColumns(10);
      pubYearTF.setBounds(225, 542, 319, 27);
      contentPane.add(pubYearTF);

      // 수정버튼
      JButton modifyBookbtn = new JButton("수정");
      modifyBookbtn.setFont(new Font("굴림", Font.BOLD, 18));
      modifyBookbtn.setBounds(493, 627, 74, 38);
      contentPane.add(modifyBookbtn);
      modifyBookbtn.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            BookDTO dto = new BookDTO();

            // 도서선택 안 된 경우 에러창 띄우기 : bookNumLA에서 값이 없을 때
            String bookNumStr = bookNumLA.getText();
            if (bookNumStr.isEmpty()) {
               JOptionPane.showMessageDialog(null, "도서가 선택되지 않았습니다.", "오류", JOptionPane.WARNING_MESSAGE);
               return;
            } else {
               // bookNumLA에서 값을 가져와서 int 타입으로 변환

               // 나머지 텍스트 필드가 비어있는지 확인
               String title = titleTF.getText();
               String author = authorTF.getText();
               String publisher = publisherTF.getText();
               String pubYear = pubYearTF.getText();

               if (title.isEmpty() || author.isEmpty() || publisher.isEmpty() || pubYear.isEmpty()) {
                  JOptionPane.showMessageDialog(null, "빈 필드가 있습니다. 모든 필드를 채워주세요.", "입력 오류",
                        JOptionPane.ERROR_MESSAGE);
                  return;
               } else {// 빈 필드가 없을 경우 텍스트 필드 값을 DTO에 저장
                  int bookNum = Integer.parseInt(bookNumStr);
                  dto.setBookNum(bookNum);
                  dto.setTitle(title);
                  dto.setAuthor(author);
                  dto.setPublisher(publisher);
                  dto.setPubYear(pubYear);

                  // bookNum으로 DB 검색하여 "대여중"인 도서는 수정 불가능하게 함
                  RentalHistoryDAO rentalHistoryDAO = new RentalHistoryDAO();
                  String state = rentalHistoryDAO.getRentalStateOfBook(dto.getBookNum());
                  if (state.equals("대여중")) {
                     JOptionPane.showMessageDialog(null, "대여중인 도서는 수정불가!!", "오류", JOptionPane.WARNING_MESSAGE);
                     return;
                  } else {
                     try {
                        // BookDAO를 생성하여 데이터베이스에 연결
                        BookDAO dao = new BookDAO();
                        // BookDAO의 updateBook 메서드를 호출하여 데이터베이스를 업데이트
                        dao.updateBook(dto);
                        System.out.println("수정완료 !");
                        dispose();
                        BookManagement bookManagement = new BookManagement();
                        bookManagement.setVisible(true);
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
      delBtn.setBounds(579, 627, 74, 38);
      delBtn.setBackground(new Color(255, 115, 115)); // 배경색을 빨간색으로 설정

      contentPane.add(delBtn);
      delBtn.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            BookDTO dto = new BookDTO();
            BookDAO dao = new BookDAO();

            // bookNum라벨이 비어있는지 확인 : 도서가 선택되지 않은 경우
            if (bookNumLA.getText().isEmpty()) {

               // 선택되지 않았음을 알리는 메시지 출력
               JOptionPane.showMessageDialog(null, "도서가 선택되지 않았습니다.", "오류", JOptionPane.WARNING_MESSAGE);

            } else { // 빈 텍스트 필드가 없음 : 도서가 선택된 경우
               // bookNumLA에서 값을 가져와서 int 타입으로 변환
               int bookNum = Integer.parseInt(bookNumLA.getText());
               dto.setBookNum(bookNum);
               RentalHistoryDAO rentalHistoryDAO = new RentalHistoryDAO();
               String state = rentalHistoryDAO.getRentalStateOfBook(dto.getBookNum());

               // bookNum으로 DB 검색하여 "대여중"인 도서는 삭제 불가능하게 함
               if (state.equals("대여중")) {
                  JOptionPane.showMessageDialog(null, "대여중인 도서는 삭제불가!!", "오류", JOptionPane.WARNING_MESSAGE);
               } else {
                  // 삭제 확인 메시지 박스 띄우기
                  int result = JOptionPane.showConfirmDialog(null,
                        bookNumLA.getText() + "번 도서를 DB에서 정말 삭제하시겠습니까?", "삭제 확인", JOptionPane.YES_NO_OPTION);

                  // 사용자가 "예"를 선택한 경우
                  if (result == JOptionPane.YES_OPTION) {

                     try {
                        dao.deleteBook(dto);
                        System.out.println("삭제 완료!");
                        // 삭제 되었습니다. 메시지 표시
                        dispose();
                        BookManagement bookManagement = new BookManagement();
                        bookManagement.setVisible(true);
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

      // 뒤로가기 버튼
      JButton backBtn = new JButton("뒤로가기");
      backBtn.setFont(new Font("굴림", Font.BOLD, 18));
      backBtn.setBounds(12, 627, 112, 54);
      // 배경색을 투명하게 설정
      backBtn.setOpaque(false);
      backBtn.setContentAreaFilled(false);
      // 경계선 없애기
      backBtn.setBorderPainted(false);
      contentPane.add(backBtn);
      backBtn.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            // 버튼 클릭 시 창 닫기
            dispose();
            
         }
      });

      // 책목록 라벨
      JLabel lblNewLabel = new JLabel("도서 목록");
      lblNewLabel.setFont(new Font("굴림", Font.BOLD, 24));
      lblNewLabel.setHorizontalAlignment(JLabel.CENTER);
      lblNewLabel.setBounds(12, 16, 118, 40);
      contentPane.add(lblNewLabel);

      // 검색버튼 옆에 검색창 텍스트필드
      textFieldSearch = new JTextField("도서명을 입력하세요.");
      textFieldSearch.setFont(new Font("굴림", Font.PLAIN, 16));
      textFieldSearch.setForeground(Color.GRAY); // 힌트 텍스트 색상 설정
      textFieldSearch.setColumns(10);
      textFieldSearch.setBounds(142, 16, 364, 36);
      contentPane.add(textFieldSearch);

      // FocusListener 추가
      textFieldSearch.addFocusListener(new FocusListener() {
         @Override
         public void focusGained(FocusEvent e) {
            // 입력란에 포커스를 얻었을 때, 힌트 텍스트가 있는 경우 텍스트를 지우고 색상을 원래대로 변경
            if (textFieldSearch.getText().equals("도서명을 입력하세요.")) {
               textFieldSearch.setText("");
               textFieldSearch.setForeground(Color.BLACK);
               textFieldSearch.setFont(new Font("굴림", Font.PLAIN, 16));

               // 검색 후테이블 셀 너비 설정
               TableColumnModel columnModel = table.getColumnModel();
               columnModel.getColumn(0).setPreferredWidth(90); // 책번호
               columnModel.getColumn(1).setPreferredWidth(500); // 도서명
               columnModel.getColumn(2).setPreferredWidth(220); // 저자명
               columnModel.getColumn(3).setPreferredWidth(220); // 출판사
               columnModel.getColumn(4).setPreferredWidth(180); // 출판일
            }
         }

         @Override
         public void focusLost(FocusEvent e) {
            // 입력란에서 포커스를 잃었을 때, 입력값이 비어있는 경우 힌트 텍스트를 다시 표시하고 색상을 회색으로 변경
            if (textFieldSearch.getText().isEmpty()) {
               textFieldSearch.setText("도서명을 입력하세요.");
               textFieldSearch.setForeground(Color.GRAY);
            }
         }
      });

      // 검색 기능과 연결된 KeyListener 등록
      textFieldSearch.addKeyListener(new KeyAdapter() {
         @Override
         public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
               // 엔터 키가 눌렸을 때 동작할 내용
               // filterTableBySearchValue(textFieldSearch.getText().trim());
               tableModelForAllSearch(textFieldSearch.getText().trim());

               // 검색 후테이블 셀 너비 설정
               TableColumnModel columnModel = table.getColumnModel();
               columnModel.getColumn(0).setPreferredWidth(90); // 책번호
               columnModel.getColumn(1).setPreferredWidth(500); // 도서명
               columnModel.getColumn(2).setPreferredWidth(220); // 저자명
               columnModel.getColumn(3).setPreferredWidth(220); // 출판사
               columnModel.getColumn(4).setPreferredWidth(180); // 출판일
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

      // 검색 버튼과 연결된 ActionListener 등록
      searchBtn.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            // 버튼 클릭 시 동작할 내용
            // filterTableBySearchValue(textFieldSearch.getText());
            tableModelForAllSearch(textFieldSearch.getText().trim());
            System.out.println("검색버튼 눌림");

            // 검색 버튼 클릭 후 테이블 셀 너비 설정
            TableColumnModel columnModel = table.getColumnModel();
            columnModel.getColumn(0).setPreferredWidth(90); // 책번호
            columnModel.getColumn(1).setPreferredWidth(500); // 도서명
            columnModel.getColumn(2).setPreferredWidth(220); // 저자명
            columnModel.getColumn(3).setPreferredWidth(220); // 출판사
            columnModel.getColumn(4).setPreferredWidth(180); // 출판일

         }
      });

      // 전체목록 버튼
      JButton btnNewButton = new JButton("전체목록");
      btnNewButton.setFont(new Font("굴림", Font.BOLD, 15));
      btnNewButton.setBounds(556, 18, 97, 42);
      // 배경색을 투명하게 설정
      btnNewButton.setOpaque(false);
      btnNewButton.setContentAreaFilled(false);
      // 경계선 없애기
      btnNewButton.setBorderPainted(false); // 버튼 경계선 제거
      contentPane.add(btnNewButton);

      btnNewButton.addActionListener(new ActionListener() {

         @Override
         public void actionPerformed(ActionEvent e) {
            // TODO Auto-generated method stub
            textFieldSearch.setText(""); // 텍스트 필드 내용 지우기
            tableModelForAllSearch(textFieldSearch.getText().trim());
            System.out.println("전체목록 버튼 눌림");

            // 전체목록 버튼 클릭 후 테이블 셀 너비 설정
            TableColumnModel columnModel = table.getColumnModel();
            columnModel.getColumn(0).setPreferredWidth(90); // 책번호
            columnModel.getColumn(1).setPreferredWidth(500); // 도서명
            columnModel.getColumn(2).setPreferredWidth(220); // 저자명
            columnModel.getColumn(3).setPreferredWidth(220); // 출판사
            columnModel.getColumn(4).setPreferredWidth(180); // 출판일
         }
      });

      // 도서목록
      // JScrollPane 테이블의 행이 많을 때 스크롤 기능을 제공하여 화면에 테이블을 표시
      JScrollPane scrollPane = new JScrollPane();
      scrollPane.setBounds(12, 66, 641, 263);
      contentPane.add(scrollPane);

      // 도서목록 테이블
      table = new JTable();
      table.setGridColor(Color.GRAY);
      table.setForeground(Color.BLACK);
      table.setFont(new Font("굴림", Font.PLAIN, 17));
      scrollPane.setViewportView(table);
      table.setModel(new DefaultTableModel(new Object[][] { { null, null, null, null, null },
            { null, null, null, null, null }, { null, null, null, null, null }, { null, null, null, null, null },
            { null, null, null, null, null }, { null, null, null, null, null }, { null, null, null, null, null },
            { null, null, null, null, null }, { null, null, null, null, null }, { null, null, null, null, null },
            { null, null, null, null, null }, { null, null, null, null, null }, { null, null, null, null, null },
            { null, null, null, null, null }, },
            new String[] { "\uB3C4\uC11C\uBC88\uD638", "\uB3C4\uC11C\uBA85", "\uC800\uC790\uBA85",
                  "\uCD9C\uD310\uC0AC", "\uCD9C\uD310\uC77C" }));

      // ListSelectionModel.SINGLE_SELECTION 하나의 행만 선택가능
      table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

      // tableModelForAllBooks() 메서드를 호출하여 테이블에 표시할 데이터 GET
      DefaultTableModel model = tableModelForAllBooks();
      table.setModel(model);

      // 검색 전 테이블 셀 너비 설정
      TableColumnModel columnModel = table.getColumnModel();
      columnModel.getColumn(0).setPreferredWidth(90); // 책번호
      columnModel.getColumn(1).setPreferredWidth(500); // 도서명
      columnModel.getColumn(2).setPreferredWidth(220); // 저자명
      columnModel.getColumn(3).setPreferredWidth(220); // 출판사
      columnModel.getColumn(4).setPreferredWidth(180); // 출판일

      // 테이블 행 높이 설정
      table.setRowHeight(35); // 행의 높이를 35픽셀로 설정

      // 선택 버튼
      JButton choice = new JButton("선택");
      choice.setFont(new Font("굴림", Font.BOLD, 18));
      choice.setBounds(556, 339, 97, 36);
      contentPane.add(choice);

      // 선택 버튼과 연결된 ActionListener 등록
      choice.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            // 테이블에서 선택된 행의 인덱스 GET
            int selectedRow = table.getSelectedRow();

            // 선택된 행이 유효한 경우, 해당 행의 데이터를 가져와서 텍스트 필드에 표시
            if (selectedRow >= 0) {
               DefaultTableModel model = (DefaultTableModel) table.getModel();
               String bookNum = model.getValueAt(selectedRow, 0).toString();
               String title = model.getValueAt(selectedRow, 1).toString();
               String author = model.getValueAt(selectedRow, 2).toString();
               String publisher = model.getValueAt(selectedRow, 3).toString();
               String pubYear = model.getValueAt(selectedRow, 4).toString();

               // 선택한 셀의 데이터를 JTextField에 매핑
               bookNumLA.setText(bookNum);
               titleTF.setText(title);
               authorTF.setText(author);
               publisherTF.setText(publisher);
               pubYearTF.setText(pubYear);

            }
         }
      });

      // 선택한 셀을 이중 클릭하여 수정되지 않도록 셀 에디터를 비활성화
      table.setDefaultEditor(Object.class, null);

      // 테이블에 MouseListener 등록
      // 테이블에서 더블클릭으로 선택된 행의 데이터를 텍스트 필드에 매핑
      table.addMouseListener(new MouseAdapter() {
         @Override
         public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2) {

               int selectedRow = table.getSelectedRow();
               if (selectedRow >= 0) {
                  DefaultTableModel model = (DefaultTableModel) table.getModel();
                  String bookNum = model.getValueAt(selectedRow, 0).toString();
                  String title = model.getValueAt(selectedRow, 1).toString();
                  String author = model.getValueAt(selectedRow, 2).toString();
                  String publisher = model.getValueAt(selectedRow, 3).toString();
                  String pubYear = model.getValueAt(selectedRow, 4).toString();

                  // 선택한 셀의 데이터를 JTextField에 매핑합니다.
                  bookNumLA.setText(bookNum);
                  titleTF.setText(title);
                  authorTF.setText(author);
                  publisherTF.setText(publisher);
                  pubYearTF.setText(pubYear);
               }
            }

         }
      });

   }

   // 모든 책리스트
   DefaultTableModel tableModelForAllBooks() {
      // DefaultTableModel 객체를 생성
      DefaultTableModel model = new DefaultTableModel();

      // 모델에 컬럼이름 추가
      model.addColumn("도서번호");
      model.addColumn("도서명");
      model.addColumn("저자명");
      model.addColumn("출판사");
      model.addColumn("출판일");

      try {
         BookDAO dao = new BookDAO();
         // BookDAO를 이용하여 데이터베이스로부터 모든 책 정보 GET
         ArrayList<BookDTO> bookList = dao.getAllBook();

         for (BookDTO dto : bookList) {

            // 각 도서 데이터를 모델에 행으로 추가
            model.addRow(new Object[] { dto.getBookNum(), dto.getTitle(), dto.getAuthor(), dto.getPublisher(),
                  dto.getPubYear() });
         }

      } catch (Exception e) {
         System.out.println("책 정보를 가져오는 동안 오류가 발생했습니다." + e.getMessage());
         e.printStackTrace();
      }

      // 완성된 모델을 반환
      return model;

   }

   // 책제목 조회시 데이터 가져와서 Table Model 에 Setting
   /* DefaultTableModel */ void tableModelForAllSearch(String keword) {
      // DefaultTableModel 객체를 생성
      DefaultTableModel model = new DefaultTableModel();

      // 모델에 컬럼이름 추가
      model.addColumn("도서번호");
      model.addColumn("도서명");
      model.addColumn("저자명");
      model.addColumn("출판사");
      model.addColumn("출판일");
      // model.addColumn("대출상태");

      try {
         BookDAO dao = new BookDAO();
         // BookDAO를 이용하여 데이터베이스로부터 모든 책 정보 GET
         ArrayList<BookDTO> bookList = dao.getBookSearch(keword);

         for (BookDTO dto : bookList) {

            // 각 도서 데이터를 모델에 행으로 추가
            model.addRow(new Object[] { dto.getBookNum(), dto.getTitle(), dto.getAuthor(), dto.getPublisher(),
                  dto.getPubYear() });
         }

      } catch (Exception e) {
         System.out.println("책 정보를 가져오는 동안 오류가 발생했습니다." + e.getMessage());
         e.printStackTrace();
      }

      // 완성된 모델을 반환
      // return model;
      this.table.setModel(model);

   }
}