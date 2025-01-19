package domein;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import DTO.DonatieDTO;
import io.github.jwdeveloper.tiktok.data.events.gift.TikTokGiftEvent;
import io.github.jwdeveloper.tiktok.data.models.users.User;

public class DomeinController implements PropertyChangeListener {
    private static DomeinController instance;
    private static final int LENGTE_LEADERBOARD = 10;
	  private TikTokController tk;
    private Queue<Donatie> donaties;
    TreeMap<Integer, List<User>> leaderboard = new TreeMap<>();
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

    public String getTopTienVanLeaderboard(){
      StringBuilder str = new StringBuilder();
      final AtomicInteger rank = new AtomicInteger(1);
      final AtomicInteger counter = new AtomicInteger(LENGTE_LEADERBOARD);
      leaderboard.entrySet().stream().sorted((e1, e2) -> e2.getKey().compareTo(e1.getKey())).forEachOrdered(e->{e.getValue().forEach(user->{
        if(counter.get()>0){
          String naamuser = user.getProfileName();
          if (naamuser.length() > 27) {
            naamuser = naamuser.substring(0, 24) + "..."; // voeg ... toe als naam te lang is
        } else {
            naamuser = String.format("%-27s", naamuser); // Pad de naam met whitespace rechts
        }
          str.append(String.format("%d %s %d %n",rank.get(), naamuser,e.getKey()));
          counter.decrementAndGet();
        }
      });
      rank.incrementAndGet();
    });
    return str.toString();
    }
    public Boolean isDonatiesEmpty(){
      return donaties.peek() == null;
    }
    public DonatieDTO getDonatie(){
      Donatie d = donaties.poll();
      return  new DonatieDTO(d.getNameGift(), d.getGiftImage(), d.getFromUser(), d.getUserImage());
    }
    private void handleLeaderBoardUpdate(TikTokGiftEvent ev){
      if (leaderboard.values().stream().flatMap(List::stream).map(User::getId).collect(Collectors.toList()).contains(ev.getUser().getId())){ // in case somebody donated who is already on the leaderboard
        System.out.println("HEEFT AL GEDONEERD!!");
        System.out.println(ev.getUser().getProfileName());
        System.out.println("HEEFT AL GEDONEERD!!");
        System.out.println("HEEFT AL GEDONEERD!!");
        int rank = leaderboard.entrySet().stream().filter(e->{
         // e.getValue().stream().flatMap(List::stream).map(User::getId).collect(Collectors.toList()).contains(ev.getUser().getId());
          if(e.getValue().stream().map(User::getId).collect(Collectors.toList()).contains(ev.getUser().getId())){
            return true;
          } else return false;
        }).findAny().get().getKey();
        leaderboard.get(rank).removeIf(user -> user.getId().equals(ev.getUser().getId()));

        if(leaderboard.get(ev.getGift().getDiamondCost()+rank) == null){
          List<User> lijst = new ArrayList<User>();
          lijst.add(ev.getUser());
          leaderboard.put(ev.getGift().getDiamondCost()+rank, lijst);
        }else{
          leaderboard.get(ev.getGift().getDiamondCost()+rank).add(ev.getUser());
        }
        if (leaderboard.get(rank).isEmpty()) { // in case rank is empty 
            leaderboard.remove(rank); // TODO dit veroorzaakt misch bug?
        } 
      } else if( leaderboard.get(ev.getGift().getDiamondCost() ) == null){ // leaderboard is not full yet and postion is empty so first user with this rank
        List<User> lijst = new ArrayList<User>();
        lijst.add(ev.getUser());
        leaderboard.put(ev.getGift().getDiamondCost(), lijst);
      } else if( leaderboard.get(ev.getGift().getDiamondCost() ) != null){ // leaderboard is not full yet but this postion ha been filled ye
        leaderboard.get(ev.getGift().getDiamondCost()).add(ev.getUser());
      }
      // TODO in geval leaderboard vol is kijken of diamonds hoger is dan laagste
      // indien ja plaats op correcte plaats
      // indien nee ... o fuck je moet sws leaderbord van alles bijhouden maar alleen de top 10 tonen
      // nieuw plan. kijk of gebruiker al bestaat dan verwijder je de gebruiker uit de lijst(delete lijst met rank indien leeg) en voeg je op nieuwe plaats toe
      // indien eerste donatie voeg op een nieuwe rank toe of op een bestaande plaats
      // toon alleen top 10
      // indien meerdere per rank dan toon je bv zo
      // 1 user22 100 diamonds
      // 2 user21 99 diamonds
      // 3 user11 90 diamonds
      // 3 user2  90 diamonds
      // 3 user221 90 diamonds
      // 4 user22 12 diamonds
      // ....









      /*if(leaderboard.size()<LENGTE_LEADERBOARD){
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
      } */tets_print_leaderboard();//leaderboard.entrySet().stream().forEach(e->System.out.println(String.format("DIAMONDS: %s  USER: ", e.getKey().toString(),e.getValue().getProfileName())));
    }

    public void tets_print_leaderboard() {
      System.out.println("********************************");
      System.out.println("START LEADERBOARD");
      System.out.println("********************************");
      final AtomicInteger rank = new AtomicInteger(1); // Use AtomicInteger for thread-safety in streams
  
      // Sort the leaderboard by diamonds (key) in descending order
      leaderboard.entrySet().stream()
          .sorted((e1, e2) -> e2.getKey().compareTo(e1.getKey())) // Sort by key (diamonds) in descending order
          .forEachOrdered((e) -> { // Use forEachOrdered to preserve order after sorting
              e.getValue().forEach(user -> { // Iterate over the list of users for each score
                  System.out.println(String.format("Rank %d: user %s met id %s en met %s diamonds", 
                      rank.get(), // Get the current rank
                      user.getProfileName(), 
                      user.getId(), 
                      e.getKey()));
              });
              rank.getAndIncrement(); // Increment the rank only after processing all users with the same score
          });
      System.out.println("********************************");
      System.out.println("END LEADERBOARD");
      System.out.println("********************************");
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
