package server.dto;

import lombok.Builder;
import lombok.NonNull;

@Builder
public record DeleteUser(@NonNull Integer registroSender, @NonNull Boolean isSenderAdmin,
                         @NonNull Integer registroToDelete,
                         String email, String senha) {
}
