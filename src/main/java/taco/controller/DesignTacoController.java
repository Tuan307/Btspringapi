package taco.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import taco.model.*;
import taco.model.Ingredient.Type;
import taco.reposity.*;
@RestController
@RequestMapping(path = "/design", produces = "application/json")
@CrossOrigin(origins = "*")
public class DesignTacoController {
private IngredientRepository tacoRepo;
private RestTemplate rest = new RestTemplate();

@ModelAttribute
public void addIngredientsToModel(Model model) {
List<Ingredient> ingredients = 
Arrays.asList(rest.getForObject("http://localhost:8080/ingredients",Ingredient[].class));
Type[] types = Ingredient.Type.values();
for (Type type : types) {
	model.addAttribute(type.toString().toLowerCase());
}
}

public DesignTacoController(IngredientRepository tacoRepo) {
	this.tacoRepo = tacoRepo;
}
@GetMapping("/recent")
public Iterable<Ingredient> recentTacos() {
	return tacoRepo.findAll();
}
@GetMapping("/{id}")
public Ingredient tacoById(@PathVariable("id") Long id) {
	Optional<Ingredient> optTaco = tacoRepo.findById(id);
	if (optTaco.isPresent()) {
		return optTaco.get();
}
	return null;
}
@PostMapping(consumes = "application/json")
@ResponseStatus(HttpStatus.CREATED)
public Ingredient postTaco(@RequestBody Ingredient taco) {
	return tacoRepo.save(taco);
}
@PostMapping
public String processDesign(@RequestParam("ingredients") String 
ingredientIds, @RequestParam("name") String name) {
List<Ingredient> ingredients = new ArrayList<Ingredient>();
for (String ingredientId : ingredientIds.split(",")) {
Ingredient ingredient = rest.getForObject("http://localhost:8080/ingredients/{id}",Ingredient.class, ingredientId);
ingredients.add(ingredient);
}
Taco taco = new Taco();
taco.setName(name);
taco.setIngredients(ingredients);
System.out.println(taco);
rest.postForObject("http://localhost:8080/design", taco, Taco.class);
return "redirect:/orders/current";
}
}
