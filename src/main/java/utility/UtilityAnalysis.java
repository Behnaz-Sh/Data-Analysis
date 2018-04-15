package utility;

import org.slf4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Collections;
import java.util.Map;

import java.util.zip.GZIPInputStream;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by Behnaz.
 */
public class UtilityAnalysis {
    private static final Logger LOG = getLogger(UtilityAnalysis.class);

    public static void main(String[] args) {
        try{
            File file = new File(UtilityAnalysis.class.getClassLoader().getResource("sample.csv.gz").getFile());
            if (fileValidation(file) != Constants.SUCCESSFUL_OPERATION) System.exit(1);
            if (statisticsOneTwo(file) != Constants.SUCCESSFUL_OPERATION) System.exit(1);
            if (statisticsThree(file) != Constants.SUCCESSFUL_OPERATION) System.exit(1);
            if (statisticsFour(file) != Constants.SUCCESSFUL_OPERATION) System.exit(1);
        } catch (Exception exception){
            LOG.error("Error in data file reading !", exception);
        }
    }

    private static int fileValidation(File file) {
        try (InputStream inputStream = new GZIPInputStream(new FileInputStream(file))) {
            int result = Validator.fileValidation(inputStream);
            switch (result) {
                case Constants.FILE_HEADER_ERROR:
                    LOG.info("Header in the data file has problem!");
                    return -1;
                case Constants.FILE_HEADER_ROWS_MISMATCH_ERROR:
                    LOG.info("Mismatch between rows and header of the data file!");
                    return -1;
                case Constants.FILE_ROWS_NO_CONTENT:
                    LOG.info("There is no rows in the data file!");
                    return -1;
                case Constants.FILE_ROWS_ERROR:
                    LOG.info("Invalid data is exist in the data file!");
                    return -1;
                case Constants.FILE_EXCEPTION_ERROR:
                    LOG.info("Data File has problem!");
                    return -1;
                default:
                    LOG.info("****************************************************************");
                    LOG.info("*************** Successful Data File Validation! ***************");
                    LOG.info("****************************************************************");
            }
        } catch (Exception exception) {
            LOG.error("Error while validating file", exception);
            return -1;
        }
        return Constants.SUCCESSFUL_OPERATION;
    }

    // Solution for statistics One and Two
    private static int statisticsOneTwo(File file) {
        try (InputStream inputStream = new GZIPInputStream(new FileInputStream(file))) {
            Map map = Parser.statisticsOneTwo(inputStream);
            if (map.size() == 0) return -1;
            LOG.info("1) Number of unique customers: {} ", map.size());
            LOG.info("2) Number of customers that have electricity only: {} ", Collections.frequency(map.values(), Constants.ELECTRICITY));
            LOG.info("\t Number of customers that have gas only: {} ", Collections.frequency(map.values(), Constants.GAS));
            LOG.info("\t Number of customers that have both electricity and gas : {} ", Collections.frequency(map.values(), Constants.ELECTRICITY_AND_GAS));
        } catch (Exception exception) {
            LOG.error("Error while running statistics One and Two ", exception);
            return -1;
        }
        return Constants.SUCCESSFUL_OPERATION;
    }

    // Solution for statistics Three
    private static int statisticsThree(File file) {
        try (InputStream inputStream = new GZIPInputStream(new FileInputStream(file))) {
            Result<Integer> result = Parser.statisticsThree(inputStream);
            if (result.isResultEmpty()) return -1;
            LOG.info("3) Number of meter readings per customer per resource:");

            if (!result.getMapElc().isEmpty()){
                int maxElc = Collections.max(result.getMapElc().values());
                LOG.info("\t Electricity");
                for (int i = 1; i <= maxElc ; i++) {
                    String h = String.valueOf(Collections.frequency(result.getMapElc().values(), i));
                    if (!h.equals("0")) {
                        LOG.info("\t\t\t\t {} customers have {} number of meter reading" , i , h);
                    }
                }
            }

            if (!result.getMapGas().isEmpty()){
                int maxGas = Collections.max(result.getMapGas().values());
                LOG.info("\t Gas");
                for (int i = 1; i <= maxGas ; i++) {
                    String g = String.valueOf(Collections.frequency(result.getMapGas().values(), i));
                    if (!g.equals("0")) {
                        LOG.info("\t\t\t\t {} customers have {} number of meter reading" , i , g);
                    }
                }
            }
        } catch (Exception exception) {
            LOG.error("Error while running statistics Three ", exception);
            return -1;
        }
        return Constants.SUCCESSFUL_OPERATION;
    }

