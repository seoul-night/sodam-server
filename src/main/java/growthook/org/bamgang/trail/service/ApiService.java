package growthook.org.bamgang.trail.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
public class ApiService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${tmap-key}")
    private String appKey;

    public String getRouteData(double startX, double startY, double endX, double endY, String startName, String endName, String passList) throws UnsupportedEncodingException {
        String url = "https://apis.openapi.sk.com/tmap/routes/pedestrian?version=1";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set("appKey", appKey);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("startX", startX);
        requestBody.put("startY", startY);
        requestBody.put("endX", endX);
        requestBody.put("endY", endY);

        requestBody.put("startName", URLEncoder.encode(startName, "UTF-8"));
        requestBody.put("endName", URLEncoder.encode(endName, "UTF-8"));
        requestBody.put("passList", passList);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        return response.getBody();
    }

    public String getRouteData(double startX, double startY, double endX, double endY, String startName, String endName) throws UnsupportedEncodingException {
        String url = "https://apis.openapi.sk.com/tmap/routes/pedestrian?version=1";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set("appKey", appKey);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("startX", startX);
        requestBody.put("startY", startY);
        requestBody.put("endX", endX);
        requestBody.put("endY", endY);

        requestBody.put("startName", URLEncoder.encode(startName, "UTF-8"));
        requestBody.put("endName", URLEncoder.encode(endName, "UTF-8"));
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        return response.getBody();
    }
}
