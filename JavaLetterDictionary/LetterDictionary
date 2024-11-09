import java.io.*;
import java.util.Scanner;

public class LetterDictionary {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            String inputFileName = getFileName(scanner, "Введите имя входного файла (для выхода введите 'exit'): ");
            if (inputFileName.equalsIgnoreCase("exit")) {
                break;
            }

            File inputFile = new File(inputFileName);
            if (!inputFile.exists() || !inputFile.isFile()) {
                System.out.println("Файл не существует или это не файл. Попробуйте снова.");
                continue;
            }

            String outputFileName = getFileName(scanner, "Введите имя выходного файла (или введите 'exit' для выхода): ");
            if (outputFileName.equalsIgnoreCase("exit")) {
                break;
            }

            File outputFile = new File(outputFileName);
            if (!outputFile.exists() || !outputFile.isFile()) {
                System.out.println("Файл не существует или это не файл. Попробуйте снова.");
                continue;
            }

            int[] letterCount = new int[52];

            try (BufferedReader fileReader = new BufferedReader(new FileReader(inputFile))) {
                int ch;
                while ((ch = fileReader.read()) != -1) {
                    char letter = (char) ch;
                    if (letter >= 'A' && letter <= 'Z') {
                        letterCount[letter - 'A']++;
                    } else if (letter >= 'a' && letter <= 'z') {
                        letterCount[letter - 'a' + 26]++;
                    }
                }
            } catch (IOException e) {
                System.out.println("Ошибка при чтении файла: " + e.getMessage());
                continue;
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFileName))) {
                for (int i = 0; i < 26; i++) {
                    if (letterCount[i] > 0) {
                        writer.write((char) (i + 'A') + ": " + letterCount[i]);
                        writer.newLine();
                    }
                }
                for (int i = 26; i < 52; i++) {
                    if (letterCount[i] > 0) {
                        writer.write((char) (i - 26 + 'a') + ": " + letterCount[i]);
                        writer.newLine();
                    }
                }
                System.out.println("Результат успешно записан в файл " + outputFileName);
            } catch (IOException e) {
                System.out.println("Ошибка при записи в файл: " + e.getMessage());
            }
        }

        scanner.close();
        System.out.println("Программа завершена.");
    }


    private static String getFileName(Scanner scanner, String prompt) {
        String fileName;
        while (true) {
            System.out.print(prompt);
            fileName = scanner.nextLine();
            if (fileName.trim().isEmpty()) {
                System.out.println("Имя файла не может быть пустым.");
            } else {
                return fileName;
            }
        }
    }
}
