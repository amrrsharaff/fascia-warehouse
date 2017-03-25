
import java.util.ArrayList;

/**
 * A sequencer.
 */
public class Sequencer {

  /** The name of the Sequencer. */
  private String name;
  /** The inventory of the sequencer. */
  private ArrayList<Fascia> sequencedFascias = new ArrayList<Fascia>();

  /**
   * Constructs a new Sequencer.
   */
  public Sequencer(String name) {
    this.name = name;
  }

  public ArrayList<Fascia> getFascias() {
    return sequencedFascias;
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
   * @return The fascia whose SKU matches with the given SKU.
   */
  public Fascia rescan(String sku, ArrayList<Fascia> allFascia) {
    // We know the if statement will be accessed because something not in the warehouse, cannot be
    // scanned.
    Fascia correctFascia = null;
    for (Fascia fascia : allFascia) {
      if (sku.equals(fascia.getSku())) {
        correctFascia = fascia;
      }
    }
    return correctFascia;
  }

  public String getName() {
    return name;
  }

}
