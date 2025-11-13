package br.com.financial.manager.app.domain.entity.token;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Document(collection = "passwordRecoveryTokens")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TokenRecovery {

    @Id
    private String token;

    @Field("userEmail")
    private String userEmail;

    @Indexed(expireAfter = "300")
    private LocalDateTime expiryDate;

    private boolean isUsed = false;
}
