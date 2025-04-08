package ai.Mayi.oauth;

import java.util.Map;

public class GoogleUserInfo implements OAuth2UserInfo{
    private Map<String, Object> attributes;

    public GoogleUserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public String getEmail() { return (String) attributes.get("email"); }
    public String getName() { return (String) attributes.get("name"); }
    public String getImageUrl() { return (String) attributes.get("picture"); }
}
