import java.util.ArrayList;
import java.util.logging.Logger;

/** A generic worker in the warehouse. */
public abstract class Worker {

  /** The logger used to log events. */
  protected static final Logger logger = Logger.getLogger(Reader.class.getName());

  /** The name of the Sequencer. */
  private String name;

  /** The fascias to be rescanned. */
  private ArrayList<String> rescannedSkus = new ArrayList<String>();

  /** Order to be processed. */
  private Order toBeProcessed;

  /**
   * Initialize a generic warehouse worker.
   * 
   * @param name The name of the worker.
   */
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
   * @param picker The picker who repicks the wrong fascia.
   */
  public void rescan(String sku, Picker picker) {
    // We know the if statement will be accessed because something not in the warehouse, cannot be
    // scanned.
    int index = rescannedSkus.size();
    String jobName = this.getClass().getSimpleName();
    ArrayList<Fascia> fascias = toBeProcessed.getOrderFascia();
    if (fascias.get(index).getSku().equals(sku)) {
      logger.info(jobName + " " + this.name + ": Fascia with SKU " + sku + " rescanned.");
      rescannedSkus.add(sku);
    } else {
      logger.info(jobName + " " + this.name + ": Fascia with SKU " + sku + " rescanned.");
      logger.warning("System: Fascias unmatched, there was an error in picking.");
      logger.warning("System: Fascia with SKU " + fascias.get(index).getSku()
          + " was incorrectly replaced by Fascia with SKU " + sku + ".");
      logger.warning("System: Picker " + picker.getName() + ", repick the Fascia with SKU "
          + fascias.get(index).getSku() + ".");
      logger.info("Picker " + picker.getName() + ": Fascia with SKU " + fascias.get(index).getSku()
          + " repicked.");
      rescannedSkus.add(fascias.get(index).getSku());
    }
  }

  /** Get the SKUs that the worker has rescanned. */
  public ArrayList<String> getRescannedSkus() {
    return rescannedSkus;
  }

  /**
   * Set the SKUs that the worker has already rescanned.
   * 
   * @param rescannedSkus An arraylist of the rescanned SKUs.
   */
  public void setRescannedSkus(ArrayList<String> rescannedSkus) {
    this.rescannedSkus = rescannedSkus;
  }

  public Order getToBeProcessed() {
    return toBeProcessed;
  }

  public void setToBeProcessed(Order toBeProcessed) {
    this.toBeProcessed = toBeProcessed;
  }
}
