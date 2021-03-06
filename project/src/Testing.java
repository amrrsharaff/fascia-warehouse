import static org.junit.Assert.*;
import java.io.File;
import java.util.ArrayList;
import java.util.logging.Logger;
import org.junit.Test;

public class Testing {
  ArrayList<Fascia> fascias = new ArrayList<Fascia>();
  ArrayList<Fascia> invertedFascias = new ArrayList<Fascia>();
  static final Logger logger = Logger.getLogger(Reader.class.getName());

  @Test
  public void testSequencerAndReplenisher() {
    String name = "Ashley";
    Sequencer sequencer = new Sequencer("Ashley");
    Picker picker = new Picker("Lindsey", 0);
    assertEquals(sequencer.getName(), name);
    Fascia fascia1 = new Fascia("black", "SES", "123", true);
    Fascia fascia2 = new Fascia("black", "SES", "124", false);
    Fascia fascia3 = new Fascia("black", "SES", "125", true);
    Fascia fascia4 = new Fascia("black", "SES", "126", false);
    Fascia fascia5 = new Fascia("black", "SES", "127", true);
    Fascia fascia6 = new Fascia("black", "SES", "128", false);
    Fascia fascia7 = new Fascia("black", "SES", "129", true);
    Fascia fascia8 = new Fascia("black", "SES", "130", false);
    fascias.add(fascia8);
    fascias.add(fascia7);
    fascias.add(fascia6);
    fascias.add(fascia5);
    fascias.add(fascia4);
    fascias.add(fascia3);
    fascias.add(fascia2);
    fascias.add(fascia1);
    invertedFascias.add(fascia1);
    invertedFascias.add(fascia2);
    invertedFascias.add(fascia3);
    invertedFascias.add(fascia4);
    invertedFascias.add(fascia5);
    invertedFascias.add(fascia6);
    invertedFascias.add(fascia7);
    invertedFascias.add(fascia8);

    assertEquals(sequencer.isCorrect(), true);
    assertEquals(sequencer.getRescannedSkus().size(), 0);

    fascia1.setLocation("A000");
    for (int i = 0; i < invertedFascias.size(); i++) {
      invertedFascias.get(i).setFasciaCount(5);
    }
    Replenisher replenisher = new Replenisher();
    replenisher.replenish("A000", invertedFascias);
    assertEquals(30, fascia1.getFasciaCount());

    assertEquals(sequencer.getSequencedFascias().size(), 0);
    sequencer.setSequencedFascias(fascias);
    assertEquals(sequencer.getSequencedFascias(), fascias);
  }

  @Test
  public void testFascia() {
    Fascia fascia = new Fascia("black", "SES", "123", true);
    assertEquals("black", fascia.getColour());
    assertEquals("SES", fascia.getModelNumber());
    assertEquals("123", fascia.getSku());
    assertTrue(fascia.isFront());
    fascia.setLocation("A000");
    assertEquals(fascia.getLocation(), "A000");
  }

  @Test
  public void testFasciaGroup() {
    Fascia fascia1 = new Fascia("Black", "SES", "123", true);
    Fascia fascia2 = new Fascia("Black", "SES", "124", false);
    Fascia fascia3 = new Fascia("Black", "SES", "125", true);
    Fascia fascia4 = new Fascia("Black", "SES", "126", false);
    Fascia fascia5 = new Fascia("Black", "SES", "127", true);
    Fascia fascia6 = new Fascia("Black", "SES", "128", false);
    Fascia fascia7 = new Fascia("Black", "SES", "129", true);
    Fascia fascia8 = new Fascia("Black", "SES", "130", false);
    invertedFascias.add(fascia1);
    invertedFascias.add(fascia2);
    invertedFascias.add(fascia3);
    invertedFascias.add(fascia4);
    invertedFascias.add(fascia5);
    invertedFascias.add(fascia6);
    invertedFascias.add(fascia7);
    invertedFascias.add(fascia8);
    FasciaGroup fasciaGroup = new FasciaGroup(invertedFascias, 1);
    assertEquals("Black SES", fasciaGroup.getOrder(1));
    assertEquals(fasciaGroup.getFascias(), invertedFascias);
    assertEquals(fasciaGroup.getRequestId(), 1);
    assertFalse(fasciaGroup.isSequenced());
    assertFalse(fasciaGroup.isLoaded());
    fasciaGroup.setSequenced(true);
    fasciaGroup.setLoaded(false);
    fasciaGroup.setRequestId(2);
    assertEquals(fasciaGroup.getRequestId(), 2);
    fasciaGroup.setLoaded(true);
    assertEquals(fasciaGroup.isLoaded(), true);
  }

