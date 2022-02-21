package todo;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Readiness;
import org.eclipse.microprofile.metrics.MetricRegistry;
import org.eclipse.microprofile.metrics.annotation.RegistryType;
import org.jboss.logging.Logger;

@Readiness
@ApplicationScoped
public class ReadinessCheck implements HealthCheck {
  @Inject Logger logger;

  @ConfigProperty(name = "app.readiness-wait-in-seconds", defaultValue = "0")
  String waitTime;

  @Inject
  @RegistryType(type = MetricRegistry.Type.APPLICATION)
  MetricRegistry metricRegistry;

  private final Instant initializationTime = Instant.now();

  @Override
  public HealthCheckResponse call() {
    logger.info("Readiness check started.");
    this.metricRegistry.counter("app_liveness_probe").inc();
    Instant readinessTime = initializationTime.plusSeconds(Integer.parseInt(waitTime));
    if (Instant.now().isBefore(readinessTime)) {
      long secondsLeft = Instant.now().until(readinessTime, ChronoUnit.SECONDS);
      logger.info("App not ready. Please be patient for another " + secondsLeft + " seconds.");
      return HealthCheckResponse.down("Time-based readiness check.");
    } else {
      logger.info("App ready.");
      return HealthCheckResponse.up("Time-based readiness check.");
    }
  }
}
