package controller;

import bean.Sortie;
import bean.SortieItem;
import controller.util.JsfUtil;
import controller.util.JsfUtil.PersistAction;
import service.SortieFacade;

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

@Named("sortieController")
@SessionScoped
public class SortieController implements Serializable {

    @EJB
    private service.SortieFacade ejbFacade;
    @EJB
    private service.SortieItemFacade sortieItemFacade;
    private List<Sortie> items = null;
    private Sortie selected;
    private List<SortieItem> sortieItems = null;

    public void itemsListener() {
        sortieItems = sortieItemFacade.findBySortie(selected);
    }

    public void refreshList() {
        items = ejbFacade.findAll();
    }

    public SortieController() {
    }

    public Sortie getSelected() {
        return selected;
    }

    public void setSelected(Sortie selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private SortieFacade getFacade() {
        return ejbFacade;
    }

    public Sortie prepareCreate() {
        selected = new Sortie();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("SortieCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("SortieUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("SortieDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Sortie> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }

    public List<SortieItem> getSortieItems() {
        if (sortieItems == null) {
            sortieItems = new ArrayList<>();
        }
        return sortieItems;
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

    public Sortie getSortie(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<Sortie> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Sortie> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = Sortie.class)
    public static class SortieControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            SortieController controller = (SortieController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "sortieController");
            return controller.getSortie(getKey(value));
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
            if (object instanceof Sortie) {
                Sortie o = (Sortie) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Sortie.class.getName()});
                return null;
            }
        }

    }

}
