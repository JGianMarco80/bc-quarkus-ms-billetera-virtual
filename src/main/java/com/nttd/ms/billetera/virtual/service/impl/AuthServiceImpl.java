package com.nttd.ms.billetera.virtual.service.impl;

import java.time.Duration;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import com.nttd.ms.billetera.virtual.entity.Auth;
import com.nttd.ms.billetera.virtual.entity.Billetera;
import com.nttd.ms.billetera.virtual.exceptions.BadRequestException;
import com.nttd.ms.billetera.virtual.repository.BilleteraRepository;
import com.nttd.ms.billetera.virtual.service.IAuthService;

import io.quarkus.redis.datasource.ReactiveRedisDataSource;
import io.quarkus.redis.datasource.value.SetArgs;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class AuthServiceImpl implements IAuthService {
    @Inject
    Logger LOGGER;

    @Inject
    BilleteraRepository repository;

    @Inject
    ReactiveRedisDataSource redisDataSource;

    @ConfigProperty(name = "auth-pattern-key")
    String authPatternKey;

    @ConfigProperty(name = "auth-time-duration-day")
    Long authTimeDurationDay;

    @Override
    public Uni<Boolean> login(Auth auth) {
        String key = this.getKey(auth.getCelular());

        return this.repository.listAll()
                .onItem()
                .<Billetera>disjoint()
                .filter(billetera -> billetera.getCelular().equals(auth.getCelular()) &&
                        billetera.getPassword().equals(auth.getPassword()))
                .toUni()
                .onItem()
                .ifNull()
                .failWith(() -> new BadRequestException("Celular o contraseña incorrecta."))
                .call(() -> this.saveSessionRedis(key, auth))
                .flatMap((r) -> this.getSessionRedis(key))
                .onItem()
                .ifNull()
                .failWith(() -> new BadRequestException(
                        "Error al iniciar sesión. Por favor, vuelva a intentar en unos minutos."))
                .replaceWith(true);
    }

    @Override
    public Uni<Void> signOut(Auth auth) {
        String key = this.getKey(auth.getCelular());

        return this.getSessionRedis(key)
                .onItem()
                .ifNull()
                .failWith(() -> new BadRequestException(
                        "La sesión no existe."))
                .call(() -> this.deleteSessionRedis(key))
                .replaceWithVoid();
    }

    @Override
    public Multi<Auth> listAll() {
        return this.redisDataSource.key().keys(authPatternKey + "*")
                .onItem()
                .<String>disjoint()
                .flatMap(key -> this.getSessionRedis(key).toMulti());
    }

    private String getKey(String celular) {
        return authPatternKey + celular;
    }

    private Uni<Void> saveSessionRedis(String key, Auth auth) {
        return this.redisDataSource
                .value(String.class, Auth.class)
                .set(key, auth, new SetArgs().ex(Duration.ofDays(authTimeDurationDay)));
    }

    private Uni<Auth> getSessionRedis(String key) {
        return this.redisDataSource
                .value(String.class, Auth.class)
                .get(key);
    }

    private Uni<Auth> deleteSessionRedis(String key) {
        return this.redisDataSource
                .value(String.class, Auth.class)
                .getdel(key);
    }
}
