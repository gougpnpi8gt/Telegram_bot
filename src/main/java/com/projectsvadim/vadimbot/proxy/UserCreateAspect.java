package com.projectsvadim.vadimbot.proxy;

import com.projectsvadim.vadimbot.enity.User.Action;
import com.projectsvadim.vadimbot.enity.User.Role;
import com.projectsvadim.vadimbot.enity.User.UserDetails;
import com.projectsvadim.vadimbot.repository.DetailsRepo;
import com.projectsvadim.vadimbot.repository.UserRepo;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import java.time.LocalDateTime;
@Aspect
@Order(10)
@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreateAspect {
    final UserRepo userRepo;
    final DetailsRepo detailsRepo;
    @Autowired
    public UserCreateAspect(UserRepo userRepo, DetailsRepo detailsRepo) {
        this.userRepo = userRepo;
        this.detailsRepo = detailsRepo;
    }

    @Pointcut("execution(* com.projectsvadim.vadimbot.service.UpdateDispatcher.distribute(..))")
    public void distributeMethodPointCut(){

    }

    @Around("distributeMethodPointCut()")
    public Object distributeMethodAdvice(ProceedingJoinPoint joinPoint) throws Throwable{
        Update update = (Update) joinPoint.getArgs()[0];
        User telegramUser;
        if (update.hasMessage()) {
            telegramUser = update.getMessage().getFrom();
            // getFrom - из сообщения получить пользователя
        } else if (update.hasCallbackQuery()){
            telegramUser = update.getCallbackQuery().getFrom();
        } else {
            return joinPoint.proceed();
            // мы позволяем методу distribute выполниться
        }
        if (userRepo.existsById(telegramUser.getId())){
            return joinPoint.proceed();
        }
        UserDetails details = UserDetails.builder()
                .firstName(telegramUser.getFirstName())
                .username(telegramUser.getUserName())
                .lastName(telegramUser.getLastName())
                .registeredAt(LocalDateTime.now())
                .build();
        detailsRepo.save(details);
        com.projectsvadim.vadimbot.enity.User.User newUser =
                com.projectsvadim.vadimbot.enity.User.User.builder()
                .chatId(telegramUser.getId())
                .action(Action.FREE)
                .role(Role.EMPTY)
                .details(details)
                .build();
        userRepo.save(newUser);
        return joinPoint.proceed();
    }
}
