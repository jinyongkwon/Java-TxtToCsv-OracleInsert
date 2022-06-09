public class App {

    public static void main(String[] args) {
        TextToCsv ttc = new TextToCsv();
        ConnectOracle co = new ConnectOracle();
        InsertOracle io = new InsertOracle(ttc, co);
        int success = ttc.process("zipcode.txt", "zipcode.csv");
        if (success == 1) {
            io.process("zipcode.csv", "UTF-8", "zipoffice");
        }
    }
}
