import java.util.ArrayList;

/**
 * A loader in the system. The loader's job is to load pallets onto the truck.
 */
public class Loader {

    /**
     * Initializes a loader.
     */
    public Loader() {
    }
    
    /**
     * Load the order onto the truck.
     * 
     * @param group a group of 8 fascias.
     */
    public void load(FasciaGroup group) {
        // Since we don't deal with truck, we just assign loaded to be true.
        group.setLoaded(true);
        for (int i = 0; i < 8; i += 2) {
            String order = group.getOrder(i);
            System.out.println("Order " + order + " loaded");

        }
        System.out.println("Picking request number " + group.getRequestId() + " was loaded");
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
}