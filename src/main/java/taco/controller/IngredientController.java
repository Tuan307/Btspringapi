package taco.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import taco.model.Ingredient;
import taco.reposity.IngredientRepository;

@RestController
@RequestMapping(path = "/ingredients", produces = "application/json")
@CrossOrigin(origins = "*")
public class IngredientController {
	
	@Autowired
	private IngredientRepository Repo;

	
	@GetMapping
	public Iterable<Ingredient> getAllIngredients() {
		return Repo.findAll();
	}
	
	@GetMapping("/{id}")
	public Ingredient getingredientById(@PathVariable("id") String id) {
	Optional<Ingredient> optIngredient = Repo.findById(id);
		if (optIngredient.isPresent()) {
			return optIngredient.get();
		}
		return null;
	}
}
