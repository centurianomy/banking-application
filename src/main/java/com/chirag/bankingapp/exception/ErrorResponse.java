//ErrorResponse: A data container! nothing else
package com.chirag.bankingapp.exception;

import java.time.LocalDateTime;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    private int status;
    private String error;
    private String message;
    private LocalDateTime timestamp;
    private Map<String, String> fieldErrors;
}


/*Note: Core OOPS concept
  Why Map<String, String> map=new HasMap<>(); ?
  	[Map] is an [interface] — a contract that says "something that stores key-value pairs."
	[HashMap] is just one specific implementation of that contract
	in future if we have have a change in req we only need to change one line,
	where obj is created

*/