package task_3;

import java.io.*;
import java.util.*;

public class Encryption2 {
    public static byte getByteFromBits(String bits){
        int b = 0;
        for (int i=0;i<bits.length(); i++){
            if (bits.charAt(i) == '1'){
                b+= Math.pow(2, bits.length() - 1 - i);
            }
        }
        return (byte) b;
    }

    public static String getContent(File file) throws IOException {
        StringBuffer stringBuffer = new StringBuffer();
        String content;
        FileInputStream fis = new FileInputStream(file);
        if (fis!= null){
            int i=-1;
            while((i=fis.read())!=-1){
                stringBuffer.append((char)i);
            }
            fis.close();
        }
        content = stringBuffer.toString();
        return content;
    }
    public static List<Integer> getSpacesFromFile(File file) throws IOException {
        String content = getContent(file);
        List<Integer> indices = new ArrayList<>();
        for (int i = 0;i<content.length(); i++){
            if (content.toCharArray()[i] == ' '){
                indices.add(i);
            }
        }
        return indices;
    }
    public static void encodeMessage(String message, File input, File output) throws IOException {
        List<Integer> spacesFromFile = getSpacesFromFile(input);
        BitSet bitSet = BitSet.valueOf(message.getBytes());
        FileOutputStream writer = new FileOutputStream(output);
        StringBuilder stringBuilder = new StringBuilder(getContent(input));
        for (int i = 0; i <= Math.min(bitSet.length(), spacesFromFile.size()); i++){
            if (bitSet.get(i)){
                stringBuilder.replace(spacesFromFile.get(i),spacesFromFile.get(i) + 1, "  ");
                for (int j= 0; j<spacesFromFile.size(); j++){
                    spacesFromFile.set(j, spacesFromFile.get(j) + 1);
                }
            }
        }
        writer.write(stringBuilder.toString().getBytes());
        writer.close();
    }
    public static String decodeMessage(File decodedFile, File output) throws IOException {
        String content = getContent(decodedFile);
        StringBuffer stringBuffer = new StringBuffer();
        for (int i=0;i<content.length() - 1;i++){
            if (content.charAt(i) == ' ' & content.charAt(i + 1) == ' ' ){
                stringBuffer.append(1);
                i++;
            }
            else if (content.charAt(i) == ' ' & content.charAt(i+1) != ' '){
                stringBuffer.append(0);
            }
            else {

            }

        }
        while (stringBuffer.indexOf("00000000") != -1 | stringBuffer.length() % 8 != 0){
            if (stringBuffer.indexOf("00000000")!= -1){
                stringBuffer.replace(stringBuffer.indexOf("00000000"), stringBuffer.indexOf("00000000") + 8, "");
            }
            if (stringBuffer.length() % 8 != 0){
                stringBuffer.replace(stringBuffer.length() - 1, stringBuffer.length(), "");
            }
        }
        System.out.println(stringBuffer);
        String[] split = stringBuffer.reverse().toString().split("(?<=\\G.{8})");
        byte[] bytes = new byte[split.length];
        for (int i=0; i<split.length;i++){
            bytes[i] = getByteFromBits(split[i]);
        }
        String finalMessage = new StringBuffer(new String(bytes)).reverse().toString();
        FileOutputStream writer = new FileOutputStream(output);
        writer.write(finalMessage.getBytes());
        writer.close();
        return finalMessage;
    }

    public static void main(String[] args) throws IOException {
        File input = new File("src/main/java/task_3/files/input.txt");
        File decodedFile = new File("src/main/java/task_3/files/decodedMessage.txt");
        System.out.println(input);
        Scanner in = new Scanner(System.in);
        System.out.print("Enter a message that you want to encode: ");
        String message = in.nextLine();
        encodeMessage(message, input, decodedFile);
        System.out.println("hidden message -> " + decodeMessage(decodedFile, new File("src/main/java/task_3/files/output.txt")));
    }
}
