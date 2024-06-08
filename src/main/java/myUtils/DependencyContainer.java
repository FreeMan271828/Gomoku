package myUtils;

import java.util.HashMap;
import java.util.Map;

public class DependencyContainer {
    private static final Map<Class<?>, Object> instances = new HashMap<>();

    public static void register(Class<?> interfaceClass, Object instance) {
        instances.put(interfaceClass, instance);
    }

    public static <T> T get(Class<T> interfaceClass) {
        return (T) instances.get(interfaceClass);
    }
}
