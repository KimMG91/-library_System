package library;

import com.toedter.calendar.JDateChooser;

import libraryDB.MemberDAO;
import libraryDB.MemberDTO;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.Font;

public class MemberRegister extends JFrame {

   private JPanel contentPane;

   private String userId;
   private String phoneNum;
   private String birthday;
   private String password;
   private String name;
   private boolean idButtonClicked = false;
   private boolean phoneNumButtonClicked = false;

   /**
    * Create the frame.
    */
   public MemberRegister() {
      setTitle("회원가입");
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      setBounds(100, 100, 483, 440);
      contentPane = new JPanel();
      contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

      setContentPane(contentPane);

      JPanel panel = new JPanel();
      panel.setBounds(0, 0, 467, 404);
      contentPane.setLayout(null);
      contentPane.add(panel);
      panel.setLayout(null);

      JLabel lblNewLabel = new JLabel("성명");
      lblNewLabel.setBounds(26, 31, 48, 23);
      panel.add(lblNewLabel);

      JLabel lblNewLabel_1 = new JLabel("생년월일");
      lblNewLabel_1.setBounds(26, 78, 91, 23);
      panel.add(lblNewLabel_1);

      TextField name1 = new TextField();
      name1.setBounds(164, 31, 159, 23);
      panel.add(name1);

      JLabel lblNewLabel_3 = new JLabel("회원 ID");
      lblNewLabel_3.setBounds(26, 129, 60, 23);
      panel.add(lblNewLabel_3);

      JLabel lblNewLabel_5 = new JLabel("비밀번호");
      lblNewLabel_5.setBounds(26, 180, 70, 23);
      panel.add(lblNewLabel_5);

      JLabel lblNewLabel_5_1 = new JLabel("비밀번호 확인");
      lblNewLabel_5_1.setBounds(26, 224, 91, 23);
      panel.add(lblNewLabel_5_1);

      JLabel lblNewLabel_6 = new JLabel("전화번호");
      lblNewLabel_6.setBounds(26, 269, 81, 23);
      panel.add(lblNewLabel_6);

      JDateChooser dateChooser = new JDateChooser();// 날짜선택 버튼.......
      dateChooser.setBounds(164, 79, 159, 21);
      panel.add(dateChooser);
      
      // PropertyChangeListener를 사용하여 선택된 날짜 변경 사항을 감지합니다.
            dateChooser.addPropertyChangeListener(new PropertyChangeListener() {
               @Override
               public void propertyChange(PropertyChangeEvent evt) {
                  if ("date".equals(evt.getPropertyName())) {
                     // "date" 속성이 변경되었을 때 선택된 날짜를 가져옵니다.
                     Date selectedDate = (Date) evt.getNewValue();

                     if (selectedDate != null) {
                        // 원하는 형식으로 날짜를 변환하여 변수에 저장합니다.
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        birthday = dateFormat.format(selectedDate);
                        // System.out.println(birthday);
                     }
                  }
               }
            });

      

      TextField id = new TextField();
      id.setFont(new Font("굴림", Font.PLAIN, 15));
      id.setBounds(164, 129, 159, 23);
      panel.add(id);

      JButton idButton = new JButton("중복확인");
      JLabel iddHintLabel = new JLabel();
      iddHintLabel.setBounds(164, 147, 266, 20); // 힌트 표시 위치와 크기 조정
      panel.add(iddHintLabel);
      idButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            final String userId = id.getText(); // id 값을 final 변수로 선언
            MemberDTO memberDTO = new MemberDTO();
            MemberDAO memberDAO = new MemberDAO();
            memberDTO.setId(userId);

            try {
               String result = memberDAO.insertId(memberDTO);
               if (userId.length() < 8 || userId.length() > 15) {
                  iddHintLabel.setText("ID는 8~15글자 사이로 입력해야 합니다.");
                  idButtonClicked = false; // 중복 검사 실패로 처리
               } else {
                  iddHintLabel.setText(result);
                  memberDTO.setId(userId); // 중복 검사를 마친 id 값을 MemberDTO에 설정
                  idButtonClicked = true; // 중복 검사 성공으로 처리
               }
            } catch (Exception ex) {
               System.out.println("ID 검사 중 오류가 발생하였습니다: " + ex.getMessage());
               idButtonClicked = false; // 중복 검사 실패로 처리
            }
         }
      });

      idButton.setBounds(333, 129, 97, 23);
      panel.add(idButton);

      TextField phon = new TextField();
      phon.setBounds(164, 269, 159, 23);
      phon.setForeground(Color.LIGHT_GRAY);
      phon.setText("-없이 입력해주세요");
      phon.addFocusListener(new FocusListener() {
         public void focusGained(FocusEvent e) {
            // 입력란에 포커스를 얻었을 때, 힌트 텍스트가 있는 경우 텍스트를 지우고 색상을 원래대로 변경
            if (phon.getText().equals("-없이 입력해주세요")) {
               phon.setText("");
               phon.setForeground(Color.BLACK);
            }
         }
         public void focusLost(FocusEvent e) {
            // 입력란에서 포커스를 잃었을 때, 입력값이 비어있는 경우 힌트 텍스트를 다시 표시하고 색상을 연하게 변경
            if (phon.getText().isEmpty()) {
               phon.setText("-없이 입력해주세요");
               phon.setForeground(Color.LIGHT_GRAY);
            }
         }
      });
      panel.add(phon);
      
      // 힌트 표시 위치와 크기 조정
      JLabel phoneNumHintLabel = new JLabel();
      phoneNumHintLabel.setBounds(164, 293, 239, 23);
      panel.add(phoneNumHintLabel);

      JButton phoneNumButton = new JButton("중복검사");
      phoneNumButton.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
              String phoneNumber = phon.getText();

            

              MemberDTO memberDTO = new MemberDTO();
              MemberDAO memberDAO = new MemberDAO();
              memberDTO.setPhoneNum(phoneNumber);

              try {
                  String result = memberDAO.insertPhoneNum(memberDTO);
                  phoneNumHintLabel.setText(result);
                  phoneNumButtonClicked = true; // 중복 검사 성공
                 
              } catch (Exception ex) {
                  System.out.println("전화번호 검사 중 오류가 발생하였습니다: " + ex.getMessage());
                  phoneNumHintLabel.setText(""); // 중복검사 실패 시, 결과 초기화
                  phoneNumButtonClicked = false; // 중복 검사 실패
              }
          }
      });

      phoneNumButton.setBounds(333, 269, 97, 23);
      panel.add(phoneNumButton);
      

      JPasswordField password1_1 = new JPasswordField();
      password1_1.setBounds(164, 181, 159, 21);
      panel.add(password1_1);

      JPasswordField password1_2 = new JPasswordField();
      password1_2.setBounds(164, 225, 159, 21);
      panel.add(password1_2);

      JLabel passwordHintLabel = new JLabel();
      passwordHintLabel.setBounds(164, 242, 239, 21); // 힌트 표시 위치와 크기 조정
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

      

      

      JLabel alertLabel = new JLabel();
      alertLabel.setBounds(26, 316, 404, 23); // 알람 표시 위치와 크기 조정
      panel.add(alertLabel);

      JButton btnNewButton_3 = new JButton("확인");
      btnNewButton_3.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            MemberDAO memberDAO = MemberDAO.getInstance();
            name = name1.getText();
            userId = id.getText();
            phoneNum = phon.getText();
            char[] passwordChars = password1_1.getPassword();
            password = new String(passwordChars);
            Date selectedDate = dateChooser.getDate();

            if (selectedDate == null) {
               // 선택된 날짜가 없을 경우 처리
               alertLabel.setText("생년월일을 선택해주세요."); // 알람 메시지 설정
               return; // 회원 등록 중단
            }

            try {
               // 유효성 검사
               if (name.isEmpty() || userId.isEmpty() || phoneNum.isEmpty() || password.isEmpty()
                     || birthday.isEmpty()) {
                  String errorMessage = "빈칸을 입력해주세요: ";
                  if (name.isEmpty())
                     errorMessage += "성명 ";
                  if (userId.isEmpty())
                     errorMessage += "회원 ID ";
                  if (phoneNum.isEmpty())
                     errorMessage += "전화번호 ";
                  if (password.isEmpty())
                     errorMessage += "비밀번호 ";
                  if (birthday.isEmpty())
                     errorMessage += "생년월일 ";

                  alertLabel.setText(errorMessage.trim()); // 알람 메시지 설정
                  return; // 회원 등록 중단
               }

               if (!idButtonClicked) {
                  alertLabel.setText("회원 ID 중복 확인을 해주세요."); // 알림 메시지 설정
                  return; // 회원 등록 중단
               }

               if (!phoneNumButtonClicked) {
                  alertLabel.setText("전화번호 중복 확인을 해주세요."); // 알림 메시지 설정
                  return; // 회원 등록 중단
               }

               MemberDTO dto = new MemberDTO();
               dto.setName(name);
               dto.setId(userId);
               dto.setPhoneNum(phoneNum);
               dto.setPassword(password);
               dto.setBirthday(birthday);

               String isSuccess = memberDAO.insertMember(dto);
               if (isSuccess != null) {
                  System.out.println("신규 회원으로 등록되었습니다.");
                  JOptionPane.showMessageDialog(null, "회원가입이 완료되었습니다. 로그인해주세요.", "회원가입 완료",
                        JOptionPane.INFORMATION_MESSAGE);
                  dispose();
               } else {
                  System.out.println("회원 등록 중 오류가 발생하였습니다.");
               }
            } catch (Exception ex) {
               System.out.println("회원 등록 중 오류가 발생하였습니다: " + ex.getMessage());
            }
         }

      });

      btnNewButton_3.setBounds(253, 349, 97, 23);
      panel.add(btnNewButton_3);
      
      JButton btnNewButton_2 = new JButton("취소");
      btnNewButton_2.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            dispose(); // 현재 창을 닫음
         }
      });
      btnNewButton_2.setBounds(117, 349, 97, 23);
      panel.add(btnNewButton_2);

      // 화면 가운데로 정렬
      setLocationRelativeTo(null);
   }

   /**
    * Launch the application.
    */
   public static void main(String[] args) {
      EventQueue.invokeLater(new Runnable() {
         public void run() {
            try {
               MemberRegister frame = new MemberRegister();
               frame.setVisible(true);
            } catch (Exception e) {
               e.printStackTrace();
            }
         }
      });
   }
}