package utility;

import com.opencsv.CSVReader;
import org.slf4j.Logger;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashSet;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by Behnaz.
 */
public class Validator {
    private static final Logger LOG = getLogger(Validator.class);

    private Validator() {}

    public static int fileValidation(InputStream inputStream) {
        String custId ="";
        try(CSVReader csvReader = new CSVReader(new InputStreamReader(inputStream,Constants.UTF8),Constants.SEPARATOR)){
            String[] line;
            String[] header = csvReader.readNext(); // current file header

            // ***************************************** Check header validation ***************************************
            LinkedHashSet<String> headerSet = new LinkedHashSet<>(Arrays.asList(header));// remove header's duplication
            LinkedHashSet<String> srcHeaderSet = createOriginalHeader();// original header pattern & right column order
            if (headerSet.size() != srcHeaderSet.size() || !headerSet.containsAll(srcHeaderSet)){
                return Constants.FILE_HEADER_ERROR;
            }
            // here we have two same size/caption headers, sounds good ! now, just check the file header cols order
            Iterator<String> itrHeader = headerSet.iterator();
            Iterator<String> itrOrigin = srcHeaderSet.iterator();
            while (itrHeader.hasNext()) {
                if (!itrOrigin.next().equals(itrHeader.next())) return Constants.FILE_HEADER_ERROR;
            }
            // ***************************************** Check rows validation *****************************************
            while ((line = csvReader.readNext()) != null) {
                custId = line[0];
                if (line.length != header.length){ // check each line have the same length like header
                    LOG.info(" Problem for custId {}" , custId );
                    return Constants.FILE_HEADER_ROWS_MISMATCH_ERROR;
                }

                if (!isRowValueValid (line)){
                    LOG.info(" Problem for custId {}" , custId );
                    return Constants.FILE_ROWS_ERROR;
                }
            }
            boolean bln = csvReader.getLinesRead() > 1;
            if (!bln) return Constants.FILE_ROWS_NO_CONTENT;
        } catch (Exception e) {
            LOG.error("Error for custId {}" , custId ,  e);
            return Constants.FILE_EXCEPTION_ERROR;
        }
        return Constants.SUCCESSFUL_OPERATION;
    }

    private static boolean isRowValueValid (String[] line) {
        try{
            if (!(isNumeric(line[0]) && isNumeric(line[1]) && isNumeric(line[3]) &&
                  isNumeric(line[4]) && isNumeric(line[5]) && isNumeric(line[6]))){
                return false;
            }
            Number number = NumberFormat.getInstance().parse(line[0]);
            if (!(number.longValue() > Long.MIN_VALUE && number.longValue() < Long.MAX_VALUE)){
                return false;
            }
            if (!(Integer.parseInt(line[6]) >= 1 && Integer.parseInt(line[6]) <= 12)) { // Bill Month
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private static boolean isNumeric(String string) {  // string is numeric (-+0..9(.)0...9)
        return string.matches("^[-+]?\\d+(\\.\\d+)?$");
    }

    private static LinkedHashSet<String> createOriginalHeader () {
        LinkedHashSet<String> srcHeaderSet = new LinkedHashSet<>();
        srcHeaderSet.add("CustID");
        srcHeaderSet.add("ElecOrGas");
        srcHeaderSet.add("Disconnect Doc");
        srcHeaderSet.add("Move In Date");
        srcHeaderSet.add("Move Out Date");
        srcHeaderSet.add("Bill Year");
        srcHeaderSet.add("Bill Month");
        srcHeaderSet.add("Span Days");
        srcHeaderSet.add("Meter Read Date");
        srcHeaderSet.add("Meter Read Type");
        srcHeaderSet.add("Consumption");
        srcHeaderSet.add("Exception Code");
        return srcHeaderSet;
    }
}
