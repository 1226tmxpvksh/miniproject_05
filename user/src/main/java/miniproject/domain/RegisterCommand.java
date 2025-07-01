package miniproject.domain;

import java.time.LocalDate;
import java.util.*;
import lombok.Data;
@Data
public class RegisterCommand {
    
    private String email;
    private String nickname;
    private String password;  //평문 패스워드(입력값, DB 저장 X)
}

