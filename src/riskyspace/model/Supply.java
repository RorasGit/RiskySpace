package riskyspace.model;

public class Supply {
	private int baseSupply;
	private int usedSupply;
	private int maxSupply;
	private int queuedSupply;

	public Supply() {
		baseSupply = 3;
	}
	
	public Supply(int baseSupply) {
		this.baseSupply = baseSupply;
	}
	
	public int getUsed() {
		return usedSupply + queuedSupply;
	}
	
	public int getMax() {
		return maxSupply;
	}

	public boolean hasEnough(int supplyIncrease) {
		return getUsed() + supplyIncrease <= getMax();
	}
	
	public void update(int numberOfColonies, int usedSupply) {
		maxSupply = baseSupply + 2*numberOfColonies;
		this.usedSupply = usedSupply;
	}
	
	public boolean isCapped() {
		return getUsed() >= getMax();
	}

	public void setQueuedSupply(int queuedSupply) {
		this.queuedSupply = queuedSupply;
	}
	
	public int getQueuedSupply() {
		return queuedSupply;
	}
	
	@Override
	protected Supply clone() {
		Supply supply = new Supply();
		supply.baseSupply = this.baseSupply;
		supply.maxSupply = this.maxSupply;
		supply.usedSupply = this.usedSupply;
		supply.queuedSupply = this.queuedSupply;
		return supply;
	}
	
	@Override
	public String toString() {
		return "Supply " + (usedSupply + queuedSupply) + "/" + maxSupply;
	}
}