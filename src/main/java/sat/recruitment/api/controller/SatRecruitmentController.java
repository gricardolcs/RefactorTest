package sat.recruitment.api.controller;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import sat.recruitment.api.common.HandleErrors;
import sat.recruitment.api.model.User;
import sat.recruitment.api.service.UserService;

@RestController
@RequestMapping(value = "/api/v1")
public class SatRecruitmentController {

    @Autowired
    private UserService userService;
    @Autowired
    private HandleErrors handleErrors;

	@PostMapping(value = "/create-user", consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(value = HttpStatus.CREATED)
	public void createUser(@RequestBody User messageBody) {
		String errors = "";

        String responseError = handleErrors.validateErrors(messageBody.getName(),
            messageBody.getEmail(),
            messageBody.getAddress(),
            messageBody.getPhone(),
            errors);

        if (errors != null && errors != "") {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errors);
        }

        userService.saveOrUpdate(messageBody);


	}



}
