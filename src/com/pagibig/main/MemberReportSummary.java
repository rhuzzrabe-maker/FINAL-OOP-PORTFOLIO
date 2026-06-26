package com.pagibig.main;

import com.pagibig.data.DataStore;
import com.pagibig.model.MemberRecord;
import com.pagibig.model.PreviousEmploymentRecord;
import com.pagibig.model.EmploymentRecord;
import java.awt.*;
import java.time.LocalDate;
import java.time.Period;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class MemberReportSummary {
    private final DataStore dataStore;
    private final Component parentComponent;

    public MemberReportSummary(DataStore dataStore, Component parentComponent) {
        this.dataStore = dataStore;
        this.parentComponent = parentComponent;
    }

    public void generateAndDisplayReport() {
        java.util.List<MemberRecord> members = dataStore.getMembers();
        int totalMembers = members.size();

        // 1. Gather & Calculate Metrics
        int employedCount = 0;
        int unemployedCount = 0;
        int maleCount = 0;
        int femaleCount = 0;
        int firstTimeCount = 0;
        
        // Age bracket counters
        int under20 = 0, age20to29 = 0, age30to39 = 0, age40to49 = 0, age50plus = 0;

        Map<String, Integer> subtypeMap = new HashMap<>();
        
        // Track unique IDs with employment history to avoid duplicate counts
        Set<String> experiencedMemberIds = new HashSet<>();
        for (PreviousEmploymentRecord p : dataStore.getPreviousEmployments()) {
            if (p.getPagibigId() != null) {
                experiencedMemberIds.add(p.getPagibigId().trim().toUpperCase());
            }
        }
        for (EmploymentRecord e : dataStore.getEmployments()) {
            if (e.getPagibigId() != null) {
                experiencedMemberIds.add(e.getPagibigId().trim().toUpperCase());
            }
        }

        LocalDate icCurrentDate = LocalDate.now();

        for (MemberRecord m : members) {
            // Occupation/Current Work Counting
            String occStatus = m.getOccupationStatus() != null ? m.getOccupationStatus().toUpperCase() : "";
            if (occStatus.contains("UNEMPLOYED")) {
                unemployedCount++;
            } else if (occStatus.contains("EMPLOYED")) {
                employedCount++;
            }

            // Sex Counting
            String sex = m.getSex() != null ? m.getSex().toUpperCase() : "";
            if ("M".equals(sex) || "MALE".equals(sex)) maleCount++;
            else if ("F".equals(sex) || "FEMALE".equals(sex)) femaleCount++;

            // First-Time Counters
            String firstTime = m.getFirstTime() != null ? m.getFirstTime().toUpperCase() : "";
            if ("YES".equals(firstTime)) firstTimeCount++;

            // Subtype Mapping Counting
            String subtype = m.getMemSubtype() != null ? m.getMemSubtype().trim().toUpperCase() : "UNSPECIFIED";
            if (subtype.isEmpty()) subtype = "UNSPECIFIED";
            subtypeMap.put(subtype, subtypeMap.getOrDefault(subtype, 0) + 1);

            // Dynamic Age Evaluation Logic
            String bDateStr = m.getBirthDate();
            if (bDateStr != null && bDateStr.matches("\\d{4}-\\d{2}-\\d{2}")) {
                try {
                    LocalDate bDate = LocalDate.parse(bDateStr);
                    int age = Period.between(bDate, icCurrentDate).getYears();
                    if (age < 20) under20++;
                    else if (age <= 29) age20to29++;
                    else if (age <= 39) age30to39++;
                    else if (age <= 49) age40to49++;
                    else age50plus++;
                } catch (Exception ignored) {}
            } else {
                age30to39++; // Standard fallback default interval grouping for empty/hint elements
            }
        }

        // Calculate Experience & Government IDs metrics
        int experiencedCount = 0;
        for (MemberRecord m : members) {
            String mId = m.getPagibigId() != null ? m.getPagibigId().trim().toUpperCase() : "";
            if (experiencedMemberIds.contains(mId)) {
                experiencedCount++;
            }
        }
        int governmentIdCount = dataStore.getGovernmentIds().size();

        // Calculate Percentages safely
        double employedPct = totalMembers > 0 ? ((double) employedCount / totalMembers) * 100 : 0;
        double unemployedPct = totalMembers > 0 ? ((double) unemployedCount / totalMembers) * 100 : 0;
        double sexMalePct = totalMembers > 0 ? ((double) maleCount / totalMembers) * 100 : 0;
        double sexFemalePct = totalMembers > 0 ? ((double) femaleCount / totalMembers) * 100 : 0;
        double firstTimePct = totalMembers > 0 ? ((double) firstTimeCount / totalMembers) * 100 : 0;
        double workingPct = totalMembers > 0 ? ((double) employedCount / totalMembers) * 100 : 0;
        double experiencedPct = totalMembers > 0 ? ((double) experiencedCount / totalMembers) * 100 : 0;

        // 2. Build GUI Panel Frame
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(Color.WHITE);

        // --- HEADER BLOCK ---
        JLabel titleLabel = new JLabel("PAG-IBIG MEMBER DATABASE SYSTEM", JLabel.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setForeground(new Color(20, 40, 80));

        JLabel subtitleLabel = new JLabel("EXECUTIVE SUMMARY REPORT PROFILE", JLabel.CENTER);
        subtitleLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitleLabel.setForeground(Color.GRAY);

        mainPanel.add(titleLabel);
        mainPanel.add(subtitleLabel);
        mainPanel.add(Box.createVerticalStrut(12));
        mainPanel.add(new JSeparator());
        mainPanel.add(Box.createVerticalStrut(12));

        // --- METRICS SEGMENT (Matched exactly to your styling specification) ---
        JPanel metricsPanel = new JPanel(new GridLayout(7, 1, 6, 6));
        metricsPanel.setBackground(Color.WHITE);
        metricsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel totalLabel = new JLabel(String.format("<html><b>Total Registered Members:</b> %d</html>", totalMembers));
        totalLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        
        JLabel empLabel = new JLabel(String.format("<html><b>Employment Status Breakdown:</b> Employed: %d (%.2f%%) | Unemployed: %d (%.2f%%)</html>", 
                employedCount, employedPct, unemployedCount, unemployedPct));
        empLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));

        JLabel sexLabel = new JLabel(String.format("<html><b>Sex Demographics Allocation:</b> Male: %d (%.2f%%) | Female: %d (%.2f%%)</html>", 
                maleCount, sexMalePct, femaleCount, sexFemalePct));
        sexLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));

        JLabel firstTimeLabel = new JLabel(String.format("<html><b>First-Time Members:</b> %d (%.2f%%)</html>", firstTimeCount, firstTimePct));
        firstTimeLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));

        JLabel workingLabel = new JLabel(String.format("<html><b>Currently Working Status:</b> %d (%.2f%%)</html>", employedCount, workingPct));
        workingLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));

        JLabel expLabel = new JLabel(String.format("<html><b>Has Work Experience:</b> %d (%.2f%%)</html>", experiencedCount, experiencedPct));
        expLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));

        JLabel govIdLabel = new JLabel(String.format("<html><b>Government IDs Tracked:</b> %d Profile(s)</html>", governmentIdCount));
        govIdLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));

        metricsPanel.add(totalLabel);
        metricsPanel.add(empLabel);
        metricsPanel.add(sexLabel);
        metricsPanel.add(firstTimeLabel);
        metricsPanel.add(workingLabel);
        metricsPanel.add(expLabel);
        metricsPanel.add(govIdLabel);

        mainPanel.add(metricsPanel);
        mainPanel.add(Box.createVerticalStrut(20));

        // --- SHARED CELL RENDERING CONFIGURATION ---
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        
        DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
        leftRenderer.setHorizontalAlignment(JLabel.LEFT);
        leftRenderer.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 0));

        // --- SUBTYPE MATRIX DISTRIBUTION TABLE ---
        JLabel tableTitle = new JLabel("Member Subtype Statistical Matrix Distribution");
        tableTitle.setFont(new Font("SansSerif", Font.BOLD, 12));
        tableTitle.setForeground(new Color(20, 40, 80));
        tableTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(tableTitle);
        mainPanel.add(Box.createVerticalStrut(6));

        DefaultTableModel subtypeModel = new DefaultTableModel(new Object[]{"MEMBER SUBTYPE", "RECORD COUNT", "PROPORTION PERCENTAGE"}, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };

        for (Map.Entry<String, Integer> entry : subtypeMap.entrySet()) {
            double pct = totalMembers > 0 ? ((double) entry.getValue() / totalMembers) * 100 : 0;
            subtypeModel.addRow(new Object[]{
                entry.getKey(), 
                String.valueOf(entry.getValue()), 
                String.format("%.2f%%", pct)      
            });
        }

        JTable subtypeTable = createStyledTable(subtypeModel, leftRenderer, centerRenderer);
        JScrollPane subtypeScroll = new JScrollPane(subtypeTable);
        subtypeScroll.setPreferredSize(new Dimension(640, 130));
        mainPanel.add(subtypeScroll);
        
        mainPanel.add(Box.createVerticalStrut(15));

        // --- AGE INTERVAL BRACKET DISTRIBUTION TABLE ---
        JLabel ageTitle = new JLabel("Demographic Age Interval Bracket Distribution");
        ageTitle.setFont(new Font("SansSerif", Font.BOLD, 12));
        ageTitle.setForeground(new Color(20, 40, 80));
        ageTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(ageTitle);
        mainPanel.add(Box.createVerticalStrut(6));

        DefaultTableModel ageModel = new DefaultTableModel(new Object[]{"AGE INTERVAL BRACKET", "RECORD COUNT", "PROPORTION PERCENTAGE"}, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };

        addAgeRow(ageModel, "Under 20 Years Old", under20, totalMembers);
        addAgeRow(ageModel, "20 - 29 Years Old", age20to29, totalMembers);
        addAgeRow(ageModel, "30 - 39 Years Old", age30to39, totalMembers);
        addAgeRow(ageModel, "40 - 49 Years Old", age40to49, totalMembers);
        addAgeRow(ageModel, "50 Years Old & Above", age50plus, totalMembers);

        JTable ageTable = createStyledTable(ageModel, centerRenderer, centerRenderer);
        JScrollPane ageScroll = new JScrollPane(ageTable);
        ageScroll.setPreferredSize(new Dimension(640, 130));
        mainPanel.add(ageScroll);

        // 3. Render Modal Dialog Frame View Box
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(parentComponent), "System Analytics Dashboard Report", true);
        dialog.setContentPane(mainPanel);
        dialog.pack();
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(parentComponent);
        dialog.setVisible(true);
    }

    private JTable createStyledTable(DefaultTableModel model, DefaultTableCellRenderer col0Renderer, DefaultTableCellRenderer numRenderer) {
        JTable table = new JTable(model);
        table.setRowHeight(24);
        
        table.setFont(new Font("SansSerif", Font.PLAIN, 12));
        table.setGridColor(new Color(230, 230, 230));
        table.setShowGrid(true);

        Font plainFont = new Font("SansSerif", Font.PLAIN, 12);
        col0Renderer.setFont(plainFont);
        numRenderer.setFont(plainFont);

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("SansSerif", Font.BOLD, 11));
        header.setBackground(new Color(240, 244, 250));
        header.setForeground(new Color(20, 40, 80));

        table.getColumnModel().getColumn(0).setCellRenderer(col0Renderer);
        table.getColumnModel().getColumn(1).setCellRenderer(numRenderer);
        table.getColumnModel().getColumn(2).setCellRenderer(numRenderer);
        return table;
    }

    private void addAgeRow(DefaultTableModel model, String bracketName, int count, int total) {
        double pct = total > 0 ? ((double) count / total) * 100 : 0;
        model.addRow(new Object[]{
            bracketName, 
            String.valueOf(count),        
            String.format("%.2f%%", pct)  
        });
    }
}