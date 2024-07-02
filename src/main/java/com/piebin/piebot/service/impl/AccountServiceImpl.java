package com.piebin.piebot.service.impl;

import com.piebin.piebot.exception.AccountException;
import com.piebin.piebot.exception.entity.AccountErrorCode;
import com.piebin.piebot.model.domain.Account;
import com.piebin.piebot.model.domain.Inventory;
import com.piebin.piebot.model.repository.AccountRepository;
import com.piebin.piebot.model.repository.InventoryRepository;
import com.piebin.piebot.service.AccountService;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final InventoryRepository inventoryRepository;

    @Override
    @Transactional
    public void register(TextChannel channel, Member member) {
        if (existsUser(member.getId()))
            throw new AccountException(AccountErrorCode.NOT_FOUND);
        String nickName = member.getNickname();
        if (ObjectUtils.isEmpty(nickName))
            nickName = member.getEffectiveName();
        Account account = Account.builder()
                .id(member.getId())
                .name(nickName)
                .build();
        accountRepository.save(account);

        Inventory inventory = Inventory.builder()
                .account(account)
                .build();
        inventoryRepository.save(inventory);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsUser(String id) {
        return accountRepository.existsById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Account findUser(String id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new AccountException(AccountErrorCode.NOT_FOUND));
    }
}
