import java.util.ArrayList;

/**
 * A loader in the system. The loader's job is to load pallets onto the truck.
 */
public class Loader {

  private String name;
  
  public String getName() {
    return name;
  }

//  public void setName(String name) {
//    this.name = name;
//  }

  /** The order to be loaded */
  private Order toBeLoaded;
  /** The skus to be scanned/rescanned */
  private ArrayList<String> rescannedSKUs;
    /**
     * Initializes a loader.
     */
    public Loader(String name) {
      this.name = name;
    }
    
//    /**
//     * Load the order onto the truck.
//     * 
//     * @param group a group of 8 fascias.
//     */
//    public void load() {
//        // Loader replies saying the picking request is laoded.
//        System.out.println("Loader " + this.name + ": Picking request with id " + this.getToBeLoaded().getRequestId() + " is loaded.");
//    }
    
    /** 
     * @param picker
     *      The picker who would repick the misplaced fascia had there been any mistake in scanning.
     * @param sku
     * 		The SKU number of the fascia that is to be scanned
     * @param allFascia
     * 		All the fascia in the warehouse.
     */
    public void rescan(String sku, ArrayList<Fascia> allFascia, Picker picker){
      int index = rescannedSKUs.size();
      ArrayList<Fascia> fascias = toBeLoaded.getOrderFascia();
      if(fascias.get(index).getSku().equals(sku)){
        System.out.println("Sequencer " + this.name + ": Fascia with SKU " + sku + " scanned.");
        rescannedSKUs.add(sku);
      } else{
        System.out.println("Loader " + this.name + ": Fascia with SKU " + sku + " scanned.");
        System.out.println("System: Fascias unmatched, there was an error in picking.");
        System.out.println("System: Fascia with SKU " + fascias.get(index).getSku() + " was incorrectly replaced by Fascia with SKU " + sku + ".");
        System.out.println("System: Picker " + picker.getName() + ", repick the Fascia with SKU " + fascias.get(index).getSku() + ".");
        System.out.println("Picker " + picker.getName() + ": Fascia with SKU " + fascias.get(index).getSku() + " repicked.");
        rescannedSKUs.add(fascias.get(index).getSku());
      }
    }

    public Order getToBeLoaded() {
      return toBeLoaded;
    }

    public void setToBeLoaded(Order toBeLoaded) {
      this.toBeLoaded = toBeLoaded;
    }

    public ArrayList<String> getRescannedSKUs() {
      return rescannedSKUs;
    }

    public void setRescannedSKUs(ArrayList<String> rescannedSKUs) {
      this.rescannedSKUs = rescannedSKUs;
    }

}