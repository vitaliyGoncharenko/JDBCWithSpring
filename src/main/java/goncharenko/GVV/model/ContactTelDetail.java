package goncharenko.GVV.model;

import java.io.Serializable;

/**
 * Created by Vitaliy on 05.02.2016.
 */
public class ContactTelDetail implements Serializable {
    private Long id;
    private Long contactId;
    private String telType;
    private String telNumЬer;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getContactId() {
        return contactId;
    }

    public void setContactId(Long contactld) {
        this.contactId = contactId;
    }

    public String getTelType() {
        return telType;
    }

    public void setTelType(String telType) {
        this.telType = telType;
    }

    public String getTelNumЬer() {
        return telNumЬer;
    }

    public void setTelNumЬer(String telNumЬer) {
        this.telNumЬer = telNumЬer;
    }

    @Override
    public String toString() {
        return "Contact Tel Detail - Id: " + id
                + ", Contact id: " + contactId
                + ", Туре: " + telType
                + ", NumЬer: " + telNumЬer;
    }
}
