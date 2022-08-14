package com.emesall.news.controller;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.emesall.news.exception.NotFoundException;



@ControllerAdvice
public class ExceptionHandlingController {

	
	private final MessageSource messageSource;
	
	@Autowired
	public ExceptionHandlingController(MessageSource messageSource) {
		super();
		this.messageSource = messageSource;
	}


	@ExceptionHandler(NotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ModelAndView handleNotFound(Exception exception) {
		String NOT_FOUND = "Error!";
		ModelAndView mav = new ModelAndView();
		mav.addObject("response", NOT_FOUND);
		mav.addObject("message", messageSource.getMessage("Exception.NotFound.message", null, Locale.getDefault()));
		mav.setViewName("errorPage");
		return mav;
	}
	
	
	@ExceptionHandler(NoHandlerFoundException.class)
	public ModelAndView handleNotFoundUrl(Exception exception) {
		String NOT_FOUND = "Error!";
		ModelAndView mav = new ModelAndView();
		mav.addObject("response", NOT_FOUND);
		mav.addObject("message", messageSource.getMessage("Exception.NotFound.message", null, Locale.getDefault()));
		mav.setViewName("errorPage");
		return mav;
	}
}
