package filetofile;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import util.FIlePath;

public class ResultToCsv {

    public List<String> resultToList(HashMap<String, List<String>> result) {
        List<String> rowList = new ArrayList<>();
        for (int i = 0; i < result.get("zipcode").size(); i++) {
            String zipcode = result.get("zipcode").get(i);
            String name = result.get("name").get(i);
            String tell = result.get("tell").get(i);
            String dong = result.get("dong").get(i);
            String gugun = result.get("gugun").get(i);
            rowList.add(zipcode + "," + name + "," + tell + "," + dong + "," + gugun);
        }
        return rowList;
    }

    // 찾은 row를 csv파일로 변환.
    public static int ListToCsv(List<String> rowList, String ResultFileName, String encType) {
        try {
            BufferedWriter bw = new BufferedWriter(
                    new FileWriter(FIlePath.RESOURCE + ResultFileName, Charset.forName(encType))); // csv쓰기
            for (String row : rowList) {
                bw.write(row);
                bw.newLine();
            }
            bw.flush(); // 버퍼가 다 차지않아서 못입력된 남은 데이터까지 입력
            bw.close(); // 버퍼 해제
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;

    }

    public int process(HashMap<String, List<String>> result, String ResultFileName, String encType) {
        List<String> rowList = resultToList(result);
        return ListToCsv(rowList, ResultFileName, encType);
    }
}
