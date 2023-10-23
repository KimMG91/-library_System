package library;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import libraryDB.BookDAO;
import libraryDB.BookDTO;
import libraryDB.MemberDTO;
import libraryDB.RentalHistoryDAO;

import javax.swing.JScrollPane;

import java.awt.Font;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.awt.event.ActionEvent;
import javax.swing.JSeparator;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

public class BookRentReturn extends JFrame {

	private JPanel contentPane;
	private JTable selectedMemberTable;
	private JScrollPane scrollPane;
	private JLabel selectedMemberLabel;
	private JLabel nowRentalLabel;
	private JScrollPane scrollPane_1;
	private JTextField searchTF;
	private JTable resultBookTable;
	private JScrollPane scrollPane_2;
	private JButton btn_BookChoose;
	private JButton btn_GoBack;
	private JButton btn_Rental;
	private JButton btn_Return;
	private JButton btn_nowRentalChoose;
	private JSeparator separator;
	RentalHistoryDAO rentalHistory = new RentalHistoryDAO();
	BookDTO bookToRent = new BookDTO();
	BookDTO bookToReturn = new BookDTO();

	boolean btn_nowRentalChooseClicked = false;
	boolean btn_BookChooseClicked = false;
	/**
	 * Launch the application.
	 */

	/*
	 * public static void main(String[] args) { EventQueue.invokeLater(new
	 * Runnable() { public void run() { try { BookRentReturn frame = new
	 * BookRentReturn(selectedBook); frame.setVisible(true); } catch (Exception e) {
	 * e.printStackTrace(); } } }); }
	 */

	/**
	 * Create the frame.
	 * 
	 * @throws Exception
	 */
	public BookRentReturn(MemberDTO selectedMember) throws Exception {
		setTitle("대여 / 반납");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 900, 679);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		// 선택한 회원 라벨
		selectedMemberLabel = new JLabel("선택한 회원의 정보");
		selectedMemberLabel.setHorizontalAlignment(SwingConstants.CENTER);
		selectedMemberLabel.setFont(new Font("굴림", Font.PLAIN, 18));
		selectedMemberLabel.setBounds(12, 14, 854, 24);
		contentPane.add(selectedMemberLabel);

		scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 44, 854, 48);
		contentPane.add(scrollPane);

		// 선택한 회원 정보 테이블
		// 회원번호, 아이디, 이름, 전화번호, 생년월일, 상태
		selectedMemberTable = new JTable();
		selectedMemberTable.setFont(new Font("굴림", Font.PLAIN, 18));
		scrollPane.setViewportView(selectedMemberTable);
		DefaultTableModel model = new DefaultTableModel();
		model.addColumn("회원번호");
		model.addColumn("아이디");
		model.addColumn("이름");
		model.addColumn("전화번호");
		model.addColumn("생년월일");

		model.addRow(new Object[] { selectedMember.getMemberNum(), selectedMember.getId(), selectedMember.getName(),
				selectedMember.getPhoneNum(), selectedMember.getBirthday() });

		selectedMemberTable.setModel(model);
		selectedMemberTable.getColumnModel().getColumn(0).setPreferredWidth(50);
		selectedMemberTable.setRowHeight(30);

		// 대여현황 라벨
		nowRentalLabel = new JLabel("대여현황 : " + rentalHistory.countNowRental(selectedMember.getMemberNum()));
		nowRentalLabel.setHorizontalAlignment(SwingConstants.CENTER);
		nowRentalLabel.setFont(new Font("굴림", Font.PLAIN, 18));
		nowRentalLabel.setBounds(12, 102, 854, 24);
		contentPane.add(nowRentalLabel);

		scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(12, 131, 854, 139);
		contentPane.add(scrollPane_1);

		// 선택한 회원의 대여현황 테이블
		// 도서번호, 제목, 저자, 대여일, 반납예정일
		JTable nowRentalTable = new JTable();
		nowRentalTable.setFont(new Font("굴림", Font.PLAIN, 18));
		scrollPane_1.setViewportView(nowRentalTable);
		DefaultTableModel nowRental = new DefaultTableModel();

		nowRental.addColumn("도서번호");
		nowRental.addColumn("도서명");
		nowRental.addColumn("저자명");
		nowRental.addColumn("대여일");
		nowRental.addColumn("반납예정일");
		// nowRentalTable.setModel(nowRental);

		RentalHistoryDAO rentalHistory = new RentalHistoryDAO();
		ArrayList<BookDTO> nowRentalList = rentalHistory.getNowRentals(selectedMember.getMemberNum());
		// System.out.println(nowRentalList);
		for (BookDTO rental : nowRentalList) {
			nowRental.addRow(new Object[] { rental.getBookNum(), rental.getTitle(), rental.getAuthor(),
					rental.getRentDt(), rental.getDueDate(rental.getRentDt()) });
		}
		nowRentalTable.setModel(nowRental);

		nowRentalTable.getColumnModel().getColumn(0).setPreferredWidth(50);
		nowRentalTable.getColumnModel().getColumn(1).setPreferredWidth(70);
		nowRentalTable.setRowHeight(30);

		// 해당 회원이 현재 대여중인 책 선택 버튼
		btn_nowRentalChoose = new JButton("반납도서선택");
		btn_nowRentalChoose.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				bookToReturn = selectNowRentalkRow(nowRentalTable);
				if (bookToReturn == null) {
					JOptionPane.showMessageDialog(null, "도서가 선택되지 않았습니다.", "오류", JOptionPane.WARNING_MESSAGE);
				} else {
					btn_nowRentalChooseClicked = true;
				}
			}
		});
		btn_nowRentalChoose.setFont(new Font("굴림", Font.PLAIN, 18));
		btn_nowRentalChoose.setBounds(603, 280, 153, 31);
		contentPane.add(btn_nowRentalChoose);

		// 구분선
		separator = new JSeparator();
		separator.setBounds(12, 321, 854, 2);
		contentPane.add(separator);

		// 도서 검색 항목
		JComboBox<String> comboBox = new JComboBox<>();
		comboBox.setModel(new DefaultComboBoxModel<>(new String[] { "전체", "도서명", "저자명", "출판사" }));
		comboBox.setMaximumRowCount(3);
		comboBox.setBounds(12, 333, 84, 28);
		contentPane.add(comboBox);

		// 도서 검색창
		searchTF = new JTextField();
		searchTF.setFont(new Font("굴림", Font.PLAIN, 18));
		searchTF.setBounds(108, 333, 648, 28);
		contentPane.add(searchTF);
		searchTF.setColumns(10);

		// 도서 검색 버튼
		JButton btn_Search = new JButton("\uAC80\uC0C9");
		btn_Search.setFont(new Font("굴림", Font.PLAIN, 18));
		btn_Search.setBounds(768, 333, 98, 32);
		contentPane.add(btn_Search);

		scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(12, 375, 854, 183);
		contentPane.add(scrollPane_2);

		// 도서 검색 결과창
		// 도서번호, 제목, 저자, 출판사, 출판일, 상태
		resultBookTable = new JTable();
		resultBookTable.setFont(new Font("굴림", Font.PLAIN, 18));
		scrollPane_2.setViewportView(resultBookTable);
		DefaultTableModel bookResult = new DefaultTableModel();

		bookResult.addColumn("도서번호");
		bookResult.addColumn("도서명");
		bookResult.addColumn("저자명");
		bookResult.addColumn("출판사");
		bookResult.addColumn("상태");

		// 도서 검색 버튼에 이벤트 걸기
		btn_Search.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String selectedValue = comboBox.getSelectedItem().toString();
				// System.out.println(selectedValue);
				String searchText = searchTF.getText();
				// System.out.println(searchText);
				try {
					String whereSql = "";
					if (searchText.equals("") || searchText.equals(" ") || searchText == null) {
						// System.out.println(selectedValue);
						JOptionPane.showMessageDialog(null, "검색어 입력 후 검색버튼을 눌러주세요.", "오류", JOptionPane.WARNING_MESSAGE);
					} else {
						switch (selectedValue) {
						case "전체":
							whereSql = "book_db.title LIKE '%" + searchText + "%'\r\n" + " OR book_db.author LIKE '%"
									+ searchText + "%'\r\n" + " OR book_db.publisher LIKE '%" + searchText + "%'";
							break;
						case "도서명":
							whereSql = "book_db.title LIKE '%" + searchText + "%'";
							break;
						case "저자명":
							whereSql = "book_db.author LIKE '%" + searchText + "%'";
							break;
						case "출판사":
							whereSql = "book_db.publisher LIKE '%" + searchText + "%'";
							break;
						}
						// System.out.println(whereSql);
						// 검색 버튼을 누를 때마다 테이블을 비우고 새로운 결과를 추가
						clearTable(bookResult);

						RentalHistoryDAO rentalHistory = new RentalHistoryDAO();
						ArrayList<BookDTO> bookList = rentalHistory.searchBookToRent(whereSql);
						// System.out.println(bookList);
						for (BookDTO dto : bookList) {
							bookResult.addRow(new Object[] { dto.getBookNum(), dto.getTitle(), dto.getAuthor(),
									dto.getPublisher(), dto.getState() });
							resultBookTable.setModel(bookResult);
						}

					}

				} catch (Exception e1) {
					System.out.println("도서 검색결과를 가져오는 동안 오류가 발생했습니다: " + e1.getMessage());
				}
			}
		});

		resultBookTable.setModel(bookResult);
		resultBookTable.getColumnModel().getColumn(0).setPreferredWidth(50);
		resultBookTable.getColumnModel().getColumn(1).setPreferredWidth(100);
		resultBookTable.getColumnModel().getColumn(4).setPreferredWidth(60);
		resultBookTable.setRowHeight(30);

		// 검색 결과 도서 선택 버튼
		btn_BookChoose = new JButton("대여도서선택");
		btn_BookChoose.setFont(new Font("굴림", Font.PLAIN, 18));
		btn_BookChoose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) { // 선택버튼을 클릭시
				bookToRent = selectBookRow(resultBookTable);
				if (bookToRent == null) {
					JOptionPane.showMessageDialog(null, "도서가 선택되지 않았습니다.", "오류", JOptionPane.WARNING_MESSAGE);
				} else {
					btn_BookChooseClicked = true;
				}
			}
		});
		btn_BookChoose.setBounds(603, 568, 153, 33);
		contentPane.add(btn_BookChoose);

		// 대여 버튼
		btn_Rental = new JButton("\uB300\uC5EC");
		btn_Rental.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (!btn_BookChooseClicked) {// 선택버튼을 안 누른 경우
					JOptionPane.showMessageDialog(null, "대여도서선택 버튼을 눌러주세요.", "오류", JOptionPane.WARNING_MESSAGE);
				} else {// 선택버튼을 누른 경우
					if (bookToRent.getState().equals("반납완료")) {
						try {
							rentalHistory.rentBook(selectedMember.getMemberNum(), bookToRent.getBookNum());
							dispose();
							BookRentReturn bookRentReturn = new BookRentReturn(selectedMember);
							bookRentReturn.setVisible(true);
						} catch (Exception e1) {
							System.out.println("대여과정에서 오류 발생 : " + e1.getMessage());
						}
					} else {
						JOptionPane.showMessageDialog(null, "이미 대여중인 도서입니다.", "오류", JOptionPane.WARNING_MESSAGE);
					}
				}
			}
		});
		btn_Rental.setFont(new Font("굴림", Font.PLAIN, 18));
		btn_Rental.setBounds(768, 568, 98, 33);
		contentPane.add(btn_Rental);

		// 반납 버튼
		btn_Return = new JButton("\uBC18\uB0A9");
		btn_Return.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				if (!btn_nowRentalChooseClicked) {
					JOptionPane.showMessageDialog(null, "반납도서선택 버튼을 눌러주세요.", "오류", JOptionPane.WARNING_MESSAGE);
				} else {
					if (bookToReturn != null) {
						try {
							rentalHistory.returnBook(selectedMember.getMemberNum(), bookToReturn.getBookNum());
							dispose();
							BookRentReturn bookRentReturn = new BookRentReturn(selectedMember);
							bookRentReturn.setVisible(true);
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}else {
						JOptionPane.showMessageDialog(null, "선택된 도서가 없습니다.", "오류", JOptionPane.WARNING_MESSAGE);
					}
				}
			}
		});
		btn_Return.setFont(new Font("굴림", Font.PLAIN, 18));
		btn_Return.setBounds(768, 280, 98, 31);
		contentPane.add(btn_Return);

		// 뒤로 가기 버튼
		btn_GoBack = new JButton("\uB4A4\uB85C \uAC00\uAE30");
		btn_GoBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				MemberManagement mm = new MemberManagement();
				mm.setVisible(true);
			}
		});
		btn_GoBack.setFont(new Font("굴림", Font.PLAIN, 18));
		btn_GoBack.setBounds(12, 599, 119, 31);
		contentPane.add(btn_GoBack);

		// 화면 가운데로 정렬
		setLocationRelativeTo(null);

	}

	public BookDTO selectNowRentalkRow(JTable tableName) {

		int selectedRow = tableName.getSelectedRow();
		BookDTO selectedBook = new BookDTO();

		if (selectedRow != -1) { // 행이 선택된 경우
			int bookNum = (int) tableName.getValueAt(selectedRow, 0);
			String title = (String) tableName.getValueAt(selectedRow, 1);
			String author = (String) tableName.getValueAt(selectedRow, 2);
			Timestamp rentDt = (Timestamp) tableName.getValueAt(selectedRow, 3);
			Timestamp returnDt = (Timestamp) tableName.getValueAt(selectedRow, 4);

			// 선택한 행의 정보를 'selectedBook' 객체에 저장
			selectedBook.setBookNum(bookNum);
			selectedBook.setTitle(title);
			selectedBook.setAuthor(author);
			selectedBook.setRentDt(rentDt);
			selectedBook.setReturnDt(returnDt);

			return selectedBook;
		} else {
			// 행이 선택되지 않은 경우
			System.out.println("선택된 행이 없습니다.");
		}

		return selectedBook;
	}

	public BookDTO selectBookRow(JTable tableName) {

		int selectedRow = tableName.getSelectedRow();
		BookDTO selectedBook = new BookDTO();

		if (selectedRow != -1) { // 행이 선택된 경우
			int bookNum = (int) tableName.getValueAt(selectedRow, 0);
			String title = (String) tableName.getValueAt(selectedRow, 1);
			String author = (String) tableName.getValueAt(selectedRow, 2);
			String publisher = (String) tableName.getValueAt(selectedRow, 3);
			String state = (String) tableName.getValueAt(selectedRow, 4);

			// 선택한 행의 정보를 'selectedBook' 객체에 저장
			selectedBook.setBookNum(bookNum);
			selectedBook.setTitle(title);
			selectedBook.setAuthor(author);
			selectedBook.setPublisher(publisher);
			selectedBook.setState(state);
			return selectedBook;
		} else {
			// 행이 선택되지 않은 경우
			System.out.println("선택된 행이 없습니다.");
		}

		return selectedBook;
	}

	private void clearTable(DefaultTableModel tableModel) {
		int rowCount = tableModel.getRowCount();
		for (int i = rowCount - 1; i >= 0; i--) {
			tableModel.removeRow(i);
		}
	}
}
