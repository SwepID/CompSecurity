package task_2;

import java.io.*;
import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;



public class Encryption {
//11000000
//
    public static byte getByteFromBits(String bits){
        int b = 0;
        for (int i=0;i<bits.length(); i++){
            if (bits.charAt(i) == '1'){
                b+= Math.pow(2, bits.length() - 1 - i);
            }
        }
        return (byte) b;
    }

    public static List<String> getContent(File file) throws IOException {
        StringBuffer stringBuffer = new StringBuffer();
        List<String> content;
        FileInputStream fis = new FileInputStream(file);
        if (fis!= null){
            int i=-1;
            while((i=fis.read())!=-1){
                stringBuffer.append((char)i);
            }
            fis.close();
        }
        content = Arrays.asList(stringBuffer.toString().split("\n"));
        return content;
    }
    public static void encodeMessage(String message, File file, List<String> content) throws IOException {

        byte[] bytes = message.getBytes();
        BitSet bitSet = BitSet.valueOf(bytes);
        FileOutputStream writer = new FileOutputStream(file);
        for (int i =0; i<= bitSet.length(); i++){
            if (content.size() > i){
                content.set(i, content.get(i).substring(0, content.get(i).length() - 1));
                if (bitSet.get(i)){
                    writer.write((content.get(i) + " \n").getBytes());
                }
                else {
                    writer.write((content.get(i) + "\n").getBytes());
                }
            }
            else {
                if (bitSet.get(i)){
                    writer.write((" \n").getBytes());
                }
                else {
                    writer.write(("\n").getBytes());
                }
            }

        }
        writer.close();
    }
    public static String decodeMessage(File file, File output) throws IOException {
        List<String> content = getContent(file);
        StringBuffer stringBuffer = new StringBuffer();
        boolean[] bits = new boolean[content.size()];
        for (int i=0;i<content.size();i++){
            if (content.get(i).length() > 0){
                if (content.get(i).charAt(content.get(i).length() - 1) == ' '){
                    stringBuffer.append(1);
                    bits[i] = true;
                }
                else {
                    stringBuffer.append(0);
                    bits[i] = false;
                }
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
        File input = new File("src/main/java/task_2/files/input.txt");
        File decodedFile = new File("src/main/java/task_2/files/decodedMessage.txt");
        System.out.println(input);
        List<String> content = getContent(input);
        Scanner in = new Scanner(System.in);
        System.out.print("Enter a message that you want to encode: ");
        String message = in.nextLine();
        encodeMessage(message, decodedFile, content);
        System.out.println("hidden message -> " + decodeMessage(decodedFile, new File("src/main/java/task_2/files/output.txt")));
    }
    //1011011
}
