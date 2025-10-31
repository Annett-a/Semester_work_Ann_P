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
import ru.itis.repository.ChatMessageRepository;
import ru.itis.repository.impl.ChatMessageRepositoryJdbc;
import ru.itis.service.RoomChatService;
import ru.itis.service.impl.JdbcRoomChatService;

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

        // Services
        AuthService authService = new AuthServiceImpl(userRepo, authValidator);
        ListingService listingService = new ListingServiceImpl(listingRepo, listingValidator, tagRepo);
        PhotoService photoService = new PhotoServiceImpl(photoRepo, listingRepo);

        // Chat (listingId)
        ChatMessageRepository chatRepo = new ChatMessageRepositoryJdbc();
        RoomChatService roomChatService = new JdbcRoomChatService(chatRepo, 50);

        ctx.setAttribute("userRepo", userRepo);
        ctx.setAttribute("listingRepo", listingRepo);
        ctx.setAttribute("tagRepo", tagRepo);
        ctx.setAttribute("photoRepo", photoRepo);
        ctx.setAttribute("authService", authService);
        ctx.setAttribute("listingService", listingService);
        ctx.setAttribute("photoService", photoService);
        ctx.setAttribute("chatService", roomChatService);
    }
}
