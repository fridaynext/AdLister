package com.caseyfriday.adlister.service;

import com.caseyfriday.adlister.data.repository.ListingRepository;
import com.caseyfriday.adlister.domain.AdListing;
import com.caseyfriday.adlister.data.entity.Listing;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.caseyfriday.adlister.data.entity.User;
import com.caseyfriday.adlister.data.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;

@Service
public class ListingService {
	private static final String LISTINGS = "/listings";
	private static final String SLASH = "/";
	
	@Value("${adlister.listing.service.url}")
	private String listingServiceUrl;
	
	private final ListingRepository listingRepository;
	private final UserRepository userRepository;
	
	private final RestTemplate restTemplate = new RestTemplate();
	
	@Autowired
	public ListingService(ListingRepository listingRepository, UserRepository userRepository) {
		this.listingRepository = listingRepository;
		this.userRepository = userRepository;
	}
	
	public List<AdListing> getAdListings() {
		List<AdListing> adListings = new ArrayList<>();
		Iterable<Listing> listings = this.listingRepository.findAll();
		if(listings!=null) {
			listings.forEach(listing->{
				AdListing adListing = new AdListing();
				
				// need to set the following values:
				// private long listingId;
				// private long userId;
				// private String title;
				// private int costInCents;
				// private String description;
				// private Date dateCreated;
				// private String userEmail;
				adListing.setTitle(listing.getTitle());
				adListing.setListingId(listing.getId());
				adListing.setUserId(listing.getUserID());
				Optional<User> userResponse = this.userRepository.findById(listing.getUserID());
				if(userResponse.isPresent()) {
					User user = userResponse.get();
					adListing.setUserEmail(user.getEmail());
				} else {
					// fallback in case no email is set on account
					adListing.setUserEmail("N.A.");
				}
				adListing.setCostInCents(listing.getCostInCents());
				adListing.setDateCreated(listing.getDateCreated());
				adListing.setDescription(listing.getDescription());
				
				adListings.add(adListing);
			});
		}
		return adListings;
	}
	
	public Listing addAdListing(AdListing adListing) {
		String url = listingServiceUrl + LISTINGS;
		HttpEntity<AdListing> request = new HttpEntity<>(adListing, null);
		return this.restTemplate.exchange(url, HttpMethod.POST, request, Listing.class).getBody();
	}
	
	public Listing getListing(long id) {
        String url = listingServiceUrl + LISTINGS + SLASH + id;
        HttpEntity<String> request = new HttpEntity<>(null, null);
        return this.restTemplate.exchange(url, HttpMethod.GET, request, Listing.class).getBody();
    }
	
	public Listing updateListing(long id, AdListing adListing) {
        System.out.println(adListing);
        String url = listingServiceUrl + LISTINGS + SLASH + id;
        HttpEntity<AdListing> request = new HttpEntity<>(adListing, null);
        return this.restTemplate.exchange(url, HttpMethod.PUT, request, Listing.class).getBody();
    }
	
}