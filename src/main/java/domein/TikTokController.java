package domein;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import DTO.DonatieDTO;
import io.github.jwdeveloper.tiktok.TikTokLive;
import lombok.Getter;


public class TikTokController implements Subject {
     @Getter
    private String streamerID;
    private PropertyChangeSupport subject;

    private int aantalLikes= 0;
    private String roomId = "";
    private int aantalViewers = 0;
    private Set<DonationObersver> donationObservers;
    public TikTokController(){
        subject = new PropertyChangeSupport(this);
        donationObservers = new HashSet<DonationObersver>();
    }
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        subject.addPropertyChangeListener(listener);
    }   
    public void setStreamerID(String id){
        if(id == null || id.isEmpty() || id.isBlank()){
            throw new IllegalArgumentException("Id mag nier leeg zijn!");
        }
        this.streamerID = id;
    }

    private void setAantalLikes(int likes){
        if(likes <0){
            throw new IllegalArgumentException("Het aantal likes kan niet negatief zijn!");
        }
        subject.firePropertyChange("aantalLikes", this.aantalLikes, likes);
        this.aantalLikes = likes;
    }
    private void setRoomId(String id){
        if(id == null || id.isEmpty() || id.isEmpty()){
            throw new IllegalArgumentException("Room ID mag niet leeg zijn!");
        }
        subject.firePropertyChange("roomId", this.roomId, id);
        this.roomId = id;
    }
    private void setAantalViewers(int viewers){
        if(viewers <0){
            throw new IllegalArgumentException("Viewers mag niet kleiner als 0 zijn");
        } else if(viewers == 0 && this.aantalViewers - 50 >0){// om te voorkomen dat random 0 viewers het aantal update
            return;
        }
        subject.firePropertyChange("aantalViewers", this.aantalViewers, viewers);
        this.aantalViewers = viewers;
    }
    private void setRoomInfo(int likes, String RoomID, int viewers){
        setAantalLikes(likes);
        setAantalViewers(viewers);
        setRoomId(RoomID);
    }


    public void startStreaming(){
        TikTokLive.newClient(streamerID).onGift((_, event) ->{
            subject.firePropertyChange("gift", null, 
            event);
            donateEvent();
        }) .onRoomInfo((_, event) ->
        {
            var roomInfo = event.getRoomInfo();
            setRoomInfo(roomInfo.getLikesCount(),roomInfo.getRoomId(),roomInfo.getViewersCount());
        }).buildAndConnect();
    }
    @Override
    public void addDonationObserver(DonationObersver ob) {
        donationObservers.add(ob);
    }
    @Override
    public void removeDonationObserver(DonationObersver ob) {
        donationObservers.remove(ob);
    }
    private void donateEvent(){
        donationObservers.forEach(ob->ob.handleDonate());
    }
}
