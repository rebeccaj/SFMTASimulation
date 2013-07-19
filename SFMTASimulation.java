/** 
SFMTASimulation.java
CS 111B  Programming Fundamentals: Java
Due: Thursday, July 18, 2013, 11:55 PM
Group E: Teddy Deng, Katherine Soohoo, Rebecca A. Johnson
We assume the csv data files are in the same directory as this file.
*/

import java.io.*; // To use the PrintWriter class
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.StringTokenizer;


/**
SFMTASimulation class creates a program to simulate the operation
of the San Francisco MUNI bus and light rail vehicle system.
Objects are instantiated to represent vehicles, drivers, passengers, and
stations. A master control loop scans through the route information to 
move vehicles to stations, updating object data as it does so, and asking
objects to update themselves according to the new information, by calling their 
decision/update methods. The passengers all queue up first thing, and when 
there are no more passengers waiting at any stations, the program is finished.
*/
public class SFMTASimulation {
    
    /* 
    We create an ArrayList containing all of the stations, ordered by station ID. 
    */
    private ArrayList<Station> stations = new ArrayList<Station>();
    private ArrayList<Vehicle> vehicles = new ArrayList<Vehicle>();
    
    // The purpose of this list is to keep track of passengers in the program.
    // When a passenger has reached their position, they are removed to the list.
    private ArrayList<Person> passengersAL = createPassengerArrayList("passengers.csv"); 
    
    // Creates two-dimensional string arrays for all 7 routes.
    private static String[][] v8xBayshoreRoute = createRouteArray("8xBayshore.csv");
    private static String[][] v47VanNessRoute = createRouteArray("47VanNess.csv");
    private static String[][] v49MissionRoute = createRouteArray("49Mission.csv");
    private static String[][] vLTaravalRoute = createRouteArray("LTaraval.csv");
    private static String[][] vNJudahRoute = createRouteArray("NJudah.csv");
    private static String[][] vKTRoute = createRouteArray("KIngleside.csv", "TThird.csv");
    
    // Instantiates the initial LRVs and Buses.
    private Bus l8xBayshore = new Bus(v8xBayshoreRoute, 8);
    private Bus l47VanNess = new Bus(v47VanNessRoute, 47);
    private Bus l49Mission = new Bus(v49MissionRoute, 49);
    private LRV LTaraval = new LRV(vLTaravalRoute, 'L');
    private LRV NJudah = new LRV(vNJudahRoute, 'N');
    private LRV KInglesideTThird = new LRV(vKTRoute, 'K');
    
    // Creates arrays holding Person objects.
    private Person[] passengers = createPassengerArray("passengers.csv");
    private Person[] drivers = createDriverArray("drivers.csv");
    
    // Instantiates an two-dimensional array holding the transfer stops.
    private String[][] transferStations = initializeTransferStops("TransferStops.csv");
    
    /**
    main method instantiates an object of the class and runs the simulation.
    @param String[] args
    */
    public static void main(String[] args) {    
        /*
        Instantiate an SFMTASimulation object. This allows us to use
        the instance variables, available to all the class's methods.
        */
        SFMTASimulation ourMuni = new SFMTASimulation();
        
        // this method puts everything in motion
        ourMuni.runSimulation();
        
    }
    
    private void runSimulation() {
        
        // Acts as a loading message.
        System.out.println("Initializing Simulation...");
        
        // initialize ArrayLists of station and vehicles objects 
        initializeStations();
        initializeVehicles();
                
        //These two tasks together satisfy task 5
        printStationPeopleCount();
        printStationDriverCount();
        
        testProgram();

        // Now kick off the movement and action:
        
        //  place one empty vehicle at each route's origin 
        
        // while there are still passengers in system:
        while (passengersAL.size() > 0) { 
            
            //  call passenger.decision() for any passengers currently on board
            
            //  call passenger.decision() for passengers waiting at station
            
            //  call passenger.decision() for transferring passengers
            
            //  call driver.decision() for all drivers on board vehicles
            
            //  call driver.decision() for two drivers waiting at each station
            
            //  move vehicles forward from current stop
            
            //  call vehicle.decision() for all vehicles
        
        } // main control loop
        
        // print any output files here if that hasn't already been done
        
        //  end of method runSimulation
        
        /* 
        
        These comments explain in detail what the decision methods called
        above should do:
         
           call the passenger's decision method, which:
           = Determines whether s/he has arrived at their next stop. That will
            probably involve looking at their instance var that stores which
            vehicle object they are in, and calling that vehicle's getStopID(),
            to compare it to the passenger's itinerary
           
            +If so and it is the final destination, s/he should:
             - call the vehicle's removePassenger method
             - destroy
                the passenger object by removign it from the array it was in.
            +If so and it is a transfer stop (from the passsenger's perspective, because it's not their final destination), passenger should:
             - call the vehicle's removePassenger method
             - update their status/location to the station (which may be a 
               different station ID, from TransferStops.csv), rather than on
                 board that vehicle
             - call the station's queueTransfer method
             
          Next scan through a list of passengers that are queued at a 
          station. This may be evidence that a station does need to keep track
          of who is at it! Alternatively, scan through all passengers, and do
          this only for those who are queued. Alternatively yet, we could use 
          two high-scope arrays, one for queued passengers, one for boarded
          ones. No matter how we find the queued passengers:
          
          Call his/her decision method, which should:
           = Determine whether a bus they want is at the station AND
             if that vehicle has any available seats. If so,
             they call the station's popPassenger() and 
             call the vehicle's addPassenger() 
             change their location/status to on board that particular vehicle.
             
         next scan through a list of waiting transfer passengers, and call their
         decision() method
         
         Now, for each driver driving a vehicle, run their decision() method, which does all of the following:
         If  they're currently at an origin or terminus, then add one to their
         instance variable that stores how many trips they've made so far.
         then see if it is
         their seventh trip. If so, remove them from their vehicle by
         calling some vehicle method (removeDriver()?),
         a  vehicle setter method to change the vehicle's instance variable 
         operator to empty or some flag that means there is no driver 
         then run queueDriver() for the station,
         and change driver status/location to no vehicle ID
         Note again that it is decision() that does all that, not the
          controlling loop
         
         next for each driver waiting at a stop, run their decision() method,
         which does all of the following:
         if there is a driverless vehicle at the stop, get on board by calling
         vehicle methods to set vehicle's operator instance variable,
          call the station's popDriver(), and change
         driver status/location to have that vehicle ID .
         set driver trip number to 0
         
         Now we have moved all passengers and drivers, as well as
         updated all station and vehicle objects' info. 
         
         So move every vehicle forward one stop. This is where we run the vehicle's decision() method, which handles the logic:
          Recall that No 
         more than two vehicles traveling in the same direction may be 
         present in a station at any time.  Vehicles approaching an occupied
          station wait in the order of their arrival. Use Station's
          getNumberOfVehicles(0) or getNumberOfVehicles(1) to see if it's full,
          if it is full, then use station queueWaitingOutbound() or
          queueWaitingInbound() to get in line.
         Has the vehicle arrived at an origin or terminus? if so, 
             change enum Direction (a flag which indicates in which direction
             along the route the vehicle will move)  
             Print this line:
             B:Vehicle#:Driver:Direction:RouteName:NumberOfCoaches
             where B indicates it's a bus, L and LRV
         See if it's reached a terminus for the FIRST time and so
         Should put a new vehicle  at the origin (which should also make that
         new vehicle's decision() method run, in case there are already 
         two vehicles at the origin)
         Do the K switch to T logic in the decision method.
              
              repeat! Until no more passengers. If we were storing all of
              the passengers in ArrayLists, we could delete passengers as
              they reach their destination, then know we're done when the
              passenger ArrayList(s) is/are empty. But maybe not worth the
              effort of revamping tons of code. 
        */
        
    }
    
