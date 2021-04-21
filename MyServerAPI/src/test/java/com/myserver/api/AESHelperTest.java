package com.myserver.api;

import com.myserver.api.util.AESHelper;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AESHelperTest {

    @Test
    public void aes() {
        try {
            String sourceText = "test123";
            String cypheredText = AESHelper.encode(sourceText);
            String decypheredText = AESHelper.decode(cypheredText);
            assertEquals(sourceText, decypheredText);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
