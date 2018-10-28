# Java Spring 5

## Vocabulary
- `pojo` - plain old java object

## Environment
- IDE - IntelliJ Ultimate Edition (Spring support) or Community Edition (does not have Spring support)

### Install Graddle
Check the latest stable distribution here https://services.gradle.org/distributions/.

Download binary

```bash
wget https://services.gradle.org/distributions/gradle-4.10.2-bin.zip
```
Create a directory for the Gradle installation.

```bash
sudo mkdir /opt/gradle
```

Extract the downloaded archive to the newly created directory.

```bash
sudo unzip -d /opt/gradle gradle-4.10.2-bin.zip
rm gradle gradle-4.10.2-bin.zip
```

Configure the PATH environment variable so that the gradle executable can be directly executed anywhere on the system.

```bash
export PATH=$PATH:/opt/gradle/gradle-4.10.2/bin
```

Check if Graddle is installed.

```bash
gradle -v
```

### Verify your environment

```bash
# Java
java -version

# JDK
javac -version

# Graddle 
gradle -v
```

## Spring Boot

Spring based applications have a lot of configuration. When we use Spring MVC, we need to configure component scan, dispatcher servlet, aview resolver, web jars (for delivering static content) among other things.  

Spring Boot looks at:  

- Frameworks available on the CLASSPATH  
- Existing configuration for the application.

Based on these, Spring Boot provides basic configuration needed to configure the application with these frameworks. This is called Auto Configuration.

All auto configuration logic is implemented in spring-boot-autoconfigure.jar.  

## Spring initializr

Bootstrapig Spring application: https://start.spring.io/. We can fast create a skeleton application.

We can choose between 2 build tools:
Maven Project or Graddle Project.

Packages:

- spring-boot-starter-web-services - SOAP Web Services
- spring-boot-starter-web - Web & RESTful applications
- spring-boot-starter-test - Unit testing and Integration Testing
- spring-boot-starter-jdbc - Traditional JDBC
- spring-boot-starter-hateoas - Add HATEOAS features to your services
- spring-boot-starter-security - Authentication and Authorization using SpringSecurity
- spring-boot-starter-data-jpa - Spring Data JPA with Hibernate
- spring-boot-starter-cache - Enabling Spring Framework’s caching support
- spring-boot-starter-data-rest - Expose Simple REST Services using Spring DataREST
- spring-boot-starter-actuator - To use advanced features like monitoring & tracing to your application out of the box
- spring-boot-starter-undertow, spring-boot-starter-jetty, spring-boot-starter-tomcat - To pick your specific choice of Embedded Servlet Container
- spring-boot-starter-logging - For Logging using logback
- spring-boot-starter-log4j2 - Logging using Log4j2

## Developer Tools
```graddle
dependencies {
	implementation("org.springframework.boot:spring-boot-devtools")
}
```

## Dependency Injection

Most important feature of Spring Framework is Dependency Injection. Atthe core of all Spring Modules is Dependency Injection or IOC Inversion ofControl.

We just use two simple annotations: `@Component` and `@Autowired`.

- Using @Component, we tell Spring Framework - Hey there, this is a bean that you need to manage.
- Using @Autowired, we tell Spring Framework - Hey find the correct match forthis specific type and autowire it in.

```java
@Component
public class HelloWorld {    
	// Body
} 

@RestController
public class HelloController {    
	@Autowired    
	private HelloWorld hello;
	
	@RequestMapping("/hello")
	public String hello() {
		return hello.printMessage();
	}
}
```
## Packages

### H2
Enable H2 console in `resources/application.properties` file.

```yml
// application.properties
spring.h2.console.enabled=true
```

Open the URL `localhost:8080/h2-console` in your browser and make sure that you run in memory database named testdb:
`JDBC URL: jdbc:h2:mem:testdb`

### JPA
- Java Persistence API
- Is the official API for working with relational data in Java
- Is only specification, not a concrete implementation
- Database independently, supports many relational databases
- Hibernate is JPA implementation 

#### Many-To-Many Relationship
The author and book tables have a many-to-many relationship. Additional table author_book connects book_id and author_id.

model/Author.java
```java
package com.github.spring5webapp.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "author")
public class Author {

    // Id is generated at time of persistence
    // If we use generation type of AUTO, ID values going to get changed when the object is persisted for the first time
    // It could cause problems within a set
    // Hibernate recommends to use unique business key f.ex. fist name, last name
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private  Long id;
    private String firstName;
    private String lastName;

    @ManyToMany(mappedBy = "authors")
    private Set<Book> books = new HashSet<>();

    // default constructor
    public Author() {
    }

    public Author(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Author(String firstName, String lastName, Set<Book> books) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.books = books;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Set<Book> getBooks() {
        return books;
    }

    public void setBooks(Set<Book> books) {
        this.books = books;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Author author = (Author) o;
        return Objects.equals(id, author.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Author{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", books=" + books +
                '}';
    }
}
```

model/Book.java

