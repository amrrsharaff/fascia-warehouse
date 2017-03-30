import java.util.ArrayList;

/**
 * A sequencer.
 */
public class Sequencer extends Worker {

  /** The inventory of the sequencer. */
  private ArrayList<Fascia> sequencedFascias = new ArrayList<Fascia>();
  /** The fascias to be sequenced. */
  private Order toBeSequenced;
  private boolean correct;

  public void setSequencedFascias(ArrayList<Fascia> sequencedFascias) {
    this.sequencedFascias = sequencedFascias;
  }

  public ArrayList<Fascia> getSequencedFascias() {
    return sequencedFascias;
  }

  /**
   * Constructs a new Sequencer.
   */
  public Sequencer(String name) {
    this.name = name;
    setCorrect(true);
  }

  public void setToBeSequenced(Order toBeSequenced) {
    this.toBeSequenced = toBeSequenced;
  }

  public ArrayList<Fascia> getFascias() {
    return sequencedFascias;
  }

  public Order getToBeSequenced() {
    return toBeSequenced;
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
    ArrayList<Fascia> fascias = toBeSequenced.getOrderFascia();
    if (fascias.get(index).getSku().equals(sku)) {
      logger.info("Sequencer " + this.name + ": Fascia with SKU " + sku + " rescanned.");
      rescannedSKUs.add(sku);
    } else {
      logger.info("Sequencer " + this.name + ": Fascia with SKU " + sku + " rescanned.");
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

  public boolean isCorrect() {
    return correct;
  }

  public void setCorrect(boolean correct) {
    this.correct = correct;
  }
}
