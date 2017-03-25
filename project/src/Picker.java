
import java.util.ArrayList;

/**
 * A picker in the system. The picker's job is to pick fascias after receiving the order.
 *
 */
public class Picker {

    /** The name of the Picker. */
    private String name;
    
    /** The status of the picker. True if the picker is ready to pick, false if busy. */
    private boolean ready;
    
    /** The inventory of the picker. */
    private ArrayList<Fascia> pickedFascias = new ArrayList<Fascia>();
    
    /** Helps the Picker find the appropriate order to work with. */
    public int groupIndex;
    
    /**
     * Initializes a new Picker.
     */
    public Picker(String name, int groupIndex) {
        ready = true;
        this.name = name;
        this.groupIndex = groupIndex;
    }

    /**
     * Checks to see if the Picker has picked the 8 fascia assigned.
     * 
     * @return true if the Picker has picked all the fascia; false otherwise.
     */
    public boolean isDone() {
        return pickedFascias.size() == 8;
    }

    public String getName() {
        return name;
    }
    
    public boolean isReady(){
        return ready;
    }

    public ArrayList<Fascia> getFascias() {
        return pickedFascias;
    }
    
    /**
     * Clears the fascia currently held by the Picker and makes them ready for the next order.
     */
    public void clearFascias() {
        ready = true;
        pickedFascias = new ArrayList<Fascia>();
    }

    /**
     * Picks the fascia at the given index of the orders.
     *  
     * @param orders the list of orders the Picker currently has to pick.
     * @param index the index of orders at which the fascia needed to be picked is located.
     * @param allFascias the list of all fascias currently in the system.
     */
    public void pickFascia(String sku, ArrayList<Fascia> allFascias) {
        ready = false;
        // we search for the fascia inside the order
        for (Fascia fascia : allFascias) {
            if (fascia.getSku().equals(sku)) {
                fascia.fasciaCount -= 1;
                if (fascia.fasciaCount <= 5) {
                    Replenisher replenisher = new Replenisher();
                    replenisher.replenish(fascia.getLocation(), allFascias);
                }
                pickedFascias.add(fascia);
            }
        }
    }
}