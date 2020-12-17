package com.example.bakingbuddy.demo.Repos;

import com.example.bakingbuddy.demo.Model.Consumable;
import com.example.bakingbuddy.demo.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConsumableRepository extends JpaRepository<Consumable, Long> {
    public long getConsumableById(long id);
    public long getAllById(long id);

}