    /**
     * A method to test the working algorithm.
     * Right now, all the passengers don't have an algorithm to come up with a route plan.
     * So whenever a vehicle arrives at their startID, they will get on regardless of where it goes.
     * If the vehicle happens to reach a passenger's stopID, they were lucky to get to their destination and they get off the vehicle.
     *
     * This method reads from an ArrayList that contains all of the passengers as Person objects.
     * When a passenger reaches their destination, they are removed from the list.
     * This method should read from the Station objects' list of people rather than the way it is now.
     *
     * This method currently prints every time a passenger boards or leaves a vehicle.
     */
    private void testProgram() {
        while (passengersAL.size() > 0) {
            for (int i = 0; i < vehicles.size() - 1; i++) {
                vehicles.get(i).goToNextStop();
                
                // Removes passengers if they've reached their destination.
                for (int k = 0; k < vehicles.get(i).getPassengerCount(); k++) {
                    passengersAL.get(k).setCurrentStationID(vehicles.get(i).getStopID());	//Update current location (which station)
                    if (vehicles.get(i).getStopID() == passengersAL.get(k).getStopID()) {
                        vehicles.get(i).removePassenger(passengersAL.get(k));
                        System.out.println(passengersAL.get(k).getName() + " departed " + vehicles.get(i).getIDNumber());
                        passengersAL.remove(k);
                    }
                }
                
                // Adds passengers if the vehicle arrived at their startID and there is space.
                for (int j = 0; j < passengersAL.size() - 1; j++) {
                    //System.out.println("Entered passenger loop");
                    if (!vehicles.get(i).isFull()) {
                        //System.out.println("Vehicle is not full");
                        if (vehicles.get(i).getStopID() == passengersAL.get(j).getStartID()) {
                            vehicles.get(i).addPassenger(passengersAL.get(j));				//Add passenger to vehicle object
                            passengersAL.get(j).setCurrentVehicleID(vehicles.get(i).getIDNumber());	//Update vehicle on
                            System.out.println(passengersAL.get(j).getName() + " boarded " + vehicles.get(i).getIDNumber());
                        }
                    }
                    else
                        break;
                }
            }
        }
    }
    
    /**
    initializeStations method consumes our data files and instantiates
    Station objects, storing them in an array called stations. This method
    first works on data in passengers.csv, then consumes drivers.csv, then
    looks in all of the route files to see if there are any stations with
    no passengers or drivers waiting at them. When this method finishes, the
    stations ArrayList contains a Station object for every station that exists
    in the MUNI system.
    */
    private void initializeStations() {
        
        consumePassengersCSVforStations();
        consumeDriversCSVforStations();
        consumeRouteCSVsForStations();
    }
    
    
    /**
    consumePassengersCSVforStations method reads passengers.csv in order to
    instantiate station objects.
    */
    private void consumePassengersCSVforStations() {    
        //method variable declarations
        String inputStr;
        String name;
        int stationID;
        StringTokenizer strToken;
        int arrayIndex;
        /* set the following to null to avoid "variable *putFile might not have been initialized" compiler errors.
        */
        Scanner inputFile = null;
        Station thisStation;
        
        
        // First we consume the passengers file. Open it here:
        try {
            File file = new File("passengers.csv");
            inputFile = new Scanner(file);
        }
        catch (FileNotFoundException e) {
            System.out.println("File passengers.csv not found.");
        }
        // Read until the end of the file.
        while (inputFile.hasNext())
        {
            inputStr = inputFile.nextLine();
            
            /* inputStr should look something like this:
            Isabella,17364,15204
            Passenger name, origin station ID, destination station ID
            We want to extract the name and origin to store in a Station object.
            */
            strToken = new StringTokenizer(inputStr, ",");
            
            name = strToken.nextToken();
            stationID = Integer.parseInt(strToken.nextToken());
            
            /* Now that we have the name of the passenger and place they're waiting, we'll create a new station object or add the passenger to the object containing this station ID, if it already exists.
            */
            if (stations.size() == 0) {
                //this is the first station - go ahead and add it to stations
                thisStation = new Station(stationID,name);
                stations.add(thisStation);
            }
            else if (stations.size() == 1) {
                /*
                findInArray works with at least two array elements. In this 
                case we just need to know if it's higher or lower than the one
                */
                
                //instantiate Station object
                thisStation = new Station(stationID,name);
                
                //add to before or after current element
                if (stationID > stations.get(0).getStationID()) {
                    stations.add(thisStation);
                }
                else {
                    stations.add(0,thisStation);
                }
            }
            else {
                
                arrayIndex = findInArray(stationID);
                
                if (arrayIndex >= 0) { //there is already an object for this station
                    // add this passenger to this station's queue
                    stations.get(arrayIndex).queuePassenger(name); 
                }
                else { // new station
                    // instantiate a new Station object
                    thisStation = new Station(stationID,name);
                    
                    /* now figure out where in array to put it. 
                    Beginning, end, or where in the middle?
                    */
                    if (stationID < stations.get(0).getStationID()) {
                        // Lowest ID, make it the first array element.
                        stations.add(0,thisStation);
                    }
                    else if (stationID >
                        stations.get(stations.size()-1).getStationID()) {
                        // Highest ID, make it the last array element.
                        stations.add(thisStation);
                    }
                    else {
                        /* find the right spot to put this new Station
                        in our sorted list
                        */
                        
                        arrayIndex = findSpotInArray(stationID);
                        
                        //now store it in array 
                        stations.add(arrayIndex,thisStation);
                    
                    }
                } // new station
            } // stations.size() > 0
        } // scanning through passengers file
        
        inputFile.close();// close the file when done.
        
    } // consumePassengersCSVforStations()
    
    
    /**
    consumeDriversCSVforStations method reads drivers.csv in order to
    instantiate new and update existing station objects.
    */
    private void consumeDriversCSVforStations() {
        //method variable declarations
        String inputStr;
        String name;
        int stationID;
        StringTokenizer strToken;
        int arrayIndex;
        /* set the following to null to avoid "variable *putFile might not have been initialized" compiler errors.
        */
        Scanner inputFile = null;
        Station thisStation;
        
        
        // First we consume the drivers file. Open it here:
        try {
            File file = new File("drivers.csv");
            inputFile = new Scanner(file);
        }
        catch (FileNotFoundException e) {
            System.out.println("File drivers.csv not found.");
        }
        // Read until the end of the file.
        while (inputFile.hasNext())
        {
            inputStr = inputFile.nextLine();
            
            /* inputStr should look something like this:
            Mary,13163,0
            Driver name, origin station ID, destination station ID
            We want to extract the name and origin to store in a Station object.
            */
            strToken = new StringTokenizer(inputStr, ",");
            
            name = strToken.nextToken();
            stationID = Integer.parseInt(strToken.nextToken());
            
            /* Now that we have the name of the driver and place they're waiting, we'll create a new station object or add the driver to the object containing this station ID, if it already exists.
            */
                
            arrayIndex = findInArray(stationID);
            
            if (arrayIndex >= 0) { //there is already an object for this station
                // add this driver to this station's queue
                stations.get(arrayIndex).queueDriver(name); 
                // sets the station to a origin or terminus
                if (!stations.get(arrayIndex).getIsOriginOrTerminus())
                    stations.get(arrayIndex).setIsOriginOrTerminus(true);
            }
            else { // new station
                // instantiate a new Station object
                thisStation = new Station(stationID,name,true);
                
                /* now figure out where in array to put it. 
                Beginning, end, or where in the middle?
                */
                if (stationID < stations.get(0).getStationID()) {
                    // Lowest ID, make it the first array element.
                    stations.add(0,thisStation);
                }
                else if (stationID >
                    stations.get(stations.size()-1).getStationID()) {
                    // Highest ID, make it the last array element.
                    stations.add(thisStation);
                }
                else {
                    /* find the right spot to put this new Station
                    in our sorted list
                    */
                    
                    arrayIndex = findSpotInArray(stationID);
                    
                    //now store it in array 
                    stations.add(arrayIndex,thisStation);
                
                }
            } // new station
        } // scanning through passengers file
        
        inputFile.close();// close the file when done.
        
    } // consumeDriversCSVforStations()
    
