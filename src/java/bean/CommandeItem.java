/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 *
 * @author the joker
 */
@Entity
public class CommandeItem implements Serializable {

    

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    private Produit produit;
    private double quantite;
    private double quantiteLivree = 0.0;
    private double totale;
    @ManyToOne
    private Commande commande;
    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Commande getCommande() {
        if(commande==null){
            commande=new Commande();
        }
        return commande;
    }

    public void setCommande(Commande commande) {
        this.commande = commande;
    }

    public Produit getProduit() {
        if(produit==null){
            produit=new Produit();
        }
        return produit;
    }

    public void setProduit(Produit produit) {
        this.produit = produit;
    }

    public double getQuantite() {
        return quantite;
    }

    public void setQuantite(double quantite) {
        this.quantite = quantite;
    }

    public double getQuantiteLivree() {
        return quantiteLivree;
    }

    public void setQuantiteLivree(double quantiteLivree) {
        this.quantiteLivree = quantiteLivree;
    }
    

    public double getTotale() {
        return totale;
    }

    public void setTotale(double totale) {
        this.totale = totale;
    }

   
    
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CommandeItem other = (CommandeItem) obj;
        if (Double.doubleToLongBits(this.quantite) != Double.doubleToLongBits(other.quantite)) {
            return false;
        }
        if (Double.doubleToLongBits(this.totale) != Double.doubleToLongBits(other.totale)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.produit, other.produit)) {
            return false;
        }
        if (!Objects.equals(this.commande, other.commande)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "CommandeItem{" + "id=" + id + '}';
    }

    
}
