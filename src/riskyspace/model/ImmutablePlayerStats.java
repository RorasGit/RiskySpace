package riskyspace.model;

import static riskyspace.model.Resource.GAS;
import static riskyspace.model.Resource.METAL;

import java.util.List;

public class ImmutablePlayerStats extends PlayerStats {
	
	private Supply supply = null;
	
	public ImmutablePlayerStats(PlayerStats stats) {
		super();
		super.setIncome(METAL, stats.getIncome(METAL));
		super.setIncome(GAS, stats.getIncome(GAS));
		super.increaseResource(METAL, stats.getResource(METAL) - this.getResource(METAL));
		super.increaseResource(GAS, stats.getResource(GAS) - this.getResource(GAS));
		this.supply = stats.getSupply();
	}
	
	@Override
	public int getUsedSupply() {
		return supply.getUsed();
	}
	
	@Override
	public int getMaxSupply() {
		return supply.getMax();
	}
	
	@Override
	public boolean isSupplyCapped() {
		return supply.isCapped();
	}
	
	@Override
	public Supply getSupply() {
		return supply.clone();
	}
	
	@Override
	public boolean useResource(Resource resource, int amount) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void update(int numberOfColonies, int usedSupply) {
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
	public List<QueueItem> reduceBuildQueue() {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void resetQueue(Position pos) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void resetAll() {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void increaseResource(Resource metal, int i) {
		throw new UnsupportedOperationException();
	}
}