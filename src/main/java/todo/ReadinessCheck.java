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
    metricRegistry.counter("app_readiness_probe").inc();
    if (isReady()) {
      return HealthCheckResponse.up("Time-based readiness check.");
    } else {
      return HealthCheckResponse.down("Time-based readiness check.");
    }
  }

  private boolean isReady() {
    Instant readyTime = initializationTime.plusSeconds(Integer.parseInt(waitTime));
    if (Instant.now().isBefore(readyTime)) {
      long secondsLeft = Instant.now().until(readyTime, ChronoUnit.SECONDS);
      logger.info("App not ready. Please be patient for another " + secondsLeft + " seconds.");
      return false;
    } else {
      logger.info("App ready.");
      return true;
    }
  }
}
