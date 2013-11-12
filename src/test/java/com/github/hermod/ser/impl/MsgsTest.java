package com.github.hermod.ser.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.github.hermod.ser.Msg;
import com.github.hermod.ser.Null;
import com.github.hermod.ser.Types;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * <p>MsgsTest. </p>
 * 
 * @author anavarro - Nov 11, 2013
 * 
 */
public class MsgsTest {

    @Test
    public void testValidatesThatClassIsNotInstanciable() throws Exception {
        final Constructor<Msgs> constructor = Msgs.class.getDeclaredConstructor();
        assertThat(Modifier.isPrivate(constructor.getModifiers())).isTrue();
        constructor.setAccessible(true);
        constructor.newInstance();
    }

    /**
     * testIsAsciiString.
     * 
     */
    @Test
    public void testIsAsciiString() {
        assertThat(Msgs.isAsciiString("Toto")).isTrue();
        assertThat(
                Msgs.isAsciiString("Tu va nous manquer Kiki Le Fada, toi qui peut être nous regarde de la haut OhhhhOhhhOhhhh Sache que ta mort nous a tous sécoué OhHéOhHéOhHéOhHéOhHé. Kiki Le Fada nous a quitté malheureusement il n'est plus là la la la la la la la. Et vous mes amis vous êtes là donc qui ne saute pas n'est pas en deuil deuil !  "))
                .isFalse();
        assertThat(Msgs.isAsciiString("€uro")).isFalse();
    }

    /**
     * testCalculateNextPowerOf2.
     * 
     * @throws Exception
     */
    @Test
    public void testCalculateNextPowerOf2() throws Exception {
        assertThat(Msgs.calculateNextPowerOf2(9)).isEqualTo(16);
        assertThat(Msgs.calculateNextPowerOf2(16)).isEqualTo(16);
        assertThat(Msgs.calculateNextPowerOf2(17)).isEqualTo(32);
        assertThat(Msgs.calculateNextPowerOf2(29)).isEqualTo(32);
        assertThat(Msgs.calculateNextPowerOf2(40)).isEqualTo(64);
        assertThat(Msgs.calculateNextPowerOf2(70)).isEqualTo(128);
    }

    /**
     * testSerializeToJson.
     * 
     * @throws Exception
     */
    @Test
    public void testSerializeToJson() throws Exception {
        // TODO see AbstractMsgTest.testToString();
    }

    /**
     * testEquals.
     * 
     * @throws Exception
     */
    @Test
    public void testEquals() throws Exception {
        // TODO see AbstractMsgTest.equals();
    }

    @Test
    public void testHashcode() throws Exception {
        // TODO see AbstractMsgTest.hashcode();
    }

}