    /**
    consumeRouteCSVforStations method reads the route csv files in order to
    instantiate new station objects that do no exist.
    */
    private void consumeRouteCSVsForStations() {
        //method variable declarations
        String inputStr;
        String name;
        int stationID;
        StringTokenizer strToken;
        int arrayIndex;
        /* set the following to null to avoid "variable *putFile might not have been initialized" compiler errors.
        */
        Scanner inputFile = null;
        Station thisStation;
        
        // An array containing the file names of all the routes files.
        String[] routeFiles = { "8xBayshore.csv", "47VanNess.csv", "49Mission.csv",
                                "KIngleside.csv", "LTaraval.csv", "NJudah.csv", "TThird.csv" };
        
        // Loops through all the file names in the array
        for (int i = 0; i < routeFiles.length; i++) {
        
            // First we consume the route file. Open it here:
            try {
                File file = new File(routeFiles[i]);
                inputFile = new Scanner(file);
            }
            catch (FileNotFoundException e) {
                System.out.println("File " + routeFiles[i] + " not found.");
            }
            
            inputFile.nextLine(); // Skips first line in the route file
            
            // Read until the end of the file.
            while (inputFile.hasNext())
            {
                inputStr = inputFile.nextLine();
                
                /* inputStr should look something like this:
                West Portal Station, 16740
                Route name, station ID
                We want to extract the station ID to store in a Station object.
                */
                strToken = new StringTokenizer(inputStr, ",");
                
                name = strToken.nextToken();
                stationID = Integer.parseInt(strToken.nextToken());
                
                /* Now that we have station ID, we'll create a new station object if it doesn't already exist.
                */
                    
                arrayIndex = findInArray(stationID);
                
                if (arrayIndex >= 0) { //there is already an object for this station
                    // Do nothing
                }
                else { // new station
                    // instantiate a new Station object
                    thisStation = new Station(stationID);
                    
                    // This line prints to the console everytime a new station is created.
                    //System.out.println("New station created with no people waiting: " + stationID );
                    
                    /* now figure out where in array to put it. 
                    Beginning, end, or where in the middle?
                    */
                    if (stationID < stations.get(0).getStationID()) {
                        // Lowest ID, make it the first array element.
                        stations.add(0,thisStation);
                    }
                    else if (stationID >
                        stations.get(stations.size()-1).getStationID()) {
                        // Highest ID, make it the last array element.
                        stations.add(thisStation);
                    }
                    else {
                        /* find the right spot to put this new Station
                        in our sorted list
                        */
                        
                        arrayIndex = findSpotInArray(stationID);
                        
                        //now store it in array 
                        stations.add(arrayIndex,thisStation);
                    
                    }
                } // new station
            } // scanning through passengers file
            
            inputFile.close();// close the file when done.
        
        }
        
    } // consumeRouteCSVforStations()
    
    /**
    findInArray method searches for a station ID in stations.
    Use this method if you have a station ID you want to find the object for.
    Utilizes binary search logic. 
    @param id the integer station ID
    @return the array index if found, otherwise -1.
    */
    private int findInArray(int id) {
        // declare and initialize method variables
        int highestIndex = stations.size()-1;
        int lowestIndex  = 0;
        int guessIndex = (highestIndex + lowestIndex ) / 2;
        int indexToReturn = -1;
        int guessID;
        boolean stopChecking = false;
        
        // look until we find it or run out of places to look
        while (!stopChecking && lowestIndex < highestIndex) {
        
            /* 
            Let's do a preliminary check to see if the ID is larger than the 
            largest unchecked ID in the array or smaller than the smallest
            unchecked ID in the array. If so, we know that it's not found.
            */
            if (id > stations.get(highestIndex).getStationID()  ||
                id < stations.get( lowestIndex).getStationID() ) {
                
                stopChecking = true; 
            }
            else {
                // Store the station ID of our guess in guessID
                guessID = stations.get(guessIndex).getStationID();
                
                if ( guessID == id ) {
                    indexToReturn = guessIndex;
                    stopChecking = true;
                }
                else if (guessID > id) {
                    // found ID too high; continue search below the guess index
                    highestIndex = guessIndex - 1;
                    guessIndex = (highestIndex + lowestIndex ) / 2;
                }
                else {
                    // found ID too low; continue search above the guess index
                    lowestIndex = guessIndex + 1;
                    guessIndex = (highestIndex + lowestIndex ) / 2;
                }
                
                /* We need to do this extra check, so while condition
                won't skip some cases.*/
                if (lowestIndex == highestIndex &&
                    id == stations.get(guessIndex).getStationID()) {
                    
                    stopChecking = true;
                    indexToReturn = guessIndex;
                }
                
            }
        } // while loop
            
        return indexToReturn;
        
    } // findInArray method
    
    
    /**
    findSpotInArray method searches for the spot where we should insert
    our new station ID into stations. It will be the index of the existing
    array element whose ID is just lower than our new ID.
    Utilizes algorithm similar to binary search. 
    @param newID the integer station ID
    @return the array index where we want to do the insert.
    */
    private int findSpotInArray(int newID) {
        // declare and initialize method variables
        int highestIndex = stations.size()-1;
        int lowestIndex  = 0;
        int guessIndex = (highestIndex + lowestIndex ) / 2;
        int guessID;
        boolean stopChecking = false;
        
        
        // look until we find it or run out of places to look
        while (!stopChecking && lowestIndex < highestIndex-1) {
            // Store the station ID of our guess in guessID
            guessID = stations.get(guessIndex).getStationID();
            
            if (guessID > newID) {
                // found ID too high; continue search below the guess index
                highestIndex = guessIndex ;
                guessIndex = (highestIndex + lowestIndex ) / 2;
            }
            else {
                // found ID too low; continue search above the guess index
                lowestIndex = guessIndex ;
                guessIndex = (highestIndex + lowestIndex ) / 2;
            }
        } // while loop
            
        return lowestIndex + 1;
        
    } // findSpotInArray method
    
    /**
     * The initializeVehicles method adds the initial vehicles to the vehicle ArrayList.
     */
    private void initializeVehicles() {
        vehicles.add(l8xBayshore);
        vehicles.add(l47VanNess);
        vehicles.add(l49Mission);
        vehicles.add(LTaraval);
        vehicles.add(NJudah);
        vehicles.add(KInglesideTThird);
    }
    
    /**
     * This launchVehicle method accepts a vehicle Object as an argument and moves the vehicle to the next station
     * @param VehicleName A Vehicle object that has already been instantiated (i.e. 18xBayshore, or NJudah)
     */
    private void launchVehicle(Vehicle vehicleName){


	//Vehicle-move actions. We also need to check all passenger ID's, so the upper-limit of should be the total number of passengers,
	//or in other words, use getTotalNumPassengersOrDrivers()...
	for(int i = 0; i < getTotalNumPassengersOrDrivers("passengers.csv"); i++){
	
		// j is used as an index for our passenger array object.
		for(int j = 0; j < getTotalNumPassengersOrDrivers("passengers.csv"); j++){
				
			//Makes sure vehicle doesn't exceed it's passenger limitations.
			while(vehicleName.getPassengerCount() <= vehicleName.getMaxCapacity()){
				
				//Checks the vehicles current location with the passengers ID. If they match,
				if(vehicleName.getStopID() == passengers[j].getStartID()){
			
					passengers[j].setCurrentStationID(vehicleName.getStopID());	    //Update current location (which station)
					passengers[j].setCurrentVehicleID(vehicleName.getIDNumber());	//Update vehicle on
					vehicleName.addPassenger(passengers[j]);				//Add passenger to vehicle object

					//Once the user gets onto the vehicle, he will be headed towards his destination.
					//We no longer need the start ID (unless its a transfer? not sure about that yet)
					//So we assign aa "unused" station ID that does not exist, to prevent the passenger
					// object from ever boarding again.
					int delete = 0;
					passengers[j].setStartID(delete);
					
					//Finding the station index where station exists
					int stationArrayIndex = findInArray(vehicleName.getStopID());
	
					stations.get(stationArrayIndex).popPassenger();
				
				}
			}//Once max capacity of vehicle has been filled, the vehicle goes to the next Station.
	
			//This if-statement ensures that the j-loop will continue to add passengers until the maximum is reached.
			//Once the coach(es) are full, only then will the vehicle move to the next stop.
			//If there are NO passengers present at the station, this if-statement makes sure that 
			//the j-loop STILL looks for possible passengers before moving on to the next stop.
			if(j == (getTotalNumPassengersOrDrivers("passengers.csv") - 1)){
				
				vehicleName.goToNextStop();	//Sends vehicles to the next station.
	
				int stationArrayIndex = findInArray(vehicleName.getStopID());

				passengers[j].setCurrentStationID(vehicleName.getStopID());
				passengers[j].setCurrentVehicleID(vehicleName.getIDNumber());

				if(passengers[j].decisionGetOffVehicle()) {
			
					vehicleName.removePassenger(passengers[j]);
		
				}
			}
		}
	}
    }
    
    
    /**
    printStationPeopleCount method
    For each station print out on one line, separated by a comma "," the Stop
    ID, and the number of passengers waiting. Save to StationPeopleCount.txt
    */
    private void printStationPeopleCount() {
        
        PrintWriter outputFile = null;
        
        try {
            outputFile = new PrintWriter("StationPeopleCount.txt");
        }
        catch (Exception e) {
            System.out.println("Error");
        }
        for (Station s : stations)
            outputFile.println(s.getStationID() + "," + s.getPassengerCount());
            
        outputFile.close();
        
        
    } // printStationPeopleCount()
    
    
    /**
    printStationDriverCount method
    For each origin or terminal station, print out on one line separated by a
    comma "," the Stop ID and the number of drivers waiting there.
    Save to StationDriverCount.txt
    */
    private void printStationDriverCount() {
        
        PrintWriter outputFile = null;
        
        try {
            outputFile = new PrintWriter("StationDriverCount.txt");
        }
        catch (Exception e) {
            System.out.println("Error");
        }
        for (Station s : stations) {
            if (s.getIsOriginOrTerminus())
                outputFile.println(s.getStationID() + "," + s.getDriverCount());
        }
        
        outputFile.close();
    }
    
