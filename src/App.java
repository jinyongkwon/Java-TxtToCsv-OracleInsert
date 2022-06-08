import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class App {
    public static void main(String[] args) throws Exception {
        textToCsv();
        createTable(connectOracle());
        insertCsv(connectOracle());
        HashMap<String, List<String>> result = findQuery(connectOracle());
    }

    public static HashMap<String, List<String>> findQuery(Connection conn) {
        try {
            HashMap<String, List<String>> result = new HashMap<>();
            List<String> zipcode = new ArrayList<>();
            List<String> name = new ArrayList<>();
            List<String> tell = new ArrayList<>();
            List<String> bunji = new ArrayList<>();
            List<String> gugun = new ArrayList<>();

            String sql = "SELECT ZIPCODE, NAME, TELL, BUNJI, GUGUN FROM ZIPCODE WHERE gugun LIKE '%해운대구%'";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.executeQuery();
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                zipcode.add(rs.getString("ZIPCODE"));
                name.add(rs.getString("NAME"));
                tell.add(rs.getString("TELL"));
                bunji.add(rs.getString("BUNJI"));
                gugun.add(rs.getString("GUGUN"));
            }
            result.put("zipcode", zipcode);
            result.put("name", name);
            result.put("tell", tell);
            result.put("bunji", bunji);
            result.put("gugun", gugun);
            System.out.println(result.get("zipcode").get(0));
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void createTable(Connection conn) {
        try {

            StringBuffer sb = new StringBuffer();
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(new FileInputStream("zipoffice.csv")));
            String[] column = reader.readLine().split(",");
            sb.append("CREATE TABLE ZIPCODE (");
            sb.append(column[0] + " VARCHAR2(100), ");
            sb.append(column[1] + " VARCHAR2(100), ");
            sb.append(column[2] + " VARCHAR2(100), ");
            sb.append(column[3] + " VARCHAR2(100), ");
            sb.append(column[4] + " VARCHAR2(100) ");
            sb.append(")");
            PreparedStatement pstmt = conn.prepareStatement(sb.toString());
            pstmt.execute();
            reader.close();
            pstmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void insertCsv(Connection conn) {
        try {
            int result = 0;
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(new FileInputStream("zipoffice.csv")));
            reader.readLine();
            while (true) {
                String[] row = reader.readLine().split(",");
                String sql = "INSERT INTO ZIPCODE VALUES(?,?,?,?,?)";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, row[0]);
                pstmt.setString(2, row[1]);
                pstmt.setString(3, row[2]);
                pstmt.setString(4, row[3]);
                pstmt.setString(5, row[4]);
                result = pstmt.executeUpdate();
                pstmt.close();
                if (result < 1) {
                    break;
                }
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static Connection connectOracle() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "SCOTT", "TIGER");
            return conn;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void textToCsv() {
        try {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(new FileInputStream("zipoffice.txt"))); // txt읽기
            BufferedWriter writer = new BufferedWriter(new FileWriter("zipoffice.csv")); // csv 쓰기
            String str; // txt내용을 담을 변수
            while ((str = reader.readLine()) != null) { // readLine()을 사용해서 1줄씩 읽음.
                str = str.replace("^", ","); // csv파일로 바꾸기위해 구분자를 ^를 ,로 변경
                writer.write(str);
                writer.newLine();
            }
            writer.flush(); // 남아있는데이터까지 flush해서 담아줌.
            reader.close(); // 사용한 BufferedReader를 닫아줌
            writer.close(); // 사용한 BufferedWriter를 닫아줌
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
