package com.inspire.common.contract.autoconfigure;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inspire.common.contract.handler.GlobalControllerAdvice;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.context.MessageSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 */
@AutoConfiguration(after = {JacksonAutoConfiguration.class, MessageSourceAutoConfiguration.class})
@ConditionalOnBean({ObjectMapper.class})
public class ContractAutoConfiguration {

    /**
     *
     * @param objectMapper
     * @return
     */
    @Bean
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    @ConditionalOnMissingBean(GlobalControllerAdvice.class)
    public GlobalControllerAdvice globalControllerAdvice(ObjectMapper objectMapper) {
        return new GlobalControllerAdvice(objectMapper);
    }

    /**
     *
     * @return
     */
    @Bean
    @ConditionalOnClass(OpenAPI.class)
    public OperationCustomizer operationCustomizer() {
        return (operation, handlerMethod) -> {
            operation.getResponses().forEach((status, apiResponse) -> {

                if(isBodyLessStatus(Integer.parseInt(status))) {
                    return;
                }

                Content newContent = new Content();
                Content oldContent = apiResponse.getContent();
                if(oldContent == null || oldContent.isEmpty()) {
                    Schema<?> wrapperSchema = new Schema<>();
                    wrapperSchema.addProperty("success", new Schema<>().type("boolean").example(true));
                    wrapperSchema.addProperty("status", new Schema<>().type("integer").example(Integer.valueOf(status)));
                    wrapperSchema.addProperty("timestamp", new Schema<>().type("string").format("date-time").example(LocalDateTime.now().toString()));
                    newContent.addMediaType("application/json", new MediaType().schema(wrapperSchema));
                    apiResponse.setContent(newContent);
                    return;
                }

                apiResponse.getContent().forEach((mediaTypeName, mediaType) -> {
                    Schema<?> originalSchema = mediaType.getSchema();
                    Schema<?> wrapperSchema = new Schema<>();
                    wrapperSchema.addProperty("success", new Schema<>().type("boolean").example(true));
                    wrapperSchema.addProperty("status", new Schema<>().type("integer").example(Integer.valueOf(status)));
                    wrapperSchema.addProperty("data", originalSchema);
                    wrapperSchema.addProperty("timestamp", new Schema<>().type("string").format("date-time").example(LocalDateTime.now().toString()));

                    if ("text/plain".equals(mediaTypeName)) {
                        newContent.addMediaType("application/json", new MediaType().schema(wrapperSchema));
                    } else {
                        mediaType.setSchema(wrapperSchema);
                        newContent.addMediaType(mediaTypeName, mediaType);
                    }
                });
                apiResponse.setContent(newContent);
            });

            return operation;
        };
    }

    private boolean isBodyLessStatus(int status) {
        return status / 100 == 1 || status / 100 == 3 || status == 204;
    }
}
