import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import play.GlobalSettings;
import play.Application;

public class Global extends GlobalSettings {

    private ApplicationContext ctx;

    @Override
    public void onStart(Application app) {
        System.out.println("Context started");
        ctx = new ClassPathXmlApplicationContext("context/components.xml");

    }

    @Override
    public <A> A getControllerInstance(Class<A> clazz) {
        System.out.println("getControllerInstance for : "+clazz.getName()+"of type"+clazz.getTypeName());
        return ctx.getBean(clazz);
    }

}
