package io.eagletech.BankingApplication;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DatabaseImpl<K extends Storable> implements Database<K> {
    List<K> dataStore = new ArrayList<>();
    @Override
    public void save(K k) {
        dataStore.add(k);
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

    @Override
    public Optional<K> findById(String storableId) {
        for(K item: dataStore) {
            if (item.getId().equals(storableId)){
                return Optional.of(item);
            }
        }
        return Optional.empty();
    }
}
