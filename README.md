# Pag-IBIG Java Swing App

This project is a Java Swing conversion of the original HTML/CSS/JS Pag-IBIG admin portal.

## Run Instructions

1. Open a terminal in the actual project folder that contains `src\com\pagibig`. If you are currently in the outer folder, first run:

   ```bat
   cd Pag-ibig_DatabaseProject-main
   ```

2. Compile the Java source:

   - In `cmd.exe`:

     ```bat
     javac -d out src\com\pagibig\*.java
     ```

   - In PowerShell:

     ```powershell
     & javac -d out (Get-ChildItem -Path src\com\pagibig\*.java | ForEach-Object FullName)
     ```

3. Run the app:

   ```bat
   java -cp out com.pagibig.PagibigAdminApp
   ```

## Credentials

- Username: `admin`
- Password: `grp3db.IM`
