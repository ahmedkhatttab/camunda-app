package com.camunda.wf.camundaapp.security;


import com.nimbusds.jwt.JWTClaimNames;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class JwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {
    private final JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
    @Value("${spring.jwt.auth.converter.principle-attr}")
    private String PRINCIPLE_CLAIM_NAME;

    @Value("${spring.jwt.auth.converter.resource-id}")
    private String RESOURCE_ID;

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {

        // Extract GrantedAuthoritys from the given Jwt. (get requested scopes)
        Collection<GrantedAuthority> jwt_authorities = jwtGrantedAuthoritiesConverter.convert(jwt);

        Collection<? extends GrantedAuthority> authorities = extractResourceRoles(jwt);

        // 3rd: principle claim name
        return new JwtAuthenticationToken(jwt, authorities, getPrincipleClaimName(jwt));
    }


    private Collection<? extends GrantedAuthority> extractResourceRoles(Jwt jwt){
        Map<String, Object> resourceAccess = jwt.getClaim("resource_access");
        Map<String, Object> resource;
        Collection<String> resourceRoles;

        // contains all claims for all clients
        if(resourceAccess == null){
            return Set.of();
        }

        // roles related to our app/resource
        resource = (Map<String, Object>) resourceAccess.get(RESOURCE_ID);
        if(resource == null){
            return Set.of();
        }

        // extract roles
        resourceRoles = (Collection<String>) resource.get("roles");

        // extract role and add "ROLE_" prefix to each role
        return resourceRoles.stream().map(role-> {
                if(!role.startsWith("ROLE_")){
                    role="ROLE_"+role;
                }
                return new SimpleGrantedAuthority(role);
            })
            .collect(Collectors.toSet());
    }

    private String getPrincipleClaimName(Jwt jwt){
        String claimName = JWTClaimNames.SUBJECT;
        if(PRINCIPLE_CLAIM_NAME != null){
            claimName = PRINCIPLE_CLAIM_NAME;
        }
        return jwt.getClaim(claimName);
    }
}
