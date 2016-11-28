package view.charts;

/**
 * Created by neukamm on 28.11.16.
 */
import java.util.HashMap;
import java.util.Map;

import javafx.scene.Node;
import javafx.scene.chart.Axis;
import javafx.scene.chart.BarChart;
import javafx.scene.text.Text;


public class BarChartExt<X, Y> extends BarChart<X, Y> {

    /**
     * Registry for text nodes of the bars
     */
    Map<Node, Node> nodeMap = new HashMap<>();

    public BarChartExt(Axis xAxis, Axis yAxis) {
        super(xAxis, yAxis);
    }

    /**
     * Add text for bars
     */
    @Override
    protected void seriesAdded(Series<X, Y> series, int seriesIndex) {

        super.seriesAdded(series, seriesIndex);

        for (int j = 0; j < series.getData().size(); j++) {

            Data<X, Y> item = series.getData().get(j);

            Node text = new Text(String.valueOf(item.getYValue()));
            nodeMap.put(item.getNode(), text);
            getPlotChildren().add(text);

        }

    }

    /**
     * Remove text of bars
     */
    @Override
    protected void seriesRemoved(final Series<X, Y> series) {

        for (Node bar : nodeMap.keySet()) {

            Node text = nodeMap.get(bar);
            getPlotChildren().remove(text);

        }

        nodeMap.clear();

        super.seriesRemoved(series);
    }

    /**
     * Adjust text of bars, position them on top
     */
    @Override
    protected void layoutPlotChildren() {

        super.layoutPlotChildren();

        for (Node bar : nodeMap.keySet()) {

            Node text = nodeMap.get(bar);

            text.relocate(bar.getBoundsInParent().getMinX(), bar.getBoundsInParent().getMinY() - 30);

        }

    }
}
