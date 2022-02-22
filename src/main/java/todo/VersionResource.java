package todo;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@Path("/a")
public class VersionResource {
    @ConfigProperty(name = "quarkus.application.version", defaultValue = "N/A")
    String version;

    @ConfigProperty(name = "app.stage-name")
    String stageName;

    @GET
    @Path("/version")
    public String getVersion(){
        return version;
    }

    @GET
    @Path("/stage")
    public String getStage(){
        return stageName;
    }
}
