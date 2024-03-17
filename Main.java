import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class ReflectionHelper {
    public static void printObjectState(Object obj) {
        System.out.println("Стан об'єкту:");
        Class<?> clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                System.out.println(field.getType().getName() + " " + field.getName() + " = " + field.get(obj));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public static List<Method> getPublicMethodsNoParams(Object obj) {
        List<Method> publicMethodsNoParams = new ArrayList<>();
        Class<?> clazz = obj.getClass();
        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            if (method.getParameterCount() == 0 && !method.getName().equals("getClass")) {
                publicMethodsNoParams.add(method);
            }
        }
        return publicMethodsNoParams;
    }

    public static void printPublicMethodsNoParams(Object obj) {
        System.out.println("Список відкритих методів без параметрів:");
        List<Method> publicMethodsNoParams = getPublicMethodsNoParams(obj);
        int count = 1;
        for (Method method : publicMethodsNoParams) {
            System.out.println(count + "). " + method.toString());
            count++;
        }
    }

    public static void callMethod(Object obj, int methodIndex) {
        List<Method> publicMethodsNoParams = getPublicMethodsNoParams(obj);
        if (methodIndex <= 0 || methodIndex > publicMethodsNoParams.size()) {
            System.out.println("Невірний порядковий номер методу.");
            return;
        }
        Method selectedMethod = publicMethodsNoParams.get(methodIndex - 1);
        try {
            Object result = selectedMethod.invoke(obj);
            System.out.println("Результат виклику методу: " + result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class Check {
    private double x;
    private double y;

    public Check(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double Dist() {
        return Math.sqrt(x * x + y * y);
    }

    public void setRandomData() {
        x = Math.random() * 10;
        y = Math.random() * 10;
    }

    @Override
    public String toString() {
        return "Check{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    public void setData(double x, double y) {
        this.x = x;
        this.y = y;
    }
}

public class Main {
    public static void main(String[] args) {
        Check obj = new Check(3.0, 4.0);
        System.out.println("Створення об'єкту...");
        ReflectionHelper.printObjectState(obj);
        ReflectionHelper.printPublicMethodsNoParams(obj);

        Scanner scanner = new Scanner(System.in);
        System.out.print("Введіть порядковий номер методу [1, n]: ");
        int methodIndex = scanner.nextInt();
        ReflectionHelper.callMethod(obj, methodIndex);
    }
}