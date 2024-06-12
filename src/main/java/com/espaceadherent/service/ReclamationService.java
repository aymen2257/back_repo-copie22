package com.espaceadherent.service;

import com.espaceadherent.dto.LocalUser;
import com.espaceadherent.model.Reclamation;
import com.espaceadherent.model.ReclamationStatus;
import com.espaceadherent.model.User;
import com.espaceadherent.repo.Reclamationrepo;
import com.espaceadherent.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;

@Service
public class ReclamationService {
    @Autowired
    private Reclamationrepo reclamationRepository;
    @Autowired
    private UserRepository userRepository;

    @Transactional
    public void saveReclamation(MultipartFile file, Reclamation reclamation) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File cannot be empty");
        }

        // Retrieve user from security context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = ((UserDetails) authentication.getPrincipal()).getUsername();
        User user = userRepository.findByEmail(username);

        reclamation.setUser(user); // Set the user
        reclamation.setFileData(file.getBytes());
        reclamation.setFileType(file.getContentType());
        reclamationRepository.save(reclamation);
    }
    public List<Reclamation> getAllReclamations() {
        return reclamationRepository.findAll();
    }
    public Reclamation updateReclamationStatus(Long id, ReclamationStatus status) {
        Reclamation reclamation = reclamationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Reclamation not found with id: " + id));
        reclamation.setStatus(status);
        return reclamationRepository.save(reclamation);
    }
    public Reclamation getFileData(Long id) {
        return reclamationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Reclamation not found with id: " + id));
    }
    public List<Reclamation> getReclamationsByUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LocalUser localUser = (LocalUser) authentication.getPrincipal(); // Casting to LocalUser
        User user = localUser.getUser(); // Getting the User object from LocalUser
        Long userId = user.getId(); // Getting the User ID
        return reclamationRepository.findByUserId(userId);
    }
}