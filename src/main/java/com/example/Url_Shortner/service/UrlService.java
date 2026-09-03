package com.example.Url_Shortner.service;
import com.example.Url_Shortner.entity.UrlMapping;
import com.example.Url_Shortner.repository.UrlMappingRepository;
import com.example.Url_Shortner.exception.UrlNotFoundException;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.Mapping;

import java.util.UUID;

@Service
public class UrlService {

    @Autowired
    private UrlMappingRepository repository;

    public String createShortUrl(String originalUrl){
        String shortCode = generateUniqueShortCode();

        UrlMapping mapping = new UrlMapping();
        mapping.setOriginalUrl(originalUrl);
        mapping.setShortCode(shortCode);
        repository.save(mapping);

        return shortCode;
    }

    public String getOriginalUrl(String Shortcode){
        UrlMapping mapping = repository.findByShortCode(Shortcode).orElseThrow(()-> new UrlNotFoundException("No Url Found" + Shortcode));
        mapping.setClickCount(mapping.getClickCount() +1);
        repository.save(mapping);

        return mapping.getOriginalUrl();
    }

    private String generateUniqueShortCode(){
        String code ;
        do{
            code = UUID.randomUUID().toString().substring(0,6);
        } while (repository.existsByShortCode(code));
        return code;

        }
    }

