package ru.untitled_devs.bot.repositories;

import java.util.List;
import java.util.Optional;

public interface BaseRepo<T, ID> {
	T save(T entity);
	Optional<T> findById(ID id);
	List<T> findAll();
	void delete(T entity);
}
