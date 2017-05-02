/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;

/**
 *
 * @author the joker
 */
@Entity
public class Commande implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    private Fournisseur fournisseur;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dateCommande;
    private Double montantTotal;
    @OneToMany(mappedBy = "commande")
    private List<CommandeItem> commandeItems;
    @OneToMany(mappedBy = "commande")
    private List<Paiment> paiments;
    private double montantPaye;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Fournisseur getFournisseur() {
        if (fournisseur == null) {
            fournisseur = new Fournisseur();
        }
        return fournisseur;
    }

    public void setFournisseur(Fournisseur fournisseur) {
        this.fournisseur = fournisseur;
    }

    public Date getDateCommande() {
        return dateCommande;
    }

    public void setDateCommande(Date dateCommande) {
        this.dateCommande = dateCommande;
    }

    public Double getMontantTotal() {
        return montantTotal;
    }

    public void setMontantTotal(Double montantTotal) {
        this.montantTotal = montantTotal;
    }

    public List<CommandeItem> getCommandeItems() {
        if (commandeItems == null) {
            commandeItems = new ArrayList<>();
        }
        return commandeItems;
    }

    public void setCommandeItems(List<CommandeItem> commandeItems) {
        this.commandeItems = commandeItems;
    }

    public List<Paiment> getPaiments() {
        if (paiments == null) {
            paiments = new ArrayList<>();
        }
        return paiments;
    }

    public void setPaiments(List<Paiment> paiments) {
        this.paiments = paiments;
    }

    public double getMontantPaye() {
        return montantPaye;
    }

    public void setMontantPaye(double montantPaye) {
        this.montantPaye = montantPaye;
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
        final Commande other = (Commande) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.fournisseur, other.fournisseur)) {
            return false;
        }
        if (!Objects.equals(this.dateCommande, other.dateCommande)) {
            return false;
        }
        if (!Objects.equals(this.montantTotal, other.montantTotal)) {
            return false;
        }

        return true;
    }

    @Override
    public String toString() {
        return "Commande{" + "id=" + id + '}';
    }

}
