package goncharenko.GVV.dao;

import goncharenko.GVV.model.Contact;

import java.util.List;

/**
 * Created by Vitaliy on 05.02.2016.
 */
public interface ContactDao {

    List<Contact> findAll();
    List<Contact> findByFirstName(String firstName);
    String findFirstNameById(Long id);
    List<Contact> findAllWithDetail();
    void insert(Contact contact);
    void insertWithDetail(Contact contact);
    void update(Contact contact);
}
