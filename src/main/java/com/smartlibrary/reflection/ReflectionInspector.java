package com.smartlibrary.reflection;

import com.smartlibrary.annotation.CourseConcept;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * Utility class for inspecting Java classes dynamically using Java Reflection API.
 * Demonstrates Unit 2: Java Reflection API (Class, Field, Method, Constructor, Annotation inspection).
 */
@CourseConcept(unit = 2, concept = "Java Reflection API")
public class ReflectionInspector {

    public static void inspectClass(String className) {
        try {
            Class<?> clazz = Class.forName(className);
            inspectClass(clazz);
        } catch (ClassNotFoundException e) {
            System.err.println("❌ Class not found: " + className);
        }
    }

    public static void inspectClass(Class<?> clazz) {
        System.out.println("\n====================================================");
        System.out.println("            JAVA REFLECTION API INSPECTOR           ");
        System.out.println("====================================================");
        System.out.println("Class Name: " + clazz.getName());
        System.out.println("Simple Name: " + clazz.getSimpleName());
        System.out.println("Superclass: " + (clazz.getSuperclass() != null ? clazz.getSuperclass().getName() : "None"));
        System.out.println("Is Interface: " + clazz.isInterface());
        System.out.println("Is Abstract: " + Modifier.isAbstract(clazz.getModifiers()));

        // Inspect Interfaces
        System.out.println("\n--- Implemented Interfaces ---");
        Class<?>[] interfaces = clazz.getInterfaces();
        if (interfaces.length == 0) {
            System.out.println("   (None)");
        } else {
            for (Class<?> iface : interfaces) {
                System.out.println("   -> Interface: " + iface.getName());
            }
        }

        // Inspect Annotations
        System.out.println("\n--- Declared Annotations ---");
        Annotation[] annotations = clazz.getDeclaredAnnotations();
        if (annotations.length == 0) {
            System.out.println("   (None)");
        } else {
            for (Annotation ann : annotations) {
                System.out.println("   -> Annotation: @" + ann.annotationType().getSimpleName());
                if (ann instanceof CourseConcept cc) {
                    System.out.println("      [Unit " + cc.unit() + ": " + cc.concept() + " | " + cc.description() + "]");
                }
            }
        }

        // Inspect Fields
        System.out.println("\n--- Declared Fields ---");
        Field[] fields = clazz.getDeclaredFields();
        for (Field f : fields) {
            System.out.println("   -> " + Modifier.toString(f.getModifiers()) + " " + f.getType().getSimpleName() + " " + f.getName());
        }

        // Inspect Constructors
        System.out.println("\n--- Constructors ---");
        Constructor<?>[] constructors = clazz.getConstructors();
        for (Constructor<?> c : constructors) {
            System.out.print("   -> " + c.getName() + "(");
            Class<?>[] params = c.getParameterTypes();
            for (int i = 0; i < params.length; i++) {
                System.out.print(params[i].getSimpleName() + (i < params.length - 1 ? ", " : ""));
            }
            System.out.println(")");
        }

        // Inspect Methods
        System.out.println("\n--- Declared Methods (Sample) ---");
        Method[] methods = clazz.getDeclaredMethods();
        int count = 0;
        for (Method m : methods) {
            if (count++ >= 8) {
                System.out.println("   ... and " + (methods.length - 8) + " more methods.");
                break;
            }
            System.out.println("   -> " + Modifier.toString(m.getModifiers()) + " " + m.getReturnType().getSimpleName() + " " + m.getName() + "()");
        }

        System.out.println("====================================================\n");
    }
}
