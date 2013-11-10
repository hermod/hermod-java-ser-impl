package com.github.hermod.ser.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

import org.junit.Test;

import com.github.hermod.ser.Types;

public class MsgsTest {

    
    @Test
    public void testValidatesThatClassIsNotInstanciable() throws Exception {
        final Constructor<Msgs> constructor = Msgs.class.getDeclaredConstructor();
        assertThat(Modifier.isPrivate(constructor.getModifiers())).isTrue();
        constructor.setAccessible(true);
        constructor.newInstance();
    }

}
