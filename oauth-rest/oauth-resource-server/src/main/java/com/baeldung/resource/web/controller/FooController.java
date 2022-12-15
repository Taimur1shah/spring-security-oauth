package com.baeldung.resource.web.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.baeldung.resource.persistence.model.Foo;
import com.baeldung.resource.service.IFooService;
import com.baeldung.resource.web.dto.FooDto;

@RestController
@RequestMapping(value = "/api/foos")
public class FooController {

    private IFooService fooService;

    public FooController(IFooService fooService) {
        this.fooService = fooService;
    }

    @CrossOrigin(origins = "http://localhost:8089")
    @GetMapping(value = "/{id}")
    public FooDto findOne(@AuthenticationPrincipal Jwt principal,@PathVariable Long id) {

        String preferred_username = principal.getClaimAsString("preferred_username");
        if(preferred_username.endsWith("unknown user")){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        Foo entity = fooService.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return convertToDto(entity);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public void create(@AuthenticationPrincipal Jwt principal,@RequestBody FooDto newFoo) {

        String preferred_username = principal.getClaimAsString("preferred_username");
        if(preferred_username.endsWith("unknown user")){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        Foo entity = convertToEntity(newFoo);
        this.fooService.save(entity);
    }

    @GetMapping
    public Collection<FooDto> findAll(@AuthenticationPrincipal Jwt principal) {

        String preferred_username = principal.getClaimAsString("preferred_username");
        if(preferred_username.endsWith("unknown user")){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        Iterable<Foo> foos = this.fooService.findAll();
        List<FooDto> fooDtos = new ArrayList<>();
        foos.forEach(p -> fooDtos.add(convertToDto(p)));
        return fooDtos;
    }

    @PutMapping("/{id}")
    public FooDto updateFoo(@AuthenticationPrincipal Jwt principal,@PathVariable("id") Long id, @RequestBody FooDto updatedFoo) {

        String preferred_username = principal.getClaimAsString("preferred_username");
        if(preferred_username.endsWith("unknown user")){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        Foo fooEntity = convertToEntity(updatedFoo);
        return this.convertToDto(this.fooService.save(fooEntity));
    }

    protected FooDto convertToDto(Foo entity) {
        FooDto dto = new FooDto(entity.getId(), entity.getName());

        return dto;
    }

    protected Foo convertToEntity(FooDto dto) {
        Foo foo = new Foo(dto.getName());
        if (!StringUtils.isEmpty(dto.getId())) {
            foo.setId(dto.getId());
        }
        return foo;
    }
}