    /**
     * The createPassengerArray method creates an array of Person objects using
     * information from the file.
     * @param fileNamePassengers The file containing a list of passengers.
     * @return passenger An array of Person objects.
     */
    public static Person[] createPassengerArray(String fileNamePassengers) {
        //Creating passenger : an Array of Person objects.
        int totalNumPersonsPassenger = getTotalNumPassengersOrDrivers(fileNamePassengers);	//Getting size of file; this will be size of array of Person objects.
        Person[] passenger = new Person[totalNumPersonsPassenger];							//Creating Array of Person object with size.
        
        try {
		Scanner inputFile = new Scanner(new File(fileNamePassengers));
		
		for(int i = 0; inputFile.hasNextLine(); i++){
			
			String line = inputFile.nextLine();	//Stores line
			String[] tokens = line.split(",");		//Splitting tokens with comma delimiter ","
			
			//Using the person constructor the initialize each index (each person) with corresponding name, and ID's.
			passenger[i] = new Person(tokens[0], Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2]), "Passenger");
		}
		
		inputFile.close();	//Close file when done.
		
        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
        }
        
        return passenger;
    }
    
     /**
     * The createPassengerArrayList method creates an array of Person objects using
     * information from the file.
     * @param fileNamePassengers The file containing a list of passengers.
     * @return passenger An arraylist of Person objects.
     */
    public static ArrayList<Person> createPassengerArrayList(String fileNamePassengers) {
        //Creating passenger : an ArrayList of Person objects.
        ArrayList<Person> passenger = new ArrayList<Person>();  //Creating ArrayList of Person object with size.
        
        try {
		Scanner inputFile = new Scanner(new File(fileNamePassengers));
		
		for(int i = 0; inputFile.hasNextLine(); i++){
			
			String line = inputFile.nextLine();	//Stores line
			String[] tokens = line.split(",");  //Splitting tokens with comma delimiter ","
			
			//Using the person constructor the initialize each index (each person) with corresponding name, and ID's.
			passenger.add(new Person(tokens[0], Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2]), "Passenger"));
		}
		
		inputFile.close();	//Close file when done.
		
        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
        }
        
        return passenger;
    }
    
    /**
     * The createDriverArray method creates an array of Person objects using
     * information from the file.
     * @param fileNameDrivers The file containing a list of drivers.
     * @return driver An array of Person objects.
     */
    public static Person[] createDriverArray(String fileNameDrivers) {
        //Creating driver : an Array of Person objects.
        int totalNumPersonsDriver = getTotalNumPassengersOrDrivers(fileNameDrivers);	//Getting size of file; this will be size of array of Person objects.
        Person[] driver = new Person[totalNumPersonsDriver];							//Creating Array of Person object with size.
        
        try {
            Scanner inputFile = new Scanner(new File(fileNameDrivers));
            
            for(int i = 0; inputFile.hasNextLine(); i++){
                
                String line = inputFile.nextLine();	//Stores line
                String[] tokens = line.split(",");		//Splitting tokens with comma delimiter ","
                
                //Using the person constructor the initialize each index (each person) with corresponding name, and ID's.
                driver[i] = new Person(tokens[0], Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2]), "Driver");
            }
            
            inputFile.close();	//Close file when done.
            
        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
        }
        
        return driver;
    }
    
    /** 
     * The createRouteArray method reads a route file and returns a two-dimensional
     * array with the first column holding the route name and the second column
     * holding the stop ID.
     * @param fileName The name of the file.
     * @return routeArray A two-dimensional array separating the route name
     *          and stop ID into two columns.
     */
    public static String[][] createRouteArray(String fileName) {
        // Calls the getNumOfStops method to determine the number of stops.
        int numOfStops = getNumOfStops(fileName); 
        Scanner inputFile = null;
        
        // Creates a two-dimensional array with a length equal to the number of stops.
        String[][] routeArray = new String[numOfStops][numOfStops];
        
        String line;     // Store the line being read
        String[] tokens; // Stores the tokenized string
        
        try {
            inputFile = new Scanner(new File(fileName));
        }
        catch (FileNotFoundException e) {
            System.out.println("File not found.");
        }
        
        inputFile.nextLine(); // Skips first line
        
        for (int i = 0; inputFile.hasNext(); i++) {
            line = inputFile.nextLine();
            tokens = line.split(",");
            
            routeArray[i][0] = tokens[0]; // Stores the route name
            routeArray[i][1] = tokens[1]; // Stores the stop ID
        }
        
        inputFile.close();
        
        return routeArray;
    }
    
    /** 
     * The createRouteArray method reads two route files and returns a two-dimensional
     * array with the first column holding the route name and the second column
     * holding the stop ID.
     * @param fileName The name of the first file.
     * @param fileName2 The name of the second file.
     * @return routeArray A two-dimensional array separating the route name
     *          and stop ID into two columns.
     */
    public static String[][] createRouteArray(String fileName, String fileName2) {
        // Calls the getNumOfStops method to determine the number of stops.
        int numOfStops = getNumOfStops(fileName, fileName2);    
        
        int fileOneStopNum = getNumOfStops(fileName);
        
        Scanner inputFile = null;
        Scanner inputFile2 = null;
        
        // Creates a two-dimensional array with a length equal to the number of stops.
        String[][] routeArray = new String[numOfStops][numOfStops];
        
        String line;     // Store the line being read
        String[] tokens; // Stores the tokenized string
        
        try {
            inputFile = new Scanner(new File(fileName));
            inputFile2 = new Scanner(new File(fileName2));
        }
        catch (FileNotFoundException e) {
            System.out.println("File(s) not found.");
        }
        
        inputFile.nextLine(); // Skips first line
        
        for (int i = 0; inputFile.hasNext(); i++) {
            line = inputFile.nextLine();
            tokens = line.split(",");
            
            routeArray[i][0] = tokens[0]; // Stores the route name
            routeArray[i][1] = tokens[1]; // Stores the stop ID
        }
        
        inputFile.close();
        
        // Adds the stops from the second file to the array.
        inputFile2.nextLine();
        inputFile2.nextLine(); // Skips the first two lines
        
        for (int i = 0; inputFile2.hasNext(); i++) {
            line = inputFile2.nextLine();
            tokens = line.split(",");
            
            routeArray[fileOneStopNum + i][0] = tokens[0]; // Stores the route name
            routeArray[fileOneStopNum + i][1] = tokens[1]; // Stores the stop ID
        }
        
        inputFile2.close();
        
        return routeArray;
    }
    
    /**
     * The getNumOfStops reads a route file and counts the number of stops.
     * @param fileName The name of the file.
     * @return stopCount The number of stops in the route.
     */
    public static int getNumOfStops(String fileName) {
        int stopCount = 0;  // Counter for stops in a route
        Scanner inputFile = null;
        
        try {
            inputFile = new Scanner(new File(fileName));
        }
        catch (FileNotFoundException e) {
            System.out.println("File " + fileName + " not found.");
        }
        
        inputFile.nextLine(); // Skips the first line
        
        while (inputFile.hasNextLine()) {
            inputFile.nextLine();   // Reads next line in the file
            stopCount++;            // Adds to the stops counter
        }
        
        inputFile.close();
        
        return stopCount;
    }
    
    /**
     * The getNumOfStops reads two route files and counts the number of stops.
     * @param fileName The name of the file.
     * @param fileName2 The name of the second file.
     * @return stopCount The number of stops in the route.
     */
    public static int getNumOfStops(String fileName, String fileName2) {
        int stopCount = 0;  // Counter for stops in a route
        
        Scanner inputFile = null;
        Scanner inputFile2 = null;
        
        try {
            inputFile = new Scanner(new File(fileName));
            inputFile2 = new Scanner(new File(fileName2));
        }
        catch (FileNotFoundException e) {
            System.out.println("File(s) not found.");
        }
        
        inputFile.nextLine(); // Skips the first line
        
        while (inputFile.hasNextLine()) {
            inputFile.nextLine();   // Reads next line in the file
            stopCount++;            // Adds to the stops counter
        }
        
        inputFile.close();
        
        // Reads the number of stops from the second file and adds to counter.
        inputFile2.nextLine();
        inputFile2.nextLine(); // Skips the first two lines
        
        while (inputFile2.hasNextLine()) {
            inputFile2.nextLine();   // Reads next line in the file
            stopCount++;             // Adds to the stops counter
        }
        
        inputFile2.close();
        
        return stopCount;
    }
    
    /**
	 * Method calculates and returns the number of lines in a file; Num of lines determines how many "people" are present in file.
	 * @param filename Name of file we will read.
	 * @return An integer that will influence size of an Array of Person objects
	 */
	public static int getTotalNumPassengersOrDrivers(String filename){
		
		int personCount = 0;	//Set flag to 0.
		
		try {
			Scanner inputFile = new Scanner(new File(filename));
			
			while(inputFile.hasNextLine()){
				inputFile.nextLine();			//Move to next line.
				personCount++;
			}
			
			inputFile.close();
			
		} catch (FileNotFoundException e) {
			System.out.println("File not found.");
		}
		
		return personCount;	//Return the count
	}
	
    /**
     * The initializeTransferStops methods returns a two-dimensional array
     * containing information about transfer stops.
     * @param fileName The name of the file.
     * @return transferStop A two-dimensional array holding the...
     *         Bus Stop ID, LRV Stop ID, Stop Name, Bus Routes
     */
    public static String[][] initializeTransferStops(String fileName) {
        int stopCounter = 0;
        try{
            Scanner inputFile = new Scanner(new File(fileName));

            inputFile.nextLine();		//Skips first line of descriptions.

            while(inputFile.hasNextLine()){

                inputFile.nextLine();
            
                stopCounter++;
            }
        }
        catch(FileNotFoundException e){
            System.out.println("File not found.");
        }

        final int TOTAL_TRANSFER_STOPS = stopCounter;

        String[][] transferStop = new String[TOTAL_TRANSFER_STOPS][TOTAL_TRANSFER_STOPS];

        try{
            
            Scanner inputFile = new Scanner(new File(fileName));

            inputFile.nextLine(); 		//Skips the first line.

            for(int i = 0; inputFile.hasNextLine(); i++){

                String line = inputFile.nextLine();

                String[] tokens = line.split(",");

                transferStop[i][0] = tokens[0]; 	//Bus Stop ID
                transferStop[i][1] = tokens[1];		//LRV Stop ID
                transferStop[i][2] = tokens[2];		//Stop Name/ Description
                transferStop[i][3] = tokens[3];		//Bus routes 
            }
        }
        catch(FileNotFoundException e){
            System.out.println("File not found.");
        }

        return transferStop;	
    }
    
     /**
     * The getRouteInfo method allows access to the private route info.
     * @param rName The name of the route.
     * @return A two-dimesional array of a route.
     */
    public static String[][] getRouteInfo(String rName) {
        if (rName.equals("8X"))
            return v8xBayshoreRoute;
        else if (rName.equals("47"))
            return v47VanNessRoute;
        else if (rName.equals("49"))
            return v49MissionRoute;
        else if (rName.equals("L"))
            return vLTaravalRoute;
        else if (rName.equals("N"))
            return vNJudahRoute;
        else if (rName.equals("KT"))
            return vKTRoute;
        else
            return null;
    }
    
} // SFMTASimulation

