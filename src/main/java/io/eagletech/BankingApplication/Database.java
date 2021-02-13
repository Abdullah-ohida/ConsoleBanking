package io.eagletech.BankingApplication;

import java.util.List;
import java.util.Optional;

public interface Database<K> {

    void save(K k);


    boolean contains(K k);

    void delete(K k);

    List<K> findAll();

    int size();

   Optional<K> find(Storable storable);

    Optional<K> findById(String storableId);
}
