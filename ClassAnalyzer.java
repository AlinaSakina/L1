import java.lang.reflect.*;

import java.util.Scanner;

public class ClassAnalyzer {

    public static String analyzeClass(String className) throws ClassNotFoundException {
        Class<?> clazz = Class.forName(className);
        return analyzeClass(clazz);
    }

    public static String analyzeClass(Class<?> clazz) {
        StringBuilder result = new StringBuilder();

        Package pkg = clazz.getPackage();
        if (pkg != null) {
            result.append("Package: ").append(pkg.getName()).append("\n");
        }

        int modifiers = clazz.getModifiers();
        result.append("Modifiers: ").append(Modifier.toString(modifiers)).append("\n");
        result.append("Class: ").append(clazz.getSimpleName()).append("\n");

        Class<?> superclass = clazz.getSuperclass();
        if (superclass != null) {
            result.append("Superclass: ").append(superclass.getSimpleName()).append("\n");
        }

        Class<?>[] interfaces = clazz.getInterfaces();
        if (interfaces.length > 0) {
            result.append("Implemented Interfaces: ");
            for (Class<?> interf : interfaces) {
                result.append(interf.getSimpleName()).append(", ");
            }
            result.delete(result.length() - 2, result.length()); // Remove trailing comma and space
            result.append("\n");
        }

        Field[] fields = clazz.getDeclaredFields();
        if (fields.length > 0) {
            result.append("Fields:\n");
            for (Field field : fields) {
                result.append("  ").append(Modifier.toString(field.getModifiers())).append(" ")
                        .append(field.getType().getSimpleName()).append(" ").append(field.getName()).append("\n");
            }
        }

        Constructor<?>[] constructors = clazz.getDeclaredConstructors();
        if (constructors.length > 0) {
            result.append("Constructors:\n");
            for (Constructor<?> constructor : constructors) {
                result.append("  ").append(Modifier.toString(constructor.getModifiers())).append(" ")
                        .append(clazz.getSimpleName()).append("(");
                Class<?>[] paramTypes = constructor.getParameterTypes();
                for (int i = 0; i < paramTypes.length; i++) {
                    result.append(paramTypes[i].getSimpleName());
                    if (i < paramTypes.length - 1) {
                        result.append(", ");
                    }
                }
                result.append(")\n");
            }
        }

        Method[] methods = clazz.getDeclaredMethods();
        if (methods.length > 0) {
            result.append("Methods:\n");
            for (Method method : methods) {
                result.append("  ").append(Modifier.toString(method.getModifiers())).append(" ")
                        .append(method.getReturnType().getSimpleName()).append(" ")
                        .append(method.getName()).append("(");
                Class<?>[] paramTypes = method.getParameterTypes();
                for (int i = 0; i < paramTypes.length; i++) {
                    result.append(paramTypes[i].getSimpleName());
                    if (i < paramTypes.length - 1) {
                        result.append(", ");
                    }
                }
                result.append(")\n");
            }
        }

        return result.toString();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter class name: ");
        String className = scanner.nextLine();
        try {
            System.out.println(analyzeClass(className));
        } catch (ClassNotFoundException e) {
            System.out.println("Class not found.");
        }
        scanner.close();
    }
}