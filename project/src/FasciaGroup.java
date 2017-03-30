
import java.util.ArrayList;

public class FasciaGroup {

  /** Checks to see if the group is sequenced. True if sequenced; false otherwise. */
  private boolean sequenced;

  /** Checks to see if the group is loaded. True if loaded; false otherwise. */
  private boolean loaded;

  /** The fascias current within the group. */
  private ArrayList<Fascia> fascias;

  /** The requestId of the group. It matches with the requestId of the same order. */
  private int requestId;

  /**
   * Initializes a new FasciaGroup.
   * 
   * @param fascias
   * @param requestId
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
   * @param index
   */
  public String getOrder(int index) {
    return fascias.get(index).getColour() + " " + fascias.get(index).getModelNumber();
  }
}
