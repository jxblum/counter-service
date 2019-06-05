package example.app.counters.repo;

import org.springframework.data.repository.CrudRepository;

import example.app.counters.model.Counter;

public interface CounterRepository extends CrudRepository<Counter, String> {

}