/**
Station is the blueprint for MUNI station objects.
*/
class Station {
    
    private int stationID; // unique station identifier
    private ArrayList<String> passengers; // passengers waiting at station
    private ArrayList<String> transfers; // transferring passengers waiting
    private ArrayList<String> drivers;    // drivers waiting at station
    private boolean isOriginOrTerminus;   
    /* vehicles currently at station (max 2 in any direction). 
    numberOfVehicles[0] stores outbound vehicles at station,
    numberOfVehicles[1] stores inbound vehicles at station
    */
    private int[] numberOfVehicles;  
    private ArrayList<Integer> waitingOutbound; // station was full
    private ArrayList<Integer> waitingInbound; 
    
    /**
    constructor, no-argument
    This shouldn't be called, as we probably always want the station ID provided. 
    */
    public Station() {
        
        stationID  = 0;
        passengers = new ArrayList<String>();
        transfers  = new ArrayList<String>();
        drivers    = new ArrayList<String>();
        isOriginOrTerminus = false;
        numberOfVehicles = new int[] {0,0};
        waitingOutbound = new ArrayList<Integer>();
        waitingInbound  = new ArrayList<Integer>();
    }
    
    /**
    constructor, one-argument
    @param id int station ID.
    */
    public Station(int id) {
        
        stationID  = id;
        passengers = new ArrayList<String>();
        transfers  = new ArrayList<String>();
        drivers    = new ArrayList<String>();
        isOriginOrTerminus = false;
        numberOfVehicles = new int[] {0,0};
        waitingOutbound = new ArrayList<Integer>();
        waitingInbound  = new ArrayList<Integer>();
    }
    
    
    /**
    constructor, two-argument
    @param id int station ID.
    @param p String passenger name to add to passengers.
    */
    public Station(int id, String p) {
        
        stationID  = id;
        passengers = new ArrayList<String>();
        transfers  = new ArrayList<String>();
        drivers    = new ArrayList<String>();
        isOriginOrTerminus = false;
        numberOfVehicles = new int[] {0,0};
        waitingOutbound = new ArrayList<Integer>();
        waitingInbound  = new ArrayList<Integer>();
        
        passengers.add( p);
    }
    
    
    /**
    constructor, three-argument
    @param id int station ID.
    @param d String driver name to add to drivers.
    @param driverFlag boolean, indicates this new station has a driver waiting
    */
    public Station(int id, String d, boolean driverFlag) {
        
        stationID  = id;
        passengers = new ArrayList<String>();
        transfers  = new ArrayList<String>();
        drivers    = new ArrayList<String>();
        isOriginOrTerminus = true; // drivers wait at origin or terminus
        numberOfVehicles = new int[] {0,0};
        waitingOutbound = new ArrayList<Integer>();
        waitingInbound  = new ArrayList<Integer>();
        
        drivers.add( d);
    }
    
    
    /**
    getStationID method
    @return the int station ID of this station
    */
    public int getStationID() {
        
        return stationID;
    }
    
    
    /**
    getPassengerCount method
    @return the int count of passengers at this station
    */
    public int getPassengerCount() {
        
        return passengers.size();
    }
    
    
    /**
    getTransferCount method
    @return the int count of transferring passengers at this station
    */
    public int getTransferCount() {
        
        return transfers.size();
    }
    
    
    /**
    getDriverCount method
    @return the int count of drivers at this station
    */
    public int getDriverCount() {
        
        return drivers.size();
    }
    
    
    /**
    getIsOriginOrTerminus method
    @return the boolean flag of whether station is origin or terminus
    */
    public boolean getIsOriginOrTerminus() {
        
        return isOriginOrTerminus;
    }
    
    
    /**
    getNumberOfVehicles method
    @param inOrOutBound int 1 if inbound, 0 if outbound
    @return int number of vehicles currently occupying station
    */
    public int getNumberOfVehicles(int inOrOutBound) {
        
        return numberOfVehicles[inOrOutBound];
    }
    
    
    /**
    getFirstWaitingOutbound method
    @return int ID of the waiting vehicle, in the outbound direction,
            next in line to make it to station
    */
    public int getFirstWaitingOutbound() {
        
        return waitingOutbound.get(0);
    }
    
    
    /**
    getFirstWaitingInbound method
    @return int ID of the waiting vehicle, in the inbound direction,
            next in line to make it to station
    */
    public int getFirstWaitingInbound() {
        
        return waitingInbound.get(0);
    }
    
    
    /**
    queuePassenger method
    Called when we have a new passenger waiting at this station.
    @param p String passenger to add to passengers
    */
    public void queuePassenger(String p) {
        
        passengers.add(p);
    }
    
    
    /**
    queueTransfer method
    Called when we have a new transferring passenger waiting at this station.
    @param t String passenger to add to transfers
    */
    public void queueTransfer(String t) {
        
        transfers.add(t);
    }
    
    
    /**
    queueDriver method
    Called when we have a new driver waiting at this station.
    @param d String driver to add to drivers
    */
    public void queueDriver(String d) {
        
        drivers.add(d);
    }
    
    
    /**
    queueWaitingOutbound method
    Called when we have a new outbound vehicle waiting to get to this station -
    it can't because there are already 2 outbound vehicles at station.
    @param v int Vehicle ID to add to waitingOutbound
    */
    public void queueWaitingOutbound(int v) {
        
        waitingOutbound.add(v);
    }
    
    
    /**
    queueWaitingInbound method
    Called when we have a new inbound vehicle waiting to get to this station -
    it can't because there are already 2 inbound vehicles at station.
    @param v int Vehicle ID to add to waitingInbound
    */
    public void queueWaitingInbound(int v) {
        
        waitingInbound.add(v);
    }
    
    
    /**
    popPassenger method
    Called when any passenger has left this station.
    */
    public void popPassenger() {
        
        passengers.remove(0);
    }
    
    
    /**
    popTransfer method
    Called when any transferring passenger has left this station.
    */
    public void popTransfer() {
        
        transfers.remove(0);
    }
    
    
    /**
    popDriver method
    Called when the first driver in line has left this station.
    */
    public void popDriver() {
        
        drivers.remove(0);
    }
    
    
    /**
    popWaitingOutbound method
    Called when the first outbound waiting vehicle in line has been able to
    make it to the station - removes them from the waitlist.
    */
    public void popWaitingOutbound() {
        
        waitingOutbound.remove(0);
    }
    
    
    /**
    popWaitingInbound method
    Called when the first inbound waiting vehicle in line has been able to
    make it to the station - removes them from the waitlist.
    */
    public void popWaitingInbound() {
        
        waitingInbound.remove(0);
    }
    
    
    /**
    setIsOriginOrTerminus method
    @param is - the boolean flag of whether station is origin or terminus
    */
    public void setIsOriginOrTerminus(boolean is) {
        
        isOriginOrTerminus = is;
    }
    
        
    /**
    addNumberOfVehicles method
    @param int inOrOutBound: 0 if outbound, 1 if inbound
    @param int n: number of vehicles to add
    */
    public void addNumberOfVehicles(int inOrOutBound, int n) {
        
        numberOfVehicles[inOrOutBound] += n;
    }
    
        
    /**
    subtractNumberOfVehicles method
    @param int inOrOutBound: 0 if outbound, 1 if inbound
    @param int n: number of vehicles to subtract
    */
    public void subtractNumberOfVehicles(int inOrOutBound, int n) {
        
        numberOfVehicles[inOrOutBound] -= n;
    }
    
        
}


