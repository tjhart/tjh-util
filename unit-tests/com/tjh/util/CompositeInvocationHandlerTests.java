package com.tjh.util;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import com.tjh.util.CompositeInvocationHandler;
import org.junit.Test;

import java.lang.reflect.Method;

public class CompositeInvocationHandlerTests{

    @Test
    public void objectForCachesDelegateOnceFound() throws NoSuchMethodException {
        Interface mockDelegate = createMock(Interface.class);
        final Method method = Interface.class.getMethod("method");

        replay(mockDelegate);

        CompositeInvocationHandler handler = new CompositeInvocationHandler(mockDelegate);
        handler.objectFor(method);
        assertThat(handler.cache.get(method), equalTo((Object)mockDelegate));
        verify(mockDelegate);
    }

    interface Interface{
        public void method();
    }
}

