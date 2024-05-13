package Model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

class NumberleModelTest {
    INumberleModel model;
    @BeforeEach
    void setUp() {
        model = new NumberleModel();
    }

    @AfterEach
    void tearDown() {
        model = null;
    }

    @org.junit.jupiter.api.Test
    void initialize() {
        model.initialize();
        assertTrue(model.invariant());
        assertTrue(model.getRemainingAttempts()==INumberleModel.MAX_ATTEMPTS);
    }

    @org.junit.jupiter.api.Test
    void processInput() {
        model.initialize();
        model.processInput("1+1+1=3");
        assertTrue(model.invariant());
        assertTrue(model.getRemainingAttempts()==INumberleModel.MAX_ATTEMPTS-1);
    }

    @org.junit.jupiter.api.Test
    void processInputOver() {
        model.initialize();
        model.processInput("1+1+1=3");
        model.processInput("1+1+1=3");
        model.processInput("1+1+1=3");
        model.processInput("1+1+1=3");
        model.processInput("1+1+1=3");
        model.processInput("1+1+1=3");
        assertTrue(model.invariant());
        assertTrue(model.isGameOver());
    }
}