class Vehicle {

    private enum Direction {INBOUND, OUTBOUND}

    private int idNumber;           // The identification number of the vehicle
    private int stopID;             // The stop ID of where the vehicle is currently located
    private int stopIndex;          // Counter to keep track of stops made
    private Direction vehicleDir;   // Direction of the vehicle
    private String[][] routeList;            // List of stations where the vehicle stops
    
    private int numOfCoaches;   // Number of coaches for a vehicle
    private int maxCapacity;    // Maximum number of passengers a vehicle can hold
    
    private int passengerCount;
    private boolean full;
    private ArrayList<Person> passengerList; // List of passenger aboard the vehicle
    private Person operator;                 // The driver operating the vehicle.
    
    /**
     * No-Arg Constructor
     */
    public Vehicle() {
        idNumber = 0;
        stopID = 0;
        stopIndex = 0;
        vehicleDir = Direction.INBOUND;
        routeList = new String[0][0];
        
        numOfCoaches = 0;
        maxCapacity = 0;
        
        passengerCount = 0;
        full = false;
        passengerList = null;
        operator = null;
    }
    
    /**
     * One-Argument Constructor
     * @param route The route that the object will follow.
     */
    public Vehicle(String[][] route) {
        idNumber = generateRandIDNum();
        stopID = Integer.parseInt(route[0][1]);
        stopIndex = 0;
        vehicleDir = Direction.INBOUND;
        routeList = route;
        
        numOfCoaches = generateRandCoachNum();
        maxCapacity = numOfCoaches * 20;
        
        passengerCount = 0;
        full = false;
        passengerList = new ArrayList<Person>(maxCapacity);
        operator = null; // Needs to be changed to an actual driver later.
    }
    
    /**
     * The generateRandIDNum generates and returns a random number ranging
     * from 1 to 999.
     */
    private int generateRandIDNum() {
        Random randGenerator = new Random();
        int randNum = randGenerator.nextInt(998) + 1;
        return randNum;
    }
    
    /**
     * The generateRandCoachNum generates a random number from 1 to 2.
     */
    private int generateRandCoachNum() {
        Random randGenerator = new Random();
        int randNum = randGenerator.nextInt(2) + 1;
        return randNum;
    }
    
    /**
     * The getIDNumber method returns the object's ID number.
     * @return An integer value of the object's ID number.
     */
    public int getIDNumber() {
        return idNumber;
    }
    
    /**
     * The getStopID method returns the object's the id of the current stop.
     * @return An integer value of the object's current stop id.
     */
    public int getStopID() {
        return stopID;
    }
    
    /**
     * The getDirection method retursn the object's current direction.
     * @return The Direction type of the object (INBOUND or OUTBOUND)
     */
    public Direction getDirection() {
        return vehicleDir;
    }
    
    /**
     * The getNumOfCoaches method returns the object's number of coaches.
     * @return An integer value of the number of coaches the object has.
     */
    public int getNumOfCoaches() {
        return numOfCoaches;
    }
    
    /**
     * The getMaxCapacity method returns the object's max capacity of passengers.
     * @return An integer value of the object's maximum capacity.
     */
    public int getMaxCapacity() {
        return maxCapacity;
    }
    
    /**
     * The getNumOfPassengers method returns the number of passengers in the vehicle.
     * @return An integer value of the current of number of passengers in the vehicle.
     */
    public int getNumOfPassengers() {
        return passengerList.size();
    }
    
    /**
     * The getPassengerCount method returns the number of passengers in the vehicle.
     * @return The number of passengers in the vehicle.
     */
    public int getPassengerCount() {
        return passengerCount;
    }
    
    /**
     * The getPassengerList method returns an array of the passengers' name
     * in each element.
     * @return list An array of the passengers' name. 
     */
    /* Obsolete method
    public Person[] getPassengerList() {
        Person[] list = new Person[passengerList.size()];
    
        for (int i = 0; i < list.length; i++)
            list[i] = passengerList.get(i);
            
        return list;
    }*/
    
    
    /**
     * The addPassenger methods adds a passenger to the vehicle.
     * @param passenger A Person object.
     */
    public void addPassenger(Person passenger) {
        if (getPassengerCount() == getMaxCapacity()) {
            System.out.println("The vehicle is full!");
        }
        else
            passengerList.add(passenger);
            passengerCount++;
    }
    
    /**
     * The removePassenger method removes a passenger from the vehicle.
     * @param passenger A Person object.
     */
    public void removePassenger(Person passenger) {
            passengerList.remove(passenger);
            passengerCount--;
    }
    
    /**
     * The isFull method checks if the object has reached its max count for passengers.
     * @return full A boolean value indicating if the object reaches its max capacity.
     */
    public boolean isFull() {
        if (passengerCount == maxCapacity)
            full = true;
        else
            full = false;
        return full;
    }
    
