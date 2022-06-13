package oracle;

import java.sql.Connection;
import java.sql.PreparedStatement;

import filetofile.TextToCsv;

public class InsertOracle {

    private TextToCsv textToCsv;
    private ConnectOracle connectOracle;

    public InsertOracle(TextToCsv textToCsv, ConnectOracle connectOracle) {
        this.textToCsv = textToCsv;
        this.connectOracle = connectOracle;
    }

    private String[] csvLine(String csvFileName, String encType) {
        String csvData = textToCsv.readFile(csvFileName, encType);
        return csvData.split("\n");
    }

    // csv파일로 zipcode테이블 생성
    private int createTable(String tableName, String csvFileName, String encType) {
        try {
            Connection conn = connectOracle.connectOracle();
            StringBuffer sb = new StringBuffer();
            String[] csvLine = csvLine(csvFileName, encType);
            String[] column = csvLine[0].split(",");
            // StringBuffer로 table생성하기 위한 쿼리를 담아줌
            sb.append("CREATE TABLE " + tableName + " (");
            sb.append(column[0] + " VARCHAR2(100), ");
            sb.append(column[1] + " VARCHAR2(100), ");
            sb.append(column[2] + " VARCHAR2(100), ");
            sb.append(column[3] + " VARCHAR2(100), ");
            sb.append(column[4] + " VARCHAR2(100) ");
            sb.append(")");

            // StringBuffer에 담긴 쿼리를 oracle에 전송
            PreparedStatement pstmt = conn.prepareStatement(sb.toString());
            pstmt.execute();

            pstmt.close(); // PreparedStatement 해제
            conn.close();
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    // 생성한 zipcode 테이블에 csv파일의 row를 집어넣음.
    private int insertCsv(String tableName, String csvFileName, String encType) {
        try {
            Connection conn = connectOracle.connectOracle();
            String[] csvLine = csvLine(csvFileName, encType);
            for (int i = 1; i < csvLine.length; i++) {
                String[] row = csvLine[i].split(","); // row에 해당하는 부분을 ,를 기준으로 쪼갬
                String sql = "INSERT INTO " + tableName + " VALUES(?,?,?,?,?)";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, row[0]);
                pstmt.setString(2, row[1]);
                pstmt.setString(3, row[2]);
                pstmt.setString(4, row[3]);
                pstmt.setString(5, row[4]);
                pstmt.executeUpdate();
                pstmt.close(); // pstmt가 계속생성되므로 한번 돌때마다 닫아줌.
            }
            conn.close();
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;

    }

    public int process(String tableName, String csvFileName, String encType) {
        int success = createTable(tableName, csvFileName, encType);
        if (success == 1) {
            return insertCsv(tableName, csvFileName, encType);
        } else {
            return insertCsv(tableName, csvFileName, encType);
        }
    }

}
