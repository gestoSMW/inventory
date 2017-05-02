/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.Inventaire;
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
public class InventaireFacade extends AbstractFacade<Inventaire> {

    @PersistenceContext(unitName = "gestionStockInventairePU")
    private EntityManager em;
    
    public Inventaire clone(Inventaire inventaireDestination,Inventaire inventaireSource){
        inventaireDestination.setId(inventaireSource.getId());
        inventaireDestination.setDateDebut(inventaireSource.getDateDebut());
        inventaireDestination.setDateFin(inventaireSource.getDateFin());
        inventaireDestination.setDescription(inventaireSource.getDescription());
        return inventaireDestination;
    }
    
    public Inventaire clone(Inventaire inventaireSource){
        Inventaire cloned=new Inventaire();
        cloned=clone(cloned,inventaireSource);
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

    public InventaireFacade() {
        super(Inventaire.class);
    }
    
}
