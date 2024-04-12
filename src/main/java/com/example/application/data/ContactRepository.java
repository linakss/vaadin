package com.example.application.data;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

    public interface ContactRepository extends JpaRepository<Contact, Long> {

        @Query("select c from Contact c " +
                "where lower(c.firstName) like lower(concat('%', :searchTerm, '%')) " +
                "or lower(c.lastName) like lower(concat('%', :searchTerm, '%'))") //3.1
        List<Contact> search(@Param("searchTerm") String searchTerm); //3.2
    }

/*В этом примере аннотация используется @Queryдля определения пользовательского запроса
(см. аннотацию 1). В этом случае он проверяет, соответствует ли строка имени или фамилии,
и игнорирует регистр. В запросе используется язык запросов Java Persistence (JPQL)
[https://en.wikipedia.org/wiki/Java_Persistence_Query_Language], который
представляет собой SQL-подобный язык для запросов к базам данных, управляемым JPA.
Вам не нужно реализовывать метод. Spring Data предоставляет реализацию на основе запроса.*/
