package domein;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.LinkedList;
import java.util.Queue;
import java.util.TreeMap;

import DTO.DonatieDTO;
import io.github.jwdeveloper.tiktok.data.events.gift.TikTokGiftEvent;
import io.github.jwdeveloper.tiktok.data.models.users.User;

public class DomeinController implements PropertyChangeListener {
    private static DomeinController instance;
    private static final int LENGTE_LEADERBOARD = 10;
	  private TikTokController tk;
    private Queue<Donatie> donaties;
    TreeMap<Integer, User> leaderboard = new TreeMap<>();
    public static DomeinController getInstance()
    {
    	if(instance == null)
    	{
    		instance = new DomeinController();
    	}
    	return instance;
    }
    public  DomeinController(){
       tk = new TikTokController();
       donaties = new LinkedList<>();
       addPropertyChangeListener(this);
    }

    public void setStreamerId(String id){
        tk.setStreamerID(id);
    }
    public void startStreamWachting(){
      tk.startStreaming();
    }
    public void addPropertyChangeListener(PropertyChangeListener pcl){
      tk.addPropertyChangeListener(pcl);
    }
    private void addDonatie(Donatie don){
      donaties.add(don);
    }

    public Boolean isDonatiesEmpty(){
      return donaties.peek() == null;
    }
    public DonatieDTO getDonatie(){
      Donatie d = donaties.poll();
      return  new DonatieDTO(d.getNameGift(), d.getGiftImage(), d.getFromUser(), d.getUserImage());
    }
    private void handleLeaderBoardUpdate(TikTokGiftEvent ev){
      if(leaderboard.size()<LENGTE_LEADERBOARD){
        leaderboard.put(ev.getGift().getDiamondCost(), ev.getUser());
      }else{
        if(leaderboard.values().stream().filter(user->user.getId().equals(ev.getUser().getId())).findAny().isPresent()){
          var user  =leaderboard.entrySet().stream().filter(e-> e.getValue().getId().equals(ev.getUser().getId())).findAny().get();
         leaderboard.remove(user.getKey());
         leaderboard.put(user.getKey()+ev.getGift().getDiamondCost(), ev.getUser());
        }else {
          // User is not in the leaderboard
          // Find the entry with the lowest diamond cost
          var lowestEntry = leaderboard.entrySet()
                  .stream()
                  .min((e1, e2) -> Integer.compare(e1.getKey(), e2.getKey()))
                  .orElse(null);

          if (lowestEntry != null && ev.getGift().getDiamondCost() > lowestEntry.getKey()) {
              // Replace the lowest entry if the new diamond cost is higher
              leaderboard.remove(lowestEntry.getKey());
              leaderboard.put(ev.getGift().getDiamondCost(), ev.getUser());
          }
      }
      } leaderboard.entrySet().stream().forEach(e->System.out.println(String.format("DIAMONDS: %s  USER: ", e.getKey().toString(),e.getValue().getProfileName())));
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
      if(evt.getPropertyName().equals("gift")){
        var event = (TikTokGiftEvent)evt.getNewValue();
        addDonatie(new Donatie(event.getGift().getName(), event.getGift().getPicture(), event.getUser().getProfileName(), event.getUser().getPicture(), event.getGift().getDiamondCost()));
        handleLeaderBoardUpdate(event);
      }
    }
    public void addDonationObserver(DonationObersver ob){
      tk.addDonationObserver(ob);
    }
    public void removeDonationObserver(DonationObersver ob){
      tk.removeDonationObserver(ob);
    }

}
