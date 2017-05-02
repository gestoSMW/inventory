/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.Fournisseur;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;

/**
 *
 * @author the joker
 */
@Stateless
public class FournisseurFacade extends AbstractFacade<Fournisseur> {

    @PersistenceContext(unitName = "gestionStockInventairePU")
    private EntityManager em;
    
    
    public Fournisseur clone(Fournisseur fournisseurDestination,Fournisseur fournisseurSource){
        fournisseurDestination.setCin(fournisseurSource.getCin());
        fournisseurDestination.setNom(fournisseurSource.getNom());
        fournisseurDestination.setEmail(fournisseurSource.getEmail());
        fournisseurDestination.setPrenom(fournisseurSource.getPrenom());
        
        return fournisseurDestination;
    }
    
    public Fournisseur clone(Fournisseur fournisseurSource){
        Fournisseur cloned=new Fournisseur();
        cloned=clone(cloned,fournisseurSource);
        return cloned;
    }
    

    @Override
    protected EntityManager getEntityManager() {
        if (em == null) {
            EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("gestionStockInventairePU");
            em = entityManagerFactory.createEntityManager();
        }
        return em;
    }

    public FournisseurFacade() {
        super(Fournisseur.class);
    }
    
}
