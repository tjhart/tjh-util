package com.tjh.util;

import static com.pica.test.Matchers.contains;
import static com.tjh.util.Classes.allInterfacesFor;
import static com.tjh.util.Classes.implementsMethod;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import com.tjh.util.Block;
import com.tjh.util.Classes;
import com.tjh.util.Sets;
import org.junit.Test;

import java.io.Serializable;
import java.math.BigDecimal;

public class ClassesTests {

    @Test
    public void assignableTo() {
        assertThat(Classes.assignableTo(Integer.class),
                equalTo(Sets.<Class>asSet(Integer.class, Comparable.class, Number.class, Serializable.class,
                        Object.class)));
    }

    @Test
    public void commonBaseClassesIncludesClassAndInterfaces() {
        assertThat(Classes.commonBaseClasses(1, 2L, 4.0f, 3.141592654d, BigDecimal.ONE),
                equalTo(Sets.<Class>asSet(Number.class, Comparable.class)));
    }

    @Test
    public void commonBaseClassesReturnsObjectForEmptyList() {
        assertThat(Classes.commonBaseClasses(), equalTo(Sets.<Class>asSet(Object.class)));
    }

    @Test
    public void commonBaseClassesReturnsClassForSingleClass() {
        assertThat(Classes.commonBaseClasses(new Subclass()), equalTo(Sets.<Class>asSet(Subclass.class)));
    }

    @Test
    public void commonBaseClassDoesNotIncludeInterfaceIfCommonClassExtendsIt() {
        assertThat(Classes.commonBaseClasses(new Subclass(), new Superclass()),
                equalTo(Sets.<Class>asSet(Superclass.class)));
    }

    @Test
    public void commonBaseClassDoesNotIncludeObjectIfMoreSpecificClassIsAvailable() {
        assertThat(Classes.commonBaseClasses(new Superclass(), new LoneClass()),
                equalTo(Sets.<Class>asSet(Interface1.class)));
    }

    @Test
    public void commonBaseClassIncludesClassIfAllMembersAreTheSame() {
        assertThat(Classes.commonBaseClasses(1, 2, 3, 4), equalTo(Sets.<Class>asSet(Integer.class)));
    }

    @Test
    public void specifyReturnsSetOfMostSpecificClasses() {
        assertThat(Classes.specify(Subclass.class, Superclass.class), equalTo(Sets.<Class>asSet(Subclass.class)));
    }

    @Test
    public void specifyConsidersMultipleHeirarchies() {
        assertThat(Classes.specify(LoneClass.class, Subclass.class, Interface1.class),
                equalTo(Sets.<Class>asSet(LoneClass.class, Subclass.class)));
    }

    @Test
    public void walkClassHierarchyCallsBlockForEachSuperclass() {
        @SuppressWarnings({"unchecked"})
        Block<Class, Object> mockBlock = createMock(Block.class);

        expect(mockBlock.invoke(Subclass.class)).andReturn(null);
        expect(mockBlock.invoke(Superclass.class)).andReturn(null);
        expect(mockBlock.invoke(Object.class)).andReturn(null);
        replay(mockBlock);

        Classes.walkClassHierarchy(Subclass.class, mockBlock);
        verify(mockBlock);
    }

    @Test
    public void walkClassHierarchyReturnsLastReturnValueFromBlock() {
        @SuppressWarnings({"unchecked"})
        Block<Class, Object> mockBlock = createMock(Block.class);

        expect(mockBlock.invoke(Superclass.class)).andReturn(null);
        expect(mockBlock.invoke(Object.class)).andReturn("expected");
        replay(mockBlock);

        assertThat(Classes.walkClassHierarchy(Superclass.class, mockBlock), equalTo((Object) "expected"));
    }

    @Test
    public void allInterfacesDoesntDuplicateInterfaceList() {
        assertThat(allInterfacesFor(Subclass.class, LoneClass.class).toArray(),
                equalTo(new Object[]{Interface2.class, Interface1.class}));
    }

    @Test
    public void implementsMethodReturnsTrueAppropriately() throws NoSuchMethodException {
        assertThat(implementsMethod(LoneClass.class, Interface1.class.getMethod("method1")), is(true));
    }

    @Test
    public void walkInterfaceHierarchyCallsBlockForEachImplementedInterface() {
        @SuppressWarnings({"unchecked"})
        Block<Class, Object> mockBlock = createMock(Block.class);

        expect(mockBlock.invoke(Interface3.class)).andReturn(null);
        expect(mockBlock.invoke(Interface1.class)).andReturn(null).atLeastOnce();
        expect(mockBlock.invoke(Interface2.class)).andReturn(null).atLeastOnce();
        replay(mockBlock);

        Classes.walkInterfaceHierarchy(Interface3.class, mockBlock);
        verify(mockBlock);
    }

    @Test
    public void walkInterfaceHierarchyCallsBlockForEachSuperclass(){
        @SuppressWarnings({"unchecked"})
        Block<Class, Object> mockBlock = createMock(Block.class);

        expect(mockBlock.invoke(Interface1.class)).andReturn(null);
        replay(mockBlock);

        Classes.walkInterfaceHierarchy(Impl.class, mockBlock);
        verify(mockBlock);
    }

    @Test
    public void allInterfacesIncludesSuperInterfaces() {
        assertThat(allInterfacesFor(Interface3.class),
                contains((Class) Interface3.class, Interface1.class, Interface2.class));
    }

    @Test
    public void loadClass(){
        assertThat(Classes.loadClass(this.getClass().getCanonicalName()), equalTo((Class)this.getClass()));
    }

    @Test
    public void instantiate(){
        assertThat(Classes.instantiate(this.getClass().getCanonicalName()), instanceOf(this.getClass()));
    }

    private interface Interface1 {
        public void method1();
    }

    private interface Interface2 {
    }

    private interface Interface3 extends Interface1, Interface2 {
    }

    private class Superclass implements Interface1 {
        public void method1() {}
    }

    private class Subclass extends Superclass implements Interface2 {
    }

    private class LoneClass implements Interface1 {
        public void method1() {}
    }

    private class Impl extends LoneClass{
    }
}
