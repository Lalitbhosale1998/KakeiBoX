package com.lalit.kakeibox;

import org.junit.Test;
import androidx.graphics.shapes.Morph;
import java.lang.reflect.Method;

public class MorphTest {
    @Test
    public void testMethods() {
        try {
            Class<?> clazz = Class.forName("androidx.graphics.shapes.Morph");
            for (Method m : clazz.getMethods()) {
                System.out.println("MORPH_METHOD: " + m.getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
