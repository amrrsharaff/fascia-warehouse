import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * A generic worker in the warehouse.
 */
public abstract class Worker {

  /** The logger used to log events. */
  protected static final Logger logger = Logger.getLogger(Reader.class.getName());
  /** The name of the Sequencer. */
  protected String name;
  /** The fascias to be rescanned. */
  protected ArrayList<String> rescannedSKUs = new ArrayList<String>();
  
  /** Order to be processed. */
  protected Order toBeProcessed;

  /** Initialize a generic warehouse worker. */
  public Worker(String name) {
    this.name = name;
  }

  /** Get the worker's name. */
  public String getName() {
    return name;
  }

  /**
   * Rescans a fascia and finds it in the database using the sku number.
   * 
   * @param sku The SKU number of the fascia that was rescanned.
   * @param allFascia All the fascia in the warehouse.
   * @param the picker who repicks the wrong fascia.
   */
  public void rescan(String sku, ArrayList<Fascia> allFascia, Picker picker) {
    // We know the if statement will be accessed because something not in the warehouse, cannot be
    // scanned.
    int index = rescannedSKUs.size();
    String jobName = this.getClass().getSimpleName();
    ArrayList<Fascia> fascias = toBeProcessed.getOrderFascia();
    if (fascias.get(index).getSku().equals(sku)) {
      logger.info(jobName + " " + this.name + ": Fascia with SKU " + sku + " rescanned.");
      rescannedSKUs.add(sku);
    } else {
      logger.info(jobName + " " + this.name + ": Fascia with SKU " + sku + " rescanned.");
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

  /** Get the SKUs that the worker has rescanned. */
  public ArrayList<String> getRescannedSKUs() {
    return rescannedSKUs;
  }
  
  /** Set the SKUs that the worker has already rescanned. */
  public void setRescannedSKUs(ArrayList<String> rescannedSKUs) {
    this.rescannedSKUs = rescannedSKUs;
  }

  public Order getToBeProcessed() {
    return toBeProcessed;
  }

  public void setToBeProcessed(Order toBeProcessed) {
    this.toBeProcessed = toBeProcessed;
  }
}
