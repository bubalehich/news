package ru.clevertec.auth.mapper;

import org.mapstruct.Mapper;
import ru.clevertec.auth.entity.User;
import ru.clevertec.auth.model.UserViewModel;

@Mapper(config = MappersConfig.class)
public interface UserMapper {
    UserViewModel userToUserViewModel(User comment);
}
