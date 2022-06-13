package oracle;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectOracle {
    // oracle 연결
    public Connection connectOracle() {
        try {
            // Oracle에 연결
            return DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "SCOTT", "TIGER");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
