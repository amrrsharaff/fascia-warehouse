
import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import org.junit.Test;

public class Testing {
  ArrayList<Fascia> fascias = new ArrayList<Fascia>();
  ArrayList<Fascia> invertedFascias = new ArrayList<Fascia>();

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
    assertEquals(sequencer.getRescannedSKUs().size(), 0);

    fascia1.setLocation("A000");
    for (int i = 0; i < invertedFascias.size(); i++) {
      invertedFascias.get(i).fasciaCount = 5;
    }
    Replenisher replenisher = new Replenisher();
    replenisher.replenish("A000", invertedFascias);
    assertEquals(30, fascia1.fasciaCount);
     
    sequencer.rescan("123", invertedFascias, picker);
    assertEquals(sequencer.getRescannedSKUs().size(), 1);
  }
    
  @Test
  public void testFascia(){
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
      System.out.println(fascias.get(0).fasciaCount);

      if (picker.isDone()){

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


    // loader.rescan(fascias.get(0).getSku(), fascias, picker);
  }
  
  @Test
  public void testReader(){
    Reader reader = new Reader();
    reader.readFascias(new File("../translation.csv"), fascias);
    
//    assertEquals(fascias.size(), 24);
//    System.out.println(fascias.size());
    
    String[] args = new String[3];
    String[] args2 = new String[0];
    Reader.main(args);
    Reader.main(args2);
    
  }

}
