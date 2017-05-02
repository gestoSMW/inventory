/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.Magasin;
import bean.Produit;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author the joker
 */
@Stateless
public class MagasinFacade extends AbstractFacade<Magasin> {

    @PersistenceContext(unitName = "gestionStockInventairePU")
    private EntityManager em;

    public List<Magasin> findByProductAndQuantite(Produit produit, double quantite) {
        String req = "SELECT s.magasin FROM Stock s WHERE s.quantite >= " + quantite + " AND s.produit.id=" + produit.getId();
        return getEntityManager().createQuery(req).getResultList();
    }

    public Magasin clone(Magasin magasinDestination, Magasin magasinSource) {
        magasinDestination.setId(magasinSource.getId());
        magasinDestination.setLibelle(magasinSource.getLibelle());
        magasinDestination.setAdresse(magasinSource.getAdresse());
        return magasinDestination;
    }

    public Magasin clone(Magasin magasinSource) {
        Magasin cloned = new Magasin();
        cloned = clone(cloned, magasinSource);
        return cloned;
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public MagasinFacade() {
        super(Magasin.class);
    }

}
