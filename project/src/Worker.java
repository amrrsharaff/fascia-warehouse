import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * A generic worker in the warehouse.
 */
public class Worker {

  /** The logger used to log events. */
  protected static final Logger logger = Logger.getLogger(Reader.class.getName());
  /** The name of the Sequencer. */
  protected String name;
  /** The fascias to be rescanned. */
  protected ArrayList<String> rescannedSKUs = new ArrayList<String>();

  /** Initialize a generic warehouse worker. */
  public Worker() {}

  /** Get the worker's name. */
  public String getName() {
    return name;
  }

  /** Rescan event. */
  public void rescan(String sku, ArrayList<Fascia> allFascia, Picker picker) {}

  /** Get the SKUs that the worker has rescanned. */
  public ArrayList<String> getRescannedSKUs() {
    return rescannedSKUs;
  }
  
  /** Set the SKUs that the worker has already rescanned. */
  public void setRescannedSKUs(ArrayList<String> rescannedSKUs) {
    this.rescannedSKUs = rescannedSKUs;
  }

}
