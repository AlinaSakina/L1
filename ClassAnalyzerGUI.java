import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.*;

public class ClassAnalyzerGUI extends JFrame {
    private JTextArea textArea;
    private JTextField inputField;

    public ClassAnalyzerGUI() {
        setTitle("Class Analyzer");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout());

        textArea = new JTextArea();
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);

        JLabel label = new JLabel("Enter full class name:");
        inputField = new JTextField(20);

        JButton analyzeButton = new JButton("Analyze");
        analyzeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                analyzeClass();
            }
        });

        JButton clearButton = new JButton("Clear");
        clearButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                clearTextArea();
            }
        });

        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        JPanel inputPanel = new JPanel(new FlowLayout());
        inputPanel.add(label);
        inputPanel.add(inputField);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(analyzeButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(exitButton);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(inputPanel, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        panel.add(centerPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        add(panel);
        setVisible(true);
    }

    private void analyzeClass() {
        String className = inputField.getText();
        try {
            String analysisResult = ClassAnalyzer.analyzeClass(className);
            textArea.setText(analysisResult);
        } catch (ClassNotFoundException ex) {
            textArea.setText("Class not found.");
        }
    }

    private void clearTextArea() {
        textArea.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new ClassAnalyzerGUI();
            }
        });
    }
}

class ClassAnalyzer {

    public static String analyzeClass(String className) throws ClassNotFoundException {
        Class<?> clazz = Class.forName(className);
        return analyzeClass(clazz);
    }

    public static String analyzeClass(Class<?> clazz) {
        StringBuilder result = new StringBuilder();

        // Package name
        Package pkg = clazz.getPackage();
        if (pkg != null) {
            result.append("Package: ").append(pkg.getName()).append("\n");
        }

        // Modifiers and class name
        int modifiers = clazz.getModifiers();
        result.append("Modifiers: ").append(Modifier.toString(modifiers)).append("\n");
        result.append("Class: ").append(clazz.getSimpleName()).append("\n");

        // Superclass
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
            result.delete(result.length() - 2, result.length());
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
}