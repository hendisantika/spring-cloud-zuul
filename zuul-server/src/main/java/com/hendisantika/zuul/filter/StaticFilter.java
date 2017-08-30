package com.hendisantika.zuul.filter;

/**
 * Created by IntelliJ IDEA.
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 8/30/17
 * Time: 6:56 AM
 * To change this template use File | Settings | File Templates.
 */

import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.filters.StaticResponseFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
public class StaticFilter extends StaticResponseFilter {

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public Object uri() {
        return Pattern.compile("/api/static.*");
    }

    @Autowired
    private ResourceLoader resourceLoader;

    @Override
    public String responseBody() {
        if (RequestContext.getCurrentContext().getRequest().getRequestURI().endsWith(".svg")) {

            RequestContext.getCurrentContext().getResponse().setContentType("image/svg+xml");

            try {
                InputStream inputStream = resourceLoader.getResource("classpath:static/example.svg").getInputStream();
                String result = new BufferedReader(new InputStreamReader(inputStream))
                        .lines()
                        .collect(Collectors.joining("\n"));
                return result;
            } catch (IOException e) {
                e.printStackTrace();
                return e.getMessage();
            }

        } else {

            return "Static content";

        }
    }
}