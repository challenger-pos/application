package com.fiap.application.usecaseimpl.user;

import com.fiap.application.gateway.user.UserGateway;
import com.fiap.core.domain.user.User;
import com.fiap.core.exception.NotFoundException;
import com.fiap.core.exception.enums.ErrorCodeEnum;
import com.fiap.usecase.user.FindUserByEmailUseCase;

public class FindUserByEmailUseCaseImpl implements FindUserByEmailUseCase {

    private final UserGateway userGateway;

    public FindUserByEmailUseCaseImpl(UserGateway userGateway) {
        this.userGateway = userGateway;
    }

    @Override
    public User execute(String email) throws NotFoundException {
        return userGateway.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(ErrorCodeEnum.USE0007.getMessage(), ErrorCodeEnum.USE0007.getCode()));
    }
}