package hu.soft4d.app;

import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeType;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.info.License;
import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@SecurityScheme(
    type = SecuritySchemeType.OPENIDCONNECT,
    securitySchemeName = "openIdConnect",
    description = "Keycloak HACCP",
    openIdConnectUrl = "https//haccp:8543/realms/base/.well-known/openid-configuration"
)
@OpenAPIDefinition(
    tags = {
        @Tag(name="Info", description="Menu item api graphical user interface.")
    },
    info = @Info(
        title = "HACCP Menu item API",
        version = "v1",
        description = "Menu item api graphical user interface.",
        license = @License(
            name = "4D Soft",
            url = "https://www.4dsoft.hu"),
        contact = @Contact(
            name = "Csongor Szegleti", email = "szegleti_csongor@4dsoft.hu",
            url = "https://www.4dsoft.hu"))
)
@ApplicationScoped
@ApplicationPath("/api/v1")
public class MenuApiV1 extends Application {
}
