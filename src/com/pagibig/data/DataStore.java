package com.pagibig.data;

import com.pagibig.model.ContactRecord;
import com.pagibig.model.EmployerRecord;
import com.pagibig.model.EmploymentRecord;
import com.pagibig.model.GovernmentIdRecord;
import com.pagibig.model.HeirRecord;
import com.pagibig.model.MemberRecord;
import com.pagibig.model.PreviousEmploymentRecord;

import java.util.ArrayList;
import java.util.List;

/**
 * DB-backed data store. Loads all records from MySQL on construction
 * and syncs every add/update/delete back to the database through DatabaseDAO.
 *
 * Write operations only update the in-memory lists if the database operation
 * succeeds, so the UI never shows data that doesn't actually exist in the DB.
 */
public class DataStore {
    private final DatabaseDAO dao = new DatabaseDAO();

    private final List<MemberRecord> members = new ArrayList<>();
    private final List<ContactRecord> contacts = new ArrayList<>();
    private final List<EmploymentRecord> employments = new ArrayList<>();
    private final List<PreviousEmploymentRecord> previousEmployments = new ArrayList<>();
    private final List<HeirRecord> heirs = new ArrayList<>();
    private final List<GovernmentIdRecord> governmentIds = new ArrayList<>();
    private final List<EmployerRecord> employers = new ArrayList<>();

    public DataStore() {
        loadAllFromDatabase();
    }

    /** Pulls every table from the database into the in-memory lists. */
    public void loadAllFromDatabase() {
        members.clear();
        members.addAll(dao.loadAllMembers());

        contacts.clear();
        contacts.addAll(dao.loadAllContacts());

        employments.clear();
        employments.addAll(dao.loadAllEmployment());

        previousEmployments.clear();
        previousEmployments.addAll(dao.loadAllPreviousEmployment());

        heirs.clear();
        heirs.addAll(dao.loadAllHeirs());

        governmentIds.clear();
        governmentIds.addAll(dao.loadAllGovernmentIds());

        employers.clear();
        employers.addAll(dao.loadAllEmployers());

        System.out.println("DataStore loaded: " +
                members.size() + " members, " +
                contacts.size() + " contacts, " +
                employments.size() + " employment records, " +
                previousEmployments.size() + " previous employment records, " +
                heirs.size() + " heirs, " +
                governmentIds.size() + " government IDs, " +
                employers.size() + " employers");
    }

    /** Convenience alias for UI refresh buttons. */
    public void refreshFromDatabase() {
        loadAllFromDatabase();
    }

    // ==================== GETTERS ====================

    public List<MemberRecord> getMembers() {
        return members;
    }

    public List<ContactRecord> getContacts() {
        return contacts;
    }

    public List<EmploymentRecord> getEmployments() {
        return employments;
    }

    public List<PreviousEmploymentRecord> getPreviousEmployments() {
        return previousEmployments;
    }

    public List<HeirRecord> getHeirs() {
        return heirs;
    }

    public List<GovernmentIdRecord> getGovernmentIds() {
        return governmentIds;
    }

    public List<EmployerRecord> getEmployers() {
        return employers;
    }

    // ==================== MEMBERS ====================

    public boolean addMember(MemberRecord member) {
        if (dao.insertMember(member)) {
            members.add(member);
            return true;
        }
        return false;
    }

    public boolean updateMember(int index, MemberRecord member) {
        if (index >= 0 && index < members.size() && dao.updateMember(member)) {
            members.set(index, member);
            return true;
        }
        return false;
    }

    public boolean deleteMember(int index) {
        if (index >= 0 && index < members.size()) {
            String pagibigId = members.get(index).getPagibigId();
            if (dao.deleteMember(pagibigId)) {
                // CASCADE DELETE may have removed related records, so reload everything
                loadAllFromDatabase();
                return true;
            }
        }
        return false;
    }

    // ==================== CONTACTS ====================

    public boolean addContact(ContactRecord contact) {
        if (dao.insertContact(contact)) {
            contacts.add(contact);
            return true;
        }
        return false;
    }

    public boolean updateContact(int index, ContactRecord contact) {
        if (index >= 0 && index < contacts.size() && dao.updateContact(contact)) {
            contacts.set(index, contact);
            return true;
        }
        return false;
    }

