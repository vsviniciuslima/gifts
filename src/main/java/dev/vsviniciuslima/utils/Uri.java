package dev.vsviniciuslima.utils;

import java.net.URI;
import java.net.URISyntaxException;

public class Uri {

    public static URI getGiftsUri(Long id) {
        try {
            return new URI("/gifts/" + id);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
