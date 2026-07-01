package com.tourism.app.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PaymentGatewayService {
    private final String keyId;
    private final String keySecret;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final HttpClient httpClient = HttpClient.newHttpClient();

    public PaymentGatewayService(@Value("${payment.razorpay.key-id:}") String keyId,
                                 @Value("${payment.razorpay.key-secret:}") String keySecret) {
        this.keyId = keyId;
        this.keySecret = keySecret;
    }

    public boolean isConfigured() {
        return keyId != null && !keyId.isBlank() && keySecret != null && !keySecret.isBlank();
    }

    public String getKeyId() {
        return keyId;
    }

    public GatewayOrder createOrder(BigDecimal amount, String receipt) {
        if (!isConfigured()) {
            return new GatewayOrder("DEMO-ORDER-" + receipt, amountInPaise(amount));
        }
        try {
            String body = objectMapper.createObjectNode()
                    .put("amount", amountInPaise(amount))
                    .put("currency", "INR")
                    .put("receipt", receipt)
                    .toString();
            HttpRequest request = HttpRequest.newBuilder(URI.create("https://api.razorpay.com/v1/orders"))
                    .header("Authorization", "Basic " + authToken())
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() >= 200 && response.statusCode() < 300) {
                JsonNode json = objectMapper.readTree(response.body());
                return new GatewayOrder(json.get("id").asText(), json.get("amount").asLong());
            }
            return new GatewayOrder("FAILED-" + receipt, amountInPaise(amount));
        } catch (Exception ex) {
            return new GatewayOrder("FAILED-" + receipt, amountInPaise(amount));
        }
    }

    public boolean verifySignature(String orderId, String paymentId, String signature) {
        if (!isConfigured()) {
            return paymentId != null && !paymentId.isBlank();
        }
        try {
            String payload = orderId + "|" + paymentId;
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(keySecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            String expected = bytesToHex(mac.doFinal(payload.getBytes(StandardCharsets.UTF_8)));
            return expected.equals(signature);
        } catch (Exception ex) {
            return false;
        }
    }

    public String refund(String paymentId, BigDecimal amount) {
        if (paymentId != null && paymentId.startsWith("LOCAL-")) {
            return "LOCAL-REFUND-" + System.currentTimeMillis();
        }
        if (!isConfigured() || paymentId == null || paymentId.isBlank()) {
            return "DEMO-REFUND-" + System.currentTimeMillis();
        }
        try {
            String body = objectMapper.createObjectNode()
                    .put("amount", amountInPaise(amount))
                    .toString();
            HttpRequest request = HttpRequest.newBuilder(URI.create("https://api.razorpay.com/v1/payments/" + paymentId + "/refund"))
                    .header("Authorization", "Basic " + authToken())
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() >= 200 && response.statusCode() < 300) {
                JsonNode json = objectMapper.readTree(response.body());
                return json.get("id").asText();
            }
        } catch (Exception ignored) {
        }
        return "FAILED-REFUND-" + System.currentTimeMillis();
    }

    private long amountInPaise(BigDecimal amount) {
        return amount.multiply(BigDecimal.valueOf(100)).setScale(0, RoundingMode.HALF_UP).longValue();
    }

    private String authToken() {
        return Base64.getEncoder().encodeToString((keyId + ":" + keySecret).getBytes(StandardCharsets.UTF_8));
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder hex = new StringBuilder();
        for (byte value : bytes) {
            hex.append(String.format("%02x", value));
        }
        return hex.toString();
    }

    public record GatewayOrder(String orderId, long amountInPaise) {
    }
}
