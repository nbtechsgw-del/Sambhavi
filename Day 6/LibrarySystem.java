import java.util.*;

// Book Class
class Book {
    int id;
    String title;
    boolean isIssued;
    Date dueDate;

    Book(int id, String title) {
        this.id = id;
        this.title = title;
        this.isIssued = false;
        this.dueDate = null;
    }
}

// Library Class
class Library {
    ArrayList<Book> books = new ArrayList<>();

    // Add Book
    void addBook(int id, String title) {
        books.add(new Book(id, title));
        System.out.println("Book added successfully!");
    }

    // Display Books
    void displayBooks() {
        for (Book b : books) {
            System.out.println("ID: " + b.id + ", Title: " + b.title +
                    ", Issued: " + b.isIssued +
                    (b.isIssued ? ", Due Date: " + b.dueDate : ""));
        }
    }

    // Issue Book
    void issueBook(int id) {
        for (Book b : books) {
            if (b.id == id && !b.isIssued) {
                b.isIssued = true;

                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DATE, 7); // due in 7 days
                b.dueDate = cal.getTime();

                System.out.println("Book issued! Due date: " + b.dueDate);
                return;
            }
        }
        System.out.println("Book not available!");
    }

    // Return Book
    void returnBook(int id) {
        for (Book b : books) {
            if (b.id == id && b.isIssued) {
                b.isIssued = false;

                Date today = new Date();

                if (today.after(b.dueDate)) {
                    System.out.println("Late return! Fine applicable.");
                } else {
                    System.out.println("Returned on time.");
                }

                b.dueDate = null;
                return;
            }
        }
        System.out.println("Invalid book ID or not issued.");
    }
}

// Main Class
public class LibrarySystem {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Library lib = new Library();

        while (true) {
            System.out.println("\n1. Add Book\n2. Display Books\n3. Issue Book\n4. Return Book\n5. Exit");
            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    System.out.print("Enter ID: ");
                    int id = sc.nextInt();
                    sc.nextLine(); // consume newline
                    System.out.print("Enter Title: ");
                    String title = sc.nextLine();
                    lib.addBook(id, title);
                    break;

                case 2:
                    lib.displayBooks();
                    break;

                case 3:
                    System.out.print("Enter Book ID to issue: ");
                    lib.issueBook(sc.nextInt());
                    break;

                case 4:
                    System.out.print("Enter Book ID to return: ");
                    lib.returnBook(sc.nextInt());
                    break;

                case 5:
                    System.out.println("Exiting...");
                    return;

                default:
                    System.out.println("Invalid choice!");
            }
        }
    }
}