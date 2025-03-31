package ai.Mayi.domain;

import ai.Mayi.domain.enums.TokenType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "token_id")
    private Long tokenId;

    @Enumerated(EnumType.STRING)
    @Column(name = "token_type", length = 30)
    @NotNull
    private TokenType tokenType;

    @Column(name = "token_value", length = 200)
    private String tokenValue;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
