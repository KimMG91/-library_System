package library;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Font;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class KingOfReader extends JFrame {

   private JPanel contentPane;
   private JList<String> list;

   /**
    * Launch the application.
    */
   public static void main(String[] args) {
      EventQueue.invokeLater(new Runnable() {
         public void run() {
            try {
               KingOfReader frame = new KingOfReader();
               frame.setVisible(true);
            } catch (Exception e) {
               e.printStackTrace();
            }
         }
      });
   }
   
   /**
    * Create the frame.
    */
   public KingOfReader() {
      setTitle("독서왕");
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      setBounds(100, 100, 351, 402);
      contentPane = new JPanel();
      contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

      setContentPane(contentPane);
      contentPane.setLayout(new BorderLayout(0, 0));

      JPanel panel = new JPanel();
      contentPane.add(panel);
      panel.setLayout(null);

      JLabel lblRanking = new JLabel("다독자 TOP 10");
      lblRanking.setHorizontalAlignment(SwingConstants.CENTER);
      lblRanking.setFont(new Font("굴림", Font.BOLD, 30));
      lblRanking.setBounds(10, 10, 299, 30);
      panel.add(lblRanking);

      list = new JList<String>();
      list.setFont(new Font("Arial Unicode MS", Font.PLAIN, 20));
      list.setBounds(0, 50, 321, 310);
      panel.add(list);

      loadData();
      
      String crownEmoji = "\uD83D\uDC51";
        
      // 화면 가운데로 정렬
      setLocationRelativeTo(null);
      
       // JList 가운데 정렬
        DefaultListCellRenderer renderer = (DefaultListCellRenderer) list.getCellRenderer();
        renderer.setHorizontalAlignment(SwingConstants.CENTER);
   }

   
   private void loadData() {
       try {
           String url = "jdbc:mariadb://14.42.124.85/library_db";
           String user = "hanul"; 
           String password = "910725"; 
           Connection conn = DriverManager.getConnection(url,user,password);
           Statement stmt = conn.createStatement();

           String sql = "SELECT member_db.name, COUNT(rental_history.bookNum) AS bookNum " +
                        "FROM member_db " +
                        "JOIN rental_history ON member_db.memberNum = rental_history.memberNum " +
                        "WHERE member_db.memberNum != 1 " + // memberNum이 1이 아닌 경우만 검색
                        "GROUP BY member_db.name " +
                        "ORDER BY bookNum DESC LIMIT 10";
           
           ResultSet resultSet = stmt.executeQuery(sql);

           
           
           String crownEmoji = "\u265A"; // 왕관 이모티콘
          
           
           DefaultListModel<String> model = new DefaultListModel<>();
           int rank = 1;
           while (resultSet.next()) {
               String memberName = resultSet.getString("name");
               int bookCount = resultSet.getInt("bookNum");
               String rName;
               // 순위와 도서 제목을 합쳐서 모델에 추가
               if(1==rank) {
                  rName =crownEmoji +rank +"위 "+ memberName  + " ("+bookCount + "권)"+crownEmoji ;
               } else {
                  rName = rank+"위 " + memberName + " (" + bookCount+"권)";
               }
               model.addElement(rName);
               rank++;
           }
           list.setModel(model);

           resultSet.close();
           stmt.close();
           conn.close();
       } catch (Exception e) {
           e.printStackTrace();
       }
   }
}