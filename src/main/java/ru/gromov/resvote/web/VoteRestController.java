package ru.gromov.resvote.web;

/*
 *   Created by Gromov Vitaly, 2019   e-mail: mr.gromov.vitaly@gmail.com
 */

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.gromov.resvote.service.VoteService;
import ru.gromov.resvote.to.RestaurantWithVoteTo;
import ru.gromov.resvote.to.VoterTo;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequestMapping(value = "api/v1/restaurants")
@RestController
@RequiredArgsConstructor
public class VoteRestController {

	@Autowired
	private final VoteService voteService;

	@GetMapping(value = "/{id}/vote")
	public List<VoterTo> getListVoteOfRestaurant(@PathVariable final String id,
	                                             @RequestParam(value = "date", required = false)
	                                             @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
		log.info("GET request: get list of vote by restaurant id: {}, and date {}", id, date);
		if (date == null) date = LocalDate.now();
		return voteService.getRestaurantVote(Long.valueOf(id), date).stream()
				.map(v -> new VoterTo(v.getUser().getId(), v.getUser().getName()))
				.collect(Collectors.toList());
	}

	@DeleteMapping(value = "/vote")
	public ResponseEntity<?> delete() {
		log.info("DELETE request: delete user vote");
		voteService.deleteCurrentVoteOfUser(1L);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@PostMapping(value = "/{id}/vote")
	public ResponseEntity<?> makeVote(@PathVariable final String id) {
		log.info("POST request: make user vote");
		voteService.makeVote(Long.valueOf(id));
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@GetMapping(value = "/vote")
	public List<RestaurantWithVoteTo> getListVotedRestaurants(@RequestParam(value = "date", required = false)
	                                                          @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
	                                                          @RequestParam(value = "page", required = false) final Integer page,
	                                                          @RequestParam(value = "size", required = false) final Integer size) {
		log.info("GET request: get list of restaurant with vote");
		if (date == null) date = LocalDate.now();
		if (page == null && size == null) {
			return voteService.getVotedRestaurants(date);
		} else {
			return voteService.getVotedRestaurantsPaginated(date, page, size).getContent();
		}

	}

}