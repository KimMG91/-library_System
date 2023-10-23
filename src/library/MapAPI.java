package library;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class MapAPI extends JFrame {

	private static final String API_KEY = "AIzaSyAgY-kaBEtCUxeaAHQL2aLEi5xOjBfAhzw";

	public MapAPI() {
		setTitle("도서관 위치");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(800, 600);
		setLocationRelativeTo(null);

		try {
			// 특정 위치의 위도(latitude)와 경도(longitude) 설정
			double latitude = 37.738046;
			double longitude = 127.044192;
			int zoomLevel = 17;

			// Google Maps Static API URL 생성
			String imageUrl = String.format(
					"https://maps.googleapis.com/maps/api/staticmap?center=%.6f,%.6f&zoom=%d&size=800x600&key=%s",
					latitude, longitude, zoomLevel, API_KEY);
			URL url = new URL(imageUrl);

			// 지도 이미지 로드
			BufferedImage image = ImageIO.read(url);

			// 마커와 설명 추가
			Graphics2D g2d = image.createGraphics();
			int markerX = 300; // 마커의 x좌표 (가로 중앙)
			int markerY = 300; // 마커의 y좌표 (세로 중앙)
			int markerSize = 15; // 마커의 크기
			g2d.setColor(Color.RED);
			g2d.fillOval(markerX - markerSize / 2, markerY - markerSize / 2, markerSize, markerSize);

			String markerLabel = "풀개도서관";
			int labelX = markerX + markerSize / 2; // 라벨의 x좌표
			int labelY = markerY + markerSize / 2; // 라벨의 y좌표
			g2d.setColor(Color.BLACK);
			g2d.drawString(markerLabel, labelX, labelY);

			// 지도 이미지를 표시할 라벨 생성
			JLabel mapLabel = new JLabel(new ImageIcon(image));

			// 창에 라벨 추가
			getContentPane().add(mapLabel, BorderLayout.CENTER);
			setLocationRelativeTo(null);

		} catch (IOException e) {
			e.printStackTrace();
		}

		// 화면 가운데로 정렬
		setLocationRelativeTo(null);

	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			MapAPI mapWindow = new MapAPI();
			mapWindow.setVisible(true);
		});
	}
}