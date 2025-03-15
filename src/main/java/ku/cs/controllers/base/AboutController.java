package ku.cs.controllers.base;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Objects;

public class AboutController {
    @FXML protected ImageView ohmImageView;
    @FXML protected ImageView peaImageView;
    @FXML protected ImageView nonthipatImageView;
    @FXML protected ImageView bonusImageView;


    @FXML
    public void initialize(){
        System.out.println("visit: HomePage");
        //โอม
        String ohmURL = Objects.requireNonNull(getClass().getResource("/ku/cs/images/account/Ohm.jpg")).toExternalForm();
        ohmImageView.setImage(new Image(ohmURL));
        //พี
        String peeUrl = Objects.requireNonNull(getClass().getResource("/ku/cs/images/account/Surased.jpg")).toExternalForm();
        peaImageView.setImage(new Image(peeUrl));
        //โน่
        String nonthipatUrl = Objects.requireNonNull(getClass().getResource("/ku/cs/images/account/nonthipat.jpg")).toExternalForm();
        nonthipatImageView.setImage(new Image(nonthipatUrl));
        //โบนัส
        String bonusuUrl = Objects.requireNonNull(getClass().getResource("/ku/cs/images/account/Bonus.jpg")).toExternalForm();
        bonusImageView.setImage(new Image(bonusuUrl));

    }

    public void handleBackButton(ActionEvent actionEvent) {
        HomeController.clickBackButton();
    }
}
