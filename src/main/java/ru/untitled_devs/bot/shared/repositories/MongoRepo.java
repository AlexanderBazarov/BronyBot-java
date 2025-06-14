package ru.untitled_devs.bot.shared.repositories;

import dev.morphia.Datastore;
import dev.morphia.query.MorphiaCursor;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.Optional;

import static dev.morphia.query.filters.Filters.*;

public abstract class MongoRepo<T> implements BaseRepo<T, ObjectId>{
	private final Class<T> objectClass;
	private final Datastore datastore;

    protected MongoRepo(Datastore datastore, Class<T> objectClass) {
		this.datastore = datastore;
		this.objectClass = objectClass;
    }

	@Override
	public T save(T entity) {
		return this.datastore.save(entity);
	}

	@Override
	public Optional<T> findById(ObjectId objectId) {
		return Optional.ofNullable(this.datastore.find(objectClass)
			.filter(eq("_id", objectId)).first());
	}

	@Override
	public List<T> findAll() {
		 try (MorphiaCursor<T> cursor = this.datastore.find(objectClass).iterator()) {
			return cursor.toList();
		 }
	}

	@Override
	public void delete(T entity) {
		datastore.delete(entity);
	}
}
