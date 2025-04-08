package ai.Mayi.oauth;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app.oauth2")
@Getter
public class OAuth2Properties {
    private String successRedirect;
    public void setSuccessRedirect(String successRedirect) {
        this.successRedirect = successRedirect;
    }
}
