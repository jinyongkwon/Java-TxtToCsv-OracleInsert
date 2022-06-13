package util;

import java.nio.file.Path;
import java.nio.file.Paths;

public class FIlePath {
    // 파일 경로 지정
    private static final String WORKSPACE = windowWorkspace();
    public static final String RESOURCE = WORKSPACE + "resource\\";

    // 파일 경로를 가져와서 spring의 파일경로로 파싱
    private static String windowWorkspace() {
        Path currentPath = Paths.get("");
        String path = currentPath.toAbsolutePath().toString();
        path = path.replace("\\", "\\\\");
        return path + "\\\\";
    }
}
