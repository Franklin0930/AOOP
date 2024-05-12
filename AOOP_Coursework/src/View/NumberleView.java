package View;// View.NumberleView.java
import Controller.NumberleController;

import Model.INumberleModel;
import Model.NumberleModel;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.Observable;
import java.util.Observer;

public class NumberleView implements Observer {
    private final INumberleModel model;
    private final NumberleController controller;
    private final JFrame frame = new JFrame("Numberle");
    private final JTextField inputTextField = new JTextField(3);;
//    private final JLabel attemptsLabel = new JLabel("Attempts remaining: ");
    private final StringBuilder input = new StringBuilder();
    private final JTextField[][] fields = new JTextField[INumberleModel.MAX_ATTEMPTS][7];
    private final JButton[][] buttons = new JButton[2][10];

    private int currentLine;
    private int currentPosition = 0;
    String[] numberKeys={"1", "2", "3", "4", "5", "6", "7", "8", "9", "0"};
    String[] operationKeys={"Back", "+", "-", "*", "/", "=", "Enter", "New Game"};
    JLabel target = new JLabel();

    public NumberleView(INumberleModel model, NumberleController controller) {
        this.controller = controller;
        this.model = model;
        this.controller.startNewGame();
        ((NumberleModel)this.model).addObserver(this);
        initializeFrame();
        this.controller.setView(this);
        update((NumberleModel)this.model, null);
    }

