package utility;

import com.opencsv.CSVReader;
import org.slf4j.Logger;

import java.io.*;
import java.text.DecimalFormat;
import java.util.*;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by Behnaz.
 */
public class Parser {
    private static final String LOG_ERROR = "Error in Statistics number:{} , CustId:{} ,";
    private static final Logger LOG = getLogger(Parser.class);

    private Parser() {}

    public static Map<String, String > statisticsOneTwo(InputStream inputStream) {
        String custId = "";
        try (CSVReader csvReader = new CSVReader(new InputStreamReader(inputStream,Constants.UTF8),Constants.SEPARATOR)) {
            String[] line;
            Map<String, String > hashMap = new HashMap<>();
            csvReader.readNext();
            while ((line = csvReader.readNext()) != null) {
                custId = line[0];
                if (hashMap.containsKey(custId)) {
                    if (hashMap.get(custId).equals(Constants.ELECTRICITY) && line[1].equals(Constants.GAS)) {
                        hashMap.put(custId, Constants.ELECTRICITY_AND_GAS);
                    }
                    if (hashMap.get(custId).equals(Constants.GAS) && line[1].equals(Constants.ELECTRICITY)) {
                        hashMap.put(custId, Constants.ELECTRICITY_AND_GAS);
                    }
                } else {
                    hashMap.put(custId, line[1]); // Electricity or Gas insertion
                }
            }
            return hashMap;
        } catch (Exception e) {
            LOG.error(LOG_ERROR , "One and Two" , custId ,  e);
            return new HashMap<>();
        }
    }

    public static Result<Integer> statisticsThree(InputStream inputStream) {
        String custId = "";
        Map<String, Integer> mapElc = new HashMap<>();
        Map<String, Integer> mapGas = new HashMap<>();

        try (CSVReader csvReader = new CSVReader(new InputStreamReader(inputStream,Constants.UTF8),Constants.SEPARATOR)) {
            String[] line;
            csvReader.readNext();
            while ((line = csvReader.readNext()) != null) {
                custId = line[0];
                if (line[1].equals(Constants.ELECTRICITY)){
                    mapElc.put(custId,mapElc.containsKey(custId) ? (mapElc.get(custId) + 1)  : 1);
                }
                if (line[1].equals(Constants.GAS)){
                    mapGas.put(custId,mapGas.containsKey(custId) ? (mapGas.get(custId) + 1)  : 1);
                }
            }
            return new Result<>(mapElc,mapGas);
        } catch (Exception e) {
            LOG.error(LOG_ERROR , "Three" , custId ,  e );
            return new  Result<>(new HashMap<>(),new HashMap<>());
        }
    }

    public static Result<Double> statisticsFour(InputStream inputStream) {
        String custId = "";
        Map<String,Double> mapElc = new HashMap<>();
        Map<String, Double> mapGas = new HashMap<>();
        try (CSVReader csvReader = new CSVReader(new InputStreamReader(inputStream,Constants.UTF8),Constants.SEPARATOR)){
            int cntElec = 0;
            int cntGas = 0;
            double sumElec;
            double sumGas;
            String[] line;
            csvReader.readNext();
            while ((line = csvReader.readNext()) != null) {
                custId = line[0];
                if (line[1].equals(Constants.ELECTRICITY)) {
                    sumElec = Double.parseDouble(line[10]);
                    mapElc.put(line[6],mapElc.containsKey(line[6]) ? (mapElc.get(line[6]) + sumElec) : sumElec);
                    cntElec ++;
                }
                if (line[1].equals(Constants.GAS)) {
                    sumGas = Double.parseDouble(line[10]);
                    mapGas.put(line[6],mapGas.containsKey(line[6]) ? (mapGas.get(line[6]) + sumGas) : sumGas);
                    cntGas ++;
                }
            }
            for (int i=1; i<13 ;i++){
                if (cntElec!=0) mapElc.put(String.valueOf(i),roundDouble(mapElc.get(String.valueOf(i)) / cntElec));
                if (cntGas!=0) mapGas.put(String.valueOf(i),roundDouble(mapGas.get(String.valueOf(i)) / cntGas));
            }
            return new Result<>(mapElc,mapGas);
        } catch (Exception e) {
            LOG.error(LOG_ERROR , "Four" , custId ,  e );
            return new  Result<>(new HashMap<>(),new HashMap<>());
        }
    }

    private static double roundDouble (double value){
        DecimalFormat decimalFormat = new DecimalFormat("#.#");
        return Double.valueOf(decimalFormat.format(value));
    }
}
