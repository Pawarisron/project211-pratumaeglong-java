package ku.cs.controllers.base;

import javafx.event.ActionEvent;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class HelpController {

    public void openLink(ActionEvent actionEvent) throws URISyntaxException, IOException {
        Desktop.getDesktop().browse(new URI("https://drive.google.com/file/d/19WMiOCJTMYTvwqX4neoznkYoQeVg14R4/view?usp=sharing"));
    }
    public void handleBackButton(ActionEvent actionEvent) {
        HomeController.clickBackButton();
    }
}
