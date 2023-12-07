package protocol.commons.dto;

import lombok.Builder;
import lombok.NonNull;

@Builder
public record DeleteUser(@NonNull Long registroSender, @NonNull Boolean isSenderAdmin,
                         @NonNull Long registroToDelete) {
}
