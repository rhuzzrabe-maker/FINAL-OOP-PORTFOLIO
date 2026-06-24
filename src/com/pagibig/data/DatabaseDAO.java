package com.pagibig.data;

import com.pagibig.model.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Database Access Object for all Pag-IBIG entities.
 * Fully synchronized with the actual MySQL schema names.
 */
public class DatabaseDAO {

    // ==================== MEMBERS ====================

    public List<MemberRecord> loadAllMembers() {
        List<MemberRecord> list = new ArrayList<>();
        String sql = "SELECT Pagibig_ID, mem_name, mem_type, sex, birth_date, citizenship FROM member";
        try (Connection conn = DataConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new MemberRecord(
                        rs.getString("Pagibig_ID"),
                        rs.getString("mem_name"),
                        rs.getString("mem_type"),
                        rs.getString("sex"),
                        rs.getString("birth_date"),
                        rs.getString("citizenship")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error loading members: " + e.getMessage());
        }
        return list;
    }

    public boolean insertMember(MemberRecord m) {
        String sql = "INSERT INTO member (Pagibig_ID, mem_name, mem_type, sex, birth_date, citizenship) VALUES (?,?,?,?,?,?)";
        try (Connection conn = DataConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, m.getPagibigId());
            ps.setString(2, m.getMemName());
            ps.setString(3, m.getMemType());
            ps.setString(4, m.getSex());
            ps.setString(5, m.getBirthDate());
            ps.setString(6, m.getCitizenship());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error inserting member: " + e.getMessage());
            return false;
        }
    }

