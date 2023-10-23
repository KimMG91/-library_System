package library;

import java.awt.EventQueue;
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

public class ManagerMenu extends JFrame {
   
   private MemberDTO memberDTO = new MemberDTO();
   
   

   private JPanel contentPane;

   

   /**
    * Create the frame.
    */
   String imagePath = "src\\library\\images\\메뉴이미지.jpg";
   // 이미지 크기 조절
   int width = 367; // 원하는 가로 크기
   int height = 426; // 원하는 세로 크기
   ImageIcon originalImageIcon = new ImageIcon(imagePath);
   Image originalImage = originalImageIcon.getImage();
   Image resizedImage = originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
   // 조절된 이미지로 ImageIcon 생성
   ImageIcon resizedImageIcon = new ImageIcon(resizedImage);
   
   public ManagerMenu(MemberDTO memberDTO)  {
      setTitle("메뉴");
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      setBounds(100, 100, 383, 425);
      contentPane = new JPanel();
      contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

      setContentPane(contentPane);
      contentPane.setLayout(null);
      
      JPanel panel = new JPanel();
      panel.setBounds(0, 0, 367, 386);
      contentPane.add(panel);
      panel.setLayout(null);
      
      this.memberDTO= memberDTO;
      
      JButton btnNewButton = new JButton("내정보 보기");
      btnNewButton.setFont(new Font("굴림", Font.PLAIN, 16));
      btnNewButton.addActionListener(new ActionListener() {
         
         @Override
         public void actionPerformed(ActionEvent e) {
            // TODO Auto-generated method stub
            try {
               MyPage myPage = new MyPage(memberDTO);
               //myPage.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
               myPage.setVisible(true);
               dispose();
               
            } catch (Exception e1) {
               System.out.println("내정보보기 오류 : " + e1.getMessage());
               //e1.printStackTrace();
            }
         }
      });
      btnNewButton.setForeground(new Color(0, 0, 0));
      btnNewButton.setBounds(27, 150, 139, 35);
      panel.add(btnNewButton);
      
      JButton btnNewButton_1 = new JButton("도서검색");
      btnNewButton_1.setFont(new Font("굴림", Font.PLAIN, 16));
      btnNewButton_1.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            BookSearch bookSearch = new BookSearch();
            bookSearch.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
            bookSearch.setVisible(true);
         }
      });
      btnNewButton_1.setForeground(new Color(0, 0, 0));
      btnNewButton_1.setBounds(195, 150, 139, 35);
      panel.add(btnNewButton_1);
      
      JButton btnNewButton_1_1 = new JButton("다독자 순위");
      btnNewButton_1_1.setFont(new Font("굴림", Font.PLAIN, 16));
      btnNewButton_1_1.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            KingOfReader kingOfReader = new KingOfReader();
            kingOfReader.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
            kingOfReader.setVisible(true);
         }
      });
      btnNewButton_1_1.setForeground(new Color(0, 0, 0));
      btnNewButton_1_1.setBounds(195, 348, 139, 23);
      panel.add(btnNewButton_1_1);
      
   
      
      JButton backButton = new JButton("로그아웃");
      backButton.setFont(new Font("굴림", Font.PLAIN, 16));
      backButton.setForeground(new Color(0, 0, 0));
      backButton.setBounds(27, 336, 125, 35);
      backButton.addActionListener(e -> {
          // 뒤로가기 버튼이 클릭되었을 때 수행할 동작을 여기에 작성
         Login login = new Login();
         login.setVisible(true); // 로그인 화면을 보여줌
         dispose(); // 현재 프레임(또는 다이얼로그)을 닫음
          
      });
      
      JButton btnNewButton_1_1_2 = new JButton("인기도서 순위");
      btnNewButton_1_1_2.setFont(new Font("굴림", Font.PLAIN, 16));
      btnNewButton_1_1_2.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            BestOfBook bestOfBook = new BestOfBook();
            bestOfBook.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
            bestOfBook.setVisible(true);
         }
      });
      btnNewButton_1_1_2.setForeground(Color.BLACK);
      btnNewButton_1_1_2.setBounds(195, 315, 139, 23);
      panel.add(btnNewButton_1_1_2);
      panel.add(backButton);
      
      JButton btnNewButton_2 = new JButton("신규회원등록");
      btnNewButton_2.setFont(new Font("굴림", Font.PLAIN, 16));
      btnNewButton_2.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            MemberRegister memberRegister = new MemberRegister();
            memberRegister.setVisible(true);
            memberRegister.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
         }
      });
      btnNewButton_2.setForeground(Color.BLACK);
      btnNewButton_2.setBounds(27, 195, 139, 35);
      panel.add(btnNewButton_2);
      
      JButton btnNewButton_3 = new JButton("회원관리");
      btnNewButton_3.setFont(new Font("굴림", Font.PLAIN, 16));
      btnNewButton_3.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
         
            MemberManagement memberSearch = new MemberManagement();
            memberSearch.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
            memberSearch.setVisible(true);
            
         }
      });
      btnNewButton_3.setForeground(Color.BLACK);
      btnNewButton_3.setBounds(27, 240, 139, 35);
      panel.add(btnNewButton_3);
      
      
      JButton btnNewButton_2_1 = new JButton("신규도서등록");
      btnNewButton_2_1.setFont(new Font("굴림", Font.PLAIN, 16));
      btnNewButton_2_1.addActionListener(new ActionListener() {

         @Override
         public void actionPerformed(ActionEvent e) {
            
            BookAdd bookAdd = new BookAdd();
            bookAdd.setVisible(true);
            bookAdd.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
         }
      });
      btnNewButton_2_1.setForeground(Color.BLACK);
      btnNewButton_2_1.setBounds(195, 195, 139, 35);
      panel.add(btnNewButton_2_1);
      
      JButton btnNewButton_2_2 = new JButton("도서관리");
      btnNewButton_2_2.setFont(new Font("굴림", Font.PLAIN, 16));
      btnNewButton_2_2.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            
            BookManagement bookManagement = new BookManagement();
            bookManagement.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
            bookManagement.setVisible(true); // 도서목록 프레임을 보이도록 설정
         }
      });
      btnNewButton_2_2.setForeground(Color.BLACK);
      btnNewButton_2_2.setBounds(195, 240, 139, 35);
      panel.add(btnNewButton_2_2);
      
       String imagePath = "src\\library\\images\\logo_transparent2.png";
          // 이미지 크기 조절
          int width = 300; // 원하는 가로 크기
          int height = 270; // 원하는 세로 크기
          ImageIcon originalImageIcon = new ImageIcon(imagePath);
          Image originalImage = originalImageIcon.getImage();
          Image resizedImage = originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
          ImageIcon resizedImageIcon = new ImageIcon(resizedImage);
      
      JLabel lblNewLabel = new JLabel(resizedImageIcon);
      lblNewLabel.setBounds(12, 10, 338, 139);
      panel.add(lblNewLabel);
      
   
      
      // 화면 가운데로 정렬
      setLocationRelativeTo(null);
      
   }
   private static class __Tmp {
      private static void __tmp() {
           javax.swing.JPanel __wbp_panel = new javax.swing.JPanel();
      }
   }
}