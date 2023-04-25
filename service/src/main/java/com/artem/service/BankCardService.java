package com.artem.service;

import com.artem.mapper.BankCardMapper;
import com.artem.model.dto.BankCardCreateDto;
import com.artem.model.dto.BankCardReadDto;
import com.artem.model.dto.BankCardUpdateDto;
import com.artem.model.entity.BankCard;
import com.artem.repository.BankCardRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BankCardService {

    private final BankCardRepository bankCardRepository;
    private final BankCardMapper bankCardMapper;
    private final UserService userService;

    public List<BankCardReadDto> findAll() {
        return bankCardRepository.findAll().stream()
                .map(bankCardMapper::mapFrom)
                .toList();
    }

    public Optional<BankCardReadDto> findById(Long id) {
        return bankCardRepository.findById(id)
                .map(bankCardMapper::mapFrom);
    }

    @Transactional
    public BankCardReadDto create(BankCardCreateDto createDto) {
        return Optional.of(createDto)
                .map(bankCardMapper::mapFrom)
                .map(bankCardRepository::save)
                .map(bankCardMapper::mapFrom)
                .orElseThrow();
    }

    @Transactional
    public Optional<BankCardReadDto> update(Long id, BankCardUpdateDto updateDto) {
        return bankCardRepository.findById(id)
                .map(bankCard -> bankCardMapper.mapFrom(bankCard, updateDto))
                .map(bankCardRepository::saveAndFlush)
                .map(bankCardMapper::mapFrom);
    }

    @Transactional
    public boolean delete(Long id) {
        return bankCardRepository.findById(id)
                .map(entity -> {
                    bankCardRepository.delete(entity);
                    bankCardRepository.flush();
                    return true;
                })
                .orElse(false);
    }

    public List<Long> getId() {
        return bankCardRepository.findAllByUserId(userService.getId()).stream()
                .map(BankCard::getId)
                .toList();
    }
}
