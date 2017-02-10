package edu.kit.pse.osip.monitoring.view.dashboard;

import edu.kit.pse.osip.core.model.base.AbstractTank;
import edu.kit.pse.osip.core.utils.language.Translator;
import java.util.Observable;
import java.util.Observer;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

/**
 * Visualizes a progression.
 * 
 * @author Martin Armbruster
 * @version 1.0
 */
final class ProgressVisualization implements Observer {
    /**
     * Stores the amount of milliseconds per second.
     */
    private final int msPerSec = 1000;
    
    /**
     * Saves the creation time of this instance in milliseconds.
     */
    private long creationTime;
    
    /**
     * true when this progression is enabled and tracked.
     */
    private boolean isEnabled;
    
    /**
     * Saves the name of this progression.
     */
    private String progressName;
    
    /**
     * The chart shows the progression.
     */
    private LineChart<Number, Number> progressChart;
    
    /**
     * The series that stores all values.
     */
    private XYChart.Series<Number, Number> progressSeries;
    
    /**
     * Creates and initializes a new chart for the progression.
     * 
     * @param progressName The name of this progression.
     */
    protected ProgressVisualization(String progressName) {
        creationTime = System.currentTimeMillis();
        this.progressName = progressName;
        isEnabled = true;
        NumberAxis x = new NumberAxis();
        x.setLabel(Translator.getInstance().getString("monitoring.tank.progress.x"));
        NumberAxis y = new NumberAxis();
        y.setLabel(Translator.getInstance().getString("monitoring.tank.progress.y"));
        progressChart = new LineChart<Number, Number>(x, y);
        progressSeries = new XYChart.Series<Number, Number>();
        progressSeries.setName(Translator.getInstance().getString("monitoring.tank.progress.legend"));
        progressChart.getData().add(progressSeries);
    }
    
    /**
     * Returns the name of this progression.
     * 
     * @return the progression name.
     */
    protected String getProgressName() {
        return progressName;
    }
    
    /**
     * Returns the chart showing the progression.
     * 
     * @return the chart showing the progression.
     */
    protected LineChart<Number, Number> getProgressChart() {
        return progressChart;
    }
    
    /**
     * Enables or disables this progression.
     * 
     * @param progressEnabled true if the progression should be enabled and false otherwise.
     */
    protected void setProgressEnabled(boolean progressEnabled) {
        isEnabled = progressEnabled;
    }
    
    /**
     * Called when the observed object has updated.
     * 
     * @param observable The observed object.
     * @param object The new value.
     */
    public void update(Observable observable, Object object) {
        if (!isEnabled) {
            return;
        }
        AbstractTank tank = (AbstractTank) observable;
        long x = (System.currentTimeMillis() - creationTime) / msPerSec;
        XYChart.Data<Number, Number> newDataPoint;
        if (progressName.equals(Translator.getInstance().getString("monitoring.tank.progress.temperature"))) {
            newDataPoint = new XYChart.Data<Number, Number>(x, tank.getLiquid().getTemperature());
        } else {
            newDataPoint = new XYChart.Data<Number, Number>(x, tank.getFillLevel());
        }
        progressSeries.getData().add(newDataPoint);
    }
}
