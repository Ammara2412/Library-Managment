package com.example.Library_Managment.dto;
import java.util.Map;

/*import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

*@Data
@NoArgsConstructor
@AllArgsConstructor*/

public class ErrorResponse {
    private String message;
    private Map<String, String> details;

    // ✅ No-arg constructor (for frameworks like Jackson)
    public ErrorResponse() {}

    // ✅ Constructor with message and details
    public ErrorResponse(String message, Map<String, String> details) {
        this.message = message;
        this.details = details;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, String> getDetails() {
        return details;
    }

    public void setDetails(Map<String, String> details) {
        this.details = details;
    }
}


/*public class ErrorResponse {
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, String> getError() {
        return error;
    }

    public void setError(Map<String, String> error) {
        this.error = error;
    }

    private Map<String, String> error;

}*/
