package com.example.Library_Managment.Services;

import com.example.Library_Managment.model.Book;
import com.example.Library_Managment.model.BorrowHistory;
import com.example.Library_Managment.model.userregister;
import com.example.Library_Managment.repository.BorrowHistoryRepository;
import com.example.Library_Managment.repository.UserRepository;
import com.example.Library_Managment.repository.BookRepository;
import com.example.Library_Managment.security.JwtUtil;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.LocalDate;
import java.util.List;

@Service
public class BookService {
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BorrowHistoryRepository borrowHistoryRepository;
    @Autowired
    private JavaMailSender mailSender; // Injects Spring Mail Sednder to send mail.
    @Autowired
    private JwtUtil jwtUtil; //Injects helper that exctracts claims from JWT token.

    //Fetch all books
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }
    //Search books by author-name,book-name,genre
    public List<Book> searchBooks(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return bookRepository.findAll(); // if no keyword, return all books
        }
        return bookRepository.searchBooks(keyword);
    }
    // Borrow a book using JWT token
    public String borrowBook(Long bookId, String token) {
        //  Extract user info from token
        String userEmail = jwtUtil.extractUsername(token);
        Long userId = jwtUtil.extractUserId(token); //

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        if (book.isBorrowed()) {
            return "Book already borrowed!";
        }


        userregister user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        book.setBorrowed(true);
        book.setBorrowedByUserId(userId);
        book.setIssuedDate(LocalDate.now());
        book.setDueDate(LocalDate.now().plusDays(14)); // 2 weeks due date
        bookRepository.save(book);


        //**Create BorrowHistory record**
        BorrowHistory history = new BorrowHistory();
        history.setUser(user);
        history.setBook(book);
        history.setBorrowDate(LocalDate.now());
        history.setDueDate(LocalDate.now().plusDays(14));
        history.setStatus(BorrowHistory.Status.BORROWED);
        borrowHistoryRepository.save(history);
        // Send confirmation email with book details and attached PDF
        sendBorrowEmail(userEmail, book);
        return "Book borrowed successfully!";
    }



    // Send borrow confirmation email with PDF attachment


    private void sendBorrowEmail(String email, Book book) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true); // true = supports attachments

          //  String firstName = user.getFirstName(); // Assuming getter exists

            // Email recipient and subject
            helper.setTo(email);
            helper.setSubject("Confirmation Mail - " + book.getBookName());



            // Email body
            String text = "Hello,\n\nYou have succesfully borrowed " + book.getBookName() +
                    "  written By: " + book.getAuthorName() +
                    "  It's Genere is: " + book.getGenre() +
                    "\nDescription: \n" + book.getDescription() +
                    "\nIt's Issued On: " + book.getIssuedDate() +
                    "\nIt's Due On: " + book.getDueDate() + "\n\n" +
                    "Please find your book attached as a PDF.\n\n" +
                    "Happy Reading!\nLibrary Book-Worm Team";

            helper.setText(text);

            // Attach the book PDF if available
            if (book.getPdfPath() != null && !book.getPdfPath().isEmpty()) {
                // Construct path relative to resources folder
                String pdfPath = "static/"  + book.getPdfPath(); // e.g., static/Romance/Emma.pdf
                ClassPathResource pdfFile = new ClassPathResource(pdfPath);

                if (pdfFile.exists()) {
                    helper.addAttachment(pdfFile.getFilename(), pdfFile);
                    System.out.println("✅ PDF file attached: " + pdfPath);
                } else {
                    System.out.println("❌ PDF file not found: " + pdfPath);
                }
            }

            // Send the email
            mailSender.send(message);
            System.out.println("✅ Borrow confirmation email sent to: " + email);

        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send borrow email: " + e.getMessage(), e);
        }
    }
    @Scheduled(cron = "0 0 9 * * ?") // every day at 9 AM
    public void sendDueDateReminders() {
        List<Book> borrowedBooks = bookRepository.findAll();

        for (Book book : borrowedBooks) {
            if (book.isBorrowed() && book.getDueDate() != null) {
                LocalDate today = LocalDate.now();
                if (today.plusDays(1).isEqual(book.getDueDate())) {
                    sendReminderEmail(book.getBorrowedByUserId(), book);
                }
            }
        }
    }

    private void sendReminderEmail(Long userId, Book book) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, false);

            // ⚠️ TODO: Replace with logic to fetch user email using userId from DB
          /*  helper.setTo("useremail@example.com");*/
            userregister user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            helper.setTo(user.getEmail());


            helper.setSubject("Book Due Date Reminder - " + book.getBookName());
            helper.setText("Hello,\n\nThis is a reminder that your borrowed book '"
                    + book.getBookName() + "' is due today (" + book.getDueDate() + ").\n\n"
                    + "Thank you,\nLibrary Team");

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send reminder email: " + e.getMessage());
        }


    }

}
















//Not required for now.

/*    private void sendAccessRevokedEmail(Book book) {
        try {
            userregister user = userRepository.findById(book.getBorrowedByUserId())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, false);

            helper.setTo(user.getEmail()); // ✅ dynamic email
            helper.setSubject("Access Revoked - " + book.getBookName());
            helper.setText("Hello " + user.getFirstName() + ",\n\nYour access to the borrowed book '"
                    + book.getBookName() + "' has been revoked as it is past the due date ("
                    + book.getDueDate() + ").\n\nPlease contact the library to renew your borrowing period.\n\n"
                    + "Thank you,\nLibrary Team");

            mailSender.send(message);
            System.out.println("⚠️ Access revoked email sent for book: " + book.getBookName());

        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send access revoked email: " + e.getMessage());
        }
    }

    public void returnBook(Long bookId, Long userId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));
        userregister user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Find active borrow history
        BorrowHistory history = borrowHistoryRepository.findByUser(user).stream()
                .filter(h -> h.getBook().getId().equals(bookId) && h.getStatus() == BorrowHistory.Status.BORROWED)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No active borrow record found"));

        // Update history
        history.setReturnDate(LocalDate.now());
        history.setStatus(BorrowHistory.Status.RETURNED);
        borrowHistoryRepository.save(history);

        // Update book
        book.setBorrowed(false);
        book.setBorrowedByUserId(null);
        bookRepository.save(book);
    }



    */









