package com.tjh.util;

import static com.pica.test.Matchers.contains;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.reset;
import static org.easymock.EasyMock.verify;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import com.tjh.util.CompositeFactory;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.UndeclaredThrowableException;
import java.util.Arrays;

public class CompositeFactoryTests {
    private MyComposite composite;
    private Interface1 impl1;
    private Interface2 impl2;

    @Test
    public void createCompositeReturnsObjectThatImplementsAllInterfaces() {
        assertThat(Arrays.<Class>asList(composite.getClass().getInterfaces()), contains((Class)MyComposite.class));
    }

    @Test
    public void invocationsOnProxyAreRedirectedToAppropriateInterfaces() {
        reset(impl1, impl2);

        impl1.method1();
        impl2.method3();
        replay(impl1, impl2);

        composite.method1();
        composite.method3();
        verify(impl1, impl2);
    }

    @Test
    public void invokePrefersFirstDeclarationOfMethod() {
        reset(impl1);
        impl1.dupMethod();
        replay(impl1);

        composite.dupMethod();
        verify(impl1, impl2);
    }

    @Test
    public void UndeclaredThrowableWrappingNoSuchMethodIsThrownIfUnknownMethodIsCalled(){
        composite = CompositeFactory.createComposite(MyComposite.class, impl1);

        try{
            composite.method3();
            fail();
        }catch(final UndeclaredThrowableException ute){
            assertThat(ute.getCause(), instanceOf(NoSuchMethodException.class));
        }
    }

    @Before
    public void before() {

        impl1 = createMock(Interface1.class);
        impl2 = createMock(Interface2.class);
        replay(impl1, impl2);

        composite = CompositeFactory.createComposite(MyComposite.class, impl1, impl2);
    }

    interface Interface1 {
        public void method1();

        public void method2();

        public void dupMethod();
    }

    interface Interface2 {
        public void method3();

        public void method4();

        public void dupMethod();
    }

    interface MyComposite extends Interface1, Interface2 {
    }
}
