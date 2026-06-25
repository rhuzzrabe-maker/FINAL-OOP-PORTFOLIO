# Database Setup Guide

## 1. Create the Database Schema

Run the `schema.sql` file in your MySQL database:

```bash
mysql -u root -p < schema.sql
```

Or open MySQL and run:
```sql
source schema.sql;
```

This creates the `im_project` database with all required tables.

## 2. Download MySQL Connector/J

Download the MySQL JDBC driver:
- Go to: https://dev.mysql.com/downloads/connector/j/
- Select "Platform Independent" 
- Download the `.jar` file (e.g., `mysql-connector-j-8.3.0.jar`)

Place it in a `lib/` folder in your project:
```
FINAL-OOP-PORTFOLIO/
├── lib/
│   └── mysql-connector-j-8.3.0.jar
├── src/
│   └── com/pagibig/...
└── out/
```

## 3. Compile the Project

```bash
cd "D:/Personal Projects/FINAL-OOP-PORTFOLIO"

# Create lib folder and add the JAR
mkdir -p lib
# (manually move the downloaded JAR into lib/)

# Compile all Java files
javac -d out -cp "lib/*" src/com/pagibig/**/*.java
```

## 4. Run the Application

```bash
java -cp "out;lib/*" com.pagibig.main.PagibigAdminApp
```

## 5. Configure Database Connection

**No `.env` file is included in the project.** The app uses these defaults (from DataConnection.java):
- Host: `127.0.0.1`
- Port: `3306`
- Database: `im_project`
- User: `root`
- Password: (empty)

If your MySQL setup is different, create a `.env` file in the project root on the machine where you run the app:

```env
DB_HOST=127.0.0.1
DB_PORT=3306
DB_NAME=im_project
DB_USER=root
DB_PASSWORD=your_password_here
```

## 6. Login Credentials

- Username: `admin`
- Password: `grp3db.OOP`

## What Changed

 **DatabaseDAO.java** - Handles all SQL operations (SELECT, INSERT, UPDATE, DELETE)  
 **DataStore.java** - Loads data from MySQL on startup and syncs changes back to DB  
 **DataConnection.java** - Fixed package mismatch; now throws SQLException on failure instead of returning null  
 **DashboardFrame.java** - Added Refresh button and error dialogs for failed DB operations  
 **schema.sql** - Database schema with foreign keys and CASCADE deletes

### Real-Time Reflection

- The UI loads data from the database when the dashboard opens.
- Every Add/Edit/Delete is written to the database immediately.
- If a database operation fails (duplicate ID, missing foreign key, bad credentials), the UI shows an error dialog and does NOT show fake data.
- Use the **Refresh** button in the top-right to manually reload all data from the database.

### Important Rules

Because of foreign keys:
1. Add a **Member** before adding their Contact, Employment, Government ID, Heir, or Previous Employment.
2. Add an **Employer** before adding Employment or Previous Employment that references it.
3. Deleting a Member automatically deletes their related records (CASCADE).
4. Deleting an Employer automatically deletes Employment records that reference it.