    public boolean deleteContact(int index) {
        if (index >= 0 && index < contacts.size() && dao.deleteContact(contacts.get(index).getPagibigId())) {
            contacts.remove(index);
            return true;
        }
        return false;
    }

    // ==================== EMPLOYMENT ====================

    public boolean addEmployment(EmploymentRecord employment) {
        if (dao.insertEmployment(employment)) {
            employments.add(employment);
            return true;
        }
        return false;
    }

    public boolean updateEmployment(int index, EmploymentRecord employment) {
        if (index >= 0 && index < employments.size() && dao.updateEmployment(employment)) {
            employments.set(index, employment);
            return true;
        }
        return false;
    }

    public boolean deleteEmployment(int index) {
        if (index >= 0 && index < employments.size()) {
            EmploymentRecord e = employments.get(index);
            if (dao.deleteEmployment(e.getPagibigId(), e.getEmployerId())) {
                employments.remove(index);
                return true;
            }
        }
        return false;
    }

    // ==================== PREVIOUS EMPLOYMENT ====================

    public boolean addPreviousEmployment(PreviousEmploymentRecord previousEmployment) {
        if (dao.insertPreviousEmployment(previousEmployment)) {
            previousEmployments.add(previousEmployment);
            return true;
        }
        return false;
    }

    public boolean updatePreviousEmployment(int index, PreviousEmploymentRecord previousEmployment) {
        if (index >= 0 && index < previousEmployments.size() && dao.updatePreviousEmployment(previousEmployment)) {
            previousEmployments.set(index, previousEmployment);
            return true;
        }
        return false;
    }

    public boolean deletePreviousEmployment(int index) {
        if (index >= 0 && index < previousEmployments.size()) {
            PreviousEmploymentRecord p = previousEmployments.get(index);
            if (dao.deletePreviousEmployment(p.getPagibigId(), p.getEmployerId(), p.getDateFrom())) {
                previousEmployments.remove(index);
                return true;
            }
        }
        return false;
    }

    // ==================== HEIRS ====================

    public boolean addHeir(HeirRecord heir) {
        if (dao.insertHeir(heir)) {
            heirs.add(heir);
            return true;
        }
        return false;
    }

    public boolean updateHeir(int index, HeirRecord heir) {
        if (index >= 0 && index < heirs.size() && dao.updateHeir(heir)) {
            heirs.set(index, heir);
            return true;
        }
        return false;
    }

    public boolean deleteHeir(int index) {
        if (index >= 0 && index < heirs.size()) {
            HeirRecord h = heirs.get(index);
            if (dao.deleteHeir(h.getPagibigId(), h.getHeirCode())) {
                heirs.remove(index);
                return true;
            }
        }
        return false;
    }

    // ==================== GOVERNMENT IDS ====================

    public boolean addGovernmentId(GovernmentIdRecord governmentId) {
        if (dao.insertGovernmentId(governmentId)) {
            governmentIds.add(governmentId);
            return true;
        }
        return false;
    }

    public boolean updateGovernmentId(int index, GovernmentIdRecord governmentId) {
        if (index >= 0 && index < governmentIds.size() && dao.updateGovernmentId(governmentId)) {
            governmentIds.set(index, governmentId);
            return true;
        }
        return false;
    }

    public boolean deleteGovernmentId(int index) {
        if (index >= 0 && index < governmentIds.size() && dao.deleteGovernmentId(governmentIds.get(index).getPagibigId())) {
            governmentIds.remove(index);
            return true;
        }
        return false;
    }

    // ==================== EMPLOYERS ====================

    public boolean addEmployer(EmployerRecord employer) {
        if (dao.insertEmployer(employer)) {
            employers.add(employer);
            return true;
        }
        return false;
    }

    public boolean updateEmployer(int index, EmployerRecord employer) {
        if (index >= 0 && index < employers.size() && dao.updateEmployer(employer)) {
            employers.set(index, employer);
            return true;
        }
        return false;
    }

    public boolean deleteEmployer(int index) {
        if (index >= 0 && index < employers.size()) {
            String employerId = employers.get(index).getEmployerId();
            if (dao.deleteEmployer(employerId)) {
                // CASCADE DELETE may have removed related records, so reload everything
                loadAllFromDatabase();
                return true;
            }
        }
        return false;
    }
}
