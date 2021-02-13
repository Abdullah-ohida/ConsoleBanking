package io.eagletech.BankingApplication;

import java.util.ArrayList;
import java.util.List;

public class Database<K> implements Storable<K>{
    List<K> dataStore = new ArrayList<>();
    @Override
    public void save(K k) {
        dataStore.add(k);
    }

    @Override
    public K findById(String id) {
        return dataStore.get(Integer.parseInt(id));
    }

    @Override
    public boolean contains(K k) {
        return dataStore.contains(k);
    }

    @Override
    public void delete(K k) {
        dataStore.remove(k);
    }

    @Override
    public List<K> findAll() {
        return dataStore;
    }

    @Override
    public int size() {
        return dataStore.size();
    }
}
