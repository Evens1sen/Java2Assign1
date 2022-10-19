package practice.lab6;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ReadZip {

    public static void main(String[] args) throws IOException {
        String fileName = "/Users/mhy/Code/javaworkspace/CS209/lab/src/main/java/practice/lab6/rt.jar";

        try (FileInputStream fis = new FileInputStream(fileName);
             BufferedInputStream bis = new BufferedInputStream(fis);
             ZipInputStream zis = new ZipInputStream(bis)) {

            ZipEntry ze;
            int cnt = 0;
            List<String> filenames = new ArrayList<>();
            while ((ze = zis.getNextEntry()) != null) {
                if (ze.getName().startsWith("java/io") || ze.getName().startsWith("java/nio")){
                    cnt++;
                    filenames.add(ze.getName());
                }
            }
            System.out.printf("# of .java files in java.io/java.nio: %d\n", cnt);
            filenames.forEach(System.out::println);
        }
    }
}
