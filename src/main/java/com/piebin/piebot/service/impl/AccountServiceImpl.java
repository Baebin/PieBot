package com.piebin.piebot.service.impl;

import com.piebin.piebot.exception.AccountException;
import com.piebin.piebot.exception.entity.AccountErrorCode;
import com.piebin.piebot.model.domain.Account;
import com.piebin.piebot.model.repository.AccountRepository;
import com.piebin.piebot.service.AccountService;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;

    @Override
    @Transactional
    public void register(Member member, TextChannel channel) {
        if (existsUser(member.getId()))
            throw new AccountException(AccountErrorCode.NOT_FOUND);
        Account account = Account.builder()
                .id(member.getId())
                .name(member.getNickname())
                .build();
        accountRepository.save(account);
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
