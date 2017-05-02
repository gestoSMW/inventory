/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.Sortie;
import bean.SortieItem;
import java.util.List;
import javax.ejb.EJB;
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
public class SortieFacade extends AbstractFacade<Sortie> {

    @PersistenceContext(unitName = "gestionStockInventairePU")
    private EntityManager em;

    @EJB
    private StockFacade stockFacade;
    @EJB
    private ProduitFacade produitFacade;
    @EJB
    private SortieItemFacade sortieItemFacade;

    public void save(Sortie sortie, List<SortieItem> sortieItems) {
        sortie.setId(generateId("Sortie", "id"));
        create(sortie);
        for (SortieItem sortieItem : sortieItems) {
            sortieItem.setSortie(sortie);
            stockFacade.updateStock(sortieItem.getMagasin(), sortieItem.getProduit(), sortieItem.getQuantite(), "-");
            produitFacade.updateProduitQuantite(sortieItem.getProduit(), sortieItem.getQuantite(), "-");
            sortieItemFacade.create(sortieItem);
        }
    }

    public Sortie clone(Sortie sortieDestination, Sortie sortieSource) {
        sortieDestination.setId(sortieSource.getId());
        sortieDestination.setRecepteur(sortieSource.getRecepteur());
        sortieDestination.setDateSortie(sortieSource.getDateSortie());
        return sortieDestination;
    }

    public Sortie clone(Sortie sortieSource) {
        Sortie cloned = new Sortie();
        cloned = clone(cloned, sortieSource);
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

    public SortieFacade() {
        super(Sortie.class);
    }

}
