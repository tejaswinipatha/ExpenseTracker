import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Arrays;


class Expense {
    private String category;
    private String description;
    private double amount;

    public Expense(String category, String description, double amount) {
        this.category = category;
        this.description = description;
        this.amount = amount;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public double getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return "Category: " + category + ", Description: " + description + ", Amount: " + amount;
    }
}

public class ExpenseTracker {
    private List<Expense> expenses = new ArrayList<>();
    private final String fileName = "expenses.txt";

    public ExpenseTracker() {
        loadExpenses();
    }

    // Add a new expense
    public void addExpense(String category, String description, double amount) {
        Expense expense = new Expense(category, description, amount);
        expenses.add(expense);
        System.out.println("Expense added: " + expense);
        saveExpenses();
    }

    // View all expenses
    public void viewExpenses() {
        if (expenses.isEmpty()) {
            System.out.println("No expenses to show.");
        } else {
            for (Expense expense : expenses) {
                System.out.println(expense);
            }
        }
    }

    // Delete an expense by index
    public void deleteExpense(int index) {
        if (index >= 0 && index < expenses.size()) {
            Expense removed = expenses.remove(index);
            System.out.println("Deleted expense: " + removed);
            saveExpenses();
        } else {
            System.out.println("Invalid index.");
        }
    }

    // Display expense summary by category
    public void showExpenseSummary() {
        double total = 0;
        System.out.println("\nExpense Summary by Category:");
        for (String category : Arrays.asList("Food", "Transport", "Entertainment", "Other")) {
 
            double sum = expenses.stream()
                                 .filter(e -> e.getCategory().equalsIgnoreCase(category))
                                 .mapToDouble(Expense::getAmount)
                                 .sum();
            System.out.println(category + ": $" + sum);
            total += sum;
        }
        System.out.println("Total Spending: $" + total);
    }

    // Save expenses to a file for data persistence
    private void saveExpenses() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            for (Expense expense : expenses) {
                writer.println(expense.getCategory() + "," + expense.getDescription() + "," + expense.getAmount());
            }
        } catch (IOException e) {
            System.out.println("Error saving expenses.");
        }
    }

    // Load expenses from a file
    private void loadExpenses() {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 3) {
                    String category = data[0];
                    String description = data[1];
                    double amount = Double.parseDouble(data[2]);
                    expenses.add(new Expense(category, description, amount));
                }
            }
        } catch (IOException e) {
            System.out.println("No saved expenses found.");
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ExpenseTracker tracker = new ExpenseTracker();
        
        System.out.println("Welcome to Personal Expense Tracker!");
        while (true) {
            System.out.println("\nChoose an option:");
            System.out.println("1. Add Expense");
            System.out.println("2. View Expenses");
            System.out.println("3. Delete Expense");
            System.out.println("4. Show Expense Summary");
            System.out.println("5. Exit");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    System.out.println("Enter category (Food, Transport, Entertainment, Other):");
                    String category = scanner.nextLine();
                    System.out.println("Enter description:");
                    String description = scanner.nextLine();
                    System.out.println("Enter amount:");
                    double amount = scanner.nextDouble();
                    tracker.addExpense(category, description, amount);
                    break;

                case 2:
                    tracker.viewExpenses();
                    break;

                case 3:
                    System.out.println("Enter expense index to delete:");
                    int index = scanner.nextInt();
                    tracker.deleteExpense(index - 1); // Adjust for zero indexing
                    break;

                case 4:
                    tracker.showExpenseSummary();
                    break;

                case 5:
                    System.out.println("Exiting Expense Tracker. Goodbye!");
                    scanner.close();
                    return;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}
