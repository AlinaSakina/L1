import java.lang.reflect.Array;

public class ArrayManipulation {

    public static Object createArray(Class<?> type, int size) {
        return Array.newInstance(type, size);
    }

    public static Object createMatrix(Class<?> type, int rows, int cols) {
        return Array.newInstance(type, rows, cols);
    }

    public static Object resizeArray(Object array, int newSize) {
        Class<?> type = array.getClass().getComponentType();
        Object newArray = Array.newInstance(type, newSize);
        int preserveLength = Math.min(Array.getLength(array), newSize);
        System.arraycopy(array, 0, newArray, 0, preserveLength);
        return newArray;
    }

    public static Object resizeMatrix(Object matrix, int newRows, int newCols) {
        Class<?> type = matrix.getClass().getComponentType().getComponentType();
        Object newMatrix = Array.newInstance(type, newRows, newCols);
        int preserveRows = Math.min(Array.getLength(matrix), newRows);
        for (int i = 0; i < preserveRows; i++) {
            Object oldRow = Array.get(matrix, i);
            Object newRow = resizeArray(oldRow, newCols);
            Array.set(newMatrix, i, newRow);
        }
        return newMatrix;
    }

    public static String arrayToString(Object array) {
        StringBuilder sb = new StringBuilder();
        sb.append(array.getClass().getComponentType().getName()).append("[").append(Array.getLength(array)).append("] = {");
        for (int i = 0; i < Array.getLength(array); i++) {
            if (i > 0) sb.append(", ");
            sb.append(Array.get(array, i));
        }
        sb.append("}");
        return sb.toString();
    }

    public static String matrixToString(Object matrix) {
        StringBuilder sb = new StringBuilder();
        Class<?> componentType = matrix.getClass().getComponentType().getComponentType();
        sb.append(componentType.getName()).append("[").append(Array.getLength(matrix)).append("][").append(Array.getLength(Array.get(matrix, 0))).append("] = {");
        for (int i = 0; i < Array.getLength(matrix); i++) {
            if (i > 0) sb.append(", ");
            sb.append(arrayToString(Array.get(matrix, i)));
        }
        sb.append("}");
        return sb.toString();
    }

    public static void main(String[] args) {
        int[] intArray = (int[]) createArray(int.class, 2);
        System.out.println(arrayToString(intArray));

        String[][] stringMatrix = (String[][]) createMatrix(String.class, 3, 5);
        System.out.println(matrixToString(stringMatrix));

        Double[][] doubleMatrix = (Double[][]) createMatrix(Double.class, 5, 5);
        System.out.println(matrixToString(doubleMatrix));

        int[][] intMatrix = { {0, 1, 2, 3, 4}, {10, 11, 12, 13, 14}, {20, 21, 22, 23, 24} };
        System.out.println(matrixToString(intMatrix));

        intMatrix = (int[][]) resizeMatrix(intMatrix, 4, 6);
        System.out.println(matrixToString(intMatrix));

        intMatrix = (int[][]) resizeMatrix(intMatrix, 3, 7);
        System.out.println(matrixToString(intMatrix));

        int[][] intMatrix2 = { {0, 1}, {10, 11} };
        System.out.println(matrixToString(intMatrix2));
    }
}