import Controller.*;
import Model.*;

import java.util.Scanner;


public class CLIApp {
    public static void main(String[] args) {
        INumberleModel model = new NumberleModel();
        NumberleController controller = new NumberleController(model);

        try (Scanner scanner = new Scanner(System.in)) {
            controller.startNewGame();
            System.out.println("Welcome to Numberle - CLI Version");
            System.out.println("You have " + controller.getRemainingAttempts() + " attempts to guess the right equation. Good luck!");

            while (!controller.isGameOver()) {
                System.out.println("\nEnter your guess (7 characters long equation): ");
                String input = scanner.nextLine();

                controller.processInput(input);

                if (controller.isGameOver()) {
                    if (controller.isGameWon()) {
                        System.out.println("Congratulations! You've guessed the equation correctly.");
                    } else {
                        System.out.println("Game Over! The correct equation was: " + controller.getTargetWord());
                    }
                } else {
                    System.out.println("Try again. You have " + controller.getRemainingAttempts() + " attempts left.");
                }
            }
        }
    }
}
