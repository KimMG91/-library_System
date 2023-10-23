package library;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableRowSorter;

import libraryDB.BookDAO;
import libraryDB.BookDTO;

public class BookSearch extends JFrame {

	private JTextField textField1;
	private JTable table;
	protected String selectedCategory;
	private JComboBox<String> comboBox;

	/**
	 * Create the frame.
	 */
	public BookSearch() {

		setTitle("도서검색");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 789, 610);
		JPanel contentPane = new JPanel();
		contentPane.setBackground(new Color(255, 255, 255));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		// 힌트 라벨
		JLabel hintLabel = new JLabel("검색어를 입력하세요");
		hintLabel.setBounds(145, 53, 478, 42);
		hintLabel.setFont(new Font("굴림", Font.PLAIN, 20));
		hintLabel.setForeground(Color.GRAY);
		hintLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
		contentPane.add(hintLabel);

		setContentPane(contentPane);
		contentPane.setLayout(null);

		// 검색어 입력 텍스트필드
		textField1 = new JTextField();
		textField1.setFont(new Font("굴림", Font.PLAIN, 20));
		textField1.setBounds(145, 53, 478, 42);
		contentPane.add(textField1);
		textField1.setColumns(10);
		textField1.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() != KeyEvent.VK_ENTER) {
					hintLabel.setVisible(false);
				}
				// 엔터키를 눌렀을 때 검색 기능 수행
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					performSearch();
				}

			}
		});
		
		// 위의 것과 중복이 아닌지 체크할 것!!!!!!!!
		// 엔터키 처리
		textField1.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					performSearch(); // 엔터키를 눌렀을 때 검색 기능 수행
				}
			}
		});

		// 검색 버튼
		JLabel btnNewButton = new JLabel("검색");
		btnNewButton.setBackground(new Color(255, 255, 255));
		btnNewButton.setBounds(642, 53, 71, 42);
		btnNewButton.setOpaque(true);
		btnNewButton.setBackground(Color.WHITE);
		btnNewButton.setHorizontalAlignment(JLabel.CENTER);
		btnNewButton.setFont(new Font("굴림", Font.PLAIN, 20));
		btnNewButton.setBorder(new EmptyBorder(10, 10, 10, 10));
		btnNewButton.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				performSearch();
			}
		});
		contentPane.add(btnNewButton);

		// 뒤로가기 버튼
		JLabel btnNewButton_1_1 = new JLabel("뒤로가기");
		btnNewButton_1_1.setBackground(new Color(255, 255, 255));
		btnNewButton_1_1.setBounds(32, 511, 119, 42);
		btnNewButton_1_1.setOpaque(true);
		btnNewButton_1_1.setBackground(Color.WHITE);
		btnNewButton_1_1.setHorizontalAlignment(JLabel.CENTER);
		btnNewButton_1_1.setFont(new Font("굴림", Font.PLAIN, 20));
		btnNewButton_1_1.setBorder(new EmptyBorder(10, 10, 10, 10));
		btnNewButton_1_1.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {

				dispose(); // 팝업으로 띄웠던 도서검색 창 닫기
			}
		});
		contentPane.add(btnNewButton_1_1);

		// 검색결과 창 판넬
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setFont(new Font("굴림", Font.PLAIN, 20));
		scrollPane.setBackground(Color.LIGHT_GRAY);
		scrollPane.setBounds(45, 114, 687, 377);
		contentPane.add(scrollPane);

		// 검색결과 테이블
		table = new JTable();
		table.setFont(new Font("굴림", Font.PLAIN, 15));
		scrollPane.setViewportView(table);
		table.setModel(new DefaultTableModel(new Object[][] { { "", "", "", "", "" }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null }, { null, null, null, null, null },
				{ null, null, null, null, null }, { null, null, null, null, null }, { null, null, null, null, null }, },
				new String[] { "책번호", "도서명", "저자명", "출판사", "출판일" }));

		DefaultTableModel model = tableModelForAllbooks();
		table.setModel(model);
		table.getColumnModel().getColumn(0).setPreferredWidth(74);
		TableColumnModel columnModel = table.getColumnModel();
		columnModel.getColumn(0).setPreferredWidth(100); // 책번호
		columnModel.getColumn(1).setPreferredWidth(500); // 도서명
		columnModel.getColumn(2).setPreferredWidth(300); // 저자명
		columnModel.getColumn(3).setPreferredWidth(200); // 출판사
		columnModel.getColumn(4).setPreferredWidth(200); // 출판일

		// 테이블 행 높이 설정
		table.setRowHeight(35); // 행의 높이를 35픽셀로 설정

		// 검색범위 설정 콤보박스
		comboBox = new JComboBox<>();
		comboBox.setBackground(new Color(255, 255, 255));
		comboBox.setModel(new DefaultComboBoxModel<>(new String[] { "도서명", "저자명", "출판사", "전체" }));
		comboBox.setMaximumRowCount(4);
		comboBox.setBounds(12, 300, 95, 23);
		contentPane.add(comboBox);

		comboBox.setBackground(Color.white); // 배경색
		comboBox.setForeground(Color.black); // 텍스트 색상

		comboBox.setBounds(46, 55, 87, 35);
		contentPane.add(comboBox);

		table.setDefaultEditor(Object.class, null); //이건 뭘까?
	}

	//모든 도서정보를 테이블에 넣는 메서드
	DefaultTableModel tableModelForAllbooks() {
		DefaultTableModel model = new DefaultTableModel();
		model.addColumn("책번호");
		model.addColumn("도서명");
		model.addColumn("저자명");
		model.addColumn("출판사");
		model.addColumn("출판일");

		setLocationRelativeTo(null);

		try {
			//DB에서 모든 도서정보 불러오는 메서드 호출
			BookDAO dao = new BookDAO();
			ArrayList<BookDTO> bookList = dao.getAllBook();

			for (BookDTO dto : bookList) {
				// 각 회원 데이터를 모델에 행으로 추가
				model.addRow(new Object[] { dto.getBookNum(), dto.getTitle(), dto.getAuthor(), dto.getPublisher(),
						dto.getPubYear(), });
			}
		} catch (Exception e) {
			System.out.println("도서 정보를 가져오는 과정에서 오류 발생 : " + e.getMessage());
			//JOptionPane.showMessageDialog(null, "도서 정보를 가져오는 동안 오류가 발생했습니다: ", "오류", JOptionPane.WARNING_MESSAGE);

		}
		return model;
	}

	//콤보박스(검색범위) 선택 값에 따라 검색결과 띄우는 메서드
	private void filterTableBySearchValue(String selectedCategory, String searchValue) {
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
		table.setRowSorter(sorter);

		if (searchValue.trim().length() == 0) {
			// 검색어가 없으면 모든 행 보이도록 설정
			sorter.setRowFilter(null);
		} else {
			// 선택된 검색 카테고리에 따라 검색어가 포함된 행만 보이도록 설정
			int columnIndex = -1;
			switch (selectedCategory) {
			case "도서명":
				columnIndex = 1; // 도서명 필드의 인덱스 (0부터 시작)
				break;
			case "저자명":
				columnIndex = 2; // 저자명 필드의 인덱스
				break;
			case "출판사":
				columnIndex = 3; // 출판사 필드의 인덱스
				break;
			case "전체":
				// 전체를 선택했을 때는 모든 필드에서 검색
				List<RowFilter<Object, Object>> filters = IntStream.range(1, model.getColumnCount())
						.mapToObj(col -> RowFilter.regexFilter("(?i)" + searchValue, col)).collect(Collectors.toList());
				sorter.setRowFilter(RowFilter.orFilter(filters));
				return;
			}

			// 선택된 검색 카테고리에 따라 검색어가 포함된 행만 보이도록 설정
			sorter.setRowFilter(RowFilter.regexFilter("(?i)" + searchValue, columnIndex));
		}
	}

	private void performSearch() {
		//검색입력 값을 String 변수에 담기
		String searchValue = textField1.getText();
		//검색범위 설정 콤보박스의 선택 값 String 변수에 담기
		String selectedCategory = (String) comboBox.getSelectedItem();
		
		//검색범위 선택 값에 따라 검색결과 띄우는 메서드 호출
		filterTableBySearchValue(selectedCategory, searchValue);
	}
}
