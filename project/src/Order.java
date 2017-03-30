import java.util.ArrayList;

/**
 * An Order that contains 4 fascia request.
 */
public class Order {

  /** The request number of the Order before this one. */
  private static int requestNumb = 0;

  /** The request number of this Order */
  private int requestId;

  /** The model number and colour of 4 orders. */
  private ArrayList<ArrayList<String>> fourOrders = new ArrayList<>(4);

  /** The fascia required for the order. */
  private ArrayList<Fascia> orderFascia = new ArrayList<>(8);

  /** Initializes an order. */
  public Order(ArrayList<ArrayList<String>> fourOrders) {
    this.fourOrders = fourOrders;
    requestNumb = requestNumb + 1;
    requestId = requestNumb;
  }

  public int getRequestId() {
    return requestId;
  }

  /**
   * Find all the fascia that are in the order.
   * 
   * @param allFascia all the fascia in the system.
   */
  public void findFascia(ArrayList<Fascia> allFascia) {
    for (ArrayList<String> order : fourOrders) {
      for (Fascia fascia : allFascia) {
        // Add the front fascia.
        if (order.get(1).equals(fascia.getColour()) && order.get(0).equals(fascia.getModelNumber())
            && fascia.isFront()) {
          orderFascia.add(fascia);
        }

        // Add the rear fascia.
        if (order.get(1).equals(fascia.getColour()) && order.get(0).equals(fascia.getModelNumber())
            && fascia.isFront() == false) {
          orderFascia.add(fascia);
        }
      }
    }
  }

  public ArrayList<Fascia> getOrderFascia() {
    return orderFascia;
  }

  public ArrayList<ArrayList<String>> getFourOrders() {
    return fourOrders;
  }

}
