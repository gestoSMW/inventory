/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.Recepteur;
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
public class RecepteurFacade extends AbstractFacade<Recepteur> {

    @PersistenceContext(unitName = "gestionStockInventairePU")
    private EntityManager em;
    
    public Recepteur clone(Recepteur recepteurDestination,Recepteur recepteurSource){
        recepteurDestination.setId(recepteurSource.getId());
        recepteurDestination.setEmail(recepteurSource.getEmail());
        recepteurDestination.setNom(recepteurSource.getNom());
        recepteurDestination.setPrenom(recepteurSource.getPrenom());
        return recepteurDestination;
    }
    
    public Recepteur clone(Recepteur recepteurSource){
        Recepteur cloned=new Recepteur();
        cloned=clone(cloned, recepteurSource);
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

    public RecepteurFacade() {
        super(Recepteur.class);
    }
    
}
