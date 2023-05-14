package hello.jdbc.domain;

import lombok.Data;

@Data
public class Members {
    private String memberId;
    private int money;
    public Members() {
    }
    public Members(String memberId, int money) {
        this.memberId = memberId;
        this.money = money;
    }
}
