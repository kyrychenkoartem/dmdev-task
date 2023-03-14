package com.artem.dao;

import com.artem.mapper.BankCardMapper;
import com.artem.model.dto.BankCardCreateDto;
import com.artem.model.dto.BankCardUpdateDto;
import com.artem.model.entity.BankCard;
import com.artem.model.type.BankType;
import com.artem.model.type.CardType;

import org.junit.jupiter.api.Test;

import static com.artem.util.ConstantUtil.ALL_BANK_CARDS;
import static com.artem.util.ConstantUtil.BANK_ACCOUNT_ID_THREE;
import static com.artem.util.ConstantUtil.BANK_ACCOUNT_ID_TWO;
import static com.artem.util.ConstantUtil.EXPIRY_DATE_EXPECTED;
import static com.artem.util.ConstantUtil.USER_ID_ONE;
import static org.assertj.core.api.Assertions.assertThat;


public class BankCardRepositoryTest extends RepositoryTestBase {

    private final BankCardRepository cardRepository = new BankCardRepository(session);
    private final UserRepository userRepository = new UserRepository(session);
    private final BankAccountRepository bankAccountRepository = new BankAccountRepository(session);
    private final BankCardMapper cardMapper = new BankCardMapper(bankAccountRepository, userRepository);

    @Test
    void checkSaveBankCard() {
        BankCard expectedBankCard = saveBankCard();
        session.clear();

        assertThat(bankAccountRepository.findById(expectedBankCard.getId()).get().getId()).isNotNull();
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
        var maybeBankCard = cardRepository.findById(expectedBankCard.getId());
        var cardUpdateDto = getBankCardUpdateDto();
        var bankCardToUpdate = cardMapper.mapFrom(maybeBankCard.get(), cardUpdateDto);

        cardRepository.update(bankCardToUpdate);
        session.clear();
        var actualBankCard = cardRepository.findById(bankCardToUpdate.getId()).get();

        assertThat(actualBankCard.getBankAccount()).isEqualTo(bankAccountRepository.findById(BANK_ACCOUNT_ID_TWO).get());
        assertThat(actualBankCard.getExpiryDate()).isEqualTo(EXPIRY_DATE_EXPECTED);
    }

    @Test
    void checkBankCardFindById() {
        var expectedBankCard = saveBankCard();

        var actualBankCard = cardRepository.findById(expectedBankCard.getId());

        assertThat(expectedBankCard).isEqualTo(actualBankCard.get());
    }

    @Test
    void checkFindAllBankCards() {
        saveBankCard();

        var bankCardList = cardRepository.findAll();

        assertThat(bankCardList.size()).isEqualTo(ALL_BANK_CARDS);
    }


    private BankCardCreateDto getBankCardCreateDto() {
        return BankCardCreateDto.builder()
                .userId(USER_ID_ONE)
                .bankAccountId(BANK_ACCOUNT_ID_THREE)
                .cardNumber("4567906543456789")
                .expiryDate("12/28")
                .bank(BankType.CIBC)
                .cvv("123")
                .cardType(CardType.DEBIT)
                .build();
    }

    private BankCardUpdateDto getBankCardUpdateDto() {
        return BankCardUpdateDto.builder()
                .bankAccount(bankAccountRepository.findById(BANK_ACCOUNT_ID_TWO).get())
                .expiryDate("11/30")
                .build();
    }

    private BankCard saveBankCard() {
        var bankCardCreateDto = getBankCardCreateDto();
        var bankCard = cardMapper.mapFrom(bankCardCreateDto);
        var expectedBankCard = cardRepository.save(bankCard);
        return expectedBankCard;
    }
}
