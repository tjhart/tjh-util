package com.tjh.util;

import org.junit.Test;

import static com.tjh.util.Strings.aOrAn;
import static com.tjh.util.Strings.join;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class StringsTests {

    @Test
    public void aOrAnReturnsCorrectValues() {
        assertThat(aOrAn("a"), equalTo("an"));
        assertThat(aOrAn("apple"), equalTo("an"));
        assertThat(aOrAn("bacon"), equalTo("a"));
        assertThat(aOrAn("e"), equalTo("an"));
        assertThat(aOrAn("elephant"), equalTo("an"));
        assertThat(aOrAn("geo prism"), equalTo("a"));
        assertThat(aOrAn("i"), equalTo("an"));
        assertThat(aOrAn("igloo"), equalTo("an"));
        assertThat(aOrAn("o"), equalTo("an"));
        assertThat(aOrAn("orchestra"), equalTo("an"));
        assertThat(aOrAn("x"), equalTo("an"));
        assertThat(aOrAn("x-ray"), equalTo("an"));
    }

    @Test
    public void joinWorks() {
        assertThat(join(",", "a", "b", "c"), equalTo("a,b,c"));
    }

    @Test
    public void joinWorksWithNoStrings(){
        assertThat(join(","), equalTo(""));
    }
}
