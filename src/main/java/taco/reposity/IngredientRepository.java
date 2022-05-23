package taco.reposity;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import taco.model.Ingredient;

public interface IngredientRepository extends CrudRepository<Ingredient, String>{

	Optional<Ingredient> findById(Long id);
	

}
