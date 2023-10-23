package library;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONObject;

import libraryDB.MemberDAO;
import libraryDB.MemberDTO;

public class FindInfoAPI extends JFrame {
	private static final String SERVICE_ID = "ncp:sms:kr:311960526871:library";
	private static final String ACCESS_KEY = "jaYrdd5vHxwOUx1XJYY4";
	private static final String SECRET_KEY = "fMoATnrtlUomjvGsy2s3Be9Cg75KAKIbAENR8bfG";

	private JLabel phoneNumberLabel;
	private JTextField phoneNumberField;
	private JButton sendButton;
	private JLabel verificationCodeLabel;
	private JTextField verificationCodeField;
	private JButton verifyButton;
	private JLabel timerLabel;

	private String verificationCode;
	private Timer timer;
	private int timeRemaining = 120; // 2분 (단위: 초)

	public FindInfoAPI() {
        setTitle("아이디/비밀번호 찾기");
        setSize(400, 300);

        phoneNumberLabel = new JLabel("전화번호:");
        phoneNumberField = new JTextField(20);
        sendButton = new JButton("인증번호 전송");

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String phoneNumber = phoneNumberField.getText();
                if (!phoneNumber.isEmpty()) {
                    sendButton.setEnabled(false); // 인증번호 전송 버튼 비활성화
                    phoneNumberField.setEnabled(false); // 전화번호 입력창 비활성화
                    sendVerificationCode(phoneNumber);
                    startTimer();
                    verificationCodeLabel.setVisible(true); // 인증번호 레이블 표시
                    verificationCodeField.setVisible(true); // 인증번호 입력창 표시
                    verifyButton.setVisible(true); // 인증번호 검증 버튼 표시
                    timerLabel.setVisible(true); // 타이머 레이블 표시
                } else {
                    JOptionPane.showMessageDialog(FindInfoAPI.this, "전화번호를 입력하세요!");
                }
            }
        });

        verificationCodeLabel = new JLabel("인증번호:");
        verificationCodeLabel.setVisible(false); // 초기에는 숨김 상태로 설정
        verificationCodeField = new JTextField(20);
        verificationCodeField.setVisible(false); // 초기에는 숨김 상태로 설정
        verifyButton = new JButton("인증번호 검증");
        verifyButton.setVisible(false); // 초기에는 숨김 상태로 설정

        verifyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String enteredCode = verificationCodeField.getText();
                if (!enteredCode.isEmpty()) {
                    verifyCode(enteredCode);
                    stopTimer();
                    dispose(); // 창 종료
                } else {
                    JOptionPane.showMessageDialog(FindInfoAPI.this, "인증번호를 입력하세요!");
                }
            }
        });

        timerLabel = new JLabel(getTimeRemainingString());
        timerLabel.setVisible(false); // 초기에는 숨김 상태로 설정

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(5, 5, 5, 5);

        // phoneNumberLabel 추가
        panel.add(phoneNumberLabel, constraints);

        // phoneNumberField 추가 - 새로운 constraints 객체를 생성하여 사용합니다.
        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(5, 5, 5, 5);
        panel.add(phoneNumberField, constraints);

        // sendButton 추가 - 새로운 constraints 객체를 생성하여 사용합니다.
        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(5, 5, 5, 5);
        panel.add(sendButton, constraints);

        // verificationCodeLabel 추가 - 새로운 constraints 객체를 생성하여 사용합니다.
        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(5, 5, 5, 5);
        panel.add(verificationCodeLabel, constraints);

        // verificationCodeField 추가 - 새로운 constraints 객체를 생성하여 사용합니다.
        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 4;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(5, 5, 5, 5);
        panel.add(verificationCodeField, constraints);

        // verifyButton 추가 - 새로운 constraints 객체를 생성하여 사용합니다.
        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 5;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(5, 5, 5, 5);
        panel.add(verifyButton, constraints);

        // timerLabel 추가 - 새로운 constraints 객체를 생성하여 사용합니다.
        constraints = new GridBagConstraints();
        constraints.gridx = 1;
        constraints.gridy = 5;
        constraints.anchor = GridBagConstraints.EAST;
        panel.add(timerLabel, constraints);

        getContentPane().add(panel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

	private void sendVerificationCode(String phoneNumber) {
		try {
			String hostNameUrl = "https://sens.apigw.ntruss.com";
			String requestUrl = "/sms/v2/services/";
			String requestUrlType = "/messages";
			String method = "POST";
			String timestamp = Long.toString(System.currentTimeMillis());
			requestUrl += SERVICE_ID + requestUrlType;
			String apiUrl = hostNameUrl + requestUrl;

			verificationCode = generateVerificationCode();
	        String cleanVerificationCode = removeHyphens(verificationCode);

	        String cleanPhoneNumber = removeHyphens(phoneNumber); // 하이픈을 제거하는 메서드 호출

			JSONObject bodyJson = new JSONObject();
			JSONObject toJson = new JSONObject();
			JSONArray toArr = new JSONArray();

			toJson.put("to", cleanPhoneNumber); // 하이픈이 제거된 phoneNumber 사용
			toArr.put(toJson);

			bodyJson.put("type", "SMS");
			bodyJson.put("from", "01094800129");
			bodyJson.put("content", "[풀개도서관]인증번호 : " + "["+cleanVerificationCode+"]"+"를 입력해주세요.");
			bodyJson.put("messages", toArr);

			String body = bodyJson.toString();

			URL url = new URL(apiUrl);

			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setUseCaches(false);
			con.setDoOutput(true);
			con.setDoInput(true);
			con.setRequestProperty("content-type", "application/json");
			con.setRequestProperty("x-ncp-apigw-timestamp", timestamp);
			con.setRequestProperty("x-ncp-iam-access-key", ACCESS_KEY);
			con.setRequestProperty("x-ncp-apigw-signature-v2",
					makeSignature(requestUrl, timestamp, method, ACCESS_KEY, SECRET_KEY));
			con.setRequestMethod(method);
			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());

			wr.write(body.getBytes());
			wr.flush();
			wr.close();

			int responseCode = con.getResponseCode();
			BufferedReader br;
			System.out.println("responseCode" + " " + responseCode);
			if (responseCode == 202) {
				br = new BufferedReader(new InputStreamReader(con.getInputStream()));
				JOptionPane.showMessageDialog(FindInfoAPI.this, "인증번호 전송 성공!");
			} else {
				br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
				JOptionPane.showMessageDialog(FindInfoAPI.this, "인증번호 전송 실패. 응답 코드: " + responseCode);
			}

			String inputLine;
			StringBuffer response = new StringBuffer();
			while ((inputLine = br.readLine()) != null) {
				response.append(inputLine);
			}
			br.close();

			System.out.println(response.toString());

		} catch (Exception e) {
			System.out.println(e);
			JOptionPane.showMessageDialog(FindInfoAPI.this, "인증번호 전송 실패: " + e.getMessage());
		}
	}
	
	private String removeHyphens(String str) {
	    return str.replaceAll("-", "");
	}

	private void verifyCode(String enteredCode) {
	    String cleanEnteredCode = removeHyphens(enteredCode);
	    String phoneNumber = phoneNumberField.getText();
	    String cleanPhoneNumber = removeHyphens(phoneNumber);

	    if (cleanEnteredCode.equals(verificationCode)) { // 하이픈 없는 인증번호와 입력된 인증번호 비교
	        JOptionPane.showMessageDialog(FindInfoAPI.this, "인증 성공!");

	        MemberDAO memberDAO = new MemberDAO();
	        MemberDTO memberDTO = memberDAO.getMemberByPhoneNumber(cleanPhoneNumber);

	        if (memberDTO != null) {
	            StringBuilder infoBuilder = new StringBuilder();
	            infoBuilder.append("회원번호: ").append(memberDTO.getMemberNum()).append("\n");
	            infoBuilder.append("아이디: ").append(memberDTO.getId()).append("\n");
	            infoBuilder.append("비밀번호: ").append(memberDTO.getPassword()).append("\n");
	            infoBuilder.append("이름: ").append(memberDTO.getName()).append("\n");
	            infoBuilder.append("생년월일: ").append(memberDTO.getBirthday()).append("\n");

	            JOptionPane.showMessageDialog(FindInfoAPI.this, infoBuilder.toString());
	        } else {
	            JOptionPane.showMessageDialog(FindInfoAPI.this, "일치하는 전화번호 데이터를 찾을 수 없습니다.");
	        }
	    } else {
	        JOptionPane.showMessageDialog(FindInfoAPI.this, "인증 실패!");
	    }
	}


	private void startTimer() {
		timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				if (timeRemaining > 0) {
					timeRemaining--;
					updateTimerLabel();
				} else {
					stopTimer();
					JOptionPane.showMessageDialog(FindInfoAPI.this, "인증 시간이 만료되었습니다.");
				}
			}
		}, 1000, 1000);
	}

	private void stopTimer() {
		if (timer != null) {
			timer.cancel();
			timer.purge();
		}
	}

	private void updateTimerLabel() {
		timerLabel.setText(getTimeRemainingString());
	}

	 private String getTimeRemainingString() {
	        int minutes = timeRemaining / 60;
	        int seconds = timeRemaining % 60;
	        return String.format("남은 시간: %02d:%02d", minutes, seconds);
	    }

	private String generateVerificationCode() {
		Random random = new Random();
		int code = random.nextInt(90000) + 10000;
		return String.valueOf(code);
	}
	
	//헤더 서명부분..API
	private String makeSignature(String url, String timestamp, String method, String accessKey, String secretKey)
			throws NoSuchAlgorithmException, InvalidKeyException {
		String space = " ";
		String newLine = "\n";

		String message = new StringBuilder().append(method).append(space).append(url).append(newLine).append(timestamp)
				.append(newLine).append(accessKey).toString();

		SecretKeySpec signingKey = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
		Mac mac = Mac.getInstance("HmacSHA256");
		mac.init(signingKey);
		byte[] rawHmac = mac.doFinal(message.getBytes(StandardCharsets.UTF_8));
		return Base64.getEncoder().encodeToString(rawHmac);
	}

	/*
	 * public static void main(String[] args) { SwingUtilities.invokeLater(new
	 * Runnable() {
	 * 
	 * @Override public void run() { new FindInfoAPI(); } }); }
	 */
}