package com.tjh.util;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ArgumentParserTests {
    private TestArgumentParser argParser;
    private boolean fooGotCalled = false;
    private String[] varArgs;
    private String bar;
    private Object[] various;

    @Test
    public void parseCallsAppropriateMethod() {
        argParser.parse("foo");
        assertThat(fooGotCalled, is(true));
    }

    @Test
    public void parseIdentifiesFlags() {
        argParser.parse("--foo");
        assertThat(fooGotCalled, is(true));
    }

    @Test
    public void parsePullsArgumentsForMethod() {
        argParser.parse("bar", "baz");
        assertThat(bar, equalTo("baz"));
    }

    @Test
    public void parseHandlesVariableArgumentFlag() {
        argParser.parse("varArgs", "arg1", "arg2", "arg3");
        assertThat(varArgs, equalTo(new String[]{"arg1", "arg2", "arg3"}));
    }

    @Test
    public void parseHandlesMultipleFlags() {
        argParser.parse("bar", "baz", "foo", "varArgs", "1", "2");
        assertThat(bar, equalTo("baz"));
        assertThat(fooGotCalled, is(true));
        assertThat(varArgs, equalTo(new String[]{"1", "2"}));
    }

    @Test
    public void parseHandlesVariousTypes() {
        argParser.parse("various", "name", "28", "true");
        assertThat(various, equalTo(new Object[]{"name", 28, true}));
    }

    @Test
    public void parseCallsIsValidCompletion() {
        argParser = spy(argParser);
        when(argParser.parse()).thenCallRealMethod();

        argParser.parse();
        verify(argParser).isValid();
    }

    @Test
    public void parseCallsPrintUsageIfResultIsFalse() {
        argParser = spy(argParser);
        when(argParser.parse("bad")).thenCallRealMethod();

        assertThat(argParser.parse("bad"), is(false));
        verify(argParser).printUsage();
    }

    @Test
    public void parseCallsPrintUsageIfAnyResultIsFalse() {
        argParser = spy(argParser);
        when(argParser.parse("bad", "foo")).thenCallRealMethod();
        assertThat(argParser.parse("bad", "foo"), is(false));
        verify(argParser).printUsage();
    }

    @Test
    public void parseHandlesVarArgsInTheMiddle() {
        argParser.parse("varArgs", "1", "2", "bar", "baz", "foo");
        assertThat(bar, equalTo("baz"));
        assertThat(fooGotCalled, is(true));
        assertThat(varArgs, equalTo(new String[]{"1", "2"}));
    }

    @Before
    public void before() {
        argParser = new TestArgumentParser();
    }

    class TestArgumentParser extends ArgumentParser {

        public boolean foo() {
            fooGotCalled = true;
            return true;
        }

        public boolean bar(final String value) {
            bar = value;
            return true;
        }

        public boolean varArgs(final String... value) {
            varArgs = value;
            return true;
        }

        public boolean various(final String name, final int age, final boolean bald) {
            various = new Object[]{name, age, bald};
            return true;
        }

        public boolean bad() {
            return false;
        }

        protected void printUsage() {
            System.out.println("ArgumentParserTests$TestArgumentParser.printUsage");
        }
    }
}
