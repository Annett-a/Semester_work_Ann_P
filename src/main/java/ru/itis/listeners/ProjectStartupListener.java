package ru.itis.listeners;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import ru.itis.repository.*;
import ru.itis.repository.impl.*;
import ru.itis.service.*;
import ru.itis.service.impl.*;
import ru.itis.validation.AuthDataValidationService;
import ru.itis.validation.ListingValidationService;

@WebListener
public class ProjectStartupListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        var ctx = sce.getServletContext();

        // DAO
        UserRepository userRepo = new UserRepositoryJdbcCrud();
        ListingRepository listingRepo = new ListingRepositoryJdbcCrud();
        TagRepository tagRepo = new JdbcTagRepository();
        PhotoRepository photoRepo = new PhotoRepositoryJdbc();

        // Validators
        AuthDataValidationService authValidator = new RegexpAuthDataValidationServiceImpl();
        ListingValidationService listingValidator = new ListingValidationServiceImpl();

        // Services (бизнес-логика)
        AuthService authService = new AuthServiceImpl(userRepo, authValidator);
        ListingService listingService = new ListingServiceImpl(listingRepo, listingValidator, tagRepo);
        PhotoService photoService = new PhotoServiceImpl(photoRepo, listingRepo);

        // NEW: чат по комнатам (listingId)
        RoomChatService roomChatService = new InMemoryRoomChatService(50);

        // put into context
        ctx.setAttribute("userRepo", userRepo);
        ctx.setAttribute("listingRepo", listingRepo);
        ctx.setAttribute("tagRepo", tagRepo);
        ctx.setAttribute("photoRepo", photoRepo);

        ctx.setAttribute("authService", authService);
        ctx.setAttribute("listingService", listingService);
        ctx.setAttribute("photoService", photoService);

        // NEW
        ctx.setAttribute("chatService", roomChatService);
    }
}
