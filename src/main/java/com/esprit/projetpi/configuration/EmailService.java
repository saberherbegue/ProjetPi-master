package com.esprit.projetpi.configuration;

public interface EmailService {
	
	public void sendSimpleMessage(
		      String to, String subject, String text);

}
