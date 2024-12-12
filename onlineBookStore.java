package Project1; 
 
import java.awt.*; 
import java.awt.event.*; 
import javax.swing.*; 
import javax.swing.border.EmptyBorder; 
import java.util.*;
import java.io.*;
 
public class onlineBookStore extends JFrame { 
 
    private static final long serialVersionUID = 1L; 
    private JPanel contentPane; 
    private JList<String> bookList; 
    private DefaultListModel<String> cartModel; 
    private JTextArea receiptArea; 
 
    private Map<String, Double> books; 
    private Map<String, ArrayList<String>> reviews; 
 
    private double totalPrice; 
    private String userName; 
    private String userPhone; 
    private String password = null;
 
    public static void main(String[] args) { 
        EventQueue.invokeLater(() -> { 
            try { 
                onlineBookStore frame = new onlineBookStore(); 
                frame.setVisible(true); 
            } 
            catch (Exception e) { 
                e.printStackTrace(); 
            } 
        }); 
    }
 
    public onlineBookStore() {
        setTitle("Online Bookstore");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 800, 600);
        contentPane = new JPanel();
        contentPane.setBackground(new Color(230, 230, 250));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        showWelcomePopup(); //show the welcome popup image

        showLoginDialog(); //show login dialog

        books = new LinkedHashMap<>();
        reviews = new LinkedHashMap<>();
        totalPrice = 0.0;
        initializeBooks();

        JLabel lblBooks = new JLabel("Available Books:");
        lblBooks.setFont(new Font("Tahoma", Font.PLAIN, 12));
        lblBooks.setBounds(20, 20, 200, 20);
        contentPane.add(lblBooks);

        bookList = new JList<>(books.keySet().toArray(new String[0]));
        bookList.setBackground(new Color(210, 230, 255));
        JScrollPane scrollPaneBooks = new JScrollPane(bookList);
        scrollPaneBooks.setBounds(20, 50, 300, 300);
        contentPane.add(scrollPaneBooks);

        JButton btnAddToCart = new JButton("Add to Cart");
        btnAddToCart.setBackground(new Color(70, 130, 180));
        btnAddToCart.setForeground(Color.WHITE);
        btnAddToCart.setBounds(20, 370, 150, 30);
        btnAddToCart.addActionListener(e -> addToCart());
        contentPane.add(btnAddToCart);

        JLabel lblCart = new JLabel("Shopping Cart:");
        lblCart.setFont(new Font("Tahoma", Font.PLAIN, 12));
        lblCart.setBounds(350, 20, 200, 20);
        contentPane.add(lblCart);

        cartModel = new DefaultListModel<>();
        JList<String> cartList = new JList<>(cartModel);
        cartList.setBackground(new Color(240, 248, 255));
        JScrollPane scrollPaneCart = new JScrollPane(cartList);
        scrollPaneCart.setBounds(350, 50, 300, 300);
        contentPane.add(scrollPaneCart);

        JButton btnPurchase = new JButton("Purchase");
        btnPurchase.setForeground(Color.WHITE);
        btnPurchase.setBackground(new Color(0, 100, 0));
        btnPurchase.setBounds(582, 370, 150, 53);
        btnPurchase.addActionListener(e -> purchaseBooks());
        contentPane.add(btnPurchase);

        JButton btnAddReview = new JButton("Add Review");
        btnAddReview.setForeground(Color.WHITE);
        btnAddReview.setBackground(new Color(34, 139, 34));
        btnAddReview.setBounds(208, 370, 150, 30);
        btnAddReview.addActionListener(e -> addReview());
        contentPane.add(btnAddReview);

        JButton btnViewReviews = new JButton("View Reviews");
        btnViewReviews.setForeground(Color.WHITE);
        btnViewReviews.setBackground(new Color(0, 128, 128));
        btnViewReviews.setBounds(400, 370, 150, 30);
        btnViewReviews.addActionListener(e -> viewReviews());
        contentPane.add(btnViewReviews);

        JLabel lblReceipt = new JLabel("Receipt:");
        lblReceipt.setFont(new Font("Tahoma", Font.PLAIN, 12));
        lblReceipt.setBounds(20, 420, 200, 20);
        contentPane.add(lblReceipt);

        receiptArea = new JTextArea();
        receiptArea.setBackground(new Color(224, 255, 255));
        receiptArea.setForeground(Color.DARK_GRAY);
        receiptArea.setEditable(false);
        JScrollPane scrollPaneReceipt = new JScrollPane(receiptArea);
        scrollPaneReceipt.setBounds(20, 450, 740, 100);
        contentPane.add(scrollPaneReceipt);

