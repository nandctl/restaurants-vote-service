package ru.gromov.resvote.service;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.SneakyThrows;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import ru.gromov.resvote.AbstractTest;
import ru.gromov.resvote.model.Dish;
import ru.gromov.resvote.model.Restaurant;
import ru.gromov.resvote.util.exception.NotFoundException;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

/*
 *   Created by Gromov Vitaly, 2019   e-mail: mr.gromov.vitaly@gmail.com
 */


public class DishServiceImplTest extends AbstractTest {

	private static final String DISH_ID_1 = "json/dish_id_1.json";
	private static final String DISH_OF_RESTAURANT_ID_1 = "json/dishes_of_Rest_id_1.json";
	private static final String EDITED_DISH = "json/edited_dish.json";
	private static final String NEW_DISHES = "json/new_dishes.json";

	@Autowired
	private RestaurantService restaurantService;

	@Autowired
	private DishService dishService;


	@WithMockUser(roles = {"ADMIN"})
	@Test
	@SneakyThrows
	public void getById() {
		final Dish dish = objectMapper.readValue(util.getTestFile(DISH_ID_1), Dish.class);
		assertEquals(dish, dishService.getById(dish.getId()));
	}

	@WithMockUser(roles = {"ADMIN"})
	@Test(expected = NotFoundException.class)
	@SneakyThrows
	public void getByIdWrongId() {
		final int wrongId = 999;
		dishService.getById(wrongId);
	}

	@WithMockUser(roles = {"ADMIN"})
	@SneakyThrows
	@Test
	public void getByRestaurantId() {
		final long restaurantId = 1L;
		List<Dish> dishesOfRestaurantId1 = objectMapper.readValue(
				util.getTestFile(DISH_OF_RESTAURANT_ID_1), new TypeReference<List<Dish>>() {
				});
		assertThat(dishService.getByRestaurantId(restaurantId, LocalDate.of(2019, 1, 3))).isEqualTo(dishesOfRestaurantId1);
	}

	@WithMockUser(roles = {"ADMIN"})
	@SneakyThrows
	@Test
	public void update() {
		final Dish dish = objectMapper.readValue(util.getTestFile(EDITED_DISH), Dish.class);
		dishService.update(dish);
		assertEquals(dish, dishService.getById(dish.getId()));
	}

	@WithMockUser(roles = {"ADMIN"})
	@SneakyThrows
	@Test(expected = NotFoundException.class)
	public void delete() {
		final long dishToDelete = 9L;
		dishService.delete(dishToDelete);
		dishService.getById(dishToDelete);
	}

	@WithMockUser(roles = {"ADMIN"})
	@SneakyThrows
	@Test
	public void createAll() {
		final int dishesCountAfterCreate = 5;
		final long restaurantId = 1L;
		List<Dish> dishes = objectMapper.readValue(
				util.getTestFile(NEW_DISHES), new TypeReference<List<Dish>>() {
				});

		final Restaurant restaurant = restaurantService.getById(restaurantId);

		List<Dish> dishList = dishes.stream()
				.map(dish -> new Dish(
						dish.getName(), dish.getPrice(), restaurant, LocalDate.now()
				)).collect(Collectors.toList());
		dishService.createAll(dishList);
		assertEquals(dishesCountAfterCreate,
				dishService.getByRestaurantId(restaurantId, LocalDate.now()).size());
	}
}