package kpu.cybersecurity.training.controller;

import com.turkraft.springfilter.boot.Filter;
import kpu.cybersecurity.training.domain.dto.request.ReqModuleDTO;
import kpu.cybersecurity.training.domain.dto.response.ResModuleDTO;
import kpu.cybersecurity.training.domain.dto.response.ResultPaginationDTO;
import kpu.cybersecurity.training.domain.entity.Module;
import kpu.cybersecurity.training.service.ModuleService;
import kpu.cybersecurity.training.util.annotation.ApiMessage;
import kpu.cybersecurity.training.util.exception.UniqueException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/modules")
public class ModuleController {

    @Autowired
    private ModuleService moduleService;

    @PostMapping("/create")
    @ApiMessage("Create Module")
    private ResponseEntity<Void> createModule(@RequestBody ReqModuleDTO dto) throws UniqueException {
        moduleService.createModule(dto);
        return ResponseEntity.ok(null);
    }

    @GetMapping("/get")
    @ApiMessage("Get Paging Modules")
    private ResponseEntity<ResultPaginationDTO<ResModuleDTO>> getPagingModules(
            Pageable pageable,
            @Filter Specification<Module> spec
            ) {

        return ResponseEntity.ok(moduleService.getPagingModules(spec, pageable));
    }

    @GetMapping("/get/list")
    @ApiMessage("Get List Modules")
    private ResponseEntity<List<ResModuleDTO>> getListModules(
            @Filter Specification<Module> spec
    ) {
        return ResponseEntity.ok(moduleService.getListModules(spec));
    }

    @GetMapping("/get/{moduleId}")
    @ApiMessage("Get Module by ModuleID")
    private ResponseEntity<ResModuleDTO> getModuleByModuleId(
            @PathVariable("moduleId") Long moduleId) {
        return ResponseEntity.ok(moduleService.getModuleByModuleId(moduleId).toResponseDto());
    }

    @PutMapping("/update/{moduleId}")
    @ApiMessage("Update Module")
    private ResponseEntity<ResModuleDTO> updateModule(
            @PathVariable("moduleId") Long moduleId,
            @RequestBody ReqModuleDTO dto
    ) throws UniqueException {
        return ResponseEntity.ok(moduleService.updateModule(moduleId, dto).toResponseDto());
    }

    @DeleteMapping("/delete/{moduleId}")
    @ApiMessage("Delete Module")
    private ResponseEntity<Void> deleteModule(@PathVariable("moduleId") Long moduleId) {
        this.moduleService.deleteModule(moduleId);
        return ResponseEntity.ok(null);
    }

}
