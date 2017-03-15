import java.util.ArrayList;

/**
 * A replenisher in the system. The replenisher's job is to replenish any given fascia or those
 * that are less than or equal to 5 in quantity.
 */
public class Replenisher {
    
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
                System.out.println("Fascia with location " + location + " was replenished");
            }
        }
    }
}