package com.navershop.navershop.custom.adapter.provider;

import com.navershop.navershop.custom.entity.Option;
import com.navershop.navershop.custom.entity.repository.OptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class OptionProviderImpl {
    private final OptionRepository optionRepository;

    public Option findById(Long id){
        return optionRepository.findById(id).orElse(null);
    }
}