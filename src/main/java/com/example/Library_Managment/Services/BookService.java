package com.example.Library_Managment.Services;

import com.example.Library_Managment.model.Book;
import com.example.Library_Managment.model.userregister;
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
    private JavaMailSender mailSender;

    @Autowired
    private JwtUtil jwtUtil; // ✅ Inject JwtUtil to extract email & userId from token

    /**
     * Fetch all books
     */
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    /**
     * Borrow a book using JWT token
     */
    public String borrowBook(Long bookId, String token) {
        // ✅ Extract user info from token
        String userEmail = jwtUtil.extractUsername(token);
        Long userId = jwtUtil.extractUserId(token); // You’ll need to implement this in JwtUtil if not already done

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        if (book.isBorrowed()) {
            return "Book already borrowed!";
        }

        book.setBorrowed(true);
        book.setBorrowedByUserId(userId);
        book.setIssuedDate(LocalDate.now());
        book.setDueDate(LocalDate.now().plusDays(14)); // 2 weeks due date
        bookRepository.save(book);

        // ✅ Send confirmation email with book details and attached PDF
        sendBorrowEmail(userEmail, book);
        return "Book borrowed successfully!";
    }

    /**
     * Send borrow confirmation email with PDF attachment
     */
 /*   private void sendBorrowEmail(String email, Book book) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true); // true = supports attachments

            helper.setTo(email);
            helper.setSubject("Confirmation Mail " + book.getBookName());

             String text = "Hello,\n\nYou have borrowed   " + book.getBookName() + "\n"
                    + "Written By : " + book.getAuthorName() + "\n"
                    + "It's Genere is: " + book.getGenre() + "\n"
                    + "Description: " + book.getDescription() + "\n\n"
                    + "Issued On: " + book.getIssuedDate() + "\n"
                    + "Its Due On: " + book.getDueDate() + "\n\n"
                    + "Please find your book attached as a PDF.\n\n"
                    + "Happy Reading!\nLibrary Book-Worm Team";

            helper.setText(text);


            // 🧾 Attach the book PDF if available
            if (book.getPdfPath() != null) {
                ClassPathResource pdfFile = new ClassPathResource(book.getPdfPath());
              //  File pdfFile = new File(book.getPdfPath());
                System.out.println("Exists? " + pdfFile.exists());
                System.out.println("Filename: " + pdfFile.getFilename());
                  //F
                if (pdfFile.exists()) {
                    System.out.println("Exists? " + pdfFile.exists());
                    helper.addAttachment(pdfFile.getFilename(), pdfFile);
                }
                else {
                    System.out.println("PDF file not found: " + book.getPdfPath());
                }
            }

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send borrow email: " + e.getMessage());
        }
    }*/

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


    /**
     * Daily scheduled reminder email for due date
     */
    @Scheduled(cron = "0 0 9 * * ?") // Every day at 9:00 AM
    public void sendDueDateReminders() {
        List<Book> borrowedBooks = bookRepository.findAll().stream()
                .filter(Book::isBorrowed)
                .filter(book -> LocalDate.now().isEqual(book.getDueDate()))
                .toList();

        for (Book book : borrowedBooks) {
            sendReminderEmail(book.getBorrowedByUserId(), book);
        }
    }

    private void sendReminderEmail(Long userId, Book book) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, false);

            // ⚠️ TODO: Replace with logic to fetch user email using userId from DB
            helper.setTo("useremail@example.com");
            helper.setSubject("Book Due Date Reminder - " + book.getBookName());
            helper.setText("Hello,\n\nThis is a reminder that your borrowed book '"
                    + book.getBookName() + "' is due today (" + book.getDueDate() + ").\n\n"
                    + "Please return it to avoid late fees.\n\n"
                    + "Thank you,\nLibrary Team");

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send reminder email: " + e.getMessage());
        }
    }


       public List<Book> searchBooks(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return bookRepository.findAll(); // if no keyword, return all books
        }
        return bookRepository.searchBooks(keyword);
    }
}














/*
// BookService.java

package com.example.Library_Managment.Services;

import com.example.Library_Managment.model.Book;
import com.example.Library_Managment.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import java.io.File;
import java.time.LocalDate;
import java.util.List;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private JavaMailSender mailSender;

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public String borrowBook(Long bookId, Long userId, String userEmail) {
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new RuntimeException("Book not found"));
        if (book.isBorrowed()) {
            return "Book already borrowed!";
        }

        book.setBorrowed(true);
        book.setBorrowedByUserId(userId);
        book.setIssuedDate(LocalDate.now());
        book.setDueDate(LocalDate.now().plusDays(14)); // 2 weeks due date
        bookRepository.save(book);

        // Send Email
        sendBorrowEmail(userEmail, book);
        return "Book borrowed successfully!";
    }

    private void sendBorrowEmail(String email, Book book) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true); // true = supports attachments

            helper.setTo(email);
            helper.setSubject("Book Borrow Confirmation - " + book.getBookName());

            String text = "Hello,\n\nYou have borrowed: " + book.getBookName() + "\n"
                    + "Author: " + book.getAuthorName() + "\n"
                    + "Genre: " + book.getGenre() + "\n"
                    + "Description: " + book.getDescription() + "\n\n"
                    + "Issued Date: " + book.getIssuedDate() + "\n"
                    + "Due Date: " + book.getDueDate() + "\n\n"
                    + "Please find your book attached as a PDF.\n\n"
                    + "Happy Reading!\nLibrary Team";

            helper.setText(text);

            // 🧾 Attach the book PDF if available
            if (book.getPdfPath() != null) {
                File pdfFile = new File(book.getPdfPath());
                if (pdfFile.exists()) {
                    helper.addAttachment(pdfFile.getName(), pdfFile);
                }
            }

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send borrow email: " + e.getMessage());
        }





    }

    // 🕒 Step 3: Reminder email scheduler — runs daily at 9 AM
    @Scheduled(cron ="0 0 9 * * ?") // Every day at 9:00 AM
    public void sendDueDateReminders () {
        List<Book> borrowedBooks = bookRepository.findAll().stream()
                .filter(Book::isBorrowed)
                .filter(book -> LocalDate.now().isEqual(book.getDueDate()))
                .toList();

        for (Book book : borrowedBooks) {
            // You’ll need user email (either store it in Book or fetch via userId)
            // For now, assuming we store email in Book for simplicity.
            sendReminderEmail(book.getBorrowedByUserId(), book);
        }
    }

    private void sendReminderEmail (Long userId, Book book){
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, false);

            helper.setTo("useremail@example.com"); // ⚠️ Replace with logic to get email from userId
            helper.setSubject("Book Due Date Reminder - " + book.getBookName());
            helper.setText("Hello,\n\nThis is a reminder that your borrowed book '"
                    + book.getBookName() + "' is due today (" + book.getDueDate() + ").\n\n"
                    + "Please return it to avoid late fees.\n\n"
                    + "Thank you,\nLibrary Team");

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send reminder email: " + e.getMessage());
        }
    }
}
*/
