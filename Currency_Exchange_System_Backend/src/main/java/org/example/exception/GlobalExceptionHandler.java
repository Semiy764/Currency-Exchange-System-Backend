package org.example.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 404 - resource looked up by id/username/etc. does not exist
    @ExceptionHandler({ResourceNotFoundException.class, EntityNotFoundException.class})
    public ResponseEntity<Map<String, Object>> handleNotFound(RuntimeException e) {
        return build(HttpStatus.NOT_FOUND, e.getMessage());
    }

    // 409 - a unique field (username, phone, national id, ...) already exists
    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<Map<String, Object>> handleDuplicate(DuplicateResourceException e) {
        return build(HttpStatus.CONFLICT, e.getMessage());
    }

    // 403 - authenticated, but not allowed to touch this specific resource
    // (custom, domain-level check - e.g. a customer trying to cancel someone
    // else's transaction; NOT the same class as Spring Security's own
    // AccessDeniedException, which is handled separately below)
    @ExceptionHandler(org.example.exception.AccessDeniedException.class)
    public ResponseEntity<Map<String, Object>> handleDomainAccessDenied(org.example.exception.AccessDeniedException e) {
        return build(HttpStatus.FORBIDDEN, e.getMessage());
    }

    // 403 - Spring Security's own access-denied (e.g. thrown by hasRole(...)
    // matchers or @PreAuthorize). Kept here too so the JSON shape is
    // consistent no matter which AccessDeniedException fired.
    @ExceptionHandler(org.springframework.security.access.AccessDeniedException.class)
    public ResponseEntity<Map<String, Object>> handleSecurityAccessDenied(
            org.springframework.security.access.AccessDeniedException e) {
        return build(HttpStatus.FORBIDDEN, "Access denied");
    }

    // 409 - business-rule violation: not enough vault balance to withdraw/sell
    @ExceptionHandler(InsufficientBalanceException.class)
    public ResponseEntity<Map<String, Object>> handleInsufficientBalance(InsufficientBalanceException e) {
        return build(HttpStatus.CONFLICT, e.getMessage());
    }

    // 400 - bad/missing input caught by manual validation in services or controllers
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleBadRequest(IllegalArgumentException e) {
        return build(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    // 400 - malformed JSON body / wrong field types that Jackson itself rejects
    @ExceptionHandler(org.springframework.http.converter.HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleUnreadableBody(Exception e) {
        return build(HttpStatus.BAD_REQUEST, "Malformed request body");
    }

    // 400 - a @PathVariable could not be converted to the expected type
    // (e.g. GET /api/vault/balances/low/abc where a BigDecimal was expected)
    @ExceptionHandler(org.springframework.web.method.annotation.MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, Object>> handleTypeMismatch(
            org.springframework.web.method.annotation.MethodArgumentTypeMismatchException e) {
        return build(HttpStatus.BAD_REQUEST,
                "Invalid value for parameter '" + e.getName() + "'");
    }

    // 500 - last-resort safety net so nothing leaks a raw stack trace to the client.
    // Keep this LAST / most generic - Spring picks the most specific handler
    // that matches, so this only fires when nothing above did.
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleUnexpected(Exception e) {
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected server error");
    }

    private ResponseEntity<Map<String, Object>> build(HttpStatus status, String message) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("message", message);
        body.put("status", status.value());
        return ResponseEntity.status(status).body(body);
    }
}