  @Test
  public void testOrder() {
    ArrayList<String> order1 = new ArrayList<>();
    ArrayList<String> order2 = new ArrayList<>();
    ArrayList<String> order3 = new ArrayList<>();
    ArrayList<String> order4 = new ArrayList<>();
    order1.add("SES");
    order2.add("SS");
    order3.add("SE");
    order4.add("S");
    order1.add("Black");
    order2.add("Black");
    order3.add("Black");
    order4.add("Black");
    ArrayList<ArrayList<String>> orders = new ArrayList<>();
    orders.add(order1);
    orders.add(order2);
    orders.add(order3);
    orders.add(order4);
    Order order = new Order(orders);
    assertEquals(order.getFourOrders(), orders);
    assertEquals(order.getRequestId(), 1);
    Fascia fascia1 = new Fascia("Black", "SES", "123", true);
    Fascia fascia2 = new Fascia("Black", "SES", "124", false);
    Fascia fascia3 = new Fascia("Black", "SS", "125", true);
    Fascia fascia4 = new Fascia("Black", "SS", "126", false);
    Fascia fascia5 = new Fascia("Black", "SE", "127", true);
    Fascia fascia6 = new Fascia("Black", "SE", "128", false);
    Fascia fascia7 = new Fascia("Black", "S", "129", true);
    Fascia fascia8 = new Fascia("Black", "S", "130", false);
    invertedFascias.add(fascia1);
    invertedFascias.add(fascia2);
    invertedFascias.add(fascia3);
    invertedFascias.add(fascia4);
    invertedFascias.add(fascia5);
    invertedFascias.add(fascia6);
    invertedFascias.add(fascia7);
    invertedFascias.add(fascia8);
    order.findFascia(invertedFascias);
    assertEquals(order.getOrderFascia(), invertedFascias);
  }

  @Test
  public void testPicker() {
    Picker picker = new Picker("Peter", 1);
    Fascia fascia1 = new Fascia("Black", "SES", "123", true);
    Fascia fascia2 = new Fascia("Black", "SES", "124", false);
    Fascia fascia3 = new Fascia("Black", "SES", "125", true);
    Fascia fascia4 = new Fascia("Black", "SES", "126", false);
    Fascia fascia5 = new Fascia("Black", "SES", "127", true);
    Fascia fascia6 = new Fascia("Black", "SES", "128", false);
    Fascia fascia7 = new Fascia("Black", "SES", "129", true);
    Fascia fascia8 = new Fascia("Black", "SES", "130", false);
    fascias.add(fascia8);
    fascias.add(fascia7);
    fascias.add(fascia6);
    fascias.add(fascia5);
    fascias.add(fascia4);
    fascias.add(fascia3);
    fascias.add(fascia2);
    fascias.add(fascia1);
    assertEquals(picker.getName(), "Peter");
    assertEquals(picker.isReady(), true);
    assertEquals(picker.isDone(), false);
    assertEquals(picker.getFascias(), new ArrayList<Fascia>());
    picker.pickFascia("123", fascias);
    picker.pickFascia("124", fascias);
    picker.pickFascia("125", fascias);
    picker.pickFascia("126", fascias);
    picker.pickFascia("127", fascias);
    picker.pickFascia("128", fascias);
    picker.pickFascia("129", fascias);
    picker.pickFascia("130", fascias);

    assertEquals(picker.isDone(), true);
    picker.clearFascias();
    for (int i = 0; i < 25; i++) {
      picker.pickFascia(fascias.get(0).getSku(), fascias);
      if (picker.isDone()) {

        picker.clearFascias();
      }
    }
    picker.pickFascia("130", fascias);
  }

  @Test
  public void testLoader() {
    Loader loader = new Loader("Larry");
    assertEquals(loader.getName(), "Larry");
    Picker picker = new Picker("Peter", 1);
    Fascia fascia1 = new Fascia("Black", "SES", "123", true);
    Fascia fascia2 = new Fascia("Black", "SES", "124", false);
    Fascia fascia3 = new Fascia("Black", "SES", "125", true);
    Fascia fascia4 = new Fascia("Black", "SES", "126", false);
    Fascia fascia5 = new Fascia("Black", "SES", "127", true);
    Fascia fascia6 = new Fascia("Black", "SES", "128", false);
    Fascia fascia7 = new Fascia("Black", "SES", "129", true);
    Fascia fascia8 = new Fascia("Black", "SES", "130", false);
    fascias.add(fascia8);
    fascias.add(fascia7);
    fascias.add(fascia6);
    fascias.add(fascia5);
    fascias.add(fascia4);
    fascias.add(fascia3);
    fascias.add(fascia2);
    fascias.add(fascia1);

    ArrayList<String> order1 = new ArrayList<>();
    ArrayList<String> order2 = new ArrayList<>();
    ArrayList<String> order3 = new ArrayList<>();
    ArrayList<String> order4 = new ArrayList<>();
    order1.add("SES");
    order2.add("SS");
    order3.add("SE");
    order4.add("S");
    order1.add("Black");
    order2.add("Black");
    order3.add("Black");
    order4.add("Black");
    ArrayList<ArrayList<String>> orders = new ArrayList<>();
    orders.add(order1);
    orders.add(order2);
    orders.add(order3);
    orders.add(order4);

    Order loadOrder = new Order(orders);
    loader.setToBeProcessed(loadOrder);
    assertEquals(loader.getToBeProcessed(), loadOrder);
  }

