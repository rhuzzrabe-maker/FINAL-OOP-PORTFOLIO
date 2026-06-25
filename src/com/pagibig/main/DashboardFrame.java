package com.pagibig.main;

import com.pagibig.data.DataStore;
import com.pagibig.model.ContactRecord;
import com.pagibig.model.EmployerRecord;
import com.pagibig.model.EmploymentRecord;
import com.pagibig.model.GovernmentIdRecord;
import com.pagibig.model.HeirRecord;
import com.pagibig.model.MemberRecord;
import com.pagibig.model.PreviousEmploymentRecord;
import java.awt.*;
import java.util.ArrayList;
import java.util.regex.Pattern;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

public class DashboardFrame extends JFrame {
    private final DataStore dataStore;

    private final DefaultTableModel memberModel;
    private final DefaultTableModel contactModel;
    private final DefaultTableModel employmentModel;
    private final DefaultTableModel previousEmploymentModel;
    private final DefaultTableModel heirModel;
    private final DefaultTableModel governmentIdModel;
    private final DefaultTableModel employerModel;

    private final JTable memberTable;
    private final JTable contactTable;
    private final JTable employmentTable;
    private final JTable previousEmploymentTable;
    private final JTable heirTable;
    private final JTable governmentIdTable;
    private final JTable employerTable;

    private final TableRowSorter<DefaultTableModel> memberSorter;
    private final TableRowSorter<DefaultTableModel> contactSorter;
    private final TableRowSorter<DefaultTableModel> employmentSorter;
    private final TableRowSorter<DefaultTableModel> previousEmploymentSorter;
    private final TableRowSorter<DefaultTableModel> heirSorter;
    private final TableRowSorter<DefaultTableModel> governmentIdSorter;
    private final TableRowSorter<DefaultTableModel> employerSorter;

    private final JTextField searchField;
    private final JTabbedPane tabbedPane;

