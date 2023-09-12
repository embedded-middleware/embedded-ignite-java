package io.github.embedded.ignite.core;

public class AnnotationUtil {
    public static EmbeddedIgnitePorts findPorts(Class<?> testClass) {
        if (testClass == null) {
            return null;
        }

        EmbeddedIgnitePorts annotation = testClass.getAnnotation(EmbeddedIgnitePorts.class);
        if (annotation != null) {
            return annotation;
        }

        return findPorts(testClass.getSuperclass());
    }

    public static EmbeddedIgniteSqlEngine sqlEngine(Class<?> testClass) {
        if (testClass == null) {
            return null;
        }

        EmbeddedIgniteSqlEngine annotation = testClass.getAnnotation(EmbeddedIgniteSqlEngine.class);
        if (annotation != null) {
            return annotation;
        }

        return sqlEngine(testClass.getSuperclass());
    }
}
