package domein;

import lombok.Getter;

@Getter
public class User extends io.github.jwdeveloper.tiktok.data.models.users.User {
    private int totaalAantalDiamanten;
   public User(io.github.jwdeveloper.tiktok.messages.data.User u){
    super(u);
   }
   public void addDiamanten(int aantal){
    if(aantal<0) throw new IllegalArgumentException("Aantal diamanten kan niet negatief zijn!");
        totaalAantalDiamanten += aantal;
   }
}
