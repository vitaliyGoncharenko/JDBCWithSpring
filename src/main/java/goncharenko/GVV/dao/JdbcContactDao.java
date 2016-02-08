package goncharenko.GVV.dao;

/**
 * Created by Vitaliy on 05.02.2016.
 */

import goncharenko.GVV.model.Contact;
import goncharenko.GVV.model.ContactTelDetail;
import goncharenko.GVV.usedMethod.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository("contactDao")
public class JdbcContactDao implements ContactDao {
    private static final Log LOG = LogFactory.getLog(JdbcContactDao.class);
    private DataSource dataSource;
    private SelectAllContacts selectAllContacts;
    private SelectContactByFirstName selectContactByFirstName;
    private UpdateContact updateContact;
    private InsertContact insertContact;
    private InsertContactTelDetail insertContactTelDetail;
    private StoredFunctionFirstNameById storedFunctionFirstNameById;

    public List<Contact> findAll() {
        return selectAllContacts.execute();
    }

    @Override
    public List<Contact> findByFirstName(String firstName) {
        Map<String, Object> paramМap = new HashMap<String, Object>();
        paramМap.put("first_name", firstName);
        return selectContactByFirstName.executeByNamedParam(paramМap);
    }

    @Override
    public String findFirstNameById(Long id) {
        List<String> result = storedFunctionFirstNameById.execute(id);
        return result.get(0);
    }

    @Override
    public void insert(Contact contact) {
        Map<String, Object> paramМap = new HashMap<String, Object>();
        paramМap.put("first_name", contact.getFirstName());
        paramМap.put("last_name", contact.getLastName());
        paramМap.put("birth_date", contact.getBirthDate());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        insertContact.updateByNamedParam(paramМap, keyHolder);
        contact.setId(keyHolder.getKey().longValue());
        LOG.info("New contact inserted with id: " + contact.getId());
    }

    @Override
    public void insertWithDetail(Contact contact) {
        insertContactTelDetail = new InsertContactTelDetail(dataSource);
        Map<String, Object> paramМap = new HashMap<String, Object>();
        paramМap.put("first_name", contact.getFirstName());
        paramМap.put("last_name", contact.getLastName());
        paramМap.put("birth_date", contact.getBirthDate());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        insertContact.updateByNamedParam(paramМap, keyHolder);
        contact.setId(keyHolder.getKey().longValue());
        LOG.info("New contact inserted with id: " + contact.getId());
        List<ContactTelDetail> contactTelDetails = contact.getContactTelDetails();
        if (contactTelDetails != null) {
            for (ContactTelDetail contactTelDetail : contactTelDetails) {
                paramМap = new HashMap<String, Object>();
                paramМap.put("contact_id", contact.getId());
                paramМap.put("tel_type", contactTelDetail.getTelType());
                paramМap.put("tel_number", contactTelDetail.getTelNumЬer());
                insertContactTelDetail.updateByNamedParam(paramМap);
                insertContactTelDetail.flush();
            }
        }
    }

    @Override
    public List<Contact> findAllWithDetail() {

        JdbcTemplate jdbcTemplate = new JdbcTemplate(getDataSource());
        String sql = "SELECT c.id, c.first_name, c.last_name, c.birth_date,\n" +
                " t.id AS contact_tel_id, t.tel_number, t.tel_type\n" +
                "FROM contact c LEFT JOIN contact_tel_detail t ON c.id = t.contact_id";
        return jdbcTemplate.query(sql, new ContactWithDetailExtractor());
    }

    @Override
    public void update(Contact contact) {
        Map<String, Object> paramМap = new HashMap<String, Object>();
        paramМap.put("first_name", contact.getFirstName());
        paramМap.put("last_name", contact.getLastName());
        paramМap.put("birth_date", contact.getBirthDate());
        paramМap.put("id", contact.getId());
        updateContact.updateByNamedParam(paramМap);
        LOG.info("Existing contact updated with id: " + contact.getId());
    }

    @Resource(name = "dataSource")
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        this.selectAllContacts = new SelectAllContacts(dataSource);
        this.selectContactByFirstName = new SelectContactByFirstName(dataSource);
        this.updateContact = new UpdateContact(dataSource);
        this.insertContact = new InsertContact(dataSource);
        this.storedFunctionFirstNameById = new StoredFunctionFirstNameById(dataSource);
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    private static final class ContactWithDetailExtractor implements ResultSetExtractor<List<Contact>> {
        @Override
        public List<Contact> extractData(ResultSet rs) throws SQLException, DataAccessException {
            Map<Long, Contact> map = new HashMap<Long, Contact>();
            Contact contact = null;
            while (rs.next()) {
                Long id = rs.getLong("id");
                contact = map.get(id);
                if (contact == null) {
                    contact = new Contact();
                    contact.setId(id);
                    contact.setFirstName(rs.getString("first_name"));
                    contact.setLastName(rs.getString("last_name"));
                    contact.setBirthDate(rs.getDate("birth_date"));
                    contact.setContactTelDetails(new ArrayList<ContactTelDetail>());
                    map.put(id, contact);
                }
                Long contactTelDetailid = rs.getLong("contact_tel_id");
                if (contactTelDetailid > 0) {
                    ContactTelDetail contactTelDetail = new ContactTelDetail();
                    contactTelDetail.setId(contactTelDetailid);
                    contactTelDetail.setContactId(id);
                    contactTelDetail.setTelType(rs.getString("tel_type"));
                    contactTelDetail.setTelNumЬer(rs.getString("tel_number"));
                    contact.getContactTelDetails().add(contactTelDetail);
                }
            }
            return new ArrayList<Contact>(map.values());
        }
    }
}

