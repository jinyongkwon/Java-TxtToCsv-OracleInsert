package oracle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SelectZipcode {

    private ConnectOracle connectOracle;

    public SelectZipcode(ConnectOracle connectOracle) {
        this.connectOracle = connectOracle;
    }

    // gugun에 해운대구가 들어있는 row를 찾음.
    public HashMap<String, List<String>> findQuery(String city) {
        try {
            Connection conn = connectOracle.connectOracle();
            // result(Map)와 result에 담을 각 List선언
            HashMap<String, List<String>> result = new HashMap<>();
            List<String> zipcode = new ArrayList<>();
            List<String> name = new ArrayList<>();
            List<String> tell = new ArrayList<>();
            List<String> dong = new ArrayList<>();
            List<String> gugun = new ArrayList<>();

            // 쿼리문 oracle로 전송
            String sql = "SELECT zipcode, name, tell, dong, gugun FROM zipoffice WHERE gugun LIKE '%'||?||'%'";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, city);

            // ResultSet에 담긴 데이터 출력및 List에 추가
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                System.out.println(rs.getString("zipcode"));
                System.out.println(rs.getString("name"));
                System.out.println(rs.getString("tell"));
                System.out.println(rs.getString("dong"));
                System.out.println(rs.getString("gugun"));
                System.out.println("======================================");
                zipcode.add(rs.getString("zipcode"));
                name.add(rs.getString("name"));
                tell.add(rs.getString("tell"));
                dong.add(rs.getString("dong"));
                gugun.add(rs.getString("gugun"));
            }

            // 각 칼럼명에 해당하는 List를 Map에 추가
            result.put("zipcode", zipcode);
            result.put("name", name);
            result.put("tell", tell);
            result.put("dong", dong);
            result.put("gugun", gugun);
            conn.close();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
