package io.writer;

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
public class StatisticsWriter {

    private HashMap<String, List<XYChart.Data<String, Number>>> data_all;

    public StatisticsWriter(){}

    /**
     * This method writes the statistics to csv file.
     * @param haploStatistics
     * @throws IOException
     */
    public void write(String path, HaploStatistics haploStatistics) throws IOException {
        if(haploStatistics != null){

            data_all = haploStatistics.getData_all();

            Writer writer = null;

            List<String> keys = new ArrayList<>();
            keys.addAll(data_all.keySet());
            keys.remove("Others");
            Collections.sort(keys);
            keys.add("Others");


            if(!path.endsWith("csv"))
                path = path + ".csv";

            writer = new BufferedWriter(new FileWriter(new File(path)));

            // write header
            writer.write("Population , Sum, ");
            for(int i = 0; i < keys.size(); i++){
                if(i == keys.size()-1)
                    writer.write(keys.get(i));
                else
                    writer.write(keys.get(i) + ",");
            }
            writer.write("\n");

            // write population HG count information
            for(int i = 0; i < haploStatistics.getNumber_of_groups() ; i++){
                int count_all_hgs = countAllHGs(i);
                for(String key : data_all.keySet()){
                    List<XYChart.Data<String, Number>> data_list = data_all.get(key);
                    writer.write(data_list.get(i).getXValue() + "," + count_all_hgs + "," );
                    break;
                }


                for(int k = 0; k < keys.size(); k++){
                    List<XYChart.Data<String, Number>> data_list = data_all.get(keys.get(k));
                    if(k == keys.size()-1)
                        writer.write(data_list.get(i).getYValue().intValue()+"");
                    else
                        writer.write(data_list.get(i).getYValue().intValue() + ",");
                }

                writer.write("\n");
            }
            writer.close();

        }

    }

    /**
     * This method count all haplogroups below one group.
     * @param group
     * @return
     */
    private int countAllHGs(int group){
        int count=0;
        for(String key : data_all.keySet()){
            count += data_all.get(key).get(group).getYValue().intValue();
        }
        return count;
    }
}
