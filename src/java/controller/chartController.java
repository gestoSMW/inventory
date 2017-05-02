/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import controller.util.ChartUtil;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.LineChartSeries;

/**
 *
 * @author the joker
 */
@Named(value = "chartController")
@SessionScoped
public class chartController implements Serializable {

    @EJB
    private service.PaimentFacade ejbFacade;
    private LineChartModel lineModel;
    private String year;
    private List<String> listOfyears;
    private List<String> listMonths;

    public void depenseStat() {
        lineModel = ChartUtil.setLineModel("bulan des depenes", "e", 1000);
        LineChartSeries series = new LineChartSeries("les depenses");
        for (int i = 1; i < 13; i++) {
            double sum = ejbFacade.sumPaiments(i, year);
            series.set(i, sum);
        }
        lineModel.addSeries(series);
    }

    public chartController() {
    }

    public List<String> getListOfyears() {
        if (listOfyears == null) {
            listOfyears = new ArrayList<>();
            for (int i = 2010; i < 2020; i++) {
                listOfyears.add(String.valueOf(i));
            }
        }
        return listOfyears;
    }

    public void setListOfyears(List<String> listOfyears) {
        this.listOfyears = listOfyears;
    }

    public LineChartModel getLineModel() {
        if (lineModel == null) {
            depenseStat();
        }
        return lineModel;
    }

    public void setLineModel(LineChartModel lineModel) {
        this.lineModel = lineModel;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

}
