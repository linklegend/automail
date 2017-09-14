package automail;

public class Building {
	
	
    /** The number of floors in the building **/
    public static int FLOORS = Integer.parseInt(Simulation.automailProperties.getProperty("Number_of_Floors"));
    
    /** Represents the ground floor location */
    public static int LOWEST_FLOOR = Integer.parseInt(Simulation.automailProperties.getProperty("Lowest_Floor"));
    
    /** Represents the mailroom location */
    public static int MAILROOM_LOCATION = Integer.parseInt(Simulation.automailProperties.getProperty("Location_of_MailRoom"));
    

}
