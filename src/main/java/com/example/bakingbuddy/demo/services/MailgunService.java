package com.example.bakingbuddy.demo.services;

import com.example.bakingbuddy.demo.Model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.UnirestException;
import org.springframework.beans.factory.annotation.Value;


@Service("mailgunService")
public class MailgunService {

    @Value("${spring.mail.from}")
    public String from;

    @Value("${mg.domain}")
    public String mgDomain;

    @Value("${mg.apikey}")
    public String mgAPIKey;

    public JsonNode sendSimpleMessage(User user, String subject, String body) throws UnirestException {
        HttpResponse<JsonNode> request = Unirest.post("https://api.mailgun.net/v3/" + mgDomain + "/messages")
                .basicAuth("api", mgAPIKey)
                .field("from", String.format("Baking Buddy <%s>", from))
                .field("to", user.getEmail())
                .field("subject", subject)
                .field("text", body)
                .asJson();

        return request.getBody();
    }

    public JsonNode sendPasswordResetMessage(User user, String resetUrl, boolean testMode) throws UnirestException {
        final String subject = "Baking Buddy | Password Reset";
        final String resetVars = String.format(
                "{\"username\": \"%s\", \"reset_url\": \"%s\"}", user.getUsername(), resetUrl
        );
        System.out.println("\033[32mresetUrl = \033[0m" + resetUrl);
        System.out.println("\033[32mresetVars = \033[0m" + resetVars);

        HttpResponse<JsonNode> request = Unirest.post("https://api.mailgun.net/v3/" + mgDomain + "/messages")
                .basicAuth("api", mgAPIKey)
                .field("from", String.format("Baking Buddy <%s>", from))
                .field("to", user.getEmail())
                .field("subject", subject)
                .field("template", "pw_reset")
                .field("h:X-Mailgun-Variables", resetVars)
                .field("o:testmode", String.format("%s", testMode))
                .asJson();

        return request.getBody();
    }

    public JsonNode sendRegisterMessage(User user, boolean testMode) throws UnirestException {
        final String subject = "Thanks for Registering to Baking Buddy!";
        final String resetVars = String.format(
                "{\"username\": \"%s\"}" , user.getUsername()
        );

        HttpResponse<JsonNode> request = Unirest.post("https://api.mailgun.net/v3/" + mgDomain + "/messages")
                .basicAuth("api", mgAPIKey)
                .field("from", String.format("Baking Buddy <%s>", from))
                .field("to", user.getEmail())
                .field("subject", subject)
                .field("template", "registeration_email")
                .field("h:X-Mailgun-Variables", resetVars)
                .field("o:testmode", String.format("%s", testMode))
                .asJson();

        return request.getBody();
    }

}
