package domein;

import io.github.jwdeveloper.tiktok.data.models.Picture;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import java.awt.Image;

@Setter(AccessLevel.PRIVATE)
@Getter
public class Donatie {
    private String nameGift;
    @Getter(AccessLevel.NONE)
    private Picture giftPicture;
    private Image giftImage;
    private String fromUser;
    @Getter(AccessLevel.NONE)
    private Picture userPicture;
    private Image userImage;
    private int diamondCost;

    public Donatie(String gift, Picture giftpic, String user, Picture userpic,int diamonds){
        setNameGift(gift);
        setGiftPicture(giftpic);
        setFromUser(user);
        setUserPicture(userpic);
        setDiamondCost(diamonds);
        setGiftImage(giftpic.downloadImage());
        setUserImage(userpic.downloadImage());
    }
    
}
