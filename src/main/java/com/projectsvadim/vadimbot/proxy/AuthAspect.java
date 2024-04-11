package com.projectsvadim.vadimbot.proxy;

import com.projectsvadim.vadimbot.enity.User.Action;
import com.projectsvadim.vadimbot.enity.User.Role;
import com.projectsvadim.vadimbot.enity.User.User;
import com.projectsvadim.vadimbot.repository.UserRepo;
import com.projectsvadim.vadimbot.service.manager.auth.AuthManager;
import com.projectsvadim.vadimbot.telegram.Bot;
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
@Aspect
@Component
@Order(100)// чтобы исправить ошибку, в случае если пользователь который совершает какое-то действие еще нет в базе
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthAspect {
    final UserRepo userRepo;
    final AuthManager authManager;
    @Autowired
    public AuthAspect(UserRepo userRepo, AuthManager authManager) {
        this.userRepo = userRepo;
        this.authManager = authManager;
    }
    @Pointcut("execution(* com.projectsvadim.vadimbot.service.UpdateDispatcher.distribute(..))")
    public void distributeMethodPointCut(){
    }
    @Around("distributeMethodPointCut()")
    public Object authMethodAdvice(ProceedingJoinPoint joinPoint)
            throws Throwable{
        Update update = (Update) joinPoint.getArgs()[0];
        User user;
        if (update.hasMessage()) {
            user = userRepo.findById(update.getMessage().getChatId()).orElseThrow();
        } else if (update.hasCallbackQuery()){
            user = userRepo.findById(update.getCallbackQuery().getMessage().getChatId()).orElseThrow();
        } else {
            return joinPoint.proceed();
        }
        if (user.getRole() != Role.EMPTY){
            return joinPoint.proceed();
        }
        if (user.getAction() == Action.AUTH){
            return joinPoint.proceed();
        }
        return authManager.answerMessage(update.getMessage(),
                (Bot) joinPoint.getArgs()[1]);
    }
}
