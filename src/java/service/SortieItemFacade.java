/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.Sortie;
import bean.SortieItem;
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
public class SortieItemFacade extends AbstractFacade<SortieItem> {

    @PersistenceContext(unitName = "gestionStockInventairePU")
    private EntityManager em;

    public List<SortieItem> findBySortie(Sortie sortie) {
        String req = "SELECT i FROM SortieItem i WHERE i.sortie.id=" + sortie.getId();
        return getEntityManager().createQuery(req).getResultList();
    }

    public SortieItem clone(SortieItem sortieItemDestination, SortieItem sortieItemSource) {
        sortieItemDestination.setId(sortieItemSource.getId());
        sortieItemDestination.setProduit(sortieItemSource.getProduit());
        sortieItemDestination.setQuantite(sortieItemSource.getQuantite());
        sortieItemDestination.setSortie(sortieItemSource.getSortie());
        sortieItemDestination.setMagasin(sortieItemSource.getMagasin());
        return sortieItemDestination;
    }

    public SortieItem clone(SortieItem sortieItemSource) {
        SortieItem cloned = new SortieItem();
        cloned = clone(cloned, sortieItemSource);
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

    public SortieItemFacade() {
        super(SortieItem.class);
    }

}
