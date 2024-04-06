package com.example.shopapp.component;

import com.example.shopapp.utils.WebUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;
import java.util.Locale;

@RequiredArgsConstructor
@Component
public class LocalizationUtil {
    private final MessageSource messageSource;
    private final LocaleResolver localeResolver;
    public String getLocalizedMessage(String messageKey)
    {
        Locale locale = localeResolver.resolveLocale(WebUtils.getCurrentRequest());
        return messageSource.getMessage(messageKey,null,locale);
    }
}
