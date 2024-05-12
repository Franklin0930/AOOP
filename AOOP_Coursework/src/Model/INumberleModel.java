package Model;

import java.util.HashMap;

public interface INumberleModel {
    int MAX_ATTEMPTS = 6;
    boolean invariant();
    void initialize();
    void processInput(String input);
    boolean isGameOver();
    boolean isGameWon();
    String getTargetNumber();
    int getRemainingAttempts();
    void startNewGame();
    int[] getInputColors();
    HashMap<Character, Integer> getKeyboardColors();
    boolean[] getFlags();
}