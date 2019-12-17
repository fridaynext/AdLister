package com.caseyfriday.adlister.web.application;

import java.util.List;

import javax.servlet.http.HttpServletRequestWrapper;

import com.caseyfriday.adlister.data.entity.Listing;
import com.caseyfriday.adlister.data.entity.User;
import com.caseyfriday.adlister.data.repository.UserRepository;
import com.caseyfriday.adlister.domain.AdListing;
import com.caseyfriday.adlister.service.ListingService;
import com.caseyfriday.auth.UserPrincipal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.trace.http.HttpTrace.Principal;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

@Controller
@RequestMapping("/")
public class ListingController {
	
	private final ListingService listingService;
	private final UserRepository userRepository;
	
	public ListingController(ListingService listingService, UserRepository userRepository) {
		super();
		this.listingService = listingService;
		this.userRepository = userRepository;
	}

	@GetMapping(value={"/","/index"})
	public String getHomePage(Model model) {
		return "index";
	}
	
	@GetMapping(value="/login")
	public String getLoginPage(Model model) {
		return "login";
	}
	
	@GetMapping(value="/logout-success")
	public String getLogoutPage(Model model) {
		return "logout";
	}
	
	@GetMapping(value="/listings")
	@PreAuthorize("hasRole('ROLE_USER')")
	public String getListings(Model model) {
		List<AdListing> adListings = this.listingService.getAdListings();
		model.addAttribute("adListings", adListings);
		return "listings";
	}
	
	@GetMapping(value="/listings/add")
	@PreAuthorize("hasRole('ROLE_USER')")
	public String getAddListingPage(Model model) {
		return "listing-view";
	}
	
	@PostMapping(value="/listings")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ModelAndView addListing(HttpServletRequestWrapper request, Model model, @ModelAttribute AdListing adListing, @AuthenticationPrincipal UserPrincipal currentUser){
        Listing listing = this.listingService.addAdListing(adListing);
        model.addAttribute("listing", listing);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) auth.getPrincipal();
        User user = this.userRepository.findByUsername(userPrincipal.getUsername());
        model.addAttribute("user", user); 
        request.setAttribute(View.RESPONSE_STATUS_ATTRIBUTE, HttpStatus.TEMPORARY_REDIRECT);
        return new ModelAndView("redirect:/listings/" + listing.getId());
    }

	
	@GetMapping(value="/listings/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public String getGuest(Model model, @PathVariable long id){
        Listing listing = this.listingService.getListing(id);
        model.addAttribute("listing", listing);
        return "listing-view";
    }
	
	@PostMapping(value="/listings/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public String updateListing(Model model, @PathVariable long id, @ModelAttribute AdListing adListing){
        Listing listing = this.listingService.updateListing(id, adListing);
        model.addAttribute("listing", listing);
        model.addAttribute("adListing", new AdListing());
        return "listing-view";
    }
	/*
	@GetMapping(value="/register")
	public String getRegisterPage(Model model) {
		return "Register";
	}
	*/
}
