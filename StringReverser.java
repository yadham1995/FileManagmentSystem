package problemsolving;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StringReverser {

    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        String str = bufferedReader.readLine();

        String output = reverseSubstrings(str);

        System.out.println("Reversed substring -> " + output);
    }

    private static String reverseSubstrings(String inputStr) {
        Stack<Integer> stack = new Stack<>();
        StringBuilder result = new StringBuilder(inputStr);

        for (int i = 0; i < inputStr.length(); i++) {
            char c = inputStr.charAt(i);
            if (c == '(') {
                stack.push(i);
            } else if (c == ')') {
                int start = stack.pop();
                reverseSubstring(result, start + 1, i - 1);
            }
        }
        return result.toString();
    }

    private static void reverseSubstring(StringBuilder inputStr, int start, int end) {
        while (start < end) {
            char temp = inputStr.charAt(start);
            inputStr.setCharAt(start, inputStr.charAt(end));
            inputStr.setCharAt(end, temp);
            start++;
            end--;
        }
    }
}
