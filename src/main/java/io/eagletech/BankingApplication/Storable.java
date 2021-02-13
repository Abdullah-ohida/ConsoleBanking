package io.eagletech.BankingApplication;

import java.util.List;

public interface Storable<K> {

    void save(K k);

    K findById(String id);

    boolean contains(K k);

    void delete(K k);

    List<K> findAll();

    int size();
}
