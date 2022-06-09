// import java.io.BufferedReader;
// import java.io.BufferedWriter;
// import java.io.FileInputStream;
// import java.io.FileWriter;
// import java.io.InputStreamReader;
// import java.sql.Connection;
// import java.sql.DriverManager;
// import java.sql.PreparedStatement;
// import java.sql.ResultSet;
// import java.util.ArrayList;
// import java.util.HashMap;
// import java.util.List;

// public class DataP09Exam {

// public static void main(String[] args) throws Exception {
// createTable(connectOracle());
// insertCsv(connectOracle());
// HashMap<String, List<String>> result = findQuery(connectOracle());
// resultToCsv(result);
// }

// // 찾은 row를 csv파일로 변환.
// public static void resultToCsv(HashMap<String, List<String>> result) {
// try {
// BufferedWriter bw = new BufferedWriter(new FileWriter("result.csv")); // csv
// 쓰기

// for (int i = 0; i < result.get("zipcode").size(); i++) {
// String zipcode = result.get("zipcode").get(i);
// String name = result.get("name").get(i);
// String tell = result.get("tell").get(i);
// String bunji = result.get("bunji").get(i);
// String gugun = result.get("gugun").get(i);
// String row = zipcode + "," + name + "," + tell + "," + bunji + "," + gugun;
// bw.write(row);
// bw.newLine();
// }
// bw.flush(); // 버퍼가 다 차지않아서 못입력된 남은 데이터까지 입력
// bw.close(); // 버퍼 해제
// } catch (Exception e) {
// e.printStackTrace();
// }

// }

// // gugun에 해운대구가 들어있는 row를 찾음.
// public static HashMap<String, List<String>> findQuery(Connection conn) {
// try {
// // result(Map)와 result에 담을 각 List선언
// HashMap<String, List<String>> result = new HashMap<>();
// List<String> zipcode = new ArrayList<>();
// List<String> name = new ArrayList<>();
// List<String> tell = new ArrayList<>();
// List<String> bunji = new ArrayList<>();
// List<String> gugun = new ArrayList<>();

// // 쿼리문 oracle로 전송
// String sql = "SELECT ZIPCODE, NAME, TELL, BUNJI, GUGUN FROM ZIPCODE WHERE
// gugun LIKE '%해운대구%'";
// PreparedStatement pstmt = conn.prepareStatement(sql);

// // ResultSet에 담긴 데이터 출력및 List에 추가
// ResultSet rs = pstmt.executeQuery();
// while (rs.next()) {
// System.out.println(rs.getString("ZIPCODE"));
// System.out.println(rs.getString("NAME"));
// System.out.println(rs.getString("TELL"));
// System.out.println(rs.getString("BUNJI"));
// System.out.println(rs.getString("GUGUN"));
// System.out.println("======================================");
// zipcode.add(rs.getString("ZIPCODE"));
// name.add(rs.getString("NAME"));
// tell.add(rs.getString("TELL"));
// bunji.add(rs.getString("BUNJI"));
// gugun.add(rs.getString("GUGUN"));
// }

// // 각 칼럼명에 해당하는 List를 Map에 추가
// result.put("zipcode", zipcode);
// result.put("name", name);
// result.put("tell", tell);
// result.put("bunji", bunji);
// result.put("gugun", gugun);
// return result;
// } catch (Exception e) {
// e.printStackTrace();
// }
// return null;
// }

// }
