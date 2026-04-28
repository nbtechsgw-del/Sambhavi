import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        LibraryManager library = new LibraryManager("library_data.dat");
        Scanner scanner = new Scanner(System.in);

        while (true) {
            showMenu();
            System.out.print("Enter your choice: ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    String title = getNonEmptyInput(scanner, "Enter book title: ");
                    String author = getNonEmptyInput(scanner, "Enter author name: ");
                    Book book = library.addBook(title, author);
                    System.out.println("Book added successfully with ID " + book.getBookId() + ".");
                    break;
                case "2":
                    printBooks(library.viewBooks());
                    break;
                case "3":
                    String keyword = getNonEmptyInput(scanner, "Enter title or author to search: ");
                    printBooks(library.searchBooks(keyword));
                    break;
                case "4":
                    String name = getNonEmptyInput(scanner, "Enter user name: ");
                    User user = library.registerUser(name);
                    System.out.println("User registered successfully with ID " + user.getUserId() + ".");
                    break;
                case "5":
                    printUsers(library.viewUsers());
                    break;
                case "6":
                    int borrowBookId = getIntInput(scanner, "Enter book ID: ");
                    int borrowerId = getIntInput(scanner, "Enter user ID: ");
                    System.out.println(library.borrowBook(borrowBookId, borrowerId));
                    break;
                case "7":
                    int returnBookId = getIntInput(scanner, "Enter book ID to return: ");
                    System.out.println(library.returnBook(returnBookId));
                    break;
                case "8":
                    System.out.println("Goodbye!");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice. Please select an option from 1 to 8.");
            }
        }
    }

    private static void showMenu() {
        System.out.println();
        System.out.println("Mini Library Management System");
        System.out.println("1. Add new book");
        System.out.println("2. View all books");
        System.out.println("3. Search books");
        System.out.println("4. Register user");
        System.out.println("5. View users");
        System.out.println("6. Borrow book");
        System.out.println("7. Return book");
        System.out.println("8. Exit");
        System.out.println();
    }

    private static String getNonEmptyInput(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String value = scanner.nextLine().trim();
            if (!value.isEmpty()) {
                return value;
            }
            System.out.println("Input cannot be empty. Please try again.");
        }
    }

    private static int getIntInput(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String value = scanner.nextLine().trim();
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException exception) {
                System.out.println("Please enter a valid numeric value.");
            }
        }
    }

    private static void printBooks(List<Book> books) {
        if (books.isEmpty()) {
            System.out.println("No books found.");
            return;
        }

        System.out.println();
        System.out.println("Books:");
        System.out.println("------------------------------------------------------------------------");
        for (Book book : books) {
            String status = book.isAvailable()
                ? "Available"
                : "Borrowed by user ID " + book.getBorrowedBy();
            System.out.println(
                "ID: " + book.getBookId()
                    + " | Title: " + book.getTitle()
                    + " | Author: " + book.getAuthor()
                    + " | Status: " + status
            );
        }
        System.out.println("------------------------------------------------------------------------");
    }

    private static void printUsers(List<User> users) {
        if (users.isEmpty()) {
            System.out.println("No users registered.");
            return;
        }

        System.out.println();
        System.out.println("Users:");
        System.out.println("----------------------------------------");
        for (User user : users) {
            System.out.println("ID: " + user.getUserId() + " | Name: " + user.getName());
        }
        System.out.println("----------------------------------------");
    }
}

class LibraryManager {
    private final String dataFile;
    private LibraryData data;

    public LibraryManager(String dataFile) {
        this.dataFile = dataFile;
        this.data = loadData();
    }

    public Book addBook(String title, String author) {
        Book book = new Book(data.nextBookId++, title, author);
        data.books.add(book);
        saveData();
        return book;
    }

    public List<Book> viewBooks() {
        return data.books;
    }

    public List<Book> searchBooks(String keyword) {
        String query = keyword.toLowerCase();
        List<Book> matches = new ArrayList<>();
        for (Book book : data.books) {
            if (book.getTitle().toLowerCase().contains(query)
                || book.getAuthor().toLowerCase().contains(query)) {
                matches.add(book);
            }
        }
        return matches;
    }

    public User registerUser(String name) {
        User user = new User(data.nextUserId++, name);
        data.users.add(user);
        saveData();
        return user;
    }

    public List<User> viewUsers() {
        return data.users;
    }

    public String borrowBook(int bookId, int userId) {
        Book book = findBook(bookId);
        if (book == null) {
            return "Book not found.";
        }

        User user = findUser(userId);
        if (user == null) {
            return "User not found.";
        }

        if (!book.isAvailable()) {
            if (book.getBorrowedBy() != null && book.getBorrowedBy() == userId) {
                return "This user has already borrowed the book.";
            }
            return "Book is already borrowed by user ID " + book.getBorrowedBy() + ".";
        }

        book.setAvailable(false);
        book.setBorrowedBy(userId);
        saveData();
        return "\"" + book.getTitle() + "\" borrowed successfully by " + user.getName() + ".";
    }

    public String returnBook(int bookId) {
        Book book = findBook(bookId);
        if (book == null) {
            return "Book not found.";
        }

        if (book.isAvailable()) {
            return "Book is already marked as available.";
        }

        Integer borrowerId = book.getBorrowedBy();
        book.setAvailable(true);
        book.setBorrowedBy(null);
        saveData();
        return "Book returned successfully from user ID " + borrowerId + ".";
    }

    private Book findBook(int bookId) {
        for (Book book : data.books) {
            if (book.getBookId() == bookId) {
                return book;
            }
        }
        return null;
    }

    private User findUser(int userId) {
        for (User user : data.users) {
            if (user.getUserId() == userId) {
                return user;
            }
        }
        return null;
    }

    private LibraryData loadData() {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(dataFile))) {
            Object savedData = inputStream.readObject();
            if (savedData instanceof LibraryData) {
                return (LibraryData) savedData;
            }
        } catch (IOException | ClassNotFoundException exception) {
            // Start with empty data if the file does not exist or cannot be read.
        }
        return new LibraryData();
    }

    private void saveData() {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(dataFile))) {
            outputStream.writeObject(data);
        } catch (IOException exception) {
            System.out.println("Warning: unable to save library data.");
        }
    }
}

class LibraryData implements Serializable {
    private static final long serialVersionUID = 1L;

    List<Book> books = new ArrayList<>();
    List<User> users = new ArrayList<>();
    int nextBookId = 1;
    int nextUserId = 1;
}

class Book implements Serializable {
    private static final long serialVersionUID = 1L;

    private final int bookId;
    private final String title;
    private final String author;
    private boolean available;
    private Integer borrowedBy;

    public Book(int bookId, String title, String author) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.available = true;
        this.borrowedBy = null;
    }

    public int getBookId() {
        return bookId;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public Integer getBorrowedBy() {
        return borrowedBy;
    }

    public void setBorrowedBy(Integer borrowedBy) {
        this.borrowedBy = borrowedBy;
    }
}

class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private final int userId;
    private final String name;

    public User(int userId, String name) {
        this.userId = userId;
        this.name = name;
    }

    public int getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }
}
