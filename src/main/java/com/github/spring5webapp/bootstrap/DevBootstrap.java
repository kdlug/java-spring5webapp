package com.github.spring5webapp.bootstrap;

import com.github.spring5webapp.model.Author;
import com.github.spring5webapp.model.Book;
import com.github.spring5webapp.repositories.AuthorRepository;
import com.github.spring5webapp.repositories.BookRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component // It makes it spring bean
public class DevBootstrap implements ApplicationListener<ContextRefreshedEvent> {

    private AuthorRepository authorRepository;
    private BookRepository bookRepository;

    // Author Repository and BookRepository will be autowired
    public DevBootstrap(AuthorRepository authorRepository, BookRepository bookRepository) {
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        initData();
    }

    private void initData() {
        // Eric
        Author eric = new Author("Eric", "Evans");
        Book ddd = new Book("Domain Driven Design", "1234", "Harper Collins");
        eric.getBooks().add(ddd);
        ddd.getAuthors().add(eric);

        authorRepository.save(eric);
        bookRepository.save(ddd);

        Author rod = new Author("Rod", "Johnson");
        Book j2ee = new Book("J2EE Development without EJB", "2244", "Helion");
        rod.getBooks().add(j2ee);
        j2ee.getAuthors().add(rod);

        authorRepository.save(rod);
        bookRepository.save(j2ee);
    }
}
