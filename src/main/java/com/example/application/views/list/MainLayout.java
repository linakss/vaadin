package com.example.application.views.list;

import com.example.application.security.SecurityService;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.theme.lumo.LumoUtility;

public class MainLayout extends AppLayout {//10.1.
    private final SecurityService securityService;
    public MainLayout(SecurityService securityService) { //15.1
        this.securityService = securityService;
        createHeader();
        createDrawer();
    }

    private void createHeader() {
        H1 logo = new H1("Vaadin CRM");
        logo.addClassNames(
                LumoUtility.FontSize.LARGE, //10.2.
                LumoUtility.Margin.MEDIUM);

        String u = securityService.getAuthenticatedUser().getUsername();
        Button logout = new Button("Log out " + u, e -> securityService.logout()); //15.2

        var header = new HorizontalLayout(new DrawerToggle(), logo, logout ); //10.3. //15.3

        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER); //10.4.
        header.expand(logo); //15.4
        header.setWidthFull();
        header.addClassNames(
                LumoUtility.Padding.Vertical.NONE,
                LumoUtility.Padding.Horizontal.MEDIUM);

        addToNavbar(header); //10.5.

    }

    private void createDrawer() {
        addToDrawer(new VerticalLayout( //10.6.
                new RouterLink("List", ListView.class), //10.7.
                new RouterLink("Dashboard", DashboardView.class)
        ));
    }
}

/*
10.1. AppLayout — это макет Vaadin с заголовком и адаптивным ящиком.
10.2. Вместо стилизации текста с помощью необработанного CSS используйте служебные классы Lumo, поставляемые с темой по умолчанию
10.3. DrawerToggle — это кнопка меню, которая переключает видимость боковой панели.
10.4. Центрирует компоненты вдоль header вертикальной оси.
10.5. Добавляет header макет в панель навигации макета приложения — раздел в верхней части экрана.
10.6. Обертывает ссылку маршрутизатора в VerticalLayout и добавляет ее в AppLayout ящик.
10.7. Создает объект RouterLink с текстом «Список» и ListView.class представлением назначения.

15.1 Выполните автоматическое подключение SecurityService и сохраните его в поле.
15.2 Создайте кнопку выхода из системы, которая вызывает logout() метод в службе.
15.3 Добавьте кнопку в макет заголовка.
15.4 Вызовите header.expand(logo), чтобы логотип занял все дополнительное пространство макета. Это может переместить кнопку выхода из системы в крайнее правое положение.
 */