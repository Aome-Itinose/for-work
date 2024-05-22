package org.kamil.forwork.util.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse extends Response {
    private String message;
    private String jwt_token;
    private Date time;
}
