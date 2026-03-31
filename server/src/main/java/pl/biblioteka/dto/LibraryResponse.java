package pl.biblioteka.dto;

public class LibraryResponse {
    private long loanId;
    private String author; 
    private String status;
    private String createdAt;
    public String getAuthor() {
		return author;
	}
//
	public void setAuthor(String author) {
		this.author = author;
	}
 
	private String tytul;
    public String getTytul() {
		return tytul;
	}

	public void setTytul(String tytul) {
		this.tytul = tytul;
	}
 
    public long getLoanId() {
		return loanId;
	}

	public void setLoanId(long loanId) {
		this.loanId = loanId;
	}

	public String getStatus() {
		return status;
	}

	@Override
	public String toString() {
		return "LibraryResponse [loanId=" + loanId + ", author=" + author + ", status=" + status + ", createdAt="
				+ createdAt + ", tytul=" + tytul + ", dueDate=" + dueDate + ", label=" + label + "]";
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public String getDueDate() {
		return dueDate;
	}

	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	private String dueDate;
    private String label = "Termin zwrotu";

    // Konstruktor, Gettery i Settery (ważne dla Jacksona!)
    public LibraryResponse(long loanId,String author, String tytul, String status, String createdAt, String dueDate) {
        this.loanId = loanId;
        this.author=author;
        this.tytul=tytul;
        this.status = status;
        this.createdAt = createdAt;
        this.dueDate = dueDate;
    } 
    
    
    // ... gettery i settery ...
}
