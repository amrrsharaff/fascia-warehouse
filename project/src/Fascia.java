/* A Fascia. */
public class Fascia {

  /** The model number of the Fascia. */
  private String modelNumber;

  /** The SKU number of the Fascia. */
  private String sku;

  /** The colour of the Fascia. */
  private String colour;

  /** The location of this Fascia. */
  private String location = new String();

  /** True if the fascia is a front fascia, false if it's a rear fascia. */
  private boolean front;

  /** The number of this Fascia currently available */
  public int fasciaCount;

  /**
   * Initializes a Fascia.
   * 
   * @param colour The colour of the fascia.
   * @param modelNumber The model number of the fascia.
   * @param sku The SKU number of the fascia.
   * @param front True if it is a front fascia, false if it's a rear fascia.
   */
  public Fascia(String colour, String modelNumber, String sku, boolean front) {
    this.colour = colour;
    this.modelNumber = modelNumber;
    this.sku = sku;
    this.setFront(front);
    this.fasciaCount = 30;
  }

  public String getModelNumber() {
    return modelNumber;
  }

  public String getSku() {
    return sku;
  }

  public String getColour() {
    return colour;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  /** Return whether the fascia is a front fascia or not. */
  public boolean isFront() {
    return front;
  }

  public void setFront(boolean front) {
    this.front = front;
  }
}
