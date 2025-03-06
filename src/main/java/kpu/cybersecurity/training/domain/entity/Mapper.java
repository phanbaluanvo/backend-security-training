package kpu.cybersecurity.training.domain.entity;

public interface Mapper<E, Res, Req> {
    Res toResponseDto();
    void fromRequestDto(Req dto);
}
