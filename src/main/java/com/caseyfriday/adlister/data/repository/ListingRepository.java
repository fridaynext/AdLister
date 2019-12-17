package com.caseyfriday.adlister.data.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.caseyfriday.adlister.data.entity.Listing;

@Repository
public interface ListingRepository extends CrudRepository<Listing, Long> {
	
}
