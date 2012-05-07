package riskyspace.model;

import java.util.List;

public class ImmutablePlayerStats extends PlayerStats {

	@Override
	public void update(int numberOfColonies, int usedSupply, int queuedSupply) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setIncome(Resource res, int newIncome) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void increaseIncome(Resource res, int additionalIncome) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void decreaseIncome(Resource res, int reducedIncome) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void gainNewResources() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean purchase(BuildAble buildAble) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean purchase(List<BuildAble> buildAbles) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void refund(BuildAble buildAble) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void refund(List<BuildAble> buildAbles) {
		throw new UnsupportedOperationException();
	}
}