import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.util.*;

public class ciphergui extends JFrame {
    private JComboBox<String> cipherSelector;
    private JTextArea inputArea, outputArea;
    private JButton encryptBtn, decryptBtn, loadFileBtn;
    private JLabel keyLabel;
    private JTextField keyField;
    
    private static final String[] CIPHERS = {
        "Caesar Cipher", 
        "Autokey Cipher",
        "Hill Cipher",
        "Monoalphabetic Cipher",
        "Playfair Cipher",
        "Rail Fence Cipher",
        "Vigenère Cipher",
        "Row Transposition Cipher"
    };
    
    public ciphergui() {
        setTitle("Classical Cipher Tool");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        
        cipherSelector = new JComboBox<>(CIPHERS);
        inputArea = new JTextArea(10, 40);
        outputArea = new JTextArea(10, 40);
        encryptBtn = new JButton("Encrypt");
        decryptBtn = new JButton("Decrypt");
        loadFileBtn = new JButton("Load from File");
        keyLabel = new JLabel("Key:");
        keyField = new JTextField(20);
        
        inputArea.setLineWrap(true);
        outputArea.setLineWrap(true);
        outputArea.setEditable(false);
        
        JScrollPane inputScroll = new JScrollPane(inputArea);
        JScrollPane outputScroll = new JScrollPane(outputArea);
        
        JPanel topPanel = new JPanel(new FlowLayout());
        topPanel.add(new JLabel("Select Cipher:"));
        topPanel.add(cipherSelector);
        topPanel.add(keyLabel);
        topPanel.add(keyField);
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(loadFileBtn);
        buttonPanel.add(encryptBtn);
        buttonPanel.add(decryptBtn);
        
        add(topPanel, BorderLayout.NORTH);
        add(inputScroll, BorderLayout.WEST);
        add(outputScroll, BorderLayout.EAST);
        add(buttonPanel, BorderLayout.SOUTH);
        
        loadFileBtn.addActionListener(e -> loadFile());
        encryptBtn.addActionListener(e -> encrypt());
        decryptBtn.addActionListener(e -> decrypt());
        
        cipherSelector.addActionListener(e -> updateKeyFieldLabel());
        
        updateKeyFieldLabel();
    }
    
    private void updateKeyFieldLabel() {
        String selectedCipher = (String) cipherSelector.getSelectedItem();
        if (selectedCipher.equals("Hill Cipher")) {
            keyLabel.setText("Matrix Key (e.g., 6 24 1, 13 16 10, 20 17 15):");
        } else if (selectedCipher.equals("Monoalphabetic Cipher")) {
            keyLabel.setText("Substitution Alphabet (26 letters):");
        } else if (selectedCipher.equals("Playfair Cipher")) {
            keyLabel.setText("Keyword:");
        } else if (selectedCipher.equals("Rail Fence Cipher")) {
            keyLabel.setText("Number of Rails:");
        } else {
            keyLabel.setText("Key:");
        }
    }
    
