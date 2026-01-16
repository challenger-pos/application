package com.fiap.usecase.user;

import com.fiap.core.domain.user.User;
import com.fiap.core.exception.NotFoundException;

public interface FindUserByEmailUseCase {
    User execute(String email) throws NotFoundException;
}