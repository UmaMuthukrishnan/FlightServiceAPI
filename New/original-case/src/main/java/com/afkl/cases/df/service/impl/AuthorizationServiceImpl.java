package com.afkl.cases.df.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.UnauthorizedClientException;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.stereotype.Service;

import com.afkl.cases.df.service.AuthorizationService;

import lombok.extern.slf4j.Slf4j;
@Configuration
@Service
@PropertySource("classpath:app.properties")
@EnableOAuth2Client
@Slf4j
public class AuthorizationServiceImpl implements AuthorizationService{
	@Value("${service.clientID}")
	private String clientID;
	@Value("${service.password}")
	private String password;
	@Value("${service.credentials}")
	private String grantType;
	@Value("${service.accessToken}")
	private String token;

	@Override
	public String getAccessToken() {
		final ClientCredentialsResourceDetails resourceDetails = new ClientCredentialsResourceDetails();
		resourceDetails.setClientId(clientID);
		resourceDetails.setClientSecret(password);
		resourceDetails.setGrantType(grantType);
		resourceDetails.setAccessTokenUri(token);
		String accessToken = null;
		try {
			final OAuth2RestTemplate oAuth2RestTemplate = new OAuth2RestTemplate(resourceDetails);
			final OAuth2AccessToken oAuth2AccessToken = oAuth2RestTemplate.getAccessToken();
			accessToken = oAuth2AccessToken.getValue();

		} catch (UnauthorizedClientException ex) {
			log.error("Exception in getting accessToken " + ex.getOAuth2ErrorCode());
		}
		return accessToken;
	}

	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}

}
