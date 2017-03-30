/**
 * A loader in the system. The loader's job is to load pallets onto the truck.
 */
public class Loader extends Worker {

  /**
   * Initializes a loader.
   */
  public Loader(String name) {
    super(name);
  }

  /** Load the order onto the truck. */
  public void load() {
    // Loader replies saying the picking request is laoded.
    logger.info("Loader " + this.name + ": Picking request with id "
        + this.getToBeProcessed().getRequestId() + " is loaded.");
  }
}
