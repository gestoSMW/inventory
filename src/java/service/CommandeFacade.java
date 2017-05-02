/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.Commande;
import bean.CommandeItem;
import bean.Paiment;
import bean.Produit;
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
public class CommandeFacade extends AbstractFacade<Commande> {

    @PersistenceContext(unitName = "gestionStockInventairePU")
    private EntityManager em;

    private CommandeItem commandeItem;
    @EJB
    private CommandeItemFacade commandeItemFacade;
    private Produit produit;

    public void clone(Commande commandeSource, Commande commandeDestination) {
        commandeDestination.setId(commandeSource.getId());
        commandeDestination.setFournisseur(commandeSource.getFournisseur());
        commandeDestination.setDateCommande(commandeSource.getDateCommande());
        commandeDestination.setMontantTotal(commandeSource.getMontantTotal());
    }

    public Commande clone(Commande commande) {
        Commande cloned = new Commande();
        clone(commande, cloned);
        return cloned;
    }

    public List<Commande> findById(Long id) {
        return em.createQuery("SELECT c FROM Commande c WHERE c.id= '" + id + "'").getResultList();
    }

    public List<Commande> findByFournisseur(String cin) {
        return em.createQuery("SELECT f FROM Commande f WHERE f.fournisseur.cin='" + cin + "'").getResultList();
    }

    public void save(Commande commande, List<CommandeItem> commandeItems) {
        Double montantTotal = 0D;
        commande.setId(generateId("Commande", "id"));
        create(commande);
        for (CommandeItem commandeItem : commandeItems) {
            montantTotal += commandeItem.getTotale() * commandeItem.getQuantite();
            commandeItem.setCommande(commande);
            commandeItemFacade.create(commandeItem);

        }

        commande.setMontantTotal(montantTotal);
        edit(commande);
    }

    //les services des statistiques -----------------------------------------------------------------------
    public List<Commande> commandePerDate(int moisInt, String year) {
        String mois = "" + moisInt;
        if (moisInt < 10) {
            mois = "0" + moisInt;
        }
        String req = "SELECT c FROM Commande c WHERE c.dateCommande LIKE '" + year + "-" + mois + "-%'";
        return getEntityManager().createQuery(req).getResultList();
    }

    public double sumCommandePaiments(int moisInt,String year) {
        double somme = 0.0;
        List<Commande> commandes = commandePerDate(moisInt, year);
        if (commandes != null) {
            for (Commande commande : commandes) {
                for (Paiment paiment : commande.getPaiments()) {
                    somme=somme+paiment.getMontant();
                }
            }
        }
        return somme;
    }

    //-----------------------------------------------------------------------------------------------------
    @Override
    protected EntityManager getEntityManager() {
        if (em == null) {
            EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("gestionStockInventairePU");
            em = entityManagerFactory.createEntityManager();
        }
        return em;
    }

    public CommandeFacade() {
        super(Commande.class);
    }

    public CommandeItemFacade getCommandeItemFacade() {
        return commandeItemFacade;
    }

    public void setCommandeItemFacade(CommandeItemFacade commandeItemFacade) {
        this.commandeItemFacade = commandeItemFacade;
    }

    public Produit getProduit() {
        return produit;
    }

    public void setProduit(Produit produit) {
        this.produit = produit;
    }

}
