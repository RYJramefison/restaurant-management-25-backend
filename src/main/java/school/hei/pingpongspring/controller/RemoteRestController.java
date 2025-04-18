package school.hei.pingpongspring.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class RemoteRestController {
    private final RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/call-remote")
    public String callRemote() {
        String url = "http://192.168.88.162:8080/ping"; // IP de la machine A
        return restTemplate.getForObject(url, String.class);
    }

    @GetMapping("/call-remote2")
    public String callRemote2() {
        String url = "http://192.168.88.162:8080/ingredients"; // IP de la machine A
        return restTemplate.getForObject(url, String.class);
    }

}
