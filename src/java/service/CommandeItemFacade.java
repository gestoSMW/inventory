/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.Commande;
import bean.CommandeItem;
import bean.Produit;
import java.util.List;
import javax.ejb.Stateless;
import controller.util.SearchUtil;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;

/**
 *
 * @author the joker
 */
@Stateless
public class CommandeItemFacade extends AbstractFacade<CommandeItem> {

    @PersistenceContext(unitName = "gestionStockInventairePU")
    private EntityManager em;
    private List<CommandeItem> commandeItems;
    private Commande commande;
    private CommandeItem commandeItem;

    public int updateQuantiteLivree(Commande commande, Produit produit, double quantite) {
        CommandeItem citem = findByProductAndCommande(commande, produit);
        if (citem == null) {
            return 0;
        } else {
            double nouveauQuantite = citem.getQuantiteLivree() + quantite;
            if (nouveauQuantite > citem.getQuantite() || quantite < 0) {
                return -1;
            } else {
                citem.setQuantiteLivree(citem.getQuantiteLivree() + quantite);
                edit(citem);
                return 1;
            }
        }
    }

    public CommandeItem findByProductAndCommande(Commande commande, Produit produit) {
        String str = "SELECT c FROM CommandeItem c WHERE c.commande.id=" + commande.getId() + " AND c.produit.id=" + produit.getId();
        return (CommandeItem) getEntityManager().createQuery(str).getSingleResult();
    }
    
    public List<CommandeItem> commandeItemNonLivrees(Commande commande){
        return getEntityManager().createQuery("SELECT c FROM CommandeItem c WHERE c.quantiteLivree<c.quantite and c.commande.id="+commande.getId()).getResultList();
    }

    @Override
    protected EntityManager getEntityManager() {
        if (em == null) {
            EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("gestionStockInventairePU");
            em = entityManagerFactory.createEntityManager();
        }
        return em;
    }

    public CommandeItemFacade() {
        super(CommandeItem.class);
    }

    public void clone(CommandeItem commandeItemSource, CommandeItem commandeItemDestination) {
        System.out.println("ha commandItemSource ==> " + commandeItemSource);
        commandeItemDestination.setId(commandeItemSource.getId());
        commandeItemDestination.setQuantite(commandeItemSource.getQuantite());
        commandeItemDestination.setProduit(commandeItemSource.getProduit());
        commandeItemDestination.setTotale(commandeItemSource.getTotale());
        commandeItemDestination.setCommande(commandeItemSource.getCommande());
        commandeItemDestination.setQuantiteLivree(commandeItemSource.getQuantiteLivree());

    }

    public CommandeItem clone(CommandeItem commandeItem) {
        CommandeItem cloned = new CommandeItem();
        clone(commandeItem, cloned);
        return cloned;
    }

    public List<CommandeItem> findById(Long id) {
        return em.createQuery("SELECT ci FROM CommandeItem ci WHERE ci.id= '" + id + "'").getResultList();
    }

    public List<CommandeItem> findByProduit(Long id) {
        return em.createQuery("SELECT ci FROM CommandeItem ci WHERE ci.produit.id= '" + id + "'").getResultList();
    }

    public List<CommandeItem> findByCommande(Commande commande) {
        return em.createQuery("SELECT ci FROM CommandeItem ci WHERE ci.commande.id= '" + commande.getId() + "'").getResultList();
    }

    public List<CommandeItem> search(Commande commande, Produit produit, Double prixMax, Double prixMin, Double qteMax, Double qteMin) {
        String req = "SELECT ci FROM CommandeItem ci WHERE 1=1 ";
        req += SearchUtil.addConstraintMinMax("ci", "prixCommandeItem", prixMin, prixMax);
        req += SearchUtil.addConstraintMinMax("ci", "quantite", qteMin, qteMax);
        if (produit != null) {
            req += SearchUtil.addConstraint("ci", "produit.id", "=", produit.getId());
        }
        if (commande != null) {
            req += SearchUtil.addConstraint("ci", "commande.id", "=", commande.getId());
        }
        return em.createQuery(req).getResultList();
    }

    public List<CommandeItem> getCommandeItems() {
        return commandeItems;
    }

    public void setCommandeItems(List<CommandeItem> commandeItems) {
        this.commandeItems = commandeItems;
    }

    public Commande getCommande() {
        return commande;
    }

    public void setCommande(Commande commande) {
        this.commande = commande;
    }

    public CommandeItem getCommandeItem() {
        return commandeItem;
    }

    public void setCommandeItem(CommandeItem commandeItem) {
        this.commandeItem = commandeItem;
    }

}