```java
package com.github.spring5webapp.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "book")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private  Long id;
    private String title;
    private String isbn;
    private String publisher;

    @ManyToMany
    @JoinTable(name = "author_book", joinColumns = @JoinColumn(name = "book_id"), inverseJoinColumns = @JoinColumn(name = "author_id"))
    private Set<Author> authors = new HashSet<>();

    public Book() {
    }

    public Book(String title, String isbn, String publisher) {
        this.title = title;
        this.isbn = isbn;
        this.publisher = publisher;
    }

    public Book(String title, String isbn, String publisher, Set<Author> authors) {
        this.title = title;
        this.isbn = isbn;
        this.publisher = publisher;
        this.authors = authors;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public Set<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(Set<Author> authors) {
        this.authors = authors;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(id, book.id) &&
                Objects.equals(title, book.title) &&
                Objects.equals(isbn, book.isbn) &&
                Objects.equals(publisher, book.publisher) &&
                Objects.equals(authors, book.authors);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", isbn='" + isbn + '\'' +
                ", publisher='" + publisher + '\'' +
                ", authors=" + authors +
                '}';
    }
}

```

#### One-To-One Relationship
The book and book_publisher tables have a one-to-one relationship via book.publisher_id and publisher.id.

##### Define JPA Entities

model/Book.java

```java
package com.github.spring5webapp.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "book")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private  Long id;
    private String title;
    private String isbn;

    @OneToOne
    @JoinColumn(name="publisher_id")
    private Publisher publisher;

    @ManyToMany
    @JoinTable(name = "author_book", joinColumns = @JoinColumn(name = "book_id"), inverseJoinColumns = @JoinColumn(name = "author_id"))
    private Set<Author> authors = new HashSet<>();

    public Book() {
    }

    public Book(String title, String isbn, Publisher publisher) {
        this.title = title;
        this.isbn = isbn;
        this.publisher = publisher;
    }

    public Book(String title, String isbn, Publisher publisher, Set<Author> authors) {
        this.title = title;
        this.isbn = isbn;
        this.publisher = publisher;
        this.authors = authors;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public Publisher getPublisher() {
        return publisher;
    }

    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }

    public Set<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(Set<Author> authors) {
        this.authors = authors;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(id, book.id) &&
                Objects.equals(title, book.title) &&
                Objects.equals(isbn, book.isbn) &&
                Objects.equals(publisher, book.publisher) &&
                Objects.equals(authors, book.authors);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", isbn='" + isbn + '\'' +
                ", publisher='" + publisher + '\'' +
                ", authors=" + authors +
                '}';
    }
}

```

model/Publisher.java

```java
package com.github.spring5webapp.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "publisher")
public class Publisher {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private  Long id;
    private String name;
    private String address;

    @OneToOne(mappedBy = "publisher")
    private Book book;

    // default constructor
    public Publisher() {
    }

    public Publisher(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Publisher publisher = (Publisher) o;
        return Objects.equals(id, publisher.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Publisher{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", book=" + book +
                '}';
    }
}

```

##### Annotations
`@Table` maps the entity with the table. If no @Table is defined, the default value is used: the class name of the entity.  
`@Id` declares the identifier property of the entity.  
`@Column` maps the entity's field with the table's column. If @Column is omitted, the default value is used: the field name of the entity.  
`@OneToOne` defines a one-to-one relationship with another entity.  
`@ManyToMany` defines many-to-many relationship with another entity.  
`@JoinColumn` indicates the entity is the owner of the relationship: the corresponding table has a column with a foreign key to the referenced table. mappedBy indicates the entity is the inverse of the relationship.

### Spring data JPA 
- Spring data repositories provides an implementation of the repository pattern
- A repository has methods for retreiving domain objects should delegate to a specialized Repository object
- Spring Data JPA uses Hibernate for persistence to supported RDBS sytems
- Repository is an `interface` which `extends CrudRepository<ModelClass, IDType>`. It can be empty.

#### Gradle configuration

```gradle
// build.graddle
dependencies {
    implementation('org.springframework.boot:spring-boot-starter-data-jpa')
}
```

#### Repository class

repositories/AuthorRepository.java

```java
package com.github.spring5webapp.repositories;

import com.github.spring5webapp.model.Author;
import org.springframework.data.repository.CrudRepository;

public interface AuthorRepository extends CrudRepository<Author, Long> {

}
```

## Spring MVC

- Model, View, Controller
- Design Patter for GUI and web apps
- Client sends request to a controller. 
- Controller decides how to get the model and view.

### Controllers

- Annotate Controller Class with `@Controller`. This will register the class as a Spring Bean and as a Controller in Spring MVC
- To map methods to http request paths use `@RequestMapping`

BookController.java

```java
package com.github.spring5webapp.controllers;

import com.github.spring5webapp.repositories.BookRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class BookController {

    private BookRepository bookRepository;

    public BookController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @RequestMapping("/books")
    public String getBooks(Model model) {
        model.addAttribute("books", bookRepository.findAll());

		// template name
        return "books";
    }
}
```

### Templates - Thymeleaf

Java template engine.

resources/templates/books.html

```html
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8"/>
    <title>Spring Framework</title>
</head>
<body>
<h1>Book List</h1>

<table>
    <tr>
        <th>ID</th>
        <th>Title</th>
        <th>Publisher</th>
    </tr>
    <tr th:each="book : ${books}">
        <td th:text="${book.id}">123</td>
        <td th:text="${book.title}">Spring in Action</td>
        <td th:text="${book.publisher.name}">Wrox</td>
    </tr>
</table>

</body>
</html>
```

xmlns:th="http://www.thymeleaf.org"