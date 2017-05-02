package controller;

import bean.Magasin;
import bean.Sortie;
import bean.SortieItem;
import controller.util.JsfUtil;
import controller.util.JsfUtil.PersistAction;
import service.SortieItemFacade;

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
import service.SortieFacade;

@Named("sortieItemController")
@SessionScoped
public class SortieItemController implements Serializable {

    @EJB
    private service.SortieItemFacade ejbFacade;
    @EJB
    private service.SortieFacade sortieFacade;
    @EJB
    private service.MagasinFacade magasinFacade;
    private List<SortieItem> items = null;
    private SortieItem selected;
    private Sortie sortie;
    private List<Magasin> magasins;

    public int validateSortie() {
        int i = 0;
        if (sortie.getRecepteur().getId() == null) {
            FacesContext.getCurrentInstance().addMessage("recepteurMsg",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "aucun recepeteur n'est selectionné", "aucun recepeteur n'est selectionné"));
            i = 1;
        }
        if (sortie.getDateSortie() == null) {
            FacesContext.getCurrentInstance().addMessage("datesortieMsg",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "aucune date n'a été choisi", "aucune date n'a été choisi"));
            i = 2;
        }
        return i;
    }

    public int validateSortieItem() {
        int i = 0;
        if (selected.getMagasin().getId() == null) {
            FacesContext.getCurrentInstance().addMessage("magasinMsg",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "aucune magasin n'a été choisi", "aucune magasin n'a été choisi"));
            i = 3;
        }
        if (selected.getProduit().getId() == null) {
            FacesContext.getCurrentInstance().addMessage("produitMsg",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "aucun produit n'a été choisi", "aucune produit n'a été choisi"));
            i = 4;
        }
        if (selected.getQuantite() == 0.0) {
            FacesContext.getCurrentInstance().addMessage("quantiteMsg",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "la quantite ne doit pas etre nulle", "la quantite ne doit pas etre nulle"));
            i = 5;
        }
        return i;
    }

    public void magasinListener() {
        magasins = magasinFacade.findByProductAndQuantite(getSelected().getProduit(), getSelected().getQuantite());
    }

    public void addItem() {
        int v = validateSortieItem();
        if (v == 0) {
            items.add(getFacade().clone(getSelected()));
        }
    }

    public SortieItemController() {
    }

    public Sortie getSortie() {
        if (sortie == null) {
            sortie = new Sortie();
        }
        return sortie;
    }

    public void setSortie(Sortie sortie) {
        this.sortie = sortie;
    }

    public List<Magasin> getMagasins() {
        if (magasins == null) {
            magasins = new ArrayList<>();
        }
        return magasins;
    }

    public void setMagasins(List<Magasin> magasins) {
        this.magasins = magasins;
    }

    public SortieItem getSelected() {
        if (selected == null) {
            selected = new SortieItem();
        }
        return selected;
    }

    public void setSelected(SortieItem selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private SortieItemFacade getFacade() {
        return ejbFacade;
    }

    public SortieFacade getSortieFacade() {
        return sortieFacade;
    }

    public SortieItem prepareCreate() {
        selected = new SortieItem();
        initializeEmbeddableKey();
        return selected;
    }

    public String create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("SortieItemCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
        return "List.xhtml";
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("SortieItemUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("SortieItemDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<SortieItem> getItems() {
        if (items == null) {
            items = new ArrayList<>();
        }
        return items;
    }

    private void persist(PersistAction persistAction, String successMessage) {
        if (selected != null) {
            setEmbeddableKeys();
            try {
                if (persistAction != PersistAction.DELETE) {
                    int v = validateSortie();
                    if (v == 0) {
                        getSortieFacade().save(sortie, items);
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

    public SortieItem getSortieItem(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<SortieItem> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<SortieItem> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = SortieItem.class)
    public static class SortieItemControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            SortieItemController controller = (SortieItemController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "sortieItemController");
            return controller.getSortieItem(getKey(value));
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
            if (object instanceof SortieItem) {
                SortieItem o = (SortieItem) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), SortieItem.class.getName()});
                return null;
            }
        }

    }

}
