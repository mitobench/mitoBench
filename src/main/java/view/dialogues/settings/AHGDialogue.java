package view.dialogues.settings;

import Logging.LogClass;
import controller.ChartController;
import io.Exceptions.HaplogroupException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import statistics.HaploStatistics;
import view.MitoBenchWindow;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class AHGDialogue extends ATabpaneDialogue {


    protected ComboBox combobox_hglist;
    protected Button okBtn;
    protected HaploStatistics haploStatistics;
    protected Scene scene;
    protected TabPane statsTabPane;
    protected MitoBenchWindow mito;
    protected Label label;

    protected ObservableList<String> options =
            FXCollections.observableArrayList(
                    "Sub-Saharan Africa (L0a,L0d,L0k,L1b,L1c,L2a,L2b,L2c,L3b,L3d,L3e,L3f,L4,L5)",
                    "Americas and the Caribbean (A2,B2,C1b,C1c,C1d,C4c,D1,D2a,D3,D4h3a,X2a,X2g)",
                    "South-eastern Asia (M*,M7,M8,M9,G,D,N*,R*,R9,B)",
                    "Europe (H)");
    protected ChartController chartcontroller;
    protected int row;
    private String[] hg_list_trimmed;


    public AHGDialogue(String title, LogClass logClass) {
        super(title, logClass);
    }


    public void init(MitoBenchWindow mito){
        this.mito = mito;
        this.chartcontroller = mito.getChartController();
        haploStatistics = new HaploStatistics(mito.getTableControllerUserBench(), mito.getChartController(), logClass);
        addHGListCombobox(haploStatistics, mito);
        this.LOG = this.logClass.getLogger(this.getClass());
        addEvents();
    }


    public void addHGListCombobox(HaploStatistics haploStatistics, MitoBenchWindow mito){
        this.statsTabPane = mito.getTabpane_statistics();
        this.scene = mito.getScene();
        this.haploStatistics = haploStatistics;

        label = new Label("Please enter comma separated list of haplogroups " +
                "\naccording to which the haplogroups should be grouped:");
        combobox_hglist = new ComboBox(options);
        combobox_hglist.setEditable(true);


        if(mito.getChartController().getCustomHGList()!=null) {
            if (mito.getChartController().getCustomHGList().length != 0) {
                String hgs = "";
                for(String s : mito.getChartController().getCustomHGList())
                    hgs += s + ",";

                if(!combobox_hglist.getItems().contains(hgs.substring(0, hgs.length()-1))){
                    combobox_hglist.getItems().addAll(hgs.substring(0, hgs.length()-1));
                }

                combobox_hglist.getSelectionModel().select(hgs.substring(0, hgs.length()-1));

            }
        }


        okBtn = new Button("OK");
        okBtn.setId("button_ok_statistics");

        row=0;

        dialogGrid.add(label, 0,row,3,1);
        dialogGrid.add(combobox_hglist, 0,++row,3,1);
        dialogGrid.add(okBtn,2,++row,1,1);
    }


    public void addEvents(){
        okBtn.setOnAction(e -> {
            if((combobox_hglist.getSelectionModel().getSelectedItem().toString().equals("") || combobox_hglist.getSelectionModel().getSelectedItem().toString().startsWith("Please"))){
                combobox_hglist.getItems().add("Please enter list here.");
                combobox_hglist.getSelectionModel().select("Please enter list here.");

            } else {
                Task task = createTask();
                mito.getProgressBarhandler().activate(task);

                task.setOnCancelled((EventHandler<Event>) event -> {
                    statsTabPane.getTabs().remove(getTab());
                    LOG.info("Calculate Haplotype frequencies.\nSpecified Haplotypes: " + Arrays.toString(hg_list_trimmed));
                    mito.getProgressBarhandler().stop();
                });

                task.setOnSucceeded((EventHandler<Event>) event -> {
                    TableView table = haploStatistics.writeToTable();
    
                    statsTabPane.getTabs().remove(getTab());
    
                    Tab tab = new Tab();
                    tab.setId("tab_statistics");
                    tab.setText("Count statistics");
                    tab.setContent(table);
                    statsTabPane.getTabs().add(tab);
                    statsTabPane.getSelectionModel().select(tab);
    
                    LOG.info("Calculate Haplotype frequencies.\nSpecified Haplotypes: " + Arrays.toString(hg_list_trimmed));
                    mito.getProgressBarhandler().stop();

                });
                new Thread(task).start();

            }


        });

    }


    public void calculateTrimmedHGList() {

        String[] hg_list;

        String p1 = combobox_hglist.getSelectionModel().getSelectedItem().toString();

        if (p1.contains("(") && p1.contains(")")) {
            String p2 = p1.split("\\(")[1];
            p1 = p2.split("\\)")[0];
        }

        Pattern p = Pattern.compile("[A-Za-z0123456789*@+,' ()]*\n*");
        Matcher m = p.matcher(p1);
        if (m.matches()) {
            p1 = p1.replace("*", "");
            hg_list = p1.split(",");
            hg_list_trimmed = Arrays.stream(hg_list).map(String::trim).toArray(String[]::new);

        } else {
            try {
                throw new HaplogroupException("Haplogroups are not in correct format.");
            } catch (HaplogroupException e) {
                e.printStackTrace();
            }
        }

    }


    public Button getOkBtn() {
        return okBtn;
    }

    public Task createTask(){
        return new Task() {
            @Override
            protected Object call() throws Exception {
                calculateTrimmedHGList();
                haploStatistics.count(hg_list_trimmed);

                return true;
            }
        };

    }

    public String[] getHg_list_trimmed() {
        return hg_list_trimmed;
    }
}
