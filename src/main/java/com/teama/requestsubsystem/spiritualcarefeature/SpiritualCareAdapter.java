package com.teama.requestsubsystem.spiritualcarefeature;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class SpiritualCareAdapter {
    private StringProperty firstName;
    private StringProperty lastName;
    private StringProperty email;
    private StringProperty phone;
    private SpiritualCareStaff spiritualBase;
    private StringProperty religion;

    public SpiritualCareAdapter(SpiritualCareStaff staff){
        firstName= new SimpleStringProperty(staff.getFirstName());
        lastName= new SimpleStringProperty(staff.getLastName());
        phone=new SimpleStringProperty(staff.getPhoneNumber());
        email = new SimpleStringProperty(staff.getEmail());
        religion = new SimpleStringProperty(staff.getReligion().toString());
        this.spiritualBase=staff;
    }

    public SpiritualCareStaff getSpiritualBase() {
        return spiritualBase;
    }

    public String getFirstName(){
        return firstName.get();
    }
    public String getLastName() {return lastName.get();}
    public String getPhone(){return phone.get();}
    public String getEmail(){return email.get();}
    public String getReligion(){return religion.get();}

    public void setFirstName(String firstName){
        this.firstName.set(firstName);
    }
    public void setLastName(String lastName){this.lastName.set(lastName);}
    public void setPhone(String phone){this.phone.set(phone);}
    public void setEmail(String email){this.email.set(email);}
    public void setReligion(String religion){this.religion.set(religion);}
}
