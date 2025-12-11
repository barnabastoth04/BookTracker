package com.booktracker.mappers;

import com.booktracker.dtos.BookDto;
import com.booktracker.model.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class BookMapper {
    @Mapping(target = "id", source = "bookId")
    public abstract BookDto entityToDto(Book book);

    @Mapping(target = "bookId", source = "id")
    public abstract Book dtoToEntity(BookDto bookDto);
}
