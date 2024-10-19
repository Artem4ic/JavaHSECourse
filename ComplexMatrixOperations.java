import java.util.Scanner;

public class ComplexMatrixOperations {

    public static boolean askToContinue(Scanner scanner) {
        System.out.print("Хотите продолжить? (да/нет): ");
        String continueInput = scanner.next();
        return continueInput.equalsIgnoreCase("да");
    }

    public static Complex[][][] getMatrices(Scanner scanner) {
        System.out.print("Введите количество матриц (1 или более): ");
        int numMatrices = getPositiveInteger(scanner);
        Complex[][][] matrices = new Complex[numMatrices][][];
        for (int i = 0; i < numMatrices; i++) {
            matrices[i] = readMatrix(scanner, i + 1);
        }

        return matrices;
    }

    private static Complex[][] readMatrix(Scanner scanner, int numMatrices) {
        System.out.printf("Введите количество строк матрицы %d: ", numMatrices);
        int rows = scanner.nextInt();
        System.out.printf("Введите количество столбцов матрицы %d: ", numMatrices);
        int cols = scanner.nextInt();

        Complex[][] matrix = new Complex[rows][cols];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                System.out.printf("Введите элемент [%d][%d] матрицы %d(формат: реальная_часть мнимая_часть): ", r + 1, c + 1, numMatrices);
                double real = scanner.nextDouble();
                double imag = scanner.nextDouble();
                matrix[r][c] = new Complex(real, imag);
            }
        }
        return matrix;
    }

    public static String getOperation(Scanner scanner) {
        System.out.print("Введите операцию (сложение, вычитание, умножение, деление, транспонирование, детерминант): ");
        return scanner.next().trim().toLowerCase();
    }

    private static int getPositiveInteger(Scanner scanner) {
        while (true) {
            try {
                int number = Integer.parseInt(scanner.nextLine());
                if (number > 0) return number;
                System.out.println("Ошибка: введите положительное целое число.");
            } catch (NumberFormatException e) {
            }
        }
    }

    public static void addMatrices(Complex[][][] matrices) {
        int rows = matrices[0].length;
        int cols = matrices[0][0].length;

        if (matrices.length < 2) {
            System.out.println("Ошибка: Для сложения необходимо как минимум две матрицы.");
            return;
        }

        for (int i = 1; i < matrices.length; i++) {
            if (matrices[i].length != rows || matrices[i][0].length != cols) {
                System.out.println("Ошибка: Все матрицы должны иметь одинаковые размеры для сложения.");
                return;
            }
        }

        Complex[][] result = new Complex[rows][cols];

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                result[r][c] = matrices[0][r][c];
                for (int i = 1; i < matrices.length; i++) {
                    result[r][c] = result[r][c].add(matrices[i][r][c]);
                }
            }
        }

        System.out.println("Результат сложения:");
        printMatrix(result);
    }

    public static void subtractMatrices(Complex[][][] matrices) {
        int rows = matrices[0].length;
        int cols = matrices[0][0].length;

        if (matrices.length < 2) {
            System.out.println("Ошибка: Для вычитания необходимо как минимум две матрицы.");
            return;
        }

        for (int i = 1; i < matrices.length; i++) {
            if (matrices[i].length != rows || matrices[i][0].length != cols) {
                System.out.println("Ошибка: Все матрицы должны иметь одинаковые размеры для вычитания.");
                return;
            }
        }

        Complex[][] result = new Complex[rows][cols];

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                result[r][c] = matrices[0][r][c];
                for (int i = 1; i < matrices.length; i++) {
                    result[r][c] = result[r][c].subtract(matrices[i][r][c]);
                }
            }
        }

        System.out.println("Результат вычитания:");
        printMatrix(result);
    }

    public static void multiplyMatrices(Complex[][][] matrices) {
        int rowsA = matrices[0].length;
        int colsA = matrices[0][0].length;
        int colsB = matrices[1][0].length;

        if (matrices.length < 2) {
            System.out.println("Ошибка: Для умножения необходимо как минимум две матрицы.");
            return;
        }

        for (int i = 1; i < matrices.length; i++) {
            if (matrices[i].length != colsA) {
                System.out.println("Ошибка: Для умножения количесвто столбцов первой матрицы должно равняться количеству строк второй матрицы.");
                return;
            }
        }

        Complex[][] result = new Complex[rowsA][colsB];

        for (int r = 0; r < rowsA; r++) {
            for (int c = 0; c < colsB; c++) {
                result[r][c] = new Complex(0, 0);
                for (int k = 0; k < colsA; k++) {
                    result[r][c] = result[r][c].add(matrices[0][r][k].multiply(matrices[1][k][c]));
                }
            }
        }

        System.out.println("Результат умножения:");
        printMatrix(result);
    }

    private static Complex[][] multiplyMatrices2(Complex[][] matrix1, Complex[][] matrix2) {
        int rowsA = matrix1.length;
        int colsA = matrix1[0].length;
        int colsB = matrix2[0].length;

        Complex[][] result = new Complex[rowsA][colsB];

        for (int r = 0; r < rowsA; r++) {
            for (int c = 0; c < colsB; c++) {
                result[r][c] = new Complex(0, 0);
                for (int k = 0; k < colsA; k++) {
                    result[r][c] = result[r][c].add(matrix1[r][k].multiply(matrix2[k][c]));
                }
            }
        }
        return result;
    }

    public static void divideMatrices(Complex[][][] matrices) {
        if (matrices.length != 2) {
            throw new IllegalArgumentException("Деление возможно только между двумя матрицами.");
        }
        Complex[][] denominator = matrices[1];
        Complex[][] numerator = matrices[0];
        if (denominator.length != denominator[0].length || calculateDeterminant(denominator).getReal() == 0) {
            System.out.println("Ошибка: Невозможно выполнить деление на ненормируемую матрицу или на ноль.");
            return;
        }

        Complex[][] inverseDenominator = inverse(denominator);
        Complex[][] resultMatrix = multiplyMatrices2(numerator, inverseDenominator);

        System.out.println("Результат деления:");
        printMatrix(resultMatrix);
    }

    private static Complex[][] inverse(Complex[][] matrix) {
        int n = matrix.length;
        if (n != matrix[0].length) {
            throw new IllegalArgumentException("Матрица должна быть квадратной.");
        }

        // Создаем расширенную матрицу [A | I]
        Complex[][] augmented = new Complex[n][2 * n];
        for (int i = 0; i < n; i++) {
            System.arraycopy(matrix[i], 0, augmented[i], 0, n);
            for (int j = 0; j < n; j++) {
                augmented[i][j + n] = (i == j) ? new Complex(1, 0) : new Complex(0, 0);
            }
        }

        // Применяем метод Гаусса-Жордана
        for (int i = 0; i < n; i++) {
            // Нормализуем строку
            Complex pivot = augmented[i][i];
            if (pivot.modulusSquared() == 0) {
                throw new ArithmeticException("Матрица не имеет обратной.");
            }
            for (int j = 0; j < 2 * n; j++) {
                augmented[i][j] = augmented[i][j].divide(pivot);
            }

            // Обнуляем остальные строки
            for (int k = 0; k < n; k++) {
                if (k != i) {
                    Complex factor = augmented[k][i];
                    for (int j = 0; j < 2 * n; j++) {
                        augmented[k][j] = augmented[k][j].subtract(augmented[i][j].multiply(factor));
                    }
                }
            }
        }

        // Извлекаем обратную матрицу
        Complex[][] inverseMatrix = new Complex[n][n];
        for (int i = 0; i < n; i++) {
            System.arraycopy(augmented[i], n, inverseMatrix[i], 0, n);
        }

        return inverseMatrix;
    }

    public static void transposeMatrix(Complex[][][] matrices) {
        int rows = matrices[0].length;
        int cols = matrices[0][0].length;

        if (matrices.length != 1) {
            System.out.println("Ошибка: транспонировать можно только одну матрицу.");
            return;
        }

        Complex[][] transposed = new Complex[cols][rows];

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                transposed[c][r] = matrices[0][r][c];
            }
        }

        System.out.println("Результат транспонирования:");
        printMatrix(transposed);
    }

    public static Complex calculateDeterminant(Complex[][] matrix) {
        int n = matrix.length;
        if (n == 1) {
            return matrix[0][0];
        }
        if (n == 2) {
            return matrix[0][0].multiply(matrix[1][1]).subtract(matrix[0][1].multiply(matrix[1][0]));
        }

        Complex determinant = new Complex(0, 0);
        for (int c = 0; c < n; c++) {
            Complex[][] subMatrix = new Complex[n - 1][n - 1];
            for (int i = 1; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (j < c) {
                        subMatrix[i - 1][j] = matrix[i][j];
                    } else if (j > c) {
                        subMatrix[i - 1][j - 1] = matrix[i][j];
                    }
                }
            }
            Complex term = matrix[0][c].multiply(calculateDeterminant(subMatrix));
            if (c % 2 == 0) {
                determinant = determinant.add(term);
            } else {
                determinant = determinant.subtract(term);
            }
        }
        return determinant;
    }

    private static void printMatrix(Complex[][] matrix) {
        for (Complex[] row : matrix) {
            for (Complex value : row) {
                System.out.print(value + " ");
            }
            System.out.println();
        }
    }

}
