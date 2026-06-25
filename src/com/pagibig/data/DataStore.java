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

    /** Pulls every table out of MySQL to fill memory lists */
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
    }

    // ==================== GETTERS ====================
    public List<MemberRecord> getMembers() { return members; }
    public List<ContactRecord> getContacts() { return contacts; }
    public List<EmploymentRecord> getEmployments() { return employments; }
    public List<PreviousEmploymentRecord> getPreviousEmployments() { return previousEmployments; }
    public List<HeirRecord> getHeirs() { return heirs; }
    public List<GovernmentIdRecord> getGovernmentIds() { return governmentIds; }
    public List<EmployerRecord> getEmployers() { return employers; }

    // ==================== WRITE PIPELINES ====================

    public boolean addMember(MemberRecord r) {
        if (dao.insertMember(r)) { members.add(r); return true; }
        return false;
    }
    public boolean updateMember(int index, MemberRecord r) {
        if (dao.updateMember(r)) { members.set(index, r); return true; }
        return false;
    }
    public boolean deleteMember(int index) {
        MemberRecord r = members.get(index);
        if (dao.deleteMember(r.getPagibigId())) { members.remove(index); return true; }
        return false;
    }

    public boolean addContact(ContactRecord r) {
        if (dao.insertContact(r)) { contacts.add(r); return true; }
        return false;
    }
    public boolean updateContact(int index, ContactRecord r) {
        if (dao.updateContact(r)) { contacts.set(index, r); return true; }
        return false;
    }
    public boolean deleteContact(int index) {
        ContactRecord r = contacts.get(index);
        if (dao.deleteContact(r.getPagibigId())) { contacts.remove(index); return true; }
        return false;
    }

    public boolean addEmployment(EmploymentRecord r) {
        if (dao.insertEmployment(r)) { employments.add(r); return true; }
        return false;
    }
    public boolean updateEmployment(int index, EmploymentRecord r) {
        if (dao.updateEmployment(r)) { employments.set(index, r); return true; }
        return false;
    }
    public boolean deleteEmployment(int index) {
        EmploymentRecord r = employments.get(index);
        if (dao.deleteEmployment(r.getPagibigId(), r.getEmployerId())) { employments.remove(index); return true; }
        return false;
    }

    public boolean addPreviousEmployment(PreviousEmploymentRecord r) {
        if (dao.insertPreviousEmployment(r)) { previousEmployments.add(r); return true; }
        return false;
    }
    public boolean updatePreviousEmployment(int index, PreviousEmploymentRecord r) {
        if (dao.updatePreviousEmployment(r)) { previousEmployments.set(index, r); return true; }
        return false;
    }
    public boolean deletePreviousEmployment(int index) {
        PreviousEmploymentRecord r = previousEmployments.get(index);
        if (dao.deletePreviousEmployment(r.getPagibigId(), r.getEmployerId(), r.getDateFrom())) { previousEmployments.remove(index); return true; }
        return false;
    }

    public boolean addHeir(HeirRecord r) {
        if (dao.insertHeir(r)) { heirs.add(r); return true; }
        return false;
    }
    public boolean updateHeir(int index, HeirRecord r) {
        if (dao.updateHeir(r)) { heirs.set(index, r); return true; }
        return false;
    }
    public boolean deleteHeir(int index) {
        HeirRecord r = heirs.get(index);
        if (dao.deleteHeir(r.getPagibigId(), r.getHeirCode())) { heirs.remove(index); return true; }
        return false;
    }

    public boolean addGovernmentId(GovernmentIdRecord r) {
        if (dao.insertGovernmentId(r)) { governmentIds.add(r); return true; }
        return false;
    }
    public boolean updateGovernmentId(int index, GovernmentIdRecord r) {
        if (dao.updateGovernmentId(r)) { governmentIds.set(index, r); return true; }
        return false;
    }
    public boolean deleteGovernmentId(int index) {
        GovernmentIdRecord r = governmentIds.get(index);
        if (dao.deleteGovernmentId(r.getPagibigId())) { governmentIds.remove(index); return true; }
        return false;
    }

    public boolean addEmployer(EmployerRecord r) {
        if (dao.insertEmployer(r)) { employers.add(r); return true; }
        return false;
    }
    public boolean updateEmployer(int index, EmployerRecord r) {
        if (dao.updateEmployer(r)) { employers.set(index, r); return true; }
        return false;
    }
    public boolean deleteEmployer(int index) {
        EmployerRecord r = employers.get(index);
        if (dao.deleteEmployer(r.getEmployerId())) { employers.remove(index); return true; }
        return false;
    }

    // =====================================================================
    // ⚡ FIXED CASCADING EXTENSION METHODS (Synced variable names to 'dao')
    // =====================================================================
    
    public boolean deleteMemberByKey(String pagibigId) {
        boolean databaseStatus = dao.deleteMember(pagibigId);
        if (databaseStatus) {
            members.removeIf(m -> m.getPagibigId().equals(pagibigId));
        }
        return databaseStatus;
    }

    public void deleteContactByMember(String pagibigId) {
        dao.deleteContact(pagibigId);
        contacts.removeIf(c -> c.getPagibigId().equals(pagibigId));
    }

    public void deleteEmploymentByMember(String pagibigId) {
        List<EmploymentRecord> targets = new ArrayList<>();
        for (EmploymentRecord e : employments) {
            if (e.getPagibigId().equals(pagibigId)) targets.add(e);
        }
        for (EmploymentRecord e : targets) {
            dao.deleteEmployment(e.getPagibigId(), e.getEmployerId());
        }
        employments.removeIf(e -> e.getPagibigId().equals(pagibigId));
    }

    public void deletePreviousEmploymentByMember(String pagibigId) {
        List<PreviousEmploymentRecord> targets = new ArrayList<>();
        for (PreviousEmploymentRecord p : previousEmployments) {
            if (p.getPagibigId().equals(pagibigId)) targets.add(p);
        }
        for (PreviousEmploymentRecord p : targets) {
            dao.deletePreviousEmployment(p.getPagibigId(), p.getEmployerId(), p.getDateFrom());
        }
        previousEmployments.removeIf(p -> p.getPagibigId().equals(pagibigId));
    }

    public void deleteHeirsByMember(String pagibigId) {
        List<HeirRecord> targets = new ArrayList<>();
        for (HeirRecord h : heirs) {
            if (h.getPagibigId().equals(pagibigId)) targets.add(h);
        }
        for (HeirRecord h : targets) {
            dao.deleteHeir(h.getPagibigId(), h.getHeirCode());
        }
        heirs.removeIf(h -> h.getPagibigId().equals(pagibigId));
    }

    public void deleteGovernmentIdByMember(String pagibigId) {
        dao.deleteGovernmentId(pagibigId);
        governmentIds.removeIf(g -> g.getPagibigId().equals(pagibigId));
    }
}