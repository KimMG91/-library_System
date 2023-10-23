package library;

import javax.swing.*;

import libraryDB.BookDAO;
import libraryDB.BookDTO;
import libraryDB.MemberDTO;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import javax.swing.border.LineBorder;

public class Quiz extends JFrame {

    private Map<String, String> questions = new HashMap<>();//질문과 정답을 저장하는 데이터 구조입니다. Map은 Key-Value 쌍을 저장하는 자료구조로, String 타입의 질문을 Key로,
    //String 타입의 정답을 Value로 가지고 있습니다.
    //HashMap은 Map 인터페이스를 구현한 구체적인 클래스입니다.
    private JLabel questionLabel;
    private JTextField answerField;
    private JButton submitButton;
    private JLabel resultLabel;
    private JLabel hintLabel; // 추가: 힌트 레이블
    private JLabel hintButtonLabel; // 추가: 힌트 버튼 레이블
    private BookDAO bookDAO;
    


    public Quiz() {
       
        bookDAO = new BookDAO();
        initializeQuestions();

        setTitle("도서 퀴즈 게임");
        setSize(525, 340); // 크기 변경: 힌트 레이블을 추가하여 크기를 더 높여줍니다.
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setBackground(new Color(255, 255, 255));
        panel.setLayout(null);

        questionLabel = new JLabel();//문제 제출 라벨 
        questionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        questionLabel.setFont(new Font("굴림", Font.PLAIN, 15));
        questionLabel.setBounds(0, 0, 509, 58);
        panel.add(questionLabel);

        answerField = new JTextField();//사용자가 입력하는 텍스트필드(답)
        answerField.setHorizontalAlignment(SwingConstants.CENTER);
        answerField.setBorder(new LineBorder(new Color(0, 0, 0)));
        answerField.setFont(new Font("굴림", Font.PLAIN, 20));
        answerField.setBounds(0, 58, 509, 58);
        panel.add(answerField);

        submitButton = new JButton("제출");//제출 버튼 
        submitButton.setBounds(0, 116, 509, 58);
        submitButton.setFont(new Font("굴림", Font.PLAIN, 19));
        panel.add(submitButton);

        resultLabel = new JLabel();//결과 출력하는 라벨 
        resultLabel.setFont(new Font("굴림", Font.PLAIN, 15));
        resultLabel.setBounds(0, 174, 509, 58);
        panel.add(resultLabel);

        hintLabel = new JLabel(); // 추가: 힌트 레이블
        hintLabel.setFont(new Font("굴림", Font.PLAIN, 15));
        hintLabel.setBounds(0, 232, 509, 58);
        panel.add(hintLabel);

        hintButtonLabel = new JLabel("힌트 보기"); //힌트 버튼 레이블
        hintButtonLabel.setHorizontalAlignment(SwingConstants.CENTER);
        hintButtonLabel.setFont(new Font("굴림", Font.PLAIN, 18));
        hintButtonLabel.setBounds(391, 232, 118, 58);
        hintButtonLabel.setForeground(new Color(0, 0, 139)); // 힌트 버튼 레이블의 글자색
        panel.add(hintButtonLabel);

        getContentPane().add(panel);

        showNextQuestion();//다음문제로 넘어가는 메서드 

        submitButton.addActionListener(new ActionListener() {//제출 버튼을 눌렀을때 
            @Override
            public void actionPerformed(ActionEvent e) {
                submitAnswer();
            }
        });

        answerField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                submitAnswer();
            }
        });

        hintButtonLabel.addMouseListener(new MouseAdapter() {// 추가: 힌트 버튼 레이블 클릭 이벤트 처리
            @Override
            public void mouseClicked(MouseEvent e) {
                showHint();//힌트보여주는 메서드 
            }
        });

        setLocationRelativeTo(null);//가운데정렬
    }

    private void initializeQuestions() {
        try {
            BookDAO dao = new BookDAO();
            ArrayList<BookDTO> books = dao.getAllBook();//dao클래스에 getAllbook를 books에 리스트로 정렬 

            for (BookDTO book : books) {
                String question = "어떤 작가가 '" + book.getTitle() + "'를 쓴 것일까요?";
                String answer = book.getAuthor();
                questions.put(question, answer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showNextQuestion() {//맵에 저장된 질문들 중에서 랜덤으로 하나의 질문을 선택하여 화면에 표시하는 작업
        Random random = new Random();
        Object[] keys = questions.keySet().toArray();//questions 맵에 저장된 모든 질문들의 Key(질문)들을 배열로 변환
        String randomKey = (String) keys[random.nextInt(keys.length)];//keys 배열에서 무작위로 하나의 Key를 선택합니다. 
        //random.nextInt(keys.length)는 0부터 keys 배열의 길이 사이에서 무작위로 정수를 반환
        String question = randomKey;//선택된 무작위 질문을 question 변수에 저장
        questionLabel.setText(question);//questionLabel에 선택된 질문을 텍스트로 설정 

        // 문제가 변경될 때마다 힌트 레이블 초기화
        hintLabel.setText("");
    }

    private void submitAnswer() {
        String userAnswer = answerField.getText().replaceAll("\\s+", "");//입력된 사용자의 답을 가져옵니다. 
        //답의 앞뒤로 있는 공백을 제거하기 위해 replaceAll("\\s+", "")를 사용
        
        String currentAnswer = questions.get(questionLabel.getText()).replaceAll("\\s+", "");
        //질문(questionLabel에 표시된 텍스트)에 대한 정답을 questions 맵에서 가져옵니다. 
        //정답의 앞뒤로 있는 공백을 제거하기 위해 replaceAll("\\s+", "")를 사용합니다.

        if (userAnswer.equalsIgnoreCase(currentAnswer)) {//사용자의 답userAnswer과 현재 질문의 정답(currentAnswer)을 대소문자 구분 없이 비교
            resultLabel.setText("정답입니다!");//사용자의 답이 정답과 일치하는 경우
        } else {
            resultLabel.setText("오답입니다. 정답은 " + questions.get(questionLabel.getText()) + "입니다.");
            
            // 오답인 경우 힌트를 보여줍니다.
            showHint();
        }

        answerField.setText("");
        showNextQuestion();
    }

    private void showHint() {//오답을 제출했을 때 정답의 힌트를 제공하는 기능 구현하는 메서드 
        String currentAnswer = questions.get(questionLabel.getText());//질문에 대한 정답을 questions 맵에서 가져오기
        int hintLength = Math.max(currentAnswer.length() / 2, 1); // 정답 길이의 절반 또는 최소 1글자 
        //정답의 길이를 절반으로 나눈 값을 hintLength에 저장합니다. 만약 정답의 길이가 2 이상인 경우에는 절반으로 나눈 값이 hintLength가 되며, 
        //정답의 길이가 1인 경우에는 최소한 1글자의 힌트를 제공하기 위해 hintLength를 1로 설정합니다.
        String hint = currentAnswer.substring(0, hintLength) + "(으)로 시작합니다.";//currentAnswer에서 첫 번째 글자부터 hintLength 길이까지를 자른 뒤, ...을 추가하여 hint에 저장합니다. 
        //이렇게 함으로써 힌트는 정답의 첫 부분 일부분과 ...이 결합된 형태로 표시됩니다.
        hintLabel.setText("힌트: " + hint);//힌트를 hintLabel에 표시합니다. "힌트: [힌트 내용]" 형태로 표시되며, [힌트 내용]은 위에서 생성한 hint 문자열입니다.
    }

   /*
    * public static void main(String[] args) { SwingUtilities.invokeLater(new
    * Runnable() {
    * 
    * @Override public void run() { MemberDTO memberDTO = new MemberDTO(); // 예시로
    * 생성한 MemberDTO 객체, 실제 사용시 해당 객체를 적절히 초기화해야 합니다. Quiz game = new
    * Quiz(memberDTO); game.setVisible(true); } }); }
    */
}