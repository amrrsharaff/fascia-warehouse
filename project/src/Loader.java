import java.util.ArrayList;

/**
 * A loader in the system. The loader's job is to load pallets onto the truck.
 */
public class Loader extends Worker {

  /** The order to be loaded */
  private Order toBeLoaded;

//   public void setName(String name) {
//   this.name = name;
//   }


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
    logger.info("Loader " + this.name + ": Picking request with id "
        + this.getToBeLoaded().getRequestId() + " is loaded.");
  }

  /**
   * @param picker The picker who would repick the misplaced fascia had there been any mistake in
   *        scanning.
   * @param sku The SKU number of the fascia that is to be scanned
   * @param allFascia All the fascia in the warehouse.
   */
  public void rescan(String sku, ArrayList<Fascia> allFascia, Picker picker) {
    int index = rescannedSKUs.size();
    ArrayList<Fascia> fascias = toBeLoaded.getOrderFascia();
    if (fascias.get(index).getSku().equals(sku)) {
      logger.info("Sequencer " + this.name + ": Fascia with SKU " + sku + " scanned.");
      rescannedSKUs.add(sku);
    } else {
      logger.info("Loader " + this.name + ": Fascia with SKU " + sku + " scanned.");
      logger.warning("System: Fascias unmatched, there was an error in picking.");
      logger.warning("System: Fascia with SKU " + fascias.get(index).getSku()
          + " was incorrectly replaced by Fascia with SKU " + sku + ".");
      logger.warning("System: Picker " + picker.getName() + ", repick the Fascia with SKU "
          + fascias.get(index).getSku() + ".");
      logger.info("Picker " + picker.getName() + ": Fascia with SKU " + fascias.get(index).getSku()
          + " repicked.");
      rescannedSKUs.add(fascias.get(index).getSku());
    }
  }

  public Order getToBeLoaded() {
    return toBeLoaded;
  }

  public void setToBeLoaded(Order toBeLoaded) {
    this.toBeLoaded = toBeLoaded;
  }

}
