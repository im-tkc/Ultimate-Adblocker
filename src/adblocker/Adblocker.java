package adblocker;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Adblocker {
    public static void main(String[] args) {
        File hostFile = Adblocker.getHostsFile();
        File adsHostsFileDir = new File("tempAdHostsFile");
        adsHostsFileDir.mkdir();
        
        try {
            Adblocker.downloadHostsFile(adsHostsFileDir.getPath());
            Adblocker.replaceHostsFile(hostFile);
        
            Set<String> adsList = IOHelper.readFilesWithoutComments(adsHostsFileDir);
            adsList = InputHelper.getField(adsList, 2, "\\s+");
            adsList.remove("localhost");
            adsList = InputHelper.prependPrefixOnAllLines("127.0.0.1\t", adsList);
            
            IOHelper.appendTextToFile(hostFile, adsList);
            adsList.clear();
            
            System.out.println("\nCleaning up data");
            IOHelper.dirRecursivelyDelete(adsHostsFileDir);
            System.out.println("All done! Press Enter to exit");
            System.in.read();
        } catch (IOException | URISyntaxException ex) {
            Logger.getLogger(Adblocker.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static File getHostsFile() {
        String programFilesPath = (System.getenv("ProgramFiles(x86)") != null) ? System.getenv("ProgramFiles(x86)") : System.getenv("ProgramFiles");
        String hostFileDir = programFilesPath + "/Acrylic DNS Proxy/";
        return new File(hostFileDir + "AcrylicHosts.txt");
    }
    
    public static void replaceHostsFile(File hostFile) throws IOException, URISyntaxException {
        try (InputStream hostFileBakStream = ClassLoader.getSystemResourceAsStream("adblocker/hosts")) {
            System.out.println("Replacing Acrylic DNS hosts file");
            IOHelper.overwriteFile(hostFileBakStream, hostFile);
        }
    }

    public static void downloadHostsFile(String savedDirectory) throws IOException {
        File hostsFileLinks = new File("hostFilesList.txt");
        Set<String> listOfUrls = IOHelper.readFileWithoutComments(hostsFileLinks);
        Iterator<String> iterateUrls = listOfUrls.iterator();
        
        while (iterateUrls.hasNext()) {
            String link = iterateUrls.next();
            
            String domainName = link.replaceAll(".*://", "");
            domainName = domainName.split(":|/")[0];

            System.out.println("Downloading files from " + domainName);
            String savedPath = savedDirectory + "/" + domainName + ".txt";
            Downloader.download(link, savedPath);
        }
    }
}
