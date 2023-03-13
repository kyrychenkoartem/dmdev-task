package com.artem.dao;

import com.artem.mapper.BankCardMapper;
import com.artem.model.dto.BankCardCreateDto;
import com.artem.model.dto.BankCardUpdateDto;
import com.artem.model.entity.BankAccount;
import com.artem.model.entity.BankCard;
import com.artem.model.entity.User;
import com.artem.model.type.BankType;
import com.artem.model.type.CardType;

import java.util.Optional;
import org.junit.jupiter.api.Test;

import static com.artem.util.ConstantUtil.ALL_BANK_CARDS;
import static com.artem.util.ConstantUtil.EXPIRY_DATE_EXPECTED;
import static org.assertj.core.api.Assertions.assertThat;


public class BankCardRepositoryTestIT extends RepositoryTestBase {

    private final BankCardRepository cardRepository = new BankCardRepository(session);
    private final UserRepository userRepository = new UserRepository(session);
    private final BankAccountRepository bankAccountRepository = new BankAccountRepository(session);
    private final BankCardMapper cardMapper = new BankCardMapper();


    @Test
    void checkSaveBankCard() {
        BankCard expectedBankCard = saveBankCard();

        assertThat(expectedBankCard.getId()).isNotNull();
    }

    @Test
    void checkBankCardDelete() {
        BankCard expectedBankCard = saveBankCard();
        var actualBankCard = cardRepository.findById(expectedBankCard.getId());

        cardRepository.delete(actualBankCard.get());

        assertThat(cardRepository.findById(actualBankCard.get().getId())).isEmpty();
    }

    @Test
    void checkUpdateBankCard() {
        BankCard expectedBankCard = saveBankCard();
        var actualBankCard = cardRepository.findById(expectedBankCard.getId());
        var cardUpdateDto = getBankCardUpdateDto();
        var bankCardToUpdate = cardMapper.mapFrom(actualBankCard.get(), cardUpdateDto);

        cardRepository.update(bankCardToUpdate);
        session.clear();

        assertThat(cardRepository.findById(bankCardToUpdate.getId()).get().getBankAccount())
                .isEqualTo(bankAccountRepository.findById(2L).get());
        assertThat(cardRepository.findById(bankCardToUpdate.getId()).get().getExpiryDate())
                .isEqualTo(EXPIRY_DATE_EXPECTED);
    }

    @Test
    void checkBankCardFindById() {
        var expectedBankCard = saveBankCard();

        var actualBankCard = cardRepository.findById(expectedBankCard.getId());

        assertThat(expectedBankCard).isEqualTo(actualBankCard.get());
    }

    @Test
    void checkFindAllBankCards() {
        var expectedBankCard = saveBankCard();

        var bankCardList = cardRepository.findAll();

        assertThat(bankCardList.size()).isEqualTo(ALL_BANK_CARDS);
    }


    private BankCardCreateDto getBankCardCreateDto(Optional<User> user, Optional<BankAccount> bankAccount) {
        return BankCardCreateDto.builder()
                .user(user.get())
                .bankAccount(bankAccount.get())
                .cardNumber("4567906543456789")
                .expiryDate("12/28")
                .bank(BankType.CIBC)
                .cvv("123")
                .cardType(CardType.DEBIT)
                .build();
    }

    private BankCardUpdateDto getBankCardUpdateDto() {
        return BankCardUpdateDto.builder()
                .bankAccount(bankAccountRepository.findById(2L).get())
                .expiryDate("11/30")
                .build();
    }

    private BankCard saveBankCard() {
        var user = userRepository.findById(1L);
        var bankAccount = bankAccountRepository.findById(1L);
        var bankCardCreateDto = getBankCardCreateDto(user, bankAccount);
        var bankCard = cardMapper.mapFrom(bankCardCreateDto);
        var expectedBankCard = cardRepository.save(bankCard);
        return expectedBankCard;
    }
}
