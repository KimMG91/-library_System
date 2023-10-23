package library;

import java.awt.EventQueue;
import java.awt.Image;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

import javax.swing.border.EmptyBorder;

import javax.swing.JTextField;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.SystemColor;
import java.awt.event.ActionListener;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Panel;
import java.awt.Color;

import java.awt.event.FocusEvent;

import libraryDB.MemberDAO;
import libraryDB.MemberDTO;

public class Login extends JFrame {

    private JPanel contentPane;
    private JTextField id_Input;
    private JTextField pass_input;

    String imagePath = "src\\library\\images\\로그인책이미지.png";
    // 이미지 크기 조절
    int width = 550; // 원하는 가로 크기
    int height = 350; // 원하는 세로 크기
    ImageIcon originalImageIcon = new ImageIcon(imagePath);
    Image originalImage = originalImageIcon.getImage();
    Image resizedImage = originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);

    // 조절된 이미지로 ImageIcon 생성
    ImageIcon resizedImageIcon = new ImageIcon(resizedImage);

    String imagePath2 = "src\\library\\images\\logo_transparent2.png";
    // 이미지 크기 조절
    int width2 = 300; // 원하는 가로 크기
    int height2 = 270; // 원하는 세로 크기
    ImageIcon originalImageIcon2 = new ImageIcon(imagePath2);
    Image originalImage2 = originalImageIcon2.getImage();
    Image resizedImage2 = originalImage2.getScaledInstance(width2, height2, Image.SCALE_SMOOTH);

    // 조절된 이미지로 ImageIcon 생성
    ImageIcon resizedImageIcon2 = new ImageIcon(resizedImage2);

    // 293, 35, 206, 227
    public Login() {
        setTitle("로그인");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 561, 365);
        contentPane = new JPanel();
        contentPane.setBackground(SystemColor.inactiveCaptionBorder);
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        // 포커스를 contentPane 패널에 맞추기
        contentPane.requestFocusInWindow();

        setContentPane(contentPane);
        contentPane.setLayout(null);

        Panel panel_1 = new Panel();
        panel_1.setBounds(0, 0, 545, 326);
        contentPane.add(panel_1);
        panel_1.setLayout(null);

        JButton btnNewButton = new JButton("회원가입");
        btnNewButton.setBackground(SystemColor.info);
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                MemberRegister uesr = new MemberRegister(); // 회원가입창으로 넘어갈수있는 키
                uesr.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
                uesr.setVisible(true);
                // dispose(); // 현재 창을 닫음
            }
        });
        btnNewButton.setBounds(51, 272, 97, 23);
        btnNewButton.setContentAreaFilled(false); // 내용 영역을 투명하게 설정
        btnNewButton.setBorder(null); // 테두리 없애기
        btnNewButton.setFocusPainted(false); // 포커스 효과 비활성화
        btnNewButton.setOpaque(false); // 배경 투명하게 설정
        btnNewButton.setForeground(Color.BLACK); // 텍스트 색상 설정

        panel_1.add(btnNewButton);

        JButton btnNewButton_1 = new JButton("아이디찾기");
        btnNewButton_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                FindInfoAPI api = new FindInfoAPI();
                api.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
                api.setVisible(true);

            }
        });
        btnNewButton_1.setBounds(160, 272, 97, 23);
        btnNewButton_1.setContentAreaFilled(false);// 배경을 투명으로
        btnNewButton_1.setBorderPainted(false); // 버튼 테두리 없애기
        btnNewButton_1.setFocusPainted(false); // 포커스 표시 없애기
        btnNewButton_1.setFocusable(false); // 포커스 받지 않기
        panel_1.add(btnNewButton_1);

        JButton btnNewButton_2 = new JButton("로그인");
        btnNewButton_2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                login(); // 로그인 처리 메서드 호출
            }
        });

        btnNewButton_2.setBounds(55, 158, 202, 51);
        btnNewButton_2.setBackground(Color.WHITE);
        btnNewButton_2.setBorderPainted(false); // 버튼 테두리 없애기
        btnNewButton_2.setFocusPainted(false); // 포커스 표시 없애기
        btnNewButton_2.setFocusable(true);
        panel_1.add(btnNewButton_2);

        id_Input = new JTextField();
        id_Input.setBounds(55, 60, 202, 39);
        id_Input.setColumns(10);
        id_Input.setForeground(Color.LIGHT_GRAY);
        id_Input.setText("아이디를 입력하세요");
        // FocusListener 추가
        id_Input.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                // 입력란에 포커스를 얻었을 때, 힌트 텍스트가 있는 경우 텍스트를 지우고 색상을 원래대로 변경
                if (id_Input.getText().equals("아이디를 입력하세요")) {
                    id_Input.setText("");
                    id_Input.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                // 입력란에서 포커스를 잃었을 때, 입력값이 비어있는 경우 힌트 텍스트를 다시 표시하고 색상을 연하게 변경
                if (id_Input.getText().isEmpty()) {
                    id_Input.setText("아이디를 입력하세요");
                    id_Input.setForeground(Color.LIGHT_GRAY);
                }
            }
        });
        // KeyListener 추가
        id_Input.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    pass_input.requestFocusInWindow();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });
        panel_1.add(id_Input);

        pass_input = new JPasswordField();
        pass_input.setBounds(55, 109, 202, 39);
        pass_input.setColumns(10);
        ((JPasswordField) pass_input).setEchoChar((char) 0); // 입력값을 숨기지 않음
        pass_input.setForeground(Color.LIGHT_GRAY);
        pass_input.setText("비밀번호를 입력해주세요");
        pass_input.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                char[] password = ((JPasswordField) pass_input).getPassword();
                String passwordString = new String(password);
                if (passwordString.equals("비밀번호를 입력해주세요")) {
                    pass_input.setText("");
                    ((JPasswordField) pass_input).setEchoChar('*'); // 입력값을 '*'로 표시
                    pass_input.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                char[] password = ((JPasswordField) pass_input).getPassword();
                String passwordString = new String(password);
                if (passwordString.isEmpty()) {
                    ((JPasswordField) pass_input).setEchoChar((char) 0); // 입력값을 숨기지 않음
                    pass_input.setForeground(Color.LIGHT_GRAY);
                    pass_input.setText("비밀번호를 입력해주세요");
                }
            }
        });
        // KeyListener 추가
        pass_input.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    login(); // 로그인 메서드 호출
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });
        panel_1.add(pass_input);

        JButton btnNewButton_3 = new JButton("도서관 위치");
        btnNewButton_3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                MapAPI map = new MapAPI();
                map.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
                map.setVisible(true);
            }
        });
        btnNewButton_3.setBounds(402, 272, 97, 23);
        btnNewButton_3.setContentAreaFilled(false); // 내용 영역을 투명하게 설정
        btnNewButton_3.setBorder(null); // 테두리 없애기
        btnNewButton_3.setFocusPainted(false); // 포커스 효과 비활성화
        btnNewButton_3.setOpaque(false); // 배경 투명하게 설정
        btnNewButton_3.setForeground(Color.BLACK); // 텍스트 색상 설정
        panel_1.add(btnNewButton_3);

        JLabel lblNewLabel = new JLabel(resizedImageIcon2);
        lblNewLabel.setBounds(279, 24, 234, 212);
        panel_1.add(lblNewLabel);

        JLabel imageLabel2 = new JLabel(resizedImageIcon);
        imageLabel2.setBounds(0, 0, 545, 326);
        panel_1.add(imageLabel2);

        // 화면 가운데로 정렬
        setLocationRelativeTo(null);
    }

   

    private void login() {
        String inputId = id_Input.getText();
        String inputPassword = pass_input.getText();

        MemberDTO memberDTO = new MemberDTO();
        MemberDAO memberDAO = new MemberDAO();
        try {
            memberDTO = memberDAO.login(inputId, inputPassword);
            //System.out.println("Login 성공 시 불러들인 객체 : " + memberDTO);
            if (memberDTO != null) {
                // 로그인 성공
                if (memberDTO.getIsManager().equalsIgnoreCase("N")) {
                     JOptionPane.showMessageDialog(null, "로그인이 완료되었습니다. 메뉴를 선택해주세요.", "로그인 완료",
                              JOptionPane.INFORMATION_MESSAGE);
                    Menu menu = new Menu(memberDTO);
                    menu.setVisible(true);
                } else {
                   JOptionPane.showMessageDialog(null, "관리자 로그인 완료되었습니다. 메뉴를 선택해주세요.", "로그인 완료",
                            JOptionPane.INFORMATION_MESSAGE);
                    ManagerMenu manager = new ManagerMenu(memberDTO);
                    manager.setVisible(true);
                }
                dispose();
            } else {
                // 로그인 실패
                if (memberDAO.checkIdExist(inputId)) {
                    // 아이디는 존재하지만 비밀번호가 틀린 경우
                    JOptionPane.showMessageDialog(null, "비밀번호가 일치하지 않습니다.", "로그인 실패",
                            JOptionPane.ERROR_MESSAGE);
                } else {
                    // 아이디가 존재하지 않는 경우
                    JOptionPane.showMessageDialog(null, "아이디가 존재하지 않습니다.", "로그인 실패",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception ex) {
            System.out.println("로그인 중 오류가 발생하였습니다: " + ex.getMessage());
        }
    }
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Login frame = new Login();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}