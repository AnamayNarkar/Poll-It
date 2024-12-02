package com.implementation.PollingApp.util;

import java.util.Optional;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ApiResponse<T> {
        private Optional<T> data;
        private String message;

        public ApiResponse(T data, String message) {
                this.data = Optional.ofNullable(data);
                this.message = message;
        }
}
