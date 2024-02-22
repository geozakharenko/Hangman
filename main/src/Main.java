import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class Main {
    private static Random random = new Random();
    private static Scanner scanner = new Scanner(System.in);
    private static int numberOfErrors = 0;
    private static int totalWords = 51301;
    private static int buffersize;
    private static boolean win;
    private static char[] playerAnswersBuffer = new char[33];
    private static String hiddenWord;


    public static void main(String[] args) {
        System.out.println("Всего слов в хранилище - 51301");
        gameStart();
    }

    public static void gameStart() {
        do {
            System.out.println("1. Начать новую игру");
            System.out.println("2. Закончить");

            if (makePlayerChoice() == '1') {
                restoreValues();
                gameLoop();
            } else {
                System.out.println("До встречи!");
                return;
            }
        } while (true);

    }

    public static char makePlayerChoice() {
        char playerChoice;
        do {
            playerChoice = scanner.next().charAt(0);
            if (playerChoice != '1' & playerChoice != '2')
                System.out.println("Введите ответ числом: 1 или 2");
            else return playerChoice;
        }
        while (true);
    }

    public static void restoreValues() {
        buffersize = 0;
        numberOfErrors = 0;
        Arrays.fill(playerAnswersBuffer, '\u0000');
    }

    public static void gameLoop() {
        generateWord();
        do {
            showPicture();
            showUsedLetters();
            showHiddenWord();
            makePlayerTurn();
        } while (checkGameStatus());

        if (win) {
            System.out.println("\nВы победили!");
        } else System.out.println("\nВы проиграли!");
        showPicture();
        System.out.println("Загаданное слово - " + hiddenWord);
    }

    public static void generateWord() {
        int RandomizedWordInt = random.nextInt(totalWords);
        takeWordFromFile(RandomizedWordInt);
    }

    public static void takeWordFromFile(int RandomizedWordInt) {
        int searchIndex = 0;
        try (BufferedReader br = new BufferedReader(
                new FileReader("main/src/Dictionary.txt"))) {
            do {
                hiddenWord = br.readLine();
                searchIndex++;
            } while (searchIndex != RandomizedWordInt);
        } catch (FileNotFoundException exception) {
            System.out.println("Ошибка при открытии файла");
        } catch (IOException exception) {
            System.out.println("Error");
        }
    }

    public static void showPicture() {
        switch (numberOfErrors) {
            case 0:
                System.out.println("_____\n|   |\n|\n|\n|\n|");
                break;
            case 1:
                System.out.println("_____\n|   |\n|   0\n|\n|\n|");
                break;
            case 2:
                System.out.println("_____\n|   |\n|   0\n|   |\n|\n|");
                break;
            case 3:
                System.out.println("_____\n|   |\n|  \\0\n|   |\n|\n|");
                break;
            case 4:
                System.out.println("_____\n|   |\n|  \\0/\n|   |\n|\n|");
                break;
            case 5:
                System.out.println("_____\n|   |\n|  \\0/\n|   |\n|  /\n|");
                break;
            case 6:
                System.out.println("_____\n|   |\n|  \\0/\n|   |\n|  / \\\n|");
                break;
        }
    }

    public static void showUsedLetters() {
        if (buffersize > 0) {
            System.out.print("\nИспользованные буквы: ");
            for (int i = 0; i < buffersize; i++) {
                System.out.print(playerAnswersBuffer[i] + " ");
            }
            System.out.println();
        }
    }

    public static void showHiddenWord() {
        for (int i = 0; i < hiddenWord.length(); i++) {
            int correctLetter = 0;
            if (buffersize == 0) {
                System.out.print("*");
            } else {
                for (int j = 0; j < buffersize; j++) {
                    if (correctLetter == 0 && hiddenWord.charAt(i) == playerAnswersBuffer[j]) {
                        System.out.print(hiddenWord.charAt(i));
                        correctLetter++;
                    }
                }
                if (correctLetter == 0) System.out.print("*");
            }
        }
    }

    public static void makePlayerTurn() {
        if (checkLetterInBuffer(takePlayerAnswer()))
            System.out.println("Такая буква есть");
        else {
            System.out.println("Нет такой буквы");
            numberOfErrors++;
        }
    }

    public static char takePlayerAnswer() {
        char answer;
        do {
            System.out.println("\nНазовите букву (а-я): ");
            answer = scanner.next().charAt(0);
        } while (((answer < 'а' | answer > 'я') & answer != 'ё') || checkRepeatLetters(answer));
        playerAnswersBuffer[buffersize++] = answer;
        return answer;
    }

    public static boolean checkRepeatLetters(char answer) {
        for (int letterIndex = 0; letterIndex < buffersize; letterIndex++) {
            if (answer == playerAnswersBuffer[letterIndex]) {
                System.out.print("Вы уже называли эту букву");
                return true;
            }
        }
        return false;
    }

    public static boolean checkLetterInBuffer(char answer) {
        for (int i = 0; i < hiddenWord.length(); i++) {
            for (int j = 0; j < buffersize; j++) {
                if (hiddenWord.charAt(i) == answer)
                    return true;
            }
        }
        return false;
    }

    public static boolean checkGameStatus() {
        if (numberOfErrors == 6) {
            win = false;
            return false;
        }
        int countCorrectAnswers = 0;
        for (int i = 0; i < hiddenWord.length(); i++) {
            for (int indexBuffer = 0; indexBuffer < buffersize; indexBuffer++) {
                if (hiddenWord.charAt(i) == playerAnswersBuffer[indexBuffer])
                    countCorrectAnswers++;
            }
        }
        if (countCorrectAnswers == hiddenWord.length()) {
            win = true;
            return false;
        }
        return true;
    }
}
