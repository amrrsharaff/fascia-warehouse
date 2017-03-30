import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Reader {

  /** The logger used to log events. */
  private static final Logger logger = Logger.getLogger(Reader.class.getName());

  /**
   * Sets up the logger to log events and messages in the system.
   * 
   * @throws IOException
   *    throws ioexception.
   */
  private static void setupLogger() throws IOException {
    // gets rid of any handling that the root Logger has in order to avoid duplicate console
    // printing
    LogManager.getLogManager().reset();
    logger.setLevel(Level.ALL);

    // creates a ConsoleHandler for the logger to use
    ConsoleHandler consoleHandler = new ConsoleHandler();
    consoleHandler.setLevel(Level.ALL);
    logger.addHandler(consoleHandler);

    // creates a FileHandler for the logger to use
    FileHandler fileHandler = new FileHandler("log.txt");
    fileHandler.setLevel(Level.ALL);
    fileHandler.setFormatter(new SimpleFormatter());
    logger.addHandler(fileHandler);
  }

  /**
   * Transfer all the fascias from the file onto the system.
   * 
   * @param file The location of all the fascia.
   * @param fascias The list which will contain all the fascias.
   */
  public void readFascias(File file, ArrayList<Fascia> fascias) {
    try {
      Scanner scanner = new Scanner(file);
      // colour, model, sku, true(if front), false(if back)
      scanner.nextLine();
      while (scanner.hasNextLine()) {
        String newLine = scanner.nextLine();
        String[] parts = newLine.split(",");
        Fascia frontFascia = new Fascia(parts[0], parts[1], parts[2], true);
        Fascia backFascia = new Fascia(parts[0], parts[1], parts[3], false);
        fascias.add(frontFascia);
        fascias.add(backFascia);
      }
      scanner.close();
      logger.info("System: All fascias available are recorded");
    } catch (FileNotFoundException error) {
      error.printStackTrace();
    }
  }

  /**
   * Adds the location of each fascia into the system.
   * 
   * @param file The file containing the location of each fascia.
   * @param fascias All of the fascias in the system.
   */
  public void setLocations(File file, ArrayList<Fascia> fascias) {
    try {
      Scanner scanner = new Scanner(file);
      while (scanner.hasNextLine()) {
        String newLine = scanner.nextLine();
        String[] parts = newLine.split(",");
        String location = parts[0] + parts[1] + parts[2] + parts[3];
        String sku = parts[4];
        // Check for matching SKUs.
        for (Fascia fascia : fascias) {
          if (fascia.getSku() == sku) {
            fascia.setLocation(location);
          }
        }

      }
      scanner.close();
      logger.info("System: All fascias' locations are now recorded");
    } catch (FileNotFoundException error) {
      error.printStackTrace();
    }
  }

  /**
   * Sets the count for those fascias that are less than 30 in stock.
   * 
   * @param file The file containing the amount of the fascias that are less than 30 in stock.
   * @param fascias All of the fascias currently in the system.
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
      logger.info("System: The number of each type of fascia is now recorded");
    } catch (FileNotFoundException error) {
      error.printStackTrace();
    }
  }

  /**
   * Fixes any error in picking which showed up during sequencing.
   * 
   * @param sequencer The sequencer who detected the error.
   * @param fascias An arrayList of class FasciaGroup which has all the picked picking requests.
   * @param picker The picker who is going to fix the problem.
   */
  public void fixError(Sequencer sequencer, ArrayList<FasciaGroup> fascias, Picker picker) {
    int index = 0;
    logger.warning("System: Orders with request ID " + sequencer.getToBeProcessed().getRequestId()
        + " were found to have an incorrect fascia received by " + "sequencer "
        + sequencer.getName() + ".");
    logger.warning("System: This set of fascias are thrown away.");
    for (FasciaGroup group : fascias) {
      // If they have the same ID, remove the group, and re-pick.
      if (group.getRequestId() == sequencer.getToBeProcessed().getRequestId()) {
        index = fascias.indexOf(group);
        fascias.remove(group);
        FasciaGroup repickedGroup = new FasciaGroup(sequencer.getToBeProcessed().getOrderFascia(),
            sequencer.getToBeProcessed().getRequestId());
        repickedGroup.setSequenced(true);
        fascias.add(index, repickedGroup);
        sequencer.setSequencedFascias(sequencer.getToBeProcessed().getOrderFascia());
        logger.warning("System: Picker " + picker.getName() + " repick Orders with request ID "
            + sequencer.getToBeProcessed().getRequestId());
        logger.info("Picker " + picker.getName() + ": Orders with request ID "
            + sequencer.getToBeProcessed().getRequestId() + " is now repicked correctly.");
        break;
      }
    }
  }

  /**
   * Runs the warehouse.
   * 
   * @param args An array of command line arguments.
   */
  public static void main(String[] args) {
    String path;
    if (args.length != 0) { // use the first argument from the command line.
      path = args[0];
    } else { // follow the standard event file
      path = "../16orders.txt";
    }
    try { // set up the logger
      Reader.setupLogger();
    } catch (IOException e1) {
      logger.log(Level.SEVERE, "File Logger not working.", e1);
    }
    Reader reader = new Reader();
    int nextGroup = 0;

    // All sets of orders in the system.
    ArrayList<Order> groups = new ArrayList<>();

    // All fascias in the system.
    ArrayList<Fascia> fascias = new ArrayList<>();

    // All groups of fascias that have been picked.
    ArrayList<FasciaGroup> pickedFascias = new ArrayList<>();

    // A single order that is received.
    ArrayList<ArrayList<String>> orders = new ArrayList<>();

    // All pickers, sequencers, and loaders in the system.
    ArrayList<Picker> pickers = new ArrayList<>();
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

    // sets amount of all fascias
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
            logger.info("We received a set of 4 orders");
          }
          // if that's a pick
        } else if (parts[0].equals("Picker")) {
          // declaring a ready picker
          if (parts[2].equals("ready")) {
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
              nextGroup++;
              pickers.add(newPicker);
            }
            logger.info("System: Picker " + parts[1] + " is ready.");
          } else if (parts[2].equals("pick")) { // an order to pick
            // look for picker
            Fascia toBePickedFascia = fascias.get(0);
            for (Picker oldPicker : pickers) {
              if (oldPicker.getName().equals(parts[1])) {
                // tell picker to pick fascia
                Order toBePicked = groups.get(oldPicker.groupIndex);
                toBePicked.findFascia(fascias);
                // pick fascia statement, actual picking happens after system confirmation.
                logger.info("System: Fascia with SKU number " + parts[3] + " was picked by picker "
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
                  logger.info("System: Picked the correct fascia");
                } else { // The system says that the picker picked the wrong fascias
                  logger.warning("System: Picker " + parts[1] + " picked the wrong fascia.");
                  logger.warning("System: The correct fascia to pick has SKU: "
                      + toBePicked.getOrderFascia().get(indexOfFascia).getSku());
                  oldPicker.pickFascia(toBePicked.getOrderFascia().get(indexOfFascia).getSku(),
                      fascias);
                  logger.info("System: Mistake has been fixed, Picker " + parts[1]
                      + " has picked the correct fascia.");

                }
                // if picker picked all 8 fascias
                if (oldPicker.isDone()) {
                  FasciaGroup pickedGroup =
                      new FasciaGroup(oldPicker.getFascias(), toBePicked.getRequestId());
                  pickedFascias.add(pickedGroup);
                  logger.info(
                      "System: All fascias in picking request number " + toBePicked.getRequestId()
                          + " were picked by picker " + oldPicker.getName());
                }
              }
            }
          }
        } else if (parts[0].equals("Replenisher")) { // if it's a Replenisher
          if (parts[2].equals("ready")) {
            logger.info("Replenisher " + parts[1] + " is ready.");
          } else {
            // Replenish at the given location
            String location = parts[3] + parts[4] + parts[5] + parts[6];
            Replenisher replenisher = new Replenisher();
            replenisher.replenish(location, fascias);
            logger.info("System: Fascias at location " + location + " should be replenished.");
            logger.info("Replenisher " + parts[1] + ": replenished fascia at " + location);
          }
        } else if (parts[0].equals("Sequencer")) { // if it's a Sequencer
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
            // Find the first FasciaGroup that is not sequenced.
            for (FasciaGroup tobeSequenced : pickedFascias) {
              if (tobeSequenced.isSequenced() == false) {
                groupNumber = tobeSequenced.getRequestId();
                tobeSequenced.setSequenced(true);
                break;
              }
            }
            // Set the order that the sequencer will work with.
            for (Order order : groups) {
              if (order.getRequestId() == groupNumber) {
                Order orderSequenced = order;
                sequencer.setToBeProcessed(orderSequenced);
              }
            }
            // Reset the sequencer's variables.
            sequencer.setSequencedFascias(new ArrayList<Fascia>());
            sequencer.setRescannedSkus(new ArrayList<String>());
            sequencer.setCorrect(true);
            logger.info("System: Sequencer " + parts[1] + " is ready");

          } else if (parts[2].equals("sequences")) {
            // Find the sequencer that will be sequencing.
            for (Sequencer oldSequencer : sequencers) {
              if (oldSequencer.getName().equals(parts[1])) {
                sequencer = oldSequencer;
              }
            }

            Fascia fasciaSeq = sequencer.getToBeProcessed().getOrderFascia()
                .get(sequencer.getSequencedFascias().size());
            // Sequence the fascia by comparing the SKU of the original order and the picked one.
            if (parts[3].equals(fasciaSeq.getSku())) {
              sequencer.getSequencedFascias().add(fasciaSeq);
              logger.info("System: Sequencer " + sequencer.getName() + " sequenced fascia with sku "
                  + fasciaSeq.getSku());
            } else {
              sequencer.getSequencedFascias().add(fasciaSeq);
              sequencer.setCorrect(false);
              logger.info("System: Sequencer " + sequencer.getName() + " sequenced fascia with sku "
                  + fasciaSeq.getSku());
            }
            if (sequencer.getSequencedFascias().size() == 8) {
              if (!sequencer.isCorrect()) {
                reader.fixError(sequencer, pickedFascias, pickers.get(0));
              } else {
                logger.info("System: Orders with request ID "
                    + sequencer.getToBeProcessed().getRequestId() + " are sequenced.");
              }
            }
          } else if (parts[2].equals("rescans")) {
            for (Sequencer oldSequencer : sequencers) {
              if (oldSequencer.getName().equals(parts[1])) {
                sequencer = oldSequencer;
              }
            }
            sequencer.rescan(parts[3], pickers.get(0));
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
            // Find the first order required to be loaded.
            for (Order order : groups) {
              if (order.getRequestId() == groupNumber) {
                Order orderLoaded = order;
                loader.setToBeProcessed(orderLoaded);
              }
            }
            loader.setRescannedSkus(new ArrayList<String>());
            logger.info("Loader " + parts[1] + " is ready");

          } else if (parts[2].equals("loads")) {
            for (Loader oldLoader : loaders) {
              if (oldLoader.getName().equals(parts[1])) {
                loader = oldLoader;
              }
            }
            logger.info("System: Loader " + loader.getName() + ", load the picking request with id "
                + loader.getToBeProcessed().getRequestId() + ".");
            loader.load();
          } else if (parts[2].equals("scans")) {
            for (Loader oldLoader : loaders) {
              if (oldLoader.getName().equals(parts[1])) {
                loader = oldLoader;
              }
            }
            logger.info("System: Loader " + loader.getName() + ", scan the fascia with SKU "
                + parts[3] + ".");
            loader.rescan(parts[3], pickers.get(0));
          } else if (parts[2].equals("rescans")) {
            for (Loader oldLoader : loaders) {
              if (oldLoader.getName().equals(parts[1])) {
                loader = oldLoader;
              }
            }
            logger.warning("System: Loader " + loader.getName() + ", rescan the fascia with SKU "
                + parts[3] + ".");
            loader.rescan(parts[3], pickers.get(0));
          }
        }
      }
      scanner.close();
      logger.info("System: All events are done.");
    } catch (FileNotFoundException error) {
      error.printStackTrace();
    }
  }
}
