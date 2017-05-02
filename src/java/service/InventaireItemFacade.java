/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.InventaireItem;
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
public class InventaireItemFacade extends AbstractFacade<InventaireItem> {

    @PersistenceContext(unitName = "gestionStockInventairePU")
    private EntityManager em;
    
    public InventaireItem clone(InventaireItem inventaireItemDestination,InventaireItem inventaireItemSource){
        inventaireItemDestination.setId(inventaireItemSource.getId());
        inventaireItemDestination.setProduit(inventaireItemSource.getProduit());
        inventaireItemDestination.setEtat(inventaireItemSource.getEtat());
        inventaireItemDestination.setDateInventaire(inventaireItemSource.getDateInventaire());
        return inventaireItemDestination;
    }
    
    public InventaireItem clone(InventaireItem inventaireItemSource){
        InventaireItem cloned=new InventaireItem();
        cloned=clone(cloned,inventaireItemSource);
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

    public InventaireItemFacade() {
        super(InventaireItem.class);
    }
    
}
