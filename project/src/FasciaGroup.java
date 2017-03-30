import java.util.ArrayList;

public class FasciaGroup {

  /** Checks to see if the group is sequenced. True if sequenced; false otherwise. */
  private boolean sequenced;

  /** Checks to see if the group is loaded. True if loaded; false otherwise. */
  private boolean loaded;

  /** The current fascias within the group. */
  private ArrayList<Fascia> fascias;

  /** The requestId of the group. It matches with the requestId of the same order. */
  private int requestId;

  /**
   * Initializes a new FasciaGroup.
   * 
   * @param fascias The fascias within the group.
   * @param requestId The request ID of the group. It's the same as the request ID of the same
   *        order.
   */
  public FasciaGroup(ArrayList<Fascia> fascias, int requestId) {
    this.fascias = fascias;
    this.requestId = requestId;
  }

  public int getRequestId() {
    return requestId;
  }

  public void setRequestId(int requestId) {
    this.requestId = requestId;
  }

  public boolean isSequenced() {
    return sequenced;
  }

  public void setSequenced(boolean sequenced) {
    this.sequenced = sequenced;
  }

  public boolean isLoaded() {
    return loaded;
  }

  public void setLoaded(boolean loaded) {
    this.loaded = loaded;
  }

  public ArrayList<Fascia> getFascias() {
    return fascias;
  }

  /**
   * Gets the colour and model number of the fascia at the given index.
   * 
   * @param index The index of the fascia whose colour and model is needed.
   */
  public String getOrder(int index) {
    return fascias.get(index).getColour() + " " + fascias.get(index).getModelNumber();
  }
}
