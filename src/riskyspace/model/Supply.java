package riskyspace.model;

public class Supply {
	private int baseSupply;
	private int usedSupply;
	private int maxSupply;

	public Supply() {
		baseSupply = 3;
	}
	
	public Supply(int baseSupply) {
		this.baseSupply = baseSupply;
	}
	
	public void setUsed(int used) {
		usedSupply = used;
	}
	
	public int getUsed() {
		return usedSupply;
	}
	
	public int getMax() {
		return maxSupply;
	}

	public boolean hasEnough(int supplyIncrease) {
		return getUsed() + supplyIncrease <= getMax();
	}
	
	public void update(int numberOfColonies) {
		maxSupply = baseSupply + 2*numberOfColonies;
	}
	
	public boolean isCapped() {
		return getUsed() >= getMax();
	}
	
	@Override
	protected Supply clone() {
		Supply supply = new Supply();
		supply.baseSupply = this.baseSupply;
		supply.maxSupply = this.maxSupply;
		supply.usedSupply = this.usedSupply;
		return supply;
	}
	
	@Override
	public String toString() {
		return "Supply " + usedSupply + "/" + maxSupply;
	}
}