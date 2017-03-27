import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Reader {

  /**
   * Transfer all the fascias from the file onto the system.
   * 
   * @param file the location of all the fascia.
   * @param fascias the list which will contain all the fascias.
   */
  public void readFascias(File file, ArrayList<Fascia> fascias) {
    try {
      Scanner scanner = new Scanner(file);
      // colour, model, sku, true(if front), false(if back)
      String firstLine = scanner.nextLine();
      while (scanner.hasNextLine()) {
        String newLine = scanner.nextLine();
        String[] parts = newLine.split(",");
        Fascia frontFascia = new Fascia(parts[0], parts[1], parts[2], true);
        Fascia backFascia = new Fascia(parts[0], parts[1], parts[3], false);
        fascias.add(frontFascia);
        fascias.add(backFascia);
      }
      scanner.close();
      System.out.println("All fascias available are recorded");
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }

  /**
   * Adds the location of each fascia into the system.
   * 
   * @param file contains the location of each fascia.
   * @param fascias all the fascias in the system.
   */
  public void setLocations(File file, ArrayList<Fascia> fascias) {
    try {
      Scanner scanner = new Scanner(file);
      while (scanner.hasNextLine()) {
        String newLine = scanner.nextLine();
        String[] parts = newLine.split(",");
        String location = parts[0] + parts[1] + parts[2] + parts[3];
        String sku = parts[4];
        for (Fascia fascia : fascias) {
          if (fascia.getSku() == sku) {
            fascia.setLocation(location);
          }
        }

      }
      scanner.close();
      System.out.println("All fascias' locations are now recorded");
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }

  /**
   * Sets the count for those fascias that are less than 30 in stock.
   * 
   * @param file contains the amount of those fascias that are less than 30 in stock.
   * @param fascias all the fascias currently in the system.
   */
  public void setCount(File file, ArrayList<Fascia> fascias) {
    try {
      Scanner scanner = new Scanner(file);
      while (scanner.hasNextLine()) {
        String newLine = scanner.nextLine();
        String[] parts = newLine.split(",");
        String location = parts[0] + parts[1] + parts[2] + parts[3];
        int amount = Integer.parseInt(parts[4]);
        for (Fascia fascia : fascias) {
          // If initial.csv has the amount for a fascia, have the
          // count to be that amount.
          if (fascia.getLocation() == location) {
            fascia.fasciaCount = amount;
          }
        }

      }
      scanner.close();
      System.out.println("The number of each type of fascia is now recorded");
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }

  /**
   * The sequencer checks the fascias to make sure if they are correct. If not, the fascia are then
   * corrected.
   * 
   * @param pickedFascias the 8 fascias picked by the picker.
   * @param groups all the orders currently in the system.
   * @param sequencer the sequencer to sequence the pickedFascias.
   * @param picker the picker who will pick the fascia in case there is a mistake.
   */
  public void requestSequencer(ArrayList<FasciaGroup> pickedFascias, ArrayList<Order> groups,
      Sequencer sequencer, Picker picker) {
    for (FasciaGroup group : pickedFascias) {
      if (!group.isSequenced()) {
        for (Order order : groups) {
          // if the order and the group of fascia have the same
          // request ID
          if (group.getRequestId() == order.getRequestId()) {
            // have the sequencer compare the original order with
            // the fascia picked by the picker
            boolean correct = sequencer.compare(order.getOrderFascia(), group.getFascias());
            if (!correct) {
              System.out.println("The order with ID " + order.getRequestId()
                  + " was not picked correctly. It will be picked again.");
              // remove the incorrect order
              int incorrectIndex = pickedFascias.indexOf(group);
              pickedFascias.remove(incorrectIndex);
              // add the correct order
              FasciaGroup correctFascia =
                  new FasciaGroup(order.getOrderFascia(), order.getRequestId());
              correctFascia.setSequenced(true);
              pickedFascias.add(incorrectIndex, correctFascia);
              System.out.println("Picker " + picker.getName() + " re-picked the correct fascias.");
            } else {
              System.out
                  .println("The order with ID " + order.getRequestId() + " was picked correctly.");
            }
            // break;
          }

        }
        group.setSequenced(true);
      }
      break;
    }
  }

  public static void main(String[] args) {
    String path;
    if (args.length != 0) { // use the first argument from the command line.
      path = args[0];
    } else { // follow the standard event file
      path = "../16orders.txt";
    }
    Reader reader = new Reader();
    int nextGroup = 0;
    ArrayList<Order> groups = new ArrayList<>();
    ArrayList<Fascia> fascias = new ArrayList<>();
    ArrayList<ArrayList<String>> orders = new ArrayList<>();
    ArrayList<Picker> pickers = new ArrayList<>();
    ArrayList<FasciaGroup> pickedFascias = new ArrayList<>();
    ArrayList<Sequencer> sequencers = new ArrayList<>();
    ArrayList<Loader> loaders = new ArrayList<>();

    File file1 = new File("../translation.csv");
    File file2 = new File("../traversal_table.csv");
    File file3 = new File("../initial.csv");
    File file4 = new File(path);

    // Makes all possible fascias in warehouse
    reader.readFascias(file1, fascias);

    // Sets location for all fascias
    reader.setLocations(file2, fascias);

    // sets amount of all Fascias
    reader.setCount(file3, fascias);

    try {
      Scanner scanner = new Scanner(file4);
      while (scanner.hasNextLine()) {
        String newLine = scanner.nextLine();
        String[] parts = newLine.split(" ");
        // If that's an order
        if (parts[0].equals("Order")) {
          // string of that order
          ArrayList<String> oneOrder = new ArrayList<>();
          oneOrder.add(parts[1]);
          oneOrder.add(parts[2]);
          orders.add(oneOrder);
          if (orders.size() == 4) {
            Order patch = new Order(orders);
            patch.findFascia(fascias);
            groups.add(patch);
            orders = new ArrayList<ArrayList<String>>();
            System.out.println("We received a set of 4 orders");
          }
          // if that's a pick
        } else if (parts[0].equals("Picker")) {
          if (parts[2].equals("ready")) { // declaring a ready picker
                                          // or setting an old picker
                                          // ready
            boolean found = false;
            // check to see if the picker already exists
            for (Picker oldPicker : pickers) {
              if (oldPicker.getName().equals(parts[1])) {
                found = true;
                oldPicker.clearFascias();
                // nextGroup is used to find the next order
                // within groups
                oldPicker.groupIndex = nextGroup;
                nextGroup++;
              }
            }
            if (!found) {
              Picker newPicker = new Picker(parts[1], nextGroup);
              System.out.println("Picker " + parts[1] + " is ready.");
              nextGroup++;
              pickers.add(newPicker);
            }
          } else if (parts[2].equals("pick")) { // an order to pick
            // look for picker
            Fascia toBePickedFascia = fascias.get(0);
            for (Picker oldPicker : pickers) {
              if (oldPicker.getName().equals(parts[1])) {
                // tell picker to pick fascia
                Order toBePicked = groups.get(oldPicker.groupIndex);
                toBePicked.findFascia(fascias);
                // pick fascia statement, actual picking happens after system confirmation.
                System.out.println("Fascia with SKU number " + parts[3] + " was picked by picker "
                    + oldPicker.getName());
                for (Fascia fascia : fascias) {
                  if (fascia.getSku().equals(parts[3])) {
                    toBePickedFascia = fascia;
                  }
                }
                int indexOfFascia = oldPicker.getFascias().size();
                if (toBePicked.getOrderFascia().get(indexOfFascia).getSku()
                    .equals(toBePickedFascia.getSku())) {
                  // picking happens here or else statement
                  oldPicker.pickFascia(parts[3], fascias);
                  System.out.println("System: Picked the correct fascia");
                } else { // The system says that the picker picked the wrong fascias
                  System.out.println("System: Picker " + parts[1] + " picked the wrong fascia.");
                  System.out.println("System: The correct fascia to pick has SKU: "
                      + toBePicked.getOrderFascia().get(indexOfFascia).getSku());
                  oldPicker.pickFascia(toBePicked.getOrderFascia().get(indexOfFascia).getSku(),
                      fascias);
                  System.out.println("System: Mistake has been fixed, Picker " + parts[1]
                      + " has picked the correct fascia.");

                }
                // if picker picked all 8 fascias
                if (oldPicker.isDone()) {
                  FasciaGroup pickedGroup =
                      new FasciaGroup(oldPicker.getFascias(), toBePicked.getRequestId());
                  pickedFascias.add(pickedGroup);
                  System.out
                      .println("All fascias in picking request number " + toBePicked.getRequestId()
                          + " were picked by picker " + oldPicker.getName());
                }
              }
            }
          }
        } else if (parts[0].equals("Replenisher")) { // if it's a
                                                     // Replenisher
          if (parts[2].equals("ready")) {
            System.out.println("Replenisher " + parts[1] + " is ready.");
          } else {
            // Replenish at the given location
            String location = parts[3] + parts[4] + parts[5] + parts[6];
            Replenisher replenisher = new Replenisher();
            replenisher.replenish(location, fascias);
            System.out.println(parts[1] + " replenished fascia at " + location);
          }
        } else if (parts[0].equals("Sequencer")) { // if it's a
                                                   // Sequencer
          Sequencer sequencer = new Sequencer("Default");
          if (parts[2].equals("ready")) {
            int groupNumber = 0;
            boolean found = false;
            // check to see if the sequencer already exists
            for (Sequencer oldSequencer : sequencers) {
              if (oldSequencer.getName().equals(parts[1])) {
                found = true;
                sequencer = oldSequencer;
              }
            }
            if (!found) {
              // if they don't exist, introduce a new sequencer
              sequencer = new Sequencer(parts[1]);
              sequencers.add(sequencer);
            }
            for (FasciaGroup tobeSequenced : pickedFascias) {
              if (tobeSequenced.isSequenced() == false) {
                groupNumber = tobeSequenced.getRequestId();
                tobeSequenced.setSequenced(true);
                break;
              }
            }
            for (Order order : groups) {
              if (order.getRequestId() == groupNumber) {
                Order orderSequenced = order;
                sequencer.setToBeSequenced(orderSequenced);
              }
            }
            sequencer.setSequencedFascias(new ArrayList<Fascia>());
            sequencer.setRescannedSKUs(new ArrayList<String>());
            sequencer.setCorrect(true);
            System.out.println("The group number assigned is "+ sequencer.getToBeSequenced().getRequestId());
            System.out.println("Sequencer " + parts[1] + " is ready");

          } else if (parts[2].equals("sequences")) {
            int index = 0;
            for (Sequencer oldSequencer : sequencers) {
              if (oldSequencer.getName().equals(parts[1])) {
                sequencer = oldSequencer;
              }
            }
            Fascia fasciaSeq =
                sequencer.getToBeSequenced().getOrderFascia().get(sequencer.getFascias().size());
            if (parts[3].equals(fasciaSeq.getSku())) {
              sequencer.getFascias().add(fasciaSeq);
              System.out.println("Sequencer " + sequencer.getName() + " sequenced fascia with sku " + fasciaSeq.getSku());
            } else {
              sequencer.getFascias().add(fasciaSeq);
              sequencer.setCorrect(false);
            }
            if (sequencer.getFascias().size() == 8) {
              if (!sequencer.isCorrect()) {
                System.out.println(
                    "System: Orders with request ID " + sequencer.getToBeSequenced().getRequestId()
                        + " were found to have an incorrect fascia received by " + "sequencer "
                        + sequencer.getName() + ".");
                System.out.println("System: This set of fascias are thrown away.");
                for (FasciaGroup group : pickedFascias) {
                  if (group.getRequestId() == sequencer.getToBeSequenced().getRequestId()) {
                    index = pickedFascias.indexOf(group);
                    pickedFascias.remove(group);
                    FasciaGroup repickedGroup =
                        new FasciaGroup(sequencer.getToBeSequenced().getOrderFascia(),
                            sequencer.getToBeSequenced().getRequestId());
                    pickedFascias.add(index, repickedGroup);
                    sequencer.setSequencedFascias(sequencer.getToBeSequenced().getOrderFascia());
                    System.out.println("System: Picker " + pickers.get(0).getName()
                        + " repick Orders with request ID "
                        + sequencer.getToBeSequenced().getRequestId());
                    System.out.println("Picker " + pickers.get(0).getName()
                        + ": Orders with request ID " + sequencer.getToBeSequenced().getRequestId()
                        + " is now repicked correctly.");
                    break;
                  }
                }
              } else {
                System.out.println("System: Orders with request ID "
                    + sequencer.getToBeSequenced().getRequestId() + " are sequenced.");
              }
            }
          } else if (parts[2].equals("rescans")) {
            for (Sequencer oldSequencer : sequencers) {
              if (oldSequencer.getName().equals(parts[1])) {
                sequencer = oldSequencer;
              }
            }
            sequencer.rescan(parts[3], fascias, pickers.get(0));
          }

        } else if (parts[0].equals("Loader")) { // load if it's a loader
          Loader loader = new Loader("Default");
          if (parts[2].equals("ready")) {
            int groupNumber = 0;
            boolean found = false;
            // check to see if the sequencer already exists
            for (Loader oldLoader : loaders) {
              if (oldLoader.getName().equals(parts[1])) {
                found = true;
                loader = oldLoader;
              }
            }
            if (!found) {
              // if they don't exist, introduce a new sequencer
              loader = new Loader(parts[1]);
              loaders.add(loader);
            }
            for (FasciaGroup tobeLoaded : pickedFascias) {
              if (tobeLoaded.isLoaded() == false) {
                groupNumber = tobeLoaded.getRequestId();
                tobeLoaded.setLoaded(true);
                break;
              }
            }
            for (Order order : groups) {
              if (order.getRequestId() == groupNumber) {
                Order orderLoaded = order;
                loader.setToBeLoaded(orderLoaded);
              }
            }
            loader.setRescannedSKUs(new ArrayList<String>());
            System.out.println("Loader " + parts[1] + " is ready");

          } else if (parts[2].equals("loads")) {
            for (Loader oldLoader : loaders) {
              if (oldLoader.getName().equals(parts[1])) {
                loader = oldLoader;
              }
            }
            System.out.println(
                "System: Loader " + loader.getName() + ", load the picking request with id "
                    + loader.getToBeLoaded().getRequestId() + ".");
            loader.load();
          } else if (parts[2].equals("scans")) {
            System.out.println("System: Loader " + loader.getName() + ", scan the fascia with SKU "
                + parts[3] + ".");
            loader.rescan(parts[3], fascias, pickers.get(0));
          } else if (parts[2].equals("rescans")) {
            System.out.println("System: Loader " + loader.getName()
                + ", rescan the fascia with SKU " + parts[3] + ".");
            loader.rescan(parts[3], fascias, pickers.get(0));
          }
        }
      }
      scanner.close();
      System.out.println("All events are done.");
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }
}
