package com.espaceadherent.service;

import com.espaceadherent.model.Contact;
import com.espaceadherent.repo.ContactRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ContactServiceImpl implements ContactService{

    @Autowired
    private ContactRepo contactRepo;

    @Override
    public List<Contact> getAllContacts() {
        return contactRepo.findAll();
    }

    @Override
    public Contact getContactById(Long id) {
        Optional<Contact> o= contactRepo.findById(id);
        return o.isPresent() ? o.get() :null;
    }

    @Override
    public void deleteContact(Long id) {
        contactRepo.deleteById(id);
    }

    @Override
    public Contact updateContact(Contact contact) {
        Date now = Calendar.getInstance().getTime();
        contact.setDate(now);
        return contactRepo.save(contact);
    }

    @Override
    public Contact addContact(Contact contact) {
        Date now = Calendar.getInstance().getTime();
        contact.setDate(now);
        return contactRepo.save(contact);
    }
}
