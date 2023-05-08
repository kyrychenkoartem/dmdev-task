package com.artem.service;

import com.artem.mapper.TransactionMapper;
import com.artem.model.dto.TransactionCreateDto;
import com.artem.model.dto.TransactionFilter;
import com.artem.model.dto.TransactionReadDto;
import com.artem.model.dto.TransactionUpdateDto;
import com.artem.repository.QPredicate;
import com.artem.repository.TransactionRepository;
import com.artem.util.UserDetailsUtil;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.artem.model.entity.QTransaction.transaction;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TransactionService implements UserPermissionService {

    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;

    public List<TransactionReadDto> findAll() {
        return transactionRepository.findAll().stream()
                .map(transactionMapper::mapFrom)
                .toList();
    }

    public Page<TransactionReadDto> findAll(TransactionFilter filter, Pageable pageable) {
        var predicate = QPredicate.builder()
                .add(filter.referenceNumber(), transaction.referenceNumber::eq)
                .add(filter.time(), transaction.time::before)
                .buildAnd();
        return transactionRepository.findAll(predicate, pageable)
                .map(transactionMapper::mapFrom);
    }

    @PreAuthorize("hasAuthority('ADMIN') or @transactionService.isUserOwner(#id)")
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

    @Override
    public boolean isUserOwner(Long transactionId) {
        var currentUserId = UserDetailsUtil.getCurrentUserId();
        var maybeTransaction = transactionRepository.findById(transactionId);
        boolean isPresent = false;
        if (maybeTransaction.isPresent()) {
            isPresent = maybeTransaction.get().getBankAccount().getAccount().getUser().getId().equals(currentUserId);
        }
        return isPresent;
    }
}
