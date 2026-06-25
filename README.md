# Pag-IBIG Java Swing App

This project is a Java Swing conversion of the original HTML/CSS/JS Pag-IBIG admin portal.

## Run Instructions

1. Open a terminal in the project root (the folder that contains `src`). Example full path used here:

   ```powershell
   Set-Location 'C:\Users\SiAzel\Downloads\Pag-ibig_DatabaseProject-main\Pag-ibig_DatabaseProject-main'
   ```

2. Compile the Java source (compile all `.java` files recursively so the new package layout is included):

   - PowerShell (recommended):

     ```powershell
     & javac -d out (Get-ChildItem -Recurse -Filter "*.java" | ForEach-Object FullName)
     ```

   - cmd.exe:

     ```bat
     cd "C:\Users\SiAzel\Downloads\Pag-ibig_DatabaseProject-main\Pag-ibig_DatabaseProject-main"
     javac -d out src\com\pagibig\**\*.java
     ```

3. Run the app. The main class was moved into `com.pagibig.main` during refactor, so use this command:

   ```bat
   java -cp out com.pagibig.main.PagibigAdminApp
   ```

4. Notes and alternatives

- If you prefer the original top-level main class name (`com.pagibig.PagibigAdminApp`), I can move `PagibigAdminApp` back to `com.pagibig` and update imports. Otherwise, use the `com.pagibig.main.PagibigAdminApp` run command above.
- If you get a `NoClassDefFoundError` or `ClassNotFoundException`, make sure `out` exists and contains the compiled package folders (`com\pagibig\main`, `com\pagibig\data`, `com\pagibig\model`).

## Credentials

- Username: `admin`
- Password: `grp3db.OOP`
