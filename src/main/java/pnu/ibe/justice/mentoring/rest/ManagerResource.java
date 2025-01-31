package pnu.ibe.justice.mentoring.rest;


import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/Managers", produces = MediaType.APPLICATION_JSON_VALUE)
public class ManagerResource {
}
