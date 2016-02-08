package goncharenko.GVV.dao;

import goncharenko.GVV.model.Contact;
import goncharenko.GVV.model.ContactTelDetail;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vitaliy on 07.02.2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:app-context.xml"})
public class JdbcContactDaoTest{
    private static final Log LOG = LogFactory.getLog(JdbcContactDaoTest.class);
    @Autowired
    JdbcContactDao contactDao;
    //for testFindAll()
    private static String firstNameExpected = "Chris";
    private static String lastNameExpected = "Schaefer";
    private static Date birthDateExpected = new Date(1981 - 05 - 03);
    //for testFindFirstNameById
    private static Long id = 1L;
    //for Insert contact
    private static String firstNameExpected2 = "Alex";
    private static String lastNameExpected2 = "Foxy";
    private static Date birthDateExpected2 = new Date(1981 - 05 - 03);
    //for testInsertWithDetail
    private static String firstName3 = "Olga";
    private static String lastName3 = "Fox";
    private static Date birthDate3 = new Date(1981 - 05 - 03);

    private static String telType1 = "Home";
    private static String telNumЬer1 = "12121212";
    private static String telType2 = "Mobile";
    private static String telNumЬer2 = "12121212";

    //for update contact
    private static String firstNameIns = "ChrisIns";
    private static String lastNameIns = "SchaeferIns";
    private static Date birthDateIns = new Date(1983 - 05 - 03);

    private static String firstNameUpdate = "ChrisUpdate";
    private static String lastNameUpdate = "SchaeferUpdate";
    private static Date birthDateUpdate = new Date(1987 - 05 - 03);

    @Test
    public void testFindAll() {
        LOG.info("Get all contact");
        List<Contact> list = contactDao.findAll();
        LOG.info("All contacts :");
        for (Contact contact : list) {
            LOG.info("Contact " + contact);
        }
        Assert.assertEquals(firstNameExpected, list.get(0).getFirstName());
        Assert.assertEquals(lastNameExpected, list.get(0).getLastName());
    }

    @Test
    public void testFindByFirstName() {
        LOG.info("Get contact by first_name");
        List<Contact> listFirstName = contactDao.findByFirstName(firstNameExpected);
        for(Contact contact: listFirstName){
            LOG.info("contact by first_name - " + firstNameExpected + " : " +contact);
        }
        Assert.assertEquals(firstNameExpected, listFirstName.get(0).getFirstName());
        Assert.assertEquals(lastNameExpected, listFirstName.get(0).getLastName());
    }

    @Test
    public void testFindFirstNameById() {
        LOG.info("Get contact first_name be id");
        String firstNameById = contactDao.findFirstNameById(id);
        System.out.println("Get contact first_name be id : " + firstNameById);
        Assert.assertEquals(firstNameExpected,firstNameById);

    }
    @Test
    public void testInsert() {
        LOG.info("Insert contact");
        Contact contact = new Contact();
        contact.setFirstName(firstNameExpected2);
        contact.setLastName(lastNameExpected2);
        contact.setBirthDate(birthDateExpected2);

        LOG.info("Insert contact" + contact);
        contactDao.insert(contact);

        List<Contact> contactList = contactDao.findByFirstName(firstNameExpected2);
        Assert.assertEquals(firstNameExpected2,contactList.get(0).getFirstName());
        Assert.assertEquals(lastNameExpected2,contactList.get(0).getLastName());


    }
    @Test
    public void testInsertWithDetail() {
        LOG.info("Insert contact with details");
        ContactTelDetail contactTelDetail1 = new ContactTelDetail();
        contactTelDetail1.setTelType(telType1);
        contactTelDetail1.setTelNumЬer(telNumЬer1);

        ContactTelDetail contactTelDetail2 = new ContactTelDetail();
        contactTelDetail2.setTelType(telType2);
        contactTelDetail2.setTelNumЬer(telNumЬer2);

        List<ContactTelDetail> contactTelDetailList = new ArrayList<ContactTelDetail>();
        contactTelDetailList.add(contactTelDetail1);
        contactTelDetailList.add(contactTelDetail2);

        Contact contact2 = new Contact();
        contact2.setFirstName(firstName3);
        contact2.setLastName(lastName3);
        contact2.setBirthDate(birthDate3);
        contact2.setContactTelDetails(contactTelDetailList);
        LOG.info("Insert contact with details :" + contact2);
        contactDao.insertWithDetail(contact2);

        List<Contact> contactList = contactDao.findByFirstName(firstName3);
        Assert.assertEquals(firstName3,contactList.get(0).getFirstName());
        Assert.assertEquals(lastName3,contactList.get(0).getLastName());
    }
    @Test
    public void testFindAllWithDetail() {
        LOG.info("Get all contact with detail");
        List<Contact> list = contactDao.findAllWithDetail();
        LOG.info("All contacts :");

        for (Contact contact : list) {
            LOG.info("contact with detail : " + contact);
        }
        Assert.assertEquals(firstNameExpected, list.get(0).getFirstName());
        Assert.assertEquals(lastNameExpected, list.get(0).getLastName());
    }
    @Test
    public void testUpdate() {
        //insert new contact and update
        Contact contact = new Contact();
        contact.setFirstName(firstNameIns);
        contact.setLastName(lastNameIns);
        contact.setBirthDate(birthDateIns);

        LOG.info("Insert new contact " + contact);
        contactDao.insert(contact);

        List<Contact> contactlist = contactDao.findByFirstName(firstNameIns);
        Contact contactForUpdate = contactlist.get(0);

        LOG.info("Update new contact");
        Contact contactUpdate = new Contact();
        contactUpdate.setId(contactForUpdate.getId());
        contactUpdate.setFirstName(firstNameUpdate);
        contactUpdate.setLastName(lastNameUpdate);
        contactUpdate.setBirthDate(birthDateUpdate);

        contactDao.update(contactUpdate);

        String firstNameUpdateById = contactDao.findFirstNameById(contactForUpdate.getId());
        Assert.assertEquals(firstNameUpdate,firstNameUpdateById);
    }
}
