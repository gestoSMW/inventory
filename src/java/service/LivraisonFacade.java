/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.Livraison;
import bean.LivraisonItem;
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
public class LivraisonFacade extends AbstractFacade<Livraison> {

    @PersistenceContext(unitName = "gestionStockInventairePU")
    private EntityManager em;
    @EJB
    private CommandeItemFacade commandeItemFacade;
    @EJB
    private StockFacade stockFacde;
    @EJB
    private ProduitFacade produitFacade;
    @EJB
    private LivraisonItemFacade livraisonItemFacade;

    public void save(Livraison livraison, List<LivraisonItem> items) {
        livraison.setId(generateId("Livraison", "id"));
        create(livraison);
        for (LivraisonItem item : items) {
            item.setLivraison(livraison);
            commandeItemFacade.updateQuantiteLivree(livraison.getCommande(), item.getProduit(), item.getQuantite());
            stockFacde.updateStock(item.getMagasin(), item.getProduit(), item.getQuantite(), "+");
            produitFacade.updateProduitQuantite(item.getProduit(), item.getQuantite(), "+");
            livraisonItemFacade.create(item);
        }
    }

    public Livraison clone(Livraison livraisonDestination, Livraison livraisonSource) {
        livraisonDestination.setId(livraisonSource.getId());
        livraisonDestination.setCommande(livraisonSource.getCommande());
        livraisonDestination.setDateLivraison(livraisonSource.getDateLivraison());
        return livraisonDestination;
    }

    public Livraison clone(Livraison livraisonSource) {
        Livraison cloned = new Livraison();
        cloned = clone(cloned, livraisonSource);
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

    public LivraisonFacade() {
        super(Livraison.class);
    }

}
