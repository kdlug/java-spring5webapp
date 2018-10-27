package com.github.spring5webapp.repositories;

import com.github.spring5webapp.model.Book;
import com.github.spring5webapp.model.Publisher;
import org.springframework.data.repository.CrudRepository;

public interface PublisherRepository extends CrudRepository<Publisher, Long> {// <Model type, ID type>
}
