package pl.biblioteka.model.fine;

/**
 * Simple DTO that describes the configured fine policy.
 */
public class FinePolicy {
    private int graceDays; //when not to count
    private long centsPerDay;
    private long capCents;//max for fine
    private boolean perDay;

    public FinePolicy() {}
    public FinePolicy(int grace, long cpd) {
    	this.graceDays=grace;
    	this.centsPerDay=cpd;
    	this.capCents=3000000;
    	this.perDay=true;
    }

    public int getGraceDays() { return graceDays; }
    public void setGraceDays(int graceDays) { this.graceDays = graceDays; }

    public long getCentsPerDay() { return centsPerDay; }
    public void setCentsPerDay(long centsPerDay) { this.centsPerDay = centsPerDay; }

    public long getCapCents() { return capCents; }
    public void setCapCents(long capCents) { this.capCents = capCents; }

    public boolean isPerDay() { return perDay; }
    public void setPerDay(boolean perDay) { this.perDay = perDay; }
}
