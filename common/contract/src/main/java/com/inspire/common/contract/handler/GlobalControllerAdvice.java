package com.inspire.common.contract.handler;

import com.inspire.common.contract.exception.CAErrorCode;
import com.inspire.common.core.dto.ApiResponse;
import com.inspire.common.core.dto.ValidException;
import com.inspire.common.core.exception.BaseException;
import com.inspire.common.core.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.http.*;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.core.MethodParameter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.method.ParameterErrors;
import org.springframework.validation.method.ParameterValidationResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * {@code RestControllerAdvice} for integrating all responses from different microservices.
 *
 * <p>
 * Provides automatic wrapping of controller responses into {@link ApiResponse}
 * and centralized exception handling. It only works on servlet-based applications.
 *
 * <hr>
 * <p>
 * 마이크로서비스 간 공통 응답을 위한 {@code RestControllerAdvice}.
 *
 * <p>
 * 컨트롤러의 응답을 {@link ApiResponse}로 래핑하며 예외 처리를 담당합니다.
 * 서블릿 기반 애플리케이션에서만 작동합니다.
 *
 * <hr>
 * @author Wooseong Urm
 * @see RestControllerAdvice
 * @since 1.0.0
 */
@Slf4j
@RestControllerAdvice
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class GlobalControllerAdvice extends ResponseEntityExceptionHandler implements ResponseBodyAdvice<Object> {

    private final ObjectMapper objectMapper;

    /**
     * Excludes HEAD, OPTIONS request <hr>
     * <p>
     * HEAD, OPTIONS 요청은 제외
     */
    private static final Set<HttpMethod> EXCLUDED_METHODS = Set.of(HttpMethod.HEAD, HttpMethod.OPTIONS);

    public GlobalControllerAdvice(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }


    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ApiResponse<?>> handleBusinessException(BaseException e) {

        ErrorCode errorCode = e.getErrorCode();
        int status = errorCode.getStatus();

        return ResponseEntity.status(status).body(ApiResponse.ofError(errorCode));
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleOtherExceptions(Exception e) {

        CAErrorCode errorCode = CAErrorCode.INTERNAL_SERVER_ERROR;

        return ResponseEntity.status(500).body(ApiResponse.ofError(500, errorCode.getCode(), errorCode.getMessage()));
    }

    @Override
    protected @Nullable ResponseEntity<Object> handleHandlerMethodValidationException(HandlerMethodValidationException ex, @NonNull HttpHeaders headers, @NonNull HttpStatusCode status, @NonNull WebRequest request) {

        // status.value() == 400 -> input validation error
        // status.value() == 500 -> validation error on a return value
        CAErrorCode errorCode = status.value() == 400 ? CAErrorCode.INVALID_REQUEST : CAErrorCode.INTERNAL_SERVER_VALIDATION_ERROR;
        List<ValidException> details = new ArrayList<>();

        ex.visitResults(new HandlerMethodValidationException.Visitor() {

            @Override
            public void cookieValue(CookieValue cookieValue, ParameterValidationResult result) {
                String candidate = cookieValue.name();
                String key = !candidate.isBlank() ? candidate : result.getMethodParameter().getParameterName();

                add(details, key, result);

            }

            @Override
            public void matrixVariable(MatrixVariable matrixVariable, ParameterValidationResult result) {
                String candidate = matrixVariable.name();
                String key = !candidate.isBlank() ? candidate : result.getMethodParameter().getParameterName();

                add(details, key, result);
            }

            @Override
            public void modelAttribute(@Nullable ModelAttribute modelAttribute, ParameterErrors errors) {
                add(details, errors);
            }

            @Override
            public void pathVariable(PathVariable pathVariable, ParameterValidationResult result) {
                String candidate = pathVariable.name();
                String key = !candidate.isBlank() ? candidate : result.getMethodParameter().getParameterName();

                add(details, key, result);
            }

            @Override
            public void requestBody(RequestBody requestBody, ParameterErrors errors) {
                add(details, errors);
            }

            @Override
            public void requestBodyValidationResult(RequestBody requestBody, ParameterValidationResult result) {
                String key = result.getMethodParameter().getParameterName();
                add(details, key, result);
            }

            @Override
            public void requestHeader(RequestHeader requestHeader, ParameterValidationResult result) {
                String candidate = requestHeader.name();
                String key = !candidate.isBlank() ? candidate : result.getMethodParameter().getParameterName();

                add(details, key, result);
            }

            @Override
            public void requestParam(@Nullable RequestParam requestParam, ParameterValidationResult result) {
                String candidate = requestParam != null ? requestParam.name() : "";
                String key = !candidate.isBlank() ? candidate : result.getMethodParameter().getParameterName();

                add(details, key, result);
            }

            @Override
            public void requestPart(RequestPart requestPart, ParameterErrors errors) {
                add(details, errors);
            }

            @Override
            public void other(ParameterValidationResult result) {
                String key = result.getMethodParameter().getParameterName();

                add(details, key, result);
            }
        });


        return ResponseEntity.status(status).body(ApiResponse.ofError(status.value(), errorCode.getCode(), errorCode.getMessage(), details));
    }


    @Override
    protected @Nullable ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, @NonNull HttpHeaders headers, @NonNull HttpStatusCode status, @NonNull WebRequest request) {

        CAErrorCode errorCode = CAErrorCode.INVALID_REQUEST;

        List<ValidException> details = ex.getBindingResult().getAllErrors().stream().map(error -> new ValidException(resolveKey(error), error.getDefaultMessage())).toList();

        return ResponseEntity.status(status).body(ApiResponse.ofError(status.value(), errorCode.getCode(), errorCode.getMessage(), details));
    }

    @Override
    protected ResponseEntity<Object> createResponseEntity(@Nullable Object body, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {

        String code = CAErrorCode.OTHER_MVC_EXCEPTION.getCode();
        String message;

        if (body instanceof ProblemDetail pd) {
            message = pd.getDetail();
        } else {
            message = body != null ? body.toString() : null;
        }
        return ResponseEntity.status(statusCode).body(ApiResponse.ofError(statusCode.value(), code, message));
    }

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {

        log.debug("supports method called");
        return !returnType.getContainingClass().getPackageName().startsWith("org.springdoc");
    }

    @Nullable
    @Override
    public Object beforeBodyWrite(@Nullable Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        log.debug("beforeBodyWrite executed");
        log.debug("content: {}", selectedContentType);
        log.debug("converter: {}", selectedConverterType);
        log.debug("body: {}", body instanceof byte[] ? new String((byte[]) body, StandardCharsets.UTF_8) : null);

        Class<?> innerType = body != null ? body.getClass() : Object.class;
        log.debug("innerType: {}", innerType);

        // 이미 ApiResponse면 그대로
        if (body instanceof ApiResponse) {
            return body;
        }

        // HEAD, OPTIONS의 경우 그대로
        HttpMethod httpMethod = request.getMethod();
        if (EXCLUDED_METHODS.contains(httpMethod)) {
            return body;
        }

        // Check Content-Type if needed

        // Servlet Response만 처리
        if (!(response instanceof ServletServerHttpResponse ssr)) {
            return body;
        }

        int status = ssr.getServletResponse().getStatus();

        // status가 1xx, 204, 3xx의 경우 넘김
        if (isBodyLessStatus(status)) {
            return body;
        }

        ApiResponse<?> apiResponse = ApiResponse.ofSuccess(status, body);

        if (StringHttpMessageConverter.class.isAssignableFrom(selectedConverterType)) {
            try {
                response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
                return objectMapper.writeValueAsString(apiResponse);
            } catch (JacksonException e) {
                throw new RuntimeException(e);
            }
        }

        return apiResponse;
    }

    private boolean isBodyLessStatus(int status) {
        return status / 100 == 1 || status / 100 == 3 || status == 204;
    }

    private void add(List<ValidException> details, String key, ParameterValidationResult result) {
        result.getResolvableErrors().forEach(error -> details.add(new ValidException(key, error.getDefaultMessage())));
    }

    private void add(List<ValidException> details, ParameterErrors errors) {
        errors.getAllErrors().forEach(error -> details.add(new ValidException(resolveKey(error), error.getDefaultMessage())));
    }

    private String resolveKey(ObjectError error) {
        return error instanceof FieldError fe ? fe.getField() : error.getObjectName();
    }
}
