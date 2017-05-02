package controller;

import bean.CommandeItem;
import bean.Livraison;
import bean.LivraisonItem;
import controller.util.JsfUtil;
import controller.util.JsfUtil.PersistAction;
import service.LivraisonItemFacade;

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
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import service.CommandeItemFacade;
import service.LivraisonFacade;

@Named("livraisonItemController")
@SessionScoped
public class LivraisonItemController implements Serializable {

    @EJB
    private service.LivraisonItemFacade ejbFacade;
    @EJB
    private service.CommandeItemFacade commandeItemFacade;
    @EJB
    private service.LivraisonFacade livraisonFacade;
    @EJB
    private service.MagasinFacade magasinFacade;
    private List<LivraisonItem> items = null;
    private LivraisonItem selected;
    private List<CommandeItem> commandeItems = null;
    private CommandeItem selectedCommandeItem;
    private Livraison selectedLivraison;

    public int validateLivraison() {
        int i = 0;
        if (selectedLivraison.getCommande().getId() == null) {
            FacesContext.getCurrentInstance().addMessage("commandeMsg",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "aucune commande n'est selectionnée", "aucune commande n'est selectionnée"));
            i = 4;
        }
        if (selectedLivraison.getDateLivraison() == null) {
            FacesContext.getCurrentInstance().addMessage("dateLivraisonMsg",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "aucune date n'a été choisi", "aucune date n'a été choisi"));
            i = 5;
        }
        return i;
    }

    public int validateLivraisonItem() {
        int i = 0;
        if (selectedCommandeItem == null) {
            FacesContext.getCurrentInstance().addMessage("quantiteMsg",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "aucune commande item n'est selectionnée", "aucune commande item n'est selectionnée"));
            i = 1;
        } else if (selected.getQuantite() == 0.0) {
            FacesContext.getCurrentInstance().addMessage("quantiteMsg",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "la quantite ne peut pas etre nulle", "la quantite ne peut pas etre nulle"));
            i = 2;
        } else if (selectedCommandeItem.getQuantite() < getSelectedCommandeItem().getQuantiteLivree() + getSelected().getQuantite()) {
            double quantiteMax = selectedCommandeItem.getQuantite() - selectedCommandeItem.getQuantiteLivree();
            FacesContext.getCurrentInstance().addMessage("quantiteMsg",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "la quantite max est " + quantiteMax, "la quantite max est " + quantiteMax));
            i = 3;
        }

        if (selected.getMagasin().getId() == null) {
            FacesContext.getCurrentInstance().addMessage("magasinMsg",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "aucune magasin n'a été choisi", "aucune magasin n'a été choisi"));
            i = 6;
        }

        return i;

    }

    public void commandeItemsListener() {
        commandeItems = commandeItemFacade.commandeItemNonLivrees(getSelectedLivraison().getCommande());
    }

    public void addItem() {
        int v = validateLivraisonItem();
        if (v == 0) {
            getSelected().setProduit(selectedCommandeItem.getProduit());
            items.add(getFacade().clone(getSelected()));
        }
    }

    public LivraisonItemController() {
    }

    public LivraisonItem getSelected() {
        if (selected == null) {
            selected = new LivraisonItem();
        }
        return selected;
    }

    public void setSelected(LivraisonItem selected) {
        this.selected = selected;
    }

    public CommandeItem getSelectedCommandeItem() {
        if (selectedCommandeItem == null) {
            selectedCommandeItem = new CommandeItem();
        }
        return selectedCommandeItem;
    }

    public void setSelectedCommandeItem(CommandeItem selectedCommandeItem) {
        this.selectedCommandeItem = selectedCommandeItem;
    }

    public Livraison getSelectedLivraison() {
        if (selectedLivraison == null) {
            selectedLivraison = new Livraison();
        }
        return selectedLivraison;
    }

    public void setSelectedLivraison(Livraison selectedLivraison) {
        this.selectedLivraison = selectedLivraison;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private LivraisonItemFacade getFacade() {
        return ejbFacade;
    }

    public CommandeItemFacade getCommandeItemFacade() {
        return commandeItemFacade;
    }

    public LivraisonFacade getLivraisonFacade() {
        return livraisonFacade;
    }

    public LivraisonItem prepareCreate() {
        selected = new LivraisonItem();
        initializeEmbeddableKey();
        return selected;
    }

    public String create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("LivraisonItemCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
        return "List.xhtml";
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("LivraisonItemUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("LivraisonItemDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<LivraisonItem> getItems() {
        if (items == null) {
            items = new ArrayList<>();
        }
        return items;
    }

    public List<CommandeItem> getCommandeItems() {
        if (commandeItems == null) {
            commandeItems = new ArrayList<>();
        }
        return commandeItems;
    }

    private void persist(PersistAction persistAction, String successMessage) {
        if (selected != null) {
            setEmbeddableKeys();
            try {
                if (persistAction != PersistAction.DELETE) {
                    int v = validateLivraison();
                    if (v == 0) {
                        getLivraisonFacade().save(selectedLivraison, items);
                    }
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

    public LivraisonItem getLivraisonItem(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<LivraisonItem> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<LivraisonItem> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = LivraisonItem.class)
    public static class LivraisonItemControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            LivraisonItemController controller = (LivraisonItemController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "livraisonItemController");
            return controller.getLivraisonItem(getKey(value));
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
            if (object instanceof LivraisonItem) {
                LivraisonItem o = (LivraisonItem) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), LivraisonItem.class.getName()});
                return null;
            }
        }

    }

}
