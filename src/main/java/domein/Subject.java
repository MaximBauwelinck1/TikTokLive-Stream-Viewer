package domein;

public interface Subject {
    public void addDonationObserver(DonationObersver ob);
    public void removeDonationObserver(DonationObersver ob);
}
