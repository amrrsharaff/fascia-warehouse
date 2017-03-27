import java.util.ArrayList;

/**
 * A sequencer.
 */
public class Sequencer {

  /** The name of the Sequencer. */
  private String name;
  /** The inventory of the sequencer. */
  private ArrayList<Fascia> sequencedFascias = new ArrayList<Fascia>();
  /** The fascias to be sequenced. */
  private Order toBeSequenced;
  /** The fascias to be rescanned. */
  private ArrayList<String> rescannedSKUs = new ArrayList<String>();
  public void setSequencedFascias(ArrayList<Fascia> sequencedFascias) {
    this.sequencedFascias = sequencedFascias;
  }

  private boolean correct;

  public ArrayList<String> getRescannedSKUs() {
    return rescannedSKUs;
  }

  public void setRescannedSKUs(ArrayList<String> rescannedSKUs) {
    this.rescannedSKUs = rescannedSKUs;
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
   * Compares the original order with the collected fascia to see if they match.
   * 
   * @param originalOrder the SKU numbers of the original 8 fascia requested within the order.
   * @param currentFascia the SKU numbers of the 8 fascia picked up by a picker.
   */
  public boolean compare(ArrayList<Fascia> originalOrder, ArrayList<Fascia> currentFascia) {
    System.out.println("Sequencer " + getName() + " sequences.");
    for (int i = 0; i < originalOrder.size(); i++) {
      if (!originalOrder.get(i).getSku().equals(currentFascia.get(i).getSku())) {
        System.out.println("Fascia with SKU " + originalOrder.get(i).getSku()
            + " was incorrectly replaced with Fascia with SKU " + currentFascia.get(i).getSku());
        return false;
      }
    }
    return true;
  }

  /**
   * Compares the original fascia with a collected fascia to see if they match.
   * 
   * @param originalItem The original fascia that was ordered
   * @param currentItem the fascia to be compared
   */
  public boolean compare(Fascia originalItem, Fascia currentItem) {
    System.out.println("Sequencer " + getName() + " sequences.");
    if (!originalItem.getSku().equals(currentItem.getSku())) {
      System.out.println("Fascia with SKU " + originalItem.getSku()
          + " was incorrectly replaced with Fascia with SKU " + currentItem.getSku());
      return false;
    } else {
      return true;
    }
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
    if(fascias.get(index).getSku().equals(sku)){
      System.out.println("Sequencer " + this.name + ": Fascia with SKU " + sku + " rescanned.");
      rescannedSKUs.add(sku);
    } else{
      System.out.println("Sequencer " + this.name + ": Fascia with SKU " + sku + " rescanned.");
      System.out.println("System: Fascias unmatched, there was an error in picking.");
      System.out.println("System: Fascia with SKU " + fascias.get(index).getSku() + " was incorrectly replaced by Fascia with SKU " + sku + ".");
      System.out.println("System: Picker " + picker.getName() + ", repick the Fascia with SKU " + fascias.get(index).getSku() + ".");
      System.out.println("Picker " + picker.getName() + ": Fascia with SKU " + fascias.get(index).getSku() + " repicked.");
      rescannedSKUs.add(fascias.get(index).getSku());
    }
  }

  
  public String getName() {
    return name;
  }

  public boolean isCorrect() {
    return correct;
  }

  public void setCorrect(boolean correct) {
    this.correct = correct;
  }

}
