package adblocker;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Adblocker {
    public static void main(String[] args) {
        File hostFile = new File("hosts");
        File adsHostsFileDir = new File("tempAdHostsFile");
        adsHostsFileDir.mkdir();
        
        try {
            Adblocker.downloadHostsFile(adsHostsFileDir.getPath());
            Adblocker.replaceHostsFile(hostFile);
            Adblocker.savingContentToHostsFile(adsHostsFileDir, hostFile);
            Adblocker.finalizing(adsHostsFileDir);
        } catch (IOException | URISyntaxException ex) {
            Logger.getLogger(Adblocker.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void replaceHostsFile(File hostFile) throws IOException, URISyntaxException {
        try (InputStream hostFileBakStream = ClassLoader.getSystemResourceAsStream("adblocker/hosts")) {
            System.out.println("Updating hosts file");
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

    public static void savingContentToHostsFile(File adsHostsFileDir, File hostFile) throws FileNotFoundException, IOException {
        Set<String> adsList = IOHelper.readFilesWithoutComments(adsHostsFileDir);
        adsList = InputHelper.getField(adsList, 2, "\\s+");
        adsList.remove("localhost");
        adsList.remove("localhost.localdomain");
        adsList = InputHelper.prependPrefixOnAllLines("127.0.0.1\t", adsList);

        Set<String> header = new LinkedHashSet<String>();
        header.add("#### List generated using Ultimate-Adblocker ####");
        header.add("####          Updated on: " + InputHelper.getDate("dd/MM/yyyy") + "         ####");
        
        IOHelper.appendTextToFile(hostFile, header);
        IOHelper.appendTextToFile(hostFile, adsList);
        
        header.clear();
        adsList.clear();
    }
    
    public static void finalizing(File adsHostsFileDir) throws IOException {
        System.out.println("\nCleaning up data");
        IOHelper.dirRecursivelyDelete(adsHostsFileDir);
        System.out.println("All done! Press Enter to exit");
        System.in.read();
    }
}
