import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

class CustomMethodInvocationException extends Exception {
    public CustomMethodInvocationException(String message) {
        super(message);
    }
}

public class MethodInvocationExample {
    public static void main(String[] args) {
        try {
            invokeMethod(new TestClass(), "testMethod", Arrays.asList(1.0, 1));
        } catch (CustomMethodInvocationException e) {
            System.out.println("Помилка виклику методу: " + e.getMessage());
        }
    }

    public static void invokeMethod(Object object, String methodName, List<Object> parameters) throws CustomMethodInvocationException {
        Class<?>[] parameterTypes = new Class<?>[parameters.size()];
        for (int i = 0; i < parameters.size(); i++) {
            parameterTypes[i] = getWrapperClass(parameters.get(i));
        }

        try {
            Method method = object.getClass().getMethod(methodName, parameterTypes);

            Object result = method.invoke(object, parameters.toArray());

            System.out.println("Результат виклику: " + result);
        } catch (NoSuchMethodException e) {
            throw new CustomMethodInvocationException("Метод з іменем " + methodName + " та переданими типами параметрів не знайдено");
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new CustomMethodInvocationException("Помилка виклику методу: " + e.getMessage());
        }
    }

    private static Class<?> getWrapperClass(Object object) {
        if (object instanceof Integer) {
            return int.class;
        } else if (object instanceof Double) {
            return double.class;
        } else if (object instanceof Float) {
            return float.class;
        } else if (object instanceof Long) {
            return long.class;
        } else if (object instanceof Short) {
            return short.class;
        } else if (object instanceof Byte) {
            return byte.class;
        } else if (object instanceof Character) {
            return char.class;
        } else if (object instanceof Boolean) {
            return boolean.class;
        } else {
            return object.getClass();
        }
    }
}

class TestClass {
    public double testMethod(double a) {
        System.out.println("TestClass [a=" + a + ", exp(-abs(a)*x)*sin(x)]");
        return Math.exp(-Math.abs(a)) * Math.sin(a);
    }

    public double testMethod(double a, int x) {
        System.out.println("TestClass [a=" + a + ", exp(-abs(a)*x)*sin(x)]");
        System.out.println("Типи: " + Arrays.asList(double.class, int.class) + ", значення: " + Arrays.asList(a, x));
        return Math.exp(-Math.abs(a) * x) * Math.sin(x);
    }
}
