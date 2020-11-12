package task_1;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class Search {
    public static ArrayDeque<File> getAllFiles(String path){
        File directory = new File(path);
        ArrayDeque<File> collect = new ArrayDeque<>();
        collect.add(directory);
        Iterator<File> iterator = collect.iterator();

        while(iterator.hasNext()){
            File file = iterator.next();
            if (file.isDirectory()){
                collect.addAll(Arrays.asList(file.listFiles()));
                collect.remove(file);
                iterator = collect.iterator();
            }
        }
        return collect;

    }
    public static String getSignatureFromFile(StringBuffer content, int skip){
        return content.substring(skip, content.length());
    }
    public static StringBuffer getStringFromFile(File file) throws IOException {
        StringBuffer stringBuffer = new StringBuffer();
        FileInputStream fis = new FileInputStream(file);
        if (fis!= null){
            int i=-1;
            while((i=fis.read())!=-1){
                stringBuffer.append((char)i);
            }
            fis.close();
        }
        return stringBuffer;
    }
    public static List<String> searchFilesBySignature(ArrayDeque<File> files, String signature) throws IOException {
        List<String> list = new ArrayList<String>();
        for (File file : files) {
            if (file.isFile()) {
                StringBuffer stringBuffer = getStringFromFile(file);
                if (stringBuffer.lastIndexOf(signature)!= -1){
                    list.add(file.getAbsolutePath());
                }
            }

        }
        return list;
    }
    public static void main(String[] args) throws IOException {
        StringBuffer content = getStringFromFile(new File("C:\\Users\\bnkha\\Desktop\\file.txt"));
        String signature = getSignatureFromFile(content, 5);
        System.out.println(signature);
        ArrayDeque<File> files = getAllFiles("C:\\Users\\bnkha\\Desktop\\search");
        //files.forEach(System.out::println);
        List<String> searchedFilesBySignature = searchFilesBySignature(files, signature);
        for (String searchedFile : searchedFilesBySignature) {
            System.out.println(searchedFile);
        }
        /*for(File file : list){
            System.out.println(file);
        }*/
    }
}
