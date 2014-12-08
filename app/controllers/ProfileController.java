package controllers;

import akka.actor.ActorRef;
import akka.pattern.Patterns;
import com.rogers.sample.AppConfig;
import com.rogers.sample.profile.SelfRegistrationRequestMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import play.Logger;

import play.data.Form;
import play.libs.F.Promise;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import scala.concurrent.Future;
import views.html.index;

import java.net.UnknownHostException;
import java.util.Map;

import static play.data.Form.form;

/**
 * Main controller for profile registration
 */
@org.springframework.stereotype.Controller
public class ProfileController extends Controller {

    private static final Logger.ALogger logger = Logger.of(ProfileController.class);
    private final AppConfig config;
    private final ActorRef regServiceActor;

    @Autowired
    public ProfileController(AppConfig config, @Qualifier("regServiceActor") ActorRef regServiceActor) {
        logger.info("ProfileController.ProfileController()::Instantiating");
        this.config = config;
        this.regServiceActor = regServiceActor;
    }

    public static Result index()   {
        return ok(index.render());
    }
    /**
     *
     * @return Promise<Result>
     * @throws UnknownHostException
     */
    public Promise<Result> register() throws UnknownHostException {
        logger.info("ProfileController.register()::binding form data");
        Form<SelfRegistrationRequestMessage> registerRequestForm = form(SelfRegistrationRequestMessage.class);
        logger.info(registerRequestForm.toString());
        Map<String, String> data = Form.form().bindFromRequest().data();
        logger.info(data.toString());
        Form<SelfRegistrationRequestMessage> form = registerRequestForm.bind(data);
        logger.info(form.toString());
        if(form.hasErrors()){
            logger.info("ProfileController.register()::form.hasErrors())");
            return Promise.pure(status(400, form.errorsAsJson()));
        } else {
            SelfRegistrationRequestMessage request = form.get();
            try{
                logger.info("ProfileController.register()::About to invoke regServiceActor)");
                Future<Object> future = Patterns.ask(regServiceActor, request, config.getTimeout());
                Promise<Object> promise = Promise.wrap(future);
                logger.info("ProfileController.register()::Got response");
                return promise.<Result> map(response -> ok(Json.toJson(response)));

            } catch (Exception e) {
                return Promise.pure(status(503));
            }
        }
    }

}

