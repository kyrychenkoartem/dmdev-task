package com.artem.service;

import com.artem.mapper.BankCardMapper;
import com.artem.model.dto.BankCardCreateDto;
import com.artem.model.dto.BankCardReadDto;
import com.artem.model.dto.BankCardUpdateDto;
import com.artem.repository.BankCardRepository;
import com.artem.util.UserDetailsUtil;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BankCardService implements UserPermissionService {

    private final BankCardRepository bankCardRepository;
    private final BankCardMapper bankCardMapper;

    public List<BankCardReadDto> findAll() {
        return bankCardRepository.findAll().stream()
                .map(bankCardMapper::mapFrom)
                .toList();
    }

    public Optional<BankCardReadDto> findByUserId(Long userId) {
        return bankCardRepository.findAllByUserId(userId).stream()
                .map(bankCardMapper::mapFrom)
                .findFirst();
    }

    @PreAuthorize("hasAuthority('ADMIN') or @bankCardService.isUserOwner(#id)")
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

    @Override
    public boolean isUserOwner(Long bankCardId) {
        var currentUserId = UserDetailsUtil.getCurrentUserId();
        return bankCardRepository.findById(bankCardId)
                .map(bankCard -> bankCard.getUser().getId().equals(currentUserId))
                .orElse(false);
    }
}
