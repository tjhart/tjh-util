package com.tjh.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class Classes {

    /**
     * Returns all classes and interfaces that <code>aClass</code> and all of it's superclasses implements or inherits
     *
     * @param aClass The class to inspect
     * @return A set of classes
     */
    public static Set<Class> assignableTo(final Class aClass) {
        final Set<Class> classes = new HashSet<Class>();
        final Block<Class, Set<Class>> block = new Block<Class, Set<Class>>() {
            public Set<Class> invoke(final Class aClass) {
                classes.add(aClass);
                return classes;
            }
        };
        walkClassHierarchy(aClass, block);
        walkInterfaceHierarchy(aClass, block);
        return classes;
    }

    /**
     * Returns a set of the most specific classes (including interfaces) that all of the members of <code>objects</code>
     * have in common.
     * <p/>
     * See {@link #specify(java.util.Collection)} for a discussion on 'most specific'
     *
     * @param objects the objects to inspect
     * @return A set of classes common to all members of <code>objects</code>
     */
    public static Set<Class> commonBaseClasses(final Collection<Object> objects) {
        final Set<Class> result = new HashSet<Class>();
        if (objects.size() < 2) {
            if (objects.size() == 1) {
                result.add(objects.iterator().next().getClass());
            } else {
                result.add(Object.class);
            }
        } else {
            final Iterator<?> iter = objects.iterator();
            final Set<Class> commonClasses = new HashSet<Class>();
            commonClasses.addAll(Classes.assignableTo(iter.next().getClass()));
            while (iter.hasNext()) {
                commonClasses.retainAll(Classes.assignableTo(iter.next().getClass()));
            }

            result.addAll(specify(commonClasses));
        }

        return result;
    }

    /**
     * @param objects the objects to inspect
     * @return A set of classes common to all members of <code>objects</code>
     * @see #commonBaseClasses(java.util.Collection)
     */
    public static Set<Class> commonBaseClasses(final Object... objects) {
        return commonBaseClasses(Arrays.asList(objects));
    }

    /**
     * Returns the most specific set of classes from the provided set
     * <p/>
     * 'Most specific' means that if a class in <code>classes</code> is an interface and implemented by another class
     * in <code>classes</code>, or is a superclass of another class in <code>classes</code>, it will be eliminated from
     * the resulting set.
     * <p/>
     * The original set is unmodified
     *
     * @param classes a collection of classes
     * @return the most specific set of classes from <code>classes</code>
     */
    public static Set<Class> specify(final Collection<Class> classes) {
        final Set<Class> result = new HashSet<Class>(classes);
        for (final Class<?> aClass : classes) {
            final Set<Class> parents = assignableTo(aClass);
            parents.remove(aClass);
            result.removeAll(parents);
            result.removeAll(Arrays.asList(aClass.getInterfaces()));
        }

        if (result.size() > 1) {
            result.remove(Object.class);
        }

        return result;
    }

    /**
     * @param classes a collection of classes
     * @return the most specific set of classes from <code>classes</code>
     * @see #specify(java.util.Collection)
     */
    public static Set<Class> specify(final Class... classes) {
        return specify(Arrays.asList(classes));
    }

    /**
     * Iterate through the class hierarchy for <code>aClass</code>, calling <code>block</code> for each class
     * <p/>
     * The walk begins at <code>aClass</code>, followed by <code>aClass</code>'s superclass, etc.
     *
     * @param aClass the class with which to begin the walk
     * @param block  the block to execute for each member of the hierarchy
     * @param <T>    The return type of {@link Block#invoke(Object)}
     * @return The return valie of the last call to {@link Block#invoke(Object)}
     */
    public static <T> T walkClassHierarchy(final Class<?> aClass, final Block<Class, T> block) {
        Class superclass = aClass;
        T result;
        do {
            result = block.invoke(superclass);
            superclass = superclass.getSuperclass();
        } while (superclass != null);

        return result;
    }

    /**
     * @param classes The classes to gather the interfaces for
     * @return The collection of interfaces implemented by <code>classes</code>
     * @see #allInterfacesFor(Iterable)
     */
    public static Collection<Class> allInterfacesFor(final Class... classes) {
        return allInterfacesFor(Arrays.asList(classes));
    }

    /**
     * Determine all interfaces implemented by each element of <code>classes</code> This includes interfaces implemented
     * by each superclass
     *
     * @param classes The classes to gather the interfaces for
     * @return The collection of interfaces implemented by <code>classes</code>
     */
    public static Collection<Class> allInterfacesFor(final Iterable<Class> classes) {
        final Set<Class> result = new LinkedHashSet<Class>();
        for (final Class aClass : classes) {
            walkInterfaceHierarchy(aClass, new Block<Class, Set<Class>>() {
                public Set<Class> invoke(final Class aClass) {
                    result.add(aClass);
                    return result;
                }
            });
        }

        return result;
    }

    /**
     * Returns true if <code>aClass</code> implements <code>method</code>, false otherwise
     *
     * @param aClass The class to inspect
     * @param method the method to look for
     * @return true if <code>aClass</code> implements <code>method</code>, false otherwise
     */
    public static boolean implementsMethod(final Class aClass, final Method method) {
        boolean result = true;
        try {
            aClass.getMethod(method.getName(), method.getParameterTypes());
        } catch (NoSuchMethodException e) {
            result = false;
        }
        return result;
    }

    /**
     * Walks the interface hierarchy of <code>aClass</code>, calling <code>block</code> for each interface implemented
     * by <code>aClass</code>, or any of its superclasses or any interfaces implemented by it's superclasses
     *
     * @param aClass The class with which to begin walking the interface hierarchy
     * @param block The block to execute for each interface
     * @param <T> The return type of the block
     * @return The last value returned by the block
     */
    public static <T> T walkInterfaceHierarchy(final Class aClass, final Block<Class, T> block) {
        final Queue<Class> interfaces = new LinkedList<Class>();
        T result = null;
        if(aClass.isInterface()){
            interfaces.add(aClass);
        }
        walkClassHierarchy(aClass, new Block<Class, Queue<Class>>(){
            public Queue<Class> invoke(final Class aClass) {
                interfaces.addAll(Arrays.asList(aClass.getInterfaces()));
                return interfaces;
            }
        });
        while(interfaces.size() > 0) {
            final Class anInterface = interfaces.remove();
            result = block.invoke(anInterface);
            interfaces.addAll(Arrays.asList(anInterface.getInterfaces()));
        }

        return result;
    }

    /**
     * Convenience method to load a class using the thread's class loader, and convert classNotFound exception
     * into a runtime exception

     * @param className the fully qualified class name
     * @return the class object
     */
    public static Class<?> loadClass(String className) {
        try {
            return Thread.currentThread().getContextClassLoader().loadClass(className);
        } catch (ClassNotFoundException e) { throw new RuntimeException(e); }
    }

    /**
     * Convenience method to instantiate a class, and convert checked exceptions to runtime exceptions
     *
     * @param className the class to instantiate
     * @return an instance of <code>className</code>
     */
    public static Object instantiate(String className) {
        try {
            return loadClass(className).newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static Collection<Method> annotatedMethods(Class<?> aClass,
                                                      final Class<? extends Annotation> annotationClass) {
        return Lists.select(aClass.getMethods(), new Block<Method, Boolean>() {
            @Override
            public Boolean invoke(Method method) { return method.getAnnotation(annotationClass) != null; }
        });
    }
}