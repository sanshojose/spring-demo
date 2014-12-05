package com.rogers.sample;

import akka.util.Timeout;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.springframework.context.annotation.Configuration;
import scala.concurrent.duration.Duration;

import java.util.concurrent.TimeUnit;

/**
 * Simple adapter to decouple config implementation details.
 * Use this instead of the play application config so code is decoupled from play.
 */

@Configuration
public class AppConfig {

    private Config config;
    private Timeout timeout;

    /**
     * Loads application.conf by default.
     */
    public AppConfig() {
        config = ConfigFactory.load();
        timeout = new Timeout(Duration.apply(15000, TimeUnit.MILLISECONDS));
    }

    public Config getConfig(){ return config; }

    public Timeout getTimeout(){ return timeout;}
  }
