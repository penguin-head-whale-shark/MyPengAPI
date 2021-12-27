package com.github.pdgs.MyPengAPI.service.posts;

import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class SchoolService {

    @Transactional
    public String findByName(String name) {
        List<HttpMessageConverter<?>> converters = new ArrayList<>();
        converters.add(new FormHttpMessageConverter());
        converters.add(new StringHttpMessageConverter());

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setMessageConverters(converters);

        String apiUrl = "http://jrady721.cafe24.com/api/school/" + name;
        String result = null;
        try {
            result = restTemplate.getForObject(apiUrl, String.class);
        } catch (HttpServerErrorException e) {
            if (e.getStatusCode().is5xxServerError()) {
                return null;
            }
        }
        System.out.println(result);
        return result;
    }

}