    public void initializeFrame() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1280, 960);
        frame.setLayout(new BorderLayout());

        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.X_AXIS));
        center.add(new JPanel());

        JPanel displayPanel = new JPanel();
        displayPanel.setLayout(new GridLayout(7, 7, 20, 20));
        displayPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        for (int i = 0; i < INumberleModel.MAX_ATTEMPTS; i++) {
            for (int j = 0; j < 7; j++) {
                fields[i][j] = new JTextField();
                fields[i][j].setEditable(false);
                fields[i][j].setHorizontalAlignment(JTextField.CENTER);
                fields[i][j].setFont(new Font("Arial",Font.PLAIN,64));
                displayPanel.add(fields[i][j]);
            }
        }
        target.setText(controller.getTargetWord());
        target.setFont(new Font("Arial",Font.PLAIN,32));
        target.setVisible(controller.getFlags()[1]);
        displayPanel.add(target);

        center.add(displayPanel);
        center.add(new JPanel());
        frame.add(center, BorderLayout.NORTH);

        JPanel keyboardPanel = new JPanel();
        keyboardPanel.setLayout(new GridLayout(2, INumberleModel.MAX_ATTEMPTS, 5, 5));
        keyboardPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));

        JPanel numberPanel = new JPanel(new GridLayout(1, 10, 5, 5));

        for (int i=0; i<10; i++) {
            buttons[0][i] = new JButton(numberKeys[i]);
            int finalI = i;
            buttons[0][i].addActionListener(e -> {
                if (currentPosition < 7) {
                    fields[currentLine][currentPosition].setText(numberKeys[finalI]);
                    currentPosition++;
                }
            });
            buttons[0][i].setBackground(new Color(43, 220, 206));
            buttons[0][i].setPreferredSize(new Dimension(50,50));
            buttons[0][i].setFont(new Font("Arial",Font.PLAIN,20));
            numberPanel.add(buttons[0][i]);
        }

        JPanel operationPanel = new JPanel(new GridLayout(1, 5, 5, 5));

        for (int i=0; i<8; i++) {
            buttons[1][i] = new JButton(operationKeys[i]);
            int finalI = i;
            buttons[1][i].addActionListener(e -> {
                switch (operationKeys[finalI]) {
                    case "Back":
                        if (currentPosition > 0) {
                            fields[currentLine][currentPosition - 1].setText("");
                            currentPosition--;
                        }
                        break;
                    case "Enter":
                        for (int j = 0; j < currentPosition; j++) {
                            input.append(fields[currentLine][j].getText());
                        }
                        controller.processInput(input.toString());
                        break;
                    case "New Game":
                        for (int n = 0; n < INumberleModel.MAX_ATTEMPTS; n++) {
                            for (int j = 0; j < 7; j++) {
                                fields[n][j].setText("");
                                fields[n][j].setBackground(null);
                            }
                        }
                        for (int n = 0; n < 2; n++) {
                            for (int j = 0; j < 10; j++) {
                                if(n==0||(n==1&&j<6)) {
                                    buttons[n][j].setBackground(new Color(43, 220, 206));
                                }
                            }
                        }
                        currentPosition = 0;
                        currentLine = 0;
                        input.setLength(0);
                        buttons[1][7].setEnabled(false);
                        controller.startNewGame();
                        target.setText(controller.getTargetWord());
                        break;
                    case "+":
                    case "-":
                    case "*":
                    case "/":
                    case "=":
                        if (currentPosition < 7) {
                            fields[currentLine][currentPosition].setText(operationKeys[finalI]);
                            currentPosition++;
                        }
                }
            });
            buttons[1][i].setBackground(new Color(43, 220, 206));
            buttons[1][i].setPreferredSize(new Dimension(50,50));
            buttons[1][i].setFont(new Font("Arial",Font.PLAIN,20));
            operationPanel.add(buttons[1][i]);
        }
        buttons[1][7].setEnabled(false);
        frame.setLayout(new BorderLayout());
        frame.add(displayPanel, BorderLayout.CENTER);

        keyboardPanel.add(numberPanel, BorderLayout.NORTH);
        keyboardPanel.add(operationPanel, BorderLayout.SOUTH);
        frame.add(keyboardPanel, BorderLayout.SOUTH);

        frame.setVisible(true);

    }


    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof String) {
            String message = (String) arg;
            switch (message) {
                case "Invalid Input":
                    JOptionPane.showMessageDialog(frame, message, message, JOptionPane.INFORMATION_MESSAGE);
                    currentPosition = input.length();
                    input.setLength(0);
                    break;
                case "Without Equals":
                    JOptionPane.showMessageDialog(frame, "Invalid equation! The equation must contains equals sign.", message, JOptionPane.INFORMATION_MESSAGE);
                    currentPosition = input.length();
                    input.setLength(0);
                    break;
                case "Without Operator":
                    JOptionPane.showMessageDialog(frame, "Invalid equation! The equation must contains at least one operator.", message, JOptionPane.INFORMATION_MESSAGE);
                    currentPosition = input.length();
                    input.setLength(0);
                    break;
                case "Without Number":
                    JOptionPane.showMessageDialog(frame, "Invalid equation! The equation must contains numbers.", message, JOptionPane.INFORMATION_MESSAGE);
                    currentPosition = input.length();
                    input.setLength(0);
                    break;
                case "Game Won":
                    for(int i=0;i<7;i++) {
                        if(controller.getInputColors()[i]==1) {
                            fields[currentLine][i].setBackground(Color.green);
                        }
                        else if(controller.getInputColors()[i]==2) {
                            fields[currentLine][i].setBackground(Color.orange);
                        }
                        else if(controller.getInputColors()[i]==3) {
                            fields[currentLine][i].setBackground(Color.gray);
                        }
                    }
                    for(int i=0;i<10;i++) {
                        if(controller.getKeyboardColors().get(numberKeys[i].charAt(0))==1) {
                            buttons[0][i].setBackground(Color.green);
                        }
                        else if(controller.getKeyboardColors().get(numberKeys[i].charAt(0))==2) {
                            buttons[0][i].setBackground(Color.orange);
                        }
                        else if(controller.getKeyboardColors().get(numberKeys[i].charAt(0))==3) {
                            buttons[0][i].setBackground(Color.gray);
                        }
                    }
                    for(int i=1;i<6;i++) {
                        if(controller.getKeyboardColors().get(operationKeys[i].charAt(0))==1) {
                            buttons[1][i].setBackground(Color.green);
                        }
                        else if(controller.getKeyboardColors().get(operationKeys[i].charAt(0))==2) {
                            buttons[1][i].setBackground(Color.orange);
                        }
                        else if(controller.getKeyboardColors().get(operationKeys[i].charAt(0))==3) {
                            buttons[1][i].setBackground(Color.gray);
                        }
                    }
                    JOptionPane.showMessageDialog(frame, "Congratulations! You won the game!", "Game Won", JOptionPane.INFORMATION_MESSAGE);
                    for (int i = 0; i < INumberleModel.MAX_ATTEMPTS; i++) {
                        for (int j = 0; j < 7; j++) {
                            fields[i][j].setText("");
                            fields[i][j].setBackground(null);
                        }
                    }
                    for (int i = 0; i < 2; i++) {
                        for (int j = 0; j < 10; j++) {
                            if(i==0||(i==1&&j<6)) {
                                buttons[i][j].setBackground(new Color(43, 220, 206));
                            }
                        }
                    }
                    currentPosition = 0;
                    currentLine = 0;
                    input.setLength(0);
                    buttons[1][7].setEnabled(false);
                    controller.startNewGame();
                    target.setText(controller.getTargetWord());
                    break;
                case "Game Over":
                    for(int i=0;i<7;i++) {
                        if(controller.getInputColors()[i]==1) {
                            fields[currentLine][i].setBackground(Color.green);
                        }
                        else if(controller.getInputColors()[i]==2) {
                            fields[currentLine][i].setBackground(Color.orange);
                        }
                        else if(controller.getInputColors()[i]==3) {
                            fields[currentLine][i].setBackground(Color.gray);
                        }
                    }
                    for(int i=0;i<10;i++) {
                        if(controller.getKeyboardColors().get(numberKeys[i].charAt(0))==1) {
                            buttons[0][i].setBackground(Color.green);
                        }
                        else if(controller.getKeyboardColors().get(numberKeys[i].charAt(0))==2) {
                            buttons[0][i].setBackground(Color.orange);
                        }
                        else if(controller.getKeyboardColors().get(numberKeys[i].charAt(0))==3) {
                            buttons[0][i].setBackground(Color.gray);
                        }
                    }
                    for(int i=1;i<6;i++) {
                        if(controller.getKeyboardColors().get(operationKeys[i].charAt(0))==1) {
                            buttons[1][i].setBackground(Color.green);
                        }
                        else if(controller.getKeyboardColors().get(operationKeys[i].charAt(0))==2) {
                            buttons[1][i].setBackground(Color.orange);
                        }
                        else if(controller.getKeyboardColors().get(operationKeys[i].charAt(0))==3) {
                            buttons[1][i].setBackground(Color.gray);
                        }
                    }
                    JOptionPane.showMessageDialog(frame, message + "! No Attempts, The correct equation was: " + controller.getTargetWord(), "Game Over", JOptionPane.INFORMATION_MESSAGE);
                    for (int i = 0; i < INumberleModel.MAX_ATTEMPTS; i++) {
                        for (int j = 0; j < 7; j++) {
                            fields[i][j].setText("");
                            fields[i][j].setBackground(null);
                        }
                    }
                    for (int i = 0; i < 2; i++) {
                        for (int j = 0; j < 10; j++) {
                            if(i==0||(i==1&&j<6)) {
                                buttons[i][j].setBackground(new Color(43, 220, 206));
                            }
                        }
                    }
                    currentPosition = 0;
                    currentLine = 0;
                    input.setLength(0);
                    buttons[1][7].setEnabled(false);
                    controller.startNewGame();
                    target.setText(controller.getTargetWord());
                    break;
                case "Try Again":
                    for(int i=0;i<7;i++) {
                        if(controller.getInputColors()[i]==1) {
                            fields[currentLine][i].setBackground(Color.green);
                        }
                        else if(controller.getInputColors()[i]==2) {
                            fields[currentLine][i].setBackground(Color.orange);
                        }
                        else if(controller.getInputColors()[i]==3) {
                            fields[currentLine][i].setBackground(Color.gray);
                        }
                    }
                    for(int i=0;i<10;i++) {
                        if(controller.getKeyboardColors().get(numberKeys[i].charAt(0))==1) {
                            buttons[0][i].setBackground(Color.green);
                        }
                        else if(controller.getKeyboardColors().get(numberKeys[i].charAt(0))==2) {
                            buttons[0][i].setBackground(Color.orange);
                        }
                        else if(controller.getKeyboardColors().get(numberKeys[i].charAt(0))==3) {
                            buttons[0][i].setBackground(Color.gray);
                        }
                    }
                    for(int i=1;i<6;i++) {
                        if(controller.getKeyboardColors().get(operationKeys[i].charAt(0))==1) {
                            buttons[1][i].setBackground(Color.green);
                        }
                        else if(controller.getKeyboardColors().get(operationKeys[i].charAt(0))==2) {
                            buttons[1][i].setBackground(Color.orange);
                        }
                        else if(controller.getKeyboardColors().get(operationKeys[i].charAt(0))==3) {
                            buttons[1][i].setBackground(Color.gray);
                        }
                    }
                    currentPosition = 0;
                    currentLine = INumberleModel.MAX_ATTEMPTS - controller.getRemainingAttempts();
                    input.setLength(0);
                    buttons[1][7].setEnabled(true);
                    break;
            }
        }
    }
}