/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.util;

import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.LineChartModel;

/**
 *
 * @author the joker
 */
public class ChartUtil {

    public static LineChartModel setLineModel(String title, String legendPosition, int maxAxis) {
        LineChartModel model = new LineChartModel();
        model.setTitle(title);
        model.setLegendPosition(legendPosition);
        Axis yAxis = model.getAxis(AxisType.Y);
        Axis xAxis=model.getAxis(AxisType.X);
        yAxis.setMin(0);
        yAxis.setMax(maxAxis);
        xAxis.setMin(1);
        xAxis.setMax(12);
        return model;
    }

}
