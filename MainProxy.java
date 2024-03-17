import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

interface MathFunction {
    double evaluate(double x);
}

public class MainProxy {
    public static void main(String[] args) {
        MathFunction f1 = MathFunctions.expSinFunction(2.5);
        MathFunction f2 = MathFunctions.squareFunction();

        MathFunction profilingProxyF1 = MathFunctions.createProfilingProxy(f1);
        MathFunction tracingProxyF1 = MathFunctions.createTracingProxy(f1);
        MathFunction profilingProxyF2 = MathFunctions.createProfilingProxy(f2);
        MathFunction tracingProxyF2 = MathFunctions.createTracingProxy(f2);

        System.out.println("F1: " + f1.evaluate(1.0));
        System.out.println("F2: " + f2.evaluate(1.0));
        System.out.println();

        System.out.println("Profiling Proxy:");
        profilingProxyF1.evaluate(1.0);
        profilingProxyF2.evaluate(1.0);
        System.out.println();

        System.out.println("Tracing Proxy:");
        tracingProxyF1.evaluate(1.0);
        tracingProxyF2.evaluate(1.0);
    }
}

class MathFunctions {
    static class ProfilingInvocationHandler implements InvocationHandler {
        private final MathFunction target;

        ProfilingInvocationHandler(MathFunction target) {
            this.target = target;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            long startTime = System.nanoTime();
            Object result = method.invoke(target, args);
            long endTime = System.nanoTime();
            System.out.println("[" + method.getName() + "] took " + (endTime - startTime) + " ns");
            return result;
        }
    }

    static class TracingInvocationHandler implements InvocationHandler {
        private final MathFunction target;

        TracingInvocationHandler(MathFunction target) {
            this.target = target;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            System.out.print("[" + method.getName() + "]");
            if (args != null && args.length > 0) {
                System.out.print("(");
                for (int i = 0; i < args.length; i++) {
                    System.out.print(args[i]);
                    if (i < args.length - 1) {
                        System.out.print(", ");
                    }
                }
                System.out.print(")");
            }
            System.out.println();
            return method.invoke(target, args);
        }
    }

    static MathFunction createProfilingProxy(MathFunction target) {
        return (MathFunction) Proxy.newProxyInstance(
                MathFunctions.class.getClassLoader(),
                new Class[]{MathFunction.class},
                new ProfilingInvocationHandler(target)
        );
    }

    static MathFunction createTracingProxy(MathFunction target) {
        return (MathFunction) Proxy.newProxyInstance(
                MathFunctions.class.getClassLoader(),
                new Class[]{MathFunction.class},
                new TracingInvocationHandler(target)
        );
    }

    static MathFunction expSinFunction(double a) {
        return x -> Math.exp(-Math.abs(a) * x) * Math.sin(x);
    }

    static MathFunction squareFunction() {
        return x -> x * x;
    }
}