    // Solution for statistics Four
    private static int statisticsFour(File file) {
        try (InputStream inputStream = new GZIPInputStream(new FileInputStream(file))) {
            Result<Double> result = Parser.statisticsFour(inputStream);
            if (result.isResultEmpty()) return -1;
            LOG.info("4) Average consumption per bill month per resource:");
            if (!result.getMapElc().isEmpty()){
                LOG.info("\t Electricity");
                LOG.info("\t\t\t\t January: {}" , result.getMapElc().get(Constants.JANUARY));
                LOG.info("\t\t\t\t February: {}" , result.getMapElc().get(Constants.FEBRUARY));
                LOG.info("\t\t\t\t March: {}" , result.getMapElc().get(Constants.MARCH));
                LOG.info("\t\t\t\t April: {}" , result.getMapElc().get(Constants.APRIL));
                LOG.info("\t\t\t\t May: {}" , result.getMapElc().get(Constants.MAY));
                LOG.info("\t\t\t\t June: {}" , result.getMapElc().get(Constants.JUNE));
                LOG.info("\t\t\t\t July: {}" , result.getMapElc().get(Constants.JULY));
                LOG.info("\t\t\t\t August: {}" , result.getMapElc().get(Constants.AUGUST));
                LOG.info("\t\t\t\t September: {}" , result.getMapElc().get(Constants.SEPTEMBER));
                LOG.info("\t\t\t\t October: {}" , result.getMapElc().get(Constants.OCTOBER));
                LOG.info("\t\t\t\t November: {}" , result.getMapElc().get(Constants.NOVEMBER));
                LOG.info("\t\t\t\t December: {}" , result.getMapElc().get(Constants.DECEMBER));
            }
            if (!result.getMapGas().isEmpty()) {
                LOG.info("\t Gas");
                LOG.info("\t\t\t\t January: {}", result.getMapGas().get(Constants.JANUARY));
                LOG.info("\t\t\t\t February: {}", result.getMapGas().get(Constants.FEBRUARY));
                LOG.info("\t\t\t\t March: {}", result.getMapGas().get(Constants.MARCH));
                LOG.info("\t\t\t\t April: {}", result.getMapGas().get(Constants.APRIL));
                LOG.info("\t\t\t\t May: {}", result.getMapGas().get(Constants.MAY));
                LOG.info("\t\t\t\t June: {}", result.getMapGas().get(Constants.JUNE));
                LOG.info("\t\t\t\t July: {}", result.getMapGas().get(Constants.JULY));
                LOG.info("\t\t\t\t August: {}", result.getMapGas().get(Constants.AUGUST));
                LOG.info("\t\t\t\t September: {}", result.getMapGas().get(Constants.SEPTEMBER));
                LOG.info("\t\t\t\t October: {}", result.getMapGas().get(Constants.OCTOBER));
                LOG.info("\t\t\t\t November: {}", result.getMapGas().get(Constants.NOVEMBER));
                LOG.info("\t\t\t\t December: {}", result.getMapGas().get(Constants.DECEMBER));
            }
        } catch (Exception exception) {
            LOG.error("Error while running statistics Four ", exception);
            return -1;
        }
        return Constants.SUCCESSFUL_OPERATION;
    }
}
