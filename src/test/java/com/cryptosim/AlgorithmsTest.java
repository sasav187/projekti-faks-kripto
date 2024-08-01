package com.cryptosim;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.Test;

public class AlgorithmsTest {

    @Test
    public void testRailfence() {

        String text = "KRIPTOGRAFIJA";
        int key = 3;
        String expected = "KTAARPORFJIGI";

        String result = Algorithms.RailFence(text, key);
        assertEquals(expected, result);
    }

    @Test
    public void testMyszkowski() {

        String text = "KRIPTOGRAFIJA";
        String key = "SIGURNOST";
        String expected = "IJRIOGTKRFAPA";

        String result = Algorithms.Myszkowski(text, key);
        assertEquals(expected, result);
    }

    @Test
    public void testPlayfair() {

        String text = "KRIPTOGRAFIJA";
        String key = "SIGURNOST";
        String expected = "QSULATUSFPGWUO";

        String result = Algorithms.Playfair(text, key);
        assertEquals(expected, result);
    }

}