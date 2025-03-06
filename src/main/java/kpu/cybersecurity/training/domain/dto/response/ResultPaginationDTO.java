package kpu.cybersecurity.training.domain.dto.response;

import kpu.cybersecurity.training.domain.entity.Mapper;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Setter
public class ResultPaginationDTO<ResDTO> {
    private Meta meta;
    private List<ResDTO> items;

    public <T extends Mapper<T, ResDTO, ?>> void setMetaAndResponse(Page<T> page
                                ) {
        this.meta = new Meta();

        this.meta.setPage(page.getNumber() + 1);
        this.meta.setSize(page.getSize());
        this.meta.setTotalPages(page.getTotalPages());
        this.meta.setTotalElements(page.getTotalElements());

        this.items = page.getContent().stream()
                .map(T::toResponseDto)
                .toList();
    }
}
