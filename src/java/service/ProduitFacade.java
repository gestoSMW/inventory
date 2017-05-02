/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.Produit;
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
public class ProduitFacade extends AbstractFacade<Produit> {

    @PersistenceContext(unitName = "gestionStockInventairePU")
    private EntityManager em;

    public int updateProduitQuantite(Produit produit, double quantite, String operateur) {
        if (produit == null || quantite < 0) {
            return 0;
        } else {
            double nouveauQuantite = 0.0;
            if (operateur.equals("+")) {
                nouveauQuantite = produit.getQuantiteGlobale() + quantite;
            } else if (operateur.equals("-")) {
                if (produit.getQuantiteGlobale() < quantite) {
                    return -1;
                }
                nouveauQuantite = produit.getQuantiteGlobale() - quantite;
                produit.setQuantiteSortie(produit.getQuantiteSortie()+quantite);
            }
            produit.setQuantiteGlobale(nouveauQuantite);
            edit(produit);
            return 1;
        }
    }

    public Produit clone(Produit produitDestination, Produit produitSource) {
        produitDestination.setId(produitSource.getId());
        produitDestination.setLibelle(produitSource.getLibelle());
        produitDestination.setPrix(produitSource.getPrix());
        produitDestination.setQuantiteGlobale(produitSource.getQuantiteGlobale());
        return produitDestination;
    }

    public Produit clone(Produit produitSource) {
        Produit cloned = new Produit();
        cloned = clone(cloned, produitSource);
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

    public ProduitFacade() {
        super(Produit.class);
    }

}
