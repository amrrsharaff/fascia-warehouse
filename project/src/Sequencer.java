
import java.util.ArrayList;

/**
 * A sequencer.
 */
public class Sequencer {
    
    /** The name of the Sequencer. */
    private String name;
    
    /** 
     * Constructs a new Sequencer.
     */
    public Sequencer(String name){
        this.name = name;
    }
    
    /** 
     * Compares the original order with the collected fascia to see if they match.
     * 
     * @param originalOrder the SKU numbers of the original 8 fascia requested within the order.
     * @param currentFascia the SKU numbers of the 8 fascia picked up by a picker.
     */
    public boolean compare(ArrayList<Fascia> originalOrder, ArrayList<Fascia> currentFascia){
      System.out.println("Sequencer " + getName() + " sequences.");
        for (int i = 0; i < originalOrder.size(); i++){
            if(!originalOrder.get(i).getSku().equals(currentFascia.get(i).getSku())){ 
              System.out.println("Fascia with SKU "+ originalOrder.get(i).getSku() + " was incorrectly replaced with Fascia with SKU " + currentFascia.get(i).getSku());
                return false;
            }
        }
        return true;    
    }
    

    public String getName() {
        return name;
    }
    
    
}