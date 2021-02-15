package com.cg.ppmtoolapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
@ControllerAdvice
@RestController
public class CustomResponseEntityHandler extends ResponseEntityExceptionHandler {
	@ExceptionHandler
	public ResponseEntity<Object> handleProjectIdException(ProjectIdException ex, WebRequest request){
		ProjectIDExceptionResponse projectIdExceptionResponse =  new ProjectIDExceptionResponse(ex.getMessage());
		return new ResponseEntity<Object>(projectIdExceptionResponse,HttpStatus.BAD_REQUEST);
	}	
	
	@ExceptionHandler
	public ResponseEntity<Object> handleProjectNotFoundException(ProjectNotFoundException ex, WebRequest request){
		ProjectNotFoundExceptionResponse projectNotFoundExceptionResponse =  new ProjectNotFoundExceptionResponse(ex.getMessage());
		return new ResponseEntity<Object>(projectNotFoundExceptionResponse,HttpStatus.BAD_REQUEST);
	}
	
}
