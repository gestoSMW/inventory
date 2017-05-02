/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.Paiment;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author the joker
 */
@Stateless
public class PaimentFacade extends AbstractFacade<Paiment> {

    @PersistenceContext(unitName = "gestionStockInventairePU")
    private EntityManager em;

    public List<Paiment> depensePerYearAndMonth(int moisInt, String year) {
        String mois = "" + moisInt;
        if (moisInt < 10) {
            mois = "0" + moisInt;
        }
        String req = "SELECT p FROM Paiment p WHERE p.datePaiment LIKE '" + year + "-" + mois + "-%'";
        return getEntityManager().createQuery(req).getResultList();
    }

    public double sumPaiments(int moisInt, String year) {
        double somme = 0.0;
        List<Paiment> paiments = depensePerYearAndMonth(moisInt, year);
        if (paiments != null) {
            for (Paiment paiment : paiments) {
                somme = somme + paiment.getMontant();
            }
        }
        return somme;
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PaimentFacade() {
        super(Paiment.class);
    }

}