    public boolean updateMember(MemberRecord m) {
        String sql = "UPDATE member SET mem_name=?, mem_type=?, sex=?, birth_date=?, citizenship=? WHERE Pagibig_ID=?";
        try (Connection conn = DataConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, m.getMemName());
            ps.setString(2, m.getMemType());
            ps.setString(3, m.getSex());
            ps.setString(4, m.getBirthDate());
            ps.setString(5, m.getCitizenship());
            ps.setString(6, m.getPagibigId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating member: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteMember(String pagibigId) {
        String sql = "DELETE FROM member WHERE Pagibig_ID=?";
        try (Connection conn = DataConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, pagibigId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting member: " + e.getMessage());
            return false;
        }
    }

    // ==================== CONTACTS ====================

    public List<ContactRecord> loadAllContacts() {
        List<ContactRecord> list = new ArrayList<>();
        String sql = "SELECT Pagibig_ID, cell_num, home_num, business_direct, business_trunk, email_address, perm_address, present_address, pref_mail_address FROM contact";
        try (Connection conn = DataConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new ContactRecord(
                        rs.getString("Pagibig_ID"),
                        rs.getString("cell_num"),
                        rs.getString("home_num"),
                        rs.getString("business_direct"),
                        rs.getString("business_trunk"),
                        rs.getString("email_address"),
                        rs.getString("perm_address"),
                        rs.getString("present_address"),
                        rs.getString("pref_mail_address")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error loading contacts: " + e.getMessage());
        }
        return list;
    }

    public boolean insertContact(ContactRecord c) {
        String sql = "INSERT INTO contact (Pagibig_ID, cell_num, home_num, business_direct, business_trunk, email_address, perm_address, present_address, pref_mail_address) VALUES (?,?,?,?,?,?,?,?,?)";
        try (Connection conn = DataConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getPagibigId());
            ps.setString(2, c.getCellNum());
            ps.setString(3, c.getHomeNum());
            ps.setString(4, c.getBusinessDirect());
            ps.setString(5, c.getBusinessTrunk());
            ps.setString(6, c.getEmailAddress());
            ps.setString(7, c.getPermAddress());
            ps.setString(8, c.getPresentAddress());
            ps.setString(9, c.getPrefMailAddress());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error inserting contact: " + e.getMessage());
            return false;
        }
    }

    public boolean updateContact(ContactRecord c) {
        String sql = "UPDATE contact SET cell_num=?, home_num=?, business_direct=?, business_trunk=?, email_address=?, perm_address=?, present_address=?, pref_mail_address=? WHERE Pagibig_ID=?";
        try (Connection conn = DataConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getCellNum());
            ps.setString(2, c.getHomeNum());
            ps.setString(3, c.getBusinessDirect());
            ps.setString(4, c.getBusinessTrunk());
            ps.setString(5, c.getEmailAddress());
            ps.setString(6, c.getPermAddress());
            ps.setString(7, c.getPresentAddress());
            ps.setString(8, c.getPrefMailAddress());
            ps.setString(9, c.getPagibigId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating contact: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteContact(String pagibigId) {
        String sql = "DELETE FROM contact WHERE Pagibig_ID=?";
        try (Connection conn = DataConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, pagibigId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting contact: " + e.getMessage());
            return false;
        }
    }

    // ==================== EMPLOYMENT ====================

    public List<EmploymentRecord> loadAllEmployment() {
        List<EmploymentRecord> list = new ArrayList<>();
        String sql = "SELECT Pagibig_ID, employer_id, employment_status, occupation, office_assignment, date_employed, Monthly_Income FROM employment";
        try (Connection conn = DataConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new EmploymentRecord(
                        rs.getString("Pagibig_ID"),
                        rs.getString("employer_id"),
                        rs.getString("employment_status"),
                        rs.getString("occupation"),
                        rs.getString("office_assignment"),
                        rs.getString("date_employed"),
                        rs.getString("Monthly_Income")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error loading employment: " + e.getMessage());
        }
        return list;
    }

    public boolean insertEmployment(EmploymentRecord e) {
        String sql = "INSERT INTO employment (Pagibig_ID, employer_id, employment_status, occupation, office_assignment, date_employed, Monthly_Income) VALUES (?,?,?,?,?,?,?)";
        try (Connection conn = DataConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, e.getPagibigId());
            ps.setString(2, e.getEmployerId());
            ps.setString(3, e.getEmploymentStatus());
            ps.setString(4, e.getOccupation());
            ps.setString(5, e.getOfficeAssignment());
            ps.setString(6, e.getDateEmployed());
            ps.setString(7, e.getMonthlyIncome());
            return ps.executeUpdate() > 0;
        } catch (SQLException e2) {
            System.err.println("Error inserting employment: " + e2.getMessage());
            return false;
        }
    }

    public boolean updateEmployment(EmploymentRecord e) {
        String sql = "UPDATE employment SET employment_status=?, occupation=?, office_assignment=?, date_employed=?, Monthly_Income=? WHERE Pagibig_ID=? AND employer_id=?";
        try (Connection conn = DataConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, e.getEmploymentStatus());
            ps.setString(2, e.getOccupation());
            ps.setString(3, e.getOfficeAssignment());
            ps.setString(4, e.getDateEmployed());
            ps.setString(5, e.getMonthlyIncome());
            ps.setString(6, e.getPagibigId());
            ps.setString(7, e.getEmployerId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e2) {
            System.err.println("Error updating employment: " + e2.getMessage());
            return false;
        }
    }

    public boolean deleteEmployment(String pagibigId, String employerId) {
        String sql = "DELETE FROM employment WHERE Pagibig_ID=? AND employer_id=?";
        try (Connection conn = DataConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, pagibigId);
            ps.setString(2, employerId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting employment: " + e.getMessage());
            return false;
        }
    }

    // ==================== PREVIOUS EMPLOYMENT ====================

    public List<PreviousEmploymentRecord> loadAllPreviousEmployment() {
        List<PreviousEmploymentRecord> list = new ArrayList<>();
        String sql = "SELECT Pagibig_ID, employer_id, date_from, date_to, prev_office_assignment FROM prevemployment";
        try (Connection conn = DataConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new PreviousEmploymentRecord(
                        rs.getString("Pagibig_ID"),
                        rs.getString("employer_id"),
                        rs.getString("date_from"),
                        rs.getString("date_to"),
                        rs.getString("prev_office_assignment")
            ));
        }
    } catch (SQLException e) {
        System.err.println("Error loading previous employment: " + e.getMessage());
    }
    return list;
}

    public boolean insertPreviousEmployment(PreviousEmploymentRecord p) {
        String sql = "INSERT INTO prevemployment (Pagibig_ID, employer_id, date_from, date_to, prev_office_assignment) VALUES (?,?,?,?,?)";
        try (Connection conn = DataConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getPagibigId());
            ps.setString(2, p.getEmployerId());
            ps.setString(3, p.getDateFrom());
            ps.setString(4, p.getDateTo());
            ps.setString(5, p.getPrevOfficeAssignment());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error inserting previous employment: " + e.getMessage());
            return false;
        }
    }

    public boolean updatePreviousEmployment(PreviousEmploymentRecord p) {
        String sql = "UPDATE prevemployment SET date_to=?, prev_office_assignment=? WHERE Pagibig_ID=? AND employer_id=? AND date_from=?";
        try (Connection conn = DataConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getDateTo());
            ps.setString(2, p.getPrevOfficeAssignment());
            ps.setString(3, p.getPagibigId());
            ps.setString(4, p.getEmployerId());
            ps.setString(5, p.getDateFrom());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating previous employment: " + e.getMessage());
            return false;
        }
    }

