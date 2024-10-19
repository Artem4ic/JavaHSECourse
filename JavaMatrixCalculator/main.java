import java.util.Scanner;

public class main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean continueRunning = true;

        while (continueRunning) {
            Complex[][][] matrices = ComplexMatrixOperations.getMatrices(scanner);
            String operation = ComplexMatrixOperations.getOperation(scanner);

            switch (operation) {
                case "сложение":
                    ComplexMatrixOperations.addMatrices(matrices);
                    break;
                case "вычитание":
                    ComplexMatrixOperations.subtractMatrices(matrices);
                    break;

                case "умножение":
                    ComplexMatrixOperations.multiplyMatrices(matrices);
                    break;

                case "деление":
                    ComplexMatrixOperations.divideMatrices(matrices);
                    break;

                case "транспонирование":
                    ComplexMatrixOperations.transposeMatrix(matrices);
                    break;

                case "детерминант":
                    if (matrices.length != 1) {
                        System.out.println("Ошибка: детерминант можно вычислить только для одной матрицы.");
                    } else {
                        Complex determinant = ComplexMatrixOperations.calculateDeterminant(matrices[0]);
                        System.out.println("Детерминант: " + determinant);

                    }
                    break;

                default:
                    System.out.println("Ошибка: неизвестная операция.");
            }

            continueRunning = ComplexMatrixOperations.askToContinue(scanner);
        }
        scanner.close();
    }

}