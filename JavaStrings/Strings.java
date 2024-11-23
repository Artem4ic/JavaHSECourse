import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.Period;

public class Strings {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean continueRunning = true;

        while (continueRunning) {
            try {
                System.out.print("Введите ФИО (Фамилия Имя Отчество): ");
                String fullName = scanner.nextLine();
                String[] nameParts = fullName.split(" ");

                if (nameParts.length != 3) {
                    throw new IllegalArgumentException("ФИО должно содержать три части: Фамилия, Имя, Отчество.");
                }

                String lastName = nameParts[0];
                String firstName = nameParts[1];
                String patronymic = nameParts[2];

                System.out.print("Введите дату рождения (дд.мм.гггг): ");
                String birthDateInput = scanner.nextLine();
                LocalDate birthDate = parseDate(birthDateInput);

                String gender = determineGender(patronymic);
                int age = calculateAge(birthDate);
                String initials = String.format("%s %s.%s.", lastName, firstName.charAt(0), patronymic.charAt(0));
                String ageString = formatAge(age);

                System.out.printf("Инициалы: %s%n", initials);
                System.out.printf("Пол: %s%n", gender);
                System.out.printf("Возраст: %s%n", ageString);

            } catch (Exception e) {
                System.err.println("Ошибка: " + e.getMessage());
            }

            System.out.print("Хотите продолжить? (да/нет): ");
            String response = scanner.nextLine();
            if (!response.equalsIgnoreCase("да")) {
                continueRunning = false;
            }
        }
        scanner.close();
    }

    private static LocalDate parseDate(String dateStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        try {
            return LocalDate.parse(dateStr, formatter);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Неверный формат даты. Используйте дд.мм.гггг.");
        }
    }

    private static String determineGender(String patronymic) {
        if (patronymic.endsWith("ович") || patronymic.endsWith("евич")) {
            return "мужчина";
        } else if (patronymic.endsWith("овна") || patronymic.endsWith("евна")) {
            return "женщина";
        } else {
            return "не удалось определить пол";
        }
    }

    private static int calculateAge(LocalDate birthDate) {
        LocalDate now = LocalDate.now();
        return Period.between(birthDate, now).getYears();
    }

    private static String formatAge(int age) {
        if (age % 10 == 1 && age % 100 != 11) {
            return age + " год";
        } else if ((age % 10 >= 2 && age % 10 <= 4) && (age % 100 < 10 || age % 100 >= 20)) {
            return age + " года";
        } else {
            return age + " лет";
        }
    }
}
