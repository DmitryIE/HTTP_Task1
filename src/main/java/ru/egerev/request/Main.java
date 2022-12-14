package ru.egerev.request;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.util.List;
import java.util.function.Predicate;

public class Main {

    private final static String REMOTE_SERVICE_URI
            = "https://raw.githubusercontent.com/netology-code/jd-homeworks/master/http/task1/cats";
    private static ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) {
        try (CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setUserAgent("My Request Service")
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(5000)
                        .setSocketTimeout(30000)
                        .setRedirectsEnabled(false)
                        .build())
                .build();) {

            HttpGet request = new HttpGet(REMOTE_SERVICE_URI);

            try (CloseableHttpResponse response = httpClient.execute(request)) {
                List<DataCats> responceList = mapper.readValue(response.getEntity().getContent(),
                        new TypeReference<>() {
                        });
                responceList.stream()
                        .filter(new Predicate<DataCats>() {
                            @Override
                            public boolean test(DataCats dataCats) {
                                if (dataCats.getUpvotes() != 0) {
                                    return true;
                                }
                                return false;
                            }
                        })
                        .forEach(System.out::println);
            }

        } catch (IOException e) {
            throw new RuntimeException();
        }
    }


}
