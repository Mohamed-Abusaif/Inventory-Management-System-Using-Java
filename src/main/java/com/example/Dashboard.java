package com.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List; // Added import for List

public class Dashboard {

    // Define a custom cell renderer for the Product objects
    static class ProductCellRenderer extends DefaultListCellRenderer {
        private static final Color SELECTED_BACKGROUND_COLOR = new Color(184, 207, 229);
        private static final Color SELECTED_TEXT_COLOR = Color.BLACK;
        private static final Color UNSELECTED_BACKGROUND_COLOR = Color.WHITE;
        private static final Color UNSELECTED_TEXT_COLOR = Color.BLACK;
        private static final Font PRODUCT_NAME_FONT = new Font(Font.SANS_SERIF, Font.BOLD, 14);
        private static final Font PRODUCT_DESCRIPTION_FONT = new Font(Font.SANS_SERIF, Font.PLAIN, 12);

        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
                boolean cellHasFocus) {
            JPanel panel = new JPanel(new BorderLayout());
            panel.setBackground(isSelected ? SELECTED_BACKGROUND_COLOR : UNSELECTED_BACKGROUND_COLOR);
            panel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

            if (value instanceof Product) {
                Product product = (Product) value;

                // Panel to hold product details
                JPanel detailsPanel = new JPanel(new GridLayout(2, 1));
                detailsPanel.setBackground(isSelected ? SELECTED_BACKGROUND_COLOR : UNSELECTED_BACKGROUND_COLOR);

                // Product name label
                JLabel nameLabel = new JLabel("Product Name: " + product.getName());
                nameLabel.setFont(PRODUCT_NAME_FONT);
                nameLabel.setForeground(isSelected ? SELECTED_TEXT_COLOR : UNSELECTED_TEXT_COLOR);

                // Product description label
                JLabel descriptionLabel = new JLabel("Description: " + product.getDescription());
                descriptionLabel.setFont(PRODUCT_DESCRIPTION_FONT);
                descriptionLabel.setForeground(isSelected ? SELECTED_TEXT_COLOR : UNSELECTED_TEXT_COLOR);

                detailsPanel.add(nameLabel);
                detailsPanel.add(descriptionLabel);

                // Panel to hold quantity and price
                JPanel infoPanel = new JPanel(new GridLayout(1, 2));
                infoPanel.setBackground(isSelected ? SELECTED_BACKGROUND_COLOR : UNSELECTED_BACKGROUND_COLOR);

                // Quantity label
                JLabel quantityLabel = new JLabel("Quantity: " + product.getQuantity());
                quantityLabel.setFont(PRODUCT_DESCRIPTION_FONT);
                quantityLabel.setForeground(isSelected ? SELECTED_TEXT_COLOR : UNSELECTED_TEXT_COLOR);

                // Price label
                JLabel priceLabel = new JLabel("Price: $" + product.getPrice());
                priceLabel.setFont(PRODUCT_DESCRIPTION_FONT);
                priceLabel.setForeground(Color.RED); // Set price color to red

                infoPanel.add(quantityLabel);
                infoPanel.add(priceLabel);

                panel.add(detailsPanel, BorderLayout.CENTER);
                panel.add(infoPanel, BorderLayout.SOUTH);
            }

            return panel;
        }
    }

    public static void main(String[] args) {
        // Login Dialog
        JTextField usernameField = new JTextField(10);
        JPasswordField passwordField = new JPasswordField(10);

        JPanel loginPanel = new JPanel(new GridLayout(2, 2));
        loginPanel.add(new JLabel("Username:"));
        loginPanel.add(usernameField);
        loginPanel.add(new JLabel("Password:"));
        loginPanel.add(passwordField);

        int loginResult = JOptionPane.showConfirmDialog(null, loginPanel, "Login", JOptionPane.OK_CANCEL_OPTION);
        if (loginResult != JOptionPane.OK_OPTION) {
            return; // User canceled login
        }

        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        // Authenticate user
        if (UserAuthentication.authenticateUser(username, password)) {
            // User authenticated, proceed to dashboard
            launchDashboard();
        } else {
            // Invalid username or password
            JOptionPane.showMessageDialog(null, "Invalid username or password", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void launchDashboard() {
        final DefaultListModel<Product> productListModel = new DefaultListModel<>(); // Declare as final
        final JList<Product> productList = new JList<>(); // Declare as final
        loadProducts(productListModel);

        JFrame frame = new JFrame("Inventory Management System");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // Title Label
        JLabel titleLabel = new JLabel("Inventory Management System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        panel.add(titleLabel, BorderLayout.NORTH);

        // Buttons Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        // Add Product Button
        JButton addProductButton = new JButton("Add Product");
        addProductButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open Add Product dialog
                showAddProductDialog(frame, productListModel); // Pass productListModel
            }
        });
        buttonPanel.add(addProductButton);

        // Edit Product Button
        JButton editProductButton = new JButton("Edit Product");
        editProductButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get selected product
                Product selectedProduct = productList.getSelectedValue();
                if (selectedProduct == null) {
                    JOptionPane.showMessageDialog(frame, "Please select a product to edit.");
                    return;
                }
                // Open edit product dialog
                showEditProductDialog(frame, selectedProduct, productListModel);
            }
        });
        buttonPanel.add(editProductButton);

        // View Inventory Button
        JButton viewInventoryButton = new JButton("View Inventory");
        viewInventoryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showInventory(frame);
            }
        });
        buttonPanel.add(viewInventoryButton);

        // Product List Panel
        productList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        productList.setModel(productListModel);

        // Set the custom cell renderer
        productList.setCellRenderer(new ProductCellRenderer());

        JScrollPane scrollPane = new JScrollPane(productList);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Delete Product Button
        JButton deleteProductButton = new JButton("Delete Product");
        deleteProductButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Delete selected product
                int selectedIndex = productList.getSelectedIndex();
                if (selectedIndex != -1) {
                    Product selectedProduct = productListModel.getElementAt(selectedIndex);
                    int option = JOptionPane.showConfirmDialog(frame, "Are you sure you want to delete this product?",
                            "Confirm Deletion", JOptionPane.YES_NO_OPTION);
                    if (option == JOptionPane.YES_OPTION) {
                        deleteProduct(selectedProduct, productListModel); // Pass productListModel
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "Please select a product to delete.");
                }
            }
        });
        buttonPanel.add(deleteProductButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        frame.add(panel);
        frame.setVisible(true);
    }

    private static void loadProducts(DefaultListModel<Product> productListModel) {
        productListModel.clear();
        List<Product> products = ProductDAO.getAllProducts();
        for (Product product : products) {
            productListModel.addElement(product);
        }
    }

    private static void deleteProduct(Product product, DefaultListModel<Product> productListModel) {
        ProductDAO.deleteProduct(product.getId());
        loadProducts(productListModel); // Refresh product list
        // JOptionPane.showMessageDialog(frame, "Product deleted successfully.");
    }

    private static void showInventory(JFrame parentFrame) {
        List<Product> products = ProductDAO.getAllProducts();

        String[] columnNames = { "Name", "Description", "Quantity", "Price" };
        Object[][] data = new Object[products.size()][4];

        for (int i = 0; i < products.size(); i++) {
            Product product = products.get(i);
            data[i][0] = product.getName();
            data[i][1] = product.getDescription();
            data[i][2] = product.getQuantity();
            data[i][3] = product.getPrice();
        }

        JTable table = new JTable(data, columnNames);
        table.setPreferredScrollableViewportSize(new Dimension(500, 200));
        table.setFillsViewportHeight(true);

        JScrollPane scrollPane = new JScrollPane(table);
        JOptionPane.showMessageDialog(parentFrame, scrollPane, "Inventory Details", JOptionPane.INFORMATION_MESSAGE);
    }

    private static void showAddProductDialog(JFrame parentFrame, DefaultListModel<Product> productListModel) {
        JDialog dialog = new JDialog(parentFrame, "Add Product", true);
        dialog.setSize(400, 300);
        dialog.setLayout(new BorderLayout());

        // Panel for input fields
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(4, 2));

        // Labels and TextFields for product details
        JLabel nameLabel = new JLabel("Name:");
        JTextField nameTextField = new JTextField();
        inputPanel.add(nameLabel);
        inputPanel.add(nameTextField);

        JLabel descriptionLabel = new JLabel("Description:");
        JTextField descriptionTextField = new JTextField();
        inputPanel.add(descriptionLabel);
        inputPanel.add(descriptionTextField);

        JLabel priceLabel = new JLabel("Price:");
        JTextField priceTextField = new JTextField();
        inputPanel.add(priceLabel);
        inputPanel.add(priceTextField);

        JLabel quantityLabel = new JLabel("Quantity:");
        JTextField quantityTextField = new JTextField();
        inputPanel.add(quantityLabel);
        inputPanel.add(quantityTextField);

        // Button Panel
        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Add");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get product details from input fields
                String name = nameTextField.getText();
                String description = descriptionTextField.getText();
                String quantityText = quantityTextField.getText();
                String priceText = priceTextField.getText();

                // Validate input fields
                if (name.isEmpty() || description.isEmpty() || quantityText.isEmpty() || priceText.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Please fill in all fields.");
                    return;
                }

                // Validate numeric input
                int quantity;
                int price;
                try {
                    quantity = Integer.parseInt(quantityText);
                    price = Integer.parseInt(priceText);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(dialog, "Please enter valid numeric values for Quantity and Price.");
                    return;
                }

                // Create Product object
                Product product = new Product();
                product.setName(name);
                product.setDescription(description);
                product.setQuantity(quantity);
                product.setPrice(price);

                // Add product to database
                ProductDAO.addProduct(product);

                // Refresh product list
                loadProducts(productListModel);

                // Close the dialog
                dialog.dispose();
                JOptionPane.showMessageDialog(parentFrame, "Product added successfully!");

            }
        });
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Close the dialog
                dialog.dispose();
            }
        });
        buttonPanel.add(addButton);
        buttonPanel.add(cancelButton);

        dialog.add(inputPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private static void showEditProductDialog(JFrame parentFrame, Product product,
            DefaultListModel<Product> productListModel) {
        JDialog dialog = new JDialog(parentFrame, "Edit Product", true);
        dialog.setSize(400, 300);
        dialog.setLayout(new BorderLayout());

        // Panel for input fields
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(4, 2));

        // Labels and TextFields for product details
        JLabel nameLabel = new JLabel("Name:");
        JTextField nameTextField = new JTextField(product.getName());
        inputPanel.add(nameLabel);
        inputPanel.add(nameTextField);

        JLabel descriptionLabel = new JLabel("Description:");
        JTextField descriptionTextField = new JTextField(product.getDescription());
        inputPanel.add(descriptionLabel);
        inputPanel.add(descriptionTextField);

        JLabel priceLabel = new JLabel("Price:");
        JTextField priceTextField = new JTextField(String.valueOf(product.getPrice()));
        inputPanel.add(priceLabel);
        inputPanel.add(priceTextField);

        JLabel quantityLabel = new JLabel("Quantity:");
        JTextField quantityTextField = new JTextField(String.valueOf(product.getQuantity()));
        inputPanel.add(quantityLabel);
        inputPanel.add(quantityTextField);

        // Button Panel
        JPanel buttonPanel = new JPanel();
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get edited product details from input fields
                String name = nameTextField.getText();
                String description = descriptionTextField.getText();
                int quantity = Integer.parseInt(quantityTextField.getText());
                int price = Integer.parseInt(priceTextField.getText());

                // Update the product object
                product.setName(name);
                product.setDescription(description);
                product.setQuantity(quantity);
                product.setPrice(price);

                // Update product in database
                ProductDAO.updateProduct(product);

                // Refresh product list
                loadProducts(productListModel);

                // Close the dialog
                dialog.dispose();
                JOptionPane.showMessageDialog(parentFrame, "Product updated successfully!");
            }
        });
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Close the dialog
                dialog.dispose();
            }
        });
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        dialog.add(inputPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }
}
