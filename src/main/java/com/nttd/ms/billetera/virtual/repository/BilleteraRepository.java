package com.nttd.ms.billetera.virtual.repository;

import com.nttd.ms.billetera.virtual.entity.Billetera;
import io.quarkus.mongodb.panache.reactive.ReactivePanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class BilleteraRepository implements ReactivePanacheMongoRepository<Billetera> {
}
