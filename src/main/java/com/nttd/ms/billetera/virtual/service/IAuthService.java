package com.nttd.ms.billetera.virtual.service;

import com.nttd.ms.billetera.virtual.entity.Auth;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;

public interface IAuthService {
    Uni<Boolean> login(Auth auth);

    Uni<Void> signOut(Auth auth);

    Multi<Auth> listAll();
}
