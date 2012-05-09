package riskyspace.model;

import java.io.Serializable;

public class Supply implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1183574126628825765L;
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
	
	/**
	 * Returns the amount of supply used by fleets and queue.
	 * @return Total supply in use.
	 */
	public int getUsed() {
		return usedSupply + queuedSupply;
	}
	
	/**
	 * Returns the amount of supply used by fleets.
	 * @return supply on the field
	 */
	public int fieldSupply() {
		return usedSupply;
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