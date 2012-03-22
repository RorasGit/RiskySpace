package battledemo;

public class Destroyer extends Ship {
	
	public Destroyer() {
		super(11, 3, 80, 2);
		this.shipType = ShipType.DESTROYER;
		this.attacks = 3;
	}

}
