import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * A replenisher in the system. The replenisher's job is to replenish any given fascia or those
 * that are less than or equal to 5 in quantity.
 */
public class Replenisher {
  
  /** The logger used to log events. */
  private static final Logger logger = Logger.getLogger(Reader.class.getName());

    /**
     * Initializes a replenisher.
     */
    public Replenisher(){
    }
    
    /**
     * Replenishes the number of a type of Fascia.
     * 
     * @param location the location of the fascia.
     * @param allFascia all the fascia in the system.
     */
    public void replenish(String location, ArrayList<Fascia> allFascia) {
        for (Fascia fascia : allFascia) {
            if (location == fascia.getLocation()) {
                fascia.fasciaCount = 30;
                logger.info("Fascia with location " + location + " was replenished");
            }
        }
    }
}