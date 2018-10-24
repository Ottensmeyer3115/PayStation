package paystation.domain;

public class DanishRateStrategy implements RateStrategy {

	@Override
	public int calculateTime(int amount) {

		int time = amount * 7;
		return time;
	}

}
