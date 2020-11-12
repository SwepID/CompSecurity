package task_4;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Encryption3 {
    public static byte getByteFromBits(String bits){
        int b = 0;
        for (int i=0;i<bits.length(); i++){
            if (bits.charAt(i) == '1'){
                b+= Math.pow(2, bits.length() - 1 - i);
            }
        }
        return (byte) b;
    }
    public static List<Integer> getIndicesFromFile(File file, Map<Character, Character> alphabet) throws IOException {
        String content = getContent(file);
        List<Integer> indices = new ArrayList<>();
        char[] chars = content.toCharArray();
        for (int i = 0;i<content.length(); i++){
            if (alphabet.containsKey(chars[i]) | alphabet.containsValue(chars[i])){
                indices.add(i);
            }
        }
        return indices;
    }
    public static String getContent(File file) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        FileInputStream fis = new FileInputStream(file);
        BufferedReader in = new BufferedReader(new InputStreamReader(fis, "UTF8"));
        String str;
        while ((str = in.readLine()) != null) {
            stringBuilder.append(str + "\n");
        }
        in.close();
        return stringBuilder.toString();
    }
    public static void encodeMessage(String message, File input, File output, Map<Character,Character> alphabet) throws IOException {
        BitSet bitSet = BitSet.valueOf(message.getBytes());
        List<Integer> indices = getIndicesFromFile(input, alphabet);
        FileOutputStream writer = new FileOutputStream(output);
        StringBuffer stringBuffer = new StringBuffer(getContent(input));
        String s = stringBuffer.toString();
        for (int i=0; i< Math.min(bitSet.length(), stringBuffer.length() - 1); i++){
            if (alphabet.containsKey(s.charAt(indices.get(i))) & bitSet.get(i)){
                stringBuffer.replace(indices.get(i), indices.get(i) + 1, alphabet.get(s.charAt(indices.get(i))).toString());
            }
        }
        writer.write(stringBuffer.toString().getBytes());
        writer.close();
    }
    public static String decodeMessage(File decodedFile, File output, Map<Character, Character> alphabet) throws IOException {
        String content = getContent(decodedFile);
        StringBuffer stringBuffer = new StringBuffer();
        List<Integer> indices = getIndicesFromFile(decodedFile, alphabet);
        for (int i=0;i<Math.min(indices.size(), content.length());i++){
            if (alphabet.containsValue(content.charAt(indices.get(i)))){
                stringBuffer.append(1);
            }
            else if (alphabet.containsKey(content.charAt(indices.get(i)))){
                stringBuffer.append(0);
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
        File input = new File("src/main/java/task_4/files/input.txt");
        File decodedFile = new File("src/main/java/task_4/files/decodedMessage.txt");
        Map<Character,Character> alternativeAlphabet = new HashMap<Character, Character>();
        alternativeAlphabet.put('А', 'A');
        alternativeAlphabet.put('Е', 'E');
        alternativeAlphabet.put('К','K');
        alternativeAlphabet.put('О','O');
        alternativeAlphabet.put('Р','P');
        alternativeAlphabet.put('С','C');
        alternativeAlphabet.put('У','Y');
        alternativeAlphabet.put('Х','X');
        alternativeAlphabet.put('В','B');
        alternativeAlphabet.put('Н','H');
        alternativeAlphabet.put('М', 'M');
        alternativeAlphabet.put('Т','T');
        alternativeAlphabet.put('а', 'a');
        alternativeAlphabet.put('е', 'e');
        alternativeAlphabet.put('о','o');
        alternativeAlphabet.put('р','p');
        alternativeAlphabet.put('с','c');
        alternativeAlphabet.put('у','y');
        alternativeAlphabet.put('х','x');
        System.out.println(input);
        Scanner in = new Scanner(System.in);
        System.out.print("Enter a message that you want to encode: ");
        String message = in.nextLine();
        encodeMessage(message, input, decodedFile, alternativeAlphabet);
        System.out.println("hidden message -> " + decodeMessage(decodedFile, new File("src/main/java/task_4/files/output.txt"), alternativeAlphabet));
    }
}
