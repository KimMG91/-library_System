package library;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.JToolBar;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.TextField;
import javax.swing.JMenuBar;
import java.awt.Font;
import javax.swing.JMenu;
import javax.swing.JCheckBox;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.border.LineBorder;
import java.awt.Color;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.text.JTextComponent;

import libraryDB.BookDAO;
import libraryDB.BookDTO;
import libraryDB.MemberDTO;

public class BookAdd extends JFrame {

   private JPanel contentPane;

   private JTextField textField;
   private JTextField textField_1;
   private JTextField textField_2;
   private JTextField textField_3;
   private JLabel lblRegisteredBook;
   private JLabel lblResult;

   public BookAdd() {

      getContentPane().setLayout(null);

      JPanel panel = new JPanel();
      panel.setBounds(0, 0, 434, 261);
      getContentPane().add(panel);

      setTitle("도서등록");
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      setBounds(100, 100, 572, 536);

      JMenuBar menuBar = new JMenuBar();
      menuBar.setToolTipText("도서추가");
      setJMenuBar(menuBar);
      contentPane = new JPanel();
      contentPane.setBackground(Color.WHITE);
      contentPane.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));

      setContentPane(contentPane);
      contentPane.setLayout(null);

      JButton btnNewButton = new JButton("추가");
      btnNewButton.setFont(new Font("굴림", Font.PLAIN, 20));
      btnNewButton.setBorder(null);
      btnNewButton.setBackground(new Color(255, 255, 255));
      btnNewButton.setForeground(Color.BLACK);

      btnNewButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {

            // 사용자 입력값 가져오기
            String title = textField.getText();
            String author = textField_1.getText();
            String publisher = textField_2.getText();
            String publicationDate = textField_3.getText();

            // BookDTO 객체 생성 및 값 설정
            BookDTO book = new BookDTO();

            book.setTitle(title);
            book.setAuthor(author);
            book.setPublisher(publisher);
            book.setPubYear(publicationDate);

            // 책 정보 데이터베이스에 추가
            BookDAO bookDAO = new BookDAO(); // BookDAO 객체 생성
            boolean success = bookDAO.insertBook(book); // DB에 책 정보 추가
            // insertBook(book);

            // 성공 여부에 따라 메시지 출력
            if (success) {
               // System.out.println("도서 추가: " + bookdto.getTitle());
               showResult("도서가 추가되었습니다.");
               resetFields();
            } else {
               showResult("※ 중복된 도서로 추가에 실패했습니다.");
            }

         }

         // 텍스트 필드 초기화
         private void resetFields() {
            textField.setText("");
            textField_1.setText("");
            textField_2.setText("");
            textField_3.setText("");
         }

      });

      btnNewButton.setBounds(287, 387, 97, 39);
      contentPane.add(btnNewButton);

      JButton btnNewButton_1_1 = new JButton("취소");
      btnNewButton_1_1.setFont(new Font("굴림", Font.PLAIN, 20));
      btnNewButton_1_1.setBorder(null);
      btnNewButton_1_1.setBackground(new Color(255, 255, 255));
      btnNewButton_1_1.setForeground(Color.BLACK);
      btnNewButton_1_1.setBounds(169, 387, 89, 39);
      btnNewButton_1_1.addActionListener(new ActionListener() {

         @Override
         public void actionPerformed(ActionEvent e) {
            dispose();//취소 누르면 창 닫기
         }
      });

      contentPane.add(btnNewButton_1_1);
      JLabel lblNewLabel = new JLabel("도서명");
      lblNewLabel.setFont(new Font("굴림", Font.PLAIN, 20));
      lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
      lblNewLabel.setBounds(53, 54, 97, 50);
      lblNewLabel.setText("도서명");
      contentPane.add(lblNewLabel);

      JLabel lblNewLabel_1_1 = new JLabel("작가명");
      lblNewLabel_1_1.setFont(new Font("굴림", Font.PLAIN, 20));
      lblNewLabel_1_1.setHorizontalAlignment(SwingConstants.CENTER);
      lblNewLabel_1_1.setBounds(53, 117, 97, 50);
      contentPane.add(lblNewLabel_1_1);

      JLabel lblNewLabel_1_1_1 = new JLabel("출판일");
      lblNewLabel_1_1_1.setFont(new Font("굴림", Font.PLAIN, 20));
      lblNewLabel_1_1_1.setHorizontalAlignment(SwingConstants.CENTER);
      lblNewLabel_1_1_1.setBounds(53, 247, 97, 64);
      contentPane.add(lblNewLabel_1_1_1);

      JLabel lblNewLabel_1_1_1_1 = new JLabel("출판사");
      lblNewLabel_1_1_1_1.setFont(new Font("굴림", Font.PLAIN, 20));
      lblNewLabel_1_1_1_1.setHorizontalAlignment(SwingConstants.CENTER);
      lblNewLabel_1_1_1_1.setBounds(53, 180, 97, 56);
      contentPane.add(lblNewLabel_1_1_1_1);

      // 텍스트 필드 생성
      textField = new JTextField();
      textField.setFont(new Font("굴림", Font.PLAIN, 18));
      textField.setBounds(182, 57, 321, 45);
      String hintText = "도서명을 입력하세요";// 힌트설정
      setDefaultHint(textField, hintText);
      contentPane.add(textField);

      textField_1 = new JTextField();
      textField_1.setFont(new Font("굴림", Font.PLAIN, 18));
      textField_1.setBounds(182, 118, 321, 47);
      String hintText_1 = "작가명을 입력하세요";
      setDefaultHint(textField_1, hintText_1);
      contentPane.add(textField_1);

      textField_2 = new JTextField();
      textField_2.setFont(new Font("굴림", Font.PLAIN, 18));
      textField_2.setBounds(182, 187, 321, 45);
      String hintText_2 = "출판사명을 입력하세요";
      setDefaultHint(textField_2, hintText_2);
      contentPane.add(textField_2);

      textField_3 = new JTextField();
      textField_3.setFont(new Font("굴림", Font.PLAIN, 18));
      textField_3.setBounds(182, 251, 320, 45);
      String hintText_3 = "출판일을 입력하세요";
      setDefaultHint(textField_3, hintText_3);
      contentPane.add(textField_3);

      lblRegisteredBook = new JLabel();
      lblRegisteredBook.setFont(new Font("굴림", Font.PLAIN, 20));
      lblRegisteredBook.setBounds(62, 304, 450, 76);
      contentPane.add(lblRegisteredBook);

      lblResult = new JLabel();
      lblResult.setFont(new Font("굴림", Font.PLAIN, 20));
      lblResult.setBounds(42, 304, 450, 91);
      contentPane.add(lblResult);

      setLocationRelativeTo(null);//가운데정렬
   }

   // 등록된 도서 정보를 표시하는 메서드
   private void showRegisteredBookInfo(BookDTO book) {
      String bookInfo = "등록된 도서 정보: " + book.getTitle() + " / 작가: " + book.getAuthor() + " / 출판사: "
            + book.getPublisher() + " / 출판일: " + book.getPubYear();
      lblRegisteredBook.setText(bookInfo);
   }

   // 결과 메시지를 표시하는 메서드
   private void showResult(String result) {
      lblResult.setText(result);
   }

   private void setDefaultHint(JTextField textField, String hintText) {
      textField.setText(hintText);//텍스트필드에 힌트 셋팅하기
      textField.setForeground(Color.GRAY);

      textField.addFocusListener(new FocusListener() {
         @Override
         public void focusLost(FocusEvent e) {//포커스가 textField에서 떠날 때 호출되는 메서드
            if (textField.getText().isEmpty()) {
               textField.setText(hintText);
               textField.setForeground(Color.GRAY); // 힌트 텍스트 색상을 설정합니다.
            }
         }

         @Override
         public void focusGained(FocusEvent e) {//포커스가 textField로 들어올 때 호출되는 메서드
            if (textField.getText().equals(hintText)) {
               textField.setText("");
               textField.setForeground(Color.BLACK); // 입력할 때의 텍스트 색상을 설정합니다.
            }
         }
      });
   }

}