    /**
     * The goToNextStop method moves the vehicle to each stop listed in the route.
     * Once an origin or terminus is reached, the vehicle changes direction.
     */
    public void goToNextStop() {
        // If vehicle is INBOUND, move forward in the array.
        if (vehicleDir.equals(Direction.INBOUND)) {
            if (stopIndex < routeList.length - 1) {
                stopIndex++;    
            }
            else {
                switchDirection();
                stopIndex--;
            }
        }
        
        // If vehicle is OUTBOUND, move backwards in the array.
        else if (vehicleDir.equals(Direction.OUTBOUND)) {
            if (stopIndex != 0) {
                stopIndex--;
            }
            else {
                switchDirection();
                stopIndex++;
            }
        }
        
        // Store the current station's stop ID.
        stopID = Integer.parseInt(routeList[stopIndex][1]);    
    }
    
    /**
     * The switchDirection method changes the vehicle's direction.
     */
    private void switchDirection() {
        if (vehicleDir.equals(Direction.INBOUND))
            vehicleDir = Direction.OUTBOUND;
        else if (vehicleDir.equals(Direction.OUTBOUND))
            vehicleDir = Direction.INBOUND;
    }

}

class Bus extends Vehicle {

    private enum Type {BUS}
    private enum RouteName {B47, B49, B8X}
    
    Type vType;
    RouteName vRouteName;

    /**
     * No-Arg Construtor
     */
    public Bus() {
        super();
        vType = null;
        vRouteName = null;
    }
    
    public Bus(String[][] route, int bNum) {
        super(route);   // Calls the superclass's constructor
        vType = Type.BUS;
        
        if (bNum == 8)
            vRouteName = RouteName.B8X;
        else if (bNum == 47)
            vRouteName = RouteName.B47;
        else if (bNum == 49)
            vRouteName = RouteName.B49;
    }
    
    /**
     * The getType method returns the type of the vehicle.
     * @return vType The type of the vehicle.
     */
    public Type getType() {
        return vType;
    }
    
    /**
     * The getRouteName method returns the name of the route the vehicle is following.
     * @return vRouteName The name of the route.
     */
    public RouteName getRouteName() {
        return vRouteName;
    }
}

class LRV extends Vehicle {

    private enum Type {LRV}
    private enum RouteName {KT, L, N}
    
    Type vType;
    RouteName vRouteName;

    /**
     * No-Arg Construtor
     */
    public LRV() {
        super();
        vType = null;
        vRouteName = null;
    }
    
    public LRV(String[][] route, char cName) {
        super(route);   // Calls the superclass's constructor
        vType = Type.LRV;
        
        if (cName == 'K')
            vRouteName = RouteName.KT;
        else if (cName == 'L')
            vRouteName = RouteName.L;
        else if (cName == 'N')
            vRouteName = RouteName.N;
    }
    
    /**
     * The getType method returns the type of the vehicle.
     * @return vType The type of the vehicle.
     */
    public Type getType() {
        return vType;
    }
    
    /**
     * The getRouteName method returns the name of the route the vehicle is following.
     * @return vRouteName The name of the route.
     */
    public RouteName getRouteName() {
        return vRouteName;
    }
}

class Person {

    private int currentStationID;	//Store string retrieved from vehicle class
    private int currentVehicleID;	
    private int startID;				//Initializes when an instance of Person in created
    private int stopID;
    private String name;
    private String personType;			//Later on when we need to transfer/ get off... might need (could use enums, dont know how)
    private boolean reachedDestination = false;
    private boolean needToTransfer = false;
    private boolean amIAtAStation = true;
    private boolean amIOnAVehicle = false;
    private ArrayList<Integer> routePlan;   // Holds the stopIDs of when to get off
    private ArrayList<String> vehiclePlan; // Holds which vehicles to board
	
	/**
	 * No arg constructor
	 */
	public Person(){

		startID = 0;
		stopID = 0;
		String name = null;
		System.out.println("Error, please enter name of person.");
	}
	
	/**
	 * Constructor that creates individual person classes.
	 * @param nameTag Assign to name.
	 * @param beginPos Assign to start ID.
	 * @param endPos Assign to Stop ID.
	 * @param typeOfPerson Assign to personType.
	 */
	public Person(String nameTag, int beginPos, int endPos, String typeOfPerson){
		
		setStartID(beginPos);
		setStopID(endPos);
		setName(nameTag);
		personType = typeOfPerson;
        routePlan = new ArrayList<Integer>();
        vehiclePlan = new ArrayList<String>();
        createRoutePlan(); // Calls a method to fill routePlan with data
        
	}
	
	public boolean decisionGetOffVehicle(){
		
		boolean getOff = false;
		
		if(getStopID() == getCurrentStationID()){
			getOff = true;
		}
		
		return getOff;
	}
		
	public void setStartID(int startPos){
		
		startID = startPos;
	}
	
	public void setStopID(int endPos){
		
		stopID = endPos;
	}
	
	public void setName(String nameTag){
		
		name = nameTag;
	}
	
	public int getStartID(){
		
		return startID;
	}
	
	public int getStopID(){
		
		return stopID;
	}
	
	public String getName(){
		
		return name;
	}
	
	public void setCurrentStationID(int stationID){
		
		//Needs to access vehicle class, get station ("location") id.
		currentStationID = stationID;
	}
	
	public void setCurrentVehicleID(int vehicleID){
		//Needs to access vehicle class, get vehicle id.
		currentVehicleID = vehicleID;
	}
	
	public int getCurrentStationID(){
		
		return currentStationID;
	}
	
	public int getCurrentVehicleID(){
		
		return currentVehicleID;
	}
	
	public String getPersonType(){
		
		return personType;
	}
	
	public void changePersonStatusStationAndVehicle(){
		
		amIOnAVehicle = true;
		amIAtAStation = false;
	}
    
