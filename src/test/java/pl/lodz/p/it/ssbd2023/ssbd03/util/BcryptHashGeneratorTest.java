package pl.lodz.p.it.ssbd2023.ssbd03.util;

import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class BcryptHashGeneratorTest {

    @Inject
    BcryptHashGenerator bcryptHashGenerator;

    @Test
    void generateHash() {
        final String plainText = "password123";
        final String generated = bcryptHashGenerator.generate(plainText.toCharArray());
        assertTrue(bcryptHashGenerator.verify(plainText.toCharArray(), generated));
    }
}