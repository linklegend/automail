package automail;

import java.util.Observable;
import java.util.Observer;

import strategies.IMailPool;
import strategies.IRobotBehaviour;

public class CommsRobot extends Robot implements Observer {

	public CommsRobot(IRobotBehaviour behaviour, IMailDelivery delivery, IMailPool mailPool, int carryingCapacity, Observable commsStation) {
		super(behaviour, delivery, mailPool, carryingCapacity);
		commsStation.addObserver(this);
		}
	
	@Override
	public void update(Observable commsStation, Object priority) {
		behaviour.priorityArrival((int) priority);		
	}

}
