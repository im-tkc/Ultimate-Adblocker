package adblocker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

public class InputHelper {
    public static Set<String> getField(Set<String> input, int pos, String delimiter) {
        Set<String> adsDomainList = new TreeSet<String>();
        Iterator<String> setIterator = input.iterator();
        
        while (setIterator.hasNext()) {
            String line = setIterator.next();
            String selectedField = line.split(delimiter)[pos - 1];
            adsDomainList.add(selectedField);
        }
        
        return adsDomainList;
    }
    
    public static Set<String> prependPrefixOnAllLines(String prefix, Set<String> linesToPrepend) {
        Set<String> adsDomainList = new TreeSet<String>();
        linesToPrepend.stream().forEach((line) -> {
            adsDomainList.add(prefix + line);
        });
        
        return adsDomainList;
    }
    
    public static String eolConverter(InputStream inputStream) throws IOException {
        BufferedReader br = null;
		StringBuilder sb = new StringBuilder();
 
		String line;
        br = new BufferedReader(new InputStreamReader(inputStream));
        while ((line = br.readLine()) != null) {
            sb.append(line);
            sb.append(System.lineSeparator());
        }
        br.close();
        
		return sb.toString();
    }
}
