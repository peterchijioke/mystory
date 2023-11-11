package com.mystory.mystory.shared;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GenericResponse {
private String message;

public GenericResponse(String message) {
	
	this.setMessage(message);
}

public String getMessage() {
	return message;
}

public void setMessage(String message) {
	this.message = message;
}


}
