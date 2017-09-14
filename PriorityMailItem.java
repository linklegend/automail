package automail;

import java.util.Comparator;

public class PriorityMailItem extends MailItem{
	
	/** The priority of the mail item from 1 low to 100 high */
    private final int PRIORITY_LEVEL;
    
	public PriorityMailItem(int dest_floor, int priority_level, int arrival_time) {
		super(dest_floor, arrival_time);
        this.PRIORITY_LEVEL = priority_level;
	}
	
    /**
    *
    * @return the priority level of a mail item
    */
   public int getPriorityLevel(){
       return PRIORITY_LEVEL;
   }
   
   @Override
   public String toString(){
       return super.toString() +
               "| Priority Level: "+ PRIORITY_LEVEL
               ;
   }
   
	/**
	 * @return a Comparator for priority levels
	 */
	public static Comparator<PriorityMailItem> priorityComparator
						= new Comparator<PriorityMailItem>() {
		public int compare(PriorityMailItem m1, PriorityMailItem m2) {
			return Integer.compare(m1.PRIORITY_LEVEL, m2.PRIORITY_LEVEL);
		}		
	};
}