  @Test
  public void testWorker() {
    ArrayList<String> skus = new ArrayList<>();
    skus.add("1");
    skus.add("2");
    skus.add("3");
    skus.add("4");
    skus.add("5");
    skus.add("6");
    skus.add("7");
    skus.add("8");
    Sequencer sequencer = new Sequencer("Amr");
    sequencer.setRescannedSkus(skus);
    assertEquals(sequencer.getRescannedSkus().get(0), "1");
    Fascia fascia1 = new Fascia("Black", "SES", "123", true);
    Fascia fascia2 = new Fascia("Black", "SES", "124", false);
    Fascia fascia3 = new Fascia("Black", "S", "125", true);
    Fascia fascia4 = new Fascia("Black", "S", "126", false);
    Fascia fascia5 = new Fascia("Black", "SS", "127", true);
    Fascia fascia6 = new Fascia("Black", "SS", "128", false);
    Fascia fascia7 = new Fascia("Black", "SE", "129", true);
    Fascia fascia8 = new Fascia("Black", "SE", "130", false);
    fascias.add(fascia1);
    fascias.add(fascia2);
    fascias.add(fascia3);
    fascias.add(fascia4);
    fascias.add(fascia5);
    fascias.add(fascia6);
    fascias.add(fascia7);
    fascias.add(fascia8);
    Picker picker = new Picker("Default", 1);
    sequencer.setRescannedSkus(new ArrayList<String>());
    ArrayList<String> order1 = new ArrayList<>();
    ArrayList<String> order2 = new ArrayList<>();
    ArrayList<String> order3 = new ArrayList<>();
    ArrayList<String> order4 = new ArrayList<>();
    order1.add("SES");
    order2.add("S");
    order3.add("SS");
    order4.add("SE");
    order1.add("Black");
    order2.add("Black");
    order3.add("Black");
    order4.add("Black");
    ArrayList<ArrayList<String>> orders = new ArrayList<>();
    orders.add(order1);
    orders.add(order2);
    orders.add(order3);
    orders.add(order4);
    Order order = new Order(orders);
    sequencer.setToBeProcessed(order);
    sequencer.getToBeProcessed().findFascia(fascias);
    sequencer.setRescannedSkus(new ArrayList<String>());
    sequencer.rescan("1", picker);
    assertEquals(sequencer.getRescannedSkus().get(0), "123");
    sequencer.rescan("124", picker);
    assertEquals(sequencer.getRescannedSkus().get(1), "124");
    FasciaGroup fasciaGroup = new FasciaGroup(fascias, 1);
    FasciaGroup fasciaGroup2 = new FasciaGroup(fascias, 7);
    ArrayList<FasciaGroup> groups = new ArrayList<>();
    groups.add(fasciaGroup);
    groups.add(fasciaGroup2);
    Reader reader = new Reader();
    reader.fixError(sequencer, groups, new Picker("James", 7));
    assertFalse(fasciaGroup2.equals(groups.get(1)));
    assertEquals(groups.get(1).getFascias(), sequencer.getToBeProcessed().getOrderFascia());
  }
  @Test
  public void testReader(){
    Reader reader = new Reader();
    File file1 = new File("../translation.csv");
    File file2 = new File("../traversal_table.csv");
    File file3 = new File("../initial.csv");
    reader.readFascias(file1, fascias);
    Fascia fascia = new Fascia("White", "S", "1", true);
    fascia.setFasciaCount(12);
    fascias.add(fascia);
    assertEquals(fascia.getColour(), fascias.get(0).getColour());
    assertEquals(fascia.getModelNumber(), fascias.get(0).getModelNumber());
    assertEquals(fascia.getSku(), fascias.get(0).getSku());
    reader.setLocations(file2, fascias);
    assertEquals(fascias.get(0).getLocation(), "A000");
    reader.setCount(file3, fascias);
    assertEquals(fascias.get(0).getFasciaCount(), 30);
  }
  @Test
  public void testMain(){
    //Since main is not specifically one part and is composed of logging and printing
    //we test it by running it and checking whether the print statement with the skus are the desired ones
    String[] args = {};
    Reader.main(args);
  }
}