        JButton btnRemoveItem = new JButton("Remove Item");
        btnRemoveItem.setBackground(new Color(112, 128, 144));
        btnRemoveItem.setForeground(Color.WHITE);
        btnRemoveItem.setFont(new Font("Tahoma", Font.PLAIN, 10));
        btnRemoveItem.setBounds(670, 126, 103, 30);
        btnRemoveItem.addActionListener(e -> removeItemFromCart(cartList));
        contentPane.add(btnRemoveItem);

        try {
            ImageIcon logoIcon = new ImageIcon("C:\\Users\\User\\Pictures\\bookstorelogo.jpg"); //bookstore logo
            Image scaledLogo = logoIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            JLabel logoLabel = new JLabel(new ImageIcon(scaledLogo));
            logoLabel.setBounds(660, 10, 120, 100);
            contentPane.add(logoLabel);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading logo image: " + e.getMessage(), "Image Error", JOptionPane.ERROR_MESSAGE);
        }
    }


 
    private void showWelcomePopup() { 
        JDialog welcomeDialog = new JDialog(this, "Welcome", true); 
        welcomeDialog.setSize(600, 400); 
        welcomeDialog.setLocationRelativeTo(null); 
        welcomeDialog.getContentPane().setLayout(new BorderLayout()); 
 
        ImageIcon bannerIcon = new ImageIcon("C:\\Users\\User\\Pictures\\welcome.jpg");
        Image scaledImage = bannerIcon.getImage().getScaledInstance(580, 300, Image.SCALE_SMOOTH); 
        JLabel bannerLabel = new JLabel(new ImageIcon(scaledImage)); 
 
        welcomeDialog.getContentPane().add(bannerLabel, BorderLayout.CENTER); //add the image to the dialog 
 
        JButton continueButton = new JButton("Continue"); //adding a "Continue" button to close the popup 
        continueButton.setFont(new Font("Arial", Font.BOLD, 14)); 
        continueButton.addActionListener(e -> welcomeDialog.dispose()); 
        welcomeDialog.getContentPane().add(continueButton, BorderLayout.SOUTH); 
 
        welcomeDialog.setVisible(true); //showing the dialog 
    }
    
    private void showLoginDialog() { 
        if (password == null) { //check if the password is not set 
            password = JOptionPane.showInputDialog(this, "Set your password:"); 
            while (password == null || password.trim().isEmpty()) { 
                password = JOptionPane.showInputDialog(this, "Password cannot be empty. Set your password:"); 
            } 
            JOptionPane.showMessageDialog(this, "Password set successfully!"); 
        } 
 
        String enteredPassword = JOptionPane.showInputDialog(this, "Enter the password:"); 
        while (!enteredPassword.equals(password)) { 
            enteredPassword = JOptionPane.showInputDialog(this, "Incorrect password. Try again:"); 
        } 
 
        userName = JOptionPane.showInputDialog(this, "Please enter your name:"); 
        if (userName == null || userName.trim().isEmpty()) { 
            userName = "Guest"; //default name at receipt
        } 
 
        boolean validPhone = false;
        while (!validPhone) { 
            try { 
                userPhone = JOptionPane.showInputDialog(this, "Please enter your phone number:"); 
                if (userPhone == null || userPhone.trim().isEmpty()) { 
                    userPhone = "N/A"; 
                    validPhone = true; //allow empty phone num as a valid option 
                } 
                else { 
                    Long.parseLong(userPhone); 
                    validPhone = true; 
                } 
            } 
            catch (NumberFormatException e) { 
                JOptionPane.showMessageDialog(this, 
                        "Invalid phone number. Please enter numbers only.", 
                        "Input Error", 
                        JOptionPane.ERROR_MESSAGE); 
            } 
        } 
 
        saveUserInfoToFile(); //save user info to the file
    }
 
    private void initializeBooks() { 
        books = new LinkedHashMap<>(); 
        reviews = new LinkedHashMap<>(); 
 
        books.put("Diary of a wimpy kid - ($10)", 10.0); 
        books.put("Cars 2 - ($10)", 10.0); 
        books.put("Transformers: Age of Ultron - ($12)", 12.0); 
        books.put("Avengers: Infinity War - ($13)", 13.0); 
        books.put("Lord of The Rings - ($15)", 15.0); 
        books.put("Batman: The Dark Knight - ($15)", 15.0); 
        books.put("Iron Man 3 - ($16)", 16.0); 
        books.put("The Hobbit - ($17)", 17.0); 
        books.put("Harry Potter and The Deathly Hallows - ($17)", 17.0); 
        books.put("The Hunger Games - ($18)", 18.0); 
        books.put("Spiderman - ($20)", 20.0); 
        books.put("Wizard Of Oz - ($20)", 20.0); 
        books.put("Rapunzel - ($23)", 23.0); 
        books.put("Percy Jackson and The Lightning Thief - ($25)", 25.0); 
        books.put("Jurassic Park - ($25)", 25.0);
        
        for (String book : books.keySet()) { 
            reviews.put(book, new ArrayList<>()); //arraylist easier to use
        } 
    } 
 
    private void saveUserInfoToFile() { 
        String filePath = "C:\\Users\\User\\Documents\\usersJava.txt"; 
        try (FileWriter writer = new FileWriter(filePath, true)) { //open file in append mode 
            writer.write("Name: " + userName + ", Phone: " + userPhone + System.lineSeparator()); //write details into file
        } 
        catch (IOException e) { 
            JOptionPane.showMessageDialog(this, 
                    "Error saving user information: " + e.getMessage(), 
                    "File Error", 
                    JOptionPane.ERROR_MESSAGE); 
        } 
    } 
 
    private void addToCart() { 
        String selectedBook = bookList.getSelectedValue(); 
        if (selectedBook != null) { 
            cartModel.addElement(selectedBook); //add books to cart
            totalPrice += books.get(selectedBook); //sum price of books
            JOptionPane.showMessageDialog(this, selectedBook + " added to cart."); 
        } 
        else { 
            JOptionPane.showMessageDialog(this, "Please select a book to add to the cart.", "Error", JOptionPane.ERROR_MESSAGE); 
        } 
    } 
 
    private void purchaseBooks() { 
        if (cartModel.isEmpty()) { 
            JOptionPane.showMessageDialog(this, "Your cart is empty. Please add books to your cart.", "Error", JOptionPane.ERROR_MESSAGE); 
            return; 
        } 
 
        StringBuilder receipt = new StringBuilder("Customer Name: ").append(userName).append("\n"); 
        receipt.append("Phone Number: ").append(userPhone).append("\n\n"); 
        receipt.append("Books Purchased:\n"); 
        for (int i = 0; i < cartModel.size(); i++) { 
            receipt.append(cartModel.get(i)).append("\n"); 
        } 
        receipt.append("\nTotal Price: $").append(String.format("%.2f", totalPrice)); 
        receiptArea.setText(receipt.toString()); 
        cartModel.clear(); 
        totalPrice = 0.0; 
 
        JOptionPane.showMessageDialog(this, "Thank you for your purchase!"); 
    } 
 
    private void removeItemFromCart(JList<String> cartList) {  //maybe want to add this?
        String selectedBook = cartList.getSelectedValue(); 
        if (selectedBook != null) { 
            cartModel.removeElement(selectedBook); //remove book from cart
            totalPrice -= books.get(selectedBook);  //minus the price of book removed
            JOptionPane.showMessageDialog(this, selectedBook + " removed from the cart."); 
        } 
        else { 
            JOptionPane.showMessageDialog(this, "Please select a book to remove from the cart.", "Error", JOptionPane.ERROR_MESSAGE); 
        } 
    }
    
    private void addReview() { 
        String selectedBook = bookList.getSelectedValue(); 
        if (selectedBook != null) { 
            String review = JOptionPane.showInputDialog(this, "Enter your review for " + selectedBook + ":"); 
            if (review != null && !review.trim().isEmpty()) { 
                reviews.get(selectedBook).add(review); 
                JOptionPane.showMessageDialog(this, "Review added successfully."); 
            } 
            else { 
                JOptionPane.showMessageDialog(this, "Review cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE); 
            } 
        } 
        else { 
            JOptionPane.showMessageDialog(this, "Please select a book to review.", "Error", JOptionPane.ERROR_MESSAGE); 
        }
    } 
 
    private void viewReviews() {
        String selectedBook = bookList.getSelectedValue(); 
        if (selectedBook != null) { 
            ArrayList<String> bookReviews = reviews.get(selectedBook); //array list containing book reviews
            if (bookReviews.isEmpty()) { 
                JOptionPane.showMessageDialog(this, "No reviews available for " + selectedBook + ".", "Reviews", JOptionPane.INFORMATION_MESSAGE); 
            } 
            else { 
                StringBuilder allReviews = new StringBuilder("Reviews for " + selectedBook + ":\n"); 
                for (String review : bookReviews) { 
                    allReviews.append("- ").append(review).append("\n");
                } 
                JOptionPane.showMessageDialog(this, allReviews.toString(), "Reviews", JOptionPane.INFORMATION_MESSAGE); 
            } 
        } 
        else { 
            JOptionPane.showMessageDialog(this, "Please select a book to view reviews.", "Error", JOptionPane.ERROR_MESSAGE); 
        } 
    }
}