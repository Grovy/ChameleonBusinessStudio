package com.compilercharisma.chameleonbusinessstudio.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 7526472295622776147L;

    /**
     * Phone number of the user/receiver
     */
    private String userPhoneNumber;

    /**
     * The message to be sent to a user
     */
    private String message;
}
