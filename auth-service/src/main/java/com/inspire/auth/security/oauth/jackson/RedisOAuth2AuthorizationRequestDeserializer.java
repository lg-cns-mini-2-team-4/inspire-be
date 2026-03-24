package com.inspire.auth.security.oauth.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

public class RedisOAuth2AuthorizationRequestDeserializer extends JsonDeserializer<OAuth2AuthorizationRequest> {
    static final TypeReference<Set<String>> STRING_SET = new TypeReference<>() {
    };
    static final TypeReference<Map<String, Object>> STRING_OBJECT_MAP = new TypeReference<>() {
    };

    @Override
    public OAuth2AuthorizationRequest deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        ObjectMapper mapper = (ObjectMapper) parser.getCodec();
        JsonNode root = mapper.readTree(parser);

        AuthorizationGrantType grantType = getGrantType(root.path("authorizationGrantType"));

        if (grantType == null) {
            throw new RuntimeException("");
        }

        OAuth2AuthorizationRequest.Builder builder = OAuth2AuthorizationRequest.authorizationCode();

        builder.authorizationUri(root.path("authorizationUri").asText(null))
                .clientId(root.path("clientId").asText(null))
                .redirectUri(root.path("redirectUri").isMissingNode() ? null : root.path("redirectUri").asText())
                .state(root.path("state").isMissingNode() ? null : root.path("state").asText())
                .authorizationRequestUri(root.path("authorizationRequestUri").isMissingNode() ? null : root.path("authorizationRequestUri").asText());

        if (root.has("scopes")) {
            builder.scopes(mapper.convertValue(root.get("scopes"), STRING_SET));
        }
        if (root.has("additionalParameters")) {
            builder.additionalParameters(mapper.convertValue(root.get("additionalParameters"), STRING_OBJECT_MAP));
        }
        if (root.has("attributes")) {
            builder.attributes(mapper.convertValue(root.get("attributes"), STRING_OBJECT_MAP));
        }

        return builder.build();
    }

    private AuthorizationGrantType getGrantType(JsonNode node) {

        if (node.isObject() && node.has("value")) {
            String value = node.get("value").asText();
            if (AuthorizationGrantType.AUTHORIZATION_CODE.getValue().equalsIgnoreCase(value)) {
                return AuthorizationGrantType.AUTHORIZATION_CODE;
            }
        }
        return null;
    }
}