    private void loadFile() {
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showOpenDialog(this);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try {
                String content = new String(Files.readAllBytes(selectedFile.toPath()));
                inputArea.setText(content);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error reading file", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void encrypt() {
        String text = inputArea.getText();
        String key = keyField.getText();
        String cipher = (String) cipherSelector.getSelectedItem();
        
        if (text.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter text to encrypt", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            String result = "";
            switch (cipher) {
                case "Caesar Cipher":
                    result = caesarCipher(text, Integer.parseInt(key), true);
                    break;
                case "Autokey Cipher":
                    result = autokeyCipher(text, key, true);
                    break;
                case "Hill Cipher":
                    result = hillCipher(text, key, true);
                    break;
                case "Monoalphabetic Cipher":
                    result = monoalphabeticCipher(text, key, true);
                    break;
                case "Playfair Cipher":
                    result = playfairCipher(text, key, true);
                    break;
                case "Rail Fence Cipher":
                    result = railFenceCipher(text, Integer.parseInt(key), true);
                    break;
                case "Vigenère Cipher":
                    result = vigenereCipher(text, key, true);
                    break;
                case "Row Transposition Cipher":
                    result = rowTranspositionCipher(text, key, true);
                    break;
                default:
                    result = "Unsupported cipher";
            }
            outputArea.setText(result);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error during encryption: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void decrypt() {
        String text = inputArea.getText();
        String key = keyField.getText();
        String cipher = (String) cipherSelector.getSelectedItem();
        
        if (text.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter text to decrypt", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            String result = "";
            switch (cipher) {
                case "Caesar Cipher":
                    result = caesarCipher(text, Integer.parseInt(key), false);
                    break;
                case "Autokey Cipher":
                    result = autokeyCipher(text, key, false);
                    break;
                case "Hill Cipher":
                    result = hillCipher(text, key, false);
                    break;
                case "Monoalphabetic Cipher":
                    result = monoalphabeticCipher(text, key, false);
                    break;
                case "Playfair Cipher":
                    result = playfairCipher(text, key, false);
                    break;
                case "Rail Fence Cipher":
                    result = railFenceCipher(text, Integer.parseInt(key), false);
                    break;
                case "Vigenère Cipher":
                    result = vigenereCipher(text, key, false);
                    break;
                case "Row Transposition Cipher":
                    result = rowTranspositionCipher(text, key, false);
                    break;
                default:
                    result = "Unsupported cipher";
            }
            outputArea.setText(result);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error during decryption: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private String caesarCipher(String text, int shift, boolean encrypt) {
        if (!encrypt) shift = -shift;
        
        StringBuilder result = new StringBuilder();
        for (char c : text.toCharArray()) {
            if (Character.isLetter(c)) {
                char base = Character.isLowerCase(c) ? 'a' : 'A';
                c = (char) ((c - base + shift + 26) % 26 + base);
            }
            result.append(c);
        }
        return result.toString();
    }
    
    private String autokeyCipher(String text, String key, boolean encrypt) {
        StringBuilder result = new StringBuilder();
        String autoKey = key + text;
        
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (Character.isLetter(c)) {
                char base = Character.isLowerCase(c) ? 'a' : 'A';
                char k = autoKey.charAt(i % autoKey.length());
                int keyShift = Character.toLowerCase(k) - 'a';
                
                if (!encrypt) keyShift = -keyShift;
                
                c = (char) ((c - base + keyShift + 26) % 26 + base);
            }
            result.append(c);
        }
        return result.toString();
    }
    
    private String hillCipher(String text, String key, boolean encrypt) {
        String[] rows = key.split(",");
        int n = rows.length;
        int[][] matrix = new int[n][n];
        
        try {
            for (int i = 0; i < n; i++) {
                String[] values = rows[i].trim().split("\\s+");
                for (int j = 0; j < n; j++) {
                    matrix[i][j] = Integer.parseInt(values[j]);
                }
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid matrix format");
        }
        
        // Pad text if necessary
        while (text.length() % n != 0) {
            text += 'x';
        }
        
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < text.length(); i += n) {
            int[] block = new int[n];
            for (int j = 0; j < n; j++) {
                char c = text.charAt(i + j);
                if (Character.isLetter(c)) {
                    block[j] = Character.toLowerCase(c) - 'a';
                } else {
                    block[j] = 0; 
                }
            }
            
            int[] transformed = new int[n];
            for (int row = 0; row < n; row++) {
                for (int col = 0; col < n; col++) {
                    transformed[row] += matrix[row][col] * block[col];
                }
                transformed[row] = (transformed[row] % 26 + 26) % 26;
            }
            
            for (int num : transformed) {
                result.append((char) (num + 'a'));
            }
        }
        return result.toString();
    }
    
    private String monoalphabeticCipher(String text, String key, boolean encrypt) {
        if (key.length() != 26) {
            throw new IllegalArgumentException("Substitution alphabet must be 26 letters");
        }
        key = key.toLowerCase();
        
        StringBuilder result = new StringBuilder();
        for (char c : text.toCharArray()) {
            if (Character.isLetter(c)) {
                boolean isUpper = Character.isUpperCase(c);
                int index = Character.toLowerCase(c) - 'a';
                
                if (index >= 0 && index < 26) {
                    char newChar = encrypt ? key.charAt(index) : (char) ('a' + key.indexOf(Character.toLowerCase(c)));
                    result.append(isUpper ? Character.toUpperCase(newChar) : newChar);
                } else {
                    result.append(c);
                }
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }
    
    private String playfairCipher(String text, String keyword, boolean encrypt) {
        keyword = keyword.toLowerCase().replaceAll("[^a-z]", "") + "abcdefghijklmnopqrstuvwxyz";
        keyword = keyword.replace("j", "i");
        
        char[][] square = new char[5][5];
        Set<Character> used = new HashSet<>();
        int index = 0;
        
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                while (index < keyword.length() && used.contains(keyword.charAt(index))) {
                    index++;
                }
                if (index < keyword.length()) {
                    square[i][j] = keyword.charAt(index);
                    used.add(keyword.charAt(index));
                } else {
                    square[i][j] = ' ';
                }
            }
        }

        String prepared = text.toLowerCase().replaceAll("[^a-z]", "").replace("j", "i");
        StringBuilder pairs = new StringBuilder();
        for (int i = 0; i < prepared.length(); ) {
            if (i + 1 >= prepared.length()) {
                pairs.append(prepared.charAt(i)).append('x');
                break;
            }
            char a = prepared.charAt(i);
            char b = prepared.charAt(i + 1);
            if (a == b) {
                pairs.append(a).append('x');
                i++;
            } else {
                pairs.append(a).append(b);
                i += 2;
            }
        }
        
        int direction = encrypt ? 1 : -1;
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < pairs.length(); i += 2) {
            char a = pairs.charAt(i);
            char b = pairs.charAt(i + 1);
            
            int[] posA = findPosition(square, a);
            int[] posB = findPosition(square, b);
            
            char newA, newB;
            
            if (posA[0] == posB[0]) { 
                newA = square[posA[0]][(posA[1] + direction + 5) % 5];
                newB = square[posB[0]][(posB[1] + direction + 5) % 5];
            } else if (posA[1] == posB[1]) { 
                newA = square[(posA[0] + direction + 5) % 5][posA[1]];
                newB = square[(posB[0] + direction + 5) % 5][posB[1]];
            } else { 
                newA = square[posA[0]][posB[1]];
                newB = square[posB[0]][posA[1]];
            }
            
            result.append(newA).append(newB);
        }
        
        return result.toString();
    }
    
    private int[] findPosition(char[][] square, char c) {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (square[i][j] == c) {
                    return new int[]{i, j};
                }
            }
        }
        return new int[]{0, 0}; 
    }
    
    private String railFenceCipher(String text, int rails, boolean encrypt) {
        if (rails < 2) {
            throw new IllegalArgumentException("Number of rails must be at least 2");
        }
        
        if (!encrypt) {
            return decryptRailFence(text, rails);
        }
        
        StringBuilder[] railStrings = new StringBuilder[rails];
        for (int i = 0; i < rails; i++) {
            railStrings[i] = new StringBuilder();
        }
        
        int rail = 0;
        int direction = 1;
        
        for (char c : text.toCharArray()) {
            railStrings[rail].append(c);
            rail += direction;
            if (rail == rails - 1 || rail == 0) {
                direction *= -1;
            }
        }
        
        StringBuilder result = new StringBuilder();
        for (StringBuilder sb : railStrings) {
            result.append(sb);
        }
        return result.toString();
    }
    
    private String decryptRailFence(String text, int rails) {
        int[] railLengths = new int[rails];
        int rail = 0;
        int direction = 1;

        for (int i = 0; i < text.length(); i++) {
            railLengths[rail]++;
            rail += direction;
            if (rail == rails - 1 || rail == 0) {
                direction *= -1;
            }
        }

        int index = 0;
        StringBuilder[] railStrings = new StringBuilder[rails];
        for (int i = 0; i < rails; i++) {
            railStrings[i] = new StringBuilder(text.substring(index, index + railLengths[i]));
            index += railLengths[i];
        }
  
        StringBuilder result = new StringBuilder();
        rail = 0;
        direction = 1;
        int[] railIndices = new int[rails];
        
        for (int i = 0; i < text.length(); i++) {
            result.append(railStrings[rail].charAt(railIndices[rail]++));
            rail += direction;
            if (rail == rails - 1 || rail == 0) {
                direction *= -1;
            }
        }
        
        return result.toString();
    }
    
    private String vigenereCipher(String text, String key, boolean encrypt) {
        key = key.toLowerCase().replaceAll("[^a-z]", "");
        if (key.isEmpty()) {
            throw new IllegalArgumentException("Key must contain at least one letter");
        }
        
        StringBuilder result = new StringBuilder();
        int keyIndex = 0;
        
        for (char c : text.toCharArray()) {
            if (Character.isLetter(c)) {
                char base = Character.isLowerCase(c) ? 'a' : 'A';
                int shift = key.charAt(keyIndex % key.length()) - 'a';
                if (!encrypt) shift = -shift;
                
                c = (char) ((c - base + shift + 26) % 26 + base);
                keyIndex++;
            }
            result.append(c);
        }
        return result.toString();
    }
    
    private String rowTranspositionCipher(String text, String key, boolean encrypt) {
        String cleaned = text.toLowerCase().replaceAll("[^a-z]", "");
        int keyLength = key.length();
        int rows = (int) Math.ceil((double) cleaned.length() / keyLength);

        char[] keyChars = key.toLowerCase().toCharArray();
        Integer[] order = new Integer[keyLength];
        for (int i = 0; i < keyLength; i++) {
            order[i] = i;
        }

        Arrays.sort(order, (a, b) -> Character.compare(keyChars[a], keyChars[b]));
        
        if (encrypt) {
            while (cleaned.length() < rows * keyLength) {
                cleaned += 'x';
            }

            char[][] grid = new char[rows][keyLength];
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < keyLength; j++) {
                    grid[i][j] = cleaned.charAt(i * keyLength + j);
                }
            }
        
            StringBuilder result = new StringBuilder();
            for (int col : order) {
                for (int row = 0; row < rows; row++) {
                    result.append(grid[row][col]);
                }
            }
            return result.toString();
        } else {
            if (cleaned.length() % rows != 0) {
                throw new IllegalArgumentException("Ciphertext length must be multiple of key length");
            }
            
            int cols = keyLength;
            int actualRows = cleaned.length() / cols;

            char[][] grid = new char[actualRows][cols];

            int[] inverseOrder = new int[cols];
            for (int i = 0; i < cols; i++) {
                inverseOrder[order[i]] = i;
            }
         
            int index = 0;
            for (int originalCol = 0; originalCol < cols; originalCol++) {
                for (int row = 0; row < actualRows; row++) {
                    grid[row][originalCol] = cleaned.charAt(index++);
                }
            }

            StringBuilder result = new StringBuilder();
            for (int row = 0; row < actualRows; row++) {
                for (int col = 0; col < cols; col++) {
                    result.append(grid[row][col]);
                }
            }
            return result.toString().replaceAll("x+$", ""); 
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ciphergui gui = new ciphergui();
            gui.setVisible(true);
        });
    }
}