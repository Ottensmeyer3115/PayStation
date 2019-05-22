package paystation.domain;

public class SpotManagerImpl implements SpotManagerInterface {

	int totalSpots;
	int occupiedSpots;
	
	public SpotManagerImpl(int totalSpots){
		this.totalSpots = totalSpots;
	}
	
	@Override
	public int getVacantSpots() {
		return this.totalSpots - this.occupiedSpots;
	}
	
	@Override
	public void reserveSpot(int timeLimit){
		occupiedSpots++;
		/**
		 * TODO
		 * At some point we should track timeLimit
		 * and expire reserved spots
		 */
	}

}
