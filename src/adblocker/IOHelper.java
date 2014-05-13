package adblocker;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class IOHelper {
    public static void dirRecursivelyDelete(File dir) {
        if (dir.isDirectory()) {
            for (File file : dir.listFiles()) {
                file.delete();
            }
            dir.delete();
        }
    }

    public static Set<String> readFilesWithoutComments(File adsFilesDirectory) throws FileNotFoundException {
        File[] adsFiles = adsFilesDirectory.listFiles();
        Set<String> adsList = new HashSet<String>();
        for (File file : adsFiles) {
            Set<String> tmpList = IOHelper.readFileWithoutComments(file);
            adsList.addAll(tmpList);
        }
        return adsList;
    }

    public static Set<String> readFileWithoutComments(File input) throws FileNotFoundException {
        Set<String> adsList;
        
        try (Scanner scanner = new Scanner(input)) {
            adsList = new HashSet<String>(10000);
            final String COMMENT_MARKER = "#";
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (line.startsWith(COMMENT_MARKER) || line.isEmpty()) {
                    continue;
                }
                line = line.replaceAll(COMMENT_MARKER + ".*$", "");
                adsList.add(line);
            }
        }
        return adsList;
    }
    
    public static void appendTextToFile(File fileToAppend, Set<String> linesToAppend) throws IOException {
        FileWriter writer = new FileWriter(fileToAppend, true);
        try (final BufferedWriter bufferWriter = new BufferedWriter(writer)) {
            String newline = System.getProperty("line.separator");
            for (String line : linesToAppend) {
                bufferWriter.write(newline);
                bufferWriter.write(line);
            }
        }
        
        writer.close();
    }
    
    public static void overwriteFile(InputStream inputStream, File fileToOverwrite) throws FileNotFoundException, IOException {
        try (FileOutputStream hostFileStream = new FileOutputStream(fileToOverwrite, false)) {
            String userContent = InputHelper.eolConverter(inputStream);
            hostFileStream.write(userContent.getBytes());
        }
    }
}
