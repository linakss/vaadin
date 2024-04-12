package com.example.application;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The entry point of the Spring Boot application.
 *
 * Use the @PWA annotation make the application installable on phones, tablets
 * and some desktop browsers.
 *
 */
@SpringBootApplication
@Theme(value = "flowcrmtutorial")
@PWA( //16.1
        name = "Vaadin CRM", //16.2
        shortName = "CRM", //16.3
        offlinePath="offline.html",
        offlineResources = { "./images/offline.webp"} //17.1
)
public class Application implements AppShellConfigurator {


    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
/*
16.1 Аннотация @PWA сообщает Vaadin создать ServiceWorker файл манифеста.
16.2 name — полное имя приложения для файла манифеста.
16.3 shortName должен быть достаточно коротким, чтобы поместиться под значком при установке, и не должен превышать 12 символов.

17.1 offlineResources — это список файлов, которые Vaadin делает доступными в автономном режиме через расширение ServiceWorker.
 */
