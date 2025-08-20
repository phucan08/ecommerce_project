
Overview
This project is a simple command-line e-commerce information management system implemented in Java, following object-oriented programming (OOP) principles. It allows users to register, login as customers or admins, manage products, orders, and users, and generate statistical figures. Data is stored in text files (e.g., data/users.txt, data/products.txt, data/orders.txt).
The system architecture includes:

Model Classes: User, Admin, Customer, Product, Order (in model package).
Operation Classes: UserOperation, AdminOperation, CustomerOperation, ProductOperation, OrderOperation, CustomerListResult, ProductListResult, OrderListResult (in operation package).
IO Class: IOInterface (in io package).
Main Class: Main (default package).

Key features:

User registration with validation (username uniqueness, password strength, email format, mobile number).
Login for admins and customers.
Admin functionalities: Manage customers/products/orders, generate test data, statistical figures, delete data.
Customer functionalities: View/update profile, browse products (with keyword search), view order history, generate consumption figures.
Pagination for lists (10 items per page).
Password encryption.
Singleton pattern for operation classes.

Prerequisites

Java Development Kit (JDK) 8 or higher.
A Java IDE (e.g., Eclipse, IntelliJ) or command-line compiler (javac).

Setup

Clone or Download the Project:

Extract the source files into a directory structure like:
src/
├── Main.java
├── io/
│   └── IOInterface.java
├── model/
│   ├── Admin.java
│   ├── Customer.java
│   ├── Order.java
│   ├── Product.java
│   └── User.java
└── operation/
    ├── AdminOperation.java
    ├── CustomerListResult.java
    ├── CustomerOperation.java
    ├── OrderListResult.java
    ├── OrderOperation.java
    ├── ProductListResult.java
    ├── ProductOperation.java
    └── UserOperation.java



Create Data Directory:

Create a data/ folder in the project root.
Populate with sample data files (users.txt, products.txt, orders.txt) from the assignment PDF or generate during runtime.


Compile the Project:

In command line (from src/ directory):
javac -d ../bin Main.java io/*.java model/*.java operation/*.java

Or import into an IDE and build the project.


Run the Application:

Command line (from project root):
java -cp bin Main

In IDE: Run Main.java.



Usage
Main Menu

1. Login: Enter username and password.

Admin default: Username admin, Password admin123.
Sample Customer: Username john_doe, Password aB3cD4eF5gH6i (decrypted from sample data).


2. Register: Enter username, password, email, mobile (validations apply: username ≥5 chars letters/underscores, unique; password ≥5 chars with letter+number; email valid format; mobile 10 digits starting with 04/03).
3. Quit: Exit the system.

Admin Menu

Show products (paginated).
Add customers.
Show customers (paginated).
Show orders (paginated).
Generate test data (placeholder).
Generate all statistical figures (placeholder for charts).
Delete all data.
Logout.

Customer Menu

Show profile.
Update profile (username/password/email/mobile with validations).
Show products (optional keyword search).
Show history orders (paginated).
Generate all consumption figures (placeholder).
Logout.

Notes

Data persists in text files in JSON-like format.
Passwords are encrypted (prefixed/suffixed with ^^ and $$, interleaved random chars).
For charts (statistical figures), placeholders are in code; implement with JavaFX if needed.
Ensure data/ folder is writable.

Sample Data

Users: See assignment PDF for sample JSON entries in users.txt.
Products: Sample entries in products.txt.
Orders: Generate or add manually in orders.txt.

Contributing
This is an individual assignment project. For improvements, fork and submit pull requests if applicable.
License
This project is for educational purposes only. No license specified.