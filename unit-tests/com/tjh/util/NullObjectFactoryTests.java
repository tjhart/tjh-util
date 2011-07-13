package com.tjh.util;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import com.tjh.util.Maps;
import com.tjh.util.NullObjectFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.theories.DataPoint;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

@RunWith(Theories.class)
public class NullObjectFactoryTests {
    private Foo nullObject;

    @DataPoint public static final Method getByte;
    @DataPoint public static final Method getShort;
    @DataPoint public static final Method getInt;
    @DataPoint public static final Method getLong;
    @DataPoint public static final Method getFloat;
    @DataPoint public static final Method getDouble;
    @DataPoint public static final Method getBoolean;
    @DataPoint public static final Method getChar;

    static {
        try {
            getByte    = Foo.class.getDeclaredMethod("getByte");
            getShort   = Foo.class.getDeclaredMethod("getShort");
            getInt     = Foo.class.getDeclaredMethod("getInt");
            getLong    = Foo.class.getDeclaredMethod("getLong");
            getFloat   = Foo.class.getDeclaredMethod("getFloat");
            getDouble  = Foo.class.getDeclaredMethod("getDouble");
            getBoolean = Foo.class.getDeclaredMethod("getBoolean");
            getChar    = Foo.class.getDeclaredMethod("getChar");
        } catch (final NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
    
    private static final Map<Class, Object> primitiveMap = Maps.<Class, Object>asMap(
            byte.class, (byte) 0,
            short.class, (short) 0,
            int.class, 0,
            long.class, 0L,
            float.class, 0.0f,
            double.class, 0.0d,
            boolean.class, false,
            char.class, (char) 0);
    @Test
    public void equalsNullIsTrue() {
        assertThat(nullObject.equals(null), is(true));
    }

    @Test
    public void equalsAnotherNullObjectIsTrue() {
        assertThat(nullObject.equals(NullObjectFactory.nullObject(Foo.class)), is(true));
    }

    @Test
    public void toStringDeliversSpecialCode(){
        assertThat(nullObject.toString(), equalTo("NullObject"));
    }

    @Theory
    public void methodsReturnDefaultForType(final Method method)
        throws InvocationTargetException, IllegalAccessException {
        assertThat(method.invoke(nullObject), equalTo(primitiveMap.get(method.getReturnType())));
    }

    @Test
    public void methodReturningStringReturnsNull(){
        assertThat(nullObject.getString(), nullValue());
    }

    @Test
    public void methodReturningFooReturnsNullObject(){
        assertThat(nullObject.getFoo(), equalTo(nullObject));
    }

    @Test
    public void nullObjectForFinalClassReturnsNull(){
        assertThat(NullObjectFactory.nullObject(Integer.class), nullValue());
    }

    @Before
    public void before() throws NoSuchMethodException {
        nullObject = NullObjectFactory.nullObject(Foo.class);
    }

    public static class Foo {
        byte getByte(){ return (byte)1;}
        short getShort(){return 2;}
        int getInt(){ return 3;}
        long getLong(){ return 4L;}
        float getFloat() {return 5.5f;}
        double getDouble(){ return 6.6d;}
        boolean getBoolean(){ return true;}
        char getChar(){ return '7';}
        String getString(){ return "some string";}
        Foo getFoo(){ return this; }
    }

}