    private void createRoutePlan() {
        ArrayList<String> startVehicle = new ArrayList<String>();
        ArrayList<String> stopVehicle = new ArrayList<String>();
        
        private static String[][] 8xBayshoreRoute = SFMTASimulation.createRouteArray("8xBayshore.csv");
        private static String[][] 47VanNessRoute = SFMTASimulation.createRouteArray("47VanNess.csv");
        private static String[][] 49MissionRoute = SFMTASimulation.createRouteArray("49Mission.csv");
        private static String[][] LTaravalRoute = SFMTASimulation.createRouteArray("LTaraval.csv");
        private static String[][] NJudahRoute = SFMTASimulation.createRouteArray("NJudah.csv");
        private static String[][] KTRoute = SFMTASimulation.createRouteArray("KIngleside.csv", "TThird.csv");
        
        // Searches through all the routes and finds routes that passes through
        // the object's startID and stopID.
        for (int i = 0; i < SFMTASimulation.getRouteInfo("8X").length; i++) {
            if (startID == Integer.parseInt(SFMTASimulation.getRouteInfo("8X")[i][1])) {
                startVehicle.add("8X");
            }
            
            //Check All Routes for Stop ID.
            
            if(i < 8xBayshoreRoute[0].length){
            	if (stopID == Integer.parseInt(SFMTASimulation.getRouteInfo("8X")[i][1])) {
           	    stopVehicle.add("8X");
                }
            }   
            
            if(i < 47VanNessRoute[0].length){
                if (stopID == Integer.parseInt(SFMTASimulation.getRouteInfo("47")[i][1])) {
                    stopVehicle.add("47");
                }
            }   
            if(i < 49MissionRoute[0].length){
                if (stopID == Integer.parseInt(SFMTASimulation.getRouteInfo("49")[i][1])) {
                    stopVehicle.add("49");
            }
            if(i < LTaravalRoute[0].length){
                if (stopID == Integer.parseInt(SFMTASimulation.getRouteInfo("L")[i][1])) {
                    stopVehicle.add("L");
                }
            }
            if(i < NJudahRoute[0].length){
                if (stopID == Integer.parseInt(SFMTASimulation.getRouteInfo("N")[i][1])) {
                    stopVehicle.add("N");
                }
            }
            if(i < KTRoute[0].length){
                if (stopID == Integer.parseInt(SFMTASimulation.getRouteInfo("KT")[i][1])) {
                   stopVehicle.add("KT");
                }
            }    
        }
        for (int i = 0; i < SFMTASimulation.getRouteInfo("47").length; i++) {
            if (startID == Integer.parseInt(SFMTASimulation.getRouteInfo("47")[i][1])) {
                startVehicle.add("47");
            }
            
             //Check All Routes for Stop ID.
            
            if(i < 8xBayshoreRoute[0].length){
            	if (stopID == Integer.parseInt(SFMTASimulation.getRouteInfo("8X")[i][1])) {
           	    stopVehicle.add("8X");
                }
            }   
            
            if(i < 47VanNessRoute[0].length){
                if (stopID == Integer.parseInt(SFMTASimulation.getRouteInfo("47")[i][1])) {
                    stopVehicle.add("47");
                }
            }   
            if(i < 49MissionRoute[0].length){
                if (stopID == Integer.parseInt(SFMTASimulation.getRouteInfo("49")[i][1])) {
                    stopVehicle.add("49");
            }
            if(i < LTaravalRoute[0].length){
                if (stopID == Integer.parseInt(SFMTASimulation.getRouteInfo("L")[i][1])) {
                    stopVehicle.add("L");
                }
            }
            if(i < NJudahRoute[0].length){
                if (stopID == Integer.parseInt(SFMTASimulation.getRouteInfo("N")[i][1])) {
                    stopVehicle.add("N");
                }
            }
            if(i < KTRoute[0].length){
                if (stopID == Integer.parseInt(SFMTASimulation.getRouteInfo("KT")[i][1])) {
                   stopVehicle.add("KT");
                }
            } 
        }
        for (int i = 0; i < SFMTASimulation.getRouteInfo("49").length; i++) {
            if (startID == Integer.parseInt(SFMTASimulation.getRouteInfo("49")[i][1])) {
                startVehicle.add("49");
            }
            
             //Check All Routes for Stop ID.
            
            if(i < 8xBayshoreRoute[0].length){
            	if (stopID == Integer.parseInt(SFMTASimulation.getRouteInfo("8X")[i][1])) {
           	    stopVehicle.add("8X");
                }
            }   
            
            if(i < 47VanNessRoute[0].length){
                if (stopID == Integer.parseInt(SFMTASimulation.getRouteInfo("47")[i][1])) {
                    stopVehicle.add("47");
                }
            }   
            if(i < 49MissionRoute[0].length){
                if (stopID == Integer.parseInt(SFMTASimulation.getRouteInfo("49")[i][1])) {
                    stopVehicle.add("49");
            }
            if(i < LTaravalRoute[0].length){
                if (stopID == Integer.parseInt(SFMTASimulation.getRouteInfo("L")[i][1])) {
                    stopVehicle.add("L");
                }
            }
            if(i < NJudahRoute[0].length){
                if (stopID == Integer.parseInt(SFMTASimulation.getRouteInfo("N")[i][1])) {
                    stopVehicle.add("N");
                }
            }
            if(i < KTRoute[0].length){
                if (stopID == Integer.parseInt(SFMTASimulation.getRouteInfo("KT")[i][1])) {
                   stopVehicle.add("KT");
                }
            } 
        }
        for (int i = 0; i < SFMTASimulation.getRouteInfo("L").length; i++) {
            if (startID == Integer.parseInt(SFMTASimulation.getRouteInfo("L")[i][1])) {
                startVehicle.add("L");
            }
            
             //Check All Routes for Stop ID.
            
            if(i < 8xBayshoreRoute[0].length){
            	if (stopID == Integer.parseInt(SFMTASimulation.getRouteInfo("8X")[i][1])) {
           	    stopVehicle.add("8X");
                }
            }   
            
            if(i < 47VanNessRoute[0].length){
                if (stopID == Integer.parseInt(SFMTASimulation.getRouteInfo("47")[i][1])) {
                    stopVehicle.add("47");
                }
            }   
            if(i < 49MissionRoute[0].length){
                if (stopID == Integer.parseInt(SFMTASimulation.getRouteInfo("49")[i][1])) {
                    stopVehicle.add("49");
            }
            if(i < LTaravalRoute[0].length){
                if (stopID == Integer.parseInt(SFMTASimulation.getRouteInfo("L")[i][1])) {
                    stopVehicle.add("L");
                }
            }
            if(i < NJudahRoute[0].length){
                if (stopID == Integer.parseInt(SFMTASimulation.getRouteInfo("N")[i][1])) {
                    stopVehicle.add("N");
                }
            }
            if(i < KTRoute[0].length){
                if (stopID == Integer.parseInt(SFMTASimulation.getRouteInfo("KT")[i][1])) {
                   stopVehicle.add("KT");
                }
            } 
        }
        for (int i = 0; i < SFMTASimulation.getRouteInfo("N").length; i++) {
            if (startID == Integer.parseInt(SFMTASimulation.getRouteInfo("N")[i][1])) {
                startVehicle.add("N");
            }
           
             //Check All Routes for Stop ID.
            
            if(i < 8xBayshoreRoute[0].length){
            	if (stopID == Integer.parseInt(SFMTASimulation.getRouteInfo("8X")[i][1])) {
           	    stopVehicle.add("8X");
                }
            }   
            
            if(i < 47VanNessRoute[0].length){
                if (stopID == Integer.parseInt(SFMTASimulation.getRouteInfo("47")[i][1])) {
                    stopVehicle.add("47");
                }
            }   
            if(i < 49MissionRoute[0].length){
                if (stopID == Integer.parseInt(SFMTASimulation.getRouteInfo("49")[i][1])) {
                    stopVehicle.add("49");
            }
            if(i < LTaravalRoute[0].length){
                if (stopID == Integer.parseInt(SFMTASimulation.getRouteInfo("L")[i][1])) {
                    stopVehicle.add("L");
                }
            }
            if(i < NJudahRoute[0].length){
                if (stopID == Integer.parseInt(SFMTASimulation.getRouteInfo("N")[i][1])) {
                    stopVehicle.add("N");
                }
            }
            if(i < KTRoute[0].length){
                if (stopID == Integer.parseInt(SFMTASimulation.getRouteInfo("KT")[i][1])) {
                   stopVehicle.add("KT");
                }
            } 
        }
        for (int i = 0; i < SFMTASimulation.getRouteInfo("KT").length; i++) {
            if (startID == Integer.parseInt(SFMTASimulation.getRouteInfo("KT")[i][1])) {
                startVehicle.add("KT");
            }
           
             //Check All Routes for Stop ID.
            
            if(i < 8xBayshoreRoute[0].length){
            	if (stopID == Integer.parseInt(SFMTASimulation.getRouteInfo("8X")[i][1])) {
           	    stopVehicle.add("8X");
                }
            }   
            
            if(i < 47VanNessRoute[0].length){
                if (stopID == Integer.parseInt(SFMTASimulation.getRouteInfo("47")[i][1])) {
                    stopVehicle.add("47");
                }
            }   
            if(i < 49MissionRoute[0].length){
                if (stopID == Integer.parseInt(SFMTASimulation.getRouteInfo("49")[i][1])) {
                    stopVehicle.add("49");
            }
            if(i < LTaravalRoute[0].length){
                if (stopID == Integer.parseInt(SFMTASimulation.getRouteInfo("L")[i][1])) {
                    stopVehicle.add("L");
                }
            }
            if(i < NJudahRoute[0].length){
                if (stopID == Integer.parseInt(SFMTASimulation.getRouteInfo("N")[i][1])) {
                    stopVehicle.add("N");
                }
            }
            if(i < KTRoute[0].length){
                if (stopID == Integer.parseInt(SFMTASimulation.getRouteInfo("KT")[i][1])) {
                   stopVehicle.add("KT");
                }
            } 
        }
        
        // Compares the list of routes that contain the passenger's startID and stopID.
        // This loop checks if the startID and the stopID are in the same route.
        // If there are, no tranfers are needed.
        for (int i = 0; i < startVehicle.size(); i++) {
            for (int j = 0; j < stopVehicle.size(); j++) {
                if (startVehicle.get(i).equals(stopVehicle.get(j))) {
                    routePlan.add(stopID);
                    vehiclePlan.add(stopVehicle.get(j));
                    return; // exit methods
                }
            }
        }
        
        // Incomplete: doesn't create a route plan for passengers who need to transfer between different routes.
    }
}

