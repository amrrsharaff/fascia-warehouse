import java.util.ArrayList;

/**
 * A sequencer.
 */
public class Sequencer extends Worker {

  /** The inventory of the sequencer. */
  private ArrayList<Fascia> sequencedFascias = new ArrayList<Fascia>();

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
    super(name);
    setCorrect(true);
  }

//  public void setToBeProcessed(Order toBeSequenced) {
//    this.toBeSequenced = toBeSequenced;
//  }

  public ArrayList<Fascia> getFascias() {
    return sequencedFascias;
  }

//  public Order getToBeProcessed() {
//    return toBeSequenced;
//  }

 

  public boolean isCorrect() {
    return correct;
  }

  public void setCorrect(boolean correct) {
    this.correct = correct;
  }


}