    public DashboardFrame(DataStore dataStore) {
        this.dataStore = dataStore;

        setTitle("Pag-IBIG Fund Admin Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(980, 680);
        setLocationRelativeTo(null);

        JPanel content = new JPanel(new BorderLayout(12, 12));
        content.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        setContentPane(content);

        JLabel headerLabel = new JLabel("Pag-IBIG Fund Member Database System");
        headerLabel.setFont(new Font("SansSerif", Font.BOLD, 22));

        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> {
            dataStore.loadAllFromDatabase();
            refreshAllTables();
        });

        JButton helpButton = new JButton("Help Desk");
        helpButton.addActionListener(e -> showHelpDialog());
        JButton logoutButton = new JButton("Log Out");
        logoutButton.addActionListener(e -> System.exit(0));

        JPanel headerBar = new JPanel(new BorderLayout(10, 0));
        headerBar.add(headerLabel, BorderLayout.WEST);
        JPanel headerActions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        headerActions.add(refreshButton);
        headerActions.add(helpButton);
        headerActions.add(logoutButton);
        headerBar.add(headerActions, BorderLayout.EAST);

        content.add(headerBar, BorderLayout.NORTH);

        searchField = new JTextField();
        searchField.setColumns(28);
        searchField.setToolTipText("Search the active table");
        searchField.addActionListener(e -> applySearchFilter());

        JButton clearSearch = new JButton("Clear");
        clearSearch.addActionListener(e -> {
            searchField.setText("");
            applySearchFilter();
        });

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(searchField);
        searchPanel.add(clearSearch);
        content.add(searchPanel, BorderLayout.SOUTH);

        memberModel = new DefaultTableModel(new Object[]{
            "Pagibig ID", "Regis Num", "Occupation Status", "First Time", 
            "Member Type", "Member Subtype", "Type Work", "Type Country", 
            "Member Name", "Fat Name", "Mot Name", "Spouse Name", 
            "MemCert Name", "Birth Date", "Place of Birth", "Sex", 
            "Height", "Weight", "Marital Status", "Citizenship", 
            "Facial Features", "Frequency of Payment"
        }, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        contactModel = new DefaultTableModel(new Object[]{"Pagibig ID", "Cell Num", "Home Num", "Business Direct", "Business Trunk", "Email Address", "Perm Address", "Present Address", "Preferred Mail Address"}, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        employmentModel = new DefaultTableModel(new Object[]{"Pagibig ID", "Employer ID", "Status", "Occupation", "Office Assignment", "Date Employed", "Monthly Income"}, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        previousEmploymentModel = new DefaultTableModel(new Object[]{"Pagibig ID", "Employer ID", "Date From", "Date To", "Prev Office Assignment"}, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        heirModel = new DefaultTableModel(new Object[]{"Pagibig ID", "Heir Code", "Heir Name", "Relationship", "Birth Date"}, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        governmentIdModel = new DefaultTableModel(new Object[]{"Pagibig ID", "TIN", "SSS", "CRN", "EM Num", "AFP/PNP", "DepEd Code"}, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        employerModel = new DefaultTableModel(new Object[]{"Employer ID", "Employer Name", "Employer Address"}, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };

        memberTable = new JTable(memberModel);
        memberTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        memberTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        memberSorter = new TableRowSorter<>(memberModel);
        memberTable.setRowSorter(memberSorter);

        contactTable = new JTable(contactModel);
        contactTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        contactTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        contactSorter = new TableRowSorter<>(contactModel);
        contactTable.setRowSorter(contactSorter);

        employmentTable = new JTable(employmentModel);
        employmentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        employmentTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        employmentSorter = new TableRowSorter<>(employmentModel);
        employmentTable.setRowSorter(employmentSorter);

        previousEmploymentTable = new JTable(previousEmploymentModel);
        previousEmploymentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        previousEmploymentTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        previousEmploymentSorter = new TableRowSorter<>(previousEmploymentModel);
        previousEmploymentTable.setRowSorter(previousEmploymentSorter);

        heirTable = new JTable(heirModel);
        heirTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        heirTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        heirSorter = new TableRowSorter<>(heirModel);
        heirTable.setRowSorter(heirSorter);

        governmentIdTable = new JTable(governmentIdModel);
        governmentIdTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        governmentIdTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        governmentIdSorter = new TableRowSorter<>(governmentIdModel);
        governmentIdTable.setRowSorter(governmentIdSorter);

        employerTable = new JTable(employerModel);
        employerTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        employerTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        employerSorter = new TableRowSorter<>(employerModel);
        employerTable.setRowSorter(employerSorter);

        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Member Information", createMemberTab());
        tabbedPane.addTab("Contact Details", createContactTab());
        tabbedPane.addTab("Current Employment", createEmploymentTab());
        tabbedPane.addTab("Previous Employment", createPreviousEmploymentTab());
        tabbedPane.addTab("Beneficiary Registry", createHeirTab());
        tabbedPane.addTab("Government IDs", createGovernmentIdTab());
        tabbedPane.addTab("Employer Registry", createEmployerTab());
        tabbedPane.addChangeListener(e -> applySearchFilter());

        content.add(tabbedPane, BorderLayout.CENTER);

        refreshAllTables();
    }

    private JPanel createMemberTab() {
        return createTablePanel(memberTable, "Member", () -> showMemberDialog(-1), () -> editTableSelection(memberTable, this::showMemberDialog, dataStore.getMembers().size()));
    }

    private JPanel createContactTab() {
        return createTablePanel(contactTable, "Contact", () -> showContactDialog(-1), () -> editTableSelection(contactTable, this::showContactDialog, dataStore.getContacts().size()));
    }

    private JPanel createEmploymentTab() {
        return createTablePanel(employmentTable, "Employment", () -> showEmploymentDialog(-1), () -> editTableSelection(employmentTable, this::showEmploymentDialog, dataStore.getEmployments().size()));
    }

    private JPanel createPreviousEmploymentTab() {
        return createTablePanel(previousEmploymentTable, "Previous Employment", () -> showPreviousEmploymentDialog(-1), () -> editTableSelection(previousEmploymentTable, this::showPreviousEmploymentDialog, dataStore.getPreviousEmployments().size()));
    }

    private JPanel createHeirTab() {
        return createTablePanel(heirTable, "Heir", () -> showHeirDialog(-1), () -> editTableSelection(heirTable, this::showHeirDialog, dataStore.getHeirs().size()));
    }

    private JPanel createGovernmentIdTab() {
        return createTablePanel(governmentIdTable, "Government ID", () -> showGovernmentIdDialog(-1), () -> editTableSelection(governmentIdTable, this::showGovernmentIdDialog, dataStore.getGovernmentIds().size()));
    }

    private JPanel createEmployerTab() {
        return createTablePanel(employerTable, "Employer", () -> showEmployerDialog(-1), () -> editTableSelection(employerTable, this::showEmployerDialog, dataStore.getEmployers().size()));
    }

    private JPanel createTablePanel(JTable table, String label, Runnable addAction, Runnable editAction) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        JButton addButton = new JButton("Add " + label);
        addButton.addActionListener(e -> addAction.run());

        JButton editButton = new JButton("Edit " + label);
        editButton.addActionListener(e -> editAction.run());

        JButton deleteButton = new JButton("Delete " + label);
        deleteButton.addActionListener(e -> deleteSelection(table, label));

        JPanel buttonBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 10));
        buttonBar.add(addButton);
        buttonBar.add(editButton);
        buttonBar.add(deleteButton);
        panel.add(buttonBar, BorderLayout.NORTH);
        return panel;
    }

    private void editTableSelection(JTable table, java.util.function.IntConsumer editor, int size) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            int modelRow = table.convertRowIndexToModel(selectedRow);
            editor.accept(modelRow);
        } else {
            JOptionPane.showMessageDialog(this, "Please select a row.", "No selection", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void showHelpDialog() {
        JOptionPane.showMessageDialog(this, "Use the tabs to switch between data types, and use Add/Edit/Delete to manage records.", "System Help Desk", JOptionPane.INFORMATION_MESSAGE);
    }

    // =====================================================================
    // 🔀 PROGRAMMATIC REGISTRATION DISPATCHER
    // =====================================================================
    private void showMemberDialog(int rowIndex) {
        if (rowIndex >= 0) {
            // Edit Mode remains an isolated single row operation
            showSingleMemberEditDialog(rowIndex);
        } else {
            // Creation Mode prompts the user for the structural wizard pathway
            String[] options = {"Full Member Record (Across Tables)", "Only This Table"};
            int choice = JOptionPane.showOptionDialog(this,
                    "What type of record do you want to add?",
                    "Add Record Entry",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

            if (choice == 0) {
                executeFullRegistrationWizardPipeline();
            } else if (choice == 1) {
                showSingleMemberEditDialog(-1);
            }
        }
    }

    // =====================================================================
    // 🧙‍♂️ COMPLETE MULTI-STEP TRANSACTION WIZARD PIPELINE
    // =====================================================================
    private void executeFullRegistrationWizardPipeline() {
        try {
            // STEP 1: Root Member Information
            MemberRecord member = gatherWizardMemberNode();
            if (member == null) return; // Wizard Cancelled

            String trackingId = member.getPagibigId();

            // STEP 2: Related Contact Details
            ContactRecord contact = gatherWizardContactNode(trackingId);
            if (contact == null) return;

            // STEP 3: Current Employment Profile (Optional)
            java.util.List<EmploymentRecord> employments = gatherWizardEmploymentNodes(trackingId);
            if (employments == null) return;

            // STEP 4: Previous Job Ledger History (Optional)
            java.util.List<PreviousEmploymentRecord> prevJobs = gatherWizardPrevEmploymentNodes(trackingId);
            if (prevJobs == null) return;

            // STEP 5: Beneficiary Registry (Mandatory - Enforces at least 1 Heir)
            java.util.List<HeirRecord> heirs = gatherWizardHeirNodes(trackingId);
            if (heirs == null || heirs.isEmpty()) return;

            // STEP 6: Government Identification Registry (Optional)
            GovernmentIdRecord govIds = gatherWizardGovernmentIdNode(trackingId);
            if (govIds == null) return;

            // === 💾 TRANSACT TRANSACTION COMIT BATCH WRITING PHASE ===
            boolean status = dataStore.addMember(member);
            if (status) dataStore.addContact(contact);
            
            if (status && !employments.isEmpty()) {
                for (EmploymentRecord emp : employments) dataStore.addEmployment(emp);
            }
            if (status && !prevJobs.isEmpty()) {
                for (PreviousEmploymentRecord pJob : prevJobs) dataStore.addPreviousEmployment(pJob);
            }
            if (status && !heirs.isEmpty()) {
                for (HeirRecord h : heirs) dataStore.addHeir(h);
            }
            
            // Persist Gov ID block only if at least one alternative key field is provided
            if (status && govIds != null && (!govIds.getTinNum().isEmpty() || !govIds.getSssNum().isEmpty() || 
                !govIds.getCrn().isEmpty() || !govIds.getEmNum().isEmpty() || !govIds.getAfpPnpNum().isEmpty() || !govIds.getDepedCode().isEmpty())) {
                dataStore.addGovernmentId(govIds);
            }

            if (status) {
                JOptionPane.showMessageDialog(this, "Full member data has been successfully added to all relations!", "Registration Complete", JOptionPane.INFORMATION_MESSAGE);
                refreshAllTables();
            } else {
                JOptionPane.showMessageDialog(this, "Batch transaction failure saving structural data records.", "Database Rejection", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Wizard session execution anomaly: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // =====================================================================
    // 🛠️ SUB-ROUTINE TRANSACTION COMPONENT NODES
    // =====================================================================
    private MemberRecord gatherWizardMemberNode() {
        JTextField idField = new JTextField();
        JTextField regisField = new JTextField();
        JComboBox<String> occCombo = new JComboBox<>(new String[]{"-- Select Status --", "EMPLOYED", "UNEMPLOYED"});
        JRadioButton firstTimeYes = new JRadioButton("YES"); JRadioButton firstTimeNo = new JRadioButton("NO");
        ButtonGroup ftG = new ButtonGroup(); ftG.add(firstTimeYes); ftG.add(firstTimeNo); firstTimeNo.setSelected(true);
        JComboBox<String> typeCombo = new JComboBox<>(new String[]{"-- Select Membership Type --", "MANDATORY", "VOLUNTARY"});
        JComboBox<String> subtypeCombo = new JComboBox<>(new String[]{"-- Select Membership Type First --"});
        
        JLabel othersLabel = new JLabel("Specify Subtype: *"); JTextField othersTextField = new JTextField();
        JLabel workLabel = new JLabel("Type of Work: *"); JComboBox<String> workCombo = new JComboBox<>(new String[]{"-- Select Type of Work --", "Land-based", "Sea-based"});
        JLabel countryLabel = new JLabel("Type Country: *"); JTextField countryField = new JTextField();
        
        othersLabel.setVisible(false); othersTextField.setVisible(false);
        workLabel.setVisible(false); workCombo.setVisible(false); countryLabel.setVisible(false); countryField.setVisible(false);

        JPanel oP = new JPanel(new GridLayout(0, 2, 8, 8)); oP.add(othersLabel); oP.add(othersTextField); oP.setVisible(false);
        JPanel ofwP = new JPanel(new GridLayout(0, 2, 8, 8)); ofwP.add(workLabel); ofwP.add(workCombo); ofwP.add(countryLabel); ofwP.add(countryField); ofwP.setVisible(false);

        typeCombo.addActionListener(e -> {
            String t = (String) typeCombo.getSelectedItem(); subtypeCombo.removeAllItems();
            if ("MANDATORY".equals(t)) {
                subtypeCombo.addItem("-- Select Mandatory Subtype --"); subtypeCombo.addItem("EMPLOYED");
                subtypeCombo.addItem("OVERSEAS FILIPINO WORKER (OFW)"); subtypeCombo.addItem("SELF-EMPLOYED"); subtypeCombo.addItem("OTHERS");
            } else if ("VOLUNTARY".equals(t)) {
                subtypeCombo.addItem("-- Select Voluntary Subtype --"); subtypeCombo.addItem("EMPLOYED");
                subtypeCombo.addItem("INDIVIDUAL PAYOR"); subtypeCombo.addItem("OTHERS");
            } else { subtypeCombo.addItem("-- Select Membership Type First --"); }
        });

        subtypeCombo.addActionListener(e -> {
            String s = (String) subtypeCombo.getSelectedItem(); if (s == null) return;
            boolean isO = "OTHERS".equals(s); boolean isOfw = "OVERSEAS FILIPINO WORKER (OFW)".equals(s);
            othersLabel.setVisible(isO); othersTextField.setVisible(isO); oP.setVisible(isO);
            workLabel.setVisible(isOfw); workCombo.setVisible(isOfw); countryLabel.setVisible(isOfw); countryField.setVisible(isOfw); ofwP.setVisible(isOfw);
            SwingUtilities.getWindowAncestor(subtypeCombo).pack();
        });

        JTextField nameField = new JTextField(); JTextField fatField = new JTextField(); JTextField motField = new JTextField(); JTextField spouseField = new JTextField();
        JTextField certField = new JTextField(); JTextField birthField = new JTextField(); JTextField placeField = new JTextField();
        JRadioButton sexM = new JRadioButton("Male"); JRadioButton sexF = new JRadioButton("Female");
        ButtonGroup sexG = new ButtonGroup(); sexG.add(sexM); sexG.add(sexF); sexM.setSelected(true);
        JTextField hField = new JTextField(); JTextField wField = new JTextField();
        JComboBox<String> maritalCombo = new JComboBox<>(new String[]{"-- Select Marital Status --", "Single / Unmarried", "Widow / er", "Annulled", "Married", "Legally Separated"});
        JTextField citizenshipField = new JTextField(); JTextField facialField = new JTextField();
        JComboBox<String> paymentCombo = new JComboBox<>(new String[]{"-- Select Frequency --", "Monthly", "Quarterly"});

        JPanel topP = new JPanel(new GridLayout(0, 2, 8, 8));
        topP.add(new JLabel("Pag-IBIG ID: *")); topP.add(idField); topP.add(new JLabel("Registration Num: *")); topP.add(regisField);
        topP.add(new JLabel("Occupation Status: *")); topP.add(occCombo); topP.add(new JLabel("First Time Member?: *"));
        JPanel rFT = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0)); rFT.add(firstTimeYes); rFT.add(firstTimeNo); topP.add(rFT);
        topP.add(new JLabel("Member Type: *")); topP.add(typeCombo); topP.add(new JLabel("Member Subtype: *")); topP.add(subtypeCombo);

        JPanel bottomP = new JPanel(new GridLayout(0, 2, 8, 8));
        bottomP.add(new JLabel("Member Name: *")); bottomP.add(nameField); bottomP.add(new JLabel("Father's Name:")); bottomP.add(fatField);
        bottomP.add(new JLabel("Mother's Name: *")); bottomP.add(motField); bottomP.add(new JLabel("Spouse Name:")); bottomP.add(spouseField);
        bottomP.add(new JLabel("Certificate Name:")); bottomP.add(certField); bottomP.add(new JLabel("Birth Date (YYYY-MM-DD): *")); bottomP.add(birthField);
        bottomP.add(new JLabel("Place of Birth: *")); bottomP.add(placeField); bottomP.add(new JLabel("Sex: *"));
        JPanel rS = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0)); rS.add(sexM); rS.add(sexF); bottomP.add(rS);
        bottomP.add(new JLabel("Height (cm):")); bottomP.add(hField); bottomP.add(new JLabel("Weight (kg):")); bottomP.add(wField);
        bottomP.add(new JLabel("Marital Status: *")); bottomP.add(maritalCombo); bottomP.add(new JLabel("Citizenship: *")); bottomP.add(citizenshipField);
        bottomP.add(new JLabel("Facial Features:")); bottomP.add(facialField); bottomP.add(new JLabel("Frequency of Payment:")); bottomP.add(paymentCombo);

        JPanel mainP = new JPanel(); mainP.setLayout(new BoxLayout(mainP, BoxLayout.Y_AXIS));
        mainP.add(new JLabel("--- STEP 1: ACCOUNT IDENTIFICATION REGISTRATION (MEMBER PROFILE) ---"));
        mainP.add(Box.createVerticalStrut(10)); mainP.add(topP); mainP.add(oP); mainP.add(ofwP); mainP.add(Box.createVerticalStrut(10)); mainP.add(bottomP);

        while (true) {
            int res = JOptionPane.showConfirmDialog(this, mainP, "Wizard Step 1: Member Data", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (res != JOptionPane.OK_OPTION) return null;

            String rawId = idField.getText().trim(); String rawReg = regisField.getText().trim();
            String rawName = nameField.getText().trim().toUpperCase(); String rawMot = motField.getText().trim().toUpperCase();
            String rawBirth = birthField.getText().trim(); String rawPlace = placeField.getText().trim().toUpperCase();
            String rawCit = citizenshipField.getText().trim().toUpperCase();

            if (rawId.isEmpty() || rawReg.isEmpty() || occCombo.getSelectedIndex() <= 0 || typeCombo.getSelectedIndex() <= 0 || 
                subtypeCombo.getSelectedIndex() <= 0 || rawName.isEmpty() || rawMot.isEmpty() || rawBirth.isEmpty() || rawPlace.isEmpty() || 
                maritalCombo.getSelectedIndex() <= 0 || rawCit.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Validation Blocked: Missing mandatory fields marked with (*).", "Validation Error", JOptionPane.ERROR_MESSAGE);
                continue;
            }

            // Verify Unique Key duplication constraints early inside the wizard stack
            boolean duplicated = false;
            for (MemberRecord record : dataStore.getMembers()) {
                if (record.getPagibigId().equals(rawId)) { duplicated = true; break; }
            }
            if (duplicated) {
                JOptionPane.showMessageDialog(this, "Registration Refused: Pag-IBIG ID already exists inside the database engine records.", "Primary Key Conflict", JOptionPane.ERROR_MESSAGE);
                continue;
            }

            String sSub = (String) subtypeCombo.getSelectedItem();
            if ("OTHERS".equals(sSub)) {
                sSub = othersTextField.getText().trim().toUpperCase();
                if (sSub.isEmpty()) { JOptionPane.showMessageDialog(this, "Validation Blocked: Please specify custom Subtype.", "Validation Error", JOptionPane.ERROR_MESSAGE); continue; }
            }

            String fWork = ""; String fCountry = "";
            if ("OVERSEAS FILIPINO WORKER (OFW)".equals((String) subtypeCombo.getSelectedItem())) {
                if (workCombo.getSelectedIndex() <= 0 || countryField.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Validation Blocked: Work type and Country entries are required for OFWs.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                    continue;
                }
                fWork = (String) workCombo.getSelectedItem(); fCountry = countryField.getText().trim().toUpperCase();
            }

            String fMarital = switch(maritalCombo.getSelectedIndex()) { case 1->"S"; case 2->"W"; case 3->"A"; case 4->"M"; case 5->"LS"; default->""; };
            return new MemberRecord(rawId, rawReg, (String) occCombo.getSelectedItem(), firstTimeYes.isSelected()?"YES":"NO", (String) typeCombo.getSelectedItem(), sSub,
                    fWork, fCountry, rawName, fatField.getText().trim().toUpperCase(), rawMot, spouseField.getText().trim().toUpperCase(), certField.getText().trim().toUpperCase(),
                    rawBirth, rawPlace, sexM.isSelected()?"M":"F", hField.getText().trim(), wField.getText().trim(), fMarital, rawCit, facialField.getText().trim().toUpperCase(),
                    paymentCombo.getSelectedIndex()==0?"":(String) paymentCombo.getSelectedItem());
        }
    }

    private ContactRecord gatherWizardContactNode(String trackingId) {
        JTextField cellField = new JTextField(); JTextField homeField = new JTextField(); JTextField directField = new JTextField();
        JTextField trunkField = new JTextField(); JTextField emailField = new JTextField(); JTextField permField = new JTextField();
        JTextField presentField = new JTextField();
        JComboBox<String> prefCombo = new JComboBox<>(new String[]{"-- Select Preferred Address --", "Present Home Address", "Permanent Home Address", "Employer/Business Address"});
        JCheckBox syncCb = new JCheckBox("Present Address is the same as Permanent Home Address");

        syncCb.addActionListener(e -> {
            if (syncCb.isSelected()) { presentField.setText(permField.getText()); presentField.setEnabled(false); }
            else { presentField.setEnabled(true); presentField.setText(""); }
        });

        permField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { if(syncCb.isSelected()) presentField.setText(permField.getText()); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { if(syncCb.isSelected()) presentField.setText(permField.getText()); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { if(syncCb.isSelected()) presentField.setText(permField.getText()); }
        });

        JPanel p = new JPanel(new GridLayout(0, 2, 8, 8));
        p.add(new JLabel("--- STEP 2: ADDRESS AND CONTACT DETAILS ---")); p.add(new JLabel(""));
        p.add(new JLabel("Cell Number: *")); p.add(cellField); p.add(new JLabel("Home Number:")); p.add(homeField);
        p.add(new JLabel("Business Direct:")); p.add(directField); p.add(new JLabel("Business Trunk:")); p.add(trunkField);
        p.add(new JLabel("Email Address:")); p.add(emailField); p.add(new JLabel("Permanent Address: *")); p.add(permField);
        p.add(syncCb); p.add(new JLabel("")); p.add(new JLabel("Present Address: *")); p.add(presentField);
        p.add(new JLabel("Preferred Mail Address: *")); p.add(prefCombo);

        while (true) {
            int res = JOptionPane.showConfirmDialog(this, p, "Wizard Step 2: Contact Specifications", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (res != JOptionPane.OK_OPTION) return null;

            String cVal = cellField.getText().trim(); String permVal = permField.getText().trim().toUpperCase();
            String presVal = syncCb.isSelected() ? permVal : presentField.getText().trim().toUpperCase();

            if (cVal.isEmpty() || permVal.isEmpty() || presVal.isEmpty() || prefCombo.getSelectedIndex() <= 0) {
                JOptionPane.showMessageDialog(this, "Validation Blocked: Missing required contact elements.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                continue;
            }
            return new ContactRecord(trackingId, cVal, homeField.getText().trim(), directField.getText().trim(), trunkField.getText().trim(), emailField.getText().trim(), permVal, presVal, (String) prefCombo.getSelectedItem());
        }
    }

    private void showEmploymentDialog(int rowIndex) {
        JTextField idField = new JTextField();
        JTextField empIdField = new JTextField();
        JComboBox<String> statusCombo = new JComboBox<>(new String[]{"-- Select Status --", "Permanent/Regular", "Casual", "Contractual", "Project-based", "Part-time/Temporary"});
        JTextField occField = new JTextField();
        JComboBox<String> officeCombo = new JComboBox<>(new String[]{"-- Select Office --", "Head Office", "Branch Office"});
        JTextField dateField = new JTextField();
        JTextField incomeField = new JTextField();

        if (rowIndex >= 0) {
            com.pagibig.model.EmploymentRecord existing = dataStore.getEmployments().get(rowIndex);
            idField.setText(existing.getPagibigId()); idField.setEditable(false);
            empIdField.setText(existing.getEmployerId()); empIdField.setEditable(false);
            statusCombo.setSelectedItem(existing.getEmploymentStatus());
            occField.setText(existing.getOccupation());
            officeCombo.setSelectedItem(existing.getOfficeAssignment());
            dateField.setText(existing.getDateEmployed());
            incomeField.setText(existing.getMonthlyIncome());
        }

        JPanel p = new JPanel(new GridLayout(0, 2, 8, 8));
        p.add(new JLabel("Pag-IBIG ID: *")); p.add(idField);
        p.add(new JLabel("Employer ID: *")); p.add(empIdField);
        p.add(new JLabel("Employment Status: *")); p.add(statusCombo);
        p.add(new JLabel("Occupation: *")); p.add(occField);
        p.add(new JLabel("Office Assignment: *")); p.add(officeCombo);
        p.add(new JLabel("Date Employed: *")); p.add(dateField);
        p.add(new JLabel("Monthly Income: *")); p.add(incomeField);

        int result = JOptionPane.showConfirmDialog(this, p, rowIndex < 0 ? "Add Employment" : "Edit Employment", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            String rawId = idField.getText().trim(); String empId = empIdField.getText().trim();
            if (rawId.isEmpty() || empId.isEmpty() || statusCombo.getSelectedIndex() <= 0 || occField.getText().trim().isEmpty() || officeCombo.getSelectedIndex() <= 0) {
                JOptionPane.showMessageDialog(this, "Validation Error: Missing fields.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Check constraint if adding standalone
            if (rowIndex < 0) {
                boolean exist = dataStore.getMembers().stream().anyMatch(m -> m.getPagibigId().equals(rawId));
                if (!exist) { JOptionPane.showMessageDialog(this, "Error: Pag-IBIG ID does not exist.", "Error", JOptionPane.ERROR_MESSAGE); return; }
            }

            com.pagibig.model.EmploymentRecord record = new com.pagibig.model.EmploymentRecord(rawId, empId, (String) statusCombo.getSelectedItem(), occField.getText().trim().toUpperCase(), (String) officeCombo.getSelectedItem(), dateField.getText().trim(), incomeField.getText().trim());
            boolean success = rowIndex >= 0 ? dataStore.updateEmployment(rowIndex, record) : dataStore.addEmployment(record);
            handleSaveResult(success, "Employment");
        }
    }

    private void showPreviousEmploymentDialog(int rowIndex) {
        JTextField idField = new JTextField();
        JTextField empIdField = new JTextField();
        JTextField fromField = new JTextField();
        JTextField toField = new JTextField();
        JComboBox<String> officeCombo = new JComboBox<>(new String[]{"-- Select Office --", "Head Office", "Branch Office"});

        if (rowIndex >= 0) {
            com.pagibig.model.PreviousEmploymentRecord existing = dataStore.getPreviousEmployments().get(rowIndex);
            idField.setText(existing.getPagibigId()); idField.setEditable(false);
            empIdField.setText(existing.getEmployerId()); empIdField.setEditable(false);
            fromField.setText(existing.getDateFrom()); fromField.setEditable(false);
            toField.setText(existing.getDateTo());
            officeCombo.setSelectedItem(existing.getPrevOfficeAssignment());
        }

        JPanel p = new JPanel(new GridLayout(0, 2, 8, 8));
        p.add(new JLabel("Pag-IBIG ID: *")); p.add(idField);
        p.add(new JLabel("Employer ID: *")); p.add(empIdField);
        p.add(new JLabel("Date From: *")); p.add(fromField);
        p.add(new JLabel("Date To: *")); p.add(toField);
        p.add(new JLabel("Office Assignment: *")); p.add(officeCombo);

        int result = JOptionPane.showConfirmDialog(this, p, rowIndex < 0 ? "Add Past Job" : "Edit Past Job", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            String rawId = idField.getText().trim();
            if (rawId.isEmpty() || empIdField.getText().trim().isEmpty() || fromField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Validation Error: Missing fields.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (rowIndex < 0) {
                boolean exist = dataStore.getMembers().stream().anyMatch(m -> m.getPagibigId().equals(rawId));
                if (!exist) { JOptionPane.showMessageDialog(this, "Error: Pag-IBIG ID does not exist.", "Error", JOptionPane.ERROR_MESSAGE); return; }
            }

            com.pagibig.model.PreviousEmploymentRecord record = new com.pagibig.model.PreviousEmploymentRecord(rawId, empIdField.getText().trim(), fromField.getText().trim(), toField.getText().trim(), (String) officeCombo.getSelectedItem());
            boolean success = rowIndex >= 0 ? dataStore.updatePreviousEmployment(rowIndex, record) : dataStore.addPreviousEmployment(record);
            handleSaveResult(success, "Previous Employment");
        }
    }

    private void showHeirDialog(int rowIndex) {
        JTextField idField = new JTextField();
        JTextField codeField = new JTextField();
        JTextField nameField = new JTextField();
        JTextField relField = new JTextField();
        JTextField birthField = new JTextField();

        if (rowIndex >= 0) {
            com.pagibig.model.HeirRecord existing = dataStore.getHeirs().get(modelToViewRowIndex(rowIndex, heirTable));
            idField.setText(existing.getPagibigId()); idField.setEditable(false);
            codeField.setText(existing.getHeirCode()); codeField.setEditable(false);
            nameField.setText(existing.getHeirName());
            relField.setText(existing.getRelationship());
            birthField.setText(existing.getHeirDateBirth());
        }

        JPanel p = new JPanel(new GridLayout(0, 2, 8, 8));
        p.add(new JLabel("Pag-IBIG ID: *")); p.add(idField);
        p.add(new JLabel("Heir Code: *")); p.add(codeField);
        p.add(new JLabel("Heir Name: *")); p.add(nameField);
        p.add(new JLabel("Relationship: *")); p.add(relField);
        p.add(new JLabel("Birth Date: *")); p.add(birthField);

        int result = JOptionPane.showConfirmDialog(this, p, rowIndex < 0 ? "Add Heir" : "Edit Heir", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            String rawId = idField.getText().trim();
            if (rawId.isEmpty() || codeField.getText().trim().isEmpty() || nameField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Validation Error: Missing fields.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (rowIndex < 0) {
                boolean exist = dataStore.getMembers().stream().anyMatch(m -> m.getPagibigId().equals(rawId));
                if (!exist) { JOptionPane.showMessageDialog(this, "Error: Pag-IBIG ID does not exist.", "Error", JOptionPane.ERROR_MESSAGE); return; }
            }

            com.pagibig.model.HeirRecord record = new com.pagibig.model.HeirRecord(rawId, codeField.getText().trim(), nameField.getText().trim().toUpperCase(), relField.getText().trim().toUpperCase(), birthField.getText().trim());
            boolean success = rowIndex >= 0 ? dataStore.updateHeir(rowIndex, record) : dataStore.addHeir(record);
            handleSaveResult(success, "Heir");
        }
    }

    private void showGovernmentIdDialog(int rowIndex) {
        JTextField idField = new JTextField();
        JTextField tinField = new JTextField();
        JTextField sssField = new JTextField();
        JTextField crnField = new JTextField();
        JTextField emField = new JTextField();
        JTextField afpField = new JTextField();
        JTextField depedField = new JTextField();

        if (rowIndex >= 0) {
            com.pagibig.model.GovernmentIdRecord existing = dataStore.getGovernmentIds().get(rowIndex);
            idField.setText(existing.getPagibigId()); idField.setEditable(false);
            tinField.setText(existing.getTinNum());
            sssField.setText(existing.getSssNum());
            crnField.setText(existing.getCrn());
            emField.setText(existing.getEmNum());
            afpField.setText(existing.getAfpPnpNum());
            depedField.setText(existing.getDepedCode());
        }

        JPanel p = new JPanel(new GridLayout(0, 2, 8, 8));
        p.add(new JLabel("Pag-IBIG ID: *")); p.add(idField);
        p.add(new JLabel("TIN:")); p.add(tinField);
        p.add(new JLabel("SSS/GSIS:")); p.add(sssField);
        p.add(new JLabel("CRN:")); p.add(crnField);
        p.add(new JLabel("Employee Num:")); p.add(emField);
        p.add(new JLabel("AFP/PNP Code:")); p.add(afpField);
        p.add(new JLabel("DepEd Code:")); p.add(depedField);

        int result = JOptionPane.showConfirmDialog(this, p, rowIndex < 0 ? "Add Government IDs" : "Edit Government IDs", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            String rawId = idField.getText().trim();
            if (rawId.isEmpty()) { JOptionPane.showMessageDialog(this, "ID is mandatory.", "Error", JOptionPane.ERROR_MESSAGE); return; }

            if (rowIndex < 0) {
                boolean exist = dataStore.getMembers().stream().anyMatch(m -> m.getPagibigId().equals(rawId));
                if (!exist) { JOptionPane.showMessageDialog(this, "Error: Pag-IBIG ID does not exist.", "Error", JOptionPane.ERROR_MESSAGE); return; }
            }

            com.pagibig.model.GovernmentIdRecord record = new com.pagibig.model.GovernmentIdRecord(rawId, tinField.getText().trim(), sssField.getText().trim(), crnField.getText().trim(), emField.getText().trim(), afpField.getText().trim(), depedField.getText().trim());
            boolean success = rowIndex >= 0 ? dataStore.updateGovernmentId(rowIndex, record) : dataStore.addGovernmentId(record);
            handleSaveResult(success, "Government ID");
        }
    }

    private void showEmployerDialog(int rowIndex) {
        JTextField idField = new JTextField();
        JTextField nameField = new JTextField();
        JTextField addressField = new JTextField();

        if (rowIndex >= 0) {
            com.pagibig.model.EmployerRecord existing = dataStore.getEmployers().get(rowIndex);
            idField.setText(existing.getEmployerId()); idField.setEditable(false);
            nameField.setText(existing.getEmployerName());
            addressField.setText(existing.getEmployerAddress());
        }

        JPanel p = new JPanel(new GridLayout(0, 2, 8, 8));
        p.add(new JLabel("Employer ID: *")); p.add(idField);
        p.add(new JLabel("Employer Name: *")); p.add(nameField);
        p.add(new JLabel("Employer Address: *")); p.add(addressField);

        int result = JOptionPane.showConfirmDialog(this, p, rowIndex < 0 ? "Add Employer" : "Edit Employer", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            String empId = idField.getText().trim();
            if (empId.isEmpty() || nameField.getText().trim().isEmpty() || addressField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Missing required info.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            com.pagibig.model.EmployerRecord record = new com.pagibig.model.EmployerRecord(empId, nameField.getText().trim().toUpperCase(), addressField.getText().trim().toUpperCase());
            boolean success = rowIndex >= 0 ? dataStore.updateEmployer(rowIndex, record) : dataStore.addEmployer(record);
            handleSaveResult(success, "Employer");
        }
    }

    // Small helper to ensure model row bounds fall within proper context sorting filters
    private int modelToViewRowIndex(int index, JTable table) {
        if (index < 0) return -1;
        try { return table.convertRowIndexToModel(index); } catch (Exception e) { return index; }
    }

    private java.util.List<EmploymentRecord> gatherWizardEmploymentNodes(String trackingId) {
        java.util.List<EmploymentRecord> list = new ArrayList<>();
        int skipChoice = JOptionPane.showConfirmDialog(this, "Do you want to add Current Employment details?", "Step 3: Current Employment (Optional)", JOptionPane.YES_NO_OPTION);
        if (skipChoice != JOptionPane.YES_OPTION) return list;

        while (true) {
            JTextField empIdField = new JTextField();
            JComboBox<String> statusCombo = new JComboBox<>(new String[]{"-- Select Status --", "Permanent/Regular", "Casual", "Contractual", "Project-based", "Part-time/Temporary"});
            JTextField occField = new JTextField();
            JComboBox<String> officeCombo = new JComboBox<>(new String[]{"-- Select Office --", "Head Office", "Branch Office"});
            JTextField dateField = new JTextField(); JTextField incomeField = new JTextField();

            JPanel p = new JPanel(new GridLayout(0, 2, 8, 8));
            p.add(new JLabel("Employer ID: *")); p.add(empIdField); p.add(new JLabel("Status: *")); p.add(statusCombo);
            p.add(new JLabel("Occupation: *")); p.add(occField); p.add(new JLabel("Office Assignment: *")); p.add(officeCombo);
            p.add(new JLabel("Date Employed (YYYY-MM-DD): *")); p.add(dateField); p.add(new JLabel("Monthly Income: *")); p.add(incomeField);

            int res = JOptionPane.showConfirmDialog(this, p, "Wizard Step 3: Employment Logging", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (res != JOptionPane.OK_OPTION) return list;

            String eId = empIdField.getText().trim(); String occ = occField.getText().trim().toUpperCase();
            String dEmp = dateField.getText().trim(); String inc = incomeField.getText().trim();

            if (eId.isEmpty() || statusCombo.getSelectedIndex() <= 0 || occ.isEmpty() || officeCombo.getSelectedIndex() <= 0 || dEmp.isEmpty() || inc.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Validation Blocked: All elements are required if filling current employment records.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                continue;
            }

            // Verify if Employer Reference relation ID key physically exists inside data mapping matrices
            boolean existEmp = false;
            for (EmployerRecord emp : dataStore.getEmployers()) {
                if (emp.getEmployerId().equalsIgnoreCase(eId)) { existEmp = true; break; }
            }
            if (!existEmp) {
                JOptionPane.showMessageDialog(this, "Constraint Refused: Employer ID '" + eId + "' does not exist. Please register the employer first.", "Foreign Key Violation", JOptionPane.ERROR_MESSAGE);
                continue;
            }

            list.add(new EmploymentRecord(trackingId, eId, (String) statusCombo.getSelectedItem(), occ, (String) officeCombo.getSelectedItem(), dEmp, inc));
            int more = JOptionPane.showConfirmDialog(this, "Add another active concurrent job entry?", "Current Employment Block Multiple", JOptionPane.YES_NO_OPTION);
            if (more != JOptionPane.YES_OPTION) break;
        }
        return list;
    }

    private java.util.List<PreviousEmploymentRecord> gatherWizardPrevEmploymentNodes(String trackingId) {
        java.util.List<PreviousEmploymentRecord> list = new ArrayList<>();
        int skipChoice = JOptionPane.showConfirmDialog(this, "Do you want to add Previous Employment history?", "Step 4: Previous Employment (Optional)", JOptionPane.YES_NO_OPTION);
        if (skipChoice != JOptionPane.YES_OPTION) return list;

        while (true) {
            JTextField empIdField = new JTextField(); JTextField fromField = new JTextField(); JTextField toField = new JTextField();
            JComboBox<String> officeCombo = new JComboBox<>(new String[]{"-- Select Office --", "Head Office", "Branch Office"});

            JPanel p = new JPanel(new GridLayout(0, 2, 8, 8));
            p.add(new JLabel("Employer ID: *")); p.add(empIdField); p.add(new JLabel("Date From (YYYY-MM-DD): *")); p.add(fromField);
            p.add(new JLabel("Date To (YYYY-MM-DD): *")); p.add(toField); p.add(new JLabel("Office Assignment: *")); p.add(officeCombo);

            int res = JOptionPane.showConfirmDialog(this, p, "Wizard Step 4: History Record Log", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (res != JOptionPane.OK_OPTION) return list;

            String eId = empIdField.getText().trim(); String dFrom = fromField.getText().trim(); String dTo = toField.getText().trim();

            if (eId.isEmpty() || dFrom.isEmpty() || dTo.isEmpty() || officeCombo.getSelectedIndex() <= 0) {
                JOptionPane.showMessageDialog(this, "Validation Blocked: All elements are required if filling historical slots.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                continue;
            }

            boolean existEmp = false;
            for (EmployerRecord emp : dataStore.getEmployers()) {
                if (emp.getEmployerId().equalsIgnoreCase(eId)) { existEmp = true; break; }
            }
            if (!existEmp) {
                JOptionPane.showMessageDialog(this, "Constraint Refused: Employer ID '" + eId + "' does not exist inside system matrices.", "Foreign Key Violation", JOptionPane.ERROR_MESSAGE);
                continue;
            }

            list.add(new PreviousEmploymentRecord(trackingId, eId, dFrom, dTo, (String) officeCombo.getSelectedItem()));
            int more = JOptionPane.showConfirmDialog(this, "Add another past job tracking segment row?", "Historical Matrix Multiplier", JOptionPane.YES_NO_OPTION);
            if (more != JOptionPane.YES_OPTION) break;
        }
        return list;
    }

    private java.util.List<HeirRecord> gatherWizardHeirNodes(String trackingId) {
        java.util.List<HeirRecord> list = new ArrayList<>();
        JOptionPane.showMessageDialog(this, "Proceeding to Beneficiary Configuration Profile Setup. At least (1) is required.", "Step 5: Beneficiary Registry (Mandatory)", JOptionPane.INFORMATION_MESSAGE);

        int customHeirIndexCount = 1;
        while (true) {
            JTextField nameField = new JTextField(); JTextField relField = new JTextField(); JTextField bField = new JTextField();
            String programHeirCode = "H" + String.format("%03d", customHeirIndexCount);

            JPanel p = new JPanel(new GridLayout(0, 2, 8, 8));
            p.add(new JLabel("Generated Heir Code:")); p.add(new JLabel(programHeirCode));
            p.add(new JLabel("Heir Full Name: *")); p.add(nameField);
            p.add(new JLabel("Relationship: *")); p.add(relField);
            p.add(new JLabel("Date of Birth (YYYY-MM-DD): *")); p.add(bField);

            int res = JOptionPane.showConfirmDialog(this, p, "Wizard Step 5: Heir Verification Node", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (res != JOptionPane.OK_OPTION) {
                if (list.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Validation Violation Refused: You must supply at least one heir to construct user records.", "Mandatory Safeguard", JOptionPane.WARNING_MESSAGE);
                    continue;
                }
                break;
            }

            String hName = nameField.getText().trim().toUpperCase(); String hRel = relField.getText().trim().toUpperCase(); String hBirth = bField.getText().trim();
            if (hName.isEmpty() || hRel.isEmpty() || hBirth.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Validation Blocked: All elements inside beneficiary slots require string tracking data.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                continue;
            }

            list.add(new HeirRecord(trackingId, programHeirCode, hName, hRel, hBirth));
            customHeirIndexCount++;

            int more = JOptionPane.showConfirmDialog(this, "Add another dependent/beneficiary to this mapping record?", "Heir Card Array", JOptionPane.YES_NO_OPTION);
            if (more != JOptionPane.YES_OPTION) break;
        }
        return list;
    }

    private GovernmentIdRecord gatherWizardGovernmentIdNode(String trackingId) {
        int skipChoice = JOptionPane.showConfirmDialog(this, "Do you want to log Government Verification Keys/Identifications?", "Step 6: Government IDs (Optional)", JOptionPane.YES_NO_OPTION);
        if (skipChoice != JOptionPane.YES_OPTION) return new GovernmentIdRecord(trackingId, "", "", "", "", "", "");

        JTextField tinF = new JTextField(); JTextField sssF = new JTextField(); JTextField crnF = new JTextField();
        JTextField emF = new JTextField(); JTextField afpF = new JTextField(); JTextField depedF = new JTextField();

        JPanel p = new JPanel(new GridLayout(0, 2, 8, 8));
        p.add(new JLabel("TIN Number:")); p.add(tinF); p.add(new JLabel("SSS/GSIS Number:")); p.add(sssF);
        p.add(new JLabel("CRN:")); p.add(crnF); p.add(new JLabel("Employee Number:")); p.add(emF);
        p.add(new JLabel("AFP/PNP Serial Badge:")); p.add(afpF); p.add(new JLabel("DepEd Division Code:")); p.add(depedF);

        int res = JOptionPane.showConfirmDialog(this, p, "Wizard Step 6: Alternative References", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (res != JOptionPane.OK_OPTION) return new GovernmentIdRecord(trackingId, "", "", "", "", "", "");

        return new GovernmentIdRecord(trackingId, tinF.getText().trim(), sssF.getText().trim(), crnF.getText().trim(), emF.getText().trim(), afpF.getText().trim(), depedF.getText().trim());
    }

    // =====================================================================
    // 🔍 STANDALONE SINGLE TABLE LOGIC GATEWAY (With Relational Checks)
    // =====================================================================
    private void showSingleMemberEditDialog(int rowIndex) {
        JTextField idField = new JTextField();
        JTextField regisField = new JTextField();
        JComboBox<String> occCombo = new JComboBox<>(new String[]{"-- Select Status --", "EMPLOYED", "UNEMPLOYED"});
        JRadioButton firstTimeYes = new JRadioButton("YES"); JRadioButton firstTimeNo = new JRadioButton("NO");
        ButtonGroup firstTimeGroup = new ButtonGroup(); firstTimeGroup.add(firstTimeYes); firstTimeGroup.add(firstTimeNo); firstTimeNo.setSelected(true);
        JComboBox<String> typeCombo = new JComboBox<>(new String[]{"-- Select Membership Type --", "MANDATORY", "VOLUNTARY"});
        JComboBox<String> subtypeCombo = new JComboBox<>(new String[]{"-- Select Membership Type First --"});

        JLabel othersLabel = new JLabel("Specify Subtype: *"); JTextField othersTextField = new JTextField();
        JLabel workLabel = new JLabel("Type of Work: *"); JComboBox<String> workCombo = new JComboBox<>(new String[]{"-- Select Type of Work --", "Land-based", "Sea-based"});
        JLabel countryLabel = new JLabel("Type Country: *"); JTextField countryField = new JTextField();

        othersLabel.setVisible(false); othersTextField.setVisible(false);
        workLabel.setVisible(false); workCombo.setVisible(false); countryLabel.setVisible(false); countryField.setVisible(false);

        JPanel othersPanel = new JPanel(new GridLayout(0, 2, 8, 8)); othersPanel.add(othersLabel); othersPanel.add(othersTextField); othersPanel.setVisible(false);
        JPanel ofwPanel = new JPanel(new GridLayout(0, 2, 8, 8)); ofwPanel.add(workLabel); ofwPanel.add(workCombo); ofwPanel.add(countryLabel); ofwPanel.add(countryField); ofwPanel.setVisible(false);

        typeCombo.addActionListener(e -> {
            String selectedType = (String) typeCombo.getSelectedItem(); subtypeCombo.removeAllItems();
            if ("MANDATORY".equals(selectedType)) {
                subtypeCombo.addItem("-- Select Mandatory Subtype --"); subtypeCombo.addItem("EMPLOYED");
                subtypeCombo.addItem("OVERSEAS FILIPINO WORKER (OFW)"); subtypeCombo.addItem("SELF-EMPLOYED"); subtypeCombo.addItem("OTHERS");
            } else if ("VOLUNTARY".equals(selectedType)) {
                subtypeCombo.addItem("-- Select Voluntary Subtype --"); subtypeCombo.addItem("EMPLOYED");
                subtypeCombo.addItem("INDIVIDUAL PAYOR"); subtypeCombo.addItem("OTHERS");
            } else { subtypeCombo.addItem("-- Select Membership Type First --"); }
        });

        subtypeCombo.addActionListener(e -> {
            String selectedSub = (String) subtypeCombo.getSelectedItem(); if (selectedSub == null) return;
            boolean isOthers = "OTHERS".equals(selectedSub); boolean isOfw = "OVERSEAS FILIPINO WORKER (OFW)".equals(selectedSub);
            othersLabel.setVisible(isOthers); othersTextField.setVisible(isOthers); othersPanel.setVisible(isOthers);
            workLabel.setVisible(isOfw); workCombo.setVisible(isOfw); countryLabel.setVisible(isOfw); countryField.setVisible(isOfw); ofwPanel.setVisible(isOfw);
            SwingUtilities.getWindowAncestor(subtypeCombo).pack();
        });

        JTextField nameField = new JTextField(); JTextField fatField = new JTextField(); JTextField motField = new JTextField(); JTextField spouseField = new JTextField();
        JTextField certField = new JTextField(); JTextField birthField = new JTextField(); JTextField placeField = new JTextField();
        JRadioButton sexMale = new JRadioButton("Male"); JRadioButton sexFemale = new JRadioButton("Female");
        ButtonGroup sexGroup = new ButtonGroup(); sexGroup.add(sexMale); sexGroup.add(sexFemale); sexMale.setSelected(true);
        JTextField heightField = new JTextField(); JTextField weightField = new JTextField();
        JComboBox<String> maritalCombo = new JComboBox<>(new String[]{"-- Select Marital Status --", "Single / Unmarried", "Widow / er", "Annulled", "Married", "Legally Separated"});
        JTextField citizenshipField = new JTextField(); JTextField facialField = new JTextField();
        JComboBox<String> paymentCombo = new JComboBox<>(new String[]{"-- Select Frequency --", "Monthly", "Quarterly"});

        if (rowIndex >= 0) {
            MemberRecord existing = dataStore.getMembers().get(rowIndex);
            idField.setText(existing.getPagibigId()); idField.setEditable(false);
            regisField.setText(existing.getRegisNum());
            if ("EMPLOYED".equalsIgnoreCase(existing.getOccupationStatus())) occCombo.setSelectedIndex(1);
            else if ("UNEMPLOYED".equalsIgnoreCase(existing.getOccupationStatus())) occCombo.setSelectedIndex(2);
            if ("YES".equalsIgnoreCase(existing.getFirstTime())) firstTimeYes.setSelected(true); else firstTimeNo.setSelected(true);
            typeCombo.setSelectedItem(existing.getMemType());
            
            String sub = existing.getMemSubtype();
            if (sub != null) {
                boolean standardMatch = false;
                for (int i = 0; i < subtypeCombo.getItemCount(); i++) {
                    if (subtypeCombo.getItemAt(i).equals(sub)) { subtypeCombo.setSelectedItem(sub); standardMatch = true; break; }
                }
                if (!standardMatch && !sub.isEmpty()) {
                    subtypeCombo.setSelectedItem("OTHERS"); othersTextField.setText(sub);
                    othersPanel.setVisible(true); othersLabel.setVisible(true); othersTextField.setVisible(true);
                }
            }
            workCombo.setSelectedItem(existing.getTypeWork()); countryField.setText(existing.getTypeCountry());
            if ("OVERSEAS FILIPINO WORKER (OFW)".equals(sub)) {
                ofwPanel.setVisible(true); workLabel.setVisible(true); workCombo.setVisible(true); countryLabel.setVisible(true); countryField.setVisible(true);
            }
            nameField.setText(existing.getMemName()); fatField.setText(existing.getFatName()); motField.setText(existing.getMotName()); spouseField.setText(existing.getSpouseName());
            certField.setText(existing.getMemCertName()); birthField.setText(existing.getBirthDate()); placeField.setText(existing.getPlaceOfBirth());
            if ("M".equalsIgnoreCase(existing.getSex())) sexMale.setSelected(true); else sexFemale.setSelected(true);
            heightField.setText(existing.getHeight()); weightField.setText(existing.getWeight());
            switch (existing.getMaritalStatus()) { case "S" -> maritalCombo.setSelectedIndex(1); case "W" -> maritalCombo.setSelectedIndex(2); case "A" -> maritalCombo.setSelectedIndex(3); case "M" -> maritalCombo.setSelectedIndex(4); case "LS" -> maritalCombo.setSelectedIndex(5); }
            citizenshipField.setText(existing.getCitizenship()); facialField.setText(existing.getFacialFeatures()); paymentCombo.setSelectedItem(existing.getFrequencyOfPayment());
        }

        JPanel topPanel = new JPanel(new GridLayout(0, 2, 8, 8));
        topPanel.add(new JLabel("Pag-IBIG ID: *")); topPanel.add(idField); topPanel.add(new JLabel("Registration Num: *")); topPanel.add(regisField);
        topPanel.add(new JLabel("Occupation Status: *")); topPanel.add(occCombo); topPanel.add(new JLabel("First Time Member?: *"));
        JPanel radioFirstTime = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0)); radioFirstTime.add(firstTimeYes); radioFirstTime.add(firstTimeNo); topPanel.add(radioFirstTime);
        topPanel.add(new JLabel("Member Type: *")); topPanel.add(typeCombo); topPanel.add(new JLabel("Member Subtype: *")); topPanel.add(subtypeCombo);

        JPanel bottomPanel = new JPanel(new GridLayout(0, 2, 8, 8));
        bottomPanel.add(new JLabel("Member Name: *")); bottomPanel.add(nameField); bottomPanel.add(new JLabel("Father's Name:")); bottomPanel.add(fatField);
        bottomPanel.add(new JLabel("Mother's Name: *")); bottomPanel.add(motField); bottomPanel.add(new JLabel("Spouse Name:")); bottomPanel.add(spouseField);
        bottomPanel.add(new JLabel("Certificate Name:")); bottomPanel.add(certField); bottomPanel.add(new JLabel("Birth Date (YYYY-MM-DD): *")); bottomPanel.add(birthField);
        bottomPanel.add(new JLabel("Place of Birth: *")); bottomPanel.add(placeField); bottomPanel.add(new JLabel("Sex: *"));
        JPanel radioSex = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0)); radioSex.add(sexMale); radioSex.add(sexFemale); bottomPanel.add(radioSex);
        bottomPanel.add(new JLabel("Height (cm):")); bottomPanel.add(heightField); bottomPanel.add(new JLabel("Weight (kg):")); bottomPanel.add(weightField);
        bottomPanel.add(new JLabel("Marital Status: *")); bottomPanel.add(maritalCombo); bottomPanel.add(new JLabel("Citizenship: *")); bottomPanel.add(citizenshipField);
        bottomPanel.add(new JLabel("Facial Features:")); bottomPanel.add(facialField); bottomPanel.add(new JLabel("Frequency of Payment:")); bottomPanel.add(paymentCombo);

        JPanel mainPanel = new JPanel(); mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        topPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 4, 0)); bottomPanel.setBorder(BorderFactory.createEmptyBorder(4, 0, 0, 0));
        mainPanel.add(topPanel); mainPanel.add(othersPanel); mainPanel.add(ofwPanel); mainPanel.add(bottomPanel);

        int result = JOptionPane.showConfirmDialog(this, mainPanel, rowIndex < 0 ? "Add Member" : "Edit Member", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            String rawId = idField.getText().trim(); String rawRegis = regisField.getText().trim();
            String rawName = nameField.getText().trim().toUpperCase(); String rawMot = motField.getText().trim().toUpperCase();
            String rawBirth = birthField.getText().trim(); String rawPlace = placeField.getText().trim().toUpperCase();
            String rawCitizen = citizenshipField.getText().trim().toUpperCase();

            if (rawId.isEmpty() || rawRegis.isEmpty() || occCombo.getSelectedIndex() <= 0 || typeCombo.getSelectedIndex() <= 0 || 
                subtypeCombo.getSelectedIndex() <= 0 || rawName.isEmpty() || rawMot.isEmpty() || rawBirth.isEmpty() || rawPlace.isEmpty() || 
                maritalCombo.getSelectedIndex() <= 0 || rawCitizen.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Validation Blocked: Missing mandatory fields marked with (*).", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String selectedSubtype = (String) subtypeCombo.getSelectedItem();
            if ("OTHERS".equals(selectedSubtype)) {
                selectedSubtype = othersTextField.getText().trim().toUpperCase();
                if (selectedSubtype.isEmpty()) { JOptionPane.showMessageDialog(this, "Validation Blocked: Specify custom Subtype.", "Validation Error", JOptionPane.ERROR_MESSAGE); return; }
            }

            String finalWork = ""; String finalCountry = "";
            if ("OVERSEAS FILIPINO WORKER (OFW)".equals((String) subtypeCombo.getSelectedItem())) {
                if (workCombo.getSelectedIndex() <= 0 || countryField.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Validation Blocked: Work type and Country required for OFWs.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                finalWork = (String) workCombo.getSelectedItem(); finalCountry = countryField.getText().trim().toUpperCase();
            }

            String finalMarital = switch (maritalCombo.getSelectedIndex()) { case 1 -> "S"; case 2 -> "W"; case 3 -> "A"; case 4 -> "M"; case 5 -> "LS"; default -> ""; };
            MemberRecord record = new MemberRecord(rawId, rawRegis, (String) occCombo.getSelectedItem(), firstTimeYes.isSelected() ? "YES" : "NO", (String) typeCombo.getSelectedItem(), selectedSubtype,
                    finalWork, finalCountry, rawName, fatField.getText().trim().toUpperCase(), rawMot, spouseField.getText().trim().toUpperCase(), certField.getText().trim().toUpperCase(),
                    rawBirth, rawPlace, sexMale.isSelected() ? "M" : "F", heightField.getText().trim(), weightField.getText().trim(), finalMarital, rawCitizen, facialField.getText().trim().toUpperCase(),
                    paymentCombo.getSelectedIndex() == 0 ? "" : (String) paymentCombo.getSelectedItem());

            boolean success = rowIndex >= 0 ? dataStore.updateMember(rowIndex, record) : dataStore.addMember(record);
            handleSaveResult(success, "Member");
        }
    }

    // =====================================================================
    // 🔍 COMPONENT DIALOG SPECIFICATIONS FOR CONTACT DETAILS
    // =====================================================================
    private void showContactDialog(int rowIndex) {
        JTextField idField = new JTextField(); JTextField cellField = new JTextField(); JTextField homeField = new JTextField();
        JTextField directField = new JTextField(); JTextField trunkField = new JTextField(); JTextField emailField = new JTextField();
        JTextField permField = new JTextField(); JTextField presentField = new JTextField();
        JComboBox<String> prefCombo = new JComboBox<>(new String[]{"-- Select Preferred Address --", "Present Home Address", "Permanent Home Address", "Employer/Business Address"});
        JCheckBox syncCb = new JCheckBox("Present Address is the same as Permanent Home Address");

        syncCb.addActionListener(e -> {
            if (syncCb.isSelected()) { presentField.setText(permField.getText()); presentField.setEnabled(false); }
            else { presentField.setEnabled(true); presentField.setText(""); }
        });

        permField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { if(syncCb.isSelected()) presentField.setText(permField.getText()); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { if(syncCb.isSelected()) presentField.setText(permField.getText()); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { if(syncCb.isSelected()) presentField.setText(permField.getText()); }
        });

        if (rowIndex >= 0) {
            ContactRecord existing = dataStore.getContacts().get(rowIndex);
            idField.setText(existing.getPagibigId()); idField.setEditable(false);
            cellField.setText(existing.getCellNum()); homeField.setText(existing.getHomeNum()); directField.setText(existing.getBusinessDirect());
            trunkField.setText(existing.getBusinessTrunk()); emailField.setText(existing.getEmailAddress()); permField.setText(existing.getPermAddress());
            presentField.setText(existing.getPresentAddress()); prefCombo.setSelectedItem(existing.getPrefMailAddress());
            if (existing.getPermAddress() != null && existing.getPermAddress().equals(existing.getPresentAddress()) && !existing.getPermAddress().isEmpty()) {
                syncCb.setSelected(true); presentField.setEnabled(false);
            }
        }

        JPanel p = new JPanel(new GridLayout(0, 2, 8, 8));
        p.add(new JLabel("Pag-IBIG ID: *")); p.add(idField); p.add(new JLabel("Cell Number: *")); p.add(cellField);
        p.add(new JLabel("Home Number:")); p.add(homeField); p.add(new JLabel("Business Direct:")); p.add(directField);
        p.add(new JLabel("Business Trunk:")); p.add(trunkField); p.add(new JLabel("Email Address:")); p.add(emailField);
        p.add(new JLabel("Permanent Address: *")); p.add(permField); p.add(syncCb); p.add(new JLabel(""));
        p.add(new JLabel("Present Address: *")); p.add(presentField); p.add(new JLabel("Preferred Mail Address: *")); p.add(prefCombo);

        int result = JOptionPane.showConfirmDialog(this, p, rowIndex < 0 ? "Add Contact" : "Edit Contact", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            String rawId = idField.getText().trim(); String cell = cellField.getText().trim();
            String perm = permField.getText().trim().toUpperCase(); String present = syncCb.isSelected() ? perm : presentField.getText().trim().toUpperCase();

            if (rawId.isEmpty() || cell.isEmpty() || perm.isEmpty() || present.isEmpty() || prefCombo.getSelectedIndex() <= 0) {
                JOptionPane.showMessageDialog(this, "Validation Blocked: Missing mandatory contact elements.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // REFERENTIAL INTEGRITY VALVE: Verify target Member primary key link exists
            if (rowIndex < 0) {
                boolean exist = false;
                for (MemberRecord m : dataStore.getMembers()) { if (m.getPagibigId().equals(rawId)) { exist = true; break; } }
                if (!exist) {
                    JOptionPane.showMessageDialog(this, "Save Refused: Pag-IBIG ID '" + rawId + "' does not exist in Member Registry.", "Foreign Key Constraint", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            ContactRecord record = new ContactRecord(rawId, cell, homeField.getText().trim(), directField.getText().trim(), trunkField.getText().trim(), emailField.getText().trim(), perm, present, (String) prefCombo.getSelectedItem());
            boolean success = rowIndex >= 0 ? dataStore.updateContact(rowIndex, record) : dataStore.addContact(record);
            handleSaveResult(success, "Contact");
        }
    }

    // =====================================================================
    // 🗑️ TRANSACTIONAL SYSTEM REMOVAL CONTROLS (With Cascaded Invalidation)
    // =====================================================================
    private void deleteSelection(JTable table, String label) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a row first.", "No Selection", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int modelRow = table.convertRowIndexToModel(selectedRow);
        String targetId = "";

        // Collect targeted relational keys safely
        if ("Member".equals(label)) targetId = dataStore.getMembers().get(modelRow).getPagibigId();
        else if ("Contact".equals(label)) targetId = dataStore.getContacts().get(modelRow).getPagibigId();
        else if ("Heir".equals(label)) targetId = dataStore.getHeirs().get(modelRow).getPagibigId();

        // PROTECTIVE SAFEGUARD VALVE A: Minimum Beneficiary Threshold Limit Check
        if ("Heir".equals(label)) {
            int beneficiaryMatchCounter = 0;
            for (HeirRecord h : dataStore.getHeirs()) {
                if (h.getPagibigId().equals(targetId)) beneficiaryMatchCounter++;
            }
            if (beneficiaryMatchCounter <= 1) {
                JOptionPane.showMessageDialog(this, "Validation Violation Refused: Member must retain at least one beneficiary inside the registry.", "Safeguard Constraint", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        // PROTECTIVE SAFEGUARD VALVE B: Cascaded Removal Core Notice Mapping
        String promptText = "Delete selected " + label + " record?";
        if ("Member".equals(label) || "Contact".equals(label)) {
            promptText = "WARNING: Wiping member '" + targetId + "' will execute CASCADED deletions\n" +
                         "across ALL associated profile tables (Employment, Heirs, Gov IDs) to prevent orphaned records.\n\n" +
                         "Are you absolutely sure you want to permanently delete this member?";
        }

        int choice = JOptionPane.showConfirmDialog(this, promptText, "Confirm System Deletion", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (choice != JOptionPane.YES_OPTION) return;

        boolean success;
        if ("Member".equals(label) || "Contact".equals(label)) {
            // Cascade cleanup across all 6 linked relations to uphold full data integrity
            dataStore.deleteGovernmentIdByMember(targetId);
            dataStore.deleteHeirsByMember(targetId);
            dataStore.deletePreviousEmploymentByMember(targetId);
            dataStore.deleteEmploymentByMember(targetId);
            dataStore.deleteContactByMember(targetId);
            success = dataStore.deleteMemberByKey(targetId);
        } else {
            // Isolated single-table remove operations
            success = switch (label) {
                case "Employment" -> dataStore.deleteEmployment(modelRow);
                case "Previous Employment" -> dataStore.deletePreviousEmployment(modelRow);
                case "Heir" -> dataStore.deleteHeir(modelRow);
                case "Government ID" -> dataStore.deleteGovernmentId(modelRow);
                case "Employer" -> dataStore.deleteEmployer(modelRow);
                default -> false;
            };
        }
        handleDeleteResult(success, label);
    }

    private void handleSaveResult(boolean success, String entityName) {
        if (success) {
            refreshAllTables();
            JOptionPane.showMessageDialog(this, entityName + " record successfully updated inside ledger matrices.", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Database Rejection: Failed to save " + entityName + ".\nEnsure ID linkages exist and fields match type assertions.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleDeleteResult(boolean success, String entityName) {
        if (success) {
            refreshAllTables();
            JOptionPane.showMessageDialog(this, entityName + " record successfully scrubbed off schemas.", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Database Execution Fault: Failed to drop " + entityName + " reference rows.\nIt may be bound by active cross-table constraints.", "Execution Failure", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void refreshAllTables() {
        refreshMemberTable();
        refreshContactTable();
        refreshEmploymentTable();
        refreshPreviousEmploymentTable();
        refreshHeirTable();
        refreshGovernmentIdTable();
        refreshEmployerTable();

        packColumnWidths(memberTable);
        packColumnWidths(contactTable);
        packColumnWidths(employmentTable);
        packColumnWidths(previousEmploymentTable);
        packColumnWidths(heirTable);
        packColumnWidths(governmentIdTable);
        packColumnWidths(employerTable);
    }

    private void refreshMemberTable() {
        memberModel.setRowCount(0);
        for (MemberRecord member : dataStore.getMembers()) {
            memberModel.addRow(new Object[]{
                member.getPagibigId(), member.getRegisNum(), member.getOccupationStatus(),
                member.getFirstTime(), member.getMemType(), member.getMemSubtype(),
                member.getTypeWork(), member.getTypeCountry(), member.getMemName(),
                member.getFatName(), member.getMotName(), member.getSpouseName(),
                member.getMemCertName(), member.getBirthDate(), member.getPlaceOfBirth(),
                member.getSex(), member.getHeight(), member.getWeight(),
                member.getMaritalStatus(), member.getCitizenship(), member.getFacialFeatures(),
                member.getFrequencyOfPayment()
            });
        }
        applySearchFilter();
    }

    private void refreshContactTable() {
        contactModel.setRowCount(0);
        for (ContactRecord contact : dataStore.getContacts()) {
            contactModel.addRow(new Object[]{contact.getPagibigId(), contact.getCellNum(), contact.getHomeNum(), contact.getBusinessDirect(), contact.getBusinessTrunk(), contact.getEmailAddress(), contact.getPermAddress(), contact.getPresentAddress(), contact.getPrefMailAddress()});
        }
        applySearchFilter();
    }

    private void refreshEmploymentTable() {
        employmentModel.setRowCount(0);
        for (EmploymentRecord employment : dataStore.getEmployments()) {
            employmentModel.addRow(new Object[]{employment.getPagibigId(), employment.getEmployerId(), employment.getEmploymentStatus(), employment.getOccupation(), employment.getOfficeAssignment(), employment.getDateEmployed(), employment.getMonthlyIncome()});
        }
        applySearchFilter();
    }

    private void refreshPreviousEmploymentTable() {
        previousEmploymentModel.setRowCount(0);
        for (PreviousEmploymentRecord previous : dataStore.getPreviousEmployments()) {
            previousEmploymentModel.addRow(new Object[]{previous.getPagibigId(), previous.getEmployerId(), previous.getDateFrom(), previous.getDateTo(), previous.getPrevOfficeAssignment()});
        }
        applySearchFilter();
    }

    private void refreshHeirTable() {
        heirModel.setRowCount(0);
        for (HeirRecord heir : dataStore.getHeirs()) {
            heirModel.addRow(new Object[]{heir.getPagibigId(), heir.getHeirCode(), heir.getHeirName(), heir.getRelationship(), heir.getHeirDateBirth()});
        }
        applySearchFilter();
    }

    private void refreshGovernmentIdTable() {
        governmentIdModel.setRowCount(0);
        for (GovernmentIdRecord governmentId : dataStore.getGovernmentIds()) {
            governmentIdModel.addRow(new Object[]{governmentId.getPagibigId(), governmentId.getTinNum(), governmentId.getSssNum(), governmentId.getCrn(), governmentId.getEmNum(), governmentId.getAfpPnpNum(), governmentId.getDepedCode()});
        }
        applySearchFilter();
    }

    private void refreshEmployerTable() {
        employerModel.setRowCount(0);
        for (EmployerRecord employer : dataStore.getEmployers()) {
            employerModel.addRow(new Object[]{employer.getEmployerId(), employer.getEmployerName(), employer.getEmployerAddress()});
        }
        applySearchFilter();
    }

    private void applySearchFilter() {
        String query = searchField.getText().trim();
        if (query.isEmpty()) {
            memberSorter.setRowFilter(null);
            contactSorter.setRowFilter(null);
            employmentSorter.setRowFilter(null);
            previousEmploymentSorter.setRowFilter(null);
            heirSorter.setRowFilter(null);
            governmentIdSorter.setRowFilter(null);
            employerSorter.setRowFilter(null);
            return;
        }

        RowFilter<DefaultTableModel, Object> filter = RowFilter.regexFilter("(?i)" + Pattern.quote(query));
        int selectedIndex = tabbedPane.getSelectedIndex();
        memberSorter.setRowFilter(null);
        contactSorter.setRowFilter(null);
        employmentSorter.setRowFilter(null);
        previousEmploymentSorter.setRowFilter(null);
        heirSorter.setRowFilter(null);
        governmentIdSorter.setRowFilter(null);
        employerSorter.setRowFilter(null);

        switch (selectedIndex) {
            case 0 -> memberSorter.setRowFilter(filter);
            case 1 -> contactSorter.setRowFilter(filter);
            case 2 -> employmentSorter.setRowFilter(filter);
            case 3 -> previousEmploymentSorter.setRowFilter(filter);
            case 4 -> heirSorter.setRowFilter(filter);
            case 5 -> governmentIdSorter.setRowFilter(filter);
            case 6 -> employerSorter.setRowFilter(filter);
        }
    }

    private void packColumnWidths(JTable table) {
        // Iterate through every column in the table
        for (int column = 0; column < table.getColumnCount(); column++) {
            int maxWidth = 0;

            // Check the width of the column header
            Object headerValue = table.getColumnModel().getColumn(column).getHeaderValue();
            java.awt.Component headerComp = table.getTableHeader().getDefaultRenderer()
                    .getTableCellRendererComponent(table, headerValue, false, false, -1, column);
            maxWidth = Math.max(maxWidth, headerComp.getPreferredSize().width);

            // Iterate through every row to find the longest input in this column
            for (int row = 0; row < table.getRowCount(); row++) {
                java.awt.Component cellComp = table.getCellRenderer(row, column)
                        .getTableCellRendererComponent(table, table.getValueAt(row, column), false, false, row, column);
                maxWidth = Math.max(maxWidth, cellComp.getPreferredSize().width);
            }

            // Set the preferred width with a small padding (e.g., 10 pixels) so text isn't cramped
            table.getColumnModel().getColumn(column).setPreferredWidth(maxWidth + 10);
        }
    }
}
