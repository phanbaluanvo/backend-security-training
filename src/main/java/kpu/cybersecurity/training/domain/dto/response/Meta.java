package kpu.cybersecurity.training.domain.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Meta {
    private int page;
    private int size;
    private int totalPages;
    private long totalElements;
}
