package riskyspace.model;

import java.io.Serializable;
import java.lang.reflect.Type;

public interface BuildAble extends Serializable{
	
	public int getSupplyCost();
	
	public int getMetalCost();
	
	public int getGasCost();
	
	public int getBuildTime();
}
