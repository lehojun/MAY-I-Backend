package ai.Mayi.oauth;

import java.util.Map;

public class KakaoUserInfo implements OAuth2UserInfo{
    private final Map<String, Object> attributes;

    public KakaoUserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public String getEmail() { return (String) attributes.get("email"); }
    public String getName() { return (String) attributes.get("name"); }
    public String getImageUrl() { return (String) attributes.get("profileimageurl"); }
}
