import filetofile.ResultToCsv;
import filetofile.TextToCsv;
import oracle.ConnectOracle;
import oracle.InsertOracle;
import oracle.SelectZipcode;

public class App {

    public static void main(String[] args) {
        TextToCsv ttc = new TextToCsv();
        ConnectOracle co = new ConnectOracle();
        InsertOracle io = new InsertOracle(ttc, co);
        SelectZipcode sz = new SelectZipcode(co);
        ResultToCsv rtc = new ResultToCsv();

        String txtFileName = "zipcode.txt";
        String csvFileName = "zipcode.csv";
        String encType = "UTF-8";
        String tableName = "zipoffice";
        String city = "해운대구";
        String resultFileName = "result.txt";

        int success = ttc.process(txtFileName, csvFileName, encType);
        if (success == 1) {
            success = io.process(tableName, csvFileName, encType);
            if (success == 1) {
                success = rtc.process(sz.findQuery(city), resultFileName, encType);
                if (success == 1) {
                    System.out.println("성공적으로 실행을 마쳤습니다.");
                }
            }
        }
    }
}
