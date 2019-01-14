package ru.gromov.resvote.web;

import lombok.SneakyThrows;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/*
 *   Created by Gromov Vitaly, 2019   e-mail: mr.gromov.vitaly@gmail.com
 */
public class RestaurantRestControllerTest extends AbstractRestControllerTest{

	private static final String ALL_RESTAURANTS = "json/restaurants_all.json";
	private static final String NEW_RESTAURANT = "json/new_restaurant.json";
	private static final String ALL_RESTAURANTS_WITH_DISHES_TODAY = "json/restaurants_all.json";
	private static final String ALL_RESTAURANTS_WITH_DISHES_20190103 = "json/restaurants_all_20190103.json";
	private static final String RESTAURANT_ID_1 = "json/restaurant_id_1.json";
	private static final String EDITED_RESTAURANT_ID_1 = "json/edited_restaurant_id_1.json";
	private static final String RESTAURANT_WITHOUT_ID_1 = "json/all_restaurant_without_id_1.json";

	@WithMockUser(roles = {"ADMIN"})
	@SneakyThrows
	@Test
	public void getAllRestaurants() {
		String json = util.getJsonString(util.getTestFile(ALL_RESTAURANTS).toPath());
		mockMvc.perform(get(REST_URL + "restaurants"))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(content().json(json));
	}

	@WithMockUser(roles = {"ADMIN"})
	@SneakyThrows
	@Test
	public void addRestaurant() {
		String json = util.getJsonString(util.getTestFile(NEW_RESTAURANT).toPath());
		mockMvc.perform(post(REST_URL + "restaurants")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json))
				.andExpect(status().isOk());
	}

	@WithMockUser(roles = {"ADMIN"})
	@SneakyThrows
	@Test
	public void getRestaurant() {
		final int restaurantId = 1;
		String json = util.getJsonString(util.getTestFile(RESTAURANT_ID_1).toPath());
		mockMvc.perform(get(REST_URL + "restaurants/"+restaurantId))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(content().json(json));
	}

	@WithMockUser(roles = {"ADMIN"})
	@SneakyThrows
	@Test
	public void update() {
		final int restaurantId = 1;
		String json = util.getJsonString(util.getTestFile(EDITED_RESTAURANT_ID_1).toPath());
		mockMvc.perform(put(REST_URL +  "restaurants/"+restaurantId)
				.contentType(MediaType.APPLICATION_JSON)
				.content(json))
				.andExpect(status().isOk());
	}

	@WithMockUser(roles = {"ADMIN"})
	@SneakyThrows
	@Test
	public void getRestaurantWithDishesByDate() {
		String json = util.getJsonString(
				util.getTestFile(ALL_RESTAURANTS_WITH_DISHES_20190103).toPath());
		mockMvc.perform(
				get(REST_URL + "restaurants/dishes").param("date", "2019-01-03"))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(content().json(json));
	}

	@WithMockUser(roles = {"ADMIN"})
	@SneakyThrows
	@Test
	public void deleteRestaurant() {
		final int restaurantId = 1;
		mockMvc.perform(delete(REST_URL + "restaurants/"+restaurantId))
				.andExpect(status().isNoContent());

	}

	@WithMockUser(roles = {"ADMIN"})
	@SneakyThrows
	@Test
	public void notAllowedRequestDelete() {
		mockMvc.perform(delete(REST_URL + "restaurants"))
				.andExpect(status().isMethodNotAllowed());

	}
	@WithMockUser(roles = {"ADMIN"})
	@SneakyThrows
	@Test
	public void notAllowedRequestPut() {
		mockMvc.perform(put(REST_URL + "restaurants"))
				.andExpect(status().isMethodNotAllowed());

	}

	@WithMockUser(roles = {"ADMIN"})
	@SneakyThrows
	@Test
	public void notAllowedRequestWithId() {
		final int restaurantId = 1;
		mockMvc.perform(post(REST_URL + "restaurants/"+restaurantId))
				.andExpect(status().isMethodNotAllowed());
	}
}