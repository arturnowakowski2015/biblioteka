package pl.biblioteka.model;

public class Renew {
	private final int MAXRENEWS = 5; 
	private int renews;
	
	public Renew() {
		this.increaseRenew();
	}
    public void renew(boolean reset) {
    	if(!reset) {
	    	if(this.getMaxRenews()<this.MAXRENEWS) {
	    		this.increaseRenew(); 
	    	}
    	}
    	else this.renews=0;
    }
    private void increaseRenew() {
    	this.renews++;
    }
    private int getMaxRenews() {
    	return this.renews;
    }
}
