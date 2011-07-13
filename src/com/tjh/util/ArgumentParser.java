package com.tjh.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Makes it convenient and easy to parse command line arguments.
 * To use, subclass, and implement methods with the same name as the flags you accept on the command line.
 * Each flag method should take zero or more arguments, depending on how you expect the call to be formatted. It should
 * then return true or false, depending on whether or not it considered the argument valid or not.
 *
 * Example:
 * class MyClass {
 *  private boolean foo = false;
 *  private String bar;
 *  private int baz;
 *
 *  public static final void main(String[] args){
 *      MyClass instance = new MyClass(args);
 *  }
 *
 *  public MyClass(String[] args){
 *      new MyParser().parse(args);
 *      run();
 *  }
 *
 *  class MyParser extends ArgumentParser{
 *      public boolean foo(){
 *          foo = true;
 *          return true;
 *      }
 *
 *      public boolean bar(String value){
 *          bar = value;
 *          return true;
 *      }
 *
 *      public boolean baz(int value){
 *          baz = value;
 *          return baz > 0;
 *      }
 *
 *      public void printUsage(){
 *          System.out.println("MyClass [--foo] --bar <bar name> --baz <number of repetitions>")
 *      }
 *  }
 * }
 */
public abstract class ArgumentParser {
    private static final Pattern FLAG_PATTERN = Pattern.compile("^-{0,2}(.+)$");
    private Map<String, Method> methodMap = new HashMap<String, Method>();
    private static final String ERROR_MSG =
            "Could not parse arguments %1$s at flag %2$s.\n" +
                    "Note that all flag method arguments must be simple types, or wrapper types in java.lang.\n" +
                    "If you have declared an array type as an argument, it must be the last argument in the list";
    private String[] args = new String[]{};

    protected ArgumentParser() {
        for (final Method method : this.getClass().getMethods()) {
            methodMap.put(method.getName(), method);
        }
    }

    /**
     * Parse the arguments
     * This method reflects on itself, searching for method names corresponding to the argument flags. If an argument
     * is "--flag", then parse will look for a method named "flag", determine how many arguments the method "flag"
     * takes, and attempt to convert every argument after "--flag" to the appropriate parameter type before calling the
     * method.
     *
     * @param args the arguments to parse
     * @return true if the parse was successful, false otherwise.
     */
    public boolean parse(final String... args) {
        this.args = args;
        boolean result = true;
        final Queue<String> queue = new LinkedList<String>(Arrays.asList(args));
        try {
            while (queue.size() > 0 && result) {
                result = parse(queue);
            }
        }
        catch (final NoSuchMethodException e) { result = false; }
        catch (final NoSuchElementException e) { result = false; }
        if (!result || !isValid()) {
            printUsage();
            result = false;
        }
        return result;
    }

    private boolean parse(final Queue<String> queue) throws NoSuchMethodException {
        final String flag = queue.peek();
        try {
            final boolean result;
            final Method targetMethod = getMethod(flag);
            if (targetMethod != null) {
                queue.remove();
                result = (Boolean) Methods.invoke(this, targetMethod,
                        getMethodArgs(targetMethod.getParameterTypes(), queue));
            } else {
                final int beforeDefault = queue.size();
                result = doDefault(queue);
                if (queue.size() == beforeDefault) {
                    throw new RuntimeException("You must remove items off the queue in 'doDefault'");
                }
            }
            return result;
        }
        catch (final IllegalAccessException e) { throw new RuntimeException(e); }
        catch (final InvocationTargetException e) { throw new RuntimeException(buildErrorMessage(flag), e); }
    }

    private String[] getMethodArgs(final Class<?>[] classes, final Queue<String> queue) {
        final List<String> result = new ArrayList<String>();
        for (final Class<?> aClass : classes) {
            if (aClass.isArray()) {
                while (queue.size() > 0 && !isFlag(queue.peek())) {
                    result.add(queue.remove());
                }
            } else {
                result.add(queue.remove());
            }
        }

        return result.toArray(new String[result.size()]);
    }

    /**
     * handle an argument that wasn't associated with a flag
     *
     * @param queue The list of command line arguments. You must remove all items off the queue that you
     * did not handle.
     * @return True if you understood the list of arguments, false otherwise
     */
    protected boolean doDefault(final Queue<String> queue) {
        queue.clear();
        return false;
    }

    protected String normalize(final String flag) {
        final Matcher matcher = FLAG_PATTERN.matcher(flag);
        return matcher.find() ? matcher.group(1) : flag;
    }

    protected String buildErrorMessage(final String flag) {
        return String.format(ERROR_MSG, Strings.join(args, ","), flag);
    }

    protected Method getMethod(final String flag) throws NoSuchMethodException {
        return methodMap.get(normalize(flag));
    }

    protected boolean isFlag(final String string) { return methodMap.keySet().contains(normalize(string)); }

    protected boolean isValid() { return true; }

    protected abstract void printUsage();
}
