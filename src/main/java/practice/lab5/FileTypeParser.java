package practice.lab5;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class FileTypeParser {

    public static void main(String[] args) throws IOException {
        String filename = args[0];
        Map<String, int[]> headerMap = new HashMap<>();
        headerMap.put("png", new int[]{0x89, 0x50, 0x4e, 0x47});
        headerMap.put("zip or jar", new int[]{0x50, 0x4b, 0x03, 0x04});
        headerMap.put("class", new int[]{0xca, 0xfe, 0xba, 0xbe});

        try (FileInputStream ins = new FileInputStream(filename)) {
            int[] header = new int[4];
            for (int i = 0; i < header.length; ++i) {
                header[i] = ins.read();
            }

            for (String type : headerMap.keySet()) {
                int[] typeHeader = headerMap.get(type);
                if (Arrays.equals(header, typeHeader)) {
                    System.out.printf("Filename: %s\n", filename);
                    System.out.print("File Header(Hex): ");
                    System.out.println(Arrays.toString(header));
                    System.out.printf("File Type: %s\n", type);
                    break;
                }

            }
        }
    }
}
