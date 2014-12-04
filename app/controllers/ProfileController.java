package controllers;

import akka.actor.ActorRef;
import akka.pattern.Patterns;
import com.fasterxml.jackson.databind.JsonNode;
import com.rogers.sample.AppConfig;
import com.rogers.sample.profile.SelfRegistrationRequestMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import play.Logger;

import play.data.Form;
import play.libs.F.Promise;
import play.mvc.Controller;
import play.mvc.Result;
import scala.concurrent.Future;

import java.net.UnknownHostException;
import java.util.Map;

import static play.data.Form.form;


@org.springframework.stereotype.Controller
public class ProfileController extends Controller {

    private static final Logger.ALogger logger = Logger.of(ProfileController.class);
    private final AppConfig config;
    private final ActorRef regServiceActor;

    @Autowired
    public ProfileController(AppConfig config, @Qualifier("regServiceActor") ActorRef regServiceActor) {
        this.config = config;
        this.regServiceActor = regServiceActor;
    }

    /**
     * @return
     * @throws UnknownHostException
     */
    public Promise<Result> register() throws UnknownHostException {

        Form<SelfRegistrationRequestMessage> registerRequestForm = form(SelfRegistrationRequestMessage.class);

        Map<String, String> data = Form.form().bindFromRequest().data();
        Form<SelfRegistrationRequestMessage> form = registerRequestForm.bind(data);

        if(form.hasErrors()){
            return Promise.pure(status(400, form.errorsAsJson()));
        } else {
            SelfRegistrationRequestMessage request = form.get();
            try{
                Future<Object> future = Patterns.ask(regServiceActor, request, config.getTimeout());
                Promise<Object> promise = Promise.wrap(future);
                System.out.println(promise.toString());
                return promise.<Result> map(response -> ok(response.toString()));

            } catch (Exception e) {
                return Promise.pure(status(503));
            }
        }
    }

}

