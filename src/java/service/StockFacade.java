/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.Magasin;
import bean.Produit;
import bean.Stock;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;

/**
 *
 * @author the joker
 */
@Stateless
public class StockFacade extends AbstractFacade<Stock> {

    @PersistenceContext(unitName = "gestionStockInventairePU")
    private EntityManager em;

    private int updateStock(Stock stock, double quantite, String operateur) {
        double nouveauQuantite = 0;
        if (operateur.equals("+")) {
            nouveauQuantite = stock.getQuantite() + quantite;
            stock.setQuantite(nouveauQuantite);
            edit(stock);
            return 2;
        } else if (operateur.equals("-") && stock.getQuantite() >= quantite) {
            nouveauQuantite = stock.getQuantite() - quantite;
            stock.setQuantite(nouveauQuantite);
            edit(stock);
            return 3;
        }
        return -3;
    }

    public int updateStock(Magasin magasin, Produit produit, double quantite, String operateur) {
        Stock stock = findStockByProduitMagasin(produit, magasin);
        if (stock == null) {
            if (operateur.equals("-")) {
                return -1;
            } else {
                //creer le stock
                System.out.println("on creer le stock");
                stock = new Stock(generateId("Stock", "id"), quantite, magasin, produit);
                create(stock);
                return 1;
            }
        } else {
            System.out.println("on update le stock");
            return updateStock(stock, quantite, operateur);
        }

    }

    public Stock findStockByProduitMagasin(Produit produit, Magasin magasin) {
        String str = "SELECT s FROM Stock s WHERE s.produit.id=" + produit.getId() + " AND s.magasin.id='" + magasin.getId() + "'";
        Stock stock = null;
        try {
            stock = (Stock) getEntityManager().createQuery(str).getSingleResult();
        } catch (NoResultException e) {
            System.out.println("error wa error a3ibad llah");
        }
        return stock;
    }

    public Stock clone(Stock stockDestination, Stock stockSource) {
        stockDestination.setId(stockSource.getId());
        stockDestination.setMagasin(stockSource.getMagasin());
        stockDestination.setProduit(stockSource.getProduit());
        stockDestination.setQuantite(stockSource.getQuantite());
        return stockDestination;
    }

    public Stock clone(Stock stockSource) {
        Stock cloned = new Stock();
        cloned = clone(cloned, stockSource);
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

    public StockFacade() {
        super(Stock.class);
    }

}
