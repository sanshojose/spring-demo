package controllers;

import com.rogers.sample.AppConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import play.data.Form;
import play.libs.Json;
import play.mvc.Result;
import views.html.index;

@Controller
public class Application extends play.mvc.Controller{

    private final AppConfig config;

    @Autowired
    public Application(AppConfig config) {
        this.config = config;

    }
     public Result index() {
        return play.mvc.Controller.ok("Helloooooo"+config.getConfig().getString("message.welcome.guest"));
    }

}