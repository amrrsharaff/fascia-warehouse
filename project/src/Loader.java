import java.util.ArrayList;

/**
 * A loader in the system. The loader's job is to load pallets onto the truck.
 */
public class Loader {

  String name;
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  /** The fascias to be sequenced. */
  private Order toBeLoaded;
    /**
     * Initializes a loader.
     */
    public Loader(String name) {
      this.name = name;
    }
    
    /**
     * Load the order onto the truck.
     * 
     * @param group a group of 8 fascias.
     */
    public void load() {
        // Loader replies saying the picking request is laoded.
        System.out.println("Loader " + this.name + ": Picking request with id " + this.getToBeLoaded().getRequestId() + " is loaded.");
    }
    
    /**
     * Rescans a fascia and finds it in the database using the sku number. 
     * 
     * @param sku
     * 		The SKU number of the fascia that was rescanned.
     * @param allFascia
     * 		All the fascia in the warehouse.
     * @return
     * 		The fascia whose SKU matches with the given SKU.
     */
    public Fascia rescan(String sku, ArrayList<Fascia> allFascia){
  	  // We know the if statement will be accessed because something not in the warehouse, cannot be scanned.
  	  Fascia correctFascia = null;
  	  for(Fascia fascia: allFascia){
  		  if(sku.equals(fascia.getSku())){
  			  correctFascia = fascia;
  		  }
  	  }  
  	  return correctFascia;
    }

    public Order getToBeLoaded() {
      return toBeLoaded;
    }

    public void setToBeLoaded(Order toBeLoaded) {
      this.toBeLoaded = toBeLoaded;
    }
}