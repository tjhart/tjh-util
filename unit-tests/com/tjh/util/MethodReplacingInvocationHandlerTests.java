package com.tjh.util;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import com.tjh.util.Maps;
import com.tjh.util.MethodReplacingInvocationHandler;
import com.tjh.util.ThrowingBlock2;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Collections;

public class MethodReplacingInvocationHandlerTests {
    private TestImpl original;
    private MyTest proxy;
    private Method testMethod;

    @Test
    public void originalMethodCalledIfNotReplaced() throws Throwable {
        InvocationHandler handler =
                new MethodReplacingInvocationHandler<MyTest>(original,
                        Collections.<Method, ThrowingBlock2<MyTest, Object[], Object, ? extends Throwable>>emptyMap());

        assertThat((String) handler.invoke(proxy, testMethod, new Object[]{}), equalTo("original"));
    }

    @Test
    public void handlerCalledIfReplaced() throws Throwable {
        final ThrowingBlock2<MyTest, Object[], Object, Exception> block2 =
                new ThrowingBlock2<MyTest, Object[], Object, Exception>() {
                    public Object invoke(MyTest proxy, Object[] objects) { return "handler"; }
                };

        InvocationHandler handler = new MethodReplacingInvocationHandler<MyTest>(original,
                Maps.<Method, ThrowingBlock2<MyTest, Object[], Object, ? extends Throwable>>asMap(testMethod, block2));

        assertThat((String) handler.invoke(proxy, testMethod, new Object[]{}), equalTo("handler"));
    }

    @Before
    public void before() throws NoSuchMethodException {

        original = new TestImpl();
        proxy = new MyTest() {
            public String method() { return "proxy"; }
        };
        testMethod = MyTest.class.getMethod("method");
    }

    interface MyTest {
        public String method();
    }

    class TestImpl implements MyTest {
        public String method() { return "original"; }
    }
}
