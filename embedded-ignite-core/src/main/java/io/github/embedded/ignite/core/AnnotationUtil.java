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
}
