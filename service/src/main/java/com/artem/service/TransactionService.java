package com.artem.service;

import com.artem.mapper.TransactionMapper;
import com.artem.model.dto.TransactionCreateDto;
import com.artem.model.dto.TransactionReadDto;
import com.artem.model.dto.TransactionUpdateDto;
import com.artem.repository.TransactionRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;

    public List<TransactionReadDto> findAll() {
        return transactionRepository.findAll().stream()
                .map(transactionMapper::mapFrom)
                .toList();
    }

    public Optional<TransactionReadDto> findById(Long id) {
        return transactionRepository.findById(id)
                .map(transactionMapper::mapFrom);
    }

    @Transactional
    public TransactionReadDto create(TransactionCreateDto createDto) {
        return Optional.of(createDto)
                .map(transactionMapper::mapFrom)
                .map(transactionRepository::save)
                .map(transactionMapper::mapFrom)
                .orElseThrow();
    }

    @Transactional
    public Optional<TransactionReadDto> update(Long id, TransactionUpdateDto updateDto) {
        return transactionRepository.findById(id)
                .map(transaction -> transactionMapper.mapFrom(transaction, updateDto))
                .map(transactionRepository::saveAndFlush)
                .map(transactionMapper::mapFrom);
    }

    @Transactional
    public boolean delete(Long id) {
        return transactionRepository.findById(id)
                .map(entity -> {
                    transactionRepository.delete(entity);
                    transactionRepository.flush();
                    return true;
                })
                .orElse(false);
    }
}
