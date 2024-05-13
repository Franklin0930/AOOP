package Controller;

import Model.INumberleModel;
import View.NumberleView;

import java.util.HashMap;

// Controller.NumberleController.java
public class NumberleController {
    private INumberleModel model;
    private NumberleView view;

    public NumberleController(INumberleModel model) {
        this.model = model;
    }

    public void setView(NumberleView view) {
        this.view = view;
    }

    public void processInput(String input) {
        model.processInput(input);
    }

    public boolean isGameOver() {
        return model.isGameOver();
    }

    public boolean isGameWon() {
        return model.isGameWon();
    }

    public String getTargetWord() {
        return model.getTargetNumber();
    }

    public int getRemainingAttempts() {
        return model.getRemainingAttempts();
    }

    public void startNewGame() {
        model.startNewGame();
    }
    public int[] getInputColors() {
        return model.getInputColors();
    }
    public HashMap<Character, Integer> getKeyboardColors() {
        return model.getKeyboardColors();
    }
    public boolean[] getFlags() {
        return model.getFlags();
    }
}