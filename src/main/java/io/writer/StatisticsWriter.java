package io.writer;

import io.IOutputData;
import javafx.scene.chart.XYChart;
import statistics.HaploStatistics;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Created by neukamm on 17.01.17.
 */
public class StatisticsWriter implements IOutputData{

    private HashMap<String, List<XYChart.Data<String, Number>>> data_all;
    private HaploStatistics haploStatistics;
    private List<String> keys;

    public StatisticsWriter(HaploStatistics haploStatistics){
        this.haploStatistics = haploStatistics;
    }

    /**
     * This method writes the statistics to csv file.
     * @throws IOException
     */
    @Override
    public void writeData(String path) throws IOException {


        if(haploStatistics != null && path.length()>0){
            data_all = this.haploStatistics.getData_all();

            keys = new ArrayList<>();
            keys.addAll(data_all.keySet());
            keys.remove("Others");
            Collections.sort(keys);
            keys.add("Others");


            if(!path.endsWith("csv"))
                path = path + ".csv";

            write(new FileOutputStream(new File(path).getAbsoluteFile()));



        }

    }

    public void write(OutputStream outputStream) throws IOException {
        OutputStreamWriter writerOutputStream = new OutputStreamWriter(outputStream, "UTF-8");
        // write header
        writerOutputStream.write("Population , Sum, ");
        for(int i = 0; i < keys.size(); i++){
            if(i == keys.size()-1){
                writerOutputStream.write(keys.get(i));
            }
            else {
                String tmp = keys.get(i) + ",";
                writerOutputStream.write(tmp);
            }
        }
        writerOutputStream.write("\n");

        // write population HG count information
        for(int i = 0; i < this.haploStatistics.getNumber_of_groups() ; i++){
            int count_all_hgs = haploStatistics.countAllHGs(i);
            for(String key : data_all.keySet()){
                List<XYChart.Data<String, Number>> data_list = data_all.get(key);
                String tmp = data_list.get(i).getXValue() + "," + count_all_hgs + ",";
                writerOutputStream.write(tmp);
                break;
            }


            for(int k = 0; k < keys.size(); k++){
                List<XYChart.Data<String, Number>> data_list = data_all.get(keys.get(k));
                if(k == keys.size()-1){
                    int val = data_list.get(i).getYValue().intValue();
                    writerOutputStream.write(val+"");
                }
                else{
                    int val = data_list.get(i).getYValue().intValue();
                    writerOutputStream.write(val+"");
                    writerOutputStream.write(",");
                }

            }

            writerOutputStream.write("\n");
        }
        writerOutputStream.close();


    }


    @Override
    public void setGroups(String groupID) {
        //Do nothing here, not required for this format at all
    }
}
