import java.util.Scanner;
import java.util.Map;
import java.util.HashMap;
import java.util.regex.Pattern;

public class Main {

    public static String calc(String input) {
        String[] calculator = input.split(" ");
        if (calculator.length != 3) {
            throw new IllegalArgumentException("Формат ввода неверен");
        }

        String operand1 = calculator[0];
        String operand2 = calculator[2];
        String symbol = calculator[1];

        boolean isArabic = Pattern.matches("^\\d+$", operand1) && Pattern.matches("^\\d+$", operand2);
        boolean isRoman = Pattern.matches("^(I|II|III|IV|V|VI|VII|VIII|IX|X)$", operand1) &&
                Pattern.matches("^(I|II|III|IV|V|VI|VII|VIII|IX|X)$", operand2);

        if (!(isArabic || isRoman)) {
            throw new IllegalArgumentException("Ошибка");
        }

        int num1 = isRoman ? romanToArabic(operand1) : Integer.parseInt(operand1);
        int num2 = isRoman ? romanToArabic(operand2) : Integer.parseInt(operand2);

        if ((num1 < 1 || num1 > 10) || (num2 < 1 || num2 > 10)) {
            throw new IllegalArgumentException("Числа вне допустимого диапазона (1-10)");
        }

        int result;
        if (symbol.equals("+")) {
            result = num1 + num2;
        } else if (symbol.equals("-")) {
            result = num1 - num2;
            if (isRoman && result < 1) {
                throw new IllegalArgumentException("Результат меньше единицы");
            }
        } else if (symbol.equals("*")) {
            result = num1 * num2;
        } else if (symbol.equals("/")) {
            if (num2 == 0) {
                throw new ArithmeticException("Деление на ноль");
            }
            result = num1 / num2;
        } else {
            throw new IllegalArgumentException("Неподдерживаемая операция");
        }

        return isRoman ? arabicToRoman(result) : Integer.toString(result);
    }

    public static int romanToArabic(String roman) {
        Map<Character, Integer> romanNumerals = new HashMap<>();
        romanNumerals.put('I', 1);
        romanNumerals.put('V', 5);
        romanNumerals.put('X', 10);
        romanNumerals.put('L', 50);
        romanNumerals.put('C', 100);
        romanNumerals.put('D', 500);
        romanNumerals.put('M', 1000);

        int result = 0;
        int prevValue = 0;

        for (int i = roman.length() - 1; i >= 0; i--) {
            int value = romanNumerals.get(roman.charAt(i));

            if (value < prevValue) {
                result -= value;
            } else {
                result += value;
            }

            prevValue = value;
        }

        return result;
    }

    public static String arabicToRoman(int number) {
        if (number < 1 || number > 3999) {
            throw new IllegalArgumentException("Число не входит в допустимый диапазон (1-3999)");
        }

        String[] thousands = {"", "M", "MM", "MMM"};
        String[] hundreds = {"", "C", "CC", "CCC", "CD", "D", "DC", "DCC", "DCCC", "CM"};
        String[] tens = {"", "X", "XX", "XXX", "XL", "L", "LX", "LXX", "LXXX", "XC"};
        String[] ones = {"", "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX"};

        return thousands[number / 1000] + hundreds[(number % 1000) / 100] + tens[(number % 100) / 10] + ones[number % 10];
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String condition = scanner.nextLine();
        try {
            String result = calc(condition);
            System.out.println("Результат: " + result);
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: " + e.getMessage());
        } catch (ArithmeticException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }
}
