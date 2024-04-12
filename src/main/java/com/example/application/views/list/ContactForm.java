package com.example.application.views.list;

import com.example.application.data.Company;
import com.example.application.data.Contact;
import com.example.application.data.Status;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.shared.Registration;

import java.util.List;

public class ContactForm extends FormLayout {

    public void setContact(Contact contact) {
        binder.setBean(contact); //6.1
    }

    TextField firstName = new TextField("Имя");
    TextField lastName = new TextField("Фамилия");
    EmailField email = new EmailField("Эл.почта");
    ComboBox<Status> status = new ComboBox<>("Статус");
    ComboBox<Company> company = new ComboBox<>("Компания");

    Button save = new Button("Сохранить");
    Button delete = new Button("Удалить");
    Button close = new Button("Отмена");

    // Other fields omitted
    BeanValidationBinder<Contact> binder = new BeanValidationBinder<>(Contact.class); //5.1

    public ContactForm(List<Company> companies, List<Status> statuses) {
        addClassName("contact-form");
        binder.bindInstanceFields(this); //5.2

        company.setItems(companies);
        company.setItemLabelGenerator(Company::getName);
        status.setItems(statuses);
        status.setItemLabelGenerator(Status::getName);

        add(firstName,
                lastName,
                email,
                company,
                status,
                createButtonsLayout());
    }

    private Component createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> validateAndSave()); // (1)
        delete.addClickListener(event -> fireEvent(new DeleteEvent(this, binder.getBean()))); // (2)
        close.addClickListener(event -> fireEvent(new CloseEvent(this))); // (3)

        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid())); // (4)
        return new HorizontalLayout(save, delete, close);
    }

    private void validateAndSave() {
        if(binder.isValid()) {
            fireEvent(new SaveEvent(this, binder.getBean())); // (5)
        }
    }

    // Events
    public static abstract class ContactFormEvent extends ComponentEvent<ContactForm> {
        private Contact contact;

        protected ContactFormEvent(ContactForm source, Contact contact) { //7.1.
            super(source, false);
            this.contact = contact;
        }

        public Contact getContact() {
            return contact;
        }
    }

    public static class SaveEvent extends ContactFormEvent {
        SaveEvent(ContactForm source, Contact contact) {
            super(source, contact);
        }
    }

    public static class DeleteEvent extends ContactFormEvent {
        DeleteEvent(ContactForm source, Contact contact) {
            super(source, contact);
        }

    }

    public static class CloseEvent extends ContactFormEvent {
        CloseEvent(ContactForm source) {
            super(source, null);
        }
    }

    public Registration addDeleteListener(ComponentEventListener<DeleteEvent> listener) { //7.2.
        return addListener(DeleteEvent.class, listener);
    }

    public Registration addSaveListener(ComponentEventListener<SaveEvent> listener) {
        return addListener(SaveEvent.class, listener);
    }
    public Registration addCloseListener(ComponentEventListener<CloseEvent> listener) {
        return addListener(CloseEvent.class, listener);
    }


}
/*
5.1. BeanValidationBinder это Binder тот, который знает об аннотациях проверки
bean-компонентов. Передавая его в Contact.class, вы определяете тип объекта,
к которому вы привязываетесь.
5.2.bindInstanceFields()сопоставляет поля по их именам Contact и ContactForm на
их основе.

С помощью этих двух строк кода вы подготовили поля пользовательского интерфейса для подключения к контакту, что является следующим шагом.

6.1.Вызовы binder.setBean()для привязки значений из контакта к полям
пользовательского интерфейса. Этот метод также добавляет прослушиватели
изменения значений для обновления изменений в пользовательском интерфейсе
обратно в объект домена.

7.1. ContactFormEvent — общий суперкласс для всех событий. Он содержит то
contact, что было отредактировано или удалено.
7.2.Методы add*Listener(), передающие правильно типизированный тип события в шину
 событий Vaadin для регистрации пользовательских типов событий.
 Выберите com.vaadin импорт, Registration если IntelliJ спросит.
*/