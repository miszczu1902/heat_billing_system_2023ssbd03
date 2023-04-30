package pl.lodz.p.it.ssbd2023.ssbd03.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class BcryptHashGeneratorTest {

    @Test
    void generateHash() {
        String plainText = "password123";
        String generated = BcryptHashGenerator.generateHash(plainText);
        assertTrue(BcryptHashGenerator.verifyPassword(plainText, generated));
    }
}