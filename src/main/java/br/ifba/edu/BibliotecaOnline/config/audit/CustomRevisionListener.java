package br.ifba.edu.BibliotecaOnline.config.audit;

import org.hibernate.envers.RevisionListener;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import br.ifba.edu.BibliotecaOnline.entities.CustomRevisionEntity;

public class CustomRevisionListener implements RevisionListener {

    @Override
    public void newRevision(Object revisionEntity) {
        CustomRevisionEntity customRevisionEntity = (CustomRevisionEntity) revisionEntity;
        
        // Pega o nome do usuário logado no Spring Security
        String username;
        try {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal instanceof UserDetails) {
                username = ((UserDetails) principal).getUsername();
            } else {
                username = principal.toString();
            }
        } catch (Exception e) {
            username = "SISTEMA";
        }
        
        customRevisionEntity.setUsername(username);
    }
}
