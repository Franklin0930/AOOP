package Model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class NumberleModel extends Observable implements INumberleModel {
    private String targetNumber;
    private int remainingAttempts;
    private boolean gameWon;
    private int[] inputColors;
    private HashMap<Character,Integer> keyboardColors;
    private boolean[] flags = {true,true,true};
    public boolean invariant() {
        return (remainingAttempts >= 0 && remainingAttempts <= MAX_ATTEMPTS) &&
                (targetNumber != null) &&
                (inputColors != null) &&
                (keyboardColors != null);
    }

    private String generateTargetEquation() {

        List<String> equations = new ArrayList<>();
        String fileName = "equations.txt";

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                equations.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (flags[2]==true) {
            Random rand = new Random();
            return equations.get(rand.nextInt(equations.size()));
        }
        else {
            return "1+2+3=6";
        }
    }

    @Override
    public void initialize() {
        remainingAttempts = MAX_ATTEMPTS;
        gameWon = false;
        setChanged();
        notifyObservers();
        targetNumber = generateTargetEquation();

        if(flags[1]==true) {
            System.out.println(targetNumber);
        }

        inputColors = new int[]{0,0,0,0,0,0,0};
        keyboardColors = new HashMap<>();
        keyboardColors.put('1',0);
        keyboardColors.put('2',0);
        keyboardColors.put('3',0);
        keyboardColors.put('4',0);
        keyboardColors.put('5',0);
        keyboardColors.put('6',0);
        keyboardColors.put('7',0);
        keyboardColors.put('8',0);
        keyboardColors.put('9',0);
        keyboardColors.put('0',0);
        keyboardColors.put('+',0);
        keyboardColors.put('-',0);
        keyboardColors.put('*',0);
        keyboardColors.put('/',0);
        keyboardColors.put('=',0);
        assert invariant();
    }

    @Override
    public void processInput(String input) {
        assert invariant();
        if (input == null || input.length() != 7) {
            setChanged();
            notifyObservers("Invalid Input");
            return;
        }

        if(flags[0]==true){
            if(!input.contains("=")){
                setChanged();
                notifyObservers("Without Equals");
                System.out.println("Invalid equation! The equation must contains equals sign.");
                return;
            }
            if(!input.contains("+")&&!input.contains("-")&&!input.contains("*")&&!input.contains("/")){
                setChanged();
                notifyObservers("Without Operator");
                System.out.println("Invalid equation! The equation must contains at least one operator.");
                return;
            }
            if(!input.contains("1")&&!input.contains("2")&&!input.contains("3")&&!input.contains("4")&&
                    !input.contains("5")&&!input.contains("6")&&!input.contains("7")&&!input.contains("8")&&
                    !input.contains("9")&&!input.contains("0")){
                setChanged();
                notifyObservers("Without Number");
                System.out.println("Invalid equation! The equation must contains numbers.");
                return;
            }
        }

        remainingAttempts--;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (i < targetNumber.length() && c == targetNumber.charAt(i)) {
                System.out.print("Green ");
                inputColors[i] = 1;
                keyboardColors.put(c,1);
            } else if (targetNumber.contains(String.valueOf(c))) {
                System.out.print("Orange ");
                inputColors[i] = 2;
                if (keyboardColors.get(c)!=1) {
                    keyboardColors.put(c, 2);
                }
            } else {
                System.out.print("Gray ");
                inputColors[i] = 3;
                keyboardColors.put(c,3);
            }
        }
        System.out.println();

        if (input.equals(targetNumber)) {
            gameWon = true;
        }
        if (isGameOver()) {
            setChanged();
            notifyObservers(gameWon ? "Game Won" : "Game Over");
        } else {
            setChanged();
            notifyObservers("Try Again");
        }
        assert invariant();
    }


    @Override
    public boolean isGameOver() {
        return remainingAttempts <= 0 || gameWon;
    }

    @Override
    public boolean isGameWon() {
        return gameWon;
    }

    @Override
    public String getTargetNumber() {
        return targetNumber;
    }

    @Override
    public int getRemainingAttempts() {
        return remainingAttempts;
    }

    @Override
    public void startNewGame() {
        initialize();
    }

    public int[] getInputColors() {
        return inputColors;
    }

    public HashMap<Character, Integer> getKeyboardColors() {
        return keyboardColors;
    }

    public boolean[] getFlags() {
        return flags;
    }
}
