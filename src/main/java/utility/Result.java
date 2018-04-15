package utility;

import java.util.Map;

/**
 * Created by Behnaz.
 */
public class Result<T> {
    private Map <String,T> mapElc;
    private Map <String,T> mapGas;

    public Result(Map<String,T > mapElc, Map<String,T> mapGas) {
        this.mapElc = mapElc;
        this.mapGas = mapGas;
    }

    public Map<String, T> getMapElc() {
        return mapElc;
    }

    public Map<String, T> getMapGas() {
        return mapGas;
    }

    public boolean isResultEmpty() {
        return (mapElc.isEmpty() && mapGas.isEmpty());
    }
}
