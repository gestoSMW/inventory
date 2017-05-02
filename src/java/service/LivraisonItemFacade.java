/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.Livraison;
import bean.LivraisonItem;
import java.util.List;
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
public class LivraisonItemFacade extends AbstractFacade<LivraisonItem> {

    @PersistenceContext(unitName = "gestionStockInventairePU")
    private EntityManager em;
    
    
    public List<LivraisonItem> findByLivraison(Livraison livraison){
        String req="SELECT i FROM LivraisonItem i WHERE i.livraison.id="+livraison.getId();
        return getEntityManager().createQuery(req).getResultList();
    }
    
    public LivraisonItem clone(LivraisonItem livraisonItemSource, LivraisonItem livraisonItemDestination) {
        livraisonItemDestination.setId(livraisonItemSource.getId());
        livraisonItemDestination.setLivraison(livraisonItemSource.getLivraison());
        livraisonItemDestination.setProduit(livraisonItemSource.getProduit());
        livraisonItemDestination.setQuantite(livraisonItemSource.getQuantite());
        livraisonItemDestination.setMagasin(livraisonItemSource.getMagasin());
        return livraisonItemDestination;
    }

    public LivraisonItem clone(LivraisonItem livraisonItemSource) {
        LivraisonItem cloned = new LivraisonItem();
        cloned = clone(livraisonItemSource, cloned);
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

    public LivraisonItemFacade() {
        super(LivraisonItem.class);
    }
    
}
