package integration.builder;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;
import ru.clevertec.news.model.comment.CommentMutationModel;

@AllArgsConstructor
@NoArgsConstructor(staticName = "aComment")
@With
public class CommentMutationModelTestBuilder {
    public CommentMutationModel build() {
        return CommentMutationModel.builder()
                .text("First one")
                .username("qwerty2007")
                .build();
    }
}
