package riskyspace.model;

import java.io.Serializable;

public interface BuildAble extends Serializable{
	
	public int getSupplyCost();
	
	public int getMetalCost();
	
	public int getGasCost();
	
	public int getBuildTime();
}
