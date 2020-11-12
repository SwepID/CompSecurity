package task_5;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Vigenere {
    public static String readFile(String path) throws IOException {
        return new String(Files.readAllBytes(Path.of(path)));
    }
    public static <T, E> T getKeyByValue(Map<T, E> map, E value) {
        for (Map.Entry<T, E> entry : map.entrySet()) {
            if (Objects.equals(value, entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }

    public static String encodeMessage(Map<Character, Map<Character, Character>> matrix, String message, String tmp){
        StringBuffer result = new StringBuffer();
        for (int i = 0; i< message.length(); i++){
            result.append(matrix.get(message.charAt(i)).get(tmp.charAt(i)));
        }

        return result.toString();
    }
    public static String decodeMessage(Map<Character, Map<Character, Character>> matrix, String encodedMessage, String key){
        StringBuffer result = new StringBuffer();
        StringBuffer tmp = new StringBuffer();
        for (int i=0;i< encodedMessage.length(); i++){
            tmp.append(key.charAt(i % key.length()));
        }
        for (int i=0; i<encodedMessage.length(); i++){
            result.append(getKeyByValue(matrix.get(tmp.charAt(i)), encodedMessage.charAt(i)));
        }
        return result.toString();
    }
    public static void main(String[] args) throws IOException {
        Map<Character, Map<Character, Character>> matrix = new HashMap<>();
        byte skip = 0;
        for (char i = 'а'; i <= 'я'; i++){
            Map<Character, Character> map = new HashMap<>();
            for (char j = 'а'; j<= 'я'; j++){
                if ((char) (j + skip) <= 'я'){
                    map.put(j, (char) (j + skip));
                }
                else{
                    map.put(j, (char)(48 + (j + skip) - 80));
                }
            }
            skip++;
            matrix.put(i, map);
        }

        //System.out.println(matrix);

        //System.out.println(matrix);
        FileWriter fileWriter = null;
        String key = readFile("src/main/java/task_5/files/key.txt");
        //Encoding
        System.out.print("Введите сообщение для кодировки: ");
        String message = new Scanner(System.in).nextLine();
        StringBuffer tmp = new StringBuffer();
        for (int i=0;i< message.length(); i++){
            tmp.append(key.charAt(i % key.length()));
        }
        fileWriter = new FileWriter("src/main/java/task_5/files/encodedMessage.txt");
        fileWriter.write(encodeMessage(matrix, message, tmp.toString()));
        fileWriter.close();
        //Decoding
        String encodedMessage = readFile("src/main/java/task_5/files/encodedMessage.txt");
        fileWriter = new FileWriter("src/main/java/task_5/files/output.txt");
        fileWriter.write(decodeMessage(matrix, encodedMessage, key));
        fileWriter.close();

    }
}
