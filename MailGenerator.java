package automail;

import java.util.*;

import strategies.IMailPool;

/**
 * This class generates the mail
 */
public class MailGenerator {

    public final int MAIL_TO_CREATE;
    public static final int HUNDRED_PERCENT = 100;
    public static final int INITIALZERO = 0;
    public static final int INITIALONE = 1;
    public static final int TWOTIMES = 2;

    private int mailCreated;
    private static int VARIATION = Integer.parseInt(Simulation.automailProperties.getProperty("Mail_Count_Percentage_Variation"));
    private final Random random;
    /** This seed is used to make the behaviour deterministic */
    
    private boolean complete;
    public static int Priority_Mail_One;
    private IMailPool mailPool;

    private HashMap<Integer,ArrayList<MailItem>> allMail;

    /**
     * Constructor for mail generation
     * @param mailToCreate roughly how many mail items to create
     * @param mailPool where mail items go on arrival
     * @param seed random seed for generating mail
     */
    public MailGenerator(int mailToCreate, IMailPool mailPool, HashMap<Boolean,Integer> seed){
        if(seed.containsKey(true)){
        	this.random = new Random((long) seed.get(true));
        }
        else{
        	this.random = new Random();	
        }
        // Vary arriving mail by +/-20%
        MAIL_TO_CREATE = mailToCreate*(HUNDRED_PERCENT-VARIATION)/HUNDRED_PERCENT+ random.nextInt(mailToCreate*TWOTIMES*VARIATION/HUNDRED_PERCENT);
        System.out.println("Num Mail Items: "+MAIL_TO_CREATE);
        Priority_Mail_One = Integer.parseInt(Simulation.automailProperties.getProperty("Priority_Mail_is_One_in"));
        mailCreated = 0;
        complete = false;
        allMail = new HashMap<Integer,ArrayList<MailItem>>();
        
        this.mailPool = mailPool;
    }

    /**
     * @return a new mail item that needs to be delivered
     */
    private MailItem generateMail(){
        int dest_floor = generateDestinationFloor();
        int priority_level = generatePriorityLevel();
        int arrival_time = generateArrivalTime();
        // Check if arrival time has a priority mail
        if(	(random.nextInt(Priority_Mail_One) > 0) ||  // Skew towards non priority mail
        	(allMail.containsKey(arrival_time) &&
        	allMail.get(arrival_time).stream().anyMatch(e -> PriorityMailItem.class.isInstance(e)))){
        	return new MailItem(dest_floor,arrival_time);      	
        }
        else{
        	return new PriorityMailItem(dest_floor,priority_level,arrival_time);
        }   
    }

    /**
     * @return a destination floor between the ranges of GROUND_FLOOR to FLOOR
     */
    private int generateDestinationFloor(){
        return Building.LOWEST_FLOOR + random.nextInt(Building.FLOORS);
    }

    /**
     * @return a random priority level selected from 1 - 100
     */
    private int generatePriorityLevel(){
        return INITIALONE + random.nextInt(HUNDRED_PERCENT);
    }
    
    /**
     * @return a random arrival time before the last delivery time
     */
    private int generateArrivalTime(){
        return INITIALONE + random.nextInt(Clock.LAST_DELIVERY_TIME);
    }

    /**
     * This class initializes all mail and sets their corresponding values,
     */
    public void generateAllMail(){
        while(!complete){
            MailItem newMail =  generateMail();
            int timeToDeliver = newMail.getArrivalTime();
            /** Check if key exists for this time **/
            if(allMail.containsKey(timeToDeliver)){
                /** Add to existing array */
                allMail.get(timeToDeliver).add(newMail);
            }
            else{
                /** If the key doesn't exist then set a new key along with the array of MailItems to add during
                 * that time step.
                 */
                ArrayList<MailItem> newMailList = new ArrayList<MailItem>();
                newMailList.add(newMail);
                allMail.put(timeToDeliver,newMailList);
            }
            /** Mark the mail as created */
            mailCreated++;

            /** Once we have satisfied the amount of mail to create, we're done!*/
            if(mailCreated == MAIL_TO_CREATE){
                complete = true;
            }
        }

    }
    
    /**
     * While there are steps left, create a new mail item to deliver
     * @return Priority, used to notify Robot
     */
    public int step(){
    	int priority = INITIALZERO;
    	// Check if there are any mail to create
        if(this.allMail.containsKey(Clock.Time())){
            for(MailItem mailItem : allMail.get(Clock.Time())){
                mailPool.addToPool(mailItem);
                if (mailItem instanceof PriorityMailItem) priority = ((PriorityMailItem) mailItem).getPriorityLevel();
                System.out.println("T: "+Clock.Time()+" | Arrive    " + mailItem.toString());
            }
        }
        return priority;
    }
    
}
