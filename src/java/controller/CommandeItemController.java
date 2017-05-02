package controller;

import bean.Commande;
import bean.CommandeItem;
import bean.Produit;
import controller.util.JsfUtil;
import controller.util.JsfUtil.PersistAction;
import service.CommandeItemFacade;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@Named("commandeItemController")
@SessionScoped
public class CommandeItemController implements Serializable {
    
    @EJB
    private service.CommandeItemFacade ejbFacade;
    @EJB
    private service.CommandeFacade commandeFacade;
    private List<CommandeItem> items = null;
    private List<CommandeItem> commandeItems = null;
    private CommandeItem selected;
    private Commande selectedCommande;
    //search
    private Commande commande;
    private Produit produit;
    private Double prixMax;
    private Double prixMin;
    private Double qteMax;
    private Double qteMin;
    
    public void save() {
        commandeFacade.save(selectedCommande, commandeItems);
    }
    
    public List<CommandeItem> getCommandeItems() {
        if (commandeItems == null) {
            commandeItems = new ArrayList();
        }
        return commandeItems;
    }
    
    public void setCommandeItems(List<CommandeItem> commandeItems) {
        this.commandeItems = commandeItems;
    }
    
    public Commande getSelectedCommande() {
        if (selectedCommande == null) {
            selectedCommande = new Commande();
        }
        return selectedCommande;
    }
    
    public void setSelectedCommande(Commande selectedCommande) {
        this.selectedCommande = selectedCommande;
    }
    
    public CommandeItemController() {
    }
    
    protected void setEmbeddableKeys() {
    }
    
    protected void initializeEmbeddableKey() {
    }
    
    private CommandeItemFacade getFacade() {
        return ejbFacade;
    }
    
    public CommandeItem prepareCreate() {
        selected = new CommandeItem();
        initializeEmbeddableKey();
        return selected;
    }
    
    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("CommandeItemCreated"));
        if (!JsfUtil.isValidationFailed()) {
            if (selectedCommande.getMontantTotal() == null) {
                selectedCommande.setMontantTotal(new Double(0));
            }
            selected.setTotale(selected.getProduit().getPrix() * selected.getQuantite());
            selectedCommande.setMontantTotal(selectedCommande.getMontantTotal() + selected.getTotale());
            getCommandeItems().add(ejbFacade.clone(selected));   // Invalidate list of items to trigger re-query.
        }
        
    }
    
    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("CommandeItemUpdated"));
    }
    
    public void search() {
        items = ejbFacade.search(commande, produit, prixMax, prixMin, qteMax, qteMin);
    }
    
    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("CommandeItemDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }
    
    private void persist(PersistAction persistAction, String successMessage) {
        if (selected != null) {
            setEmbeddableKeys();
            try {
                if (persistAction != PersistAction.DELETE) {
                    getFacade().edit(selected);
                } else {
                    getFacade().remove(selected);
                }
                JsfUtil.addSuccessMessage(successMessage);
            } catch (EJBException ex) {
                String msg = "";
                Throwable cause = ex.getCause();
                if (cause != null) {
                    msg = cause.getLocalizedMessage();
                }
                if (msg.length() > 0) {
                    JsfUtil.addErrorMessage(msg);
                } else {
                    JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
                }
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            }
        }
    }
    
    public CommandeItem getCommandeItem(java.lang.Long id) {
        return getFacade().find(id);
    }
    
    public List<CommandeItem> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }
    
    public List<CommandeItem> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }
    
    @FacesConverter(forClass = CommandeItem.class)
    public static class CommandeItemControllerConverter implements Converter {
        
        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            CommandeItemController controller = (CommandeItemController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "commandeItemController");
            return controller.getCommandeItem(getKey(value));
        }
        
        java.lang.Long getKey(String value) {
            java.lang.Long key;
            key = Long.valueOf(value);
            return key;
        }
        
        String getStringKey(java.lang.Long value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }
        
        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof CommandeItem) {
                CommandeItem o = (CommandeItem) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), CommandeItem.class.getName()});
                return null;
            }
        }
        
    }
    
    public Produit getProduit() {
        return produit;
    }
    
    public void setProduit(Produit produit) {
        this.produit = produit;
    }
    
    public CommandeItemFacade getEjbFacade() {
        return ejbFacade;
    }
    
    public void setEjbFacade(CommandeItemFacade ejbFacade) {
        this.ejbFacade = ejbFacade;
    }
    
    public CommandeItem getSelected() {
        if (selected == null) {
            selected = new CommandeItem();
        }
        return selected;
    }
    
    public void setSelected(CommandeItem selected) {
        this.selected = selected;
    }
    
    public List<CommandeItem> getItems() {
        items = ejbFacade.findAll();
        return items;
    }
    
    public Commande getCommande() {
        return commande;
    }
    
    public void setCommande(Commande commande) {
        this.commande = commande;
    }
    
    public Double getPrixMax() {
        return prixMax;
    }
    
    public void setPrixMax(Double prixMax) {
        this.prixMax = prixMax;
    }
    
    public Double getPrixMin() {
        return prixMin;
    }
    
    public void setPrixMin(Double prixMin) {
        this.prixMin = prixMin;
    }
    
    public Double getQteMax() {
        return qteMax;
    }
    
    public void setQteMax(Double qteMax) {
        this.qteMax = qteMax;
    }
    
    public Double getQteMin() {
        return qteMin;
    }
    
    public void setQteMin(Double qteMin) {
        this.qteMin = qteMin;
    }
    
}
