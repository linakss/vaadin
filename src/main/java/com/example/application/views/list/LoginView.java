package com.example.application.views.list;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@Route("login") //13.1
@PageTitle("Login | Vaadin CRM")
@AnonymousAllowed
public class LoginView extends VerticalLayout implements BeforeEnterObserver {

    private final LoginForm login = new LoginForm(); //13.2

    public LoginView(){
        addClassName("login-view");
        setSizeFull(); //13.3
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        login.setAction("login"); //13.4

        add(new H1("Vaadin CRM"), login);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        // inform the user about an authentication error
        if(beforeEnterEvent.getLocation() //13.5
                .getQueryParameters()
                .getParameters()
                .containsKey("error")) {
            login.setError(true);
        }
    }
}

/*
13.1 Сопоставьте вид с "login" путем. LoginView должен охватывать все окно браузера, поэтому не используйте его MainLayout в качестве родительского.
13.2 Создайте экземпляр LoginForm компонента для захвата имени пользователя и пароля.
13.3 Увеличьте LoginView размер и отцентрируйте его содержимое — как по горизонтали, так и по вертикали — с помощью вызовов setAlignItems(`Alignment.CENTER)` и setJustifyContentMode(`JustifyContentMode.CENTER)`.
13.4 Установите LoginForm действие для "login" публикации формы входа в Spring Security.
13.5 Прочитайте параметры запроса и отобразите ошибку, если попытка входа в систему не удалась.
 */