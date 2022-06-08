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

public class DataP09Exam {
    public static void main(String[] args) throws Exception {
        textToCsv();
        createTable(connectOracle());
        insertCsv(connectOracle());
        HashMap<String, List<String>> result = findQuery(connectOracle());
        resultToCsv(result);
    }

    // 찾은 row를 csv파일로 변환.
    public static void resultToCsv(HashMap<String, List<String>> result) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("result.csv")); // csv 쓰기

            // result에 있는 value의 size만큼 for문을 돌려 write로 데이터 입력.
            for (int i = 0; i < result.get("zipcode").size(); i++) {
                String zipcode = result.get("zipcode").get(i);
                String name = result.get("name").get(i);
                String tell = result.get("tell").get(i);
                String bunji = result.get("bunji").get(i);
                String gugun = result.get("gugun").get(i);
                String row = zipcode + "," + name + "," + tell + "," + bunji + "," + gugun;
                bw.write(row);
                bw.newLine();
            }
            bw.flush(); // 버퍼가 다 차지않아서 못입력된 남은 데이터까지 입력
            bw.close(); // 버퍼 해제
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // gugun에 해운대구가 들어있는 row를 찾음.
    public static HashMap<String, List<String>> findQuery(Connection conn) {
        try {
            // result(Map)와 result에 담을 각 List선언
            HashMap<String, List<String>> result = new HashMap<>();
            List<String> zipcode = new ArrayList<>();
            List<String> name = new ArrayList<>();
            List<String> tell = new ArrayList<>();
            List<String> bunji = new ArrayList<>();
            List<String> gugun = new ArrayList<>();

            // 쿼리문 oracle로 전송
            String sql = "SELECT ZIPCODE, NAME, TELL, BUNJI, GUGUN FROM ZIPCODE WHERE gugun LIKE '%해운대구%'";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.executeQuery();

            // ResultSet에 담긴 데이터 출력및 List에 추가
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                System.out.println(rs.getString("ZIPCODE"));
                System.out.println(rs.getString("NAME"));
                System.out.println(rs.getString("TELL"));
                System.out.println(rs.getString("BUNJI"));
                System.out.println(rs.getString("GUGUN"));
                System.out.println("======================================");
                zipcode.add(rs.getString("ZIPCODE"));
                name.add(rs.getString("NAME"));
                tell.add(rs.getString("TELL"));
                bunji.add(rs.getString("BUNJI"));
                gugun.add(rs.getString("GUGUN"));
            }

            // 각 칼럼명에 해당하는 List를 Map에 추가
            result.put("zipcode", zipcode);
            result.put("name", name);
            result.put("tell", tell);
            result.put("bunji", bunji);
            result.put("gugun", gugun);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // csv파일로 zipcode테이블 생성
    public static void createTable(Connection conn) {
        try {
            StringBuffer sb = new StringBuffer();
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(new FileInputStream("zipoffice.csv")));
            String[] column = br.readLine().split(","); // zipoffice의 첫번째 라인을 ,를 기준으로 쪼갬

            // StringBuffer로 table생성하기 위한 쿼리를 담아줌
            sb.append("CREATE TABLE ZIPCODE (");
            sb.append(column[0] + " VARCHAR2(100), ");
            sb.append(column[1] + " VARCHAR2(100), ");
            sb.append(column[2] + " VARCHAR2(100), ");
            sb.append(column[3] + " VARCHAR2(100), ");
            sb.append(column[4] + " VARCHAR2(100) ");
            sb.append(")");

            // StringBuffer에 담긴 쿼리를 oracle에 전송
            PreparedStatement pstmt = conn.prepareStatement(sb.toString());
            pstmt.execute();

            br.close(); // BufferedReader 해제
            pstmt.close(); // PreparedStatement 해제
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // 생성한 zipcode 테이블에 csv파일의 row를 집어넣음.
    public static void insertCsv(Connection conn) {
        try {
            String input = ""; // readLine으로 읽는 데이터를 넣을 변수 선언

            BufferedReader br = new BufferedReader(
                    new InputStreamReader(new FileInputStream("zipoffice.csv")));

            br.readLine(); // 맨 윗줄은 칼럼명이라 한줄 제외하고 시작
            while ((input = br.readLine()) != null) { // 파일에서 읽은 한줄씩 input에 넣음
                String[] row = input.split(","); // row에 해당하는 부분을 ,를 기준으로 쪼갬
                String sql = "INSERT INTO ZIPCODE VALUES(?,?,?,?,?)";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, row[0]);
                pstmt.setString(2, row[1]);
                pstmt.setString(3, row[2]);
                pstmt.setString(4, row[3]);
                pstmt.setString(5, row[4]);
                pstmt.executeUpdate();
                pstmt.close(); // pstmt가 계속생성되므로 한번 돌때마다 닫아줌.
            }
            br.close(); // BufferedReader 해제
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // oracle 연결
    public static Connection connectOracle() {
        try {
            // Oracle에 연결
            Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "SCOTT", "TIGER");
            return conn;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // txt파일을 csv파일로 바꿔서 저장
    public static void textToCsv() {
        try {
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(new FileInputStream("zipoffice.txt"))); // txt읽기
            BufferedWriter bw = new BufferedWriter(new FileWriter("zipoffice.csv")); // csv 쓰기
            String str; // txt내용을 담을 변수
            while ((str = br.readLine()) != null) { // readLine()을 사용해서 1줄씩 읽음.
                str = str.replace("^", ","); // csv파일로 바꾸기위해 구분자를 ^를 ,로 변경
                bw.write(str);
                bw.newLine();
            }
            bw.flush(); // 남아있는데이터까지 flush해서 담아줌.
            br.close(); // 사용한 BufferedReader를 닫아줌
            bw.close(); // 사용한 BufferedWriter를 닫아줌
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
