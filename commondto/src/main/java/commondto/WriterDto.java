package commondto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WriterDto {
    private Long writerId;
    private String approvalStatus; //작가승인여부
}

