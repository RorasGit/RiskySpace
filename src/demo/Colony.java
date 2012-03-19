package demo;

public class Colony implements BattleAble {
	
	private Player owner = null;
	private int income = 0;
//	private int damage = 0;
//	private int shield = 0;
//	private int variation = 0;
	
	public Colony (Resource resource, Player owner) {
		this.owner = owner;
//		this.damage = 8;
//		this.shield = 30;
//		this.variation = 3;
		if (resource == Resource.METAL) {
			this.income = 40;	
		} else {
			this.income = 20;
		}
	}
	
	public void getIncome() {
		//TODO
	}

}
