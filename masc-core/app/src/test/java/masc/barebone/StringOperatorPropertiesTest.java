package masc.barebone;

import edu.wm.cs.masc.mutation.properties.StringOperatorProperties;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class StringOperatorPropertiesTest {
    StringOperatorProperties properties;

    @Before
    public void setUp() throws Exception {
        properties = new StringOperatorProperties(
                "src/main/resources/Cipher.properties");
    }

    @Test
    public void getType() {
        assertEquals("StringOperator", properties.getType());
    }

    /*
    @Test
    public void getOutput_dir() {
        //test is kinda pointless
        assertEquals("/csci435outputfiles",
                properties.getOutputDir());
    }
    */

    @Test
    public void getApi_name() {
        assertEquals("javax.crypto.Cipher", properties.getApiName());
    }

    @Test
    public void getInvocation() {
        assertEquals("getInstance", properties.getInvocation());
    }

    @Test
    public void getSecureParam() {
        assertEquals("AES/GCM/NoPadding", properties.getSecureParam());
    }

    @Test
    public void getInsecureParam() {
        assertEquals("AES", properties.getInsecureParam());
    }

    @Test
    public void getNoise() {
        assertEquals("~", properties.getNoise());
    }

    @Test
    public void getVariableName() {
        assertEquals("cryptoVariable", properties.getVariableName());
    }
}