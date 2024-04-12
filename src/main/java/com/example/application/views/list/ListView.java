package com.example.application.views.list;

import com.example.application.data.Contact;
import com.example.application.services.CrmService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

@PermitAll //@PermitAll аннотации к обоим представлениям, чтобы все вошедшие в систему пользователи могли получить к ним доступ.
@Route(value = "", layout = MainLayout.class) //11.1
@PageTitle("Contacts | Vaadin CRM")
public class ListView extends VerticalLayout {
    Grid<Contact> grid = new Grid<>(Contact.class);
    TextField filterText = new TextField();
    ContactForm form; //1.1
    CrmService service;

    public ListView(CrmService service) { //4.1
        this.service = service;
        addClassName("list-view");
        setSizeFull();
        configureGrid();
        configureForm(); //1.2.

        add(getToolbar(), getContent()); //1.3.
        updateList();
        closeEditor(); //8.1
    }

    private HorizontalLayout getContent() {
        HorizontalLayout content = new HorizontalLayout(grid, form);
        content.setFlexGrow(2, grid); //1.4.
        content.setFlexGrow(1, form);
        content.addClassNames("content");
        content.setSizeFull();
        return content;
    }

    private void configureForm() {
        //form = new ContactForm(Collections.emptyList(), Collections.emptyList()); //1.5.
        form = new ContactForm(service.findAllCompanies(), service.findAllStatuses()); //4.3
        form.setWidth("25em");
        form.addSaveListener(this::saveContact); //9.1.
        form.addDeleteListener(this::deleteContact); //9.2.
        form.addCloseListener(e -> closeEditor()); //9.3.
    }

    private void saveContact(ContactForm.SaveEvent event) {
        service.saveContact(event.getContact());
        updateList();
        closeEditor();
    }

    private void deleteContact(ContactForm.DeleteEvent event) {
        service.deleteContact(event.getContact());
        updateList();
        closeEditor();
    }

    private void configureGrid() {
        grid.addClassNames("contact-grid"); //10
        grid.setSizeFull();
        grid.setColumns("firstName", "lastName", "email"); //5
        grid.addColumn(contact -> contact.getStatus().getName()).setHeader("Статус"); //6
        grid.addColumn(contact -> contact.getCompany().getName()).setHeader("Компания");
        grid.getColumns().forEach(col -> col.setAutoWidth(true)); //7

        grid.asSingleSelect().addValueChangeListener(event -> editContact(event.getValue())); //8.2
    }

    private Component getToolbar() {
        filterText.setPlaceholder("Фильтрация по имени...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY); //8
        filterText.addValueChangeListener(e -> updateList()); //4.4

        Button addContactButton = new Button("Найти контакт");
        addContactButton.addClickListener(click -> addContact()); //8.3

        var toolbar = new HorizontalLayout(filterText, addContactButton); //9
        toolbar.addClassName("toolbar"); //10
        return toolbar;
    }

    public void editContact(Contact contact) { //8.4
        if (contact == null) {
            closeEditor();
        } else {
            form.setContact(contact);
            form.setVisible(true);
            addClassName("editing");
        }
    }

    private void closeEditor(){
        form.setContact(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    private void addContact(){ //8.5
        grid.asSingleSelect().clear();
        editContact(new Contact());
    }


    private void updateList() { //4.5
        grid.setItems(service.findAllContacts(filterText.getValue()));
    }
}
/*
1. Представление расширяет VerticalLayout, которое размещает все дочерние компоненты вертикально.
2. Компонент Grid набран с помощью Contact.
3. Конфигурация сетки извлекается в отдельный метод, чтобы конструктор было легче читать.
4. Добавьте панель инструментов и сетку в файл VerticalLayout.
5. Определите, какие свойства Contact сетки должны отображаться.
6. Определите пользовательские столбцы для вложенных объектов.
7. Настройте столбцы для автоматической регулировки их размера в соответствии с содержимым.
8. Настройте поле поиска для запуска событий изменения значения только тогда, когда пользователь перестает вводить текст. Таким образом, вы избегаете ненужных вызовов базы данных, но прослушиватель по-прежнему запускается, а пользователь не покидает фокус с поля.
9. На панели инструментов используется HorizontalLayout для размещения TextField и Button рядом друг с другом.
10. Добавление некоторых имен классов к компонентам упрощает стилизацию приложения в дальнейшем с помощью CSS.

1.1. Создает ссылку на форму, чтобы у вас был доступ к ней из других методов.
1.2. Создайте метод инициализации формы.
1.3.Измените add()метод вызова getContent(). Метод возвращает объект HorizontalLayout, который обертывает форму и сетку, показывая их рядом друг с другом.
1.4.Используйте setFlexGrow(), чтобы указать, что сетка должна занимать вдвое больше места, чем форма.
1.5. Инициализируйте форму с пустыми списками компаний и статусов: вы добавите их в следующей части.

4.1. Автосвязывание CrmService через конструктор. Сохраните его в поле, чтобы иметь к нему доступ другими способами.
4.2.Позвоните updateList(), как только построите представление.
4.3. Используйте сервис для получения компаний и статусов.
4.4.Звоните updateList()в любое время, когда изменится фильтр.
4.5.updateList()устанавливает элементы сетки, вызывая службу со значением из текстового поля фильтра.

8.1.Вызов closeEditor()в конце конструктора: устанавливает для контакта формы значение null, удаляя старые значения; скрывает форму; и удаляет "editing"класс CSS из представления.
8.2.addValueChangeListener()добавляет прослушиватель в сетку. Компонент Grid поддерживает режимы множественного и однократного выбора. Вам нужно выбрать только один Contact, чтобы вы могли использовать этот asSingleSelect()метод. Метод getValue()возвращает значение Contact в выбранной строке или значение null, если выбор отсутствует.
8.3.Звонок addContact(), когда пользователь нажимает кнопку «Добавить контакт».
8.4.editContact()устанавливает выбранный контакт в ContactForm и скрывает или показывает форму, в зависимости от выбора. Он также устанавливает "editing"имя класса CSS при редактировании.
8.5.addContact()очищает выбор сетки и создает новый Contact.

9.1. Прослушиватель событий сохранения вызывает saveContact(). Он использует contactService для сохранения контакта в событии в базу данных, обновляет список и закрывает редактор.
9.2. Прослушиватель событий удаления вызывает deleteContact(). В процессе он также использует contactService удаление контакта из базы данных, обновляет список и закрывает редактор.
9.3. Прослушиватель событий закрытия закрывает редактор.

11.1 ListView по - прежнему соответствует пустому пути, но теперь используется MainLayout в качестве родительского.
*/
