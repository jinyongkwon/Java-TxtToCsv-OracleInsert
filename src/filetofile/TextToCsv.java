package filetofile;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;

import util.FIlePath;

public class TextToCsv {

    // txt파일 읽기
    public String readFile(String FileName, String ecnType) {
        try {
            BufferedReader br = new BufferedReader(
                    new FileReader(FIlePath.RESOURCE + FileName, Charset.forName(ecnType))); // txt읽기
            String str; // txt내용을 담을 변수
            StringBuffer sb = new StringBuffer();
            while ((str = br.readLine()) != null) { // readLine()을 사용해서 1줄씩 읽음.
                sb.append(str + "\n");
            }
            br.close();
            return sb.toString();
        } catch (Exception e) {
            System.out.println("파일 경로가 잘못되었습니다.");
        }
        return null;
    }

    private String changeCsv(String txtFileName, String targetStr, String replaceStr) {
        return txtFileName.replace(targetStr, replaceStr);
    }

    // 파일쓰기
    private int writeFile(String FileName, String ecnType, String data) {
        try {
            BufferedWriter bw = new BufferedWriter(
                    new FileWriter(FIlePath.RESOURCE + FileName, Charset.forName(ecnType))); // csv 쓰기
            bw.write(data);
            bw.flush(); // 남아있는데이터까지 flush해서 담아줌.
            bw.close(); // 사용한 BufferedWriter를 닫아줌
            return 1;
        } catch (Exception e) {
            System.out.println("파일 쓰기에 실패하였습니다. 이유:" + e.getMessage());
        }
        return -1;
    }

    public int process(String txtFileName, String csvFileName, String encType) {
        String txtFile = readFile(txtFileName, encType);
        String changeFile = changeCsv(txtFile, "^", ",");
        return writeFile(csvFileName, encType, changeFile);
    }
}
