package es.codeurjc.controllers;

import es.codeurjc.services.AuthService;
import es.codeurjc.classes.User;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.logging.Logger;

@Controller
@RequestMapping("/auth")
public class AuthController {
	@PostMapping("/login")
	public ResponseEntity<Void> login(@RequestBody String jsonRequested) {
		try {
			JSONObject jsonObject = new JSONObject(jsonRequested);
			String email = jsonObject.getString("email");
			String password = jsonObject.getString("password");
			User userRequested = new User(email, password);
			if (AuthService.authenticate(userRequested))
				return ResponseEntity.ok().build();
			return ResponseEntity.badRequest().build();
		}
		catch(JSONException e){
			Logger.getLogger("Error has occurred during parsing JSON.");
		}
		return ResponseEntity.notFound().build();
	}
	@PostMapping("/register")
	public ResponseEntity<Void> register(@RequestBody String jsonRequested){
		try{
			JSONObject jsonObject = new JSONObject(jsonRequested);
			String email = jsonObject.getString("email");
			String username = jsonObject.getString("username");
			String password = jsonObject.getString("password");
			User userRequested = new User(email, password, username);
			// Check if already exists or not and act in consequence
			if (AuthService.userExists(userRequested))
				return ResponseEntity.badRequest().build();
			AuthService.addUser(userRequested);
			return ResponseEntity.ok().build();
		} catch(JSONException e){
			Logger.getLogger("Error has occurred during parsing JSON.");
			return ResponseEntity.badRequest().build();
		} catch (NullPointerException e){
			Logger.getLogger("Tried to add or check a null user.");
			return ResponseEntity.badRequest().build();
		}
	}
}
