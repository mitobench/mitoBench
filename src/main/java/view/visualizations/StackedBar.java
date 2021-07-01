package view.visualizations;

import controller.ChartController;
import controller.GroupController;
import controller.TableControllerUserBench;
import controller.VisualizationController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.effect.Glow;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import view.menus.VisualizationMenu;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by neukamm on 29.11.16.
 */
public class StackedBar extends AChart{

    private final TableControllerUserBench tableController;
    private List< XYChart.Series<String, Number>> seriesList = new ArrayList<>();
    private StackedBarChart<String, Number> sbc;
    private TabPane tabPane;
    private final Glow glow = new Glow(.5);
    private VisualizationMenu graphicsMenu;
    private ChartController chartController;
    private String[] hg_user_selection;
    private VisualizationController visualizationController;

    public StackedBar(String title, TabPane vBox, VisualizationMenu graphicsMenu, ChartController cc, TableControllerUserBench tc,
                      VisualizationController visualizationController) {
        super("", "Frequency in %", graphicsMenu.getLogClass());

        tabPane = vBox;
        this.graphicsMenu = graphicsMenu;
        chartController = cc;
        tableController = tc;

        // set auto-ranging to false to allow manual settings
        yAxis.setAutoRanging(false);
        yAxis.setLowerBound(0);
        yAxis.setUpperBound(100);

        sbc = new StackedBarChart<>(xAxis, yAxis);
        sbc.setTitle(title);
        sbc.prefWidthProperty().bind(tabPane.widthProperty());
        sbc.setAnimated(false);
        sbc.setCategoryGap(20);
        sbc.setLegendSide(Side.TOP);

        this.visualizationController = visualizationController;

        setContextMenu(sbc, vBox);

    }


    /**
     * This method adds data to the bar plot as series
     *
     * @param data for bar plot
     * @param name  name of the data set
     */
    public void addSeries(List<XYChart.Data<String, Number>> data, String name){
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName(name);

        for(int i = 0; i < data.size(); i++){
            series.getData().add(data.get(i));
        }

        boolean onlyNull = true;
        for(int i = 0; i < series.getData().size(); i++){
            if(series.getData().get(i).getYValue().doubleValue() > 0){
                onlyNull = false;
            }
        }

        if(!onlyNull){
            this.seriesList.add(series);
        }

    }


    /**
     * This method cleans up all data
     */
    public void clearData(){
        sbc.getData().clear();
        seriesList.clear();
        xAxis.getCategories().clear();
    }


    /**
     * This method adds a tooltip to the chart, which provides information such as the name of the Haplogroup and their
     * occurrences.
     *
     * @param
     */
    public void addTooltip(){

        for (final XYChart.Series<String, Number> series : sbc.getData()) {
            for (final XYChart.Data<String, Number> data : series.getData()) {
                Tooltip tooltip = new Tooltip();
                data.getNode().setOnMouseMoved(event -> {
                    // +15 moves the tooltip 15 pixels below the mouse cursor;
                    tooltip.show(data.getNode(), event.getScreenX(), event.getScreenY() + 15);
                    tooltip.setText(series.getName() + " | " + data.getYValue().toString() + "%");
                });
                data.getNode().setOnMouseExited(event -> tooltip.hide());

            }
        }
    }


    /**
     * This method adds a listener to each part of the bar (representing one macro-HG of group).
     * By clicking on this part, a new bar plot opens that contains only the haplogroups of the macrogroup.
     */
    public void addListener(){
        //now you can get the nodes.
        for (XYChart.Series<String,Number> serie: sbc.getData()){
            for (XYChart.Data<String, Number> item: serie.getData()){
                Node n = item.getNode();
                n.setEffect(null);
                n.setOnMouseEntered(e -> n.setEffect(glow));
                n.addEventHandler(MouseEvent.MOUSE_EXITED, mouseEvent -> n.setEffect(null));

                n.setOnMouseClicked(e -> {
                    if(MouseButton.PRIMARY.equals(e.getButton())){
                        createSubBarPlot(item);
                    }
                });
            }
        }
    }

    /**
     * This method gets all haplogroups that belongs to the macrogroup and are represented in a
     * group.
     *
     * @param item
     * @throws MalformedURLException
     */
    private void createSubBarPlot(XYChart.Data<String, Number> item) {

        String hg = item.getNode().accessibleTextProperty().get().split(" ")[0].trim();
        String group = item.getXValue();

        TableColumn haplo_col = graphicsMenu.getTableController().getTableColumnByName("Haplogroup");
        TableColumn group_col = graphicsMenu.getTableController().getTableColumnByName("Grouping");
        String[] selection_haplogroups;

        if(group_col==null){
            String[][] cols = chartController.prepareColumns(new String[]{"Haplogroup"}, tableController.getSelectedRows());
            selection_haplogroups = cols[0];
        } else{
            // get only those haplogroups that does not already correspond to another macroHG displayed
            String[][] cols = chartController.prepareColumns(new String[]{"Haplogroup", "Grouping"}, tableController.getSelectedRows());
            selection_haplogroups = cols[0];

        }

        HashMap<String, ArrayList> hgs_summed = chartController.summarizeHaplogroups(Arrays.asList(selection_haplogroups), hg_user_selection);
        List<String> sub_hgs = hgs_summed.get(hg);

        if(sub_hgs != null){

            visualizationController.initHaploBarchart(hg + " ("+ group +")");
            List<String> columnData = new ArrayList<>();

            for (Object tmp : graphicsMenu.getTableController().getTable().getItems()) {

                String hg_row = (String) haplo_col.getCellObservableValue(tmp).getValue();
                if(hg_row.contains("+")){
                    hg_row = hg_row.split("\\+")[0];
                }

                if(group_col==null){
                    if(sub_hgs.contains(hg_row))
                        columnData.add((String)haplo_col.getCellObservableValue(tmp).getValue());
                } else {
                    if(sub_hgs.contains(hg_row) &&
                            group.equals(group_col.getCellObservableValue(tmp).getValue()))
                        columnData.add((String)haplo_col.getCellObservableValue(tmp).getValue());
                }


            }

            graphicsMenu.createHaploBarchart(haplo_col, columnData);
        }
    }


    /*

            GETTER and SETTER


     */

    public void setCategories(String[] groups){
        ObservableList categories = FXCollections.observableArrayList();
        categories.addAll(Arrays.asList(groups));
        xAxis.setCategories(categories);
    }
    public List<XYChart.Series<String, Number>> getSeriesList() {
        return seriesList;
    }
    public StackedBarChart<String, Number> getSbc() {
        return sbc;
    }
    public void setHg_user_selection(String[] hg_user_selection) {
        this.hg_user_selection = hg_user_selection;
    }


    @Override
    protected void layoutChartChildren(double v, double v1, double v2, double v3) {

    }
}