    public boolean deletePreviousEmployment(String pagibigId, String employerId, String dateFrom) {
        String sql = "DELETE FROM prevemployment WHERE Pagibig_ID=? AND employer_id=? AND date_from=?";
        try (Connection conn = DataConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, pagibigId);
            ps.setString(2, employerId);
            ps.setString(3, dateFrom);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting previous employment: " + e.getMessage());
            return false;
        }
    }

    // ==================== HEIRS ====================

    public List<HeirRecord> loadAllHeirs() {
        List<HeirRecord> list = new ArrayList<>();
        String sql = "SELECT Pagibig_ID, heir_code, heir_name, relationship, heir_date_birth FROM heir";
        try (Connection conn = DataConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new HeirRecord(
                        rs.getString("Pagibig_ID"),
                        rs.getString("heir_code"),
                        rs.getString("heir_name"),
                        rs.getString("relationship"),
                        rs.getString("heir_date_birth")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error loading heirs: " + e.getMessage());
        }
        return list;
    }

    public boolean insertHeir(HeirRecord h) {
        String sql = "INSERT INTO heir (Pagibig_ID, heir_code, heir_name, relationship, heir_date_birth) VALUES (?,?,?,?,?)";
        try (Connection conn = DataConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, h.getPagibigId());
            ps.setString(2, h.getHeirCode());
            ps.setString(3, h.getHeirName());
            ps.setString(4, h.getRelationship());
            ps.setString(5, h.getHeirDateBirth());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error inserting heir: " + e.getMessage());
            return false;
        }
    }

    public boolean updateHeir(HeirRecord h) {
        String sql = "UPDATE heir SET heir_name=?, relationship=?, heir_date_birth=? WHERE Pagibig_ID=? AND heir_code=?";
        try (Connection conn = DataConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, h.getHeirName());
            ps.setString(2, h.getRelationship());
            ps.setString(3, h.getHeirDateBirth());
            ps.setString(4, h.getPagibigId());
            ps.setString(5, h.getHeirCode());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating heir: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteHeir(String pagibigId, String heirCode) {
        String sql = "DELETE FROM heir WHERE Pagibig_ID=? AND heir_code=?";
        try (Connection conn = DataConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, pagibigId);
            ps.setString(2, heirCode);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting heir: " + e.getMessage());
            return false;
        }
    }

    // ==================== GOVERNMENT IDS ====================

    public List<GovernmentIdRecord> loadAllGovernmentIds() {
        List<GovernmentIdRecord> list = new ArrayList<>();
        String sql = "SELECT Pagibig_ID, tin_num, SSS_Num, crn, em_num, afp_pnp_num, deped_code FROM governmentid";
        try (Connection conn = DataConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new GovernmentIdRecord(
                        rs.getString("Pagibig_ID"),
                        rs.getString("tin_num"),
                        rs.getString("SSS_Num"),
                        rs.getString("crn"),
                        rs.getString("em_num"),
                        rs.getString("afp_pnp_num"),
                        rs.getString("deped_code")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error loading government ids: " + e.getMessage());
        }
        return list;
    }

    public boolean insertGovernmentId(GovernmentIdRecord g) {
        String sql = "INSERT INTO governmentid (Pagibig_ID, tin_num, SSS_Num, crn, em_num, afp_pnp_num, deped_code) VALUES (?,?,?,?,?,?,?)";
        try (Connection conn = DataConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, g.getPagibigId());
            ps.setString(2, g.getTinNum());
            ps.setString(3, g.getSssNum());
            ps.setString(4, g.getCrn());
            ps.setString(5, g.getEmNum());
            ps.setString(6, g.getAfpPnpNum());
            ps.setString(7, g.getDepedCode());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error inserting government id: " + e.getMessage());
            return false;
        }
    }

    public boolean updateGovernmentId(GovernmentIdRecord g) {
        String sql = "UPDATE governmentid SET tin_num=?, SSS_Num=?, crn=?, em_num=?, afp_pnp_num=?, deped_code=? WHERE Pagibig_ID=?";
        try (Connection conn = DataConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, g.getTinNum());
            ps.setString(2, g.getSssNum());
            ps.setString(3, g.getCrn());
            ps.setString(4, g.getEmNum());
            ps.setString(5, g.getAfpPnpNum());
            ps.setString(6, g.getDepedCode());
            ps.setString(7, g.getPagibigId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating government id: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteGovernmentId(String pagibigId) {
        String sql = "DELETE FROM governmentid WHERE Pagibig_ID=?";
        try (Connection conn = DataConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, pagibigId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting government id: " + e.getMessage());
            return false;
        }
    }

    // ==================== EMPLOYERS ====================

    public List<EmployerRecord> loadAllEmployers() {
        List<EmployerRecord> list = new ArrayList<>();
        String sql = "SELECT employer_id, employer_name, employer_address FROM employer";
        try (Connection conn = DataConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new EmployerRecord(
                        rs.getString("employer_id"),
                        rs.getString("employer_name"),
                        rs.getString("employer_address")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error loading employers: " + e.getMessage());
        }
        return list;
    }

    public boolean insertEmployer(EmployerRecord e) {
        String sql = "INSERT INTO employer (employer_id, employer_name, employer_address) VALUES (?,?,?)";
        try (Connection conn = DataConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, e.getEmployerId());
            ps.setString(2, e.getEmployerName());
            ps.setString(3, e.getEmployerAddress());
            return ps.executeUpdate() > 0;
        } catch (SQLException e2) {
            System.err.println("Error inserting employer: " + e2.getMessage());
            return false;
        }
    }

    public boolean updateEmployer(EmployerRecord e) {
        String sql = "UPDATE employer SET employer_name=?, employer_address=? WHERE employer_id=?";
        try (Connection conn = DataConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, e.getEmployerName());
            ps.setString(2, e.getEmployerAddress());
            ps.setString(3, e.getEmployerId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e2) {
            System.err.println("Error updating employer: " + e2.getMessage());
            return false;
        }
    }

    public boolean deleteEmployer(String employerId) {
        String sql = "DELETE FROM employer WHERE employer_id=?";
        try (Connection conn = DataConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, employerId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting employer: " + e.getMessage());
            return false;
        }
    }
}