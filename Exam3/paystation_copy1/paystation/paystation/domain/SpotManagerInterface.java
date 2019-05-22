package paystation.domain;

public interface SpotManagerInterface {
	
	/**
	 * Retrieves the number of vacant spots
	*/
	public int getVacantSpots();
	
	/**
	 * Reserves a spot
	 */
	public void reserveSpot(int timeLimit);

}
